package com.blog.springboot.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Huoqu {
	public static void main(String[] args) {
		
		try {
			String url = "http://www.javaniu.com/302.htm";
			System.out.println("���ʵ�ַ:"+url);
			URL serverUrl;
			serverUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
			connection.setRequestMethod("GET");
			//��������false��������Զ�redirect��Location�ĵ�ַ
			connection.setInstanceFollowRedirects(false);
			connection.addRequestProperty("Accept", "UTF-8");
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
			connection.addRequestProperty("Referer", "http://javaniu.com/");
			connection.connect();
			String location = connection.getHeaderField("Location");
			
			serverUrl = new URL(location);
			connection = (HttpURLConnection)serverUrl.openConnection();
			connection.setRequestMethod("GET");
			
			connection.addRequestProperty("Accept-Charset", "UTF-8;");  
			connection.addRequestProperty("User-Agent",  
                    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");  
			connection.addRequestProperty("Referer", "http://zuidaima.com/");  
			connection.connect();  
			System.out.println("��ת��ַ:"+location);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
