package com.myspace.washit;

import com.myspace.washit.Order;
import com.myspace.washit.Customer;
import java.util.Map;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.StringReader;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Locale;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class CreateOrderTaskHandler implements java.io.Serializable, WorkItemHandler
{
    static final long serialVersionUID = 1L;

    public CreateOrderTaskHandler() {}

    public void	executeWorkItem(WorkItem workItem, WorkItemManager manager) 
    {
        String url = "https://washit-18577.firebaseio.com/orders.json";
        
        // Extract parameters
        Order order = (Order) workItem.getParameter("Order");
        Customer customer = (Customer) workItem.getParameter("Customer");
        
        // Formatter for ISO8601 date
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH);

        // Store order data
        String json = Json.createObjectBuilder()
            .add("addClothesHangers", order.getAddClothesHangers())
            .add("blouses", order.getBlouses())
            .add("customerFirebaseId", customer.getFirebaseId())
            .add("delivered", false)
            .add("deliveryDate", format.format(order.getDeliveryDate()))
            .add("depositPaid", true)
            .add("estimatedPrice", order.getEstimatedPrice())
            .add("estimatedWeight", order.getEstimatedWeight())
            .add("ironing", order.getIroning())
            .add("jeans", order.getJeans())
            .add("jumpers", order.getJumpers())
            .add("laundryProgram", order.getLaundryProgram())
            .add("orderPaid", false)
            .add("pickUpDate", format.format(order.getPickUpDate()))
            .add("pickedUp", false)
            .add("realPrice", 0.0)
            .add("realWeight", 0.0)
            .add("shirts", order.getShirts())
            .add("shorts", order.getShorts())
            .add("tShirts", order.gettShirts())
            .build()
            .toString();
        
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
            
            // Header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            
            // Send
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		    wr.writeBytes(json);
		    wr.flush();
		    wr.close();
		    
		    int responseCode = con.getResponseCode();
		    System.out.println("\nSending 'POST' request to URL : " + url);
		    System.out.println("Post parameters : " + json);
		    System.out.println("Response Code : " + responseCode);

		    BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
	    	String inputLine;
		    StringBuffer response = new StringBuffer();

		    while ((inputLine = in.readLine()) != null) {
			    response.append(inputLine);
		    }
		    in.close();
		    
		    // Store Firebase Id
		    JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
            JsonObject object = jsonReader.readObject();
            jsonReader.close();
            String firebaseId = object.getString("name");
            order.setFirebaseId(firebaseId);

        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("ORDER CREATED!!!");

        // Notify manager that work item has been completed
        manager.completeWorkItem(workItem.getId(), new HashMap<String,Object>());
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
}