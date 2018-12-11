package com.engine.service;

import java.util.List;

import org.w3c.dom.Document;

public interface PageService {

	public List<Document> loadPages(String path, String area);
}
