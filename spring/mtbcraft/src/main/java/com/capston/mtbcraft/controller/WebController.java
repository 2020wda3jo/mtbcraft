package com.capston.mtbcraft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/")
@Controller
public class WebController {
	@RequestMapping("/index")
	@ResponseBody
	public String index() {
		return "index";
	}
}
