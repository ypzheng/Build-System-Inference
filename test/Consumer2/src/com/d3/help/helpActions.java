package com.d3.help;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class helpActions {
	
    public WebDriver Driver;
    public String TimeToWait; 
    public helpMapping Mapping;
    public WebDriverWait wait;
	
	
    public void init(WebDriver driver, Long timeout)
    {

      	Mapping = new helpMapping(driver);
        PageFactory.initElements(driver, Mapping);
        Driver = driver;
        wait = new WebDriverWait(driver, timeout);
    	
    }
    
    public void helpButton(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.Help));	
	    Mapping.Help.click(); 
	}

}
