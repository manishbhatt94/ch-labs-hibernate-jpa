package com.entity;

public class Employee {

	private int employeedId;

	private String employeeName;

	private String employeeAddress;

	private int employeeSalary;

	public Employee() {
		super();
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
