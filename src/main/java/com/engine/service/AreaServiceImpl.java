package com.engine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engine.repository.AreaRepositoryXML;


@Service
public class AreaServiceImpl implements AreaService {

	private AreaRepositoryXML areaRepositoryXML;
	
	@Autowired
	public AreaServiceImpl(AreaRepositoryXML areaRepositoryXML) {
		this.areaRepositoryXML=areaRepositoryXML;
	}

	@Override
	public List<String> loadAreas(String path) {
		return areaRepositoryXML.loadAreas(path);
	}

}
