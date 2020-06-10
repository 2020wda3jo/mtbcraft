package com.mtbcraft.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtbcraft.dto.Club;
import com.mtbcraft.dto.Club_Join;
import com.mtbcraft.mapper.CommunityMapper;
import com.mtbcraft.mapper.MemberMapper;

@Service
@Transactional
public class CommunityService {
	@Resource(name="com.mtbcraft.mapper.CommunityMapper")
	@Autowired
	private CommunityMapper communityMapper;
	
	//클럽 생성
	public void insertClub(Club club) throws Exception {
		communityMapper.insertClub(club);
	}
	
	///클럽이름 중복검사
	public int checkClubName(String cb_name){
		return communityMapper.checkClubName(cb_name);
	}
	
	public void insertCJ(Club_Join cb_join) {
		communityMapper.insertCJ(cb_join);
	}
	
}