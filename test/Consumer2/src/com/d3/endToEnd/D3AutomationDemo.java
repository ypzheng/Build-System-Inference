package com.d3.endToEnd;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.testng.annotations.*;
import org.testng.ITestResult;

import com.d3.testrails.D3TestRails;
import com.d3.utils.*;
import com.d3.login.loginActions;
import com.d3.accounts.accountsActions;
import com.d3.help.helpActions;
import com.d3.messages.messagesActions;
import com.d3.moneyMovement.moneyMovementActions;
import com.d3.planning.planningActions;
import com.d3.settings.settingsActions;
import com.d3.transactions.transactionsActions;
import com.d3.dashboard.dashboardActions;

//import com.gurock.testrail.APIException;


public class D3AutomationDemo {

	public WebDriver driver;
	String TestCase; 
	String TestRun = "1"; 
	loginActions LoginActions = new loginActions();
	accountsActions AccountsActions = new accountsActions();
	dashboardActions DashboardActions = new dashboardActions();
	messagesActions MessagesActions = new messagesActions();
	transactionsActions TransactionsActions = new transactionsActions();
	moneyMovementActions MoneyMovementActions = new moneyMovementActions();
	planningActions PlanningActions = new planningActions();
	helpActions HelpActions = new helpActions();
	settingsActions SettingsActions = new settingsActions();
		
	
	D3TestRails d3testrails = new D3TestRails();
	Utils utils = new Utils();
   	//Properties p = Utils.loadProperties(".\\conf\\properties.properties");            


	@BeforeClass(alwaysRun = true)
	@Parameters({"browse", "WebdriverTimeout", "baseurl"})
	public void launchBrowser(@Optional("CHROME") String browse, String WebdriverTimeout, String baseurl)
	{
        driver = Utils.getWebDriver(browse, WebdriverTimeout); 
    	Long timeout = Long.valueOf(WebdriverTimeout);
		driver.get(baseurl);
		LoginActions.init(driver, timeout);
		AccountsActions.init(driver, timeout);
		DashboardActions.init(driver, timeout);
		MessagesActions.init(driver, timeout);
		TransactionsActions.init(driver, timeout);
		MoneyMovementActions.init(driver, timeout);
		PlanningActions.init(driver, timeout);
		HelpActions.init(driver, timeout);
		SettingsActions.init(driver, timeout);

	}
	
/*	@BeforeClass(alwaysRun = true)
	@Parameters({"testRailUrl", "testRailUserName", "testRailPassWord"})
	public void initTestRails(String testRailUrl, String testRailUserName, String testRailPassWord)
	{	
		d3testrails.InitRail(testRailUrl, testRailUserName, testRailPassWord);
	}*/
	
	
  @Test(priority = 1, groups = {"smoke", "regression"})
  public void verifyHomepageTitle() {
	   TestCase = "12";
	   LoginActions.veriyHomePage(driver);
  }
    
  @Test(priority = 2, groups = {"smoke", "regression"})
  @Parameters({"userName"})
  public void verifyInvalidLogin(String userName) 
  {
	   TestCase = "1";
	   LoginActions.loginUn(driver, userName);
	   LoginActions.loginPw(driver, "xxxxxx");
       WebElement submit =  driver.findElement(By.cssSelector("button.btn.btn-submit"));
       submit.submit();
	   //LoginActions.submit(driver);
	   //WebDriverWait wait = new WebDriverWait(driver, 10);
	   //wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[@class='user-alert']"), "Invalid User Credentials"));
	   Utils.isTextPresent(driver, "Invalid");

  }
  
  @Test(priority = 3, groups = {"smoke", "regression"})
  @Parameters({"userName", "passWord"})
  public void verifyValidLogin(String userName, String passWord) 
  {
	   TestCase = "2";
	   driver.findElement(By.name("user-name")).clear();
	   driver.findElement(By.name("password")).clear();
	   LoginActions.loginUn(driver, userName);
	   LoginActions.loginPw(driver, passWord);
       WebElement submit =  driver.findElement(By.cssSelector("button.btn.btn-submit"));
       submit.submit();
	   //LoginActions.submit(driver);
  }
  
  @Test(priority = 4, groups = {"smoke", "regression"})
  @Parameters({"secretQuestion"})
  public void verifySecretQuestion(String secretQuestion) 
  {
	   TestCase = "13";
	   LoginActions.secretQuestion(driver, secretQuestion);
	   LoginActions.privateDevice(driver);
       WebElement submit =  driver.findElement(By.cssSelector("button.btn.btn-submit"));
       submit.submit();
	   //LoginActions.submit(driver);
	   Utils.isTextPresent(driver, "Last Login:");
	   Utils.isTextPresent(driver, "Logout");	   
	   Utils.isTextPresent(driver, "Samuel Adams III");
	  //String expectedTitle = "Welcome: Mercury Tours";
		//String actualTitle = driver.getTitle();
		//Assert.assertEquals(actualTitle, expectedTitle);
  }
  
//  @Test(priority = 5, groups = {"smoke", "regression"})
//  public void termsOfService() {
//	   //TestCase = "";
//	  _aiTemp.termsOfService(driver);
//	  _aiTemp.submit(driver);
//  }
  
