package com.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Employee {

	@Id
	@Column(name = "employee_id")
	private int employeeId;

	@Column(name = "employee_name")
	private String employeeName;

	@Column(name = "employee_designation")
	private String employeeDesignation;

	/**
	 * If we don't put any association mapping annotation like {@code @OneToOne} on
	 * {@code private Address employeeAddress;}, then we get below error:
	 *
	 * Exception in thread "main" org.hibernate.MappingException: Could not
	 * determine type for: com.entity.Address, at table: Employee, for columns:
	 * [org.hibernate.mapping.Column(employeeAddress)] at
	 * org.hibernate.mapping.SimpleValue.getType(SimpleValue.java:515)
	 */

	@OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private Address employeeAddress;

	public Employee() {
		super();
	}

	public Employee(int employeeId, String employeeName, String employeeDesignation) {
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.employeeDesignation = employeeDesignation;
	}

	public void addEmployeeAddress(Address employeeAddress) {
		if (employeeAddress != null) {
			employeeAddress.setEmployee(this);
		}
		this.employeeAddress = employeeAddress;
	}

	public void removeEmployeeAddress() {
		if (employeeAddress != null) {
			employeeAddress.setEmployee(null);
			employeeAddress = null;
		}
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeDesignation() {
		return employeeDesignation;
	}

	public void setEmployeeDesignation(String employeeDesignation) {
		this.employeeDesignation = employeeDesignation;
	}

	public Address getEmployeeAddress() {
		return employeeAddress;
	}

	public void setEmployeeAddress(Address employeeAddress) {
		this.employeeAddress = employeeAddress;
	}

	/*
	 * Important Note: This toString() implementation doesn't refer the
	 * employeeAddress field, to avoid circular dependency when printing this object
	 * & so avoiding StackOverflowError.
	 */
	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", employeeName=" + employeeName + ", employeeDesignation="
				+ employeeDesignation + "]";
	}

}
