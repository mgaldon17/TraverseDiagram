package com.traverse.diagram.challenge;

import java.io.IOException;

public class Main {
	
	public static void main( String[] args ) throws IOException
    {
    	ModelParser model = new ModelParser(); 
    	model.parse(args[0], args[1]);
    	
    }

}
