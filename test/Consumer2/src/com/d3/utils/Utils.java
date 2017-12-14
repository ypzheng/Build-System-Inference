package com.d3.utils;
import io.selendroid.client.SelendroidDriver;
import io.selendroid.common.SelendroidCapabilities;
import io.selendroid.common.device.DeviceTargetPlatform;
import io.selendroid.standalone.SelendroidConfiguration;
import io.selendroid.standalone.SelendroidLauncher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public final class Utils  {
	// onstructor Called
	public Utils() { }
	
    public static boolean login (WebDriver driver, String username, String password) {

        WebElement myDynamicElement = driver.findElement(By.id("username"));
        myDynamicElement.sendKeys(username);
        //WebElement myDynamicElement = driver.findElement(By.name("j_username")).sendKeys("Admin");
        driver.findElement(By.id("password")).sendKeys(password);
        
        //Clicking the LOGIN Button for login in
        driver.findElement(By.name("submit")).click();       
        Utils.sleep(5000);
        if(driver.getTitle().contains("Dashboard - Nimblevox"))
        {
        	return true;
        }
        
        return false;	
      
     } //End Login
    
    
    public static void isTextPresent(WebDriver driver, String what) { 
		try 
        { 
				WebDriverWait wait = new WebDriverWait(driver, 10);
        		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[contains(.,'" + what + "')]"), what));
                assert true;
        }
        catch (NoSuchElementException e) 
        { 
                assert false;
        } 
    } //End is isTextPresent 
    

    public static void isTextNotPresent(WebDriver driver, String what) { 
    	Utils.sleep(1000);
    	List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'" + what + "')]")); 
        assert (list.size() == 0);
    }
  

    public static long createRandomInteger(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }
    
    public static String getDateMMddyyyy()
    {
    	DateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
    	Date date = new Date();
     	return dateFormat.format(date);
    }
    
    public static String getDateMMddyyyywithSlash()
    {
    	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    	Date date = new Date();
     	return dateFormat.format(date);
    }
    
    public static void sleep(int time)
    {
    	try 
    	{
    		Thread.sleep(time);
    	} 
    	catch(InterruptedException ex) 
    	{	
    		Thread.currentThread().interrupt();
    	}
    	
    }
    
    public static void takeScreenShot(WebDriver driver, String fileName)
    {
	    File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	    // Now you can do whatever you need to do with it, for example copy somewhere
	    try 
	    {
			FileUtils.copyFile(scrFile, new File("C:\\tmp\\" + fileName));
		} 
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
    }
    
    //call with Properties p = Utils.loadProperties();
    //then p.getProperty("username")
    public static Properties loadProperties(String filePath)
    {
    	Properties listProperties = new Properties();
		//System.out.println(filePath);

		FileInputStream file = null;
		try 
		{
			file = new FileInputStream(filePath);
			
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try 
		{
			 listProperties.load(file);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return listProperties;
    }
    
	public static String getAPIKey (String webserver, String username) throws IOException {
	   	String apiKey = null;
	   	//Need to lookup the apikey to issue API from this user
	       try {
	           String url = "jdbc:mysql://" + webserver + "/cas";
	           Connection conn = DriverManager.getConnection (url, "cas", "deckparty");
	           Statement stmt = conn.createStatement();
	           ResultSet rs;

	           rs = stmt.executeQuery("SELECT api_key FROM user WHERE username = '" + username + "'");
	           while ( rs.next() ) {
	            
	               apiKey = rs.getString("api_key");
	               //System.out.println("APIKEY ="+api);
	           }
	           conn.close();
	       } 
	       catch (Exception e) {
	           System.err.println("Got an exception on getAPIKey! ");
	           System.err.println(e.getMessage());
	       }
		return apiKey;
    } //End getAPIKey
	
    public static String getXMLResponseTags(StringBuffer string, String what) throws IOException  {
    	  
	    String startTag = "<" + what + ">";
	    String endTag = "</" + what + ">";
	    int start = string.indexOf(startTag) + startTag.length();
	    int end = string.indexOf(endTag);
	    String result = string.substring(start, end);
	    result = trimLeft(result);
	    result = trimRight(result);
	    //System.out.print("[" + result + "]\n");
	    return result;
	    
     }
    
    public static String trimLeft(String s) {
	    return s.replaceAll("^\\s+", "");
	}
	 
    public static String trimRight(String s) {
	    return s.replaceAll("\\s+$", "");
	}
    
    public static String getXMLResponseTags(String string, String what) throws IOException  {
    	  
	    String startTag = "<" + what + ">";
	    String endTag = "</" + what + ">";
	    int start = string.indexOf(startTag) + startTag.length();
	    int end = string.indexOf(endTag);
	    String result = string.substring(start, end);
	    result = Utils.trimLeft(result);
	    result = Utils.trimRight(result);
	    //System.out.print("[" + result + "]\n");
	    return result;
	    
     }
    
    public static Boolean waitForTitle(WebDriver driver, String title, int timeout)
    {
    	WebDriverWait wait = new WebDriverWait(driver, timeout);
    	return wait.until(ExpectedConditions.titleContains(title));
    }
    
	public enum BrowserType {
	    FIREFOX, IE, CHROME, ANDROID
	}
    
	public static WebDriver getWebDriver(String browserType, String timeout) {
		File file;
		WebDriver driver = null;
		SelendroidLauncher selendroidServer = null;
		BrowserType browser = null;
		
    	Long timeOut = Long.valueOf(timeout);
		
		switch (browserType)
    	{
    	    case "FIREFOX": 
    	    	browser = Utils.BrowserType.FIREFOX;
	        	driver = new FirefoxDriver();
	        	driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
	        	driver.manage().window().maximize();
	    	    return driver; 
    	    case "IE":
    	    	browser = Utils.BrowserType.IE;
	        	DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
	    		capabilities.setCapability("nativeEvents", false);
	        	capabilities.setCapability("ignoreProtectedModeSettings", true);
	        	capabilities.setCapability("requireWindowFocus", true);
	            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
	            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
	    	    // Create a new instance of the Internet Explorer driver
	        	file = new File(".\\libs\\IEDriverServer.exe");
	        	System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
	        	driver = new InternetExplorerDriver(capabilities);
	        	driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
	        	driver.manage().window().maximize();
	    	    return driver;
            case "CHROME":
            	browser = Utils.BrowserType.CHROME;
	        	file = new File(".\\libs\\chromedriver.exe");
	        	System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
	        	driver = new ChromeDriver();
	        	driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
	        	driver.manage().window().maximize();
	            return driver;
            case "ANDROID":
            	browser = Utils.BrowserType.ANDROID;
	            SelendroidConfiguration config = new SelendroidConfiguration();
	            selendroidServer = new SelendroidLauncher(config);
	            selendroidServer.launchSelendroid();
	            DesiredCapabilities caps = SelendroidCapabilities.android();
	            caps.setCapability(SelendroidCapabilities.PLATFORM_VERSION, DeviceTargetPlatform.ANDROID19);
	            caps.setCapability(SelendroidCapabilities.EMULATOR, true);	            
	            try {
	            		driver = new SelendroidDriver(caps);
	            	} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	            	}
	            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
	            return driver;
            default:
            	browser = Utils.BrowserType.CHROME;
	        	file = new File(".\\libs\\chromedriver.exe");
	        	System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
	        	driver = new ChromeDriver();
	        	driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
	        	driver.manage().window().maximize();
	            return driver;
            	
    	}
    	
	}

}

