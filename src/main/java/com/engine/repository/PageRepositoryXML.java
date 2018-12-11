package com.engine.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;


@Repository
public class PageRepositoryXML {
	
	
	/*
	 * Get Pages
	 * @return: paths to the pages
	 */
	
	public List<Document> loadPages(String path,String area) {
		List<Document> pages = new ArrayList<Document>();
		
		try {

			System.out.println("Ricerco pagine dell'area " + area + " in :" + path + area + "...");
			File folder = new File(path+area);
			
			if (!folder.exists() || !folder.isDirectory() || folder==null)
				return null;
			
			System.out.println("Trovate " + folder.listFiles().length + " pagine");
			
			if (folder.listFiles().length == 0) {
				System.out.println("Nessuna pagina trovata!");
				return null;
			}else {
				
				File[] files = folder.listFiles();
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        factory.setNamespaceAware(true); // never forget this!
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc ;
		        
				for (File file : files){
					
					System.out.println("Page "+ file.getName() +" caricata!");
					doc = builder.parse(path+area+"/"+file.getName());
					pages.add(doc);
		        }
				return pages;
			}
			
		} catch (Exception e) {
			System.err.println("Problema sull'estrazione delle pagine : ");
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Get pages of the area
	
	public List<Document> loadPages(String xmlStringPath) {
		Grid page = null;
		List<Grid> pages = new ArrayList<Grid>();

		try {

			System.out.println("Percorso " + xmlStringPath + "...");
			File folder = new File(xmlStringPath);
			
			if (!folder.exists() || !folder.isDirectory() || folder==null)
				return null;
			
			File[] files = folder.listFiles();
			for (File file : files){
				
				System.out.println("Accedo al file " + xmlStringPath + file.getName());
				Unmarshaller unmarshaller = JAXBContext.newInstance(Grid.class).createUnmarshaller();
				page = (Grid) unmarshaller.unmarshal(file);
				System.out.println("Page "+ page +" caricato!");
				pages.add(page);
	        }

		} catch (JAXBException e) {
			System.err.println("La procedura si è interrotta in fase di unmarshalling : ");
			e.printStackTrace();
			return null;
		}
		
		return pages;

	} */
}
