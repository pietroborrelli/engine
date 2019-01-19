package com.engine.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.engine.domain.abstractmodel.Collection;
import com.engine.domain.abstractmodel.Entry;
import com.engine.domain.interactionflowelement.InteractionFlowElement;

@Component
public class Output {

	
	/**
	 * @param collection
	 * @param path
	 * @param area
	 * @return void. Print in console NoSQL Abstract Model and the access path
	 */
	public void printAbstractModel(Collection collection, String folderPath) {
		String outputPath = folderPath + collection.getName()+".txt";
		

		System.out.println("Output on: " + outputPath);

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(outputPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter printWriter = new PrintWriter(fileWriter);
		// collection
		printWriter.println(collection.toString());
		// block
		printWriter.println("\n\tBlock " + collection.getBlock().getKey().getPartitionKeys().toString() + " ; "
				+ collection.getBlock().getKey().getSortKeys().toString());
		// entries
		printWriter.println("\n\t\tEntries");
		for (Entry entry : collection.getBlock().getEntries().stream().distinct().collect(Collectors.toList())) {
			printWriter.println("\t\t\t" + entry.toString());
		}

		printWriter.println("\nPath:\n");

		Integer count = 0;
		for (InteractionFlowElement ife : collection.getPath().getInteractionFlowElements()) {
			count++;
			printWriter.println(count + "." + ife.getClass() + "  ->  " + ife.getId());
		}

		printWriter.close();
	}

	/**
	 * @param collection
	 * @param area
	 * @param script
	 * @return void. Print in console Physical Model -> script
	 */
	public void printPhysicalModel(Collection collection, String folderPath, String script) {
		
		String outputPath = folderPath + collection.getName()+".txt";

		System.out.println("Output on: " + outputPath);

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(outputPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter printWriter = new PrintWriter(fileWriter);
		// script
		printWriter.println(script);

		printWriter.close();
	}

}
