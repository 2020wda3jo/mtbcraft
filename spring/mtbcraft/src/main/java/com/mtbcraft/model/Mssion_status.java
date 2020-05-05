package com.mtbcraft.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Mssion_status {
	
	@Id
	@Column
	private int ms_num;
	@Column
	private int ms_mission;
	@Column
	private String ms_rider;
	@Column
	private int ms_status;
	@Column
	private Timestamp ms_time;
	public int getMs_num() {
		return ms_num;
	}
	public void setMs_num(int ms_num) {
		this.ms_num = ms_num;
	}
	public int getMs_mission() {
		return ms_mission;
	}
	public void setMs_mission(int ms_mission) {
		this.ms_mission = ms_mission;
	}
	public String getMs_rider() {
		return ms_rider;
	}
	public void setMs_rider(String ms_rider) {
		this.ms_rider = ms_rider;
	}
	public int getMs_status() {
		return ms_status;
	}
	public void setMs_status(int ms_status) {
		this.ms_status = ms_status;
	}
	public Timestamp getMs_time() {
		return ms_time;
	}
	public void setMs_time(Timestamp ms_time) {
		this.ms_time = ms_time;
	}
	
}
