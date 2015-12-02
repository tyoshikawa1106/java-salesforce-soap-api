package com.example.web;

import com.example.app.SalesforceApiUtil;
import com.example.object.Account;
import com.sforce.async.AsyncApiException;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes(value = "loginUser")
@Controller
public class SalesforceSoapApiDemoController {
	
	private SalesforceApiUtil sfdcApiUtil = new SalesforceApiUtil();
	
	@ModelAttribute("loginUser")
	LoginUser loginUser() {
        System.out.println("create loginUser");
        return new LoginUser();
    }
	
	@RequestMapping(value="/", method=RequestMethod.GET)
    public String showLogin(LoginUser loginUser) {
        return "login";
    }

    @RequestMapping(value="/home", method=RequestMethod.GET)
    public String showHome(LoginUser loginUser, Model model) throws ConnectionException {
        PartnerConnection partnerConnection = loginUser.getPartnerConnection();
        System.out.println("UserInfo = " + partnerConnection.getUserInfo());
        
        UserInfo userInfo = new UserInfo();
        userInfo.setUserFullName(partnerConnection.getUserInfo().getUserFullName());
        model.addAttribute("userInfo", userInfo);

        return "home";
    }
    
    @RequestMapping(value="/account", method=RequestMethod.GET)
    public String showAccount(LoginUser loginUser, Model model) throws ConnectionException {
        PartnerConnection partnerConnection = loginUser.getPartnerConnection();
        System.out.println("UserInfo = " + partnerConnection.getUserInfo());
        
        Account account = new Account();
        account.setName("Salesforce.com");
        model.addAttribute("account", account);

        return "account";
    }
	
	@RequestMapping(value="/", method=RequestMethod.POST)
    public String doLogin(@Valid LoginUser loginUser, BindingResult bindingResult) throws ConnectionException, AsyncApiException {
		try {
			// ログイン情報取得
			String userId = loginUser.getUserId();
			String password = loginUser.getPassword();
			String authEndpoint = loginUser.getAuthEndpoint();
			// ConnectorConfig情報を作成
	        ConnectorConfig partnerConfig = this.sfdcApiUtil.getConnectorConfig(userId, password, authEndpoint);
	        // PartnerConnection情報を作成
	        PartnerConnection partnerConnection = com.sforce.soap.partner.Connector.newConnection(partnerConfig);
            // ユーザ情報にセット
            loginUser.setPartnerConnection(partnerConnection);
		} catch (ConnectionException e) {
            System.out.println("<< ConnectionException >> " + e.getMessage());
            return "login";
        }
		return "redirect:home";
    }
}
