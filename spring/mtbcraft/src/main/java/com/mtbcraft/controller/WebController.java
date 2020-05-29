package com.mtbcraft.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mtbcraft.dto.Gpx;
import com.mtbcraft.dto.Info_GPX;
@RequestMapping("/")
@Controller
public class WebController {
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	@RequestMapping("/intro")
	public String intro() {
		return "/intro";
	}
	@RequestMapping("/update")
	public String updateintro() {
		return "update";
	}
	
	// 여기서부터 테스트입니다.
	@RequestMapping("/test")
	public String testLocal() {
		return "test";
	}
	@RequestMapping("/test2")
	public String testServer() {
		return "test2";
	}
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> uploadFile(
	    @RequestParam("uploadfile") MultipartFile uploadfile) {
	  
	  try {
	    String filename = uploadfile.getOriginalFilename();
	    String directory = "C:\\Users\\TACK\\Desktop\\study";
	    String filepath = Paths.get(directory, filename).toString();
	    
	    // Save the file locally
	    BufferedOutputStream stream =
	        new BufferedOutputStream(new FileOutputStream(new File(filepath)));
	    stream.write(uploadfile.getBytes());
	    stream.close();
	  }
	  catch (Exception e) {
	    System.out.println(e.getMessage());
	    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	  }
	  
	  return new ResponseEntity<>(HttpStatus.OK);
	} 
	@RequestMapping(value = "/uploadFileToServer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> uploadFileToServer(
			@RequestParam("uploadfile") MultipartFile uploadfile) {
		
		try {
			String filename = uploadfile.getOriginalFilename();
			String directory = "/home/ec2-user/data/ridingrecord";
			String filepath = Paths.get(directory, filename).toString();
			
			// Save the file locally
			BufferedOutputStream stream =
					new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(uploadfile.getBytes());
			stream.close();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	} 
	
	@RequestMapping(value="/readgpx", method = RequestMethod.GET)
	@ResponseBody
	public Gpx readGPX() throws Exception {
		File filePath = new File("C:\\Users\\TACK\\Desktop\\study\\test.gpx");
		String txt = "";
		FileInputStream fis = new FileInputStream(filePath); 
		while(true) { 
			int res = fis.read(); 
			if(res<0) { 
				break; 
			}else { 
				txt += ((char)res);
			}
		}
		
		Gpx gpx = new Gpx();
		gpx.setting(txt);
		return gpx;
	}
	@RequestMapping(value="/readgpxFromServer", method = RequestMethod.GET)
	@ResponseBody
	public Gpx readGPXFromServer() throws Exception {
		File filePath = new File("/home/ec2-user/data/ridingrecord/test.gpx");
		String txt = "";
		FileInputStream fis = new FileInputStream(filePath); 
		while(true) { 
			int res = fis.read(); 
			if(res<0) { 
				break; 
			}else { 
				txt += ((char)res);
			}
		}
		
		Gpx gpx = new Gpx();
		gpx.setting(txt);
		return gpx;
	}
	// 여기서까지 테스트입니다.
}
