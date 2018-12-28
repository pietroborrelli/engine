package com.engine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Document;

import com.engine.domain.abstractmodel.Collection;
import com.engine.domain.interactionflowelement.InteractionFlowElement;
import com.engine.domain.interactionflowelement.viewelement.viewcontainer.Area;
import com.engine.domain.interactionflowelement.viewelement.viewcontainer.Page;
import com.engine.domain.wrapper.Path;
import com.engine.inspector.FrontEndInspector;
import com.engine.mapper.datamodel.DataModel;
import com.engine.service.AreaService;
import com.engine.service.DataModelService;
import com.engine.service.NoAmService;
import com.engine.service.PageService;

@Configuration
@SpringBootApplication
public class EngineApplication implements CommandLineRunner {

	private DataModelService dataModelService;
	private AreaService areaService;
	private PageService pageService;
	private NoAmService noAmService;

	@Value("${input.path.datamodel}")
	private String inputPathDataModel;
	@Value("${input.path.areas}")
	private String inputPathAreas;
	@Value("${input.path.abstractmodel}")
	private String inputPathAbstractModel;

	@Autowired
	public EngineApplication(DataModelService dataModelService, AreaService areaService, PageService pageService, NoAmService noAmService) {
		this.dataModelService = dataModelService;
		this.areaService = areaService;
		this.pageService = pageService;
		this.noAmService = noAmService;
	}

	public static void main(String[] args) {
		SpringApplication.run(EngineApplication.class, args);
	}

	public void run(String... args) throws Exception {

		initializeFileSystem();

		FrontEndInspector frontEndInspector = initializeFrontEndInspector();

		navigateFrontEnd(frontEndInspector);

	}

	private void navigateFrontEnd(FrontEndInspector frontEndInspector) throws Exception {

		List<Area> areas = new ArrayList<Area>();
		List<Page> pages = new ArrayList<Page>();

		// get areas
		ArrayList<String> areaNames = (ArrayList<String>) areaService.loadAreas(inputPathAreas);
		if (areaNames == null)
			throw new NullPointerException("Aree non presenti!");
		else
			// set areas
			areaNames.stream().forEach(n -> areas.add(new Area(n)));

		// get pages
		for (Area area : areas) {
			ArrayList<Document> documents = (ArrayList<Document>) pageService.loadPages(inputPathAreas, area.getName());
			if (documents == null)
				break;
			
			pages.clear();
			for (Document document : documents) {
				//set document on which frontEndInspector will work
				frontEndInspector.setDocument(document);
				
				Page page = frontEndInspector.elaborateDocument();
				
				//List<ViewComponent> leavesViewComponents = frontEndInspector.findLeavesViewComponents();
				List<InteractionFlowElement> viewComponents = frontEndInspector.findViewComponents();
				List<Path> paths = frontEndInspector.extractPaths(viewComponents);
				List<Collection> collections = noAmService.computeAbstractModelsByPaths(paths);
				
				pages.add(page);
				// findPatterns(page);
				/*
				XPathExpression expr = xpath.compile("//PowerIndexUnit");
				Object result = expr.evaluate(page, XPathConstants.NODESET);
				NodeList nodes = (NodeList) result;

				for (int nodeCount = 0; nodeCount < nodes.getLength(); nodeCount++) {
					Node node = nodes.item(nodeCount);
					for (int attributeCount = 0; attributeCount < node.getAttributes().getLength(); attributeCount++) {
						Attr attribute = (Attr) node.getAttributes().item(attributeCount);
						System.out.println(attribute.getValue()); //
						System.out.println(nodes.item(i).getAttributes().item(1).get);
					}
				}
*/
			}
			area.setPages(pages);
		}

	}

	private FrontEndInspector initializeFrontEndInspector() {
		// get data model
		DataModel dataModel = dataModelService.loadDataModelFromFile(inputPathDataModel);
		if (dataModel == null)
			throw new NullPointerException("Data Model vuoto!");

		// create front end inspector and set data model
		return new FrontEndInspector(dataModel);

	}

	private void initializeFileSystem() throws IOException {
		// prepare and clean directory
		FileUtils.cleanDirectory(new File(inputPathAbstractModel));
	}

}
