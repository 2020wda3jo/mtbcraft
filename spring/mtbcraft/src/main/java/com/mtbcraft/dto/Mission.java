package com.mtbcraft.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Mission {
	@Id
	@Column
	private int m_num;
	@Column
	private int m_badge;
	@Column
	private String m_name;
	@Column
	private String m_content;
	public int getM_num() {
		return m_num;
	}
	public void setM_num(int m_num) {
		this.m_num = m_num;
	}
	public int getM_badge() {
		return m_badge;
	}
	public void setM_badge(int m_badge) {
		this.m_badge = m_badge;
	}
	public String getM_name() {
		return m_name;
	}
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	public String getM_content() {
		return m_content;
	}
	public void setM_content(String m_content) {
		this.m_content = m_content;
	}
	
}
