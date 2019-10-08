package cn.ybzy.rediswb.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.ybzy.rediswb.model.User;

public interface UserService {

	// 向数据库里插入新的用户
	public void insert(User user);

	// 实现登录认证的业务逻辑
	public void login(String username, String password, HttpServletResponse response);

	public User getUserByUid(Integer uid);

	public void insertUserFocus(int loginuserid, int focususerid);

	public List<Integer> getFollowState(int loginuserid, int focususerid);

	public void deleteUserFocus(int loginuserid, int focususerid);

	public int getFs(int uid);

	public int getFollows(int uid);

	public List<User> getLast50Users();


	
}
