package eu.europeana.creative.dataset.culturecam;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.api.client.dataset.DatasetDescriptor;
import eu.europeana.api.client.myeuropeana.exception.MyEuropeanaApiException;
import eu.europeana.api.client.thumbnails.ThumbnailAccessorUtils;
import eu.europeana.api.client.thumbnails.download.ThumbnailDownloader;
import eu.europeana.api.client.thumbnails.processing.LargeThumbnailsetProcessing;
import eu.europeana.creative.dataset.IRTestConfigurations;
import eu.europeana.creative.dataset.culturecam.input.SelectionDescriptionEnum;
import eu.europeana.creative.dataset.culturecam.input.SelectionDescriptionImpl;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;

public class CultureCamThumbnailMapsTest extends ThumbnailAccessorUtils implements IRTestConfigurations{

	//private boolean overwriteThumbnails = false;
	String colSelectionFilename = "/selection/input/culturecam_5000.csv"; 
	String designInputFilename = "/selection/input/design_v1.csv";
	//String thumbnailMapFolder = "/selection/thumbnailmap";
	final String STEP_THUMBNAILMAP = "THUMBNAILMAP";
	final String STEP_SUBSET = "SUBSET";
	private String processingStep = null;
	private boolean overwriteThumbnails = false;
		
	@Before
	public void init(){
		String dataset = "culturecam";
		setDataset(dataset);
	}
	
	//@Test
	public void buildSelectedCollections() throws MyEuropeanaApiException, IOException{
		File colSelectionCvsFile = new File(getCollectionsCvsFolder() + colSelectionFilename);
		//we misuse the readThumbnailsMap as this is the same implementation as readCollectionsMap
		DatasetDescriptor descriptor;
		int missingThumbnails;
		int missingThumbnailsSum = 0;
		int expectedResultsTotal = 0;
		int expectedResults = 0;
		SelectionDescriptionImpl selectionDescription;
		String subsetName;
		
		Map<String, String> selectedCollections = readThumbnailsMap(colSelectionCvsFile);
		//#ID;Title;Portal link;Results;items;selection;dicriminator;Content selection comments
		
		for (Map.Entry<String, String> collection : selectedCollections.entrySet()) {
			selectionDescription = new SelectionDescriptionImpl(collection.getKey(), collection.getValue().split(";"));
			
			subsetName = buildSubSetName(selectionDescription);
			descriptor = new DatasetDescriptor(subsetName, selectionDescription.getId());
			this.setProcessingStep(STEP_THUMBNAILMAP);
			expectedResults = selectionDescription.getIntFieldValue(SelectionDescriptionEnum.RESULT_COUNT);
			
			File thumbnailsMapFile = getCollectionCsvFile(descriptor, STEP_THUMBNAILMAP);
			if(thumbnailsMapFile.exists()){
				log.info("Skip selected collection. Thumbnailsmap exists already :" + thumbnailsMapFile);
				continue;
			}
			
			missingThumbnails = createSubset(subsetName, selectionDescription.getId(), selectionDescription.getFieldValue(SelectionDescriptionEnum.PORTAL_LINK), 0, expectedResults);
			missingThumbnailsSum += missingThumbnails;
			expectedResultsTotal += expectedResults;
			if(missingThumbnails > 0)
				System.out.println("Missing thumbnails in dataset:" + descriptor + ": " + missingThumbnails);
		}
		//we expect no more than 10 missing Thumbnails
		log.info("Number of missing thumbnails: " + missingThumbnailsSum);
		log.info("Total expected results: " + expectedResultsTotal);
	}

