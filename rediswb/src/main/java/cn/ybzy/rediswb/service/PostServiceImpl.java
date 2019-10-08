package cn.ybzy.rediswb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.ybzy.rediswb.dao.PostDao;
import cn.ybzy.rediswb.model.Post;
import cn.ybzy.rediswb.redis.JedisClientPoolDao;

@Service("postService")
public class PostServiceImpl implements PostService {
	
	@Autowired
	private PostDao postDao;
	
	@Autowired
	private JedisClientPoolDao jedisClientPool;

	@Override
	public void insert(Post post) {
		postDao.insert(post);
		//添加完数据，一定要清楚redis缓存
		jedisClientPool.hdel("content_list", "profile:uid:"+post.getUser().getId());
		jedisClientPool.hdel("content_list", "home:uid:"+post.getUser().getId());
	}

	@Override
	public List<Post> getPostByUid(int uid) {
		List<Post> postList = null;
		ObjectMapper mapper = new ObjectMapper();
		String postListJson = null;

		//1.先看看redis里面有数据没的，有直接取之
		postListJson = jedisClientPool.hget("content_list", "profile:uid:"+uid);
		if(StringUtils.isEmpty(postListJson)) {
			System.out.println("-----------是从mysql中取出的数据--------------");
			//2.没有，在去mysql里取数据
			postList = postDao.getPostByUid(uid);
			try {
				postListJson = mapper.writeValueAsString(postList);
				//3.mysql中取出的数据放进redis里边
				jedisClientPool.hset("content_list", "profile:uid:"+uid, postListJson);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("-----------是从redis中取出的数据--------------");
			try {
				postList = mapper.readValue(postListJson, new TypeReference<List<Post>>() {});
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return postList;
	}

	@Override
	public List<Post> getHomePost(int uid) {
		List<Post> postList = null;
		ObjectMapper mapper = new ObjectMapper();
		String postListJson = null;
		//1.先看看redis里面有数据没的，有直接取之
		postListJson = jedisClientPool.hget("content_list", "home:uid:"+uid);
		if(StringUtils.isEmpty(postListJson)) {
			System.out.println("-----------home是从mysql中取出的数据--------------");
			//2.没有，在去mysql里取数据
			postList = postDao.getHomePostByUid(uid);
			try {
				postListJson = mapper.writeValueAsString(postList);
				//3.mysql中取出的数据放进redis里边
				jedisClientPool.hset("content_list", "home:uid:"+uid, postListJson);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("-----------home是从redis中取出的数据--------------");
			try {
				postList = mapper.readValue(postListJson, new TypeReference<List<Post>>() {});
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return postList;
	}

	@Override
	public List<Post> getLast50Posts() {
		List<Post> postList = null;
		ObjectMapper mapper = new ObjectMapper();
		String postListJson = null;
		//1.先看看redis里面有数据没的，有直接取之
		postListJson = jedisClientPool.get("timeline:50post");
		if(StringUtils.isEmpty(postListJson)) {
			System.out.println("-----------timeline是从mysql中取出的数据--------------");
			//2.没有，在去mysql里取数据
			postList = postDao.getLast50Posts();
			try {
				postListJson = mapper.writeValueAsString(postList);
				//3.mysql中取出的数据放进redis里边
				jedisClientPool.set("timeline:50post",postListJson);
				jedisClientPool.expire("timeline:50post", 60);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("-----------timeline是从redis中取出的数据--------------");
			try {
				postList = mapper.readValue(postListJson, new TypeReference<List<Post>>() {});
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return postList;
	}


}
