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

public class QuickPayPaymentFutureDate {
	
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
		LoginActions.init(driver, timeout);
		DashboardActions.init(driver, timeout);

	}
				
	@BeforeClass(alwaysRun = true)
	@Parameters({"testRailUrl", "testRailUserName", "testRailPassWord"})
	public void initTestRails(String testRailUrl, String testRailUserName, String testRailPassWord)
	{	
		d3testrails.InitRail(testRailUrl, testRailUserName, testRailPassWord);
	}
	
	
	  @Test(priority = 3, groups = {"smoke", "regression"})
	  @Parameters({"userName", "passWord", "secretQuestion"})
	  public void verifyQuickPayPaymentFutureDate(String userName, String passWord, String secretQuestion) throws InterruptedException 
	  {
		   TestCase = "3";
		   LoginActions.loginUn(driver, userName);
		   LoginActions.loginPw(driver, passWord);
		   LoginActions.submit(driver);
		   LoginActions.secretQuestion(driver, secretQuestion);
		   LoginActions.privateDevice(driver);
		   LoginActions.submit(driver);
		   DashboardActions.quickPay(driver);
		   DashboardActions.quickPaySelectRecipient(driver);
		   Utils.isTextPresent(driver, "BEST BUY");
		   DashboardActions.bestBuyAccount(driver);
		   Utils.isTextPresent(driver, "AMOUNT");
		   driver.findElement(By.name("amount")).clear();
		   DashboardActions.setQuickPayAmount(driver, "1");
		   Thread.sleep(1000);
		   driver.findElement(By.name("scheduledDate")).clear();
		   DashboardActions.quickPayCalendarSpecificDate(driver, "12/31/2015");
		   DashboardActions.quickPaySubmitButton(driver);
		   Utils.isTextPresent(driver, "A payment of $1.00 to BEST BUY is scheduled for 12/31/2015");
		   DashboardActions.quickPayConfirm(driver);
		   Utils.isTextPresent(driver, "The payment was not scheduled. Please try again.");
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