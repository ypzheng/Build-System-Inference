package com.d3.testrails;

import com.gurock.testrail.APIClient;
import com.gurock.testrail.APIException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.HashMap;

import org.json.simple.JSONObject;
 
public class D3TestRails
{
	
	//status_id 1 = Passed 
			//2 = Blocked 
			//4 = Retest 
			//5 = Failed
	
	APIClient client;
	
	public void InitRail(String url, String userName, String password) {
		client = new APIClient(url);
		client.setUser(userName);
		client.setPassword(password);
		
	}

	public void Passed(String TestRun, String TestCase, String Comment) throws MalformedURLException, IOException, APIException {
		Map data = new HashMap();
		data.put("status_id", new Integer(1));
		data.put("comment", Comment);
		JSONObject r = (JSONObject) client.sendPost("add_result_for_case/" + TestRun + "/" + TestCase, data);
	}
	
	public void Blocked(String TestRun, String TestCase, String Comment) throws MalformedURLException, IOException, APIException {
		Map data = new HashMap();
		data.put("status_id", new Integer(2));
		data.put("comment", Comment);
		JSONObject r = (JSONObject) client.sendPost("add_result_for_case/" + TestRun + "/" + TestCase, data);
	}
	
	public void Retest(String TestRun, String TestCase, String Comment) throws MalformedURLException, IOException, APIException {
		Map data = new HashMap();
		data.put("status_id", new Integer(4));
		data.put("comment", Comment);
		JSONObject r = (JSONObject) client.sendPost("add_result_for_case/" + TestRun + "/" + TestCase, data);
	}
	
	public void Failed(String TestRun, String TestCase, String Comment) throws MalformedURLException, IOException, APIException {
		Map data = new HashMap();
		data.put("status_id", new Integer(5));
		data.put("comment", Comment);
		JSONObject r = (JSONObject) client.sendPost("add_result_for_case/" + TestRun + "/" + TestCase, data);
	}
}