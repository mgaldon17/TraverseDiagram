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
	boolean finish; 
	public List<FlowNode> list;
	public final Collection <FlowNode> path = null ; 
	public ArrayList<FlowNode> saved_nodes = new ArrayList<FlowNode>();
	public ArrayList<FlowNode> start_end = new ArrayList<FlowNode>();
	public ArrayList<FlowNode> double_node = new ArrayList<FlowNode>();
	public ArrayList<FlowNode> blacklist = new ArrayList<FlowNode>();
	
	boolean singleNode = false;
	
	public BpmnModelInstance parse() throws IOException {
		
		String xml = GetXml.getXml();
		InputStream targetStream = new ByteArrayInputStream(xml.getBytes());

		BpmnModelInstance modelInstance = Bpmn.readModelFromStream(targetStream);
		
		final FlowNode start_node = (FlowNode) modelInstance.getModelElementById("reviewInvoice");
		final FlowNode end_node = (FlowNode) modelInstance.getModelElementById("prepareBankTransfer");
		
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
	
	
	
	public void checkIfEnd(FlowNode end, FlowNode node) throws IOException {
		
		if(node == end) {
			
			finish = true;
			
			System.out.println("\nEnd node reached: " + end.getId() + "." + "\n\n----End of path----\n");
			
			String s0 = ("The path from " + start_end.get(0).getId() + " to " + start_end.get(1).getId() + " is: ");
		

			for(int i = 0; i<saved_nodes.size(); i++) {
				
				s0 += saved_nodes.get(i).getId() + ", ";

			}
			
			System.out.println(s0 + ".");
			System.exit(-1);

		}else{

			find(node, end);
		}
		
	}
	
	public void find(FlowNode start, FlowNode end) throws IOException {
		
		try {
			if(!saved_nodes.contains(start)) {
				saved_nodes.add(start);
			}
			List<FlowNode> list_nodes = getFlowingFlowNodes(start); //Get all nodes
			
			if (list_nodes.isEmpty()) {
				System.out.println("Path not found");
				System.exit(-1);
			}
			
			//Save next nodes in the list
			if(list_nodes.size() == 1) {
				
				System.out.println("Only 1 next node found: " + list_nodes.get(0).getId());
				
				save(list_nodes.get(0), true);
				
				checkIfEnd(end, list_nodes.get(0));
				
				
			}else{
				
				NodeScanner scanner = new NodeScanner(list_nodes, end);
				ArrayList<FlowNode> nodes = scanner.following;
				
				for(int i = 0; i<nodes.size(); i++) {
					
					System.out.println(nodes.get(i).getId());

				}
				
				if(nodes.size()==1) {
					find(nodes.get(0), end);
				}
				
				
			}

		}catch(NullPointerException npe) {
			System.out.println("\nThe id the of the element does not exist");
			System.exit(-1);
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
