package com.mtbcraft.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


public class Scrap_Status {
	private String ss_rider;
	private int ss_rnum;
	private int c_num;
	private int c_distance;
	private int c_level;
	private String c_area;
	private String c_gpx;
	
	public String getSs_rider() {
		return ss_rider;
	}
	public void setSs_rider(String ss_rider) {
		this.ss_rider = ss_rider;
	}
	public int getSs_rnum() {
		return ss_rnum;
	}
	public void setSs_rnum(int ss_rnum) {
		this.ss_rnum = ss_rnum;
	}
	public int getC_distance() {
		return c_distance;
	}
	public void setC_distance(int c_distance) {
		this.c_distance = c_distance;
	}
	public int getC_level() {
		return c_level;
	}
	public void setC_level(int c_level) {
		this.c_level = c_level;
	}
	public String getC_area() {
		return c_area;
	}
	public void setC_area(String c_area) {
		this.c_area = c_area;
	}
	public int getC_num() {
		return c_num;
	}
	public void setC_num(int c_num) {
		this.c_num = c_num;
	}
	public String getC_gpx() {
		return c_gpx;
	}
	public void setC_gpx(String c_gpx) {
		this.c_gpx = c_gpx;
	}
	
	
	
}
