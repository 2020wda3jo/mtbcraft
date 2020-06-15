package com.mtbcraft.mapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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

@Repository("com.mtbcraft.mapper.AndroidMapper")
public interface AndroidMapper {

	//안드로이드 로그인
	public List<AnLogin> LoginProcess(AnLogin login) throws Exception;
	
	//안드로이드 유저 정보 가져오기
	public LoginInfo getLoginInfo(String LoginId) throws Exception;
	
	//안드로이드 앱에서 라이딩 기록 저장
	public String insertRecord(RidingRecord record) throws Exception;
	
	//안드로이드 앱에서 라이딩 기록 저장 ( 경쟁전 포함 )
	public String insertRecordWithComp(RidingRecord record) throws Exception;

	//주행기록 전체보기
	public List<RidingRecord> getRidingRecordAll();
		
	//안드로이드 앱에서 라이딩 기록 리스트 가져오기
	public List<App_RidingRecord> readRecord(String rr_rider);
	
	// 사용자 라이딩 기록 검색
	public List<RidingRecord> getRidingRecordDetail(@Param("rr_rider") String rr_rider, @Param("rr_num") String rr_num) throws Exception;
	
	
	//코스 가져오기
	public List<App_RidingRecord> getCourse() throws Exception;
		
	//코스 상세보기
	public List<Course> getCourseItem(String c_num) throws Exception;
	
	//코스 스크랩
	public String insertScrap(Scrap_Status scrap) throws Exception;
	
	//스크랩 코스 보기
	public List<Scrap_Status> getScrap(String rr_rider) throws Exception;
	
	//스크랩 코스 상세보기
	public List<Course> getScrapDetail(String rr_rider, String ss_course) throws Exception;

	public List<Course> getRidingRecordAllItem(String rr_num) throws Exception;

	//경쟁전 가져오기
	public List<Competition> getCompetition() throws Exception;
	
	//경쟁전 참가 내역 가져오기
	public List<String> getjoinedComp(String rr_rider) throws Exception;
	
	//경쟁전 코스 가져오기
	public String getCompCourse(int c_num) throws Exception;
	
	//경쟁전 클럽 순위 가져오기
	public List<CompClub> getCompClub(int cs_comp) throws Exception;
	
	//경쟁전 뱃지 가져오기
	public List<Badge> getCompBadge(int comp_badge) throws Exception;
	
	//경쟁전 개인 점수 가져오기
	public List<CompScore> getCompScore(int comp_num) throws Exception;
	
	public String RidingOpenSet(RidingRecord record) throws Exception;

	public List<Course> delScrap(String ss_rnum) throws Exception;

	//좋아요
	public String likeput(Like_Status likestatus) throws Exception;

	public List<DangerousArea> getDanger() throws Exception;
	
	//경쟁전 점수 업데이트
	public void updateCompScore( @Param("avg_speed") int avg_speed, @Param("rr_comp") int rr_comp,
								@Param("r_club") int r_club, @Param("LoginId") String LoginId) throws Exception;
	
	//경쟁전 모든 점수 가져오기
	public List<Competition_Status> getCompAllScore(int comp_num) throws Exception;
	
	public void updateRank( @Param("rr_comp") int rr_comp, @Param("r_club") int r_club,
							@Param("score") int score, @Param("rank") int rank) throws Exception;
	
	public List<Mission_Status> getMissionStatus(String LoginId) throws Exception;
	
	public List<Mission> getNoClearMission (String LoginId) throws Exception;
	
	public void updateMissionStatus ( @Param("LoginId") String LoginId, @Param("typeScore1") int typeScore1,
										@Param("typeScore2") int typeScore2) throws Exception;
	
	public void insertMissionCom ( @Param("LoginId") String LoginId, @Param("mc_mission") int mc_mission, @Param("mc_time") Timestamp mc_time ) throws Exception;
}
