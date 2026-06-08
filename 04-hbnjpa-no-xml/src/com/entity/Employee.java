package com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hbn_employee")
public class Employee {

	@Id
	@Column(name = "employee_id")
	private int employeedId;

	@Column(name = "employee_name", length = 50, nullable = false)
	private String employeeName;

	@Column(name = "employee_address", length = 150)
	private String employeeAddress;

	@Column(name = "employee_salary", nullable = false)
	private int employeeSalary;

	public Employee() {
		super();
		// No-argument constructor is required by JPA/Hibernate for entity
		// instantiation.
		// This is required when using EntityManager's find(), for instance.
	}

	public Employee(int employeedId, String employeeName, String employeeAddress, int employeeSalary) {
		super();
		this.employeedId = employeedId;
		this.employeeName = employeeName;
		this.employeeAddress = employeeAddress;
		this.employeeSalary = employeeSalary;
	}

	public int getEmployeedId() {
		return employeedId;
	}

	public void setEmployeedId(int employeedId) {
		this.employeedId = employeedId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeAddress() {
		return employeeAddress;
	}

	public void setEmployeeAddress(String employeeAddress) {
		this.employeeAddress = employeeAddress;
	}

	public int getEmployeeSalary() {
		return employeeSalary;
	}

	public void setEmployeeSalary(int employeeSalary) {
		this.employeeSalary = employeeSalary;
	}

	@Override
	public String toString() {
		return "Employee [employeedId=" + employeedId + ", employeeName=" + employeeName + ", employeeAddress="
				+ employeeAddress + ", employeeSalary=" + employeeSalary + "]";
	}

}
