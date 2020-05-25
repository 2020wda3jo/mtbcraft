package com.mtbcraft.controller;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mtbcraft.dto.RidingRecord;
import com.mtbcraft.service.AndroidService;
import com.mtbcraft.service.MemberService;

@Controller
public class AndroidController {

	@Autowired
	MemberService memberService;

	@Autowired
	AndroidService androidService;

	// 二쇳뻾湲곕줉 �벑濡�(�븞�뱶濡쒖씠�뱶)
	@RequestMapping(value = "/api/upload")
	@ResponseBody
	public Map<String, String> insertriding(HttpServletRequest request) throws Exception {
		RidingRecord record = new RidingRecord();

		// �쁽�옱�궇吏� timestamp
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// formatter.setTimeZone(TimeZone.getTimeZone("GMT+09"));
		Calendar cal = Calendar.getInstance();
		String today = null;
		today = formatter.format(cal.getTime());
		Timestamp ts = Timestamp.valueOf(today);
		record.setRr_rider(request.getParameter("rr_rider"));
		record.setRr_date(ts);
		record.setRr_distance(Integer.parseInt(request.getParameter("rr_distance")));
		record.setRr_topspeed(Integer.parseInt(request.getParameter("rr_topspeed")));
		record.setRr_avgspeed(Integer.parseInt(request.getParameter("rr_avgspeed")));
		record.setRr_high(Integer.parseInt(request.getParameter("rr_high")));
		record.setRr_gpx(request.getParameter("rr_gpx"));
		record.setRr_open(Integer.parseInt(request.getParameter("rr_open")));
		record.setRr_breaktime(Integer.parseInt(request.getParameter("rr_breaktime")));
		record.setRr_time(Integer.parseInt(request.getParameter("rr_time")));
		record.setRr_area(request.getParameter("rr_area"));

		androidService.insertRecord(record);
		// �븞�뱶濡쒖씠�뱶濡쒕��꽣 諛쏆� �뜲�씠�꽣
		System.out.println("rr_rider " + request.getParameter("rr_rider")); // �쉶�썝�븘�씠�뵒
		System.out.println("rr_distance " + request.getParameter("rr_distance")); // �삤�뒛�궇吏�
		System.out.println("rr_topspeed " + request.getParameter("rr_topspeed")); // �냼�슂�떆媛�
		System.out.println("rr_avgspeed " + request.getParameter("rr_avgspeed")); // �씠�룞嫄곕━
		System.out.println("rr_high " + request.getParameter("rr_high")); // 理쒕��냽�룄
		System.out.println("rr_gpx " + request.getParameter("rr_gpx")); // �룊洹좎냽�룄
		System.out.println("rr_open " + request.getParameter("rr_open")); // �쉷�뱷怨좊룄
		System.out.println("rr_breaktime " + request.getParameter("rr_breaktime")); // gpx
		System.out.println("rr_time " + request.getParameter("rr_time")); // 怨듦컻�뿬遺�
		System.out.println("rr_area " + request.getParameter("rr_area")); // �쉷�뱷怨좊룄

		// �븞�뱶濡쒖씠�뱶�뿉寃� �쟾�떖�븯�뒗 �뜲�씠�꽣
		Map<String, String> result = new HashMap<String, String>();
		result.put("data1", request.getParameter("rr_rider"));
		result.put("data2", request.getParameter("rr_distance"));
		result.put("data3", request.getParameter("rr_topspeed"));
		result.put("data4", request.getParameter("rr_avgspeed"));
		result.put("data5", request.getParameter("rr_high"));
		result.put("data6", request.getParameter("rr_gpx"));
		result.put("data7", request.getParameter("rr_open"));
		result.put("data8", request.getParameter("rr_breaktime"));
		result.put("data9", request.getParameter("rr_time"));
		result.put("data10", request.getParameter("rr_area"));

		return result;
	}

	// 二쇳뻾湲곕줉 媛��졇�삤湲�
	@RequestMapping(value = "/api/get/{rr_rider}")
	public @ResponseBody List<RidingRecord> getRidingRecord(@PathVariable String rr_rider) throws Exception {
		return memberService.getRidingRecord(rr_rider);
	}

	// 二쇳뻾湲곕줉 媛��졇�삤湲�
	@RequestMapping(value = { "/api/get/{rr_rider}/{rr_num}" })
	public @ResponseBody List<RidingRecord> getRidingRecordDetail(@PathVariable(value = "rr_rider") String rr_rider,
			@PathVariable(value = "rr_num") String rr_num) throws Exception {
		System.out.println(memberService.getRidingRecord(rr_rider));
		return memberService.getRidingRecordDetail(rr_rider, rr_num);
	}
	
	@RequestMapping(value="/androidUpload", method=RequestMethod.POST)
	public String upload(HttpServletRequest request, MultipartFile file1){

		try{
				
			String path = "";
			String fileName="";
				
			if(!file1.isEmpty()){ //첨부파일이 존재하면
				//첨부파일의 이름
				fileName=file1.getOriginalFilename();
				try{
					//디렉토리 생성
					new File(path + "/"+ fileName).mkdirs();
					//지정된 업로드 경로로 저장됨
					file1.transferTo(new File(path + "/"+ fileName));
					System.out.println(path);

						
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
				
				
		}catch(Exception e){
			e.printStackTrace();
		}
		return "redirect:/";
	}
}
