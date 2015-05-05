package eu.europeana.creative.dataset.pd;

import it.cnr.isti.vir.features.mpeg7.imageanalysis.ScalableColorPlusImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.api.client.EuropeanaApi2Client;
import eu.europeana.api.client.dataset.DatasetDescriptor;
import eu.europeana.api.client.exception.EuropeanaApiProblem;
import eu.europeana.api.client.model.EuropeanaApi2Results;
import eu.europeana.api.client.model.search.Facet;
import eu.europeana.api.client.model.search.FacetField;
import eu.europeana.api.client.myeuropeana.exception.MyEuropeanaApiException;
import eu.europeana.api.client.thumbnails.ThumbnailAccessorUtils;
import eu.europeana.api.client.thumbnails.download.ThumbnailDownloader;
import eu.europeana.api.client.thumbnails.processing.LargeThumbnailsetProcessing;
import eu.europeana.creative.dataset.IRTestConfigurations;
import eu.europeana.creative.dataset.culturecam.input.SelectionDescriptionEnum;
import eu.europeana.creative.dataset.culturecam.input.SelectionDescriptionImpl;
import eu.europeana.creative.dataset.pt.classification.GrayScaleSepiaDetector;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;

public class PdThumbnailMapsTest extends ThumbnailAccessorUtils implements IRTestConfigurations{

	//private boolean overwriteThumbnails = false;
	String jsonInputFilename = "/collections/pd/selection/input/pd_collections_facets.json"; 
	String collectionsInputFilename = "/selection/input/pd_collections.csv"; 
	String selectedCollectionsInputFilename = "/selection/input/selected_pd_collections.csv";
	//String designInputFilename = "/selection/input/design_v1.csv";
	//String thumbnailMapFolder = "/selection/thumbnailmap";
	final String STEP_THUMBNAILMAP = "THUMBNAILMAP";
	final String STEP_SUBSET = "SUBSET";
	private static final String STEP_CLASSIFIED = "CLASSIFIED";
	private String processingStep = null;
	private boolean overwriteThumbnails = false;
	final String IMAGE_FOLDER = "/app/eucreative/imagesimilarityhome/pd/image/";
	
		
	@Before
	public void init(){
		String dataset = "pd";
		setDataset(dataset);
	}
	
	//@Test
	public void buildCollectionsCvs() throws IOException, EuropeanaApiProblem{
		String json =	readJsonFile(jsonInputFilename);
		EuropeanaApi2Client client = new EuropeanaApi2Client();
		EuropeanaApi2Results results = client.parseApiResponse(json);
		
		File pdCollectionFacets = new File(getCollectionsCvsFolder() + collectionsInputFilename);
		Facet facet = results.getFacets().get(0);
		Map<String, String> collectionsMap = new LinkedHashMap<String, String>();
		
		String collection;
		String collectionData;
		//build Map
		for (FacetField field : facet.getFields()) {
			collection = field.getLabel();
			collectionData = field.getCount()+";" + "http://www.europeana.eu/portal/search.html?query=europeana_collectionName:";
			//escape bracket
			collectionData += collection.replace("(", "\\(");
			collectionData += "&profile=minimal&qf=RIGHTS%3Ahttp%3A%2F%2Fcreativecommons.org%2Fpublicdomain%2F*&qt=false";
			
			//keep only collections with more than 100 objects
			if(field.getCount() > 100){
				collectionsMap.put(collection, collectionData);
				System.out.println(collection + ":" + field.getCount());
				
			}else
				break;
			
		}
		
		DatasetDescriptor descriptor = new DatasetDescriptor("facets", "pd-collection");
		writeThumbnailsToCsvFile(descriptor, collectionsMap, pdCollectionFacets, POLICY_OVERWRITE_FILE );
		
	}	
	
	
	//@Test
	public void buildSelectedCollections() throws MyEuropeanaApiException, IOException{
		File selectedPdCollections = new File(getCollectionsCvsFolder() + selectedCollectionsInputFilename);
		//we misuse the readThumbnailsMap as this is the same implementation as readCollectionsMap
		
		DatasetDescriptor descriptor;
		int missingThumbnails;
		int missingThumbnailsSum = 0;
		int expectedResultsTotal = 0;
		int expectedResults = 0;
		SelectionDescriptionImpl selectionDescription;
		
		Map<String, String> selectedCollections = readThumbnailsMap(selectedPdCollections);
		//#ID;Title;Portal link;Results;items;selection;dicriminator;Content selection comments
		
		for (Map.Entry<String, String> collection : selectedCollections.entrySet()) {
			selectionDescription = new SelectionDescriptionImpl(collection.getKey(), collection.getValue().split(";"));
			
			descriptor = buildDatasetDescriptor(selectionDescription);
			
			this.setProcessingStep(STEP_THUMBNAILMAP);
			expectedResults = selectionDescription.getIntFieldValue(SelectionDescriptionEnum.RESULT_COUNT);
			
			File thumbnailsMapFile = getCollectionCsvFile(descriptor, STEP_THUMBNAILMAP);
			if(thumbnailsMapFile.exists()){
				log.info("Skip selected collection. Thumbnailsmap exists already :" + thumbnailsMapFile);
				continue;
			}
			
			missingThumbnails = createSubset(descriptor.getImageSetName(), descriptor.getCollectionName(), selectionDescription.getFieldValue(SelectionDescriptionEnum.PORTAL_LINK), 0, expectedResults);
			missingThumbnailsSum += missingThumbnails;
			expectedResultsTotal += expectedResults;
			if(missingThumbnails > 0)
				System.out.println("Missing thumbnails in dataset:" + descriptor + ": " + missingThumbnails);
		}
		//we expect no more than 10 missing Thumbnails
		log.info("Number of missing thumbnails: " + missingThumbnailsSum);
		log.info("Total expected results: " + expectedResultsTotal);
	}

