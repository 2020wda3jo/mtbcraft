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
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mtbcraft.dto.Badge;
import com.mtbcraft.dto.CompIng;
import com.mtbcraft.dto.Competition;
import com.mtbcraft.dto.Mission;
import com.mtbcraft.dto.MissionComplete;
import com.mtbcraft.service.EntertainmentService;
import com.mtbcraft.service.MemberService;

@Controller
public class EntertainController {

	@Autowired
	
	private EntertainmentService entertainmentService;

	// 경쟁전
	@RequestMapping("/entertainment/competitions")
	public String competitions() {
		return "entertainment/competitions";
	}

	//경쟁전 기록 페이지
	@RequestMapping("/entertainment/history")
	public String history() {
		return "entertainment/history";
	}

	// 경쟁전 기록보기
	@RequestMapping("/entertainment/history/test")
	public @ResponseBody List<Competition> getCompetition() throws Exception {
		return entertainmentService.getCompetition();
	}

	// 미션 페이지
	@RequestMapping("/entertainment/missions")
	public String missions() {
		return "entertainment/missions";
	}

	// 미션 데이터
	@RequestMapping("/entertainment/missions/test")
	public @ResponseBody List<Mission> getMission() throws Exception {
		return entertainmentService.getMission();
	}

	// 미션 완료확인 페이지
	@RequestMapping("/entertainment/missionComplete")
	public String missionComplete() {
		return "entertainment/missionComplete";
	}

	// 미션 완료여부 조회 데이터
	@RequestMapping("/entertainment/missions/test2")
	public @ResponseBody List<MissionComplete> getMissionComplete(String mc_rider) throws Exception {
		return entertainmentService.getMissionComplete(mc_rider);
	}



//	// 진행중인 경쟁전 데이터
//	@RequestMapping("/entertainment/compIng/test")
//	public @ResponseBody List<CompIng> getCompIng() throws Exception {
//		return entertainmentService.getCompIng();
//	}
	
	// 진행중인 경쟁전 데이터
	@RequestMapping(value = "/entertainment/compIng" , method = RequestMethod.GET)
	public String getCompIng(Model model) throws Exception {
		List<CompIng> list = entertainmentService.getCompIng();
		System.out.println(list.size());
		model.addAttribute("list", list);
		return "entertainment/compIng";
	}
	
	
	//경쟁전 상세보기 데이터
	@RequestMapping(value = "/entertainment/compDetail" , method = RequestMethod.GET)
	public String getCompDetail(Model model) throws Exception {
		List<CompIng> list = entertainmentService.getCompIng();
		model.addAttribute("list", list);
//		switch (list.size()) {
//		case 0: 
//					
//		}
		return "entertainment/compDetail";
	}

	// 미션 등록페이지
	@RequestMapping("/entertainment/missionUpload")
	public String missionUpload() {
		return "entertainment/missionUpload";
	}

	// 미션 등록
	@RequestMapping(value = "/entertainment/missionUpload", method = RequestMethod.POST)
	public String fileUpload(Mission mission) throws Exception {
		entertainmentService.missionUpload(mission);
		return "entertainment/success";

	}

	// 배지 조회페이지
	@RequestMapping("/entertainment/badge")
	public String badge() {
		return "entertainment/badge";
	}

	// 등록된 배지
	@RequestMapping("/entertainment/badge/test")
	public @ResponseBody List<Badge> getBadge() throws Exception {
		return entertainmentService.getBadge();
	}

	// 배지 등록 페이지
	@RequestMapping("/entertainment/badgeUpload")
	public String badgeUpload() {
		return "entertainment/badgeUpload";
	}

	// 배지 이미지, 값 등록
	@RequestMapping(value = "/entertainment/badgeUpload/test", method = RequestMethod.POST)
	public String badgeUpload(@RequestPart MultipartFile files, Badge badge) throws Exception {
		String filename = files.getOriginalFilename();
		String directory = "C:\\ServerFiles";
		String filepath = Paths.get(directory, filename).toString();
		badge.setBg_image(filename);
		entertainmentService.BadgeUpload(badge);

		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
		stream.write(files.getBytes());
		stream.close();

		return "entertainment/success";

	}
	
	// 외부 이미지 가져오기
	@GetMapping(value = "/imagetest")
	public @ResponseBody byte[] getImage() throws IOException {
		InputStream in = null;
		
		in = new BufferedInputStream(new FileInputStream("C:\\Users\\zfrzf\\Desktop\\badge.png"));
		return IOUtils.toByteArray(in);
	}

}
