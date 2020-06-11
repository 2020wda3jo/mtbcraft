package com.mtbcraft.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.Course_Review;
import com.mtbcraft.dto.DangerousArea;
import com.mtbcraft.dto.Gpx;
import com.mtbcraft.dto.Like_Status;
import com.mtbcraft.dto.No_Danger;
import com.mtbcraft.dto.RidingRecord;
import com.mtbcraft.dto.Scrap_Status;
import com.mtbcraft.service.MemberService;
import com.mtbcraft.service.RidingService;
@Controller
public class RidingController {
	@Autowired
	private RidingService ridingService;

	// 코스 메뉴 진입
	@RequestMapping(value = "/riding/course")
	public String course(String rider, Model model) throws Exception {
		List<RidingRecord> rrlist = ridingService.getRidingRecord(rider);
		for(int i=0;i<rrlist.size();i++) {
			rrlist.get(i).setRr_gpx(rrlist.get(i).getRr_dateYYYYMMDD());
			int like = ridingService.getRR_Like(rrlist.get(i).getRr_num());
			rrlist.get(i).setRr_like(like);
		}//course.html에서 사용되지않는 gpx변수를 원하는 문자열을 표현하기 위해 사용
		List<RidingRecord> scraplist = ridingService.getScrapCourse(rider);
		for(int i=0;i<scraplist.size();i++) {
			scraplist.get(i).setRr_gpx(scraplist.get(i).getRr_dateYYYYMMDD());
			int like = ridingService.getRR_Like(scraplist.get(i).getRr_num());
			scraplist.get(i).setRr_like(like);
		}
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

	// RR_NUM으로 GPX파일 조회
		@RequestMapping(value="/getGpxByRR_Num", method = RequestMethod.GET)
		@ResponseBody
		public Gpx getGpxByRR_Num(int rr_num) throws Exception {
			String gpxFile = ridingService.getGpxFileByRR_Num(rr_num);
			Gpx gpx = new Gpx();
			makeGpx(gpx, gpxFile);
			gpx.setRr_num(rr_num);
			return gpx;
		}
	
	//RR_NUM으로 RIDINGRECORD 조회
	@RequestMapping(value="/getRidingRecordByRR_Num", method = RequestMethod.GET)
	@ResponseBody
	public RidingRecord getRidingRecordByRR_Num(int rr_num) throws Exception {
		System.out.println(rr_num);
		RidingRecord rr = ridingService.getRidingRecordDetail(rr_num);
		int like = ridingService.getRR_Like(rr_num);
		rr.setRr_like(like);
		return rr;
	}

	// 사용자 주행 기록 공개비공개
	@RequestMapping(value = "/riding/update", method = RequestMethod.PUT)
	@ResponseBody
	public String updateRidingRecord(int rr_num, int rr_open){
		try {
			ridingService.updateRidingRecord(rr_num, rr_open);
		} catch (Exception e) {
			return "fail";
		}
		
		if(rr_open==1) {
			return "y";
		}else {
			return "n";
		}
	}
	
	// 주행 기록 코스 명 변경
	@RequestMapping(value = "/ridingrecord/rr_name", method = RequestMethod.PUT)
	@ResponseBody
	public String updateRidingRecordName(int rr_num, String rr_name) throws Exception{
		ridingService.updateRidingRecordName(rr_num, rr_name);
		return "success";
	}
	
	// 코스 조회
	@RequestMapping(value = "/riding/course/list", method = RequestMethod.GET)
	public @ResponseBody List<RidingRecord> getCourse() throws Exception {
		return ridingService.getCourses(); 
	}

	// 사용자 스크랩 코스 조회
	@RequestMapping(value = "/riding/scrap", method = RequestMethod.GET)
	public @ResponseBody List<RidingRecord> getScrapCourse(String rr_rider) throws Exception {
		return ridingService.getScrapCourse(rr_rider);
	}

	// 사용자 스크랩 코스 등록
	@RequestMapping(value = "/riding/scrap/{ss_rnum}/{ss_rider}", method = RequestMethod.POST)
	@ResponseBody
	public String postScrapCourse(@PathVariable int ss_rnum, @PathVariable String ss_rider) {
		try {
			List<RidingRecord> list = ridingService.getRidingRecord(ss_rider);
			for(int i=0;i<list.size();i++) {
				if(ss_rnum==list.get(i).getRr_num()) {
					return "failMyRR";
				}
			}
		} catch (Exception e1) {}
		try {
			ridingService.postScrapCourse(ss_rider, ss_rnum);
			return "success";
		} catch (Exception e) {
			return "fail";
		}
	}

	// 사용자 스크랩 코스 삭제
	@RequestMapping(value = "/riding/scrap", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteScrapCourse(String ss_rider, int ss_rnum) throws Exception {
		
		ridingService.deleteScrapCourse(ss_rider, ss_rnum);
		 
		return "success";
	}

	// 위험 지역 조회
	@RequestMapping(value = "/riding/DA", method = RequestMethod.GET)
	public @ResponseBody List<DangerousArea> getDangerousArea() throws Exception {
		return ridingService.getDangerousArea();
	}

	// 사용자 등록 위험 지역 조회
	@RequestMapping(value = "/riding/DA/check", method = RequestMethod.GET)
	public @ResponseBody List<DangerousArea> getUserDangerousArea(String rr_rider) throws Exception {
		return ridingService.getUserDangerousArea(rr_rider);
	}
	
	// 위험지역 등록 신청
	@RequestMapping(value = "/riding/DA", method = RequestMethod.POST)
	public String postDangerousArea(DangerousArea da, String page, Model model) throws Exception {
		ridingService.postDangerousArea(da);
		if(page.equals("search")) {
			return "riding/search";
		}else{
			List<RidingRecord> rrlist = ridingService.getRidingRecord(da.getDa_rider());
			for(int i=0;i<rrlist.size();i++) {
				rrlist.get(i).setRr_gpx(rrlist.get(i).getRr_dateYYYYMMDD());
			}//course.html에서 사용되지않는 gpx변수를 원하는 문자열을 표현하기 위해 사용
			List<RidingRecord> scraplist = ridingService.getScrapCourse(da.getDa_rider());
			model.addAttribute("ridingrecords", rrlist);
			model.addAttribute("scrapcourses", scraplist);
			return "riding/course";
		}
	}
		
	// 사용자 등록 위험 지역 삭제
	@RequestMapping(value = "/riding/DA/delete", method = RequestMethod.DELETE)
	public @ResponseBody String deleteDangerousArea(@RequestBody DangerousArea da) throws Exception {
		/*
		 * System.out.println(da.toString());
		 * memberService.deleteDangerousArea(da.getDa_num());
		 */
		return "success";
	}
	// 다른 사용자 등록 위험 지역 해지 신청
	@RequestMapping(value = "/riding/DA/delete", method = RequestMethod.POST)
	public @ResponseBody String postNO_DangerousArea(@RequestBody No_Danger nd) throws Exception {
		/*
		 * System.out.println(nd.getNd_num()+"/"+nd.getNd_rider()+"/"+nd.getNd_content()
		 * +"/"+nd.getNd_image()); try { memberService.postNoDanger(nd); return
		 * "success"; } catch (Exception e) { return "fail"; }
		 */
		return "success";
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

	// 라이딩 넘버로 코스 리뷰 조회
	@RequestMapping(value = "/riding/reviews/{cr_rnum}", method = RequestMethod.GET)
	@ResponseBody
	public List<Course_Review> getReviews(@PathVariable int cr_rnum) throws Exception {

		return ridingService.getCourseReviews(cr_rnum);
	}
		
	// 코스 리뷰 등록
	@RequestMapping(value = "/riding/review", method = RequestMethod.POST)
	@ResponseBody
	public String myreviewpost(@RequestPart MultipartFile files, Course_Review cr ) throws Exception {
		
		if(!files.isEmpty()) {
			String filename = files.getOriginalFilename();
	        String directory = "/home/ec2-user/data/review/";
	        //String directory = "C:\\Users\\TACK\\Desktop\\study\\review\\";
	        String filepath = Paths.get(directory, filename).toString();             
	        
	        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
	        stream.write(files.getBytes());
	        stream.close();
	        
	        cr.setCr_images(filename);
		}
		// 현재날짜 timestamp
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// formatter.setTimeZone(TimeZone.getTimeZone("GMT+09"));
		Calendar cal = Calendar.getInstance();
		String today = null;
		today = formatter.format(cal.getTime());
		Timestamp ts = Timestamp.valueOf(today);
		
		cr.setCr_time(ts);
		
		ridingService.postCourseReview(cr);
		return "success";
		
	}

	// 코스 리뷰 삭제
	@RequestMapping(value = "/riding/review", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteCourseReview(int cr_num) throws Exception {
		ridingService.deleteCourseReview(cr_num);
		return "success";
	}
	
	//리뷰 수정
	@RequestMapping(value = "/riding/review", method = RequestMethod.PUT)
	@ResponseBody
	public String updateCourseReview(int cr_num, String cr_content) throws Exception {
		ridingService.updateCourseReview(cr_num, cr_content);
		return "success";
	}

	// 코스 검색 진입
	@RequestMapping(value = "/riding/search", method = RequestMethod.GET)
	public String coursesearch() {
		return "riding/search";
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
	
	//코스 추천
	@RequestMapping(value = "/riding/like", method = RequestMethod.POST)
	@ResponseBody
	public String postLS(Like_Status ls) {
		System.out.println(ls.getLs_rider());
		System.out.println(ls.getLs_rnum());
		try {
			
			List<RidingRecord> list = ridingService.getRidingRecord(ls.getLs_rider());
			
			for(int i=0;i<list.size();i++) {
				if(list.get(i).getRr_num()==ls.getLs_rnum()) {
					return "myrr";
				}
			}
			
			ridingService.postLS(ls);
			
		} catch (Exception e) {
			return "already";
		}
		
		return "success";
	}
	
	//코스 추천 취소
	@RequestMapping(value = "/riding/like", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteLS(Like_Status ls) {
		
		ridingService.deleteLS(ls);
		
		return "success";
	}
	
	private void makeGpx(Gpx gpx, String gpxFile) throws Exception {
		String path = "/home/ec2-user/data/gpx/"+gpxFile;
		//String path = "C:\\Users\\TACK\\Desktop\\study\\"+gpxFile;
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
		gpx.setting(txt);
	}
	
	//이미지 로딩
	@GetMapping(value = "/image/review/{imageFile}")
	public @ResponseBody byte[] getImage(@PathVariable String imageFile) throws IOException {
		InputStream in = null;
	    in = new  BufferedInputStream(new FileInputStream("/home/ec2-user/data/review/"+imageFile)); 
	    //in = new  BufferedInputStream(new FileInputStream("C:\\Users\\TACK\\Desktop\\study\\review\\"+imageFile)); 
	    return IOUtils.toByteArray(in);
	}
		
	//이미지 업로드
	private void uploadImage(MultipartFile uploadfile) throws IOException {
		String filename = uploadfile.getOriginalFilename();
		String directory = "/home/ec2-user/data/review";
		//String directory = "C:\\Users\\TACK\\Desktop\\study\\review";
		String filepath = Paths.get(directory, filename).toString();
		
		// Save the file locally
		BufferedOutputStream stream =
				new BufferedOutputStream(new FileOutputStream(new File(filepath)));
		stream.write(uploadfile.getBytes());
		stream.close();
	}
}
