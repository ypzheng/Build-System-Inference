package com.d3.messages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.d3.utils.Utils;


public class messagesActions {
	
    public WebDriver Driver;
    public String TimeToWait; 
    public messagesMapping Mapping;
    public WebDriverWait wait;
	
	
    public void init(WebDriver driver, Long timeout)
    {

      	Mapping = new messagesMapping(driver);
        PageFactory.initElements(driver, Mapping);
        Driver = driver;
        wait = new WebDriverWait(driver, timeout);
    	
    }
	
    public void messagesButton(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.Messages));	
	    Mapping.Messages.click(); 
	}
    
    public void searchField(WebDriver driver, String searchTerm)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.SearchField));	
	    new Actions(driver).moveToElement(Mapping.SearchField).perform();
	    Mapping.SearchField.sendKeys(searchTerm);
	    Mapping.SearchSubmit.click();
    }
    
    public void searchDropDown(WebDriver driver, String searchCategory)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.SearchCategory));	
	    new Actions(driver).moveToElement(Mapping.SearchCategory).click().perform();
	    driver.findElement(By.linkText(searchCategory)).click();
    }
    
    public void messagesCalendar(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.MessagesCalendar));	
	    new Actions(driver).moveToElement(Mapping.MessagesCalendar).perform();
	    Mapping.MessagesCalendar.click();
    }
    
    public void messagesCalendarCurrentDate(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.MessagesCalendarEndDate));	
	    new Actions(driver).moveToElement(Mapping.MessagesCalendarEndDate).perform();
	    Mapping.MessagesCalendarEndDate.sendKeys(Utils.getDateMMddyyyywithSlash()); 
    }
    
    public void messagesCalendarSpecificDate(WebDriver driver, String date)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.MessagesCalendarStartDate));	
	    new Actions(driver).moveToElement(Mapping.MessagesCalendarStartDate).perform();
	    Mapping.MessagesCalendarStartDate.sendKeys(date); 
    }

    public void messagesSearch(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.MessagesSearch));	
	    new Actions(driver).moveToElement(Mapping.MessagesSearch).perform();
	    Mapping.MessagesSearch.click();
    }
    
    public void deleteSingleMessage(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.FirstCheckBox));	
	    new Actions(driver).moveToElement(Mapping.FirstCheckBox).perform();
	    Mapping.FirstCheckBox.click();
	    wait.until(ExpectedConditions.visibilityOf(Mapping.MessagesDeleteButton));
	    new Actions(driver).moveToElement(Mapping.MessagesDeleteButton).perform();
	    Mapping.MessagesDeleteButton.click();
    }
    
    public void deleteMultipleMessages(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.FirstCheckBox));
	    new Actions(driver).moveToElement(Mapping.FirstCheckBox).perform();
	    Mapping.FirstCheckBox.click();
	    new Actions(driver).moveToElement(Mapping.SecondCheckBox).perform();
	    Mapping.SecondCheckBox.click();
	    wait.until(ExpectedConditions.visibilityOf(Mapping.MessagesDeleteButton));
	    new Actions(driver).moveToElement(Mapping.MessagesDeleteButton).perform();
	    Mapping.MessagesDeleteButton.click();
    }
    
    public void messagesDeleteButton(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.MessagesDeleteButton));	
	    new Actions(driver).moveToElement(Mapping.MessagesDeleteButton).perform();
	    Mapping.MessagesDeleteButton.click();
    }
    
}
