package eu.europeana.creative.dataset.evaluation;

import it.cnr.isti.indexer.IndexHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import eu.europeana.creative.dataset.IRTestConfigurations;
import eu.europeana.creative.dataset.evaluation.om.CategorizedCollection;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;

public class BaseResultComputation implements IRTestConfigurations{

	public static final String DEMO_DATASET = "demo";
	IRConfiguration config;
	protected IndexHelper helper = new IndexHelper();

	public BaseResultComputation() {
		super();
	}

	public IRConfiguration getConfig() {
		if (config == null)
			config = new IRConfigurationImpl();
		return config;
	}

	protected CategorizedCollection readCategorizedCollection(File file) throws IOException {
		
		BufferedReader reader = null;
		CategorizedCollection collection = new CategorizedCollection();
		try {
			reader = new BufferedReader(new FileReader(file));
			// BufferedReader reader = new BufferedReader(new
			// FileReader("/collection_07501_thumbnails.csv"));
	
			String header = reader.readLine();
			String[] parts = header.split(";");
			//collection name remove the #
			collection.setCollectionName(parts[0].substring(1).trim());
			collection.setObjectsCount(Integer.parseInt(parts[1].trim()));
			collection.setContentClass(parts[2].trim());
			collection.setContentSubClass(parts[3].trim());
			
		} finally {
			try{
				reader.close();
			}catch(Exception e){
				System.out.println("exception when closing reader: ");
				e.printStackTrace();
			}
		}
		
		return collection;
	
	}

	@Override
	public String getCollectionsCvsFolder(String dataset) {
		return COLLECTIONS_FOLDER + dataset + "/europeana/";
	}

	public String getCollectionsEvaluationFolder(String dataset) {
		return COLLECTIONS_FOLDER + dataset + "/evaluation/";
	}

	protected String getExperimentExecutionResultsFile(String dataset) {
		return getCollectionsEvaluationFolder(dataset) + "experiment_execution.csv";
	}

}