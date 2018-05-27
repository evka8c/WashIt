package com.myspace.washit;

import com.myspace.washit.Customer;
import java.util.HashMap;
import java.io.IOException;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
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
            con.setRequestMethod("PATCH");
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
        
        // Notify manager that work item has been completed
        manager.completeWorkItem(workItem.getId(), new HashMap<String,Object>());
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
}
