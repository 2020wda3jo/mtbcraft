package com.capston.mtbcraft.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Follow {
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
