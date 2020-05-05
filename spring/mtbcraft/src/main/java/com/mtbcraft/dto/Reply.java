package com.mtbcraft.dto;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Reply {
	@Id
	@Column
	private int re_num;
	@Column
	private int re_board;
	@Column
	private String re_rider;
	@Column
	private String re_content;
	@Column
	private Timestamp re_day;
	@Column
	private int re_confirm;

	public int getRe_num() {
		return re_num;
	}

	public void setRe_num(int re_num) {
		this.re_num = re_num;
	}

	public int getRe_board() {
		return re_board;
	}

	public void setRe_board(int re_board) {
		this.re_board = re_board;
	}

	public String getRe_rider() {
		return re_rider;
	}

	public void setRe_rider(String re_rider) {
		this.re_rider = re_rider;
	}

	public String getRe_content() {
		return re_content;
	}

	public void setRe_content(String re_content) {
		this.re_content = re_content;
	}

	public Timestamp getRe_day() {
		return re_day;
	}

	public void setRe_day(Timestamp re_day) {
		this.re_day = re_day;
	}

	public int getRe_confirm() {
		return re_confirm;
	}

	public void setRe_confirm(int re_confirm) {
		this.re_confirm = re_confirm;
	}

}
