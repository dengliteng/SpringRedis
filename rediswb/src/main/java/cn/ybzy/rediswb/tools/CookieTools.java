package cn.ybzy.rediswb.tools;

import javax.servlet.http.Cookie;

public class CookieTools {
	
	//工具方法，在cookies数组中找到我们想要的cookie
	public static Cookie findCookie(Cookie[] cookies,String cookieName) {
		Cookie cookie = null;
		if(cookies != null && cookies.length > 0) {
			for(int i=0;i<cookies.length;i++) {
				if(cookieName.equals(cookies[i].getName())) {
					cookie = cookies[i];
					break;
				}
			}
		}
		return cookie;
	}

}
