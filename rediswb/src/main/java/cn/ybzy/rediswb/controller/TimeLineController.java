package cn.ybzy.rediswb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.ybzy.rediswb.model.Post;
import cn.ybzy.rediswb.model.User;
import cn.ybzy.rediswb.service.PostService;
import cn.ybzy.rediswb.service.UserService;

@Controller
public class TimeLineController {
	@Autowired
	private UserService userService;
	@Autowired
	private PostService postService;
	
	@RequestMapping(value = "/timeline.html", method = RequestMethod.GET)
	public String profile(Model model) {
		
		//显示最新的50个用户
		List<User> last50Users = userService.getLast50Users();
		model.addAttribute("last50Users", last50Users);
		
		//显示最近的50条微博
		List<Post> last50Posts = postService.getLast50Posts();
		model.addAttribute("last50Posts", last50Posts);
		
		
		return "timeline";
	}
}
