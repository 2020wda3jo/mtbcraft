package com.mtbcraft.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.DangerousArea;
import com.mtbcraft.dto.Gpx;
import com.mtbcraft.dto.No_Danger;
import com.mtbcraft.dto.RidingRecord;
import com.mtbcraft.dto.Scrap_Status;
import com.mtbcraft.service.MemberService;
import com.mtbcraft.service.RidingService;
@Controller
public class RidingController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private RidingService ridingService;

	// 코스 메뉴 진입
	@RequestMapping(value = "/riding/course")
	public String course(String rider, Model model) throws Exception {
		List<RidingRecord> rrlist = memberService.getRidingRecord(rider);
		for(int i=0;i<rrlist.size();i++) {
			rrlist.get(i).setRr_gpx(rrlist.get(i).getRr_dateYYYYMMDD());
		}//course.html에서 사용되지않는 gpx변수를 원하는 문자열을 표현하기 위해 사용
		List<Course> scraplist = memberService.getScrapCourse(rider);
		model.addAttribute("ridingrecords", rrlist);
		model.addAttribute("scrapcourses", scraplist);
		return "riding/course";
	}
	
	// 메인화면 구성을 위한 최신주행기록 3개 조회
	@RequestMapping(value="/ridingrecordTop3", method=RequestMethod.GET)
	@ResponseBody
	public List<RidingRecord> getRidingRecordTop3(String rr_rider) throws Exception{
		List<RidingRecord> list = ridingService.getRidingRecordTop3(rr_rider);
		return list;
	}

	// 사용자 주행 기록 조회
	@RequestMapping(value = "/riding/check", method = RequestMethod.GET)
	public @ResponseBody List<RidingRecord> getRidingRecord(String rr_rider) throws Exception {
		return memberService.getRidingRecord(rr_rider);
	}
	// RR_NUM으로 GPX파일 조회
	@RequestMapping(value="/getGpxFileByRR_Num", method = RequestMethod.GET)
	@ResponseBody
	public Gpx getMyGPX(int rr_num) throws Exception {
		String gpxFile = ridingService.getGpxFileByRR_Num(rr_num);
		String path = "/home/ec2-user/data/gpx/"+gpxFile;
		File file = new File(path);
		String txt = "";
		FileInputStream fis = new FileInputStream(file); 
		while(true) { 
			int res = fis.read(); 
			if(res<0) { 
				break; 
			}else { 
				txt += ((char)res);
			}
		}
		fis.close();
		Gpx gpx = new Gpx();
		gpx.setting(txt);
		return gpx;
	}
	//RR_NUM으로 RIDINGRECORD 조회
	@RequestMapping(value="/getRidingRecordByRR_Num", method = RequestMethod.GET)
	@ResponseBody
	public RidingRecord getRidingRecordByRR_Num(int rr_num) throws Exception {
		return ridingService.getRidingRecordDetail(rr_num);
	}

	// 사용자 주행 기록 공개비공개
	@RequestMapping(value = "/riding/update", method = RequestMethod.GET)
	public String updateRidingRecord(int rr_num, int rr_open) throws Exception {
		System.out.print(rr_num + "의 현재 OPEN 상태 " + rr_open + "에서");
		rr_open = rr_open == 1 ? 0 : 1;
		System.out.print(rr_open + "상태로 전환합니다.\n");
		memberService.updateRidingRecord(rr_num, rr_open);
		return "/riding/course";
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

	// 사용자 스크랩 코스 등록
	@RequestMapping(value = "/riding/scrap/check", method = RequestMethod.POST)
	@ResponseBody
	public String postScrapCourse(@RequestBody Scrap_Status ss) {
		String ss_rider = ss.getSs_rider();
		int ss_course = ss.getSs_course();
		try {
			memberService.postScrapCourse(ss_rider, ss_course);
			System.out.println("사용자 " + ss_rider + "이 " + ss_course + "코스를 스크랩합니다.");
			return "success";
		} catch (Exception e) {
			System.out.println("사용자 " + ss_rider + "은 이미" + ss_course + "코스를 스크랩하였습니다.");
			return "fail";
		}
	}

	// 사용자 스크랩 코스 삭제
	@RequestMapping(value = "/riding/scrap/delete", method = RequestMethod.GET)
	public String deleteScrapCourse(String ss_rider, int ss_course) throws Exception {
		System.out.println("사용자 " + ss_rider + "의 스크랩코스 " + ss_course + "를 삭제합니다");
		memberService.deleteScrapCourse(ss_rider, ss_course);
		return "/riding/course";
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
	// 위험지역 등록 신청
	@RequestMapping(value = "/riding/DA/post", method = RequestMethod.POST)
	public @ResponseBody String postDangerousArea(@RequestBody DangerousArea da) throws Exception {
		System.out.println(da.toString());
		memberService.postDangerousArea(da);
		return "success";
	}
	// 사용자 등록 위험 지역 삭제
	@RequestMapping(value = "/riding/DA/delete", method = RequestMethod.DELETE)
	public @ResponseBody String deleteDangerousArea(@RequestBody DangerousArea da) throws Exception {
		System.out.println(da.toString());
		memberService.deleteDangerousArea(da.getDa_num());
		return "success";
	}
	// 다른 사용자 등록 위험 지역 해지 신청
	@RequestMapping(value = "/riding/DA/delete", method = RequestMethod.POST)
	public @ResponseBody String postNO_DangerousArea(@RequestBody No_Danger nd) throws Exception {
		System.out.println(nd.getNd_num()+"/"+nd.getNd_rider()+"/"+nd.getNd_content()+"/"+nd.getNd_image());
		try {
			memberService.postNoDanger(nd);
			return "success";
		} catch (Exception e) {
			return "fail";
		}
		
		
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

	// 인원모집
	@RequestMapping(value = "/riding/comeon", method = RequestMethod.GET)
	public String comeon() {

		return "/riding/comeon";
	}
}
