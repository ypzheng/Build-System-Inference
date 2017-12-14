package com.d3.moneyMovement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class moneyMovementMapping {
	
	public WebDriver driver;

    public moneyMovementMapping(WebDriver driver)
    {
        this.driver = driver;
    }
	
    @FindBy(how = How.CSS, using = "div.nav-icon.money-movement.center")
    
    public WebElement MoneyMovement;

}
