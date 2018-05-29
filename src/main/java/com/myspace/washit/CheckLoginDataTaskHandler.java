package com.myspace.washit;

import com.myspace.washit.Customer;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.Scanner;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.util.Map;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class CheckLoginDataTaskHandler implements java.io.Serializable, WorkItemHandler
{
    static final long serialVersionUID = 1L;

    public CheckLoginDataTaskHandler() {}

    public void	executeWorkItem(WorkItem workItem, WorkItemManager manager) 
    {
        // Extract parameters
        Customer customer = (Customer) workItem.getParameter("Customer");
        
        // Prepare url
        String url = "https://washit-18577.firebaseio.com/customers.json?orderBy=\"email\"&equalTo=\"" + customer.getEmail() + "\"";

        // Check if user exists
        String jsonString = jsonGetRequest(url);
        if (!jsonString.contains(customer.getEmail())) 
        {
            System.out.println("CUSTOMER DOES NOT EXISTS!!!!");
            throw new RuntimeException("There is no customer with filled email address in system.");
        };
        
        // Get customer data json
        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        JsonObject customerJson = null;
        String firebaseId = "";
	    for (Object e : json.entrySet()) 
	    {
            Map.Entry entry = (Map.Entry) e;
            customerJson = (JsonObject) entry.getValue();
            firebaseId = (String) entry.getKey();
        }
        
        // Check if customer is activated
        Boolean activated = customerJson.getBoolean("activated");
        if (!activated)
        {
            System.out.println("CUSTOMER IS NOT ACIVATED!!!");
            throw new RuntimeException("Customer is not activated.");
        }
        customer.setActivated(activated);
        
        // Check password
        String password = customerJson.getString("password");
        if (!password.equals(customer.getPassword())) 
        {
            System.out.println("INVALID PASSWORD!!!");
            throw new RuntimeException("Invalid password.");
        }
        
        // Set other properties
        System.out.println(firebaseId);
        customer.setAddress(customerJson.getString("address"));
        customer.setFirstName(customerJson.getString("firstName"));
        customer.setLastName(customerJson.getString("lastName"));

        System.out.println("LOGIN SUCCEEDED!!!");
        
        // Notify manager that work item has been completed
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("Customer", customer);
        manager.completeWorkItem(workItem.getId(), result);
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
