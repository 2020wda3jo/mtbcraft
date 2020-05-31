package com.mtbcraft.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


public class Scrap_Status {
	private String ss_rider;
	private int ss_course;
	
	public String getSs_rider() {
		return ss_rider;
	}
	public void setSs_rider(String ss_rider) {
		this.ss_rider = ss_rider;
	}
	public int getSs_course() {
		return ss_course;
	}
	public void setSs_course(int ss_course) {
		this.ss_course = ss_course;
	}
}
