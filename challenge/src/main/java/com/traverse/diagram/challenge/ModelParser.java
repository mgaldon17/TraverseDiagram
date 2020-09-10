package com.traverse.diagram.challenge;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.camunda.commons.utils.IoUtil;

public class ModelParser {
	
	public void parse() throws IOException {
		
		String xml = GetXml.getXml();
		InputStream targetStream = new ByteArrayInputStream(xml.getBytes());

		BpmnModelInstance modelInstance = Bpmn.readModelFromStream(targetStream);
		
		// get the source and target element

		SequenceFlow sequenceFlow = (SequenceFlow) modelInstance.getModelElementById("sequenceFlow_178");
		System.out.println(sequenceFlow.getSource().getName());
		System.out.println(sequenceFlow.getTarget().getName());
		

		try {
			
			FlowNode source = sequenceFlow.getSource();
			FlowNode target = sequenceFlow.getTarget();
			
			
			System.out.println("Source: " + source.getId());
			
			//Get the following nodes
			System.out.println("Next node: "  + target.getId()); 

			for(FlowNode node : getFlowingFlowNodes(target)) {
				
				System.out.println("Next node: " + node.getId());

			}
			
		}catch(NullPointerException npe) {
			System.out.println("The id the of the element does not exist");
			System.exit(-1);
		} 


		
	}
	
	public Collection<FlowNode> getFlowingFlowNodes(FlowNode node) {
		
		Collection<FlowNode> followingFlowNodes = new ArrayList<FlowNode>();
		
		for (SequenceFlow sequenceFlow : node.getOutgoing()) {
		  followingFlowNodes.add(sequenceFlow.getTarget());
		}

		return followingFlowNodes;
	}


	

}
