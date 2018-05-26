package com.myspace.washit;

import java.util.Map;
import java.util.HashMap;
import org.apache.commons.text.RandomStringGenerator;

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
        
        // Activate customer
        
        // Store customer's data
        
        // Notify manager that work item has been completed
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("ActivationCode", "12345678");
        manager.completeWorkItem(workItem.getId(), result);
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
}
