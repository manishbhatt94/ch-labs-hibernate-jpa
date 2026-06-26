package com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Footballer extends Player {

	@Column(name = "footballer_type", length = 30)
	private String footballerType;

	private int goals;

	public Footballer() {
		super();
	}

	public Footballer(int id, String name, String footballerType, int goals) {
		super(id, name);
		this.footballerType = footballerType;
		this.goals = goals;
	}

	public String getFootballerType() {
		return footballerType;
	}

	public void setFootballerType(String footballerType) {
		this.footballerType = footballerType;
	}

	public int getGoals() {
		return goals;
	}

	public void setGoals(int goals) {
		this.goals = goals;
	}

	@Override
	public String toString() {
		return "Footballer [footballerType=" + footballerType + ", goals=" + goals + "]";
	}

	@Override
	public String playerString() {
		return super.toString() + "{ " + toString() + " }";
	}

}
