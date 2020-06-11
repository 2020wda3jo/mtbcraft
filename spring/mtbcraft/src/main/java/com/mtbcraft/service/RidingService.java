package com.mtbcraft.service;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.DangerousArea;
import com.mtbcraft.dto.RidingRecord;
import com.mtbcraft.mapper.RidingMapper;

@Service
@Transactional
public class RidingService {
	@Resource(name="com.mtbcraft.mapper.RidingMapper")
	@Autowired
	private RidingMapper ridingMapper;
	
	//로그인 후 페이지 구성을 위한 최신 라이딩 기록 조회
	public List<RidingRecord> getRidingRecordTop3(String rr_rider) throws Exception{
		return ridingMapper.getRidingRecordTop3(rr_rider);
	}
	
	//사용자 라이딩 기록 조회
	public List<RidingRecord> getRidingRecord(String rr_rider) throws Exception {
		return ridingMapper.getRidingRecord(rr_rider);
	}
	
	//라이딩 넘버로 GPX파일명 조회
	public String getGpxFileByRR_Num(int rr_num) throws Exception{
		return ridingMapper.getGpxFileByRR_Num(rr_num);
	}
	
	//라이딩 넘버로 라이딩기록 조회
	public RidingRecord getRidingRecordDetail(int rr_num) throws Exception {
		return ridingMapper.getRidingRecordDetail(rr_num);
	}
	
	//라이딩 기록 공개/비공개 전환
	public void updateRidingRecord(int rr_num, int rr_open) throws Exception{
		ridingMapper.updateRidingRecord(rr_num, rr_open);
	}
	
	//등록된 위험지역 조회
	public List<DangerousArea> getDangerousArea() throws Exception{
		return ridingMapper.getDangerousArea();
	}

	//사용자가 등록한 위험지역 조회
	public List<DangerousArea> getUserDangerousArea(String rr_rider) throws Exception{
		return ridingMapper.getUserDangerousArea(rr_rider);
	}
	
	//위험지역 등록
	public void postDangerousArea(DangerousArea da) throws Exception {
		ridingMapper.postDangerousArea(da);
	}
	
	//등록된 코스 조회
	public List<RidingRecord> getCourses() throws Exception{
		return ridingMapper.getCourses();
	}
	
	//코스스크랩 조회
	public List<Course> getScrapCourse(String rr_rider) throws Exception{
		return ridingMapper.getScrapCourse(rr_rider);
	}
<<<<<<< HEAD
=======
	
	// 사용자 스크랩 코스 추가
	public void postScrapCourse(@Param("ss_rider") String ss_rider, @Param("ss_rnum") int ss_rnum) {
		ridingMapper.postScrapCourse(ss_rider, ss_rnum);
	}
	
	// 사용자 스크랩 코스 삭제
	public void deleteScrapCourse(@Param("ss_rider") String ss_rider, @Param("ss_rnum") int ss_rnum) {
		ridingMapper.deleteScrapCourse(ss_rider, ss_rnum);
	}
	
	//라이딩 넘버로 리뷰 조회
	public List<Course_Review> getCourseReviews(int cr_rnum) throws Exception{
		return ridingMapper.getCourseReviews(cr_rnum);
	}
	
	//리뷰 등록
	public void postCourseReview(Course_Review cr) throws Exception{
		ridingMapper.postCourseReview(cr);
	}
	
	//리뷰 삭제
	public void deleteCourseReview(int cr_num) throws Exception{
		ridingMapper.deleteCourseReview(cr_num);
	}
	
	//리뷰 수정
	public void updateCourseReview(int cr_num, String cr_content) throws Exception{
		ridingMapper.updateCourseReview(cr_num, cr_content);
	}
<<<<<<< HEAD
>>>>>>> parent of 3b60609... 웹_ 코스 추천 기능 추가
=======
>>>>>>> parent of 3b60609... 웹_ 코스 추천 기능 추가
}
