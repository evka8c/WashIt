package com.myspace.washit;

import java.util.HashMap;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class CreateCustomerTaskHandler implements java.io.Serializable, WorkItemHandler
{
    static final long serialVersionUID = 1L;

    public CreateCustomerTaskHandler() {}

    public void	executeWorkItem(WorkItem workItem, WorkItemManager manager) 
    {
        String url = "https%3A%2F%2Fwashit%2D18577%2Efirebaseio%2Ecom%2Fcustomers%2Ejson";
        
        // Extract parameters
        Customer customer = (Customer) workItem.getParameter("Customer");
        
        // Do some work
        
        // Notify manager that work item has been completed
        manager.completeWorkItem(workItem.getId(), new HashMap<String,Object>());
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
}
