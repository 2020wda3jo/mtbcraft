package com.capston.mtbcraft.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Rider {
	@Id
	@Column
	private String r_id;
	@Column
	private String r_pw;
	@Column
	private String r_name;
	@Column
	private String r_nickname;
	@Column
	private Date r_birth;
	@Column
	private String r_phone;
	@Column
	private int gender;
	@Column
	private String r_addr;
	@Column
	private String r_addr2;
	@Column
	private int r_badge;
	public String getR_id() {
		return r_id;
	}
	public void setR_id(String r_id) {
		this.r_id = r_id;
	}
	public String getR_pw() {
		return r_pw;
	}
	public void setR_pw(String r_pw) {
		this.r_pw = r_pw;
	}
	public String getR_name() {
		return r_name;
	}
	public void setR_name(String r_name) {
		this.r_name = r_name;
	}
	public String getR_nickname() {
		return r_nickname;
	}
	public void setR_nickname(String r_nickname) {
		this.r_nickname = r_nickname;
	}
	public Date getR_birth() {
		return r_birth;
	}
	public void setR_birth(Date r_birth) {
		this.r_birth = r_birth;
	}
	public String getR_phone() {
		return r_phone;
	}
	public void setR_phone(String r_phone) {
		this.r_phone = r_phone;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getR_addr() {
		return r_addr;
	}
	public void setR_addr(String r_addr) {
		this.r_addr = r_addr;
	}
	public String getR_addr2() {
		return r_addr2;
	}
	public void setR_addr2(String r_addr2) {
		this.r_addr2 = r_addr2;
	}
	public int getR_badge() {
		return r_badge;
	}
	public void setR_badge(int r_badge) {
		this.r_badge = r_badge;
	}
	public String getR_image() {
		return r_image;
	}
	public void setR_image(String r_image) {
		this.r_image = r_image;
	}
	@Column
	private String r_image;
	
}
