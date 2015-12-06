package com.example.web;

import com.example.app.SalesforceApiUtil;
import com.example.object.Account;
import com.sforce.async.AsyncApiException;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import java.util.ArrayList;
import java.util.List;

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
        return new LoginUser();
    }
	
	@RequestMapping(value="/", method=RequestMethod.GET)
    public String showLogin(LoginUser loginUser) {
        return "login";
    }

    @RequestMapping(value="/home", method=RequestMethod.GET)
    public String showHome(LoginUser loginUser, Model model) throws ConnectionException {
        PartnerConnection partnerConnection = loginUser.getPartnerConnection();
        
        UserInfo userInfo = new UserInfo();
        userInfo.setUserFullName(partnerConnection.getUserInfo().getUserFullName());
        model.addAttribute("userInfo", userInfo);

        return "home";
    }
    
    @RequestMapping(value="/account", method=RequestMethod.GET)
    public String showAccount(LoginUser loginUser, Model model) throws ConnectionException {
        PartnerConnection partnerConnection = loginUser.getPartnerConnection();
        
        String soqlQuery = "SELECT Name, AccountNumber FROM Account LIMIT 200";
        QueryResult qr = partnerConnection.query(soqlQuery);
        
        List<Account> accounts = new ArrayList<Account>();
        
        boolean done = false;
        int loopCount = 0;
        // Loop through the batches of returned results
        while (!done) {
          System.out.println("Records in results set " + loopCount++ + " - ");
          SObject[] records = qr.getRecords();
          // Process the query results
          for (int i = 0; i < records.length; i++) {
        	Account acc = new Account();
        	acc.setName(String.valueOf(records[i].getField("Name")));
        	acc.setAccountNumber(String.valueOf(records[i].getField("AccountNumber")));
        	// Add List
        	accounts.add(acc);
          }
          if (qr.isDone()) {
            done = true;
          } else {
            qr = partnerConnection.queryMore(qr.getQueryLocator());
          }
        }
        
        Account account = new Account();
        account.setName("Salesforce.com");
        model.addAttribute("account", account);
        model.addAttribute("accounts", accounts);

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
