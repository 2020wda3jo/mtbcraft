package com.mtbcraft.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Dangerous_Area {
	@Id
	@Column
	private int da_num;
	@Column
	private String da_rider;
	@Column
	private String da_location;
	@Column
	private String da_addr;
	@Column
	private String da_image;
	@Column
	private int da_status;
	@Column
	private String da_content;
	public int getDa_num() {
		return da_num;
	}
	public void setDa_num(int da_num) {
		this.da_num = da_num;
	}
	public String getDa_rider() {
		return da_rider;
	}
	public void setDa_rider(String da_rider) {
		this.da_rider = da_rider;
	}
	public String getDa_location() {
		return da_location;
	}
	public void setDa_location(String da_location) {
		this.da_location = da_location;
	}
	public String getDa_addr() {
		return da_addr;
	}
	public void setDa_addr(String da_addr) {
		this.da_addr = da_addr;
	}
	public String getDa_image() {
		return da_image;
	}
	public void setDa_image(String da_image) {
		this.da_image = da_image;
	}
	public int getDa_status() {
		return da_status;
	}
	public void setDa_status(int da_status) {
		this.da_status = da_status;
	}
	public String getDa_content() {
		return da_content;
	}
	public void setDa_content(String da_content) {
		this.da_content = da_content;
	}
	
}
