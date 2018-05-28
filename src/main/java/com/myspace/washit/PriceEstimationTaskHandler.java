package com.myspace.washit;

import com.myspace.washit.Order;
import java.util.HashMap;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class PriceEstimationTaskHandler implements java.io.Serializable, WorkItemHandler
{
    static final long serialVersionUID = 1L;

    public PriceEstimationTaskHandler() {}

    public void	executeWorkItem(WorkItem workItem, WorkItemManager manager) 
    {
        // Extract parameters
        //Order order = (Order) workItem.getParameter("Order");
        Customer customer = (Customer) workItem.getParameter("Order");
        
        System.out.println("DATA OK!!!: " + customer.getFirstName());
        System.out.println("DATA OK!!!: " + customer.getLastName());
        System.out.println("DATA OK!!!: " + customer.getEmail());
        System.out.println("DATA OK!!!: " + customer.getPassword());
        System.out.println("DATA OK!!!: " + customer.getAddredd());
        System.out.println("DATA OK!!!: " + customer.getActivated());
        
        // Notify manager that work item has been completed
        manager.completeWorkItem(workItem.getId(), new HashMap<String,Object>());
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
}
