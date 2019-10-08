package cn.ybzy.rediswb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserFocusDao {

	// 创建关注关系
	public void insert(@Param("loginid") Integer loginid, @Param("focusid") Integer focusid);

	public List<Integer> getFollowState(@Param("loginid") int loginuserid, @Param("focusid") int focususerid);

	public void delete(@Param("loginid") int loginuserid, @Param("focusid") int focususerid);

	// 获取粉丝数
	public int getFs(@Param("uid") int uid);

	//获取关注数
	public int getFollows(@Param("uid") int uid);

}
