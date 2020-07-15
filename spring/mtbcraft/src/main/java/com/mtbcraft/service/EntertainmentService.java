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
import com.mtbcraft.dto.EntertainRider;
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
	public List<Badge> getBadge(String rider) throws Exception{
		return entertainmentMapper.getBadge(rider);
	}
	
	//배지 등록
	public void BadgeUpload(Badge badge)throws Exception{
		entertainmentMapper.BadgeUpload(badge);
	}
	//미션 전체보기
	public List<Mission> getMission() throws Exception{
		return entertainmentMapper.getMission();
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
	
	//참여한 최근 경쟁전 3개조회
	public List<Competition> getRecentComp3(String rider){
		return entertainmentMapper.getRecentComp3(rider);
	}
	
	//참여한 최근 경쟁전 4개 조회
	public List<Competition> getRecentComp4(String rider){
		return entertainmentMapper.getRecentComp4(rider);
	}
	
	// 진행중인 경쟁전 2개 조회
	public List<Competition> getIngComp2(){
		return entertainmentMapper.getIngComp2();
	}

	// 종료된 경쟁전 2개 조회
	public List<Competition> getEndComp2(){
		return entertainmentMapper.getEndComp2();
	}
	
	//번호로 경쟁전 조회
	public Competition getComp(int comp_num) {
		return entertainmentMapper.getComp(comp_num);
	}
	
	//성공한 미션 조회
	public List<Mission> getCompleteMission(String rider){
		return entertainmentMapper.getCompleteMission(rider);
	}
	
	//미션 상세 정보 조회
	public Mission getMissionDetail(int m_num) {
		return entertainmentMapper.getMissionDetail(m_num);
	}
	
	//미션 상세 정보 조회시 성공 인원 조회
	public List<EntertainRider> getER_List(int m_num){
		return entertainmentMapper.getER_List(m_num);
	}
	
	//전체 회원 조회
	public int getTotalRiders() {
		return entertainmentMapper.getTotalRiders();
	}
}