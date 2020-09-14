package com.traverse.diagram.challenge;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
	
	boolean finish = false ;
	public List<FlowNode> list;
	public final Collection <FlowNode> path = null ; 
	boolean hasMore = false;
	public ArrayList<String> saved_nodes = new ArrayList<String>();
	public ArrayList<String> start_end = new ArrayList<String>();
	
	public BpmnModelInstance parse() throws IOException {
		
		String xml = GetXml.getXml();
		InputStream targetStream = new ByteArrayInputStream(xml.getBytes());

		BpmnModelInstance modelInstance = Bpmn.readModelFromStream(targetStream);
		
		final FlowNode start_node = (FlowNode) modelInstance.getModelElementById("StartEvent_1");
		final FlowNode end_node = (FlowNode) modelInstance.getModelElementById("prepareBankTransfer");
		
		start_end.add(start_node.getId());
		start_end.add(end_node.getId());

		System.out.println("Start node: " + start_node.getId() + "\n");
		
		

		find(start_node, end_node);
		return modelInstance;
		
	}
	
	public void checkIfEnd(FlowNode end, List<FlowNode> list) throws IOException {
		

		if(list.contains(end)) {
			finish = true;
			System.out.println("End node reached: " + end.getId() + "." + "\n\n----End of path.----\n");
			
			System.out.println("The path from " +start_end.get(0) + " to " + start_end.get(1) + " is \n");
			
			for(int i = 0; i<saved_nodes.size(); i++) {

				System.out.println(saved_nodes.get(i));

			}
			
			System.exit(-1);

		}else {
			
			find(list.get(0), end);
		}
		
	}
	
	public void find(FlowNode start, FlowNode end) throws IOException {
		
		try {
			
			List<FlowNode> list_nodes = getFlowingFlowNodes(start); //Get all nodes
			
			//Save next nodes in the list
			if(list_nodes.size() == 1) {
				
				System.out.println("Only 1 next node found: " + list_nodes.get(0).getId());

				save(list_nodes.get(0).getId());

				checkIfEnd(end, list_nodes);
				
				
			}else{
				
				String s1 = "\nSeveral nodes were found: ";

				for(int i = 0; i<list_nodes.size(); i++) {
					
					s1 += list_nodes.get(i).getId();
					
					if(i == list_nodes.size() -1) {
						s1 += ".";
					}else {
						s1+= ", ";
					}
					

					
			
				}
				
				System.out.println(s1);
				
		
			}
			
			
			


		}catch(NullPointerException npe) {
			//System.out.println("\nThe id the of the element does not exist");
			System.exit(-1);
		} 
	}
	
	public void save(String node_to_save) {
		

		saved_nodes.add(node_to_save);
		
	}

	

	public List<FlowNode> getFlowingFlowNodes(FlowNode node) {
		
		List<FlowNode> followingFlowNodes = new ArrayList<FlowNode>();
		
		for (SequenceFlow sequenceFlow : node.getOutgoing()) {
		  followingFlowNodes.add(sequenceFlow.getTarget());
		}

		return followingFlowNodes;
	}


	

}
