package com.engine.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.engine.domain.abstractmodel.Collection;
import com.engine.domain.abstractmodel.Entry;
import com.engine.domain.interactionflowelement.InteractionFlowElement;


@Component
public class Output {

	@Value("${output.path.abstractmodel}")
	private String outputPathAbstractModel;
	@Value("${output.path.physicalmodel}")
	private String outputPathPhysicalModel;
	
	/**
	 * @param collection
	 * @param path
	 * @param area
	 * @return void. Print in console NoSQL Abstract Model and the access path
	 */
	public void printAbstractModel(Collection collection, String area ) {
		
		System.out.println("Output on: " + outputPathAbstractModel + area + "/" +collection.getPath().getIdPath() + "/" + collection.getId() +"-"+ collection.getName()+".txt");
		
	    FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(outputPathAbstractModel + area + "/"  + collection.getPath().getIdPath() + "/" + collection.getId() + "-"+ collection.getName()+".txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    PrintWriter printWriter = new PrintWriter(fileWriter);
	    //collection
	    printWriter.println(collection.toString());
	    //block
	    printWriter.println("\n\tBlock " +
	    	collection.getBlock().getKey().getPartitionKeys().toString() + " ; " + collection.getBlock().getKey().getSortKeys().toString());
	    //entries
	    printWriter.println("\n\t\tEntries");
	    for (Entry entry : collection.getBlock().getEntries()) {
	    	printWriter.println("\t\t\t" + entry.toString()); 
	    }

	    printWriter.println("\nPath:\n");
	    	
	    Integer count = 0;
	    for (InteractionFlowElement ife : collection.getPath().getInteractionFlowElements()) {
	    	count++;
	    	printWriter.println(count + "." + ife.getClass() + "  ->  " + ife.getId() );
	    }
	    
	    printWriter.close();
	}
	
/**
 * @param collection
 * @param area
 * @param script
 * @return void. Print in consolle Physical Model -> script
 */
public void printPhysicalModel(Collection collection,String area, String script) {
		
		System.out.println("Output on: " + outputPathPhysicalModel + area + "/" + collection.getPath().getIdPath() +"/"+ collection.getId() +"-"+ collection.getName()+".txt");
		
	    FileWriter fileWriter=null;
		try {
			fileWriter = new FileWriter(outputPathPhysicalModel + area + "/" + collection.getPath().getIdPath() +"/"+  collection.getId() + "-"+ collection.getName()+".txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    PrintWriter printWriter = new PrintWriter(fileWriter);
	    //script
	    printWriter.println(script);
	    
	    printWriter.close();
	}

}
