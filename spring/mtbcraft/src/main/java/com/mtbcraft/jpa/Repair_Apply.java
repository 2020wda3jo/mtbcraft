package com.mtbcraft.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Repair_Apply {
	@Id
	@Column
	private int ra_num;
	@Column
	private String ra_rider;
	@Column
	private String ra_shop;
	@Column
	private int ra_type;
	@Column
	private String ra_date;
	@Column
	private String ra_content;
	@Column
	private int ra_confirm;

	public int getRa_num() {
		return ra_num;
	}

	public void setRa_num(int ra_num) {
		this.ra_num = ra_num;
	}

	public String getRa_rider() {
		return ra_rider;
	}

	public void setRa_rider(String ra_rider) {
		this.ra_rider = ra_rider;
	}

	public String getRa_shop() {
		return ra_shop;
	}

	public void setRa_shop(String ra_shop) {
		this.ra_shop = ra_shop;
	}

	public int getRa_type() {
		return ra_type;
	}

	public void setRa_type(int ra_type) {
		this.ra_type = ra_type;
	}

	public String getRa_date() {
		return ra_date;
	}

	public void setRa_date(String ra_date) {
		this.ra_date = ra_date;
	}

	public String getRa_content() {
		return ra_content;
	}

	public void setRa_content(String ra_content) {
		this.ra_content = ra_content;
	}

	public int getRa_confirm() {
		return ra_confirm;
	}

	public void setRa_confirm(int ra_confirm) {
		this.ra_confirm = ra_confirm;
	}

}
