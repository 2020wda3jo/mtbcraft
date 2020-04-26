package com.capston.mtbcraft.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Repair_History {
	@Id
	@Column
	private int rh_num;
	@Column
	private String rh_day;
	@Column
	private String rh_review;
	@Column
	private Timestamp rh_time;
	@Column
	private String rh_answer;
	@Column
	private int rh_score;
	public int getRh_num() {
		return rh_num;
	}
	public void setRh_num(int rh_num) {
		this.rh_num = rh_num;
	}
	public String getRh_day() {
		return rh_day;
	}
	public void setRh_day(String rh_day) {
		this.rh_day = rh_day;
	}
	public String getRh_review() {
		return rh_review;
	}
	public void setRh_review(String rh_review) {
		this.rh_review = rh_review;
	}
	public Timestamp getRh_time() {
		return rh_time;
	}
	public void setRh_time(Timestamp rh_time) {
		this.rh_time = rh_time;
	}
	public String getRh_answer() {
		return rh_answer;
	}
	public void setRh_answer(String rh_answer) {
		this.rh_answer = rh_answer;
	}
	public int getRh_score() {
		return rh_score;
	}
	public void setRh_score(int rh_score) {
		this.rh_score = rh_score;
	}
	
}
