package com.blog.springboot.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.table.TableStringConverter;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.reflection.wrapper.BaseWrapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlProcessor.ResolutionMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.blog.springboot.mapper.PostsMapper;
import com.blog.springboot.pojo.NewReply;
import com.blog.springboot.pojo.Posts;
import com.blog.springboot.pojo.Reply;
import com.blog.springboot.pojo.SoCode;
import com.blog.springboot.pojo.User;
import com.blog.springboot.util.Data;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.blog.springboot.mail.Test;

import ch.qos.logback.core.net.LoginAuthenticator;

@Controller
public class UserPostsController {
	@Autowired
	PostsMapper postsMapper;

	@RequestMapping("/userAddPosts")
	public String listPosts(Posts posts, HttpServletRequest request) throws Exception {
		posts.setTime(new Date());
		HttpSession session = request.getSession();
		int user_id = (Integer) session.getAttribute("user_id");
		posts.setUser_id(postsMapper.get_user(user_id).getId());
		postsMapper.insertPosts(posts);
		return "redirect:userListPosts";
	}

	@RequestMapping("/userDeletePosts")
	public String deletePosts(Posts posts) throws Exception {
		postsMapper.delete(posts.getId());
		return "redirect:userListPosts";
	}

	@RequestMapping("/userUpdatePosts")
	public String updatePosts(Posts posts) throws Exception {
		postsMapper.update(posts);
		return "redirect:userListPosts";
	}

	@RequestMapping("/userEditPosts")
	public String listPosts(int id, Model model) throws Exception {
		Posts posts = postsMapper.getPosts(id);
		model.addAttribute("p", posts);
		return "userEditPosts";
	}

	@RequestMapping("/userListPosts")
	public String listPosts(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
		PageHelper.startPage(start, size, "id asc");
		List<Posts> ps = postsMapper.getAllPosts();
		PageInfo<Posts> page = new PageInfo<Posts>(ps);
		model.addAttribute("page", page);
		return "userListPosts";
	}

	@RequestMapping("/userInfo/{id}")
	public String blog_info(@PathVariable(value = "id") int id, Model model, HttpServletRequest request) {
		Posts posts = postsMapper.getPosts(id);
		HttpSession session = request.getSession();
		session.setAttribute("userinfo_id", id);
		model.addAttribute("p", posts);
		return "userinfo";
	}

