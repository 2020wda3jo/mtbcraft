package com.mtbcraft.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtbcraft.dto.Club;
import com.mtbcraft.dto.Club_Join;
import com.mtbcraft.mapper.CommunityMapper;

@Service
@Transactional
public class CommunityService {
	@Resource(name = "com.mtbcraft.mapper.CommunityMapper")
	@Autowired
	private CommunityMapper communityMapper;

	// 클럽 생성
	public void insertClub(Club club) throws Exception {
		communityMapper.insertClub(club);
	}

	/// 클럽이름 중복검사
	public int checkClubName(String cb_name) {
		return communityMapper.checkClubName(cb_name);
	}

	public void insertCJ(Club_Join cb_join) {
		communityMapper.insertCJ(cb_join);
	}

	// 클럽 멤버수 조회
	public int getMember(Club cb_num) {
		return communityMapper.getMember(cb_num);
	}

	// 클럽 조회
	public List<Club> getClub(Club cb_num) {
		return communityMapper.getClub(cb_num);
	}

	// 클럽 가입
	public void signClub(Club_Join cb_join) {
		communityMapper.signClub(cb_join);
	}

}