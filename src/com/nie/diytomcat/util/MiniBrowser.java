package com.nie.diytomcat.util;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 3137596614@qq.com
 * @version 1.0
 * @date 2020/7/14 13:31
 **/
public class MiniBrowser {
	/**
	 * 一个小型的浏览器方便查看http响应
	 */
	public static void main(String[] args) throws Exception {
		String url = "https://www.baidu.com/?tn=88093251_33_hao_pg";
		String contentString = getContentString(url, false);
		System.out.println(contentString);
		String httpString = getHttpString(url, false);
		System.out.println(httpString);

	}

	public static byte[] getContentBytes(String url) {
		return getContentBytes(url, false);
	}

	public static String getContentString(String url) {
		return getContentString(url, false);
	}

	public static byte[] getContentBytes(String url, boolean gzip) {
		byte[] bytes = getHttpBytes(url, gzip);
		byte[] doubleReturn = "\r\n\r\n".getBytes();
		int pos = -1;
		for (int i = 0; i < bytes.length - doubleReturn.length; i++) {
			byte[] temp = Arrays.copyOfRange(bytes, i, i + doubleReturn.length);
			if (Arrays.equals(doubleReturn, temp)) {
				pos = i;
				break;
			}
		}
		if (-1 == pos) {
			return null;
		}
		pos += doubleReturn.length;
		return Arrays.copyOfRange(bytes, pos, bytes.length);
	}

	public static String getHttpString(String url, boolean gzip) {
		byte[] bytes = getHttpBytes(url, gzip);
		return new String(bytes).trim();
	}

	public static String getHttpString(String url) {
		return getHttpString(url, false);
	}

	public static String getContentString(String url, boolean gzip) {
		byte[] bytes = getContentBytes(url, gzip);
		if (null == bytes) {
			return null;
		}
		return new String(bytes, StandardCharsets.UTF_8).trim();
	}

	public static byte[] getHttpBytes(String url, boolean gzip) {
		byte[] result = null;
		try {
			URL u = new URL(url);
			Socket client = new Socket();
			int port = u.getPort();
			if (-1 == port) {
				port = 80;
			}
			InetSocketAddress inetSocketAddress = new InetSocketAddress(u.getHost(), port);
			client.connect(inetSocketAddress, 1000);
			Map<String, String> requestHeaders = new HashMap<>();

			requestHeaders.put("Host", u.getHost() + ":" + port);
			requestHeaders.put("Accept", "text/html");
			requestHeaders.put("Connection", "close");
			requestHeaders.put("User-Agent", "NieXuDong");

			if (gzip) {
				requestHeaders.put("Accept-Encoding", "gzip");
			}

			String path = u.getPath();
			if (path.length() == 0) {
				path = "/";
			}

			String firstLine = "GET " + path + " HTTP/1.1\r\n";

			StringBuffer httpRequestString = new StringBuffer();
			httpRequestString.append(firstLine);
			Set<String> headers = requestHeaders.keySet();
			for (String header : headers) {
				String headerLine = header + ":" + requestHeaders.get(header) + "\r\n";
				httpRequestString.append(headerLine);
			}

			PrintWriter pWriter = new PrintWriter(client.getOutputStream(), true);
			pWriter.println(httpRequestString);
			InputStream is = client.getInputStream();
			result = readBytes(is);
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = e.toString().getBytes(StandardCharsets.UTF_8);
		}

		return result;
	}

	/**
	 * MiniBrowser读取浏览器的信息采用循环读取，每次都1024个字节并抽取成方法
	 */
	public static byte[] readBytes(InputStream inputStream) throws IOException {
		//每次读取的字节数
		int buffer = 1024;
		byte[] bytes = new byte[buffer];
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		while (true) {
			int length = inputStream.read(bytes);
			if (-1 == length) {
				break;
			}
			byteArrayOutputStream.write(bytes, 0, length);
			if (length != buffer) {
				break;
			}
		}
		return byteArrayOutputStream.toByteArray();
	}
}
