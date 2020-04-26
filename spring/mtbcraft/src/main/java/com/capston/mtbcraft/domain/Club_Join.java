package com.capston.mtbcraft.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Club_Join {
	@Id
	@Column
	private String cj_rider;
	@Column
	private int cj_club;
	@Column
	private Timestamp cj_join;
	@Column
	private Timestamp cj_withdraw;
}
