package com.example.web;

import javax.validation.constraints.NotNull;
import com.sforce.ws.ConnectorConfig;

public class LoginUser {
    
    @NotNull
    private String userId;
    @NotNull
    private String password;
    private String authEndpoint;
    private ConnectorConfig partnerConfig;
    
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
    
    public void setAuthEndpoint(String authEndpoint) {
    	this.authEndpoint = authEndpoint;
    }

    public ConnectorConfig getPartnerConfig() {
        return this.partnerConfig;
    }
    
    public void setPartnerConfig(ConnectorConfig partnerConfig) {
        this.partnerConfig = partnerConfig;
    }
}
