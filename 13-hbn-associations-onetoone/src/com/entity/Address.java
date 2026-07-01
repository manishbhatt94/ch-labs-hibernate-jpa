package com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Address {

	@Id
	@Column(name = "address_id")
	private int addressId;

	@Column(name = "address_house_no")
	private String houseNo;

	@Column(name = "address_street")
	private String street;

	@Column(name = "address_locality")
	private String locality;

	@Column(name = "address_city")
	private String city;

	@Column(name = "address_state")
	private String state;

	@OneToOne(fetch = FetchType.LAZY)
//	@OneToOne(fetch = FetchType.EAGER) // FetchType.EAGER is the default
//	@OneToOne
	@JoinColumn(name = "employee_id", unique = true, foreignKey = @ForeignKey(name = "fk_employee_id"))
	private Employee employee;

	public Address() {
		super();
	}

	public Address(int addressId, String houseNo, String street, String locality, String city, String state) {
		super();
		this.addressId = addressId;
		this.houseNo = houseNo;
		this.street = street;
		this.locality = locality;
		this.city = city;
		this.state = state;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/*
	 * Important Note: This toString() implementation doesn't refer the employee
	 * field, to avoid circular dependency when printing this object & so avoiding
	 * StackOverflowError.
	 */
	@Override
	public String toString() {
		return "Address [addressId=" + addressId + ", houseNo=" + houseNo + ", street=" + street + ", locality="
				+ locality + ", city=" + city + ", state=" + state + "]";
	}

}
