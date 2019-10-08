<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
				<a href="<%=request.getContextPath()%>/index.html">主页</a> | <a href="<%=request.getContextPath()%>/timeline.html">热点</a> | <a href="<%=request.getContextPath()%>/logout.html">退出</a>
			</div>
		</div>
		<div id="welcomebox">
			<div id="registerbox">
				<h2>注册!</h2>
				<b>请注册账号!</b>
				<form method="POST" action="<%=request.getContextPath()%>/register.html">
					<table>
						<tr>
							<td>用户名</td>
							<td><input type="text" name="username"></td>
						</tr>
						<tr>
							<td>密码</td>
							<td><input type="password" name="password"></td>
						</tr>
						<tr>
							<td>重复密码</td>
							<td><input type="password" name="password2"></td>
						</tr>
						<tr>
							<td colspan="2" align="right"><input type="submit" name="doit" value="注册"></td>
						</tr>
					</table>
				</form>
				<h2>已经注册了? 请直接登陆</h2>
				<form method="POST" action="login.html">
					<table>
						<tr>
							<td>用户名</td>
							<td><input type="text" name="username"></td>
						</tr>
						<tr>
							<td>密码:</td>
							<td><input type="password" name="password"></td>
						</tr>
						<tr>
							<td colspan="2" align="right"><input type="submit" name="doit" value="Login"></td>
						</tr>
					</table>
				</form>
			</div>
			<ul>
				<li>Redis 是一种key-value 数据库</li>
				<li>本项目是一个使用Redis的实战案例</li>
			</ul>
		</div>
		<div id="footer">
			redis版本的仿微博项目 <a href="http://redis.io">Redis key-value database</a>
		</div>
	</div>
</body>
</html>
