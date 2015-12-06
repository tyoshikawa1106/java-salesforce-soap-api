package com.example.object;

public class Account {
	
	public String name;
	public String accountNumber;
	
	public void setName(String name) {
		if (!name.isEmpty()) {
			this.name = name;
		} else {
			this.name = "";
		}
	}
	
	public void setAccountNumber(String accountNumber) {
		if (!accountNumber.isEmpty()) {
			this.accountNumber = accountNumber;
		} else {
			this.accountNumber = "";
		}
	}
}