package com.myspace.washit;

import com.myspace.washit.Customer;
import java.util.HashMap;

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
        String url = (String) workItem.getParameter("Url");
        Customer customer = (Customer) workItem.getParameter("Customer");
        
        // Do some work
        System.out.println("Url: " + url);
        System.out.println("Customer: " + customer);
        
        // Notify manager that work item has been completed
        manager.completeWorkItem(workItem.getId(), new HashMap<String,Object>());
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
}
