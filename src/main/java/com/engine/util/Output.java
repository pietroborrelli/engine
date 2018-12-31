package com.engine.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.engine.domain.abstractmodel.Collection;
import com.engine.domain.abstractmodel.Entry;


@Component
public class Output {

	@Value("${output.path.abstractmodel}")
	private String outputPathAbstractModel;
	@Value("${output.path.physicalmodel}")
	private String outputPathPhysicalModel;
	
	public void print(Collection collection, String area ) {
		
		System.out.println("Output on: " + outputPathAbstractModel + area + "/" + collection.getId() +"-"+ collection.getName()+".txt");
		
	    FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(outputPathAbstractModel + area + "/" + collection.getId() + "-"+ collection.getName()+".txt");
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

	    
	    printWriter.close();
	}
	
public void printScript(Collection collection,String area, String script) {
		
		System.out.println("Output on: " + outputPathPhysicalModel + area + "/" + collection.getId() +"-"+ collection.getName()+".txt");
		
	    FileWriter fileWriter=null;
		try {
			fileWriter = new FileWriter(outputPathPhysicalModel + area + "/" + collection.getId() + "-"+ collection.getName()+".txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    PrintWriter printWriter = new PrintWriter(fileWriter);
	    //script
	    printWriter.println(script);
	    
	    printWriter.close();
	}

}