	protected DatasetDescriptor buildDatasetDescriptor(
			SelectionDescriptionImpl selectionDescription) {
		DatasetDescriptor descriptor;
		String[] idParts;
		idParts = selectionDescription.getId().split("_", 2);
		descriptor = new DatasetDescriptor(idParts[0], idParts[1].replace('(', '-'));
		return descriptor;
	}

	//@Test
	public void downloadSelectedCollectionsThumbnails() throws FileNotFoundException, IOException {
		
		File selectedPdCollections = new File(getCollectionsCvsFolder() + selectedCollectionsInputFilename);
		//we misuse the readThumbnailsMap as this is the same implementation as readCollectionsMap
		
		Map<String, String> selectedCollections = readThumbnailsMap(selectedPdCollections);
		//#ID;Title;Portal link;Results;items;selection;dicriminator;Content selection comments
		
		DatasetDescriptor descriptor;
		SelectionDescriptionImpl selectionDescription;
		
		for (Map.Entry<String, String> collection : selectedCollections.entrySet()) {
			selectionDescription = new SelectionDescriptionImpl(collection.getKey(), collection.getValue().split(";"));
			descriptor = buildDatasetDescriptor(selectionDescription);
			
			System.out.println("Downloading thumbnails for collection: " + descriptor);
			downloadThumbnails(descriptor);
		}
		
	}
	
	
	private void downloadThumbnails(DatasetDescriptor descriptor) throws FileNotFoundException, IOException {
		//this.setProcessingStep(STEP_THUMBNAILMAP);
		//expectedResults = selectionDescription.getIntFieldValue(SelectionDescriptionEnum.RESULT_COUNT);
		File thumbnailsMapFile = getCollectionCsvFile(descriptor, STEP_THUMBNAILMAP);
		File downloadFolder = getConfig().getImageFolderAsFile(getDataset());
		
		LargeThumbnailsetProcessing datasetDownloader = new LargeThumbnailsetProcessing(thumbnailsMapFile);
		ThumbnailDownloader observer = new ThumbnailDownloader(downloadFolder);
		observer.setSkipExistingFiles(!overwriteThumbnails);
		observer.setFilterThumbnails(true);
		
		datasetDownloader.addObserver(observer);
		datasetDownloader.processThumbnailset(0, -1, 1000);
		
		log.debug("Skipped items: " + datasetDownloader.getSkippedItemsCount());
		log.warn("Failed downloads: " + datasetDownloader.getFailureCount());
		log.info("Downloaded files: " + datasetDownloader.getItemsProcessed());
		
	}
	
	//@Test
	public void categorizeSubsetThumbnails() throws FileNotFoundException, IOException {
		
		File selectedPdCollections = new File(getCollectionsCvsFolder() + selectedCollectionsInputFilename);
		//we misuse the readThumbnailsMap as this is the same implementation as readCollectionsMap
		
		Map<String, String> selectedCollections = readThumbnailsMap(selectedPdCollections);
		//#ID;Title;Portal link;Results;items;selection;dicriminator;Content selection comments
		
		DatasetDescriptor descriptor;
		SelectionDescriptionImpl selectionDescription;
		
		for (Map.Entry<String, String> collection : selectedCollections.entrySet()) {
			selectionDescription = new SelectionDescriptionImpl(collection.getKey(), collection.getValue().split(";"));
			descriptor = buildDatasetDescriptor(selectionDescription);
			
			System.out.println("performing thumbnail categorization for collection: " + descriptor);
			categorizeThumbnails(descriptor);
		}
		
	}
	
