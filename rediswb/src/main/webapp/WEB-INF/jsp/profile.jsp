<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="it">
<head>
<meta content="text/html; charset=UTF-8" http-equiv="content-type">
<title>Redis实战案例</title>
<link href="<%=request.getContextPath()%>/res/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="page">
		<div id="header">
			<a href="/"><img style="border: none" src="<%=request.getContextPath()%>/res/img/logo.png" width="192" height="85" alt="Retwis"></a>
			<div id="navbar">
				<a href="<%=request.getContextPath()%>/index.html">主页</a> | <a href="<%=request.getContextPath()%>/timeline.html">热点</a> | <a
					href="<%=request.getContextPath()%>/logout.html">退出</a>
			</div>
		</div>
		<h2 class="username">${user.username}</h2>
		
		<a href="<%=request.getContextPath()%>/follow.html?focusuid=${user.id}&f=${f}" class="button">${fsbtnValue}</a>

		<c:forEach items="${postList}" var="postMsg">
		<div class="post">
			<a class="username" href="profile.html?u=${user.id}">${user.username}</a>${postMsg.content}<br> <i>${postMsg.createTime} 通过 web发布</i>
		</div>
		</c:forEach>

		<div id="footer">
			redis版本的仿微博项目 <a href="http://redis.io">Redis key-value database</a>
		</div>
	</div>
</body>
</html>