	@RequestMapping("/userLogin")
	public String userlogin(int id, String password, Model model,
			@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "size", defaultValue = "5") int size, HttpServletRequest request) {
		User user = postsMapper.get_user(id);
		if (user != null) {
			if (user.getPassword().equals(password)) {
				HttpSession session = request.getSession();
				session.setAttribute("name", user.getName());
				session.setAttribute("user_id", user.getId());
				model.addAttribute("username", session.getAttribute("name"));
				System.out.println("===============================");
				System.out.println(session.getAttribute("name"));
			} else {
				model.addAttribute("login_error", "false");
				return "error";
			}
		} else {
			model.addAttribute("login_error", "false");
			return "error";
		}
//		PageHelper.startPage(start,size,"id asc");
//		List<Posts> ps=postsMapper.getAllPosts();
//		PageInfo<Posts> page = new PageInfo<Posts>(ps);
//		model.addAttribute("page",page);
		model.addAttribute("login_success", "true");
		return "success";
	}

	@RequestMapping("/userRegister")
	public String register(String name, String password, String password2, Model model) {
		System.out.println(name);
		System.out.println(password);
		System.out.println(password2);
		if (name != null && password != null && password2 != null && !name.equals("") && !password.equals("")
				&& !password2.equals("")) {
			User user = new User();
			System.out.println("�ҽ�����");
			if (password.equals(password2)) {
				user.setName(name);
				user.setPassword(password);
				int resultNum = postsMapper.insertUser(user);
				System.out.println("=================================");
				System.out.println(resultNum);
				if (resultNum == 1) {
					model.addAttribute("register_success", "true");
					model.addAttribute("register_user", user);
					System.out.println(user.getId());
					return "success";
					// �жϴ��ص�����
				} else {
					model.addAttribute("register_error", "false");
					return "error";
					// ��ת����ҳ��Model��ֵ
				}
			}
		}
		model.addAttribute("register_error", "false");
		return "error";
	}

	@RequestMapping("/insertUserReply")
	public String insertReply(String reply_info, int posts_id, Model model, HttpServletRequest request) {
		System.out.println(posts_id);
		HttpSession session = request.getSession();
		int reply_user_id = (Integer) session.getAttribute("user_id");

		Reply reply = new Reply();

		reply.setPosts_id(posts_id);
		reply.setReply_info(reply_info);
		reply.setTime(new Date());
		reply.setReply_user_id(reply_user_id);
		int resultNum = postsMapper.insertReply(reply);
		if (resultNum == 1) {
			System.out.println("��ӻ����ɹ�");
		} else {
			System.out.println("��ӻ���ʧ��");
		}

		return "forward:userInfo/" + session.getAttribute("userinfo_id");
	}

	@RequestMapping("/index")
	public String index(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
		PageHelper.startPage(start, 5, "id desc");
		List<Posts> ps = postsMapper.getAllPosts();
		PageInfo<Posts> page = new PageInfo<Posts>(ps);

		PageHelper.startPage(start, size, "id desc");
		List<Posts> psForTime = postsMapper.getAllPosts();
		PageInfo<Posts> pageInfo = new PageInfo<Posts>(psForTime);

		PageHelper.startPage(start, size, "popularity desc");
		List<Posts> psForPopularitye = postsMapper.getAllPosts();
		PageInfo<Posts> pagePopularity = new PageInfo<Posts>(psForPopularitye);
		model.addAttribute("pageinfo", pageInfo);
		model.addAttribute("page", page);
		model.addAttribute("pagePopularity", pagePopularity);
		return "index";
	}

	@RequestMapping("/details")
	public String details() {
		return "details";
	}

	@RequestMapping("/details/{id}")
	public String blog_details(@PathVariable(value = "id") int id, Model model, HttpServletRequest request) {
		Posts posts = postsMapper.getPosts(id);
		posts.setPopularity(posts.getPopularity() + 1);
		postsMapper.updateWithPopularity(posts);
		model.addAttribute("p", posts);
		PageHelper.startPage(0, 5, "id desc");
		List<Posts> psForTime = postsMapper.getAllPosts();
		PageInfo<Posts> pageInfo = new PageInfo<Posts>(psForTime);
		model.addAttribute("pageinfo", pageInfo);
		List<NewReply> newReplies = postsMapper.get_newReply(id);
		if (newReplies.size() == 0) {
			System.out.println("����ID:" + posts.getId() + "��������");
		}
		Posts posts_1 = postsMapper.getPosts(posts.getId() - 1);
		Posts posts_2 = postsMapper.getPosts(posts.getId() + 1);
		if (posts_1 == null) {
			model.addAttribute("posts_1", null);
		} else {
			model.addAttribute("posts_1", posts_1);
		}
		if (posts_2 == null) {
			model.addAttribute("posts_2", null);
		} else {
			model.addAttribute("posts_2", posts_2);
		}

		model.addAttribute("newReplies", newReplies);
		return "details";
	}

	@ResponseBody
	@RequestMapping("/details/comment")
	public Data blog_details_comment(String username, String useremail, String reply_info, int posts_id,
			String mail_yzm) {
		NewReply newReply = new NewReply();
		newReply.setUsername(username);
		newReply.setUseremail(useremail);
		newReply.setReply_info(reply_info);
		newReply.setPosts_id(posts_id);
		newReply.setTime(new Date());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (mail_yzm.equals("")) {
			System.out.println("����ʧ��,��Ȩ�벻��Ϊ��");
			return new Data(-1, "����ʧ��,��Ȩ�벻��Ϊ��");
		} else {
			SoCode soCode = postsMapper.get_code(mail_yzm);
			if (soCode == null) {
				System.out.println("��Ȩ�벻����");
				return new Data(-1, "��Ȩ�벻����");
			} else {
				long one = newReply.getTime().getTime();
				long two = soCode.getTime().getTime();
				int minutes = (int) ((one - two) / (1000 * 60));
				System.out.println("����ʱ��ķ��Ӳ�Ϊ:" + minutes);
				if (minutes > 5) {
					System.out.println("��֤ʧ�ܣ���Ȩ����Ч�ڳ�ʱ");
					int num_yzm = postsMapper.delete_code(mail_yzm);
					if (num_yzm == 1) {
						System.out.println("ʧЧ��Ȩ��ɾ���ɹ�");
					} else {
						System.out.println("ʧЧ��Ȩ��ɾ��ʧ��");
					}
					return new Data(-1, "��֤ʧ�ܣ���Ȩ����Ч�ڳ�ʱ");
				} else {
					SoCode soCode2 = postsMapper.selectSoCode(useremail);
					if(postsMapper.selectSoCode(useremail)==null){
						System.out.println("������û�л�ȡ����Ȩ��");
						return new Data(-1,"������û�л�ȡ����Ȩ��");
					}else if (soCode2.getCode().equals(mail_yzm) && soCode2.getUsermail().equals(useremail)) {

						System.out.println("��֤�ɹ�,��Ȩ��:" + mail_yzm);
						// �������
						int num = postsMapper.save_comment(newReply);
						if (num != 0 || num != -1) {
							System.out.println("������ݳɹ�");
							int num_yzm = postsMapper.delete_code(mail_yzm);
							if (num_yzm == 1) {
								System.out.println("ɾ���ɹ�");
							} else {
								System.out.println("ɾ��ʧ��");
							}
						}
					}else{
						System.out.println("��֤ʧ�ܣ� ��鿴����Ȩ���Ƿ��ɸ��������");
						return new Data(-1,"��֤ʧ��,��鿴����Ȩ���Ƿ��ɸ��������");
					}
				}
			}
		}
		return new Data(1, "������ӳɹ�");

	}

	@ResponseBody
	@RequestMapping("/details/mail")
	public Data blog_yzm(String useremail) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(useremail);
		SoCode soCode = postsMapper.selectSoCode(useremail);
		// �жϸ������Ƿ������֤��

		if (soCode == null) {
			System.out.println("============soCode=null");
			// ����������������һ�������ݲ��ҷ����ʼ�
			soCode = new SoCode();
			soCode.setUsermail(useremail);
			soCode.setCode(Test.getStringRandom(8));
			soCode.setTime(new Date());
			postsMapper.save_code(soCode);
			Test.sendEmail(useremail, soCode.getCode());
		} else {
			System.out.println("==================soCode!=null");
			long ago = soCode.getTime().getTime();
			long now = System.currentTimeMillis();
			System.out.println("===================ago:" + ago);
			System.out.println("====================now:" + now);
			System.out.println("=======================" + (now - ago));
			if (System.currentTimeMillis() - ago > (5000 * 60)) {
				System.out.println("===============����5����");
				// �����ϴη����ʼ�����5����
				soCode.setCode(Test.getStringRandom(8));
				soCode.setTime(new Date());
				postsMapper.updateSoCode(soCode);
				Test.sendEmail(useremail, soCode.getCode());
			} else if (System.currentTimeMillis() - ago < (1000 * 60)) {
				System.out.println("====================����Ƶ��");
				return new Data(-1, "����Ƶ��" + ((1000 * 60) - (now - ago)));
			} else {
				System.out.println("====================����1����С��5����");
				Test.sendEmail(useremail, soCode.getCode());
				soCode.setTime(new Date());
				postsMapper.updateSoCode(soCode);
			}
		}

		return new Data(1, "���ͳɹ�");
	}
	
	
	@RequestMapping("/editor")
	public String demo() {
		return "demo";
	}
	
	@RequestMapping("/insertPosts")
	public String insertPosts(String title,String content,String simple) {
		System.out.println("����InsertPosts");
		Posts posts = new Posts();
		posts.setTitle(title);
		posts.setContent(content);
		posts.setSimple(simple);
		posts.setTime(new Date());
		postsMapper.save(posts);
		return "redirect:index";
	}
	
	@RequestMapping(value = "/editorMDUpload" ,method = RequestMethod.POST)
	public void editorMD(HttpServletRequest request,HttpServletResponse response,@RequestParam("editormd-image-file") MultipartFile file) throws Exception{
		try {
			request.setCharacterEncoding("utf-8");
			//���ﷵ�ظ�ʽ��html/txt�����ϴ�
			response.setHeader("Content-Type", "application/json");
			//���web��Ŀ��ȫ·��
			String rootPath = request.getSession().getServletContext().getRealPath("/resource/upload");
			//Calendar.getInstance()�ǻ�ȡһ��Calendar���󲢿��Խ���ʱ��ļ��㣬ʱ����ָ��
			Calendar date = Calendar.getInstance();
			//�������·����MONTH��ֵ�ĳ�ʼֵ��0���������Ҫ��������ʾ��ȷ���·�ʱ����Ҫ��1
			File dateFile = new File(date.get(Calendar.YEAR)+"/"+(date.get(Calendar.MONTH)+1)+"/"+(date.get(Calendar.DATE)));
			//����ļ������·��
			String originalFile = file.getOriginalFilename();
			//�õ�����·����
			File newFile = new File(rootPath+File.separator+dateFile+File.separator+originalFile);
			
			//�ļ������ھʹ���
			if(!newFile.getParentFile().exists()) {
				newFile.getParentFile().mkdirs();
			}
			file.transferTo(newFile);
			System.out.println(dateFile+"/"+file.getOriginalFilename());
			//JSON��ʽ
			response.getWriter().write("{\"success\":1,\"message\":\"�ϴ��ɹ�\",\"url\":\"/resource/upload/"+date.get(Calendar.YEAR)+"/"+(date.get(Calendar.MONTH)+1)+"/"+date.get(Calendar.DATE)+"/"+file.getOriginalFilename()+"\"}");
			
		}catch (Exception e) {
			// TODO: handle exception
			response.getWriter().write("{\"success\":0}");
		}
		
	}
	
	@RequestMapping("/deletePosts")
	public String deletePosts(int id) {
		int num = postsMapper.delete(id);
		if(num == 1) {
			System.out.println("ɾ���ɹ�");
			return "redirect:index";
		}

		return "/details/"+id;
		
		
	}
}
