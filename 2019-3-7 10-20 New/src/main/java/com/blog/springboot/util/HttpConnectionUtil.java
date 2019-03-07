package com.blog.springboot.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
 
/**
 * Javaԭ����API�����ڷ���HTTP���󣬼�java.net.URL��java.net.URLConnection����ЩAPI�ܺ��á��ܳ��ã�
 * ��������㣻
 * 
 * 1.ͨ��ͳһ��Դ��λ����java.net.URL����ȡ��������java.net.URLConnection�� 2.��������Ĳ��� 3.��������
 * 4.������������ʽ��ȡ�������� 5.�ر�������
 * 
 * @author LZH
 *
 */
public class HttpConnectionUtil {
 
 
    /**
     * 
     * @param urlPath
     *            ����·��
     * @param downloadDir
     *            ���ش��Ŀ¼
     * @return ���������ļ�·��
     */
    @SuppressWarnings("finally")
	public static String downloadFile(String urlPath, String downloadDir) {
        File file = null;
        String path=null;
        try {
            // ͳһ��Դ
            URL url = new URL(urlPath);
            // ������ĸ��࣬������
            URLConnection urlConnection = url.openConnection();
            // http��������
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // �趨����ķ�����Ĭ����GET
            httpURLConnection.setRequestMethod("POST");
            // �����ַ�����
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // �򿪵��� URL ���õ���Դ��ͨ�����ӣ������δ�������������ӣ���
            httpURLConnection.connect();
 
            // �ļ���С
            int fileLength = httpURLConnection.getContentLength();
 
            // �ļ���
            String filePathUrl = httpURLConnection.getURL().getFile();
            String fileFullName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separatorChar) + 1);
 
//            System.out.println("file length---->" + fileLength);
 
            URLConnection con = url.openConnection();
 
            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());
 
             path = downloadDir + File.separatorChar + fileFullName;
            
            file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
                // ��ӡ���ذٷֱ�
                // System.out.println("������-------> " + len * 100 / fileLength +
                // "%\n");
            }
            bin.close();
            out.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return path;
        }
 
    }
 
}