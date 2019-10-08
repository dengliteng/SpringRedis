package cn.ybzy.rediswb.service;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.ybzy.rediswb.dao.UserDao;
import cn.ybzy.rediswb.dao.UserFocusDao;
import cn.ybzy.rediswb.model.User;
import cn.ybzy.rediswb.redis.JedisClientPoolDao;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserFocusDao userFocusDao;

	// 要操作redis
	@Autowired
	private JedisClientPoolDao jedisClientPool;

	@Override
	public void insert(User user) {
		// 服务层又需要userDao
		userDao.insert(user);
	}

	@Override
	public void login(String username, String password, HttpServletResponse response) {
		// 1.通过用户名username，查找一下数据表里的数据，存在这个用户不？
		User user = userDao.getUserByu(username);
		if (user == null) {
			throw new RuntimeException("用户名或密码不正确！");
		}
		// 2.有此用户进一步判断密码对不对
		if (!user.getPassword().equals(password)) {
			throw new RuntimeException("用户名或密码不正确！");
		}

		// 3.登录认证成功，我们要把登录成功的用户的信息，保存到redis里，传统的做法是保存到session
		// 还需要将user对象，转json格式字符串的工具
		ObjectMapper mapper = new ObjectMapper();
		String userStr = null;
		try {
			userStr = mapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("jackson转换对象为json时发生异常！");
		}
		jedisClientPool.set("loginUser:id:" + user.getId(), userStr);
		// session是有时效性
		jedisClientPool.expire("loginUser:id:" + user.getId(), 1800);
		// 最后还要把userid放到浏览器端的cookie
		Cookie cookie = new Cookie("currUser", user.getId() + "");
		// 通过cookie实现跨域共享，后面的课程单点登录，和集群。。。
		response.addCookie(cookie); // 把cookie写到浏览器的客户端
	}

	@Override
	public User getUserByUid(Integer u) {
		return userDao.getUserByUid(u);
	}

	@Override
	public void insertUserFocus(int id, int id2) {
		userFocusDao.insert(id, id2);

		// 同步缓存
		jedisClientPool.del("home:uid:" + id2 + ":fs");
		jedisClientPool.del("home:uid:" + id + ":follows");
	}

	@Override
	public List<Integer> getFollowState(int loginuserid, int focususerid) {
		return userFocusDao.getFollowState(loginuserid, focususerid);
	}

	@Override
	public void deleteUserFocus(int loginuserid, int focususerid) {
		userFocusDao.delete(loginuserid, focususerid);

		// 同步缓存
		jedisClientPool.del("home:uid:" + focususerid + ":fs");
		jedisClientPool.del("home:uid:" + loginuserid + ":follows");
	}

	@Override
	public int getFs(int uid) {
		int fs;
		// 从redis取
		String fsStr = jedisClientPool.get("home:uid:" + uid + ":fs");
		if (StringUtils.isEmpty(fsStr)) {
			System.out.println("----------fs：从mysql-----------");
			// 从mysql取
			fs = userFocusDao.getFs(uid);
			// 存redis
			jedisClientPool.set("home:uid:" + uid + ":fs", fs + "");
		} else {
			System.out.println("----------fs：从redis----------");
			fs = Integer.parseInt(fsStr);
		}
		return fs;
	}

	@Override
	public int getFollows(int uid) {
		int follows;
		// 从redis取
		String fsStr = jedisClientPool.get("home:uid:" + uid + ":follows");
		if (StringUtils.isEmpty(fsStr)) {
			System.out.println("----------follows：从mysql-----------");
			// 从mysql取
			follows = userFocusDao.getFollows(uid);
			// 存redis
			jedisClientPool.set("home:uid:" + uid + ":follows", follows + "");
		} else {
			System.out.println("----------follows：从redis-----------");
			follows = Integer.parseInt(fsStr);
		}
		return follows;
	}

	@Override
	public List<User> getLast50Users() {
		List<User> rUsers = null;
		ObjectMapper mapper = new ObjectMapper();
		//先从redis
		String last50UsersStr = jedisClientPool.get("timeline:last50Users");
		if(StringUtils.isEmpty(last50UsersStr)) {
			//从mysql取
			rUsers = userDao.getLast50Users();
			if(rUsers!=null)
				try {
					last50UsersStr = mapper.writeValueAsString(rUsers);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			//存redis
			jedisClientPool.set("timeline:last50Users",last50UsersStr);
			jedisClientPool.expire("timeline:last50Users", 60);
		}else {
			try {
				rUsers = mapper.readValue(last50UsersStr, new TypeReference<List<User>>() {});
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return rUsers;
	}


}
