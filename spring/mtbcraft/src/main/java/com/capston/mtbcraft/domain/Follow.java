package com.capston.mtbcraft.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Follow {
	@Id				//이거 안하면..실행을 안시켜주더라--;
	@Column
	private String follower;
	@Column
	private String follow;
	public String getFollower() {
		return follower;
	}
	public void setFollower(String follower) {
		this.follower = follower;
	}
	public String getFollow() {
		return follow;
	}
	public void setFollow(String follow) {
		this.follow = follow;
	}
	
}
