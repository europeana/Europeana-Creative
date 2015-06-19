package eu.europeana.creative.dataset.smk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.api.client.dataset.DatasetDescriptor;
import eu.europeana.api.client.thumbnails.ThumbnailAccessorUtils;
import eu.europeana.creative.dataset.IRTestConfigurations;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;

public class SmkThumbnailMapsTest extends ThumbnailAccessorUtils implements IRTestConfigurations{

	//private boolean overwriteThumbnails = false;
	String selectionInputCultureCam = "/selection/input/culturecam_v1.csv";
	String selectionInputSmk = "/selection/input/smk_content.csv";
	String selectionInputNew = "/selection/input/new.csv";
	String selectionInputOnb = "/selection/input/onb.csv";
	String selectionRemoveBadCC = "/selection/remove/cc_bad_images.csv";
	String selectionRemoveBadSMK = "/selection/remove/smk_bad_images.csv";
	String selectionAggregationSmk = "/selection/aggregation/smk.csv";
	String selectionAggregationCultureCam = "/selection/aggregation/culturecam.csv";
	//String thumbnailMapFolder = "/selection/thumbnailmap";
//	private String processingStep = null;
//	private boolean overwriteThumbnails = false;
		
	@Before
	public void init(){
		String dataset = "smk";
		setDataset(dataset);
	}
	
	
	protected File getCollectionCsvFile(DatasetDescriptor dataset,
			final String processingStep) {
		if(processingStep != null){
			String fileName = getCollectionsCvsFolder() + processingStep.toLowerCase() + "/" + dataset.getImageSetName()
				+ "_" + encode(dataset.getCollectionName()) + ".csv";
			return new File(fileName);
		}else{
			return super.getCollectionCsvFile(dataset);
		}
	}
	
	
	@Test
	public void verifyThumbnails() throws FileNotFoundException, IOException {
		// File datasetFile = getConfig().getDatasetFile(getDataset());
		//File downloadFolder = getConfig().getImageFolderAsFile(getDataset());
		File datasetFile = new File(getCollectionsCvsFolder() + selectionAggregationSmk);
//		File datasetFile = new File(getCollectionsCvsFolder() + selectionAggregationCultureCam);
		
		Map<String, String> newTagMap = readThumbnailsMap(datasetFile);
		String id;
		File thumbnailFile;
		for (Map.Entry<String, String> tag : newTagMap.entrySet()) {
			id = tag.getKey();
			thumbnailFile = getConfig().getImageFile("culturecam", id);
			if(!thumbnailFile.exists())
				System.out.println("Image not found for id: " + id);
			if(thumbnailFile.length() == 3583)
				System.out.println("Placeholder image found for :" + id);
		}
	}
	
	@Test
	public void performSmkDatasetAggregation() throws IOException {
		
		log.trace("Aggregating dataset: " + getDataset());
		
		File cultureCamFile = new File(getCollectionsCvsFolder() + selectionInputCultureCam);
		File newFile = new File(getCollectionsCvsFolder() + selectionInputNew);
		File smkContentFile = new File(getCollectionsCvsFolder() + selectionInputSmk);
		File toRemoveFileSmk = new File(getCollectionsCvsFolder() + selectionRemoveBadSMK);
		//File toRemoveFileCC = new File(getCollectionsCvsFolder() + selectionRemoveBadCC);
		File aggregatedFileSmk = new File(getCollectionsCvsFolder() + selectionAggregationSmk);
		//File aggregatedFileCC = new File(getCollectionsCvsFolder() + selectionAggregationCultureCam);
		
		Map<String, String> datasetMap = new TreeMap<String, String>();
		Map<String, String> tmpMap;
		
		tmpMap = readThumbnailsMap(cultureCamFile);//this should eliminate existing duplications
		datasetMap.putAll(tmpMap);
		log.debug("CultureCam (v1): " + tmpMap.size());
		log.debug("Smk_cc: " + datasetMap.size());
		
		tmpMap = readThumbnailsMap(newFile);//this should eliminate existing duplications
		datasetMap.putAll(tmpMap);
		log.debug("New tag: " + tmpMap.size());
		log.debug("Smk_cc_new: " + datasetMap.size());
		
		tmpMap = readThumbnailsMap(smkContentFile);//this should eliminate existing duplications
		datasetMap.putAll(tmpMap);
		log.debug("Smk Content tag: " + tmpMap.size());
		log.debug("Smk_cc_new_smk: " + datasetMap.size());
		
		Map<String, String> removeMap = readThumbnailsMap(toRemoveFileSmk);
		log.debug("To remove: " + removeMap.size());
		for (String key : removeMap.keySet()) {
			datasetMap.remove(key);
		}
		log.debug("Smk_final: " + datasetMap.size());
		log.debug("Smk_final written to: " + aggregatedFileSmk.getAbsolutePath());
		
		
		DatasetDescriptor dataset = new DatasetDescriptor("II", "SMK");
		writeThumbnailsToCsvFile(dataset, datasetMap , aggregatedFileSmk);
		
	}
	
