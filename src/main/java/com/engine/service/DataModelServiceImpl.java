package com.engine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engine.mapper.datamodel.DataModel;

import com.engine.repository.DataModelRepositoryXML;

@Service
public class DataModelServiceImpl implements DataModelService {

	
	private DataModelRepositoryXML dataModelRepositoryXML;
	
	@Autowired
	public DataModelServiceImpl(DataModelRepositoryXML dataModelRepositoryXML) {
		this.dataModelRepositoryXML=dataModelRepositoryXML;
	}
	
	@Override
	public DataModel loadDataModelFromFile(String path) {
		
		return dataModelRepositoryXML.loadDataModel(path);
	}

}