	@Test
	public void buildSubsets() throws MyEuropeanaApiException, IOException{
		File colSelectionCvsFile = new File(getCollectionsCvsFolder() + colSelectionFilename);
		//we misuse the readThumbnailsMap as this is the same implementation as readCollectionsMap
		DatasetDescriptor descriptor;
		int selectionCount;
		String selectionType;
		SelectionDescriptionImpl selectionDescription;

		Map<String, String> selectedCollections = readThumbnailsMap(colSelectionCvsFile);
		//#ID;Title;Portal link;Results;items;selection;dicriminator;Content selection comments
		File thumbnailsMapFile = null;
		File subsetFile = null;
		Map<String, String> fullCollectionMap;
		Map<String, String> subsetThumbnailMap;
		
		for (Map.Entry<String, String> collection : selectedCollections.entrySet()) {
			
			selectionDescription = new SelectionDescriptionImpl(collection.getKey(), collection.getValue().split(";"));
			String subsetName = buildSubSetName(selectionDescription);
			descriptor = new DatasetDescriptor(subsetName, selectionDescription.getId());
			
			thumbnailsMapFile = getCollectionCsvFile(descriptor, STEP_THUMBNAILMAP);
			subsetFile = getCollectionCsvFile(descriptor, STEP_SUBSET);
			fullCollectionMap = readThumbnailsMap(thumbnailsMapFile);
			
			this.setProcessingStep(STEP_THUMBNAILMAP);
			selectionType = selectionDescription.getFieldValue(SelectionDescriptionEnum.SELECTION_TYPE);
			selectionCount = selectionDescription.getIntFieldValue(SelectionDescriptionEnum.SELECTED_COUNT);
			if("all".equals(selectionType)){
				//copy whole file
				log.trace("Copying to file: " + subsetFile );
				copyFile(thumbnailsMapFile, subsetFile);
			}else{
				//select thumbnails
				subsetThumbnailMap = generateSubset(fullCollectionMap, selectionCount); 
				//write thumbnails
				log.debug("writing subset of size: " + subsetThumbnailMap.size() + "\n to file:" + subsetFile);
				writeThumbnailsToCsvFile(descriptor, subsetThumbnailMap, subsetFile, POLICY_OVERWRITE_FILE);
			}
		}
	}

	private Map<String, String> generateSubset(
			Map<String, String> fullCollectionMap, int selectionCount) {
		
		Map<String, String> subsetMap = new HashMap<String, String>(selectionCount);
		if(fullCollectionMap.size() < selectionCount)
			throw new RuntimeException("Fullcollection has less items than the expected subset: " + selectionCount);
		
		Object[] keys = fullCollectionMap.keySet().toArray();
		int i;
		Random random = new Random();
		
		while(subsetMap.size() < selectionCount){
			i = random.nextInt(fullCollectionMap.size());
			subsetMap.put((String)keys[i], fullCollectionMap.get(keys[i]));
		}
		
		return subsetMap;
	}

	protected String buildSubSetName(
			SelectionDescriptionImpl selectionDescription) {
		String subsetName = selectionDescription.getFieldValue(SelectionDescriptionEnum.TITLE);
		subsetName = subsetName.substring(0, Math.min(subsetName.length(), 10));
		return subsetName;
	}
	
	
	protected File getCollectionCsvFile(DatasetDescriptor dataset) {
		return getCollectionCsvFile(dataset, getProcessingStep());
	}

	protected File getCollectionCsvFile(DatasetDescriptor dataset,
			final String processingStep) {
		if(STEP_THUMBNAILMAP.equals(processingStep) || STEP_SUBSET.equals(processingStep)){
			String fileName = getCollectionsCvsFolder() + processingStep.toLowerCase() + "/" + dataset.getImageSetName()
				+ "_" + encode(dataset.getCollectionName()) + ".csv";
			return new File(fileName);
		}else{
			return super.getCollectionCsvFile(dataset);
		}
	}
	
