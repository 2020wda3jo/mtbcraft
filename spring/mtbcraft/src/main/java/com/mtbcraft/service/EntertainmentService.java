package com.mtbcraft.service;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtbcraft.dto.Badge;
import com.mtbcraft.dto.CompIng;
import com.mtbcraft.dto.Competition;
import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.DangerousArea;
import com.mtbcraft.dto.Member;
import com.mtbcraft.dto.Mission;
import com.mtbcraft.dto.MissionComplete;
import com.mtbcraft.dto.No_Danger;
import com.mtbcraft.dto.RidingRecord;
import com.mtbcraft.mapper.EntertainmentMapper;
import com.mtbcraft.mapper.MemberMapper;

@Service
@Transactional
public class EntertainmentService {
	@Resource(name="com.mtbcraft.mapper.EntertainmentMapper")
	@Autowired
	private EntertainmentMapper entertainmentMapper;
	
	
	//배지 조회
	public List<Badge> getBadge() throws Exception{
		return entertainmentMapper.getBadge();
	}
	
	//배지 등록
	public void BadgeUpload(Badge badge)throws Exception{
		entertainmentMapper.BadgeUpload(badge);
	}
	//미션 전체보기
	public List<Mission> getMission() throws Exception{
		return entertainmentMapper.getMission();
	}
	
	//완료한 미션보기
	public List<MissionComplete> getMissionComplete(String rr_rider) throws Exception{
		return entertainmentMapper.getMissionComplete(rr_rider);
	}

	
	//지난 경쟁전 보기
	public List<Competition> getCompetition() throws Exception{
		return entertainmentMapper.getCompetition();
	}
	
	//미션 업로드
	public void missionUpload(Mission mission) throws Exception {
		entertainmentMapper.missionUpload(mission);
	}
	
	//진행중인 경쟁전 보기
	public List<CompIng> getCompIng() throws Exception{
		return entertainmentMapper.getCompIng();
	}
	
	
	
}