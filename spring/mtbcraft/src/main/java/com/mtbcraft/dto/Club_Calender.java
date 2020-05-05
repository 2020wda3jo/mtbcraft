package com.mtbcraft.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Club_Calender {
	@Id
	@Column
	private int cc_num;
	@Column
	private int cc_club;
	@Column
	private String cc_rider;
	@Column
	private String cc_possible;
	@Column
	private String cc_content;
	
	public int getCc_num() {
		return cc_num;
	}
	public void setCc_num(int cc_num) {
		this.cc_num = cc_num;
	}
	public int getCc_club() {
		return cc_club;
	}
	public void setCc_club(int cc_club) {
		this.cc_club = cc_club;
	}
	public String getCc_rider() {
		return cc_rider;
	}
	public void setCc_rider(String cc_rider) {
		this.cc_rider = cc_rider;
	}
	public String getCc_possible() {
		return cc_possible;
	}
	public void setCc_possible(String cc_possible) {
		this.cc_possible = cc_possible;
	}
	public String getCc_content() {
		return cc_content;
	}
	public void setCc_content(String cc_content) {
		this.cc_content = cc_content;
	}
	
}
