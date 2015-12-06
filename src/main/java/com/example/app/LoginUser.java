package com.example.app;

import com.sforce.soap.partner.PartnerConnection;

public class LoginUser {
    
  private String userId;
  private String password;
  private PartnerConnection partnerConnection;
    
    public String getUserId() {
      return this.userId;
    }
    
    public void setUserId(String userId) {
      this.userId = userId;
    }
    
    public String getPassword() {
      return this.password;
    }
    
    public void setPassword(String password) {
      this.password = password;
    }
    
    public String getAuthEndpoint() {
      return "https://login.salesforce.com/services/Soap/u/35.0";
    }

    public PartnerConnection getPartnerConnection() {
      return this.partnerConnection;
    }
    
    public void setPartnerConnection(PartnerConnection partnerConnection) {
      this.partnerConnection = partnerConnection;
    }
}
