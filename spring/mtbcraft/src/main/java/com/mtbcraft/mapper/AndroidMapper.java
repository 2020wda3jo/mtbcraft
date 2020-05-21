package com.mtbcraft.mapper;

import org.springframework.stereotype.Repository;

import com.mtbcraft.dto.RidingRecord;

@Repository("com.mtbcraft.mapper.AndroMapper")
public interface AndroidMapper {

	//안드로이드 앱에서 라이딩 기록 저장
	public String insertRecord(RidingRecord record) throws Exception;

	//안드로이드 앱에서 라이딩 기록 리스트 가져오기
	public String readRecord(String rr_rider);
	
	//안드로이드 앱에서 라이딩 기록 보기
}
