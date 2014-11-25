package eu.europeana.creative.dataset.design;

import org.junit.Before;

import eu.europeana.creative.dataset.indexing.LocalImageIndexingTest;

public class DesignIndexingDatasetTest extends LocalImageIndexingTest{

	@Before
	public void init(){
		String dataset = "design";
		setDataset(dataset);
	}
}
