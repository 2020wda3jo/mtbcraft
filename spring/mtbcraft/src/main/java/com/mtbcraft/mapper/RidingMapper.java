package com.mtbcraft.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.DangerousArea;
import com.mtbcraft.dto.RidingRecord;

@Repository("com.mtbcraft.mapper.RidingMapper")
public interface RidingMapper {
	//로그인 후 페이지 구성을 위한 최신 라이딩 기록 조회
	public List<RidingRecord> getRidingRecordTop3(String rr_rider) throws Exception;
	
	//사용자 라이딩 기록 조회
	public List<RidingRecord> getRidingRecord(String rr_rider) throws Exception;
	
	//라이딩 넘버로 GPX파일명 조회
	public String getGpxFileByRR_Num(int rr_num) throws Exception;
	
	//라이딩 넘버로 라이딩기록 조회
	public RidingRecord getRidingRecordDetail(int rr_num) throws Exception;
	
	//라이딩 기록 공개/비공개 전환
	public void updateRidingRecord(@Param("rr_num")int rr_num, @Param("rr_open") int rr_open) throws Exception;
	
	//등록된 위험 지역 조회
	public List<DangerousArea> getDangerousArea() throws Exception;
	
	//사용자가 등록한 위험 지역 조회
	public List<DangerousArea> getUserDangerousArea(String rr_rider) throws Exception;
	
	// 위험지역 등록 신청
	public void postDangerousArea(DangerousArea da) throws Exception;
	
	//현재는 주행기록을 조회하기에 최종결정시에 수정필요함
	//등록된 코스 조회
	public List<RidingRecord> getCourses() throws Exception;
	
	//사용자 스크랩 코스 조회
	public List<Course> getScrapCourse(String rr_rider) throws Exception;
}