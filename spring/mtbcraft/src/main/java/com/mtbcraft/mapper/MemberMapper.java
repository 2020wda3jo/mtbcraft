package com.mtbcraft.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mtbcraft.dto.Login;
import com.mtbcraft.dto.Member;
import com.mtbcraft.dto.RidingRecord;

@Repository("com.mtbcraft.mapper")
public interface MemberMapper {

//회원가입 작성
	public String memberInsert(Member member) throws Exception;
	public String memberLogin(Login login) throws Exception;
	
	//라이딩기록검색 테스트
	public List<RidingRecord> getRidingRecord(String rr_rider) throws Exception;
}
