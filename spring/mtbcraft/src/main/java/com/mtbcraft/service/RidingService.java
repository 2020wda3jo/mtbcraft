package com.mtbcraft.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtbcraft.dto.RidingRecord;
import com.mtbcraft.mapper.RidingMapper;

@Service
@Transactional
public class RidingService {
	@Resource(name="com.mtbcraft.mapper.RidingMapper")
	@Autowired
	private RidingMapper ridingMapper;
	
	//라이딩 넘버로 GPX파일명 조회
	public String getGpxFileByRR_Num(int rr_num) throws Exception{
		return ridingMapper.getGpxFileByRR_Num(rr_num);
	}
	
	//라이딩 넘버로 라이딩기록 조회
	public RidingRecord getRidingRecordDetail(int rr_num) throws Exception {
		return ridingMapper.getRidingRecordDetail(rr_num);
	}
}
