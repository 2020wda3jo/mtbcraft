package com.mtbcraft.service;

import javax.annotation.Resource;


import org.springframework.stereotype.Service;

import com.mtbcraft.dao.MemberDao;
import com.mtbcraft.model.Login;
import com.mtbcraft.model.Member;


@Service
public class MemberService {
	@Resource(name="com.mtbcraft.dao")
	MemberDao memberMapper;
	
	
	public String memberInsert(Member member) throws Exception {
		return memberMapper.memberInsert(member);
	}
	
	public String memberLogin(Login login) throws Exception {
		return memberMapper.memberLogin(login);
	}
}
