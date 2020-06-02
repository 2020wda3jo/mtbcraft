package com.mtbcraft.service;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
	
	//위험지역 등록
	public void postDangerousArea(DangerousArea da) throws Exception {
		ridingMapper.postDangerousArea(da);
	}
}
