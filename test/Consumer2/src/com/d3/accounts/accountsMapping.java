package com.d3.accounts;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class accountsMapping {
	
	public WebDriver driver;

    public accountsMapping(WebDriver driver)
    {
        this.driver = driver;
    }
	
    @FindBy(how = How.CSS, using = "div.nav-icon.accounts.center")
    
    public WebElement Accounts;
    
    @FindBy(how = How.NAME, using = "accountName")
    
    public WebElement AssetAccount;
    
    @FindBy(how = How.XPATH, using = "//*[@id='main']/section/section[1]/section[2]/ul/li[1]/div[2]/div[2]/div[2]/div[5]/div/div/button[1]")
    
    public WebElement ExcludeButton;
    
    @FindBy(how = How.XPATH, using = "//*[@id='main']/section/section[1]/section[2]/ul/li[1]/div[2]/div[1]/div/div/div/div[2]/div/button[2]")
    
    public WebElement ExcludeButtonContinue;
        

}
