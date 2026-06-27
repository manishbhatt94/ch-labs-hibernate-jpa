package com.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Embeddable;

@Embeddable
public class Car {

	private String carNo;

	private String carModel;

	private LocalDateTime carPurchasedAt;

	private BigDecimal carPurchasePrice;

	public Car() {
		super();
	}

	public Car(String carNo, String carModel, LocalDateTime carPurchasedAt, BigDecimal carPurchasePrice) {
		super();
		this.carNo = carNo;
		this.carModel = carModel;
		this.carPurchasedAt = carPurchasedAt;
		this.carPurchasePrice = carPurchasePrice;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public LocalDateTime getCarPurchasedAt() {
		return carPurchasedAt;
	}

	public void setCarPurchasedAt(LocalDateTime carPurchasedAt) {
		this.carPurchasedAt = carPurchasedAt;
	}

	public BigDecimal getCarPurchasePrice() {
		return carPurchasePrice;
	}

	public void setCarPurchasePrice(BigDecimal carPurchasePrice) {
		this.carPurchasePrice = carPurchasePrice;
	}

	@Override
	public String toString() {
		return "Car [carNo=" + carNo + ", carModel=" + carModel + ", carPurchasedAt=" + carPurchasedAt
				+ ", carPurchasePrice=" + carPurchasePrice + "]";
	}

}
