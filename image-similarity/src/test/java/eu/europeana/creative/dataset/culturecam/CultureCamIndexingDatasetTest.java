package eu.europeana.creative.dataset.culturecam;

import org.junit.Before;

import eu.europeana.creative.dataset.design.DesignIndexingDatasetTest;
import eu.europeana.creative.dataset.indexing.LocalImageIndexingTest;

public class CultureCamIndexingDatasetTest extends LocalImageIndexingTest{

	@Before
	public void init(){
		String dataset = "culturecam";
		setDataset(dataset);
	}
}
