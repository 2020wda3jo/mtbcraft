package com.mtbcraft.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtbcraft.dto.Badge;
import com.mtbcraft.dto.Course_Review;
import com.mtbcraft.dto.DangerousArea;
import com.mtbcraft.dto.Like_Status;
import com.mtbcraft.dto.Rider;
import com.mtbcraft.dto.RidingRecord;
import com.mtbcraft.mapper.MyPageMapper;
import com.mtbcraft.mapper.RidingMapper;

@Service
@Transactional
public class MyPageService {
	@Resource(name="com.mtbcraft.mapper.MyPageMapper")
	@Autowired
	private MyPageMapper myPageMapper;
	
	//사용자 주행기록 조회
	public List<RidingRecord> getRR(String rider){
		return myPageMapper.getRR(rider);
	}
	
	//배지 조회
	public List<Badge> getBadge(String rider) throws Exception{
		return myPageMapper.getBadge(rider);
	}
	
	//사용자 정보 조회
	public Rider getUserInfo(String nickname) {
		return myPageMapper.getUserInfo(nickname);
	}
	
}
