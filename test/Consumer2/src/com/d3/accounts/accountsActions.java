package com.d3.accounts;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class accountsActions {
	
    public WebDriver Driver;
    public String TimeToWait; 
    public accountsMapping Mapping;
    public WebDriverWait wait;
	
    public void init(WebDriver driver, Long timeout)
    {

      	Mapping = new accountsMapping(driver);
        PageFactory.initElements(driver, Mapping);
        Driver = driver;
        wait = new WebDriverWait(driver, timeout);
    	
    }
    
    public void accountsButton(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.Accounts));	
	    Mapping.Accounts.click(); 
	}

    public void assetAccountButton(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.AssetAccount));	
	    Mapping.AssetAccount.click(); 
	}
    
    public void excludeButton(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.ExcludeButton));	
	    Mapping.ExcludeButton.click(); 
	}
    
    public void excludeButtonContinue(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.ExcludeButtonContinue));	
	    Mapping.ExcludeButtonContinue.click(); 
	}

    
}
