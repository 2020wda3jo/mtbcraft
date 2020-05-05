package com.mtbcraft.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Riding_Record {
	@Id
	@Column
	private int rr_num;
	@Column
	private String rr_rider;
	@Column
	private Timestamp rr_date;
	@Column
	private int rr_distance;
	@Column
	private int rr_topspeed;
	@Column
	private int rr_avgspeed;
	@Column
	private int rr_high;
	@Column
	private String rr_gpx;
	@Column
	private int rr_open;

	public int getRr_num() {
		return rr_num;
	}

	public void setRr_num(int rr_num) {
		this.rr_num = rr_num;
	}

	public String getRr_rider() {
		return rr_rider;
	}

	public void setRr_rider(String rr_rider) {
		this.rr_rider = rr_rider;
	}

	public Timestamp getRr_date() {
		return rr_date;
	}

	public void setRr_date(Timestamp rr_date) {
		this.rr_date = rr_date;
	}

	public int getRr_distance() {
		return rr_distance;
	}

	public void setRr_distance(int rr_distance) {
		this.rr_distance = rr_distance;
	}

	public int getRr_topspeed() {
		return rr_topspeed;
	}

	public void setRr_topspeed(int rr_topspeed) {
		this.rr_topspeed = rr_topspeed;
	}

	public int getRr_avgspeed() {
		return rr_avgspeed;
	}

	public void setRr_avgspeed(int rr_avgspeed) {
		this.rr_avgspeed = rr_avgspeed;
	}

	public int getRr_high() {
		return rr_high;
	}

	public void setRr_high(int rr_high) {
		this.rr_high = rr_high;
	}

	public String getRr_gpx() {
		return rr_gpx;
	}

	public void setRr_gpx(String rr_gpx) {
		this.rr_gpx = rr_gpx;
	}

	public int getRr_open() {
		return rr_open;
	}

	public void setRr_open(int rr_open) {
		this.rr_open = rr_open;
	}

}
