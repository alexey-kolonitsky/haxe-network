package main.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by akalanitski on 03.08.2017.
 */
public class HaxeRemoteProxy {

	private String strUrl;
	private URL _url;
	private HttpURLConnection _connection;
	private HaxeSerializer serializer;
	private HaxeDeserializer deserializer;

	public HaxeRemoteProxy(String url) {
		strUrl = url;
	}

	public Object call(String[] path, Object[] params) throws Exception {
		_url = new URL(strUrl);

		serializer = new HaxeSerializer();
		serializer.serialize(path);
		serializer.serialize(params);
		_connection = (HttpURLConnection) _url.openConnection();
		_connection.setRequestMethod("POST");
		_connection.setRequestProperty("X-Haxe-Remoting", "1");
		_connection.setDoOutput(true);

		OutputStream os = null;
		os = _connection.getOutputStream();
		String postParams = "__x=" + serializer.toString();
		os.write(postParams.getBytes());
		os.flush();
		os.close();

		int responseCode = _connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(_connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			String str = response.toString();
			str = str.substring(3);
			deserializer = new HaxeDeserializer(new StringBuffer(str));
			Object result = deserializer.deserialize();
			return result;
		}
		return null;
	}
}
