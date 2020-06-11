package com.mtbcraft.mapper;

import org.springframework.stereotype.Repository;

import com.mtbcraft.dto.Reply;

@Repository("com.mtbcraft.mapper.ReplyMapper")
public interface ReplyMapper {

	public int insert(Reply reply);
}
