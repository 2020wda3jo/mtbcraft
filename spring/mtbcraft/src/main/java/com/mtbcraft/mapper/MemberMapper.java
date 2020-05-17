package com.mtbcraft.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.DangerousArea;
import com.mtbcraft.dto.Login;
import com.mtbcraft.dto.Member;
import com.mtbcraft.dto.RidingRecord;

@Repository("com.mtbcraft.mapper")
public interface MemberMapper {

//회원가입 작성
	public String memberInsert(Member member) throws Exception;
	public String memberLogin(Login login) throws Exception;
	
	//라이딩기록검색
	public List<RidingRecord> getRidingRecord(String rr_rider) throws Exception;
	
	//등록된 코스 조회
	public List<Course> getCourse() throws Exception;
	
	//사용자 스크랩 코스 조회
	public List<Course> getScrapCourse(String rr_rider) throws Exception;
	
	//등록된 위험 지역 조회
	public List<DangerousArea> getDangerousArea() throws Exception;

	//사용자가 등록한 스크랩 코스 조회
	public List<DangerousArea> getUserDangerousArea(String rr_rider) throws Exception;

}
