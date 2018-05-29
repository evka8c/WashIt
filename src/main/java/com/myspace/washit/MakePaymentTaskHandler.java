package com.myspace.washit;

import com.myspace.washit.PaymentInfo;
//import java.util.Map;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.StringReader;
import java.net.HttpURLConnection;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class MakePaymentTaskHandler implements java.io.Serializable, WorkItemHandler 
{
    static final long serialVersionUID = 1L;

    public MakePaymentTaskHandler() {}

    public void	executeWorkItem(WorkItem workItem, WorkItemManager manager) 
    {
        // Extract parameters
        PaymentInfo paymentInfo = (PaymentInfo) workItem.getParameter("PaymentInfo");
        
        String url = "http://www.mocky.io/v2/5b0dbbe13200004d00c1982d";
        
        // Send payment info
        String json = Json.createObjectBuilder()
            .add("cardNumber", paymentInfo.getCardNumber())
            .add("cvv", customer.getCvv())
            .add("name", customer.getName())
            .add("validThrough", customer.getValidThrough())
            .build()
            .toString();
        
        boolean paymentSuceeded = false;
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            
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
		    
		    System.out.println("AAAAAAA: " + response.toString());
		    
		    // Store Firebase Id
		    //JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
            //JsonObject object = jsonReader.readObject();
            //jsonReader.close();
            //String firebaseId = object.getString("name");
            //customer.setFirebaseId(firebaseId);

        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("!!!");

        // Notify manager that work item has been completed
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("PaymentSucceeded", paymentSuceeded);
        manager.completeWorkItem(workItem.getId(), result);
    }
    
    public void	abortWorkItem(WorkItem workItem, WorkItemManager manager) {}
}
