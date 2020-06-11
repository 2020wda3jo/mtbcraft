package com.mtbcraft.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mtbcraft.dto.Club;
import com.mtbcraft.dto.Club_Join;

@Repository("com.mtbcraft.mapper.CommunityMapper")
public interface CommunityMapper {

	//클럽 생성
	public void insertClub(Club club) throws Exception;
	
	//클럽이름 중복검사
	public int checkClubName(String cb_name);
	
	public void insertCJ(Club_Join cb_join);
	
	// 클럽 멤버 조회
	public int getMember(Club cb_num);
	
	// 클럽 조회
	public List<Club> getClub(Club cb_num);
	
	// 클럽 가입
	public void signClub(Club_Join cb_join);
}
