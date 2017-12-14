package com.d3.transactions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class transactionsActions {
	
    public WebDriver Driver;
    public String TimeToWait; 
    public transactionsMapping Mapping;
    public WebDriverWait wait;
	
	
    public void init(WebDriver driver, Long timeout)
    {

      	Mapping = new transactionsMapping(driver);
        PageFactory.initElements(driver, Mapping);
        Driver = driver;
        wait = new WebDriverWait(driver, timeout);
    	
    }

    public void transactionsButton(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.Transactions));	
	    Mapping.Transactions.click(); 
	}

    public void clickFirstTransaction(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.FirstTransaction));	
	    Mapping.FirstTransaction.click(); 
	}
    
    public void clickTransactionCategory(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.ClickTransactionCategory));	
	    Mapping.ClickTransactionCategory.click(); 
	}
    
    public void addNewCategoryButton(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.AddNewCategoryButton));	
	    Mapping.AddNewCategoryButton.click();
	}
    
    public void addCategoryName(WebDriver driver, String username)
    {
    	wait.until(ExpectedConditions.visibilityOf(Mapping.AddCategoryName));
    	Mapping.AddCategoryName.sendKeys(username);
    }
    
    public void clickSubcategoryButton(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.ClickSubcategoryButton));	
	    Mapping.ClickSubcategoryButton.click(); 
	}
    
    public void clickSelectParentDropdown(WebDriver driver)
    {
	    wait.until(ExpectedConditions.visibilityOf(Mapping.ClickSelectParentDropdown));	
	    Mapping.ClickSelectParentDropdown.click(); 
	}
    
    public void clickCategoryName(WebDriver driver)
    {
    	wait.until(ExpectedConditions.visibilityOf(Mapping.ClickCategoryName));
    	Mapping.ClickCategoryName.click(); 
    }
    
    public void clickCategorySubmitButton(WebDriver driver)
    {
    	wait.until(ExpectedConditions.visibilityOf(Mapping.CategorySubmitButton));
    	Mapping.CategorySubmitButton.click(); 
    }
    
    public void clickCategoryPlusIcon(WebDriver driver)
    {
    	wait.until(ExpectedConditions.visibilityOf(Mapping.ClickCategoryPlusIcon));
    	Mapping.ClickCategoryPlusIcon.click(); 
    }
        
    public void clickNewSubcategory(WebDriver driver)
    {
    	wait.until(ExpectedConditions.visibilityOf(Mapping.ClickNewSubcategory));
    	Mapping.ClickNewSubcategory.click(); 
    }
    
    public void clickViewSimilarButton(WebDriver driver)
    {
    	wait.until(ExpectedConditions.visibilityOf(Mapping.ClickViewSimilarButton));
    	Mapping.ClickViewSimilarButton.click(); 
    }

    public void clickIgnoreButton(WebDriver driver)
    {
    	wait.until(ExpectedConditions.visibilityOf(Mapping.ClickIgnoreButton));
    	Mapping.ClickIgnoreButton.click(); 
    }
    
    public void transactionsSearch(WebDriver driver, String search)
    {
    	wait.until(ExpectedConditions.visibilityOf(Mapping.TransactionsSearch));
    	Mapping.TransactionsSearch.sendKeys(search);
    }
    
    public void transactionsSearchSubmit(WebDriver driver)
    {
    	wait.until(ExpectedConditions.visibilityOf(Mapping.TransactionsSearchSubmit));
    	Mapping.TransactionsSearchSubmit.click(); 
    }
    
    
    
}
