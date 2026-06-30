package com.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ecomm_order")
public class Order {

	@Id
	@Column(name = "order_id")
	private int orderId;

	@Column(name = "order_time")
	private LocalDateTime orderTime;

	@Column(name = "order_amount")
	private BigDecimal orderAmount;

	public Order() {
		super();
	}

	public Order(int orderId, LocalDateTime orderTime, BigDecimal orderAmount) {
		super();
		this.orderId = orderId;
		this.orderTime = orderTime;
		this.orderAmount = orderAmount;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", orderTime=" + orderTime + ", orderAmount=" + orderAmount + "]";
	}

}
