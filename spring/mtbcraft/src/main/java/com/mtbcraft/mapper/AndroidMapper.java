package com.mtbcraft.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mtbcraft.dto.AnLogin;
import com.mtbcraft.dto.App_RidingRecord;
import com.mtbcraft.dto.Competition;
import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.Login;
import com.mtbcraft.dto.RidingRecord;
import com.mtbcraft.dto.Scrap_Status;

@Repository("com.mtbcraft.mapper.AndroidMapper")
public interface AndroidMapper {

	//안드로이드 로그인
	public List<AnLogin> LoginProcess(AnLogin login) throws Exception;
	
	//안드로이드 앱에서 라이딩 기록 저장
	public String insertRecord(RidingRecord record) throws Exception;

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
}
