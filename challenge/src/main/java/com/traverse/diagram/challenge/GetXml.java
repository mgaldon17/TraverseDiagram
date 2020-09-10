package com.traverse.diagram.challenge;
import java.io.IOException;

import org.camunda.bpm.engine.impl.util.json.JSONObject;

import com.traverse.diagram.challenge.GetRequest;

public class GetXml {
	
	public static String getXml() throws IOException {
		
		String s = GetRequest.sendGET();
		
		JSONObject j = GetRequest.convertToJSON(s);
		
		String xml = j.getString("bpmn20Xml");
		
		return xml; 
		
		
	}
	

}
