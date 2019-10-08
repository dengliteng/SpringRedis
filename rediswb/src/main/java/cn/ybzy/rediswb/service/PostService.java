package cn.ybzy.rediswb.service;

import java.util.List;

import cn.ybzy.rediswb.model.Post;

public interface PostService {
	
	//插入微博信息到数据库
	public void insert(Post post);

	
	//根据用户id查询此用户发布的微博信息
	public List<Post> getPostByUid(int uid);


	//根据登录的用户id，获取此用户发布的信息和此用户关注的用户发布的信息
	public List<Post> getHomePost(int uid);


	public List<Post> getLast50Posts();


}
