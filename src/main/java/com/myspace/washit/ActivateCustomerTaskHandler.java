package com.myspace.washit;

import com.myspace.washit.Customer;
import java.util.HashMap;
import java.io.IOException;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import javax.json.Json;
import java.io.DataOutputStream;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class ActivateCustomerTaskHandler implements java.io.Serializable,  WorkItemHandler
{
    static final long serialVersionUID = 1L;

    public ActivateCustomerTaskHandler() {}

    public void	executeWorkItem(WorkItem workItem, WorkItemManager manager) 
    {
        // Extract parameters
        Customer customer = (Customer) workItem.getParameter("Customer");
        String registrationCode = (String) workItem.getParameter("RegistrationCode");
        
        // Check registration code
        if (!customer.getRegistrationCode().equals(registrationCode)) 
        {
            System.out.println("INVALID REGISTRATION CODE!!!");
            throw new RuntimeException("The entered registration code is invalid.");
        };
        
        // Prepare customer to activate url
        String url = "https://washit-18577.firebaseio.com/customers/" + customer.getFirebaseId() + "/.json";
        
        // Prepare activated json
        String json = Json.createObjectBuilder()
            .add("activated", true)
            .build()
            .toString();
        
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
            
            // Header
            con.setRequestMethod("POST");
            con.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // Send
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		    wr.writeBytes(json);
		    wr.flush();
		    wr.close();

            int responseCode = con.getResponseCode();
		    System.out.println("\nSending 'PATCH' request to URL : " + url);
		    System.out.println("Patch parameters : " + json);
		    System.out.println("Response Code : " + responseCode);
		    
        }   catch (IOException ex) {
            ex.printStackTrace();
        }
        
        System.out.println("CUSTOMER ACTIVATED!!!");
        
        // Notify manager that work item has been completed
        manager.completeWorkItem(workItem.getId(), new HashMap<String,Object>());
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
}
