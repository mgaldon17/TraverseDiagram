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
	public ArrayList<FlowNode> saved_nodes = new ArrayList<FlowNode>();
	public ArrayList<FlowNode> start_end = new ArrayList<FlowNode>();
	public final ArrayList<FlowNode> double_node = new ArrayList<FlowNode>();
	public ArrayList<FlowNode> blacklist = new ArrayList<FlowNode>();
	public boolean endReached = false;
	Stack stack = new Stack();
	
	public BpmnModelInstance parse(String start, String end) throws IOException {
		
		String xml = GetXml.getXml();
		InputStream targetStream = new ByteArrayInputStream(xml.getBytes());

		BpmnModelInstance modelInstance = Bpmn.readModelFromStream(targetStream);
		
		final FlowNode start_node = (FlowNode) modelInstance.getModelElementById(start);
		final FlowNode end_node = (FlowNode) modelInstance.getModelElementById(end);
		
		start_end.add(start_node);
		start_end.add(end_node);
		
		try {
			System.out.println("Start node: " + start_node.getId() + "\n");
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
			
			result+=stack.pop();
		}
		
		result+="]";
		
		System.out.println(result);
		
	}
	
	public boolean checkIfEnd(FlowNode end, FlowNode node) throws IOException {
		
		if(node == end) {
			
			finish = true;
			System.out.println("\nEnd node reached: " + end.getId() + "." + "\n\n----End of path----\n");
			
			String s0 = ("The path from " + start_end.get(0).getId() + " to " + start_end.get(1).getId() + " is: ");
		

			for(int i = 0; i<saved_nodes.size(); i++) {
				
				s0 += saved_nodes.get(i).getId() + ", ";

			}
			finish = true;
			System.out.println(s0 + ".");
			System.exit(-1);
			return true;

		}else{
			finish = false;
			return finish;
		}
	}
	
	public void find(FlowNode start, FlowNode end) throws IOException {
		
		try {
			if(!saved_nodes.contains(start)) {
				saved_nodes.add(start);
			}
			
			List<FlowNode> list_nodes = getFlowingFlowNodes(start); //Get all nodes
			
			if (list_nodes.isEmpty()) {
				System.out.println("Reached the end. Path not found");
				endReached = true;
			}
			
			//Save next nodes in the list
			if(list_nodes.size() == 1) {
				
				System.out.println("Only 1 next node found: " + list_nodes.get(0).getId());
				
				save(list_nodes.get(0), true);
				
				while(!checkIfEnd(end, list_nodes.get(0))) {
					
					find(list_nodes.get(0), end);
				}
				
			}else{
				if(list_nodes.size()==1) {
					find(list_nodes.get(0), end);
					saved_nodes.add(list_nodes.get(0));
					
				}else{ 
					///Tomar el nodo 1 y mandarlo a find. Si no encuentra el path, tomar el nodo 2

					if(!finish) {
						
						scanPath(list_nodes);

					}
				}
			}

		}catch(NullPointerException npe) {
			System.out.println("\nThe id the of the element does not exist");
			System.exit(-1);
		} 
	}
	
	public void scanPath(List<FlowNode> list_nodes) throws IOException {
		
		List<FlowNode> path1 = getFlowingFlowNodes(list_nodes.get(0));
		List<FlowNode> path2 = getFlowingFlowNodes(list_nodes.get(1));
		FlowNode end = start_end.get(1);
		
		/*
		System.out.println("List nodes");
		for(int i = 0; i<path1.size(); i++) {
			
			System.out.println(path1.get(i).getId());

		}*/
		//find(path1.get(0), end);
		if(endReached = true) {
			path1.clear();
		}
		
		find(path2.get(0), end);
		if(endReached = true) {
			path2.clear();
		}
	}

	public void save(FlowNode node_to_save, boolean singleNode) {
		
		if(singleNode) {
			saved_nodes.add(node_to_save);
		}else{
			double_node.add(node_to_save);
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
