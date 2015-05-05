package eu.europeana.creative.dataset.culturecam.v2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.creative.dataset.indexing.LocalImageIndexingTest;

public class TagSubsetIndexingTest extends LocalImageIndexingTest{

	//String subset = null;
	
	@Before
	public void init(){
		setDataset("culturecam");
		//setSubset("tags/new");
	}

//	protected String getSubset() {
//		return subset;
//	}

//	protected void setSubset(String subset) {
//		this.subset = subset;
//	}
	
	public String getSubsetCvsFile(){
		//return getConfig().getDatasetsFolderAsFile().getParent() + "/subsets/" + getDataset() + "/csvs/" + getSubset() + ".csv";
		String tagSelectionFilename = "/selection/input/tags/new.csv";
		File tagSelectionFile = new File(getCollectionsCvsFolder()
				+ tagSelectionFilename);
		return tagSelectionFile.getAbsolutePath();
	}
	
	@Test
	public void indexThumbnails() throws FileNotFoundException, IOException {

		ensureParmsInit();
		
		File datasetFile = getDatasetFileToIndex();
		if(datasetFile == null || !datasetFile.exists())
			System.out.println("Skip test missing file: " + datasetFile);
		else
			updateIndex(datasetFile, false);
		
	}
	
	@Override
	protected File getDatasetFileToIndex() {
		return new File(getSubsetCvsFile());
	}
}
