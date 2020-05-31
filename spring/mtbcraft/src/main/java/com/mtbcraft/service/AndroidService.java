package com.mtbcraft.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtbcraft.mapper.AndroidMapper;
import com.mtbcraft.dto.AnLogin;
import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.Login;
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
	//라이딩기록 저장
	public String insertRecord(RidingRecord record) throws Exception{
		return androidMapper.insertRecord(record);
	}
	
	//라이딩기록 보기
	public String readRecord(String rr_rider) throws Exception{
		return androidMapper.readRecord(rr_rider);
	}
	
	//코스가져오기
	public List<Course> getCourse() throws Exception{
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
	public List<Scrap_Status> getScrapDetail(String rr_rider, String ss_course) throws Exception {
		return androidMapper.getScrapDetail(rr_rider, ss_course);
	}

}
