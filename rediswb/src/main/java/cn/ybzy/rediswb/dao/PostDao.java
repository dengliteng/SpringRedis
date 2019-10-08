package cn.ybzy.rediswb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.ybzy.rediswb.model.Post;

public interface PostDao {
	
	//实现插入微博信息的方法
	public void insert(Post post);

	//根据uid查询post信息
	public List<Post> getPostByUid(@Param("uid") int uid);

	
	//根据登录的用户id，获取此用户发布的信息和此用户关注的用户发布的信息
	public List<Post> getHomePostByUid(@Param("uid") int uid);

	public List<Post> getLast50Posts();


}
