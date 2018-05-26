package com.myspace.washit;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import javax.json.Json;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.DataOutputStream;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class CreateCustomerTaskHandler implements java.io.Serializable, WorkItemHandler
{
    static final long serialVersionUID = 1L;

    public CreateCustomerTaskHandler() {}

    public void	executeWorkItem(WorkItem workItem, WorkItemManager manager) 
    {
        String url = "https://washit-18577.firebaseio.com/customers.json";
        
        // Extract parameters
        Customer customer = (Customer) workItem.getParameter("Customer");
        
        // Generate passphrase
        String passphrase = Long.toHexString(Double.doubleToLongBits(new Random().nextLong()));
        customer.setPassphrase(passphrase);
        
        // Store customer's data
        String json = Json.createObjectBuilder()
            .add("activated", customer.getActivated())
            .add("address", customer.getAddress())
            .add("email", customer.getEmail())
            .add("firstName", customer.getFirstName())
            .add("lastName", customer.getLastName())
            .add("passphrase", customer.getPassphrase())
            .build()
            .toString();
        
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
            
            // Header
            con.setRequestMethod("POST");
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
		    System.out.println("\nSending 'POST' request to URL : " + url);
		    System.out.println("Post parameters : " + json);
		    System.out.println("Response Code : " + responseCode);

		    BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
	    	String inputLine;
		    StringBuffer response = new StringBuffer();

		    while ((inputLine = in.readLine()) != null) {
			    response.append(inputLine);
		    }
		    in.close();
		
		    // Respond
		    System.out.println(response.toString());
		    
        } catch (Exception e){
            e.printStackTrace();
        }

        // Notify manager that work item has been completed
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("Passphrase", passphrase);
        result.put("Customer", customer);
        manager.completeWorkItem(workItem.getId(), result);
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
}
