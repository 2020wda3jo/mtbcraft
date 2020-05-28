package com.mtbcraft.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtbcraft.mapper.AndroidMapper;
import com.mtbcraft.dto.Login;
import com.mtbcraft.dto.RidingRecord;


@Service
@Transactional
public class AndroidService {

	@Autowired
	private AndroidMapper androidMapper;
	
	//로그인
	public String login(Login login) throws Exception{
		return androidMapper.login(login);
	}
	//라이딩기록 저장
	public String insertRecord(RidingRecord record) throws Exception{
		return androidMapper.insertRecord(record);
	}
	
	//라이딩기록 보기
	public String readRecord(String rr_rider) throws Exception{
		return androidMapper.readRecord(rr_rider);
	}
	

}
