package eu.europeana.creative.dataset.pd;

import java.io.File;

import org.junit.Before;

import eu.europeana.creative.dataset.design.DesignIndexingDatasetTest;
import eu.europeana.creative.dataset.indexing.LocalImageIndexingTest;
import eu.europeana.service.ir.image.IRConfiguration;

public class PdIndexingDatasetTest extends LocalImageIndexingTest{

	@Before
	public void init(){
		String dataset = "pd";
		setDataset(dataset);
	}
	
	protected File getDatasetFileToIndex() {
		IRConfiguration config = getConfig();
		//File datasetFile = config.getDatasetFile(DATASET_EU_CREATIVE_COLOR);
		File datasetFile = config.getDatasetFile("culturecam");
		return datasetFile;
	}
}
