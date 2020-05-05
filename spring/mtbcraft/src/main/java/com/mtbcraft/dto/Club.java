package com.mtbcraft.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Club {
@Id
@Column
private int cb_num;
@Column
private String cb_manager;
@Column
private int cb_badge;
@Column
private String cb_name;
@Column
private String cb_image;
@Column
private int cb_member;
@Column
private String cb_premium;
public int getCb_num() {
	return cb_num;
}
public void setCb_num(int cb_num) {
	this.cb_num = cb_num;
}
public String getCb_manager() {
	return cb_manager;
}
public void setCb_manager(String cb_manager) {
	this.cb_manager = cb_manager;
}
public int getCb_badge() {
	return cb_badge;
}
public void setCb_badge(int cb_badge) {
	this.cb_badge = cb_badge;
}
public String getCb_name() {
	return cb_name;
}
public void setCb_name(String cb_name) {
	this.cb_name = cb_name;
}
public String getCb_image() {
	return cb_image;
}
public void setCb_image(String cb_image) {
	this.cb_image = cb_image;
}
public int getCb_member() {
	return cb_member;
}
public void setCb_member(int cb_member) {
	this.cb_member = cb_member;
}
public String getCb_premium() {
	return cb_premium;
}
public void setCb_premium(String cb_premium) {
	this.cb_premium = cb_premium;
}

}
