package com.blog.springboot.miusc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {
	public String httpRequestUtils(String url,String params) {
		PrintWriter out = null;
		BufferedReader in = null;
		StringBuilder result = new StringBuilder();
		try {
			URL reqUrl = new URL(url);
			//��������
			URLConnection conn = reqUrl.openConnection();
			
			//��������ͷ
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		//  conn.setRequestProperty("Connection","Keep-Alive");//���ֳ�����
			conn.setDoOutput(true);//����true�ſ���ʹ��conn.getOutputStream().write()
			conn.setDoInput(true);//�ſ���ʹ��conn.getInputStream().read();
			
			//д�����
			out = new PrintWriter(conn.getOutputStream());
			//���ò���������ֱ��д&������Ҳ����ֱ�Ӵ���ƴ�Ӻõ�
			out.println(params);
			//flush������Ļ���
			out.flush();
			
			//����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {//ʹ��finally�����ر������������
			try {
				if(out != null) {
					out.close();
				}
				if(in != null) {
					in.close();
				}
			} catch (IOException ex) {
				// TODO: handle exception
			}
			
			
		}
		
		return result.toString();
		
	}
	
	
	//����api��ȡ���ص�json���н���
	public List<String> jsonIsOk(String url){
		String httpurl = "https://api.mlwei.com/music/api/?key=523077333&id="+url+"&type=so&cache=0&size=hq&nu=1";
		String result = httpRequestUtils(httpurl, "");
		System.out.println(result);
		
		JSONObject jsonObject = JSONObject.fromObject(result);
		//ͨ��getString("")�ֱ�ȡ���������Ϣ
		String arraydata = jsonObject.getString("Body");
		System.out.println(arraydata);
		
		
		//��jsonArray�ַ���ת��ΪJSONArray
		JSONArray jsonArray = JSONArray.fromObject(arraydata);
		System.out.println(jsonArray.size());
		//ȡ�������һ��Ԫ��
		JSONObject jSongs = JSONObject.fromObject(jsonArray.get(0));
		//ȡ����һ��Ԫ�ص���Ϣ������ת��ΪJSONObject
		System.out.println(jSongs.getString("author"));
		System.out.println(jSongs.getString("url"));
		
		List<String> list = new ArrayList<String>();
		list.add(jSongs.getString("author"));
		list.add(jSongs.getString("url"));
		
		return list;
	}
	
	public static void main(String[] args) throws IOException {
		Test test = new Test();
		List<String> list = test.jsonIsOk("%E6%88%92%E7%83%9F");
		System.out.println(list.get(0));
		System.out.println(list.get(1));
		String url = list.get(1);
		
		
	}
}
