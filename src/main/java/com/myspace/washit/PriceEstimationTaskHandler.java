package com.myspace.washit;

import com.myspace.washit.Order;
import java.util.Map;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.StringReader;
//import java.text.SimpleDateFormat;
//import java.text.DateFormat;
//import java.util.Locale;

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
        
        // Get price list
        String url = "http://www.mocky.io/v2/5b0d551531000058009d5632";
        
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
            
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
            
            // Response code
            int responseCode = con.getResponseCode();
		    System.out.println("\nSending 'GET' request to URL : " + url);
		    System.out.println("Response Code : " + responseCode);
		    
        }   catch (Exception e) {
            e.printStackTrace();
        }
        
        String deposit = "10000";
        
        // Notify manager that work item has been completed
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("Deposit", deposit);
        result.put("Order", order);
        manager.completeWorkItem(workItem.getId(), result);
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
}
