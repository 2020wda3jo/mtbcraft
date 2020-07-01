package com.mtbcraft.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mtbcraft.dto.Board;

@Repository("com.mtbcraft.mapper.BoardMapper")
public interface BoardMapper {
	
	public List<Board> getBoardList(int b_club);
	
	public int getJoinCLub(String cj_rider);
	
	// 게시글 생성
	public void insertBoard(Board board) throws Exception;
	
	// 게시글 조회
	public Board getBoardNum(int b_num);
	
	//게시글 수정
	public void updateBoard(Board board);
	
	// 게시글 삭제
	public boolean deleteBoard(int b_num);
}

