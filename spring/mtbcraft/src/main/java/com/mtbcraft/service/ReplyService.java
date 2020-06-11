package com.mtbcraft.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtbcraft.mapper.ReplyMapper;

@Service
@Transactional
public class ReplyService {
	@Resource(name="com.mtbcraft.mapper.ReplyMapper")
	@Autowired
	private ReplyMapper replyMapper;
}
