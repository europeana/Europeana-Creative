package eu.europeana.creative.indexer;

import it.cnr.isti.config.index.ImageDemoConfigurationImpl;
import it.cnr.isti.config.index.IndexConfiguration;
import it.cnr.isti.exception.ImageIndexingException;
import it.cnr.isti.feature.extraction.FeatureExtractionException;
import it.cnr.isti.indexer.TestImageIndexing;

import java.io.IOException;

import org.junit.Test;

//TODO: verify if it makes sense to add dependency on the melampo-demo
public class TestBuildImageDemoIndex extends TestImageIndexing {

	@Override
	public void testImageIndexing() throws ImageIndexingException,
			FeatureExtractionException, IOException {
		// disable indexing of test image
	}

	@Override
	protected IndexConfiguration getIndexConfig() {
		return new ImageDemoConfigurationImpl();
	}

	@Test 
	public void testBuildDemoIndex() throws IOException, ImageIndexingException{
		indexDataset("demo");
	}
	
	@Override
	public void testBuildTestIndex() throws IOException, ImageIndexingException {
		//super.testBuildTestIndex();
	}
}
