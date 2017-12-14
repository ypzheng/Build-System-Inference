package com.d3.help;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class helpMapping {
	
	public WebDriver driver;

    public helpMapping(WebDriver driver)
    {
        this.driver = driver;
    }
	
    @FindBy(how = How.CSS, using = "div.nav-icon.help.center")
    
    public WebElement Help;

}
