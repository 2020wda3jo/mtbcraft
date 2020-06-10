package com.mtbcraft.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mtbcraft.dto.Club;
import com.mtbcraft.dto.Club_Join;
import com.mtbcraft.service.CommunityService;

@Controller
public class CommunityController {
	@Autowired
	private CommunityService communityService;
	
	// 커뮤니티
	@RequestMapping(value = "/community", method = RequestMethod.GET)
	public String comunity() {
		return "/community";
	}

	// 커뮤니티 클럽
	@RequestMapping(value = "/community/club", method = RequestMethod.GET)
	public String comunityclub() {
		
		return "/community/club/club";
	}
	
	// 커뮤니티 클럽 만들기 페이지
	@RequestMapping(value = "/community/club/create", method = RequestMethod.GET)
	public String moveClubCreatePage() {
		return "/community/club/create";
	}

	// 커뮤니티 클럽 만들기
	@RequestMapping(value = "/community/club/create", method = RequestMethod.POST)
	public String clubcreate(Club club, MultipartFile uploadfile) throws Exception {
	
		String filename = uploadfile.getOriginalFilename();
		//String directory = "/home/ec2-user/data/club"; // 서버
		String directory = "C:\\ServerFiles"; // 로컬
		String filepath = Paths.get(directory, filename).toString();
		
		club.setCb_image(filename);
		communityService.insertClub(club);
		Club_Join cb_join = new Club_Join();
		cb_join.setCj_rider(club.getCb_manager()); // cb_join 안에 club의 cb_manager를 삽입
		communityService.insertCJ(cb_join);
		// Save the file locally
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
		stream.write(uploadfile.getBytes());
		stream.close();
		
		return "/community/club/success";
	}
	
	@RequestMapping(value = "/community/club/create/check", method = RequestMethod.GET)
	@ResponseBody
	public String checkClubName(String cb_name){
		int result=0;
		try {
			result = communityService.checkClubName(cb_name);
			System.out.println(cb_name+"/"+result);
			if(result==1) {
				return "fail";
			}else {
				return "success";
			}
		} catch (Exception e) {
			return "success";
		}
	}

	// 커뮤니티 클럽 가입
	@RequestMapping(value = "/community/club/join", method = RequestMethod.GET)
	public String clubjoin() {
		return "/community/club/join";
	}

	// 커뮤니티 클럽 가입
	@RequestMapping(value = "/community/club/join", method = RequestMethod.POST)
	public String clubjoinpost() {
		return "/community/club/join";
	}

	// 클럽 게시판
	@RequestMapping(value = "/community/club/clubboard", method = RequestMethod.GET)
	public String clubboard() {
		return "/community/club/myclub/clubboard";
	}

	// 클럽 게시판 검색
	@RequestMapping(value = "/community/club/myclub/clubboard/search", method = RequestMethod.GET)
	public String clubcreateget() {
		return "/community/club/myclub/clubboard/search";
	}

	// 클럽 게시판 글쓰기
	@RequestMapping(value = "/community/club/myclub/clubboard/posting", method = RequestMethod.POST)
	public String clubcreatepost() {
		return "/community/club/myclub/clubboard/posting";
	}

	// 클럽 캘린더
	@RequestMapping(value = "/community/club/myclub/calender", method = RequestMethod.GET)
	public String clubcalender() {
		return "/community/club/myclub/calender";
	}
	
	
	//  SNS
	@RequestMapping(value = "/community/sns", method = RequestMethod.GET)
	public String snsget() {
		return "/community/sns";
	}
		
	// SNS
	@RequestMapping(value = "/community/sns", method = RequestMethod.POST)
	public String snspost() {
		return "/community/sns";
	}

	// 중고거래 GET
	@RequestMapping(value = "/community/trade", method = RequestMethod.GET)
	public String tradeget() {
		return "/community/trade";
	}

	// 중고거래 글쓰기 POST
	@RequestMapping(value = "/community/trade/posting", method = RequestMethod.POST)
	public String tradeposting() {
		return "/community/trade/posting";
	}
}