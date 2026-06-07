package com.entity;

public class Employee {

	private int employeedId;

	private String name;

	private String address;

	private int salary;

	public Employee() {
		super();
	}

	public Employee(int employeedId, String name, String address, int salary) {
		super();
		this.employeedId = employeedId;
		this.name = name;
		this.address = address;
		this.salary = salary;
	}

	public int getEmployeedId() {
		return employeedId;
	}

	public void setEmployeedId(int employeedId) {
		this.employeedId = employeedId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Employee [employeedId=" + employeedId + ", name=" + name + ", address=" + address + ", salary=" + salary
				+ "]";
	}

}
