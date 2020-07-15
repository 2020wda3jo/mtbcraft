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

	// 미션 등록
	public void missionUpload(Mission mission) throws Exception;
	
	//배지 조회
	public List<Badge> getBadge(String rider);
	
	//배지 등록
	public void BadgeUpload(Badge badge)throws Exception;

	//참여한 최근 경쟁전 3개 조회
	public List<Competition> getRecentComp3(String rider);
	
	//참여한 최근 경쟁전 4개 조회
	public List<Competition> getRecentComp4(String rider);
	
	// 진행중인 경쟁전 2개 조회
	public List<Competition> getIngComp2();

	// 종료된 경쟁전 2개 조회
	public List<Competition> getEndComp2();
	
	//번호로 경쟁전 조회
	public Competition getComp(int comp_num);
	
	//성공한 미션 조회
	public List<Mission> getCompleteMission(String rider);
	
}