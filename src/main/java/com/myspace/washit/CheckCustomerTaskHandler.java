package com.myspace.washit;

import com.myspace.washit.Customer;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class CheckCustomerTaskHandler implements java.io.Serializable, WorkItemHandler
{
    static final long serialVersionUID = 1L;

    public CheckCustomerTaskHandler() {}
    
    public void	executeWorkItem(WorkItem workItem, WorkItemManager manager) 
    {
        // Extract parameters
        Customer customer = (Customer) workItem.getParameter("Customer");
        
        // Prepare url for email
        String url = "https://washit-18577.firebaseio.com/customers.json?orderBy=\"email\"&equalTo=\"" + customer.getEmail() + "\"";
        
        // Check if customer exists
        String json = jsonGetRequest(url);
        if (json.contains(customer.getEmail())) 
        {
            System.out.println("EMAIL ALREADY EXISTS!!!");
            throw new RuntimeException("There is already a customer with filled email address.");
        };
        
        System.out.println("DATA OK!!!");
        
        // Notify manager that work item has been completed
        manager.completeWorkItem(workItem.getId(), new HashMap<String,Object>());
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
    
    
    private static String streamToString(InputStream inputStream) 
    {
        String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
        return text;
    }
    
    private static String jsonGetRequest(String url) 
    {
        String json = null;
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
            
            // Header
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // Send
            con.setDoOutput(true);
            con.setInstanceFollowRedirects(false);
            con.connect();
            InputStream inStream = con.getInputStream();
            json = streamToString(inStream);
            
            int responseCode = con.getResponseCode();
		    System.out.println("\nSending 'GET' request to URL : " + url);
		    System.out.println("Response Code : " + responseCode);
		    
        }   catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return json;
    }
}