	@Test
	public void performCCDatasetAggregation() throws IOException {
		
		log.trace("Aggregating dataset: " + getDataset());
		
		File cultureCamFile = new File(getCollectionsCvsFolder() + selectionInputCultureCam);
		File newFile = new File(getCollectionsCvsFolder() + selectionInputNew);
		File onbFile = new File(getCollectionsCvsFolder() + selectionInputOnb);
		//File smkContentFile = new File(getCollectionsCvsFolder() + selectionInputSmk);
		//File toRemoveFileSmk = new File(getCollectionsCvsFolder() + selectionRemoveBadSMK);
		File toRemoveFileCC = new File(getCollectionsCvsFolder() + selectionRemoveBadCC);
		//File aggregatedFileSmk = new File(getCollectionsCvsFolder() + selectionAggregationSmk);
		File aggregatedFileCC = new File(getCollectionsCvsFolder() + selectionAggregationCultureCam);
		
		Map<String, String> datasetMap = new TreeMap<String, String>();
		Map<String, String> tmpMap;
		
		tmpMap = readThumbnailsMap(cultureCamFile);//this should eliminate existing duplications
		datasetMap.putAll(tmpMap);
		log.debug("CultureCam (v1): " + tmpMap.size());
		log.debug("CultureCam_v1: " + datasetMap.size());
		
		tmpMap = readThumbnailsMap(newFile);//this should eliminate existing duplications
		datasetMap.putAll(tmpMap);
		log.debug("New tag: " + tmpMap.size());
		log.debug("CultureCam_v1_new: " + datasetMap.size());
		
		tmpMap = readThumbnailsMap(onbFile);//this should eliminate existing duplications
		datasetMap.putAll(tmpMap);
		log.debug("Onb selection: " + tmpMap.size());
		log.debug("CultureCam_v1_new_onb: " + datasetMap.size());
		
//		tmpMap = readThumbnailsMap(smkContentFile);//this should eliminate existing duplications
//		datasetMap.putAll(tmpMap);
//		log.debug("Smk Content tag: " + tmpMap.size());
//		log.debug("Smk_cc_new_smk: " + datasetMap.size());
		
		Map<String, String> removeMap = readThumbnailsMap(toRemoveFileCC);
		log.debug("To remove: " + removeMap.size());
		for (String key : removeMap.keySet()) {
			datasetMap.remove(key);
		}
		log.debug("CultureCam_final: " + datasetMap.size());
		log.debug("CultureCam_final written to: " + aggregatedFileCC.getAbsolutePath());
		
		
		DatasetDescriptor dataset = new DatasetDescriptor("V2", "CultureCam");
		writeThumbnailsToCsvFile(dataset, datasetMap , aggregatedFileCC);
		
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
	
	
	protected String getCollectionsCvsFolder() {
		return getCollectionsCvsFolder(getDataset());
	}
	
	public String getCollectionsCvsFolder(String dataset) {
		return IRTestConfigurations.COLLECTIONS_FOLDER + dataset + "/";
	}

//	public String getProcessingStep() {
//		return processingStep;
//	}
//
//	public void setProcessingStep(String processingStep) {
//		this.processingStep = processingStep;
//	}
	
	
	@Test
	public void copyThumbnails() throws FileNotFoundException, IOException{
		File datasetFile = getDataSetFile(false);
		Map<String, String> thumbnailMap = readThumbnailsMap(datasetFile);
		File imageFile;
		File destFile;
		for (String id : thumbnailMap.keySet()) {
			System.out.println("copying image with id: " + id);
			imageFile = getConfig().getImageFile(getDataset(), id);
			if(!imageFile.exists())
				System.out.println("Error: file not found + " + imageFile.getCanonicalPath());
			else{
				destFile = new File(imageFile.getPath().replaceFirst("app", "inst"));
				copyFile(imageFile, destFile);
			}
				
		}
		
	}
	
	
}
