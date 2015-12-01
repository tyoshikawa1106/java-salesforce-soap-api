package com.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
 
@Controller
public class LightningController {
	
	@ModelAttribute
    UserForm setUpForm() {
        return new UserForm();
    }
 
    @RequestMapping("/")
    public String index(Model model) {
    	setUpForm();
        Account account = new Account();
        account.setAccountName("Salesforce.com");
        model.addAttribute("account", account);
        return "index";
    }
}
 
class Account {
    public String name;

    public void setAccountName(String accountName) {
        this.name = accountName;
    }
}