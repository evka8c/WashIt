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
        Order order = (Order) workItem.getParameter("Order");
        
        System.out.println("SHIRTS?: " + order.getShirts());
        System.out.println("BLOUSES?: " + order.getBlouses());
        System.out.println("T-SHIRTS?: " + order.gettShirts());
        System.out.println("JEANS?: " + order.getJeans());
        System.out.println("SHORTS?: " + order.getShorts());
        System.out.println("JUMPERS?: " + order.getJumpers());
        System.out.println("ESTIMATED WEIGHT?: " + order.getEstimatedWeight());
        System.out.println("LAUNDRY PROGRAM?: " + order.getLaundryProgram());
        System.out.println("IRONING?: " + order.getIroning());
        System.out.println("ADD CLOTHES HANGERS?: " + order.getAddClothesHangers());
        System.out.println("PICK UP DATE?: " + order.getPickUpDate());
        System.out.println("DELIVERY DATE?: " + order.getDeliveryDate());


        
        // Notify manager that work item has been completed
        manager.completeWorkItem(workItem.getId(), new HashMap<String,Object>());
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
}