	//@Test
	public void downloadThumbnails() throws FileNotFoundException, IOException {
		File datasetFile = getConfig().getDatasetFile(getDataset());
		File downloadFolder = getConfig().getImageFolderAsFile(getDataset());
		
		if(!datasetFile.exists())
			performDatasetAggregation();
		
		LargeThumbnailsetProcessing datasetDownloader = new LargeThumbnailsetProcessing(datasetFile);
		ThumbnailDownloader observer = new ThumbnailDownloader(downloadFolder);
		observer.setSkipExistingFiles(!overwriteThumbnails);
		datasetDownloader.addObserver(observer);
		datasetDownloader.processThumbnailset(0, -1, 1000);
		
		log.debug("Skipped items: " + datasetDownloader.getSkippedItemsCount());
		log.warn("Failed downloads: " + datasetDownloader.getFailureCount());
		log.info("Downloaded files: " + datasetDownloader.getItemsProcessed());
		
		assertEquals(0, datasetDownloader.getFailureCount());
//		for (String itemId : skippedItems) {
//			System.out.println(itemId);
//		}
	}
	
	//@Test
	public void filterDesignThumbnails() throws IOException {
		
		//read design input
		File desginV1 = new File(getCollectionsCvsFolder() + designInputFilename);
		//we misuse the readThumbnailsMap as this is the same implementation as readCollectionsMap
		Map<String, String> designV1Thumbnails = readThumbnailsMap(desginV1);
		log.debug("Items in design_v1 dataset :" + designV1Thumbnails.size());
		
		//read culturecam subsets
		File cvsFolder = new File(getCollectionsCvsFolder() + STEP_SUBSET.toLowerCase() + "/");
		File[] collectionFiles = cvsFolder.listFiles();
		Map<String, String> subset;
		
		for (int i = 0; i < collectionFiles.length; i++) {
			//for each subset
			subset = readThumbnailsMap(collectionFiles[i]);
			
			for (String thumbnailId: subset.keySet()) {
				//remove items available in subsets
				if(designV1Thumbnails.containsKey(thumbnailId)){
					log.trace("removing item from subset: " + thumbnailId);
					designV1Thumbnails.remove(thumbnailId);					
				}
			}
			
		}
		
		DatasetDescriptor designSubset = new DatasetDescriptor("Design", "V1");
		File designSubsetFile = getCollectionCsvFile(designSubset, STEP_SUBSET);
		log.info("Writing items in desing subset: " + designV1Thumbnails.size());
		writeThumbnailsToCsvFile(designSubset, designV1Thumbnails, designSubsetFile);

		
//		log.trace("Closing dataset file");
//		datasetWriter.close();
	}
	
	private void performDatasetAggregation() throws IOException {
		File cvsFolder = new File(getCollectionsCvsFolder() + STEP_SUBSET.toLowerCase() + "/");
		File[] collectionFiles = cvsFolder.listFiles();
		BufferedReader reader = null;
		// String headerLine = null;
		String line = null;
		BufferedWriter datasetWriter = getDataSetFileWriter(false);

		log.debug("Aggregating dataset: " + getDataset());
		
		for (int i = 0; i < collectionFiles.length; i++) {
			reader = new BufferedReader(new FileReader(collectionFiles[i]));
			boolean firstLine = true;
			while ((line = reader.readLine()) != null) {
				// write headers to sysout
				if (firstLine) {
					log.debug("Writting dataset headerline: " + line);
					firstLine = false;
				}
				// write all data to dataset
				datasetWriter.write(line);
				datasetWriter.write("\n");

			}
			datasetWriter.flush();
			// close reader
			try {
				reader.close();
			} catch (IOException e) {
				System.out.println("cannot close reader for: "
						+ collectionFiles[i]);
				e.printStackTrace();
			}
		}
		log.trace("Closing dataset file");
		datasetWriter.close();
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

	public String getProcessingStep() {
		return processingStep;
	}

	public void setProcessingStep(String processingStep) {
		this.processingStep = processingStep;
	}
	
	protected void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
	        destFile.getParentFile().mkdirs();
	    	destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}
	
	//@Test
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
