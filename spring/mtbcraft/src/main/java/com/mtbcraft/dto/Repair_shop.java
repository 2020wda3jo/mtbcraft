package com.mtbcraft.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Repair_shop {
	@Id
	@Column
	private String rs_id;
	@Column
	private String rs_pw;
	@Column
	private String rs_name;
	@Column
	private String rs_phone;
	@Column
	private String rs_image;
	@Column
	private String rs_info;
	@Column
	private String rs_addr;
	@Column
	private String rs_addr2;
	@Column
	private String rs_holiday;
	public String getRs_id() {
		return rs_id;
	}
	public void setRs_id(String rs_id) {
		this.rs_id = rs_id;
	}
	public String getRs_pw() {
		return rs_pw;
	}
	public void setRs_pw(String rs_pw) {
		this.rs_pw = rs_pw;
	}
	public String getRs_name() {
		return rs_name;
	}
	public void setRs_name(String rs_name) {
		this.rs_name = rs_name;
	}
	public String getRs_phone() {
		return rs_phone;
	}
	public void setRs_phone(String rs_phone) {
		this.rs_phone = rs_phone;
	}
	public String getRs_image() {
		return rs_image;
	}
	public void setRs_image(String rs_image) {
		this.rs_image = rs_image;
	}
	public String getRs_info() {
		return rs_info;
	}
	public void setRs_info(String rs_info) {
		this.rs_info = rs_info;
	}
	public String getRs_addr() {
		return rs_addr;
	}
	public void setRs_addr(String rs_addr) {
		this.rs_addr = rs_addr;
	}
	public String getRs_addr2() {
		return rs_addr2;
	}
	public void setRs_addr2(String rs_addr2) {
		this.rs_addr2 = rs_addr2;
	}
	public String getRs_holiday() {
		return rs_holiday;
	}
	public void setRs_holiday(String rs_holiday) {
		this.rs_holiday = rs_holiday;
	}
	
}
