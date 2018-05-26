package com.myspace.washit;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import javax.json.Json;
import javax.net.ssl.HttpsURLConnection;

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


        URLConnection con = new URL(url).openConnection();
        HttpsURLConnection https = (HttpURLConnection)con;
        https.setRequestMethod("POST");
        https.setDoOutput(true);

        System.out.println("Customer" + customer.getId());
        
        // Notify manager that work item has been completed
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("Passphrase", passphrase);
        result.put("Customer", customer);
        manager.completeWorkItem(workItem.getId(), result);
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
}
