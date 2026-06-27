package com.entity;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Company {

	private String companyRegistrationNo;

	private String companyName;

	private String companyPolicy;

	@Embedded
	private Account account;

	public Company() {
		super();
	}

	public Company(String companyRegistrationNo, String companyName, String companyPolicy, Account account) {
		super();
		this.companyRegistrationNo = companyRegistrationNo;
		this.companyName = companyName;
		this.companyPolicy = companyPolicy;
		this.account = account;
	}

	public String getCompanyRegistrationNo() {
		return companyRegistrationNo;
	}

	public void setCompanyRegistrationNo(String companyRegistrationNo) {
		this.companyRegistrationNo = companyRegistrationNo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyPolicy() {
		return companyPolicy;
	}

	public void setCompanyPolicy(String companyPolicy) {
		this.companyPolicy = companyPolicy;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "Company [companyRegistrationNo=" + companyRegistrationNo + ", companyName=" + companyName
				+ ", companyPolicy=" + companyPolicy + ", account=" + account + "]";
	}

}
