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

		SequenceFlow sequenceFlow = (SequenceFlow) modelInstance.getModelElementById("sequenceFlow_180");

		FlowNode source = sequenceFlow.getSource();
		FlowNode target = sequenceFlow.getTarget();
		
		System.out.println("Source: " + source.getId() + "\nTarget: "  + target.getId() + "\n");
		
		//Get the following nodes

		for(FlowNode node : getFlowingFlowNodes(target)) {
			
			System.out.println("Next node: " + node.getId());
			getFlowingFlowNodes(node);
		}
		
		
		
		//System.out.println(getFlowingFlowNodes(target));
		
		
	}
	
	public Collection<FlowNode> getFlowingFlowNodes(FlowNode node) {
		
		Collection<FlowNode> followingFlowNodes = new ArrayList<FlowNode>();
		
		for (SequenceFlow sequenceFlow : node.getOutgoing()) {
		  followingFlowNodes.add(sequenceFlow.getTarget());
		}
		/*
		for(FlowNode f : followingFlowNodes) {
			
			System.out.println(f.getId());
			
		}
		*/
		  
		return followingFlowNodes;
	}


	

}
