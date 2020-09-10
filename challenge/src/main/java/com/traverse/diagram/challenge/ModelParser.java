package com.traverse.diagram.challenge;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.camunda.commons.utils.IoUtil;

import com.sun.glass.ui.CommonDialogs.Type;

public class ModelParser {
	
	public final Collection <FlowNode> path = null ; 
	public BpmnModelInstance parse() throws IOException {
		
		String xml = GetXml.getXml();
		InputStream targetStream = new ByteArrayInputStream(xml.getBytes());

		BpmnModelInstance modelInstance = Bpmn.readModelFromStream(targetStream);
		final FlowNode start_node = (FlowNode) modelInstance.getModelElementById("invoice_approved");
		final FlowNode end_node = (FlowNode) modelInstance.getModelElementById("invoiceProcessed");

		System.out.println("Start node: " + start_node.getId());
		find(start_node, end_node);
		return modelInstance;
		
	}
	
	public void find(FlowNode start, FlowNode end) throws IOException {
		
		try {

			Collection<FlowNode> list_nodes = getFlowingFlowNodes(start);
			
			for(FlowNode node : list_nodes) {
				
				if(list_nodes.contains(end)) {
					
					System.out.println("End node: " + node.getId() + "." + "\n\nEnd of path.");
					//path.add(node);

				}else{ 
					
					System.out.println("Next node: " + node.getId());
					//Add possible paths here for the nodes
					//find(node, end);
					
				}
			}
			
		}catch(NullPointerException npe) {
			//System.out.println("\nThe id the of the element does not exist");
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
