package eu.europeana.creative.dataset.culturecam.v2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class CultureCamE280IndexingTest extends CultureCamIndexingDatasetTest{

	/**
	 * invoked by the indexThumbnails test in superclases
	 */
	protected File getDatasetFileToIndex() {
		String collectionSubsetsFolder = getCollectionsCvsFolder(getDataset());
		String e280SubsetFile = collectionSubsetsFolder + "subset_v1/e280_dataset.csv"; 
		return new File(e280SubsetFile);
	}
	
	@Override
	//@Test
	public void removeThumbnails() throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		super.removeThumbnails();
	}
	
	@Override
	protected File getDatasetFileToRemove() {
		//reindexing the same dataset
		return getDatasetFileToIndex();
	}
}
