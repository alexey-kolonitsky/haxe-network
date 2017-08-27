package org.haxelib.core;

import org.haxelib.HaxelibConstants;
import org.haxelib.core.data.CurrentVersion;
import org.haxelib.core.data.HaxelibException;
import org.haxelib.model.HaxelibEntity;
import org.haxelib.model.HaxelibVersion;
import org.haxelib.utils.KFile;
import org.haxelib.utils.KPath;
import org.haxelib.utils.KString;

import javax.json.*;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by akalanitski on 18.08.2017.
 */
public class HaxelibRepository {

	/** Used haxelib protocol */
	private HaxelibProtocol protocol;


	//---------------------------------
	// repository
	//---------------------------------

	private File _repository;

	/**
	 * Directory with unpacked libraries.
	 */
	public File getRepository() {
		return _repository;
	}
	public void setRepository(File value) throws HaxelibException, IOException {
		if (value.exists()) {
			if (value.isDirectory())
				_repository = value;
			else if (value.isFile())
				throw HaxelibException.ImposibleRepositoryDestination(value.getAbsolutePath());
		} else {
			value.mkdir();
		}
		_repository = value;
	}


	//---------------------------------
	// libraries
	//---------------------------------


	private ArrayList<HaxelibEntity> _libs;

	/**
	 * List of HaxelibEntities for each installed library.
	 */
	public ArrayList<HaxelibEntity> list() {
		return _libs;
	}


	//---------------------------------
	// Constructor
	//---------------------------------

	/**
	 * Create haxelib repository basend on repositoryPath
	 * @param repositoryPath
	 */
	public HaxelibRepository(String repositoryPath) {
		_repository = new File(repositoryPath);
		if (!_repository.exists()) {
			System.out.println("Haxelib directory doesn't exists");
		}
		if (_repository.isFile()) {
			System.out.println("Haxelib should be not Directory");
		}
		protocol = new HaxelibProtocol();
	}

	public void scanRepository() throws HaxelibException, IOException {
		if (_libs == null) {
			_libs = new ArrayList<HaxelibEntity>();
			String[] libraryNames = _repository.list();
			for (String libraryName : libraryNames) {
				add(libraryName);
			}
		}
	}

	//--------------------------------------------------------------------------
	// Library API
	//--------------------------------------------------------------------------

	public HaxelibEntity get(String libraryName) throws HaxelibException {
		if (!protocol.isLibraryName(libraryName))
			throw HaxelibException.InvalidLibraryName(libraryName);
		for (HaxelibEntity lib : _libs) {
			if (lib.name.equals(libraryName)) {
				return lib;
			}
		}
		return null;
	}

	public void add(String libraryName) throws HaxelibException, IOException {
		if (!protocol.isLibraryName(libraryName))
			throw HaxelibException.InvalidLibraryName(libraryName);

		HaxelibEntity lib = createLibrary(libraryName);
		lib.versions = scanLibraryHome(lib.homeDirectory);
		try {
			lib.currentVersion = getCurrentVersion(libraryName, lib);
		} catch (HaxelibException exception) {
			lib.addError(exception);
		} finally {
			_libs.add(lib);
		}
	}

	public boolean has(String libraryName) throws HaxelibException {
		if (!protocol.isLibraryName(libraryName))
			throw HaxelibException.InvalidLibraryName(libraryName);
		HaxelibEntity lib = get(libraryName);
		return lib != null;
	}

	public void remove(String libraryName) throws HaxelibException {
		if (!protocol.isLibraryName(libraryName))
			throw HaxelibException.InvalidLibraryName(libraryName);
		HaxelibEntity lib = get(libraryName);
		if (lib == null)
			throw HaxelibException.LibraryNotInstalled(libraryName);

		KFile directory = new KFile(lib.homeDirectoryPath);
		directory.delete();
		_libs.remove(lib);
	}

	//--------------------------------------------------------------------------
	// Version API
	//--------------------------------------------------------------------------

	public CurrentVersion getCurrentVersion(String libraryName, HaxelibEntity entity) throws HaxelibException, IOException {
		if (!protocol.isLibraryName(libraryName))
			throw HaxelibException.InvalidLibraryName(libraryName);

		CurrentVersion currentVersion = new CurrentVersion();
		String content = null;
		KPath path = new KPath(_repository.getAbsolutePath(), libraryName, HaxelibConstants.LIBRARY_CURRENT_VERSION_FILE_NAME);
		KFile versionFile = new KFile(path.toString());
		if (versionFile.exists()) {
			content = versionFile.getContent();
			if (protocol.isVersion(content)) {
				currentVersion.version = content;
				currentVersion.kind = HaxelibEntity.HAXELIB_TYPE_REMOTE;
				currentVersion.path = createLibraryPath(libraryName, content);
			}
			else {
				currentVersion.version = content;
				currentVersion.kind = HaxelibEntity.HAXELIB_TYPE_VCS;
				currentVersion.path = createLibraryPath(libraryName, content);
			}
		}

		path = new KPath(_repository.getAbsolutePath(), libraryName, HaxelibConstants.LIBRARY_DEVELOPMENT_VERSION_FILE_NAME);
		versionFile = new KFile(path.toString());
		if (versionFile.exists()) {
			if (KString.isNotEmpty(currentVersion.version)) {
				throwExceptionIfNoEntity(entity, HaxelibException.VersionConflict(libraryName));
			}
			content = versionFile.getContent();
			if (KString.isNotEmpty(content)) {
				currentVersion.version = "dev";
				currentVersion.kind = HaxelibEntity.HAXELIB_TYPE_DEVELOPMENT;
				currentVersion.path = content;
			}
		}
		return currentVersion;
	}

