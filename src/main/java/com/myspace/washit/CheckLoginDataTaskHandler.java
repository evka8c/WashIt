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

public class CheckLoginDataTaskHandler implements java.io.Serializable, WorkItemHandler
{
    static final long serialVersionUID = 1L;

    public CheckLoginDataTaskHandler() {}

    public void	executeWorkItem(WorkItem workItem, WorkItemManager manager) 
    {
        String url = "https://washit-18577.firebaseio.com/customers.json";
        
        // Extract parameters
        Customer customer = (Customer) workItem.getParameter("Customer");
        
        // Check if user exists
        String jsonString = jsonGetRequest(url);
        if (!jsonString.contains(customer.getEmail())) {
            System.out.println("CUSTOMER DOES NOT EXISTS!!!!");
            throw new RuntimeException("There is no customer with filled email address in system.");
        };
        
        System.out.println("CUSTOMER DOES EXISTS!!!!");
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Password: " + customer.getPassword());
        
        JsonObject json = Json.createReader(new StringReader(jsonString)).readObject();
        
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
		    
		    // Respond
		    System.out.println(json);
		    
        }   catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return json;
    }
}
