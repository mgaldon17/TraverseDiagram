package com.traverse.diagram.challenge;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.commons.utils.IoUtil;

public class ModelParser {
	
	public void parse() throws IOException {
		
		String xml = GetXml.getXml();
		InputStream targetStream = new ByteArrayInputStream(xml.getBytes());
		
		BpmnModelInstance modelInstance = Bpmn.readModelFromStream(targetStream);
		System.out.println(modelInstance);
	}
	

}
