package com.traverse.diagram.challenge;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

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
	
	boolean finish;

	public ArrayList<FlowNode> start_end = new ArrayList<FlowNode>();
	public final ArrayList<FlowNode> double_node = new ArrayList<FlowNode>();
	public ArrayList<FlowNode> blacklist = new ArrayList<FlowNode>();
	public boolean endReached = false;
	Stack<FlowNode> stack = new Stack<FlowNode>();
	
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
		} catch (NullPointerException npe) {
			System.out.println("Start node not found");
			System.exit(-1);
		}
		find(start_node, end_node);
		return modelInstance;
	}
	
	public void printPath() {
		
		String result = "The path from " + start_end.get(0).getId() + " and " + start_end.get(1).getId() +" is: [";
		
		while(!stack.empty()) {
			
			result+=stack.pop().getId();
			if (stack.empty()) {
				
			}else {
				result += ", ";
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
			//System.out.println(following.get(0).getId());
			if (following.isEmpty()) {
				System.out.println("Reached the end. Path not found");
				endReached = true;
			}
			
			//Save next nodes in the list
			if(following.size() == 1 && !checkIfEnd(end, following.get(0))) { //Only 1 following node and not the end
				
				
				System.out.println("Only 1 next node found: " + following.get(0).getId());
				stack.push(following.get(0));
				find(following.get(0), end);
				
			}else if (following.size() == 1 && checkIfEnd(end, following.get(0))){ //Only 1 following node and  the end
				
				//End
				
			}else if(following.size() > 1 && checkIfEnd(end, following.get(0))){ //Several following node and the end
					
			
			}else if(following.size() != 1 && !checkIfEnd(end, following.get(0))) { //Several following nodes and not the end
				
				System.out.println(following.get(1).getId());
				scanPath(following);
				
			}
			
		
		}catch(NullPointerException npe) {

				System.out.println("\nThe id the of the element does not exist");
				System.exit(-1);
		} 
	}
	
	public void scanPath(List<FlowNode> following) throws IOException {
		
		
		List<FlowNode> path1 = getFlowingFlowNodes(following.get(0));
		List<FlowNode> path2 = getFlowingFlowNodes(following.get(1));
		FlowNode end = start_end.get(1);
		
		
		//System.out.println(following.get(0).getId() + "--------> " + path1.get(0).getId());
		//System.out.println(following.get(1).getId() + "--------> " + path2.get(0).getId());
		
		
		
		//Path2
		
		find(following.get(0), end);
		
	}


	public List<FlowNode> getFlowingFlowNodes(FlowNode node) {
		
		List<FlowNode> followingFlowNodes = new ArrayList<FlowNode>();
		
		for (SequenceFlow sequenceFlow : node.getOutgoing()) {
		  followingFlowNodes.add(sequenceFlow.getTarget());
		}

		return followingFlowNodes;
	}


	

}
