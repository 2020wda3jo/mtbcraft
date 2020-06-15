package com.mtbcraft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mtbcraft.dto.Qna;
import com.mtbcraft.dto.Repair_Apply;
import com.mtbcraft.service.RepairService;

@Controller
public class RepairController {
	@Autowired
	private RepairService repairService;

	// 정비 메인
	@RequestMapping(value = "/repair", method = RequestMethod.POST)
	public String repair(String rider, Model model) {
		int type = repairService.getType(rider);
		if (type == 1) {
			model.addAttribute("qnaList", repairService.getQNA(rider));
			model.addAttribute("applyList", repairService.getRepairApply(rider));
			model.addAttribute("noQnaList", repairService.getNoAnserQNA(rider));
			model.addAttribute("noApplyList", repairService.getNoConfirmRepairApply(rider));
			return "repair/shop/main";
		}
		return "repair/main";
	}

	// 정비 가이드
	@RequestMapping(value = "/repair/guide", method = RequestMethod.GET)
	public String guide(int result) {
		return "repair/guide";
	}

	// 정비소 검색
	@RequestMapping(value = "/repair/search", method = RequestMethod.GET)
	public String repairSearch(int result, String zipcode) {
		return "repair/search";
	}

	// 정비소 회원 정비 신청 조회
	@RequestMapping(value = "/repair/shop/apply/{ra_num}", method = RequestMethod.GET)
	public String s_repairApplyGet(@PathVariable int ra_num, Model model) {
		model.addAttribute("apply", repairService.getDetailRepairApply(ra_num));
		return "repair/shop/apply";
	}

	// 정비소 회원 정비 신청 수락
	@RequestMapping(value = "/repair/shop/apply/{ra_num}", method = RequestMethod.POST)
	public String s_repairApplyPut(int ra_num, Repair_Apply ra, Model model) {
		ra.setRa_confirm(1);
		repairService.updateRepairApply(ra);
		model.addAttribute("rider", ra.getRa_shop());
		return "repair/shop/success";
	}
	
	// 정비소 회원 정비 신청 거절
	@RequestMapping("/repair/shop/noapply")
	public String noapply(Repair_Apply ra, Model model) {
		ra.setRa_confirm(2);
		repairService.updateRepairApply(ra);
		model.addAttribute("rider", ra.getRa_shop());
		return "repair/shop/success";
	}

	// 정비소 회원 QnA 조회
	@RequestMapping(value = "/repair/shop/qna/{qa_num}", method = RequestMethod.GET)
	public String s_repairQnaGet(@PathVariable int qa_num, Model model) {
		model.addAttribute("qna", repairService.getDetailQna(qa_num));
		return "repair/shop/qna";
	}

	// 정비소 회원 QnA 답변
	@RequestMapping(value = "/repair/shop/qna", method = RequestMethod.POST)
	public String s_repairQnaPut(Qna qna, Model model) {
		repairService.updateDetailQna(qna);
		model.addAttribute("rider", qna.getQa_shop());
		return "repair/shop/success";
	}
}