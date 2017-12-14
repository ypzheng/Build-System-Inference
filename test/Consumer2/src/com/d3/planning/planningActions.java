package com.d3.planning;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class planningActions {
	
    public WebDriver Driver;
    public String TimeToWait; 
    public planningMapping Mapping;
    public WebDriverWait wait;
	
	
    public void init(WebDriver driver, Long timeout)
    {

      	Mapping = new planningMapping(driver);
        PageFactory.initElements(driver, Mapping);
        Driver = driver;
        wait = new WebDriverWait(driver, timeout);
    	
    }
    
    public void planningButton(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.Planning));	
	    Mapping.Planning.click(); 
	}
    
}
