package com.engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
import com.engine.parser.Parser;
import com.engine.service.AreaService;
import com.engine.service.DataModelService;
import com.engine.service.NoAmService;
import com.engine.service.PageService;
import com.engine.util.Output;

@Configuration
@SpringBootApplication
public class EngineApplication implements CommandLineRunner {

	private DataModelService dataModelService;
	private AreaService areaService;
	private PageService pageService;
	private NoAmService noAmService;
	private Parser parser;
	private Output output;

	// input path
	@Value("${input.path.datamodel}")
	private String inputPathDataModel;
	@Value("${input.path.areas}")
	private String inputPathAreas;
	@Value("${input.path.abstractmodel}")
	private String inputPathAbstractModel;
	
	// output path
	@Value("${output.path.abstractmodel}")
	private String outputPathAbstractModel;
	@Value("${output.path.physicalmodel}")
	private String outputPathPhysicalModel;
	
	
	@Autowired
	public EngineApplication(DataModelService dataModelService, AreaService areaService, PageService pageService,
			NoAmService noAmService, Parser parser, Output output) {
		this.dataModelService = dataModelService;
		this.areaService = areaService;
		this.pageService = pageService;
		this.noAmService = noAmService;
		this.parser = parser;
		this.output = output;

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
				// set document on which frontEndInspector will work
				frontEndInspector.setDocument(document);
				Page page = frontEndInspector.elaborateDocument();

				// List<ViewComponent> leavesViewComponents =
				// frontEndInspector.findLeavesViewComponents();
				List<InteractionFlowElement> viewComponents = frontEndInspector.findViewComponents();
				List<Path> paths = frontEndInspector.extractPaths(viewComponents);
				List<Collection> collections = noAmService.computeAbstractModelsByPaths(paths);
				collections = noAmService.optimizeReadingAccessPaths(collections);

				generateModels(collections,area.getName());

				pages.add(page);

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

	private void initializeFileSystem() {
		// prepare and clean directory
		try {
			FileUtils.cleanDirectory(new File(inputPathAbstractModel));
			FileUtils.cleanDirectory(new File(outputPathAbstractModel));
			FileUtils.cleanDirectory(new File(outputPathPhysicalModel));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateModels(List<Collection> collections, String area) {

		//create output folder according with the area
		new File(outputPathPhysicalModel+area+"/").mkdirs();
		new File(outputPathAbstractModel+area+"/").mkdirs();
		
		for (Collection collection : collections) {

			// print noAM
			output.printAbstractModel(collection,area);
			// print Physical Implementation
			output.printPhysicalModel(collection, area,  parser.buildPhysicalModel(collection));

		}
	}

}
