package com.nie.diytomcat.util;

import cn.hutool.system.SystemUtil;

import java.io.File;

/**
 * @author 3137596614@qq.com
 * @version 1.0
 * @date 2020/7/20 17:52
 **/
public class Constant {
	public final static String RESPONSE_HEAD_202 = "HTTP/1.1 200 OK\r\n" + "Content-Type:{}\r\n\r\n";
	public final static File WEBAPP_FOLDER = new File(SystemUtil.get("user.dir", "webapps"));
	public final static File ROOT_FOLDER =new File(WEBAPP_FOLDER,"Root");
}
