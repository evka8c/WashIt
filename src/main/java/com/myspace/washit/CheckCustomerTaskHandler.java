package com.myspace.washit;

import com.myspace.washit.Customer;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
        String url = "https://washit-18577.firebaseio.com/customers.json";
        
        // Extract parameters
        Customer customer = (Customer) workItem.getParameter("Customer");
        
        // Do some work
        String json = jsonGetRequest(url);
        if (json.contains(customer.getEmail())) {
            System.out.println("USER EXISTS!!!!");
        };
        
        // Notify manager that work item has been completed
        manager.completeWorkItem(workItem.getId(), new HashMap<String,Object>());
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
    
    
    
    private static String streamToString(InputStream inputStream) 
    {
        String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
        
        return text;
    }
    
    private static String jsonGetRequest(String urlQueryString) 
    {
        String json = null;
        try {
            URL url = new URL(urlQueryString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();
            InputStream inStream = connection.getInputStream();
            json = streamToString(inStream);
        }   catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return json;
    }
}