  @Test(priority = 6, groups = {"smoke", "regression"})
  public void verifyPlanButton() 
  {
	   TestCase = "14";
	   DashboardActions.planButton(driver);
	   Utils.isTextPresent(driver, "Cash Flow Trends");
	   Utils.isTextPresent(driver, "Financial Goal Progress");	   
  }  
 
//  @Test(priority = 7, groups = {"smoke", "regression"})
//  public void verifyCreateBudget() {
//  TestCase = "6";
//	   _aiTemp.createBudget(driver);
//  }
// 
//  @Test(priority = 8, groups = {"smoke", "regression"})
//  public void verifyManageButton() {
//  TestCase = "7";
//	   _aiTemp.manageButton(driver);
//  }

  @Test(priority = 9, groups = {"smoke", "regression"})
  public void verifyMessagesButton() 
  {
	   TestCase = "15";
	   MessagesActions.messagesButton(driver);
	   Utils.isTextPresent(driver, "Messages: Notices");
  }  
  
  @Test(priority = 10, groups = {"smoke", "regression"})
  public void verifyAccountsButton() 
  {
	   TestCase = "16";
	   AccountsActions.accountsButton(driver);
	   Utils.isTextPresent(driver, "My Accounts");
	   Utils.isTextPresent(driver, "Assets");
	   Utils.isTextPresent(driver, "Liabilities");
  }  
 
  @Test(priority = 11, groups = {"smoke", "regression"})
  public void verifyTransactionsButton() 
  {
	   TestCase = "17";
	   TransactionsActions.transactionsButton(driver);
	   Utils.isTextPresent(driver, "All Accounts");
  }  
 
  @Test(priority = 12, groups = {"smoke", "regression"})
  public void verifyMoneyMovementButton() 
  {
	   TestCase = "18";
	   MoneyMovementActions.moneyMovementButton(driver);
	   Utils.isTextPresent(driver, "Money Movement: Schedule");
	   Utils.isTextPresent(driver, "Payments & Transfers");
  }  
 
  @Test(priority = 13, groups = {"smoke", "regression"})
  public void verifyPlanningButton() 
  {
	   TestCase = "19";
	   PlanningActions.planningButton(driver);
	   Utils.isTextPresent(driver, "Planning: Budget");
	   Utils.isTextPresent(driver, "Income Categories");
	   Utils.isTextPresent(driver, "Expense Categories");
  }  
 
  @Test(priority = 14, groups = {"smoke", "regression"})
  public void verifyHelpButton() 
  {
	   TestCase = "20";
	   HelpActions.helpButton(driver);
	   Utils.isTextPresent(driver, "Help: Frequently Asked Questions");
	   Utils.isTextPresent(driver, "Customer Support:");
	   Utils.isTextPresent(driver, "402-555-1234");
  }  
 
  @Test(priority = 15, groups = {"smoke", "regression"})
  public void verifySettingsButton() 
  {
	   TestCase = "21";
	   SettingsActions.settingsButton(driver);
	   Utils.isTextPresent(driver, "Settings:");
	   Utils.isTextPresent(driver, "User Profile");
  }  
    
  /*@AfterMethod(alwaysRun = true)
  public void updateTestRails(ITestResult result) 
  {
     if (result.getStatus() == ITestResult.SUCCESS) {
        try {
			d3testrails.Passed(TestRun, TestCase, "It worked!");
		} catch (IOException | APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //System.out.print("Successful TestRun [" + TestRun + "] TestCase[" + TestCase + "]\n");
     }
     else
     {
         try {
 			d3testrails.Failed(TestRun, TestCase, "It failed!");
 		} catch (IOException | APIException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
        //System.out.print("Failed TestRun [" + TestRun + "] TestCase[" + TestCase + "]\n");
     }
  }*/  
     
  @AfterMethod(alwaysRun = true)
  public void takeScreenShotOnFailure(ITestResult testResult) throws IOException 
  {
	  
		  Date date = new Date(System.currentTimeMillis());
	      String dateString = date.toString();
      
    	 if (testResult.getStatus() == ITestResult.FAILURE) { 
    		 File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE); 
    		 FileUtils.copyFile(scrFile, new File(".\\Reports\\Screenshots\\" + testResult.getName()  + dateString + ".png")); 
    		 } 
  }
  

   
  @AfterClass(alwaysRun = true)
  public void terminateBrowser()
  {
	  driver.quit();
  }
  
}