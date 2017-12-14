package com.d3.dashboard;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.d3.dashboard.dashboardActions;
import com.d3.login.loginActions;
import com.d3.testrails.D3TestRails;
import com.d3.utils.Utils;
import com.gurock.testrail.APIException;
public class TransferNowCurrentDate {
	
	public WebDriver driver;
	String TestCase; 
	String TestRun = "1"; 
	dashboardActions DashboardActions = new dashboardActions();
	loginActions LoginActions = new loginActions();
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
		DashboardActions.init(driver, timeout);
		LoginActions.init(driver, timeout);


	}
				
	@BeforeClass(alwaysRun = true)
	@Parameters({"testRailUrl", "testRailUserName", "testRailPassWord"})
	public void initTestRails(String testRailUrl, String testRailUserName, String testRailPassWord)
	{	
		d3testrails.InitRail(testRailUrl, testRailUserName, testRailPassWord);
	}
	
	
	  @Test(priority = 4, groups = {"smoke", "regression"})
	  @Parameters({"userName", "passWord", "secretQuestion"})
	  public void verifyTransferNowCurrentDate(String userName, String passWord, String secretQuestion) throws InterruptedException 
	  {
		   TestCase = "4";
		   LoginActions.loginUn(driver, userName);
		   LoginActions.loginPw(driver, passWord);
		   LoginActions.submit(driver);
		   LoginActions.secretQuestion(driver, secretQuestion);
		   LoginActions.privateDevice(driver);
		   LoginActions.submit(driver);
		   DashboardActions.transferNow(driver);
		   DashboardActions.transferNowSelectRecipient(driver);
		   Utils.isTextPresent(driver, "My Credit Card Account");
		   DashboardActions.transferNowMyCreditCardAccount(driver);
		   Utils.isTextPresent(driver, "AMOUNT");
		   driver.findElement(By.name("amount")).clear();
		   DashboardActions.setTransferNowAmount(driver, "1");
		   DashboardActions.transferNowSubmitButton(driver);
		   Utils.isTextPresent(driver, "A transfer of $1.00 to My Deposit - Savings Account is scheduled for ");
		   DashboardActions.transferNowConfirm(driver);
		   Utils.isTextPresent(driver, "Success");
	  } 

  @AfterMethod(alwaysRun = true)
  public void updateTestRails(ITestResult result) 
  {
     if (result.getStatus() == ITestResult.SUCCESS) {
        try {
			d3testrails.Passed(TestRun, TestCase, "It worked!");
		} catch (IOException | APIException e) {
			e.printStackTrace();
		}
        //System.out.print("Successful TestRun [" + TestRun + "] TestCase[" + TestCase + "]\n");
     }
     else
     {
         try {
 			d3testrails.Failed(TestRun, TestCase, "It failed!");
 		} catch (IOException | APIException e) {
 			e.printStackTrace();
 		}
        //System.out.print("Failed TestRun [" + TestRun + "] TestCase[" + TestCase + "]\n");
     }
  }  
     
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