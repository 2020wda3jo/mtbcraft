package com.mtbcraft.dto;

public class AnLogin  {
	public String r_id;
	public String r_pw;
	public String r_nickname;
	public String cb_name;

	public String getR_nickname() {
		return r_nickname;
	}

	public void setR_nickname(String r_nickname) {
		this.r_nickname = r_nickname;
	}

	public String getCb_name() {
		return cb_name;
	}

	public void setCb_name(String cb_name) {
		this.cb_name = cb_name;
	}

	public AnLogin() {}
	
	public AnLogin(String r_id, String r_pw) {
		super();
		this.r_id=r_id;
		this.r_pw=r_pw;
	}
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
}