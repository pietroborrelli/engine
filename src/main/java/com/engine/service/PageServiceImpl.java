package com.engine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.engine.repository.PageRepositoryXML;


@Service
public class PageServiceImpl implements PageService {

	private PageRepositoryXML pageRepositoryXML;
	
	@Autowired
	public PageServiceImpl(PageRepositoryXML pageRepositoryXML) {
		this.pageRepositoryXML=pageRepositoryXML;
	}

	@Override
	public List<Document> loadPages(String path,String area) {
		return pageRepositoryXML.loadPages(path,area);
	}

}
