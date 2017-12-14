package com.d3.dashboard;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class dashboardMapping {
	
	public WebDriver driver;

    public dashboardMapping(WebDriver driver)
    {
        this.driver = driver;
    }

public WebElement Manage;
	
    @FindBy(how = How.CSS, using = "button.btn.flipper-button.plan-button")
//  @FindBy(how = How.XPATH, using = "//img[contains(@src,'assets/img/glyphicons_173_play_right.png')]")
    
	public WebElement Plan;
    
    @FindBy(how = How.CSS, using = "button.create-budget.btn.pull-right")
    
    public WebElement CreateBudget;
    
    @FindBy(how = How.CLASS_NAME, using = "button-header")
    
    public WebElement QuickPay;
    
    @FindBy(how = How.CLASS_NAME, using = "select2-chosen")
    
    public WebElement QuickPaySelectRecipient;
    
    @FindBy(how = How.ID, using = "select2-result-label-8")

    public WebElement BestBuyAccount;
	
    @FindBy(how = How.CLASS_NAME, using = "select2-result-sub")

    public WebElement MyCreditCardAccount;

    @FindBy(how = How.NAME, using = "amount")

    public WebElement Amount;
    
    @FindBy(how = How.NAME, using = "scheduledDate")

    public WebElement QuickPayCalendar;
    
    @FindBy(how = How.CSS, using = "button.btn.btn-ok")

    public WebElement QuickPaySubmit;
    
    @FindBy(how = How.CSS, using = "button.btn.btn-confirm")

    public WebElement QuickPayConfirm;
    
    @FindBy(how = How.XPATH, using = "//*[@id='main']/div/section[1]/div[2]/div[1]/div[2]/article/header/h2")
    
    public WebElement TransferNow;
    
    @FindBy(how = How.XPATH, using = "//*[@id='select2-chosen-2']")
    
    public WebElement TransferNowSelectRecipient;

    @FindBy(how = How.CLASS_NAME, using = "select2-result-sub")
    
    public WebElement TransferNowMyCreditCardAccount;
    
    @FindBy(how = How.CSS, using = "button.btn.btn-ok")

    public WebElement TransferNowSubmit;
    
    @FindBy(how = How.CSS, using = "button.btn.btn-confirm")

    public WebElement TransferNowConfirm;

    
	
}
