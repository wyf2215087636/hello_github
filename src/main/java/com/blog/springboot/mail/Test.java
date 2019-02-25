package com.blog.springboot.mail;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.blog.springboot.mapper.PostsMapper;
import com.blog.springboot.pojo.SoCode;
import com.blog.springboot.util.Data;
import com.sun.mail.util.MailSSLSocketFactory;



public class Test {

	// �ʼ��༭
	public static MimeMessage createSimpleMail(Session session, String usermail, String yzm) throws Exception {
		// �����ʼ�����
		MimeMessage message = new MimeMessage(session);
		// ָ���ʼ��ķ�����
		message.setFrom(new InternetAddress("feixunsat@163.com"));
		// ָ���ʼ����ռ��ˣ����ڷ����˺��ռ�����һ���ģ��Ǿ����Լ����Լ���
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(usermail));
		// �ʼ��ı���
		message.setSubject("���ۻظ���Ȩ��");
		// �ʼ����ı�����
		message.setContent("����������Ȩ��Ϊ:" + yzm, "text/html;charset=UTF-8");
		// ���ش����õ��ʼ�����
		return message;
	}

	// ��֤�������
	// ����������ֺ���ĸ,
	public static String getStringRandom(int length) {

		String val = "";
		Random random = new Random();

		// ����length����ʾ���ɼ�λ�����
		for (int i = 0; i < length; i++) {

			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// �����ĸ��������
			if ("char".equalsIgnoreCase(charOrNum)) {
				// ����Ǵ�д��ĸ����Сд��ĸ
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	public static void sendEmail(String useremail, String yzm) throws Exception {
		Properties properties = new Properties();
		// ����debug���ԣ��Ա��ڿ���̨�鿴
		properties.setProperty("mail.debug", "true");
		// �����ʼ�������������
		properties.setProperty("mail.host", "smtp.qq.com");
		// ���ͷ�������Ҫ�����֤
		properties.setProperty("mail.smtp.auth", "true");
		// �����ʼ�Э������
		properties.setProperty("mail.transport.protocol", "smtp");

		// ����SSL���ܣ������ʧ��
		
		MailSSLSocketFactory mailSSLSocketFactory = new MailSSLSocketFactory();
		
		mailSSLSocketFactory.setTrustAllHosts(true);
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.ssl.socketFactory", mailSSLSocketFactory);

		// ����session
		Session session = Session.getInstance(properties);
		// ͨ��session�õ�transport����
		Transport transport = session.getTransport();
		// �����ʼ�������:�������ͣ��˺ţ���Ȩ���������(����ȫ)
		transport.connect("smtp.163.com", "feixunsat@163.com", "wyf2801998");
		// ������֤��
		// String yzm = Test.getStringRandom(8);
		// ������ݵ�����
		/*
		 * SoCode soCode = new SoCode(); soCode.setUsermail(useremail);
		 * soCode.setCode(yzm); soCode.setTime(new Date()); //������֤�뵽���ݿ� int num =
		 * postsMapper.save_code(soCode); //�ж��Ƿ�������ݳɹ� if(num==1) {
		 */

		// �����ʼ�createSimpleMail
		Message message = Test.createSimpleMail(session, useremail, yzm);
		// �����ʼ�
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();

		// data.init(1, "�ʼ����ͳɹ�(���δ�յ�������������)");

		/*
		 * }else { data.init(-1, "���ݲ���ʧ��"); }
		 */
	}
}
