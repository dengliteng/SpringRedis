package cn.ybzy.rediswb.controller;

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
public class ProFileController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisClientPoolDao jedisClientPool;
	
	@Autowired
	private PostService postService;
	
	@RequestMapping(value = "/profile.html", method = RequestMethod.GET)
	public String profile(Integer u,Model model,HttpServletRequest request) {
		//拿到传过来的uid
		User focusUser = userService.getUserByUid(u);
		model.addAttribute("user", focusUser);
		
		//拿到当前登录的用户
		User loginUser = CommonTools.getLoginUser(request, jedisClientPool);
		
		
		//判断当前登录的用户，和目标用户直接的关注关系
		List<Integer> followStatList = userService.
				getFollowState(loginUser.getId(),focusUser.getId());
		
		boolean fsFlag = false;
		if(followStatList.size()>0) {
			for(int i=0;i<followStatList.size();i++) {
				if(followStatList.get(i)==1) {
					//当前用户关注了目标用户
					fsFlag = true;
					break;
				}
			}
		}
		
		if(fsFlag) {
			model.addAttribute("fsbtnValue", "取消关注ta");
			model.addAttribute("f","1");
		}else {
			model.addAttribute("fsbtnValue", "关注ta");
			model.addAttribute("f","0");
		}
		
		//显示个人主页下面的微博信息
		List<Post> postList = postService.getPostByUid(focusUser.getId());
		model.addAttribute("postList", postList);
		
		return "profile";
	}
	
	@RequestMapping(value="/follow.html",method=RequestMethod.GET)
	public String follow(Integer focusuid,Integer f,HttpServletRequest request) {
		//判断focusuid存不存在，不存在直接跳回，省略
		User focusUser = userService.getUserByUid(focusuid);
		if(focusUser == null) return "redirect:/profile.html?u="+focusuid;
		
		//拿到当前登录用户的信息
		User loginUser = CommonTools.getLoginUser(request, jedisClientPool);
		
		if(f==0) {
			//调用userService建立关注关系
			userService.insertUserFocus(loginUser.getId(),focusUser.getId());
		}else if(f==1) {
			//调用userService取消关注关系
			userService.deleteUserFocus(loginUser.getId(),focusUser.getId());
		}
		
		return "redirect:/profile.html?u="+focusuid;
	}
}
