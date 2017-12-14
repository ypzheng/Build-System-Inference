package com.d3.login;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

public class loginMapping {
	
	public WebDriver driver;


    public loginMapping(WebDriver driver)
    {
        this.driver = driver;
    }

	    //@FindBy(how = How.XPATH, using = " /html/body/div/table/tbody/tr/td[1]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[1]/td[2]/font/a")
	    
	    //public WebElement Flights; 
	    @FindBy(how = How.NAME, using = "user-name")
	    
	    public WebElement UserName;
	    
	    @FindBy(how = How.NAME, using = "password")
	    
	    public WebElement Password;
	    
	    @FindBy(how = How.CSS, using = "button.btn.btn-submit")
	    //@FindBy(how = How.XPATH, using = "//*[@id='challenge-form']/div[2]/div[2]/button[2]")
	    
	    public WebElement Submit;
	    
	    @FindBy(how = How.NAME, using = "secret-question")
	    
	    public WebElement SecretQuestion;
	    
	    @FindBy(how = How.NAME, using = "isPrivate")
	    
	    public WebElement PrivateDevice;
	    
	    @FindBy(how = How.CSS, using = "button.btn.flipper-button.manage-button")

	    public WebElement Terms;
	    
	    @FindBy(how = How.ID, using = "tosAccept")
	    
	    public WebElement Manage;

}
