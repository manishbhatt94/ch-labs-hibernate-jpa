package com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Cricketer extends Player {

	@Column(name = "cricketer_type", length = 30)
	private String cricketerType;

	private int runs;

	public Cricketer() {
		super();
	}

	public Cricketer(int id, String name, String cricketerType, int runs) {
		super(id, name);
		this.cricketerType = cricketerType;
		this.runs = runs;
	}

	public String getCricketerType() {
		return cricketerType;
	}

	public void setCricketerType(String cricketerType) {
		this.cricketerType = cricketerType;
	}

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	@Override
	public String toString() {
		return "Cricketer [cricketerType=" + cricketerType + ", runs=" + runs + "]";
	}

	@Override
	public String playerString() {
		return super.toString() + "{ " + toString() + " }";
	}

}
