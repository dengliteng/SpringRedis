package cn.ybzy.rediswb.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.ybzy.rediswb.redis.JedisClientPoolDao;
import cn.ybzy.rediswb.tools.CookieTools;

public class AuthInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private JedisClientPoolDao jedisClientPool;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//springmvc拦截器关键的方法
		//拿到客户端的cookie数值
		Cookie[] cookies = request.getCookies();
		//找到目录，登录的时候放进去currUser
		Cookie cookie = CookieTools.findCookie(cookies, "currUser");
		if(cookie!=null) {
			//拼接cookie里的userid，从redis中查找登录用户的信息
			String userStr = jedisClientPool.get("loginUser:id:"+cookie.getValue());
			//System.out.println(userStr);
			//User user = new ObjectMapper().readValue(userStr, User.class);
			if(userStr!=null) {
				//用户是登录成功的状态，放行
				return true;
			}else {
				//用户登录过期
				response.sendRedirect(request.getContextPath() + "/index.html");
				return false;
			}
			
		}else {
			// 没有登入
			response.sendRedirect(request.getContextPath() + "/index.html");
			return false;
		}
	}
	
	

}
