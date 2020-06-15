package com.mtbcraft.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtbcraft.dto.Board;
import com.mtbcraft.mapper.BoardMapper;

@Service
@Transactional
public class BoardService {
	@Resource(name="com.mtbcraft.mapper.BoardMapper")
	@Autowired
	private BoardMapper boardMapper;

	public List<Board> getBoardList(int b_club) {
		return boardMapper.getBoardList(b_club);
	}
	
	public int getJoinCLub(String cj_rider) {
		return boardMapper.getJoinCLub(cj_rider);
	}
	
	// 게시글 생성
	public void insertBoard(Board board) throws Exception {
		boardMapper.insertBoard(board);
	}
}
