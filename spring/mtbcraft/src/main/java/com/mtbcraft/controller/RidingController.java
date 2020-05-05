package com.mtbcraft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RidingController {
	//코스 메뉴
	@RequestMapping("/riding/course")
	public String coursepost() {
		return "/riding/course";
	}
	
	//내 코스 공유
	@RequestMapping(value="/riding/course/my/share", method=RequestMethod.GET)
	public String sharecourseget() {
		
		return "/riding/course/my/share";
	}
	
	//내 코스 리뷰
	@RequestMapping(value="/riding/course/my/review", method=RequestMethod.POST)
	public String reviewpost() {
		
		return "/riding/course/my/review";
	}
	
	//내 코스 리뷰
	@RequestMapping(value="/riding/myreview", method=RequestMethod.GET)
	public String myreviewget(int result, String courseId) {
		
		System.out.println(result);
		System.out.println(courseId);
		return "/riding/myreview";
	}
	//내 코스 리뷰 POST
	@RequestMapping(value="/riding/myreviewIn", method=RequestMethod.POST)
	public String myreviewpost(int result, String courseId, String courseImage, String userRecord) {
			
		System.out.println(result);
		System.out.println(courseId);
		System.out.println(courseImage);
		System.out.println(userRecord);
		return "/riding/myreviewIn";
	}
		
		//
		
	//코스 검색
	@RequestMapping(value="/riding/course/search", method=RequestMethod.GET)
	public String coursesearch(int result, String courseId, String courseImage) {
		System.out.println(result);
		System.out.println(courseId);
		System.out.println(courseImage);
		return "/riding/course/search";
	}
		
	//코스 인원모집
	@RequestMapping(value="/riding/course/member", method=RequestMethod.POST)
	public String coursesearch(int result, String courseId, String courseImage, String area, String time, String member, String content) {
		System.out.println(result);
		System.out.println(courseId);
		System.out.println(courseImage);
		System.out.println(area);
		System.out.println(time);
		System.out.println(member);
		System.out.println(content);
		return "/riding/course/member";
	}
				
	//코스 스크랩
	@RequestMapping(value="/riding/course/scrap", method=RequestMethod.POST)
	public String coursescrap(int result, String courseId, String courseImage) {
		System.out.println(result);
		System.out.println(courseId);
		System.out.println(courseImage);
		return "/riding/course/scrap";
	}
	
	//인원모집
	@RequestMapping(value="/riding/comeon", method=RequestMethod.GET)
	public String comeon() {
		
		return "/riding/comeon";
	}
}
