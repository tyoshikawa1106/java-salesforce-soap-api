package com.example.web;

import com.example.app.SalesforceApiUtil;
import com.sforce.async.AsyncApiException;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SalesforceSoapApiDemoController {
	
	private SalesforceApiUtil sfdcApiUtil = new SalesforceApiUtil();
	
	@RequestMapping(value="/", method=RequestMethod.GET)
    public String showLogin(LoginUser loginUser) {
        return "login";
    }

    @RequestMapping(value="/home", method=RequestMethod.GET)
    public String showHome(LoginUser loginUser) {
        System.out.println(loginUser.getUserId());

        return "home";
    }
	
	@RequestMapping(value="/", method=RequestMethod.POST)
    public String doLogin(@Valid LoginUser loginUser, BindingResult bindingResult) throws ConnectionException, AsyncApiException {
		PartnerConnection partnerConnection = null;
		try {
			// ログイン情報取得
			String userId = loginUser.getUserId();
			String password = loginUser.getPassword();
			String authEndpoint = loginUser.getAuthEndpoint();
			// ConnectorConfig情報を作成
	        ConnectorConfig partnerConfig = this.sfdcApiUtil.getConnectorConfig(userId, password, authEndpoint);
            // ユーザ情報にセット
            loginUser.setPartnerConfig(partnerConfig);
            System.out.println(loginUser.getUserId());
		} catch (ConnectionException e) {
            System.out.println("<< ConnectionException >> " + e.getMessage());
            return "login";
        }
		return "home";
    }
}
