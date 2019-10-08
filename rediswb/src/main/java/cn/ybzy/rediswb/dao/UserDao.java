package cn.ybzy.rediswb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.ybzy.rediswb.model.User;

public interface UserDao {

	// 插入新用户
	public void insert(User user);

	// 通过用户名查找用户的方法
	public User getUserByu(@Param("username") String username);

	//根据用户id获取user
	public User getUserByUid(@Param("uid") Integer uid);

	public List<User> getLast50Users();

}
