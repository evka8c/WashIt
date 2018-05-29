package com.myspace.washit;

import com.myspace.washit.Order;
import java.util.Map;
import java.util.HashMap;

import java.net.HttpURLConnection;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.StringReader;

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
        
        // Get price list
        String url = "http://www.mocky.io/v2/5b0d65c4310000b20c9d568c";
        JsonObject priceListJson = null;
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
            priceListJson = object;
            
            // Response code
            int responseCode = con.getResponseCode();
		    System.out.println("\nSending 'GET' request to URL : " + url);
		    System.out.println("Response Code : " + responseCode);
		    System.out.println("PRICING LIST RETRIEVED!!!");
		    
        }   catch (Exception e) {
            e.printStackTrace();
        }
        
        // Do price estimate based on order's data
        String amount = String.valueOf(getPriceEstimate(order, priceListJson));
        System.out.println("PRICE ESTIMATION DONE!!!");
        
        // Notify manager that work item has been completed
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("Amount", amount);
        result.put("Order", order);
        manager.completeWorkItem(workItem.getId(), result);
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
    
    
    private Double getPriceEstimate(Order order, JsonObject priceListJson)
    {
        Double priceEstimate = 0.0;
        int kinds = 0;
        
        // Estimated weight
        Double estimatedWeight = order.getEstimatedWeight();

        // Laundry type
        JsonObject laundryTypeJson = priceListJson.getJsonObject("laundryType");
        if (order.getShirts()) 
        {
            priceEstimate += laundryTypeJson.getJsonNumber("shirts").doubleValue();
            kinds++;
        }
        if (order.getBlouses()) 
        {
            priceEstimate += laundryTypeJson.getJsonNumber("blouses").doubleValue();
            kinds++;
        }
        if (order.gettShirts()) 
        {
            priceEstimate += laundryTypeJson.getJsonNumber("tShirts").doubleValue();
            kinds++;
        }
        if (order.getJeans()) 
        {
            priceEstimate += laundryTypeJson.getJsonNumber("jeans").doubleValue();
            kinds++;
        }
        if (order.getShorts()) 
        {
            priceEstimate += laundryTypeJson.getJsonNumber("shorts").doubleValue();
            kinds++;
        }
        if (order.getJumpers()) 
        {
            priceEstimate += laundryTypeJson.getJsonNumber("jumpers").doubleValue();
            kinds++;
        }
        priceEstimate = priceEstimate / kinds * estimatedWeight;
        
        // Laundry program
        JsonObject laundryProgramJson = priceListJson.getJsonObject("laundryProgram");
        if (order.getLaundryProgram().equals("Economy")) {
            priceEstimate = priceEstimate * laundryProgramJson.getJsonNumber("economy").doubleValue();
        }
        else if (order.getLaundryProgram().equals("Business")) {
            priceEstimate = priceEstimate * laundryProgramJson.getJsonNumber("business").doubleValue();
        }
        else {
            priceEstimate = priceEstimate * laundryProgramJson.getJsonNumber("firstClass").doubleValue();
        }
        
        // Additional services
        JsonObject additionalServicesJson = priceListJson.getJsonObject("additionalServices");
        if (order.getIroning())  {
            priceEstimate += additionalServicesJson.getJsonNumber("ironing").doubleValue() * estimatedWeight;
        }
        if (order.getAddClothesHangers())  {
            priceEstimate += additionalServicesJson.getJsonNumber("clothesHangers").doubleValue();
        }

        return priceEstimate;
    }
}
