package cn.ybzy.rediswb.tools;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.ybzy.rediswb.model.User;
import cn.ybzy.rediswb.redis.JedisClientPoolDao;

public class CommonTools {

	public static User getLoginUser(HttpServletRequest request,JedisClientPoolDao jedisClientPool) {
		// 获取登录的用户信息
		Cookie[] cookies = request.getCookies();
		Cookie cookie = CookieTools.findCookie(cookies, "currUser");
		User user = null;
		if (cookie != null) {
			try {
				String loginUserJson = jedisClientPool.get("loginUser:id:" + cookie.getValue());
				if(!StringUtils.isEmpty(loginUserJson))
					user = new ObjectMapper().readValue(loginUserJson, User.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return user;
	}

}
