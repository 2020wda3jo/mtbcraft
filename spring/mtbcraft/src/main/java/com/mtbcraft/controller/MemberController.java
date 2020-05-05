package com.mtbcraft.controller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import com.mtbcraft.model.Member;
import org.apache.commons.io.FileUtils;
import com.mtbcraft.service.MemberService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;

@Controller
public class MemberController {
	
	@Autowired
	MemberService memberService;

	// 회원가입 종류 선택
	@RequestMapping(value = "/member/join_select", method = RequestMethod.GET)
	public String join_select() {
		return "/member/join_select";
	}

	// 일반회원가입
	@RequestMapping("/member/general_join")
	public String general_join() {
		return "/member/general_join";
	}
	
	// 일반회원가입
	@RequestMapping(value = "/member/general_join", method = RequestMethod.POST)
	public String joinPOST(HttpServletRequest request, @RequestParam("userphoto") MultipartFile profile) throws Exception{
		
		//파일명
        String originalFile = profile.getOriginalFilename();
        //파일명 중 확장자만 추출                                                //lastIndexOf(".") - 뒤에 있는 . 의 index번호
        String originalFileExtension = originalFile.substring(originalFile.lastIndexOf("."));
        //업무에서 사용하는 리눅스, UNIX는 한글지원이 안 되는 운영체제 
        //파일업로드시 파일명은 ASCII코드로 저장되므로, 한글명으로 저장 필요
        //UUID클래스 - (특수문자를 포함한)문자를 랜덤으로 생성                    "-"라면 생략으로 대체
        String storedFileName = UUID.randomUUID().toString().replaceAll("-", "") + originalFileExtension;
        String filePath =  request.getSession().getServletContext().getRealPath("/");
        //파일을 저장하기 위한 파일 객체 생성
        File file = new File(filePath + storedFileName);
        
        //파일 저장
        profile.transferTo(file);
        
		Member member  = new Member();
		member.setR_id(request.getParameter("userid"));
		member.setR_pw(request.getParameter("userpw"));
		member.setR_name(request.getParameter("username"));
		member.setR_nickname(request.getParameter("usernickname"));
		member.setR_birth(request.getParameter("userbirth"));
		member.setR_phone(request.getParameter("userphone"));
		member.setR_gender(request.getParameter("usergender"));
		member.setR_image(originalFile);
		member.setR_addr(request.getParameter("useraddr"));
		member.setR_addr2(request.getParameter("useraddr2"));
		member.setR_type(request.getParameter("usertype"));
		
		System.out.println("아이디"+request.getParameter("userid") + "비밀번호"+request.getParameter("userpw") +"이름"+request.getParameter("username") +"닉네임"+request.getParameter("usernickname")
		+ "생년월일"+request.getParameter("userbirth")+"번호"+request.getParameter("userphone") + "성별"+request.getParameter("usergender")+ "프로필사진"+ profile.getOriginalFilename()+ "주소"+request.getParameter("useraddr")+ "상세주소"+request.getParameter("useraddr2")+ "유 저타입"+request.getParameter("usertype"));
		System.out.println("파일이름" + profile.getOriginalFilename());
		memberService.memberInsert(member);
		
		
		
		return "redirect:/";
	}
	
	// 도로명 주소
		@RequestMapping(value = "/member/general_join/address_popup", method = RequestMethod.GET)
		public String address_popup(HttpServletRequest request) throws Exception{
			
			String inputYn = request.getParameter("inputYn");
			String roadFullAddr = request.getParameter("roadFullAddr");
			String roadAddrPart1 = request.getParameter("roadAddrPart1");
			String roadAddrPart2 = request.getParameter("roadAddrPart2");
			String engAddr = request.getParameter("engAddr");
			String jibunAddr = request.getParameter("jibunAddr");
			String zipNo = request.getParameter("zipNo");
			String addrDetail = request.getParameter("addrDetail");
			String admCd = request.getParameter("admCd");
			String rnMgtSn = request.getParameter("rnMgtSn");
			String bdMgtSn = request.getParameter("bdMgtSn");
			String detBdNmList = request.getParameter("detBdNmList"); 
			
			
			
			String bdNm = request.getParameter("bdNm");
			String bdKdcd = request.getParameter("bdKdcd");
			String siNm = request.getParameter("siNm");
			String sggNm = request.getParameter("sggNm");
			String emdNm = request.getParameter("emdNm");
			String liNm = request.getParameter("liNm");
			String rn = request.getParameter("rn");
			String udrtYn = request.getParameter("udrtYn");
			String buldMnnm = request.getParameter("buldMnnm");
			String buldSlno = request.getParameter("buldSlno");
			String mtYn = request.getParameter("mtYn");
			String lnbrMnnm = request.getParameter("lnbrMnnm");
			String lnbrSlno = request.getParameter("lnbrSlno");
			String emdNo = request.getParameter("emdNo");
			
			return "/member/address_popup";
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