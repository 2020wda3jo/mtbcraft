package com.mtbcraft.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mtbcraft.dto.Badge;
import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.Course_Review;
import com.mtbcraft.dto.DangerousArea;
import com.mtbcraft.dto.Like_Status;
import com.mtbcraft.dto.RidingRecord;

@Repository("com.mtbcraft.mapper.MyPageMapper")
public interface MyPageMapper {
	//사용자 주행기록 조회
	public List<RidingRecord> getRR(String rider);
	
	//사용자 배지 조회
	public List<Badge> getBadge(String rider);
	
}
