package com.nie.diytomcat;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.nie.diytomcat.http.Request;
import com.nie.diytomcat.http.Response;
import com.nie.diytomcat.util.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author 3137596614@qq.com
 * @version 1.0
 * @date 2020/7/13 21:34
 **/
public class BootStrap {
	public static void main(String[] args) {
		try{
			/*端口号*/
			int port=18080;
			/*检查端口是否被占用如果被占用则抛出信息到控制台*/
			if (!NetUtil.isUsableLocalPort(port)){
				System.out.println("18080端口被占用");
				return;
			}
			/*通过Socket通信*/
			ServerSocket serverSocket=new ServerSocket(port);
			/*打开输入输出流循环接收数据大小为1024字节*/
			while(true){
				Socket socket=serverSocket.accept();
				Request request=new Request(socket);
				System.out.println("浏览器的输入信息:"+request.getRequestString());
				System.out.println("浏览器的URI:"+request.getRequestUri());
				Response response=new Response();
				String string="服务端响应";
				response.getPrintWriter().println(string);
				handler200(socket, response);
				socket.close();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void handler200(Socket socket,Response response) throws IOException {
		//得到响应内容
		String contentType=response.getContentType();
		//得到响应消息头
		String header200= Constant.RESPONSE_HEAD_202;
		//连接成完整的http响应
		header200= StrUtil.format(header200, contentType);
		byte[] content=response.getBody();
		byte[] header=header200.getBytes();
		//转换成字节,拼接字节http响应
		byte[] headerContent=new byte[content.length+header.length];
		ArrayUtil.copy(header, 0, headerContent,0,header.length);
		ArrayUtil.copy(content, 0, headerContent, header.length,content.length );
		OutputStream outputStream=socket.getOutputStream();
		outputStream.write(headerContent);
		socket.close();
	}
}
