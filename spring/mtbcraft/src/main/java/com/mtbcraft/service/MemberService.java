package com.mtbcraft.service;

import javax.annotation.Resource;


import org.springframework.stereotype.Service;

import com.mtbcraft.dto.Login;
import com.mtbcraft.dto.Member;
import com.mtbcraft.mapper.MemberMapper;


@Service
public class MemberService {
	@Resource(name="com.mtbcraft.mapper")
	MemberMapper memberMapper;
	
	
	public String memberInsert(Member member) throws Exception {
		return memberMapper.memberInsert(member);
	}
	
	public String memberLogin(Login login) throws Exception {
		return memberMapper.memberLogin(login);
	}
}
