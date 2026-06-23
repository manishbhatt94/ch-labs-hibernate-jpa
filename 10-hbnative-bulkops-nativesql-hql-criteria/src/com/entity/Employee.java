package com.entity;

@javax.persistence.Entity
@javax.persistence.Table(name = "hbn_employee")
// Below is a Named (HQL) Query:
@org.hibernate.annotations.NamedQuery(name = "Employee.findByName", query = "SELECT e FROM Employee e WHERE e.employeeName = :name")
// Below is a Named Native (SQL) Query:
@org.hibernate.annotations.NamedNativeQuery(name = "Employee.findBySalaryGreaterThan", query = "SELECT * FROM hbn_employee WHERE employee_salary > :salary", resultClass = Employee.class)
public class Employee {

	@javax.persistence.Id
	@javax.persistence.Column(name = "employee_id")
	private int employeeId;

	@javax.persistence.Column(name = "employee_name", length = 50, nullable = false)
	private String employeeName;

	@javax.persistence.Column(name = "employee_address", length = 150)
	private String employeeAddress;

	@javax.persistence.Column(name = "employee_salary", nullable = false)
	private int employeeSalary;

	public Employee() {
		super();
		// No-argument constructor is required by JPA/Hibernate for entity
		// instantiation.
		// This is required when using EntityManager's find(), for instance.
	}

	/**
	 * Parameterized constructor required in
	 * {@link com.mainapp.Launch_CriteriaAPI#demoReadComplex_MultiSelect} method for
	 * creating Employee objects with only employeeId, employeeName and
	 * employeeSalary fields populated.
	 *
	 * This is for using:
	 * {@code criteriaQuery.multiselect(root.get("employeeId"), root.get("employeeName"), root.get("employeeSalary")); }
	 *
	 * @param employeeId
	 * @param employeeName
	 * @param employeeSalary
	 */
	public Employee(int employeeId, String employeeName, int employeeSalary) {
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.employeeSalary = employeeSalary;
	}

	public Employee(int employeeId, String employeeName, String employeeAddress, int employeeSalary) {
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.employeeAddress = employeeAddress;
		this.employeeSalary = employeeSalary;
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
		return "Employee [employeeId=" + employeeId + ", employeeName=" + employeeName + ", employeeAddress="
				+ employeeAddress + ", employeeSalary=" + employeeSalary + "]";
	}

}
