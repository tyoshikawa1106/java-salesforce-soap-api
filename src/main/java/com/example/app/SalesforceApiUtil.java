package com.example.app;

import com.sforce.async.AsyncApiException;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class SalesforceApiUtil {
    
    /**
     * ConnectorConfigにセッション情報を保存
     * @param userInfo ユーザ情報
     * @return ConnectorConfig
     * @throws ConnectionException
     * @throws AsyncApiException
     */
    public ConnectorConfig getConnectorConfig(String userId, String password, String authEndpoint) throws ConnectionException, AsyncApiException {
        System.out.println("-- getConnectorConfig --");
        ConnectorConfig partnerConfig = new ConnectorConfig();
        partnerConfig.setUsername(userId);
        partnerConfig.setPassword(password);
        partnerConfig.setAuthEndpoint(authEndpoint);
        new PartnerConnection(partnerConfig);
        return partnerConfig;
    }
}