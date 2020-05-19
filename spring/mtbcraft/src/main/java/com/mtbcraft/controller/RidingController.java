package com.mtbcraft.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.DangerousArea;
import com.mtbcraft.dto.RidingRecord;
import com.mtbcraft.dto.VisionDto;
import com.mtbcraft.service.MemberService;

import java.sql.Timestamp;
@Controller
public class RidingController {
	@Autowired
	private MemberService memberService;

	// 코스 메뉴 진입
	@RequestMapping("/riding/course")
	public String course() {
		return "/riding/course";
	}

	// 사용자 주행 기록 조회
	@RequestMapping(value = "/riding/check", method = RequestMethod.GET)
	public @ResponseBody List<RidingRecord> getRidingRecord(String rr_rider) throws Exception {
		return memberService.getRidingRecord(rr_rider);
	}
	
	//주행기록 등록(안드로이드)
	
	
	@RequestMapping(value = "/api/upload")
	@ResponseBody
	public Map<String, String> insertriding(HttpServletRequest request) throws Exception {
		
		//안드로이드로부터 받은 데이터
		System.out.println(request.getParameter("title"));
        System.out.println(request.getParameter("memo"));
        
        //안드로이드에게 전달하는 데이터
        Map<String, String> result = new HashMap<String, String>();
        result.put("data1", "된다?");
        result.put("data2", "고롷췌");
        
        return result;

	}

	// 코스 조회
	@RequestMapping(value = "/riding/course/check", method = RequestMethod.GET)
	public @ResponseBody List<Course> getCourse() throws Exception {
		return memberService.getCourse();
	}

	// 사용자 스크랩 코스 조회
	@RequestMapping(value = "/riding/scrap/check", method = RequestMethod.GET)
	public @ResponseBody List<Course> getScrapCourse(String rr_rider) throws Exception {
		return memberService.getScrapCourse(rr_rider);
	}

	// 위험 지역 조회
	@RequestMapping(value = "/riding/DA/checkA", method = RequestMethod.GET)
	public @ResponseBody List<DangerousArea> getDangerousArea() throws Exception {
		return memberService.getDangerousArea();
	}

	// 사용자 등록 위험 지역 조회
	@RequestMapping(value = "/riding/DA/check", method = RequestMethod.GET)
	public @ResponseBody List<DangerousArea> getUserDangerousArea(String rr_rider) throws Exception {
		return memberService.getUserDangerousArea(rr_rider);
	}

	// 내 코스 공유
	@RequestMapping(value = "/riding/course/my/share", method = RequestMethod.GET)
	public String sharecourseget() {

		return "/riding/course/my/share";
	}

	// 내 코스 리뷰
	@RequestMapping(value = "/riding/course/my/review", method = RequestMethod.POST)
	public String reviewpost() {

		return "/riding/course/my/review";
	}

	// 내 코스 리뷰
	@RequestMapping(value = "/riding/myreview", method = RequestMethod.GET)
	public String myreviewget(int result, String courseId) {

		System.out.println(result);
		System.out.println(courseId);
		return "/riding/myreview";
	}

	// 내 코스 리뷰 POST
	@RequestMapping(value = "/riding/myreviewIn", method = RequestMethod.POST)
	public String myreviewpost(int result, String courseId, String courseImage, String userRecord) {

		System.out.println(result);
		System.out.println(courseId);
		System.out.println(courseImage);
		System.out.println(userRecord);
		return "/riding/myreviewIn";
	}

	//

	// 코스 검색
	@RequestMapping(value = "/riding/course/search", method = RequestMethod.GET)
	public String coursesearch(int result, String courseId, String courseImage) {
		System.out.println(result);
		System.out.println(courseId);
		System.out.println(courseImage);
		return "/riding/course/search";
	}

	// 코스 인원모집
	@RequestMapping(value = "/riding/course/member", method = RequestMethod.POST)
	public String coursesearch(int result, String courseId, String courseImage, String area, String time, String member,
			String content) {
		System.out.println(result);
		System.out.println(courseId);
		System.out.println(courseImage);
		System.out.println(area);
		System.out.println(time);
		System.out.println(member);
		System.out.println(content);
		return "/riding/course/member";
	}

	// 코스 스크랩
	@RequestMapping(value = "/riding/course/scrap", method = RequestMethod.POST)
	public String coursescrap(int result, String courseId, String courseImage) {
		System.out.println(result);
		System.out.println(courseId);
		System.out.println(courseImage);
		return "/riding/course/scrap";
	}

	// 인원모집
	@RequestMapping(value = "/riding/comeon", method = RequestMethod.GET)
	public String comeon() {

		return "/riding/comeon";
	}
}
