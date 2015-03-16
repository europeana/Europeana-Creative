package eu.europeana.creative.dataset.design;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.api.client.MyEuropeanaClient;
import eu.europeana.api.client.dataset.DatasetDescriptor;
import eu.europeana.api.client.myeuropeana.exception.MyEuropeanaApiException;
import eu.europeana.api.client.myeuropeana.impl.MyEuropeanaClientImpl;
import eu.europeana.api.client.myeuropeana.response.TagsApiResponse;
import eu.europeana.api.client.myeuropeana.thumbnails.ThumbnailFromTagsResponseAccessor;
import eu.europeana.api.client.thumbnails.ThumbnailAccessorUtils;
import eu.europeana.api.client.thumbnails.download.ThumbnailDownloader;
import eu.europeana.api.client.thumbnails.processing.LargeThumbnailsetProcessing;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;

public class DesignBuildDatasetTest extends ThumbnailAccessorUtils{

	private boolean overwriteThumbnails = false;
	
	@Before
	public void init(){
		String dataset = "design";
		setDataset(dataset);
	}
	
	@Test
	public void buildDataset() throws MyEuropeanaApiException, IOException{
		String jsonFile = "/myeuropeana/design/mydata_tag_v2.json";
		TagsApiResponse apiResponse = readJsonFile(jsonFile);
		
		ThumbnailFromTagsResponseAccessor ta = new ThumbnailFromTagsResponseAccessor();
		File datasetFile = getDataSetFile(false);
		if(datasetFile.exists())
			datasetFile.delete();
		
		Map<String, Map<String, String>> thumbnailsByTag = ta.getThumbnailsFromTagsApiResponse(apiResponse);
		DatasetDescriptor descriptor;
		
		int objectCount = 0; 
		for (Map.Entry<String, Map<String, String>> entry : thumbnailsByTag.entrySet()) {
			descriptor = new DatasetDescriptor(getDataset(), entry.getKey());
			descriptor.setClassifications(new String[]{entry.getKey()});
			writeThumbnailsToCsvFile(descriptor, entry.getValue(), datasetFile, POLICY_APPEND_TO_FILE);
			objectCount += entry.getValue().size();
			System.out.println("TAG: " + entry.getKey() + " - " + entry.getValue().size());
		}	
		
		assertEquals(apiResponse.getTotalResults(), objectCount);
	}
	
	@Test
	public void downloadThumbnails() throws FileNotFoundException, IOException {

		File datasetFile = getConfig().getDatasetFile(getDataset());
		File downloadFolder = getConfig().getImageFolderAsFile(getDataset());

		LargeThumbnailsetProcessing datasetDownloader = new LargeThumbnailsetProcessing(datasetFile);
		ThumbnailDownloader observer = new ThumbnailDownloader(downloadFolder);
		observer.setSkipExistingFiles(!overwriteThumbnails);
		datasetDownloader.addObserver(observer);
		datasetDownloader.processThumbnailset(0, -1, 1000);
		
		System.out.println("Skipped items: " + datasetDownloader.getSkippedItemsCount());
		System.out.println("Failed downloads: " + datasetDownloader.getFailureCount());
		System.out.println("Downloaded files: " + datasetDownloader.getItemsProcessed());
		
		assertEquals(0, datasetDownloader.getFailureCount());
//		for (String itemId : skippedItems) {
//			System.out.println(itemId);
//		}
	}

	private TagsApiResponse readJsonFile(String testResource) throws IOException, MyEuropeanaApiException {
		TagsApiResponse res = null;
		InputStream resourceAsStream = null;
		try {
			resourceAsStream = getClass().getResourceAsStream(
					testResource);
			MyEuropeanaClient client = new MyEuropeanaClientImpl();
			res = client.parseTagsApiResponse(resourceAsStream);
			
		} finally {
			if(resourceAsStream!= null)
				resourceAsStream.close();
		}
		
		return res;

	}
	
	protected IRConfiguration getConfig() {
		IRConfiguration config = new IRConfigurationImpl();
		return config;
	}
	
	public File getDataSetFile(boolean urls) {
		IRConfiguration config = getConfig();
		if (urls)
			return config.getDatasetUrlsFile(getDataset());
		else
			return config.getDatasetFile(getDataset());
	}

	public boolean isOverwriteThumbnails() {
		return overwriteThumbnails;
	}

	public void setOverwriteThumbnails(boolean overwriteThumbnails) {
		this.overwriteThumbnails = overwriteThumbnails;
	}
	
}
