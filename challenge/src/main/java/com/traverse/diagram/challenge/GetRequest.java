package com.traverse.diagram.challenge;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import org.camunda.bpm.engine.impl.util.json.JSONException;
import org.camunda.bpm.engine.impl.util.json.JSONObject;


public class GetRequest {
	
	public static final String URL = "https://n35ro2ic4d.execute-api.eu-central"
			+ "-1.amazonaws.com/prod/engine-rest/process-definition/key/invoice/xml";
	
	public static String sendGET() throws IOException {
		
		URL obj = new URL(URL);
		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
		connection.setRequestMethod("GET");
		
		
		try {
			
			int responseCode = connection.getResponseCode();
			System.out.println("GET Response Code :: " + responseCode);
			
			if (responseCode == HttpURLConnection.HTTP_OK) { // If connection is OK
				
				
				BufferedReader in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					
					response.append(inputLine);
				}
				
				in.close();

				// Print result and pass response
				//System.out.println(response.toString());
				
				return response.toString();
				
			} else {
				System.out.println("GET request not worked");
			}
			
		} catch (UnknownHostException uhe) {
			
			System.out.println("Wrong URL");
			
		}
		return null;
	}

	public static JSONObject convertToJSON(String json) {
		
		try {
		     JSONObject jsonObject = new JSONObject(json);
		     
		     return jsonObject;
		     
		} catch (JSONException err){
			
			System.out.println("The object gathered is not a JSON file");
			return new JSONObject();
		     
		}

	}

}

	
