package com.nie.diytomcat.http;

import cn.hutool.core.util.StrUtil;
import com.nie.diytomcat.util.MiniBrowser;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author 3137596614@qq.com
 * @version 1.0
 * @date 2020/7/20 18:47
 **/
public class Request {
	/**
	 * 创造Request类用来封装http请求内容
	 */

	private String requestString;
	private String requestUri;
	private Socket socket;

	public Request(Socket socket) throws IOException {
		this.socket=socket;
		parseHttpRequest();
		if (StrUtil.isEmpty(requestString)){
			return;
		}
		parseUri();
	}
	/**
	 *对URL进行解析
	 */

	private void parseHttpRequest() throws IOException {
		InputStream inputStream=this.socket.getInputStream();
		byte[] bytes= MiniBrowser.readBytes(inputStream);
		requestString=new String(bytes, StandardCharsets.UTF_8);
	}
	/**
	 * 对URI的解析
	 */

	private void parseUri(){
		String temp;
		temp= StrUtil.subBetween(requestString, " "," ");
		if (!StrUtil.contains(temp,'?')){
			requestUri=temp;
			return;
		}
		temp=StrUtil.subBefore(temp,'?',false);
		requestUri=temp;
	}
	public String getRequestString() {
		return requestString;
	}

	public String getRequestUri() {
		return requestUri;
	}
}
