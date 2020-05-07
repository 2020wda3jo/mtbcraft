package com.mtbcraft.mapper;

import org.springframework.stereotype.Repository;

import com.mtbcraft.dto.Login;
import com.mtbcraft.dto.Member;

@Repository("com.mtbcraft.mapper")
public interface MemberMapper {

//회원가입 작성
	public String memberInsert(Member member) throws Exception;
	public String memberLogin(Login login) throws Exception;
}
