package cn.ybzy.rediswb.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.ybzy.rediswb.model.Post;
import cn.ybzy.rediswb.model.User;
import cn.ybzy.rediswb.redis.JedisClientPoolDao;
import cn.ybzy.rediswb.service.PostService;
import cn.ybzy.rediswb.service.UserService;
import cn.ybzy.rediswb.tools.CommonTools;

@Controller
public class HomeController {
	@Autowired
	private PostService postService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisClientPoolDao jedisClientPool;
	
	@RequestMapping(value="/home.html",method=RequestMethod.GET)
	public String home(HttpServletRequest request,Model model) {
		//获取登录的用户信息
		User loginUser = CommonTools.getLoginUser(request, jedisClientPool);
		if(loginUser != null) {
			model.addAttribute("loginUser", loginUser);
		}
		
		//实现粉丝数，关注数的显示
		int fs = userService.getFs(loginUser.getId());
		int folllows = userService.getFollows(loginUser.getId());
		model.addAttribute("fs", fs);
		model.addAttribute("folllows", folllows);
		
		//实现下面显示的微博信息
		List<Post> homePostList =  postService.getHomePost(loginUser.getId());
		model.addAttribute("homePostList",homePostList);
		
		
		return "home";
	}
	
	
	@RequestMapping(value="/post.html",method=RequestMethod.POST)
	public String post(Post post,HttpServletRequest request) {
		User loginUser = CommonTools.getLoginUser(request, jedisClientPool);
		post.setCreateTime(new Date());
		post.setUser(loginUser);
		
		//调用services里的方法，做数据插入
		postService.insert(post);
		
		return "redirect:/home.html";
	}
	

}
