package com.mtbcraft.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mtbcraft.dto.Badge;
import com.mtbcraft.dto.CompIng;
import com.mtbcraft.dto.Competition;
import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.DangerousArea;
import com.mtbcraft.dto.Login;
import com.mtbcraft.dto.Member;
import com.mtbcraft.dto.Mission;
import com.mtbcraft.dto.MissionComplete;
import com.mtbcraft.dto.No_Danger;
import com.mtbcraft.dto.RidingRecord;

@Repository("com.mtbcraft.mapper.EntertainmentMapper")
public interface EntertainmentMapper {

	// 진행중인 경쟁전 조회
	public List<CompIng> getCompIng() throws Exception;
	
	// 등록된 경쟁전 조회
	public List<Competition> getCompetition() throws Exception;

	// 등록된 미션 조회
	public List<Mission> getMission() throws Exception;

	// 미션 성공여부 조회
	public List<MissionComplete> getMissionComplete(String rr_rider) throws Exception;

	// 미션 등록
	public void missionUpload(Mission mission) throws Exception;
	
	//배지 조회
	public List<Badge> getBadge() throws Exception;
	
	//배지 등록
	public void BadgeUpload(Badge badge)throws Exception;




}