	public void setVersion(String libraryName, String version) throws HaxelibException, IOException{
		HaxelibEntity lib = get(libraryName);
		if (lib == null)
			throw HaxelibException.LibraryNotInstalled(libraryName);
		KPath versionFilePath = new KPath(lib.homeDirectoryPath, HaxelibConstants.LIBRARY_CURRENT_VERSION_FILE_NAME);
		KFile file = new KFile(versionFilePath.toString());
		file.setContent(version);
		CurrentVersion currentVersion = new CurrentVersion();
		currentVersion.version = version;
		currentVersion.path = createLibraryPath(libraryName, version);
		currentVersion.kind = HaxelibEntity.HAXELIB_TYPE_REMOTE;
		lib.currentVersion = currentVersion;
	}

	public void remoteVersion(String libraryName, String version) throws HaxelibException {
		HaxelibEntity lib = get(libraryName);
		if (lib == null)
			throw HaxelibException.LibraryNotInstalled(libraryName);
		if (!lib.hasVersion(version))
			throw HaxelibException.VersionNotInstalled(libraryName, version);
		String libraryPath = createLibraryPath(libraryName, version);
		KFile directory = new KFile(libraryPath);
		directory.delete();
	}

	public String createLibraryPath(String libraryName, String version) {
		KPath path = new KPath(_repository.getAbsolutePath(), libraryName, protocol.encodeFilename(version));
		return path.toString();
	}


	//--------------------------------------------------------------------------
	// Private
	//--------------------------------------------------------------------------

	private void throwExceptionIfNoEntity(HaxelibEntity entity, HaxelibException exception) throws HaxelibException {
		if (entity == null)
			throw exception;
		entity.addError(exception);
	}

	private HaxelibEntity createLibrary(String libraryName) throws IOException {
		HaxelibEntity result = new HaxelibEntity();
		result.name = libraryName;
		result.homeDirectoryPath = new KPath(_repository.getAbsolutePath(), libraryName).toString();
		result.homeDirectory = new File(result.homeDirectoryPath);
		return result;
	}

	private HaxelibVersion[] scanLibraryHome(File libHomeDirectory) {
		ArrayList<HaxelibVersion> versions = new ArrayList<HaxelibVersion>();
		File[] versionDirectories = libHomeDirectory.listFiles();
		for (File versionDir : versionDirectories) {
			if (versionDir.isDirectory()) {
				HaxelibVersion version = createVersionFromDir(versionDir);
				versions.add(version);
			}
		}
		int n = versions.size();
		HaxelibVersion[] result = new HaxelibVersion[n];
		for (int i = 0; i < n; i++) {
			result[i] = versions.get(i);
		}
		return result;
	}

	private HaxelibVersion createVersionFromDir(File versionDir) {
		KPath libFilePath = new KPath(versionDir.getPath(), HaxelibConstants.LIBRARY_DESCRIPTION_FILE_NAME);
		HaxelibVersion version = new HaxelibVersion();
		version.name = protocol.decodeFilename(versionDir.getName());
		version.entity = createHaxelibEntityFromJson(new KFile(libFilePath.toString()));
		return version;
	}

	private HaxelibEntity createHaxelibEntityFromJson(KFile jsonLibraryFile) {
		InputStream in = jsonLibraryFile.getContentStream();
		if (in == null) {
			return null;
		}
		JsonReader rdr = Json.createReader(in);
		JsonObject obj = rdr.readObject();
		HaxelibEntity result = new HaxelibEntity();
		result.name = obj.containsKey("name") ? obj.getString("name") : "";
		result.url = obj.containsKey("url") ? obj.getString("url") : "";
		result.license = obj.containsKey("license") ? obj.getString("license") : "";
		result.description = obj.containsKey("description") ? obj.getString("description") : "";
		result.version = obj.containsKey("version") ? obj.getString("version") : "";
		JsonArray tags = obj.containsKey("tags") ? obj.getJsonArray("tags") : null;
		result.tags = new ArrayList<>();
		if (tags != null) {
			for (int i = 0; i < tags.size(); i++) {
				String tag = tags.getString(i);
				result.tags.add(tag);
			}
		}
		JsonArray contributors = obj.getJsonArray("contributors");
		JsonObject dependencies = obj.getJsonObject("dependencies");
		rdr.close();
		return result;
	}
}
