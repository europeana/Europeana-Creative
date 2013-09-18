package eu.europeana.creative.indexer;

import it.cnr.isti.feature.extraction.FeatureExtractionException;
import it.cnr.isti.indexer.ImageIndexingException;
import it.cnr.isti.indexer.TestImageIndexing;

import java.io.IOException;

public class TestBuildImageDemoIndex extends TestImageIndexing {

	@Override
	public void testImageIndexing() throws ImageIndexingException,
			FeatureExtractionException, IOException {
		//disable indexing of test image
	}
	
}
