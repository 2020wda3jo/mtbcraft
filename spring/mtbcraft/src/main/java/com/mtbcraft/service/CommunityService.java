package com.mtbcraft.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtbcraft.dto.Club;
import com.mtbcraft.dto.Club_Calender;
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
	
	//클럽 가입
	public void insertCJ(Club_Join cb_join) {
		communityMapper.insertCJ(cb_join);
	}
	
	//클럽 캘린더 일정 조회
	public List<Club_Calender> getCCList(int cc_club){
		return communityMapper.getCCList(cc_club);
	}
	
	//클럽 캘린더 일정 등록
	public void postCC(Club_Calender cc) {
		communityMapper.postCC(cc);
	}
	
	//클럽 캘린더 일정 수정
	public void updateCC(Club_Calender cc) {
		communityMapper.updateCC(cc);
	}
	
	//클럽 캘린더 일정 삭제
	public void deleteCC(int cc_num) {
		communityMapper.deleteCC(cc_num);
	}
	
}