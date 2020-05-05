package com.mtbcraft.jpa;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Qna {
	@Id
	@Column
	private int qa_num;
	@Column
	private String qa_title;
	@Column
	private String qa_content;
	@Column
	private String qa_answer;
	@Column
	private Timestamp qa_time;
	@Column
	private String qa_rider;
	@Column
	private String qa_shop;
	public int getQa_num() {
		return qa_num;
	}
	public void setQa_num(int qa_num) {
		this.qa_num = qa_num;
	}
	public String getQa_title() {
		return qa_title;
	}
	public void setQa_title(String qa_title) {
		this.qa_title = qa_title;
	}
	public String getQa_content() {
		return qa_content;
	}
	public void setQa_content(String qa_content) {
		this.qa_content = qa_content;
	}
	public String getQa_answer() {
		return qa_answer;
	}
	public void setQa_answer(String qa_answer) {
		this.qa_answer = qa_answer;
	}
	public Timestamp getQa_time() {
		return qa_time;
	}
	public void setQa_time(Timestamp qa_time) {
		this.qa_time = qa_time;
	}
	public String getQa_rider() {
		return qa_rider;
	}
	public void setQa_rider(String qa_rider) {
		this.qa_rider = qa_rider;
	}
	public String getQa_shop() {
		return qa_shop;
	}
	public void setQa_shop(String qa_shop) {
		this.qa_shop = qa_shop;
	}
	
}
