package com.mtbcraft.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Course_Point {
	@Id
	@Column
	private int cp_num;
	@Column
	private int cp_course;
	@Column
	private int cp_club;
	@Column
	private int cp_point;
	public int getCp_num() {
		return cp_num;
	}
	public void setCp_num(int cp_num) {
		this.cp_num = cp_num;
	}
	public int getCp_course() {
		return cp_course;
	}
	public void setCp_course(int cp_course) {
		this.cp_course = cp_course;
	}
	public int getCp_club() {
		return cp_club;
	}
	public void setCp_club(int cp_club) {
		this.cp_club = cp_club;
	}
	public int getCp_point() {
		return cp_point;
	}
	public void setCp_point(int cp_point) {
		this.cp_point = cp_point;
	}
	
}
