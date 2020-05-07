package com.mtbcraft.dao;

import org.springframework.stereotype.Repository;

import com.mtbcraft.model.Login;
import com.mtbcraft.model.Member;

@Repository("com.mtbcraft.dao")
public interface MemberDao {

//회원가입 작성
	public String memberInsert(Member member) throws Exception;
	public String memberLogin(Login login) throws Exception;
}
