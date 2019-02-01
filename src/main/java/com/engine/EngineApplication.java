package com.engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Document;

import com.engine.domain.abstractmodel.Collection;
import com.engine.domain.enumeration.OutputE;
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
	@Value("${input.path.siteview}")
	private String inputPathSiteView;

	// output path
	@Value("${output.path}")
	private String outputPath;

	static String OUTPUT_PM = "physical_model/";
	static String OUTPUT_NOAM = "abstract_model/";

	static String OUTPUT_PM_PATH_OPT = "physical_model/path_optimization/";
	static String OUTPUT_NOAM_PATH_OPT = "abstract_model/path_optimization/";

	static String OUTPUT_PM_PAGE_OPT = "physical_model/page_optimization/";
	static String OUTPUT_NOAM_PAGE_OPT = "abstract_model/page_optimization/";

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

		System.out.println(" START ");

		initializeFileSystem();

		FrontEndInspector frontEndInspector = initializeFrontEndInspector();

		noAmService.setDataModelUtil(frontEndInspector.getDataModelUtil());

		inspectFrontEnd(frontEndInspector);

		System.out.println(" END SUCCESS ");
	}

	public void inspectFrontEnd(FrontEndInspector frontEndInspector) throws Exception {

		List<Area> areas = new ArrayList<Area>();
		List<Page> pages = new ArrayList<Page>();

		// get areas
		ArrayList<String> areaNames = (ArrayList<String>) areaService.loadAreas(inputPathSiteView);
		if (areaNames == null)
			throw new NullPointerException("Aree non presenti!");
		else
			// set areas
			areaNames.stream().forEach(n -> areas.add(new Area(n)));

		// get pages
		for (Area area : areas) {
			ArrayList<Document> documents = (ArrayList<Document>) pageService.loadPages(inputPathSiteView, area.getName());
			if (documents == null)
				continue;

			pages.clear();
			for (Document document : documents) {
				// set document on which frontEndInspector will work
				frontEndInspector.setDocument(document);
				Page page = frontEndInspector.elaborateDocument();

				// found a page with no wr model
				if (page.getId() == null && page.getName() == null)
					continue;

				List<InteractionFlowElement> viewComponents = frontEndInspector.findViewComponents();

				List<Path> paths = frontEndInspector.extractPaths(viewComponents);

				paths.stream().forEach(p -> p.setCollections(noAmService.computeAbstractModels(p)));
				paths.stream().forEach(p -> generateModels(p, p.getCollections(), page, area.getName(), OutputE.NO));
				
				/*
				paths.stream().forEach(p -> p.setCollections(noAmService.pathOptimization(p.getCollections())));
				paths.stream()
						.forEach(p -> generateModels(p, p.getCollections(), page, area.getName(), OutputE.PATH_OPT));

				*/
				for (Path path : paths) {
				if (!path.getCollections().isEmpty()) {
					List<Collection> optimizedPageCollections = new ArrayList<Collection>(
							noAmService.pageOptimization(path.getCollections()));
					if (!optimizedPageCollections.isEmpty())
						paths.stream().forEach(p -> generateModels(p, optimizedPageCollections, page, area.getName(),
								OutputE.PAGE_OPT));
				}
				}
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

			if (new File(outputPath + OUTPUT_NOAM).exists())
				FileUtils.cleanDirectory(new File(outputPath + OUTPUT_NOAM));

			if (new File(outputPath + OUTPUT_PM).exists())
				FileUtils.cleanDirectory(new File(outputPath + OUTPUT_PM));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateModels(Path path, List<Collection> collections, Page page, String area, OutputE optimization) {

		String folderNoAMPath = "";
		String folderPhysicalPath="";
		String pageName = page.getName().substring(page.getName().lastIndexOf("#") + 1);

		// create output folder according with the area

		if (optimization.equals(OutputE.NO)) {
			folderNoAMPath = outputPath + OUTPUT_NOAM + area + "/" + pageName + "/"
					+ path.getCollections().stream().map(n -> n.getName()).collect(Collectors.joining("_")) + "/";
			folderPhysicalPath = outputPath + OUTPUT_PM + area + "/" + pageName + "/"
					+ path.getCollections().stream().map(n -> n.getName()).collect(Collectors.joining("_")) + "/";
			new File(folderNoAMPath).mkdirs();
			new File(folderPhysicalPath).mkdirs();
		}

		if (optimization.equals(OutputE.PATH_OPT)) {
			folderNoAMPath = outputPath + OUTPUT_NOAM_PATH_OPT + area + "/" + pageName + "/"
					+ path.getCollections().stream().map(n -> n.getName()).collect(Collectors.joining("_"));
			folderPhysicalPath = outputPath + OUTPUT_PM_PATH_OPT + area + "/" + pageName + "/"
					+ path.getCollections().stream().map(n -> n.getName()).collect(Collectors.joining("_"));
			new File(folderNoAMPath).mkdirs();
			new File(folderPhysicalPath).mkdirs();
		}

		if (optimization.equals(OutputE.PAGE_OPT)) {
			folderNoAMPath = outputPath + OUTPUT_NOAM_PAGE_OPT + area + "/" + pageName + "/";
			folderPhysicalPath = outputPath + OUTPUT_PM_PAGE_OPT + area + "/" + pageName + "/";
			new File(folderNoAMPath).mkdirs();
			new File(folderPhysicalPath).mkdirs();
		}

		if (!collections.isEmpty()) {
			System.out.println("----------- OUTPUT -------------");

			for (Collection collection : collections) {

				// print noAM
				output.printAbstractModel(collection, folderNoAMPath);
				// print Physical Implementation
				output.printPhysicalModel(collection, folderPhysicalPath, parser.buildPhysicalModel(collection));
				
				//print query
				String queryName = "SELECT_" + collection.getName().toLowerCase().replaceAll(" ", "_");
				output.printSelectQuery(parser.createSelectQuery(collection),queryName, folderPhysicalPath);

			}
		}
	}

}
