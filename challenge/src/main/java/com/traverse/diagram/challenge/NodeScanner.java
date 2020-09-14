package com.traverse.diagram.challenge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.model.bpmn.instance.FlowNode;

public class NodeScanner {
	
	ModelParser model = new ModelParser();
	public ArrayList<FlowNode> node_list = new ArrayList<FlowNode>();
	public boolean found;
	ArrayList<FlowNode> following =  new ArrayList<FlowNode>();
	
	public NodeScanner() {}
	
	public NodeScanner(List<FlowNode> double_node, FlowNode end) {
		
		if(double_node.contains(end)) {
			
			System.out.println("Node found: " + double_node.get(1).getId());
		}else {
			
			scan(double_node, end);
		}
	

	}
	
	public ArrayList<FlowNode> scan(List<FlowNode> double_node, FlowNode end) {
		
		System.out.println("Node 0 " + double_node.get(0).getId());
		System.out.println("Node 1 " + double_node.get(1).getId());
		
		List<FlowNode> following0 = model.getFlowingFlowNodes(double_node.get(0));
		List<FlowNode> following1 = model.getFlowingFlowNodes(double_node.get(1));
		

		if(following0.isEmpty()) {
			
			System.out.println("Only " + double_node.get(1).getId());
			following.add(following1.get(0));
			return following;
		}
		
		if(following1.isEmpty()) {
			System.out.println("Only " + double_node.get(0).getId());
			following.add(following0.get(0));
			return following;
		}
		if(!following0.isEmpty() && !following1.isEmpty()) {
			System.out.println("Both " + double_node.get(0).getId() + " and " + 
					double_node.get(0).getId());
			
			following.add(following1.get(0));
			following.add(following0.get(0));
			return following;
		}
		
		return following;

	}


}
