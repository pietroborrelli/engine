package com.engine.repository;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.springframework.stereotype.Repository;


@Repository
public class AreaRepositoryXML {
	
	private String[] blackListDirectories = { "CVS" };

	
	/*
	 * Get Areas
	 * @return: paths to the areas
	 */
	
	public List<String> loadAreas(String path) {
		
		try {

			System.out.println("Ricerco aree in " + path + "...");
			File folder = new File(path);
			
			if (!folder.exists() || !folder.isDirectory() || folder==null)
				return null;
			
			//get only subdirectories
			String[] subDirectories = folder.list(new FilenameFilter() {
			  @Override
			  public boolean accept(File current, String name) {
			    return new File(current, name).isDirectory();
			  }
			});
			
			List<String> areas = new ArrayList<String>(Arrays.asList(subDirectories));
			
			// remove if directory belongs to black list
			for (String directory : subDirectories ) {
			
			if (Arrays.stream(blackListDirectories)
					.anyMatch(directory::equals))
				areas.remove(directory);
			}
			
			System.out.println("Trovate " + areas.size() + " aree");
			
			if (areas.size() == 0) {
				System.out.println("Nessuna area trovata!");
				return null;
			}else {
				return areas;
			}
			
		} catch (Exception e) {
			System.err.println("Problema sull'estrazione delle aree : ");
			e.printStackTrace();
			return null;
		}
	}
	
}
