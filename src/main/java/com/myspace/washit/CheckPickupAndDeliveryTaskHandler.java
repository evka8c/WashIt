package com.myspace.washit;

import com.myspace.washit.Order;
import java.util.Map;
import java.util.HashMap;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class CheckPickupAndDeliveryTaskHandler implements java.io.Serializable, WorkItemHandler
{
    static final long serialVersionUID = 1L;

    public CheckPickupAndDeliveryTaskHandler() {}

    public void	executeWorkItem(WorkItem workItem, WorkItemManager manager) 
    {
        // Extract parameters
        Order order = (Order) workItem.getParameter("Order");
        long pickupDate = order.getPickUpDate().getTime();
        long deliveryDate = order.getDeliveryDate().getTime();
        
        System.out.println("PICK UP DATE?: " + pickupDate);
        System.out.println("DELIVERY DATE?: " + deliveryDate);
        
        // Create 
        String json = Json.createObjectBuilder()
            .add("pickupDate", pickupDate)
            .add("deliveryDate", deliveryDate)
            .build()
            .toString();

        System.out.println("CUSTOMER CREATED!!!");
        
        // Notify manager that work item has been completed
        manager.completeWorkItem(workItem.getId(), new HashMap<String,Object>());
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
}
