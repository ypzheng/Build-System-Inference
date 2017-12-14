package com.d3.transactions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class transactionsMapping {
	
	public WebDriver driver;

    public transactionsMapping(WebDriver driver)
    {
        this.driver = driver;
    }

    @FindBy(how = How.CSS, using = "div.nav-icon.transactions.center")
    
    public WebElement Transactions;
    
    @FindBy(how = How.CLASS_NAME, using = "span-nr4")
    
    public WebElement FirstTransaction;
    
    @FindBy(how = How.NAME, using = "categoryName")
    
    public WebElement ClickTransactionCategory;

    @FindBy(how = How.CSS, using = "button.btn.add-category.pull-right")
    
    public WebElement AddNewCategoryButton;
    
    @FindBy(how = How.CSS, using = "input.span12.field.name")
    
    public WebElement AddCategoryName;
    
    @FindBy(how = How.CSS, using = "input.field.parent-category-cb")
    
    public WebElement ClickSubcategoryButton;
    
    @FindBy(how = How.CLASS_NAME, using = "dataLabel")
    
    public WebElement ClickSelectParentDropdown;
    
    @FindBy(how = How.LINK_TEXT, using = "Business")
    
    public WebElement ClickCategoryName;
    
    @FindBy(how = How.CSS, using = "button.btn.create-category")
    
    public WebElement CategorySubmitButton;
    
    @FindBy(how = How.CLASS_NAME, using = "icon-plus")
    
    public WebElement ClickCategoryPlusIcon;
        
    @FindBy(how = How.XPATH, using = "//*[@id='main']/section/div[2]/ul/li[1]/div[2]/div[2]/div/div/div[1]/ul/li[1]/ul/li/span[2]")
    
    public WebElement ClickNewSubcategory;
    
    @FindBy(how = How.CSS, using = "button.btn.viewSimilar")
    
    public WebElement ClickViewSimilarButton;
    
    @FindBy(how = How.CSS, using = "button.btn.closeSimilar")
    
    public WebElement ClickIgnoreButton;
    
    @FindBy(how = How.NAME, using = "searchTerm")
    
    public WebElement TransactionsSearch;
    
    @FindBy(how = How.CLASS_NAME, using = "icon-search")
    
    public WebElement TransactionsSearchSubmit;   
             
    	
}

