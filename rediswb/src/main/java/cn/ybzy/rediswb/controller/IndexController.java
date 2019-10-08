package cn.ybzy.rediswb.controller;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.ybzy.rediswb.model.User;
import cn.ybzy.rediswb.redis.JedisClientPoolDao;
import cn.ybzy.rediswb.service.UserService;
import cn.ybzy.rediswb.tools.CommonTools;
import cn.ybzy.rediswb.tools.CookieTools;

@Controller
public class IndexController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisClientPoolDao jedisClientPool;
	
	@RequestMapping(value= {"/","/index","/index.html"},method=RequestMethod.GET)
	public String index(HttpServletRequest request) {
		User loginUser = CommonTools.getLoginUser(request, jedisClientPool);
		if(loginUser != null) {
			return "redirect:/home.html";
		}
		return "index";
	}
	
	/**
	 * 接受注册的数据的方法
	 */
	@RequestMapping(value="/register.html",method=RequestMethod.POST)
	public String register(User user,String password2) {
		//新用户注册逻辑，后端数据的有效性验证。。。细节东西省略
		//简化的判断一下，两次输入是不是一致
		if(!user.getPassword().equals(password2)) {
			throw new RuntimeException("前后输入的密码不对！");
		}
		//实施注册新用户的核心功能：插入用户信息到数据库
		user.setCreateTime(new Date());
		userService.insert(user); 
		return "redirect:/index.html";
	}
	
	@RequestMapping(value="/login.html",method=RequestMethod.POST)
	public String login(String username,String password,HttpServletResponse response) {
		//进行登录验证。。。。用户输入的数据有效性验证，省略。。。
		//验证用户名和密码对不对
		try {
			userService.login(username,password,response);
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/index.html";
		}
		
		return "redirect:/home.html";
	}
	
	
	@RequestMapping(value="/logout.html",method=RequestMethod.GET)
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		Cookie cookie = CookieTools.findCookie(cookies, "currUser");
		if(cookie!=null) {
			//上传redis中的user信息
			jedisClientPool.del("loginUser:id:" + cookie.getValue());
			//删除cookie里userid
			cookie.setValue("");
			response.addCookie(cookie); //生效到客户端
		}
		return "redirect:/index.html";
	}
	
	

}
