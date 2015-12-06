package com.example.object;

public class User {
	
	public String userFullName;
	
	public void setUserFullName(String userFullName) {
    if (!userFullName.isEmpty()) {
		  this.userFullName = userFullName;
    } else {
      this.userFullName = "";
    }
	}
}
