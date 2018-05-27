package com.myspace.washit;

import com.myspace.washit.Customer;
import java.util.HashMap;
import java.io.InputStream;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class DeleteCustomerTaskHandler implements java.io.Serializable,  WorkItemHandler
{
    static final long serialVersionUID = 1L;

    public DeleteCustomerTaskHandler() {
    }

    public void	executeWorkItem(WorkItem workItem, WorkItemManager manager) 
    {
        // Extract parameters
        Customer customer = (Customer) workItem.getParameter("Customer");
        
        // Prepare customer to delete url
        String url = "https://washit-18577.firebaseio.com/customers/" + customer.getFirebaseId() + ".json";
        
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
            
            // Header
            con.setRequestMethod("DELETE");
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
        
        // Notify manager that work item has been completed
        manager.completeWorkItem(workItem.getId(), new HashMap<String,Object>());
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
}
