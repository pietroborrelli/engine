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
	@Value("${output.path.abstractmodel.optimization}")
	private String outputPathAbstractModelOptimization;
	@Value("${output.path.physicalmodel.optimization}")
	private String outputPathPhysicalModelOptimization;

	/**
	 * @param collection
	 * @param path
	 * @param area
	 * @return void. Print in console NoSQL Abstract Model and the access path
	 */
	public void printAbstractModel(Collection collection, String area, boolean optimization) {

		String outputPath = "";
		if (optimization)
			outputPath = outputPathAbstractModelOptimization + area + "/" + collection.getPath().getIdPath() + "/"
			+ collection.getId() + "-" + collection.getName() + ".txt";
		else
			outputPath = outputPathAbstractModel + area + "/" + collection.getPath().getIdPath() + "/"
					+ collection.getId() + "-" + collection.getName() + ".txt";
			
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
		for (Entry entry : collection.getBlock().getEntries()) {
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
	 * @return void. Print in consolle Physical Model -> script
	 */
	public void printPhysicalModel(Collection collection, String area, String script, boolean optimization) {
		String outputPath = "";
		if (optimization)
			outputPath = outputPathPhysicalModelOptimization + area + "/" + collection.getPath().getIdPath() + "/"
					+ collection.getId() + "-" + collection.getName() + ".txt";
		else
			outputPath = outputPathPhysicalModel + area + "/" + collection.getPath().getIdPath() + "/"
					+ collection.getId() + "-" + collection.getName() + ".txt";

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
