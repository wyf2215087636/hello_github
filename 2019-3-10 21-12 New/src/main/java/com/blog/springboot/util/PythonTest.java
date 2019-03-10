package com.blog.springboot.util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.text.html.HTML;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.python.antlr.PythonParser.return_stmt_return;

public class PythonTest {
	

	public static void main(String[] args){
		//����һ��http����
		/*HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet("https://api.mlwei.com/music/api/wy/?key=523077333&cache=0&type=url&id=32507038");
		
		try {
			HttpResponse response = httpClient.execute(httpGet,httpContext);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//��ȡ�ض���֮���������Ϣ
		HttpHost targetHost = (HttpHost) httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
		//��ȡʵ�ʵ���������URI
		HttpUriRequest realRequest = (HttpUriRequest) httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
		
		System.out.println("host:====================="+targetHost);
		System.out.println("��ʵ��ַ:====================="+realRequest.getURI());
		System.out.println(targetHost+""+realRequest.getURI());*/
		
		PythonTest test = new PythonTest();
		test.start_download("https://api.mlwei.com/music/api/wy/?key=523077333&cache=0&type=url&id=32507038","��Ա");
		System.out.println("���سɹ�~~~~~~~~~~~~~~~~~~!!!!!!!!!!!!!!!!!!");
			
			
		
		
		 
		
		//���ض�����===ʧ��
		/*String downloadFile = HttpConnectionUtil.downloadFile("http://music.163.com/song/media/outer/url?id=1313354324", "D:\\");
		System.out.println("���ط��ص�ַ:"+downloadFile);
		try {
			//�ȴ�����
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
	}
	
	
	public int start_download(String url,String mp3_name) {
		//mp3Url MP3��URL
				try {
					InputStream inputStream = new URL(url(url)).openConnection().getInputStream();
					FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\22150\\Desktop\\"+mp3_name+".mp3");//�����ļ������
					byte[] bb = new byte[1024];//���ջ���
					int len;
					while((len = inputStream.read(bb))>0) {//����
						fileOutputStream.write(bb,0,len);//д���ļ�
					}
					fileOutputStream.close();
					inputStream.close();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//�������ӡ�������
				return 1;
	}
	
	
	
	public String url(String url) {
		//����һ��http����
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext httpContext = new BasicHttpContext();
				HttpGet httpGet = new HttpGet(url);
				
				try {
					HttpResponse response = httpClient.execute(httpGet,httpContext);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//��ȡ�ض���֮���������Ϣ
				HttpHost targetHost = (HttpHost) httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
				//��ȡʵ�ʵ���������URI
				HttpUriRequest realRequest = (HttpUriRequest) httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
				
				System.out.println("host:====================="+targetHost);
				System.out.println("��ʵ��ַ:====================="+realRequest.getURI());
				System.out.println(targetHost+""+realRequest.getURI());
				
				return targetHost+""+realRequest.getURI();
	}
	
	
}
