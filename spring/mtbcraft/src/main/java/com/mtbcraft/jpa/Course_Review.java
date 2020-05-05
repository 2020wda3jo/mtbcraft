package com.mtbcraft.jpa;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Course_Review {
	@Id
	@Column
	private int cr_num;
	@Column
	private int cr_couse;
	@Column
	private String cr_rider;
	@Column
	private Timestamp cr_time;
	@Column
	private String cr_content;
	public int getCr_num() {
		return cr_num;
	}
	public void setCr_num(int cr_num) {
		this.cr_num = cr_num;
	}
	public int getCr_couse() {
		return cr_couse;
	}
	public void setCr_couse(int cr_couse) {
		this.cr_couse = cr_couse;
	}
	public String getCr_rider() {
		return cr_rider;
	}
	public void setCr_rider(String cr_rider) {
		this.cr_rider = cr_rider;
	}
	public Timestamp getCr_time() {
		return cr_time;
	}
	public void setCr_time(Timestamp cr_time) {
		this.cr_time = cr_time;
	}
	public String getCr_content() {
		return cr_content;
	}
	public void setCr_content(String cr_content) {
		this.cr_content = cr_content;
	}
	
}
