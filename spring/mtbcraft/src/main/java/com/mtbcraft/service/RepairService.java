package com.mtbcraft.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtbcraft.dto.Qna;
import com.mtbcraft.dto.Repair_Apply;
import com.mtbcraft.mapper.RepairMapper;

@Service
@Transactional
public class RepairService {
	
	@Resource(name = "com.mtbcraft.mapper.RepairMapper")
	@Autowired
	private RepairMapper repairMapper;
	
	//사용자 타입 조회 0:일반 1:정비소 2:관리자
	public int getType(String rider) {
		return repairMapper.getType(rider);
	}
	
	//정비소 문의 조회
	public List<Qna> getQNA(String shop){
		return repairMapper.getQNA(shop);
	}
		
	//정비소 신청 조회
	public List<Repair_Apply> getRepairApply(String shop){
		return repairMapper.getRepairApply(shop);
	}
	
	//정비소 답변 안한 문의 조회
	public List<Qna> getNoAnserQNA(String shop){
		return repairMapper.getNoAnserQNA(shop);
	}
	
	//정비소 확인 안한 신청 조회
	public List<Repair_Apply> getNoConfirmRepairApply(String shop){
		return repairMapper.getNoConfirmRepairApply(shop);
	}
	
	//문의번호로 문의글 조회
	public Qna getDetailQna(int qa_num) {
		return repairMapper.getDetailQna(qa_num);
	}
	
	//신청번호로 신청 조회
	public Repair_Apply getDetailRepairApply(int ra_num) {
		return repairMapper.getDetailRepairApply(ra_num);
	}
	
	//문의번호로 문의글 답변
	public void updateDetailQna(Qna qna) {
		repairMapper.updateDetailQna(qna);
	}
	
	//신청번호로 신청 수락 및 거절
	public void updateRepairApply(Repair_Apply ra) {
		repairMapper.updateRepairApply(ra);
	}
}
