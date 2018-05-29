package com.myspace.washit;

import com.myspace.washit.Order;
import java.util.Map;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Locale;

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
        Date pickupDate = order.getPickUpDate();
        Date deliveryDate = order.getDeliveryDate();
        
        // Reservation system url
        String url = "http://www.convert-unix-time.com/api?timestamp=now&timezone=prague&format=iso8601";
        
        // Check availability of pick up and delivery dates and times
        Date availableDate = null;
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            
            // Header
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // Send
            con.setDoOutput(true);
            con.setInstanceFollowRedirects(false);
            con.connect();
            
            // Response
            BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
	    	String inputLine;
		    StringBuffer response = new StringBuffer();

		    while ((inputLine = in.readLine()) != null) {
			    response.append(inputLine);
		    }
		    in.close();
		    
		    JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
            JsonObject object = jsonReader.readObject();
            jsonReader.close();
            String dateString = object.getString("localDate");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH);
            availableDate = format.parse(dateString);
            
            // Response code
            int responseCode = con.getResponseCode();
		    System.out.println("\nSending 'GET' request to URL : " + url);
		    System.out.println("Response Code : " + responseCode);
		    
        }   catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("INVALID DELIVERY DATE!!! PD " + pickupDate);
        System.out.println("INVALID DELIVERY DATE!!! DT " + deliveryDate);
        System.out.println("INVALID DELIVERY DATE!!! TS " + availableDate);
        
        if (pickupDate.compareTo(availableDate) <= 0) 
        {
            System.out.println("SELECTED DATE IS NOT AVAILABLE");
            throw new RuntimeException("The selected date is not available.");
        }
        
        // Check if delivery is later than pick up
        if (pickupDate.compareTo(deliveryDate) >= 0) 
        {
            System.out.println("INVALID DELIVERY DATE!!!");
            throw new RuntimeException("Delivery date must be greater than pick up date.");
        }
        
        // Notify manager that work item has been completed
        manager.completeWorkItem(workItem.getId(), new HashMap<String,Object>());
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
}
    