package com.mtbcraft.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtbcraft.mapper.AndroidMapper;
import com.mtbcraft.dto.AnLogin;
import com.mtbcraft.dto.App_RidingRecord;
import com.mtbcraft.dto.Badge;
import com.mtbcraft.dto.CompClub;
import com.mtbcraft.dto.CompScore;
import com.mtbcraft.dto.Competition;
import com.mtbcraft.dto.Competition_Status;
import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.DangerousArea;
import com.mtbcraft.dto.Like_Status;
import com.mtbcraft.dto.Login;
import com.mtbcraft.dto.LoginInfo;
import com.mtbcraft.dto.Mission;
import com.mtbcraft.dto.Mission_Status;
import com.mtbcraft.dto.RidingRecord;
import com.mtbcraft.dto.Scrap_Status;


@Service
@Transactional
public class AndroidService {

	@Autowired
	private AndroidMapper androidMapper;
	
	//로그인
	public List<AnLogin> LoginProcess(AnLogin login) throws Exception{
		return androidMapper.LoginProcess(login);
	}
	
	public LoginInfo getLoginInfo(String LoginId) throws Exception{
		return androidMapper.getLoginInfo(LoginId);
	}
	//라이딩기록 저장
	public String insertRecord(RidingRecord record) throws Exception{
		return androidMapper.insertRecord(record);
	}
	
	public String insertRecordWithComp(RidingRecord record) throws Exception{
		return androidMapper.insertRecordWithComp(record);
	}
	
	//라이딩 기록 전체보기
	public List<RidingRecord> getRidingRecordAll() {
		return androidMapper.getRidingRecordAll();
	}
	
	//라이딩기록 보기
	public List<App_RidingRecord> readRecord(String rr_rider) throws Exception{
		return androidMapper.readRecord(rr_rider);
	}
	
	//주행기록 상세가져오기
	public List<RidingRecord> getRidingRecordDetail(String rr_rider, String rr_num) throws Exception {
		return androidMapper.getRidingRecordDetail(rr_rider, rr_num);
	}
	
	//코스가져오기
	public List<App_RidingRecord> getCourse() throws Exception{
		return androidMapper.getCourse();
	}
	
	//코스상세보기
	public List<Course> getCourseItem(String c_num) throws Exception{
		return androidMapper.getCourseItem(c_num);
	}
	
	//코스 스크랩
	public String insertScrap(Scrap_Status scrap) throws Exception {
		return androidMapper.insertScrap(scrap);
	}
	
	//스크랩코스 보기
	public List<Scrap_Status> getScrap(String rr_rider) throws Exception {
		return androidMapper.getScrap(rr_rider);
	}
	
	//스크랩코스 상세보기
	public List<Course> getScrapDetail(String rr_rider, String ss_course) throws Exception {
		return androidMapper.getScrapDetail(rr_rider, ss_course);
	}
	public List<Course> getRidingRecordAllItem(String rr_num) throws Exception {
		return androidMapper.getRidingRecordAllItem(rr_num);
	}
	
	//경쟁전가져오기
	public List<Competition> getCompetition() throws Exception{
		return androidMapper.getCompetition();
	}
	
	//경쟁전 참가내역 가져오기
	public List<String> getjoinedComp(String rr_rider) throws Exception{
		return androidMapper.getjoinedComp(rr_rider);
	}
	
	public String getCompCourse(int c_num) throws Exception {
		return androidMapper.getCompCourse(c_num);
	}
	
	public List<CompClub> getCompClub(int cs_comp) throws Exception {
		return androidMapper.getCompClub(cs_comp);
	}
	
	public List<Badge> getCompBadge(int comp_badge) throws Exception {
		return androidMapper.getCompBadge(comp_badge);
	}
	
	public List<CompScore> getCompScore(int comp_num) throws Exception {
		return androidMapper.getCompScore(comp_num);
	}
	
	public List<Course> delScrap(String ss_rnum) throws Exception {
		return androidMapper.delScrap(ss_rnum);
	}
	public String likeput(Like_Status likestatus) throws Exception {
		return androidMapper.likeput(likestatus);
		
	}
	public List<DangerousArea> getDanger() throws Exception {
		// TODO Auto-generated method stub
		return androidMapper.getDanger();
	}
	
	//경쟁전 현황 점수 변경
	public void updateCompScore(int avg_speed, int rr_comp, int r_club, String LoginId) throws Exception{
		androidMapper.updateCompScore(avg_speed, rr_comp, r_club, LoginId);
	}
	
	//경쟁전 모든 점수 가져오기
	public List<Competition_Status> getCompAllScore(int comp_num) throws Exception{
		return androidMapper.getCompAllScore(comp_num);
	}
	
	public void updateRank(int rr_comp, int r_club, int score, int rank) throws Exception{
		androidMapper.updateRank(rr_comp, r_club, score, rank);
	}
	
	public List<Mission_Status> getMissionStatus(String LoginId) throws Exception{
		return androidMapper.getMissionStatus(LoginId);
	}
	
	public List<Mission> getNoClearMission(String LoginId) throws Exception{
		return androidMapper.getNoClearMission(LoginId);
	}
	
	public void updateMissionStatus ( String LoginId, int typeScore1, int typeScore2 ) throws Exception{
		androidMapper.updateMissionStatus(LoginId, typeScore1, typeScore2);
	}
	
	public void insertMissionCom ( String LoginId, int mc_mission, Timestamp mc_time ) throws Exception {
		androidMapper.insertMissionCom(LoginId, mc_mission, mc_time);
	}
}
