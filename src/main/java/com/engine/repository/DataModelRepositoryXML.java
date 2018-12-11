package com.engine.repository;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Repository;

import com.engine.mapper.datamodel.DataModel;


@Repository
public class DataModelRepositoryXML {
	
		// JAXBContext is thread safe and can be created once
		private static JAXBContext jaxbContext = null;

		//singleton
		public static JAXBContext getInstance() {
			if (jaxbContext == null) {
				try {
					jaxbContext = JAXBContext.newInstance(DataModel.class);
				} catch (Exception e) {
					throw new IllegalStateException(e);
				}
				return jaxbContext;
			}else
				return jaxbContext;
		}
	
	/*
	 * Get data model representing the domain
	 */
	public DataModel loadDataModel(String xmlStringPath) {
		DataModel dataModel = null;
		try {

			System.out.println("Apro file " + xmlStringPath + "...");
			File file = new File(xmlStringPath);
			Unmarshaller unmarshaller = getInstance().createUnmarshaller();
			dataModel = (DataModel) unmarshaller.unmarshal(file);
			System.out.println("Data Model caricato!");

		} catch (JAXBException e) {
			System.err.println("La procedura si è interrotta in fase di unmarshalling : ");
			e.printStackTrace();
			return null;
		}

		return dataModel;

	}
}
