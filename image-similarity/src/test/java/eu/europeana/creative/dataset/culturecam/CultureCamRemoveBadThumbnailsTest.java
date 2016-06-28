package eu.europeana.creative.dataset.culturecam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.creative.dataset.indexing.LocalImageIndexingTest;

public class CultureCamRemoveBadThumbnailsTest extends LocalImageIndexingTest{

	@Before
	public void init(){
		String dataset = "culturecam";
		setDataset(dataset);
	}
	
	@Override
	@Test
	public void indexThumbnails() throws FileNotFoundException, IOException {
		//super.indexThumbnails();
		//disable test
	}
	
	@Override
	@Test
	public void removeThumbnails() throws FileNotFoundException, IOException {
		super.removeThumbnails();
		//disable test
	}
	
	
	@Override
	protected File getDatasetFileToRemove() {
		File outFile = new File(getCollectionsCvsFolder() + "filtering/badThumbnails.csv");
		return outFile;
	}
}
