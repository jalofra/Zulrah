package elfZulrah;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HDU {
	private static final int BUFFER_SIZE = 4096;
	public static boolean unauthorized = false;

	public static void dCA(String fU, String saveDir) throws IOException {
		URL url = new URL(fU);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			String fileName = "";
			String disposition = httpConn.getHeaderField("Content-Disposition");
			//String contentType = httpConn.getContentType();
			//int contentLength = httpConn.getContentLength();

			if (disposition != null) {
				int index = disposition.indexOf("filename=");
				if (index > 0) {
					fileName = disposition.substring(index + 10, disposition.length()); // -1
				}
			} else {
				fileName = fU.substring(fU.lastIndexOf("/") + 1, fU.length());
			}

			InputStream inputStream = httpConn.getInputStream();
			String saveFilePath = saveDir + File.separator + fileName;
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);

			int bytesRead = -1;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.close();
			inputStream.close();

		} else {
			unauthorized = true;
		}
		httpConn.disconnect();
	}
}