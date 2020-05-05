package com.mtbcraft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mtbcraft.jpa.Member;

@Controller
public class MemberController {
	// 회원가입 종류 선택
	@RequestMapping(value = "/member/join_select", method = RequestMethod.GET)
	public String join_select() {
		return "/member/join_select";
	}

	// 일반회원가입
	@RequestMapping(value = "/member/general_join", method = RequestMethod.GET)
	public String general_join() {
		return "/member/general_join";
	}
	

	// 일반회원가입
	@RequestMapping(value = "/member/general_join", method = RequestMethod.POST)
	public String joinPOST() {
		
		return "/member/general_join";
	}

	// 아이디 중복 체크
	@RequestMapping(value = "/member/id_check", method = RequestMethod.POST)
	public String idCheck(int result, String userid) {
		return "/member/id_check";
	}

	// 도로명 주소 찾기
	@RequestMapping(value = "/member/address_find", method = RequestMethod.GET)
	public String addressFind(int result) {
		return "/member/address_find";
	}

	// 정비소 회원가입
	@RequestMapping(value = "/member/bikecenter", method = RequestMethod.GET)
	public String bikeJoinGet(int result) {
		return "/member/bikecenter";
	}

	// 정비소 회원가입
	@RequestMapping(value = "/member/bikecenter", method = RequestMethod.POST)
	public String bikeJoinPost(int result, String userid, String password, String centername, String phone, String addr, String addr2, String holiday) {
		return "/member/bikecenter";
	}

	// 로그인
	@RequestMapping(value = "/member/login", method = RequestMethod.GET)
	public String loginGet(int result) {
		return "/member/login";
	}

	// 로그인
	@RequestMapping(value = "/member/login", method = RequestMethod.POST)
	public String loginPost(int result, String userid, String password) {
		return "/member/login";
	}

	// 로그아웃
	@RequestMapping(value = "/member/logout", method = RequestMethod.POST)
	public String logout(int result, String userid) {
		return "/member/logout";
	}
}