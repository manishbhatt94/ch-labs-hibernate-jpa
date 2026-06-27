package com.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Account {

	private String accountNo;

	private String accountHolderName;

	private String accountHolderAddress;

	private String bankName;

	private String branchIfsc;

	public Account() {
		super();
	}

	public Account(String accountNo, String accountHolderName, String accountHolderAddress, String bankName,
			String branchIfsc) {
		super();
		this.accountNo = accountNo;
		this.accountHolderName = accountHolderName;
		this.accountHolderAddress = accountHolderAddress;
		this.bankName = bankName;
		this.branchIfsc = branchIfsc;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public String getAccountHolderAddress() {
		return accountHolderAddress;
	}

	public void setAccountHolderAddress(String accountHolderAddress) {
		this.accountHolderAddress = accountHolderAddress;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchIfsc() {
		return branchIfsc;
	}

	public void setBranchIfsc(String branchIfsc) {
		this.branchIfsc = branchIfsc;
	}

	@Override
	public String toString() {
		return "Account [accountNo=" + accountNo + ", accountHolderName=" + accountHolderName
				+ ", accountHolderAddress=" + accountHolderAddress + ", bankName=" + bankName + ", branchIfsc="
				+ branchIfsc + "]";
	}

}
