package com.d3.planning;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class planningMapping {
	
	public WebDriver driver;

    public planningMapping(WebDriver driver)
    {
        this.driver = driver;
    }
	
    @FindBy(how = How.CSS, using = "div.nav-icon.planning.center")
    
    public WebElement Planning;

}
