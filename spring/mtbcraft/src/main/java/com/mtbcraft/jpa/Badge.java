package com.mtbcraft.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Badge {
	@Id
	@Column
	private int bg_num;
	@Column
	private String bg_name;
	@Column
	private String bg_image;
	public int getBg_num() {
		return bg_num;
	}
	public void setBg_num(int bg_num) {
		this.bg_num = bg_num;
	}
	public String getBg_name() {
		return bg_name;
	}
	public void setBg_name(String bg_name) {
		this.bg_name = bg_name;
	}
	public String getBg_image() {
		return bg_image;
	}
	public void setBg_image(String bg_image) {
		this.bg_image = bg_image;
	}
}
