package com.example.web;

import com.example.app.*;
import com.example.object.*;
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
public class SalesforceSoapApiController {
	
	private SalesforceApiUtil sfdcApiUtil = new SalesforceApiUtil();
	
	/**
	 * セッションにログイン情報を保持
	 */
	@ModelAttribute("loginUser")
	LoginUser loginUser() {
        return new LoginUser();
    }
	
	/**
	 * Login Page【GET】
	 * @param loginUser
	 * @return
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
    public String showLogin(LoginUser loginUser) {
        return "login";
    }
	
	/**
	 * Login Page【POST】
	 * @param loginUser
	 * @return
	 */
	@RequestMapping(value="/", method=RequestMethod.POST)
    public String doSalesforceLogin(@Valid LoginUser loginUser, BindingResult bindingResult) throws ConnectionException, AsyncApiException {
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

    /**
     * Home Page【GET】
     * @param loginUser
     * @param model
     * @return
     * @throws ConnectionException
     */
	@RequestMapping(value="/home", method=RequestMethod.GET)
    public String showHome(LoginUser loginUser, Model model) throws ConnectionException {
		// PartnerConnection取得
        PartnerConnection partnerConnection = loginUser.getPartnerConnection();
        // ユーザ情報取得
        User userInfo = new User();
        userInfo.setUserFullName(partnerConnection.getUserInfo().getUserFullName());
        // モデルにセット
        model.addAttribute("userInfo", userInfo);

        return "home";
    }
    
	/**
     * Account Page【GET】
     * @param loginUser
     * @param model
     * @return
     * @throws ConnectionException
     */
    @RequestMapping(value="/account", method=RequestMethod.GET)
    public String showAccount(LoginUser loginUser, Model model) throws ConnectionException {
    	// PartnerConnection取得
        PartnerConnection partnerConnection = loginUser.getPartnerConnection();
        
        // 取引先取得クエリ実行
        String soqlQuery = "SELECT Name, AccountNumber FROM Account LIMIT 200";
        QueryResult qr = partnerConnection.query(soqlQuery);
        
        // 取引先リスト
        List<Account> accounts = new ArrayList<Account>();
        // 取引先情報をリストにセット
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
        // モデルにセット
        model.addAttribute("accounts", accounts);

        return "account";
    }
}
