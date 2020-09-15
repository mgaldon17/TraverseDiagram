package com.traverse.diagram.challenge;

import java.io.IOException;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	
        System.out.println("Argument one = "+ args[0]);
        System.out.println("Argument two = "+ args[1]);

    	ModelParser model = new ModelParser(); 
    	model.parse(args[0], args[1]);
    	
    	
    	
    	
    	
    	

    }
}
