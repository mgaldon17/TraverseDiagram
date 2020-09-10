package com.traverse.diagram.challenge;

import java.io.IOException;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	
    	//String json = GetRequest.sendGET();
    	
    	//String xml = GetXml.getXml();
    	
    	//System.out.println(xml);
    	ModelParser model = new ModelParser();
    	model.parse();
    	

    }
}
