package com.d3.settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class settingsMapping {
	
	public WebDriver driver;

    public settingsMapping(WebDriver driver)
    {
        this.driver = driver;
    }

    @FindBy(how = How.CSS, using = "div.nav-icon.settings.center")
    
    public WebElement Settings;
	
}
