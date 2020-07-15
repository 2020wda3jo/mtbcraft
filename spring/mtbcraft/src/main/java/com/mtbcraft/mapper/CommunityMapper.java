package com.mtbcraft.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mtbcraft.dto.Board;
import com.mtbcraft.dto.Club;
import com.mtbcraft.dto.Club_Calender;
import com.mtbcraft.dto.Club_Join;
import com.mtbcraft.dto.Reply;

@Repository("com.mtbcraft.mapper.CommunityMapper")
public interface CommunityMapper {

	// 클럽 생성
	public void insertClub(Club club) throws Exception;

	// 클럽이름 중복검사
	public int checkClubName(String cb_name);

	// 클럽 가입
	public void insertCJ(Club_Join cb_join);

	// 클럽 캘린더 일정 조회
	public List<Club_Calender> getCCList(int cc_club);

	// 클럽 캘린더 일정 등록
	public void postCC(Club_Calender cc);

	// 클럽 캘린더 일정 수정
	public void updateCC(Club_Calender cc);

	// 클럽 캘린더 일정 삭제
	public void deleteCC(int cc_num);

	// 클럽 멤버 조회
	public int getMember(Club cb_num);

	// 클럽 조회
	public List<Club> getClub(Club cb_num);

	// 클럽 가입
	public void signClub(Club_Join cb_join);
	
	//가입 클럽 조회
	public int getJoinCLub(String rider);
	
	//클럽 넘버로 클럽 정보 조회
	public Club getClubInfo(int c_num);
	
	//SNS게시글 조회
	public List<Board> getSNSList(String rider);
	
	//SNS게시글 등록
	public void postSNS(Board board);
	
	//SNS게시글 삭제
	public void deleteSNS(int b_num);
	
	//SNS게시글 수정
	public void updateSNS(Board board);

	//SNS게시글 수정2
	public void updateSNS2(Board board);
	
	//SNS댓글 조회
	public List<Reply> getReply(int re_board);
	
	//SNS댓글 등록
	public void postReply(Reply reply);

	//SNS댓글 삭제
	public void deleteReply(int re_num);
	
	//SNS댓글 수정
	public void putReply(Reply reply);
}
