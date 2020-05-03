package com.capston.mtbcraft.controller;

import java.sql.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MemberController {
	// 회원가입 종류 선택
	@RequestMapping(value = "/member/join_select", method = RequestMethod.GET)
	public String joinSelect(int result) {
		return "/member/join_select";
	}

	// 일반회원가입
	@RequestMapping(value = "/member/join", method = RequestMethod.GET)
	public String joinPost(int result) {
		return "/member/join";
	}

	// 일반회원가입
	@RequestMapping(value = "/member/join", method = RequestMethod.POST)
	public String joinGet(int result, String userid, String password, String name, Date birthday, String phone, String addr, String addr2, int clubnum, int batnum) {
		return "/member/join";
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