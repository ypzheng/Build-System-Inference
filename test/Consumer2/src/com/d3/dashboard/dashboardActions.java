package com.d3.dashboard;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.d3.utils.Utils;


public class dashboardActions {
	
    public WebDriver Driver;
    public String TimeToWait; 
    public dashboardMapping Mapping;
    public WebDriverWait wait;
	
	
    public void init(WebDriver driver, Long timeout)
    {

      	Mapping = new dashboardMapping(driver);
        PageFactory.initElements(driver, Mapping);
        Driver = driver;
        wait = new WebDriverWait(driver, timeout);
    	
    }
    
    public void planButton(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.Plan));	
	    Mapping.Plan.click(); 
	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn.active.flipper-button.plan-button")));
    }
    
    public void createBudget(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn.active.flipper-button.plan-button")));
	    wait.until(ExpectedConditions.visibilityOf(Mapping.CreateBudget));	
	    Mapping.CreateBudget.click(); 
	}
    
    public void manageButton(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.Manage));	
	    Mapping.Manage.click(); 
	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn.active.flipper-button.manage-button")));
	}
    
    public void quickPay(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.QuickPay));	
	    Mapping.QuickPay.click(); 
	}

    public void quickPaySelectRecipient(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.QuickPaySelectRecipient));	
	    Mapping.QuickPaySelectRecipient.click(); 
	}
    
    public void bestBuyAccount(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.BestBuyAccount));	
	    Actions action = new Actions(driver);    
	   		action.moveToElement(Mapping.BestBuyAccount).click().build().perform();   
    }
    
    public void myCreditCardAccount(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.MyCreditCardAccount));	
	    Actions action = new Actions(driver);    
	   		action.moveToElement(Mapping.MyCreditCardAccount).click().build().perform();   
    }

    public void setQuickPayAmount(WebDriver driver, String amount)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.Amount));	
	    new Actions(driver).moveToElement(Mapping.Amount).perform();
	    Mapping.Amount.sendKeys(amount);; 
    }
    
    public void quickPayCalendarCurrentDate(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.QuickPayCalendar));	
	    new Actions(driver).moveToElement(Mapping.QuickPayCalendar).perform();
	    Mapping.QuickPayCalendar.sendKeys(Utils.getDateMMddyyyywithSlash()); 
    }
    
    public void quickPayCalendarSpecificDate(WebDriver driver, String date)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.QuickPayCalendar));	
	    new Actions(driver).moveToElement(Mapping.QuickPayCalendar).perform();
	    Mapping.QuickPayCalendar.sendKeys(date); 
    }
    
    public void quickPaySubmitButton(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.QuickPaySubmit));	
	    Actions action = new Actions(driver);    
   		action.moveToElement(Mapping.QuickPaySubmit).click().build().perform();
    }
    
    public void quickPayConfirm(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.QuickPayConfirm));	
	    Actions action = new Actions(driver);    
   		action.moveToElement(Mapping.QuickPayConfirm).click().build().perform();   
    }    
    
    public void transferNow(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.TransferNow));	
	    Mapping.TransferNow.click(); 
	}
    
    public void transferNowSelectRecipient(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.TransferNowSelectRecipient));	
	    Mapping.TransferNowSelectRecipient.click(); 
	}
    
    public void transferNowMyCreditCardAccount(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.TransferNowMyCreditCardAccount));	
	    Actions action = new Actions(driver);    
	   		action.moveToElement(Mapping.TransferNowMyCreditCardAccount).click().build().perform();   
    }
    
    public void setTransferNowAmount(WebDriver driver, String amount)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.Amount));	
	    new Actions(driver).moveToElement(Mapping.Amount).perform();
	    Mapping.Amount.sendKeys(amount); 
    }
    
    public void transferNowSubmitButton(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.TransferNowSubmit));	
	    Actions action = new Actions(driver);    
   		action.moveToElement(Mapping.TransferNowSubmit).click().build().perform();
    }
    
    public void transferNowConfirm(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.TransferNowConfirm));	
	    Actions action = new Actions(driver);    
   		action.moveToElement(Mapping.TransferNowConfirm).click().build().perform();   
    } 

}
