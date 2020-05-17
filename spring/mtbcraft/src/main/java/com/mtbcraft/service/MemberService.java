package com.mtbcraft.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.DangerousArea;
import com.mtbcraft.dto.Login;
import com.mtbcraft.dto.Member;
import com.mtbcraft.dto.RidingRecord;
import com.mtbcraft.mapper.MemberMapper;


@Service
@Transactional
public class MemberService {
	@Resource(name="com.mtbcraft.mapper")
	@Autowired
	private MemberMapper memberMapper;
	
	
	public String memberInsert(Member member) throws Exception {
		return memberMapper.memberInsert(member);
	}
	
	public String memberLogin(Login login) throws Exception {
		return memberMapper.memberLogin(login);
	}
	
	public List<RidingRecord> getRidingRecord(String rr_rider) throws Exception {
		return memberMapper.getRidingRecord(rr_rider);
	}
	
	public List<Course> getCourse() throws Exception{
		return memberMapper.getCourse();
	}
	
	public List<Course> getScrapCourse(String rr_rider) throws Exception{
		return memberMapper.getScrapCourse(rr_rider);
	}
	
	public List<DangerousArea> getDangerousArea() throws Exception{
		return memberMapper.getDangerousArea();
	}
	
	public List<DangerousArea> getUserDangerousArea(String rr_rider) throws Exception{
		return memberMapper.getUserDangerousArea(rr_rider);
	}
}
