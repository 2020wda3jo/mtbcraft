package com.mtbcraft.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "RIDER")
public class Member {
	
	@Id
	@Column(nullable=false)
	private String R_ID;
	@Column
	private String R_PW;
	@Column
	private String R_NAME;
	@Column
	private String R_NICKNAME;
	@Column
	private String R_BIRTH;
	@Column
	private String R_PHONE;
	@Column
	private int R_GENDER;
	@Column
	private String R_ADDR;
	@Column
	private String R_ADDR2;
	@Column
	private String R_IMAGE;
	@Column
	private String R_BADGE;
	@Column
	private int R_TYPE;
	
}
