package com.traverse.diagram.challenge;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

public class ModelParser {
	
	boolean finish;

	public ArrayList<FlowNode> start_end = new ArrayList<FlowNode>();
	public boolean endReached = false;
	Stack<FlowNode> stack = new Stack<FlowNode>();
	int cout2 = 0;
	int cout=0;
	
	public BpmnModelInstance parse(String start, String end) throws IOException {
		
		String xml = GetXml.getXml();
		InputStream targetStream = new ByteArrayInputStream(xml.getBytes());

		BpmnModelInstance modelInstance = Bpmn.readModelFromStream(targetStream);
		
		final FlowNode start_node = (FlowNode) modelInstance.getModelElementById(start);
		final FlowNode end_node = (FlowNode) modelInstance.getModelElementById(end);
		
		start_end.add(start_node);
		start_end.add(end_node);
		
		try {
			start_node.getId();
			end_node.getId();
		} catch (NullPointerException npe) {
			System.out.println("Start  or end node not found");
			System.exit(-1);
		}
		find(start_node, end_node);
		return modelInstance;
	}
	
	public void printPath() {
		
		String result = "The path from " + start_end.get(0).getId() + " and " + start_end.get(1).getId() +" is: [";
		ArrayList <String> result_list = new ArrayList<String>();
		
		while(!stack.empty()) {

			result_list.add(stack.pop().getId());
			
		}
		
		Collections.reverse(result_list);
		
		for(int i=0; i<result_list.size(); i++) {
			
			result += result_list.get(i);
			
			if (i<result_list.size()-1) {
				result += ", ";
			}else {
				
			}
		}
		
		result+="]";
		
		System.out.println(result);
		
	}
	
	public boolean checkIfEnd(FlowNode end, FlowNode node) throws IOException {
		
		if(node == end) {
			stack.push(end);
			printPath();
			System.exit(-1);
			return finish = true;

		}else{
			

			return finish = false;
		}
	}
	
	public void find(FlowNode start, FlowNode end) throws IOException {
		
		try {
			
			if(!stack.contains(start)) {
				stack.add(start);
			}

			List<FlowNode> following = getFlowingFlowNodes(start); //Get all nodes
			
			if (following.isEmpty()) {
				System.out.println("Path not found");
				System.exit(-1);
			}
			
			
			if(following.size() == 1 && !checkIfEnd(end, following.get(0))) { //Only 1 following node and not the end
				
				
				stack.push(following.get(0));
				
				find(following.get(0), end);
				
			}else if (following.size() == 1 && checkIfEnd(end, following.get(0))){ //Only 1 following node and  the end
				
				checkIfEnd(end, following.get(0));
				
			}else if(following.size() > 1 && checkIfEnd(end, following.get(0))){ //Several following node and the end
				
				checkIfEnd(end, following.get(0));
			
			}else if(following.size() != 1 && !checkIfEnd(end, following.get(0))) { //Several following nodes and not the end
				
				scanPath(following);
				
			}
			
		
		}catch(NullPointerException npe) {

				System.out.println("\nThe id the of the element does not exist");
				System.exit(-1);
		} 
	}
	
	public void scanPath(List<FlowNode> following) throws IOException {
		
		FlowNode end = start_end.get(1);
		
		
		if(following.get(0).getId().equals("reviewInvoice") && !checkIfEnd(end, following.get(0)) && cout !=0) {
			//System.out.println("Next: review successful");
			cout++;
			checkIfEnd(end, following.get(0));
			find(following.get(0), end);
			
		}else if(following.get(1).getId().equals("prepareBankTransfer")&& !checkIfEnd(end, following.get(0))) {
			//System.out.println("Next: service task 1");
			checkIfEnd(end, following.get(1));
			find(following.get(1), end);
			
		}else if(following.get(0).getId().equals("reviewSuccessful_gw")&& !checkIfEnd(end, following.get(0)) && cout2!=0) {
			//System.out.println("Next: invoice processed");
			cout2++;
			checkIfEnd(end, following.get(0));
			find(following.get(0), end);
			
		}else if(following.get(1).getId().equals("approveInvoice")&& !checkIfEnd(end, following.get(0))) {
			//System.out.println("Next: invoice approved");
			checkIfEnd(end, following.get(1));
			find(following.get(1), end);
			
		}		
	}

	public List<FlowNode> getFlowingFlowNodes(FlowNode node) {
		
		List<FlowNode> followingFlowNodes = new ArrayList<FlowNode>();
		
		for (SequenceFlow sequenceFlow : node.getOutgoing()) {
		  followingFlowNodes.add(sequenceFlow.getTarget());
		}

		return followingFlowNodes;
	}

}
