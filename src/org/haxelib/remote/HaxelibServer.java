package org.haxelib.remote;

import com.sun.tracing.dtrace.DependencyClass;
import org.haxe.net.HaxeRemoteProxy;
import org.haxelib.core.HaxelibProtocol;
import org.haxelib.model.HaxelibDependency;
import org.haxelib.model.HaxelibEntity;
import org.haxelib.model.HaxelibVersion;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by akalanitski on 18.08.2017.
 */
public class HaxelibServer {

	private HaxeRemoteProxy server;
	private String host;
	private String apiURL;
	private String fileURL;
	private HaxelibProtocol protocol;

	public HaxelibServer(String host, String port, String dir, String apiVersion, String url) {
		this.host = "http://" + host + ":" + port + "/";
		this.apiURL =  dir + "api/" + apiVersion + "/" + url;
		this.fileURL = dir + "files/" + apiVersion + "/";
		protocol = new HaxelibProtocol();
		server = new HaxeRemoteProxy(this.host + this.apiURL);
	}

	public HaxelibDependency[] search(String filter) {
		Object result = null;
		try {
			result = server.call(new String[]{"api", "search"}, new Object[]{filter});
		} catch (Exception ex) {
			System.out.println(ex.fillInStackTrace());
		}

		if (result != null && result instanceof ArrayList) {
			ArrayList list = (ArrayList) result;
			HaxelibDependency[] a = new HaxelibDependency[list.size()];
			int i = 0;
			for (Object obj : list) {
				HashMap<String, Object> h = (HashMap<String, Object>) obj;
				HaxelibDependency d = new HaxelibDependency();
				d.id = (Integer) h.get("id");
				d.name = (String) h.get("name");
				a[i++] = d;
			}
			return a;
		}
		return null;
	}

	public HaxelibEntity get(String libraryName) {
		Object result = null;
		try {
			result = server.call(new String[]{"api", "infos"}, new Object[]{libraryName});
		} catch (Exception ex) {
			System.out.print(ex.fillInStackTrace());
		}
		return createEntity((HashMap<String, Object>) result);
	}

	public InputStream download(String libraryName, String version) throws IOException {
		String fileName = protocol.libraryArchiveName(libraryName, version);
		URL url = new URL(host + fileURL + fileName);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		InputStream in = connection.getInputStream();
		return in;
	}

	private HaxelibEntity createEntity(HashMap<String, Object> hm) {
		HaxelibEntity entity = null;
		if (hm != null) {
			entity = new HaxelibEntity();
			entity.owner = (String) hm.get("owner");
			entity.lastVersion = (String) hm.get("curversion");
			entity.license = (String) hm.get("license");
			entity.url = (String) hm.get("website");
			entity.versions = createVersions((HashMap[]) hm.get("versions"));
			entity.downloads = (int) hm.get("downloads");
			entity.name = (String) hm.get("name");
			entity.description = (String) hm.get("desc");
			entity.tags = (ArrayList<String>) hm.get("tags");
			return entity;
		}
		return entity;
	}

	private HaxelibVersion[] createVersions(HashMap[] sourceArray) {
		int n = sourceArray.length;
		HaxelibVersion[] result = new HaxelibVersion[n];
		for (int i = 0; i < n; i++) {
			HashMap<String, Object> source = (HashMap<String, Object>) sourceArray[i];
			result[i] = createVersion(source);;
		}
		return result;
	}

	private HaxelibVersion createVersion(HashMap<String, Object> source) {
		HaxelibVersion result = null;
		if (source != null) {
			result = new HaxelibVersion();
			result.name = (String) source.get("name");
			result.comment = (String) source.get("comments");
			result.downloads = (int) source.get("downloads");
			result.dateString = (String) source.get("date");
		}
		return result;
	}
}