	public File categorizeThumbnails(DatasetDescriptor datasetDescriptor) throws FileNotFoundException, IOException {

		// String thumbnailsFile = getCvsFileForStep(datasetDescriptor,
		// STEP_THUMBNAILS);
		// new File(thumbnailsFile)
		File inputFile = getCollectionCsvFile(datasetDescriptor, STEP_THUMBNAILMAP);
		File outputFile = getCollectionCsvFile(datasetDescriptor, STEP_CLASSIFIED);

		LargeThumbnailsetProcessing datasetCategorization = new LargeThumbnailsetProcessing(
				inputFile);
		// String imageFolder = getConfiguration().getImageFolder(getDataset());
		String imageFolder = IMAGE_FOLDER;

		GrayScaleSepiaDetector observer = new GrayScaleSepiaDetector(new File(
				imageFolder), 85, 3);
		//final File outputFile = new File(outFile);
		//Set<String> selectedCategories = new HashSet<String>();
		//selectedCategories.add(ScalableColorPlusImpl.ImageType.COLOR.)
		
		observer.setOutputFile(outputFile);

		datasetCategorization.addObserver(observer);
		if(blockSize < 0)
			blockSize = 1000;
		
		datasetCategorization.processThumbnailset(start, limit, blockSize);

		System.out.println("Skipped items: "
				+ datasetCategorization.getFailureCount());
		return outputFile;

	}
	

//	protected Map<String, String> generateSubset(
//			Map<String, String> fullCollectionMap, int selectionCount) {
//		
//		Map<String, String> subsetMap = new HashMap<String, String>(selectionCount);
//		if(fullCollectionMap.size() < selectionCount)
//			throw new RuntimeException("Fullcollection has less items than the expected subset: " + selectionCount);
//		
//		Object[] keys = fullCollectionMap.keySet().toArray();
//		int i;
//		Random random = new Random();
//		
//		while(subsetMap.size() < selectionCount){
//			i = random.nextInt(fullCollectionMap.size());
//			subsetMap.put((String)keys[i], fullCollectionMap.get(keys[i]));
//		}
//		
//		return subsetMap;
//	}

//	protected String buildSubSetName(
//			SelectionDescriptionImpl selectionDescription) {
//		String subsetName = selectionDescription.getFieldValue(SelectionDescriptionEnum.TITLE);
//		subsetName = subsetName.substring(0, Math.min(subsetName.length(), 10));
//		return subsetName;
//	}
	
	
	protected File getCollectionCsvFile(DatasetDescriptor dataset) {
		return getCollectionCsvFile(dataset, getProcessingStep());
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
	public void performDatasetAggregation() throws IOException {
		File cvsFolder = new File(getCollectionsCvsFolder() + STEP_CLASSIFIED.toLowerCase() + "/");
		File[] collectionFiles = cvsFolder.listFiles();
		BufferedReader reader = null;
		// String headerLine = null;
		String line = null;
		BufferedWriter datasetWriter = getDataSetFileWriter(false);

		log.debug("Aggregating dataset: " + getDataset());
		boolean isColorful;
		
		for (int i = 0; i < collectionFiles.length; i++) {
			reader = new BufferedReader(new FileReader(collectionFiles[i]));
			boolean firstLine = true;
			while ((line = reader.readLine()) != null) {
				// write headers to sysout
				if (firstLine) {
					log.debug("Writting dataset headerline: " + line);
					firstLine = false;
				}
				
				isColorful = line.endsWith(";COLOR");
				// write all data to dataset
				if(isColorful){
					datasetWriter.write(line);
					datasetWriter.write("\n");
				}

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
	
	@Test
	public void copyUsedImages() throws FileNotFoundException, IOException{
		File datasetFile = getConfig().getDatasetFile(getDataset());
		Map<String, String> thumbnailsMap = readThumbnailsMap(datasetFile);
		File sourceFile, destFile;
		
		for (String id : thumbnailsMap.keySet()) {
			sourceFile = getConfig().getImageFile(getDataset(), id);
			destFile = new File(sourceFile.getAbsolutePath().replace("app", "tmp"));
			copyFile(sourceFile, destFile);
		}
		System.out.println("completed copying images!");
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
	    
		if(!destFile.getParentFile().exists())
			destFile.getParentFile().mkdirs();
		
		if(!destFile.exists()) 
	        destFile.createNewFile();
	    

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
	
	private String readJsonFile(String testResource) throws IOException {
		BufferedReader reader = null;
		StringBuilder out = null;
		try {
			InputStream resourceAsStream = getClass().getResourceAsStream(
					testResource);
			reader = new BufferedReader(new InputStreamReader(
					resourceAsStream));
			out = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				out.append(line);
			}
		} finally {
			if(reader!= null)
				reader.close();
		}
		return out.toString();

	}
}
