package eu.europeana.creative.dataset.culturecam.v2;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
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
import eu.europeana.creative.dataset.IRTestConfigurations;
import eu.europeana.creative.dataset.culturecam.input.SelectionDescriptionEnum;
import eu.europeana.creative.dataset.culturecam.input.SelectionDescriptionImpl;
import eu.europeana.creative.dataset.culturecam.v2.download.CimecThumbnailDownloader;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;

public class CultureCamV2ThumbnailMapsTest extends ThumbnailAccessorUtils
		implements IRTestConfigurations {

	// private boolean overwriteThumbnails = false;
	String tagSelectionFilename = "/selection/input/tags/new.csv";
	String cimecIdsFilename = "/selection/input/tags/cimec_ids.csv";

	
	// String colectionThumbnailsFilename =
	// "/selection/input/thumbnails_v1/culturecam_5000.csv";
	// String colectionClassifiedFilename =
	// "/selection/input/thumbnails_v1/culturecam_pd_classified.csv";
	//
	// String designInputFilename = "/selection/input/design_v1.csv";
	// String thumbnailMapFolder = "/selection/thumbnailmap";
	// final String STEP_THUMBNAILMAP = "THUMBNAILMAP";
	final String STEP_THUMBNAILMAP_V2 = "THUMBNAILMAP_V2";

	final String STEP_SUBSET_V2 = "SUBSET_V2";
	// final String STEP_CLASSIFIED_V1 = "CLASSIFIED_V1";
	// final String STEP_FILTER_OUT = "FILTER_OUT";

	private String processingStep = null;
	private boolean overwriteThumbnails = false;

	final String IMAGE_FOLDER = "/app/eucreative/imagesimilarityhome/culturecam/image/";

	@Before
	public void init() {
		String dataset = "culturecam";
		setDataset(dataset);
	}

	//@Test
	public void buildNewTagSelection() throws MyEuropeanaApiException,
			IOException {
		String jsonFile = "/myeuropeana/culturecam/user_tag_action=LIST&tag=new.json";
		TagsApiResponse apiResponse = readJsonFile(jsonFile);

		ThumbnailFromTagsResponseAccessor ta = new ThumbnailFromTagsResponseAccessor();
		File tagSelectionFile = new File(getCollectionsCvsFolder()
				+ tagSelectionFilename);

		if (tagSelectionFile.exists())
			tagSelectionFile.delete();

		Map<String, Map<String, String>> thumbnailsByTag = ta
				.getThumbnailsFromTagsApiResponse(apiResponse);
		DatasetDescriptor descriptor;

		int objectCount = 0;
		for (Map.Entry<String, Map<String, String>> entry : thumbnailsByTag
				.entrySet()) {
			descriptor = new DatasetDescriptor(getDataset(), entry.getKey());
			descriptor.setClassifications(new String[] { entry.getKey() });
			writeThumbnailsToCsvFile(descriptor, entry.getValue(),
					tagSelectionFile, POLICY_APPEND_TO_FILE);
			objectCount += entry.getValue().size();
			System.out.println("TAG: " + entry.getKey() + " - "
					+ entry.getValue().size());
		}

		assertEquals(apiResponse.getTotalResults(), objectCount);

	}

	private TagsApiResponse readJsonFile(String testResource)
			throws IOException, MyEuropeanaApiException {
		TagsApiResponse res = null;
		InputStream resourceAsStream = null;
		try {
			resourceAsStream = getClass().getResourceAsStream(testResource);
			MyEuropeanaClient client = new MyEuropeanaClientImpl();
			res = client.parseTagsApiResponse(resourceAsStream);

		} finally {
			if (resourceAsStream != null)
				resourceAsStream.close();
		}

		return res;

	}

	@Test
	public void verifyThumbnails() throws FileNotFoundException, IOException {
		// File datasetFile = getConfig().getDatasetFile(getDataset());
		//File downloadFolder = getConfig().getImageFolderAsFile(getDataset());
		File tagThumbnailMap = new File(getCollectionsCvsFolder()
				+ tagSelectionFilename);
		
		Map<String, String> newTagMap = readThumbnailsMap(tagThumbnailMap);
		String id;
		File thumbnailFile;
		for (Map.Entry<String, String> tag : newTagMap.entrySet()) {
			id = tag.getKey();
			thumbnailFile = getConfig().getImageFile(getDataset(), id);
			if(thumbnailFile.length() == 3583)
				System.out.println(id);
		}
	}
	
	@Test
	public void downloadCimecThumbnails() throws FileNotFoundException, IOException {
		// File datasetFile = getConfig().getDatasetFile(getDataset());
		//File downloadFolder = getConfig().getImageFolderAsFile(getDataset());
		File cimecIds = new File(getCollectionsCvsFolder()
				+ cimecIdsFilename);
		
		File downloadFolder = getConfig().getImageFolderAsFile(getDataset());
		
		CimecThumbnailDownloader downloader = new CimecThumbnailDownloader(downloadFolder);
		//ThumbnailDownloader observer = new ThumbnailDownloader(downloadFolder);
//		observer.setSkipExistingFiles(!overwriteThumbnails);
//		datasetDownloader.addObserver(observer);
//		datasetDownloader.processThumbnailset(0, -1, 1000);
		
		Map<String, String> thumbnailMap = readThumbnailsMap(cimecIds);
		downloader.downloadCimecIds(thumbnailMap);
	}
	
	

	
	//@Test
		public void downloadThumbnails() throws FileNotFoundException, IOException {
			// File datasetFile = getConfig().getDatasetFile(getDataset());
			File downloadFolder = getConfig().getImageFolderAsFile(getDataset());
			File tagThumbnailMap = new File(getCollectionsCvsFolder()
					+ tagSelectionFilename);

//			if (!tagThumbnailMap.exists())
//				performDatasetAggregation(STEP_THUMBNAILMAP_V2, tagThumbnailMap);

			LargeThumbnailsetProcessing datasetDownloader = new LargeThumbnailsetProcessing(
					tagThumbnailMap);
			ThumbnailDownloader observer = new ThumbnailDownloader(downloadFolder);
			observer.setSkipExistingFiles(!overwriteThumbnails);
			datasetDownloader.addObserver(observer);
			datasetDownloader.processThumbnailset(0, -1, 1000);

			log.debug("Skipped items: " + datasetDownloader.getSkippedItemsCount());
			log.warn("Failed downloads: " + datasetDownloader.getFailureCount());
			log.info("Downloaded files: " + datasetDownloader.getItemsProcessed());

			assertEquals(0, datasetDownloader.getFailureCount());
			// for (String itemId : skippedItems) {
			// System.out.println(itemId);
			// }
		}
	// File colSelectionCvsFile = new File(getCollectionsCvsFolder() +
	// tagSelectionFilename);
	//
	//
	//
	//
	//
	// //we misuse the readThumbnailsMap as this is the same implementation as
	// readCollectionsMap
	// DatasetDescriptor descriptor;
	// int missingThumbnails;
	// int missingThumbnailsSum = 0;
	// int expectedResultsTotal = 0;
	// int expectedResults = 0;
	// SelectionDescriptionImpl selectionDescription;
	// String subsetName;
	//
	// Map<String, String> selectedCollections =
	// readThumbnailsMap(colSelectionCvsFile);
	// //#ID;Title;Portal link;Results;items;selection;dicriminator;Content
	// selection comments
	//
	// for (Map.Entry<String, String> collection :
	// selectedCollections.entrySet()) {
	// selectionDescription = new SelectionDescriptionImpl(collection.getKey(),
	// collection.getValue().split(";"));
	//
	// subsetName = buildSubSetName(selectionDescription);
	// descriptor = new DatasetDescriptor(subsetName,
	// selectionDescription.getId());
	// this.setProcessingStep(STEP_THUMBNAILMAP_V2);
	// expectedResults =
	// selectionDescription.getIntFieldValue(SelectionDescriptionEnum.RESULT_COUNT);
	//
	// File thumbnailsMapFile = getCollectionCsvFile(descriptor,
	// STEP_THUMBNAILMAP_V2);
	// if(thumbnailsMapFile.exists()){
	// log.info("Skip selected collection. Thumbnailsmap exists already :" +
	// thumbnailsMapFile);
	// continue;
	// }
	//
	// missingThumbnails = createSubset(subsetName,
	// selectionDescription.getId(),
	// selectionDescription.getFieldValue(SelectionDescriptionEnum.PORTAL_LINK),
	// 0, expectedResults);
	// missingThumbnailsSum += missingThumbnails;
	// expectedResultsTotal += expectedResults;
	// if(missingThumbnails > 0)
	// System.out.println("Missing thumbnails in dataset:" + descriptor + ": " +
	// missingThumbnails);
	// }
	// //we expect no more than 10 missing Thumbnails
	// log.info("Number of missing thumbnails: " + missingThumbnailsSum);
	// log.info("Total expected results: " + expectedResultsTotal);

	// 3@Test
	// public void categorizeSubsetThumbnails() throws FileNotFoundException,
	// IOException {
	//
	// File colSelectionCvsFile = new File(getCollectionsCvsFolder() +
	// colSelectionFilename);
	// //we misuse the readThumbnailsMap as this is the same implementation as
	// readCollectionsMap
	// DatasetDescriptor descriptor;
	// SelectionDescriptionImpl selectionDescription;
	// String subsetName;
	//
	// Map<String, String> selectedCollections =
	// readThumbnailsMap(colSelectionCvsFile);
	// //#ID;Title;Portal link;Results;items;selection;dicriminator;Content
	// selection comments
	//
	// File categorizedThumbnailsFile;
	// File thumbnailsCvsFile;
	//
	// for (Map.Entry<String, String> collection :
	// selectedCollections.entrySet()) {
	// selectionDescription = new SelectionDescriptionImpl(collection.getKey(),
	// collection.getValue().split(";"));
	//
	// subsetName = buildSubSetName(selectionDescription);
	// descriptor = new DatasetDescriptor(subsetName,
	// selectionDescription.getId());
	// this.setProcessingStep(STEP_CLASSIFIED_V1);
	//
	// thumbnailsCvsFile = getCollectionCsvFile(descriptor,
	// STEP_THUMBNAILMAP_V2);
	//
	// //categorize by color-fullness
	// categorizedThumbnailsFile = categorizeThumbnails(descriptor,
	// thumbnailsCvsFile);
	//
	// System.out.println("subset written to file: " +
	// categorizedThumbnailsFile);
	// }
	// }

	// 4@Test
	// public void filterDesignThumbnails() throws IOException {
	//
	// //read design input
	// File desginV1 = new File(getCollectionsCvsFolder() +
	// designInputFilename);
	// //we misuse the readThumbnailsMap as this is the same implementation as
	// readCollectionsMap
	// Map<String, String> designV1Thumbnails = readThumbnailsMap(desginV1);
	// log.debug("Items in design_v1 dataset :" + designV1Thumbnails.size());
	//
	// //read culturecam subsets
	// File cvsFolder = new File(getCollectionsCvsFolder() +
	// STEP_SUBSET_V1.toLowerCase() + "/");
	// File[] collectionFiles = cvsFolder.listFiles();
	// Map<String, String> subset;
	//
	// for (int i = 0; i < collectionFiles.length; i++) {
	// //for each subset
	// subset = readThumbnailsMap(collectionFiles[i]);
	//
	// for (String thumbnailId: subset.keySet()) {
	// //remove items available in subsets
	// if(designV1Thumbnails.containsKey(thumbnailId)){
	// log.trace("removing item from subset: " + thumbnailId);
	// designV1Thumbnails.remove(thumbnailId);
	// }
	// }
	//
	// }
	//
	// DatasetDescriptor designSubset = new DatasetDescriptor("Design", "V1");
	// File designSubsetFile = getCollectionCsvFile(designSubset,
	// STEP_SUBSET_V1);
	// log.info("Writing items in desing subset: " + designV1Thumbnails.size());
	// writeThumbnailsToCsvFile(designSubset, designV1Thumbnails,
	// designSubsetFile);
	//
	//
	// // log.trace("Closing dataset file");
	// // datasetWriter.close();
	// }

	// @Test
	// public void filterDesignNonPDThumbnails() throws IOException,
	// EuropeanaApiProblem {
	//
	// //read design input
	// File desginV1 = new File(getCollectionsCvsFolder() +
	// designInputFilename);
	// //we misuse the readThumbnailsMap as this is the same implementation as
	// readCollectionsMap
	// Map<String, String> designV1Thumbnails = readThumbnailsMap(desginV1);
	// log.debug("Items in design_v1 dataset :" + designV1Thumbnails.size());
	//
	// EuropeanaApi2Client euClient = new EuropeanaApi2Client();
	// EuropeanaApi2Item obj;
	//
	// Map<String, String> designNonPDThumbnails = new HashMap<String,
	// String>();
	// Api2Query searchQuery = new Api2Query();
	// EuropeanaApi2Results searchResults;
	//
	//
	// for (Map.Entry<String, String> item : designV1Thumbnails.entrySet()) {
	// obj = null;//clear obj
	// if(!(searchQuery.getSubQueries() == null) &&
	// !searchQuery.getSubQueries().isEmpty())
	// searchQuery.getSubQueries().remove(0);//re-initialize
	//
	// searchQuery.addSubQuery(new SubQuery("europeana_id", item.getKey(),
	// false, true, false));
	//
	// searchResults = euClient.searchApi2(searchQuery, 2, -1);
	//
	// if(!searchResults.getItems().isEmpty())
	// obj = searchResults.getItems().get(0);
	// else
	// System.out.println("Cannot find object with id query: " + item.getKey());
	//
	// if(obj != null && !hasPdRights(obj)){
	// designNonPDThumbnails.put(item.getKey(), item.getValue());
	// }
	// }
	//
	// DatasetDescriptor designSubset = new DatasetDescriptor("Design", "V1");
	// File filterOoutFile = getCollectionCsvFile(designSubset,
	// STEP_FILTER_OUT);
	// log.info("Writing items in desing subset: " +
	// designNonPDThumbnails.size());
	// writeThumbnailsToCsvFile(designSubset, designNonPDThumbnails,
	// filterOoutFile);
	//
	//
	// // log.trace("Closing dataset file");
	// // datasetWriter.close();
	// }
	//

	// protected boolean hasPdRights(BaseAggregation aggregation) {
	// boolean ret = false;
	// if(aggregation != null && aggregation.getEdmRights() != null){
	//
	// Collection<List<String>> allRights = aggregation.getEdmRights().values();
	//
	// for (List<String> rightsList : allRights) {
	// if(rightsList == null || rightsList.isEmpty())
	// continue;
	//
	// else for (String rights : rightsList) {
	// if(rights != null && rights.indexOf("/publicdomain/") > -1)
	// ret = true;
	// }
	// }
	//
	//
	// }
	// return ret;
	// }
	//
	// protected boolean hasPdRights(EuropeanaApi2Item item) {
	// boolean ret = false;
	// if(item != null && item.getRights() != null){
	// for (String rights : item.getRights()) {
	// if(rights.indexOf("/publicdomain/") > -1)
	// return true;
	// }
	// }
	// return ret;
	// }

	// @Test
	public void aggregateDataset() throws IOException {
		File datasetFile = getConfig().getDatasetFile(getDataset());
		performDatasetAggregation(STEP_SUBSET_V2, datasetFile);
	}

	// public File categorizeThumbnails(DatasetDescriptor datasetDescriptor,
	// File thumbnailsFile) throws FileNotFoundException, IOException {
	//
	// // String thumbnailsFile = getCvsFileForStep(datasetDescriptor,
	// // STEP_THUMBNAILS);
	// // new File(thumbnailsFile)
	// File outputFile = getCollectionCsvFile(datasetDescriptor,
	// STEP_CLASSIFIED_V1);
	//
	// LargeThumbnailsetProcessing datasetCategorization = new
	// LargeThumbnailsetProcessing(
	// thumbnailsFile);
	// // String imageFolder = getConfiguration().getImageFolder(getDataset());
	// String imageFolder = IMAGE_FOLDER;
	//
	// GrayScaleSepiaDetector observer = new GrayScaleSepiaDetector(new File(
	// imageFolder), 85, 3);
	// //final File outputFile = new File(outFile);
	// observer.setOutputFile(outputFile);
	//
	// datasetCategorization.addObserver(observer);
	// if(blockSize < 0)
	// blockSize = 1000;
	//
	// datasetCategorization.processThumbnailset(start, limit, blockSize);
	//
	// System.out.println("Skipped items: "
	// + datasetCategorization.getFailureCount());
	// return outputFile;
	//
	// }

	protected String buildSubSetName(
			SelectionDescriptionImpl selectionDescription) {
		String subsetName = selectionDescription
				.getFieldValue(SelectionDescriptionEnum.TITLE);
		subsetName = subsetName.substring(0, Math.min(subsetName.length(), 15));
		return subsetName;
	}

	protected File getCollectionCsvFile(DatasetDescriptor dataset) {
		return getCollectionCsvFile(dataset, getProcessingStep());
	}

	protected File getCollectionCsvFile(DatasetDescriptor dataset,
			final String processingStep) {
		if (processingStep != null) {
			String fileName = getCollectionsCvsFolder()
					+ processingStep.toLowerCase() + "/"
					+ dataset.getImageSetName() + "_"
					+ encode(dataset.getCollectionName()) + ".csv";
			return new File(fileName);
		} else {
			return super.getCollectionCsvFile(dataset);
		}
	}

	// @Test
	// public void categorizeThumbnails() throws FileNotFoundException,
	// IOException {

	// DatasetDescriptor datasetDescriptor;
	// File thumbnailsFile;

	// String thumbnailsFile = getCvsFileForStep(datasetDescriptor,
	// STEP_THUMBNAILS);
	// new File(thumbnailsFile)
	// String outFile = getCvsFileForStep(datasetDescriptor, STEP_CLASSIFIED);

	// File thumbnailsFile = new File(getCollectionsCvsFolder() +
	// colectionThumbnailsFilename);
	// File outFile = new File(getCollectionsCvsFolder() +
	// colectionClassifiedFilename);
	//
	//
	// LargeThumbnailsetProcessing datasetCategorization = new
	// LargeThumbnailsetProcessing(
	// thumbnailsFile);
	// //String imageFolder = getConfiguration().getImageFolder(getDataset());
	// String imageFolder = IMAGE_FOLDER;
	//
	// GrayScaleSepiaDetector observer = new GrayScaleSepiaDetector(new File(
	// imageFolder), 85, 3);
	// //final File outputFile = new File(outFile);
	// observer.setOutputFile(outFile);
	//
	// datasetCategorization.addObserver(observer);
	// blockSize = 1000;
	// datasetCategorization.processThumbnailset(start, limit, blockSize);
	//
	// System.out.println("Skipped items: "
	// + datasetCategorization.getFailureCount());
	// //return outFile;
	//
	// }

	private void performDatasetAggregation(String step, File datasetFile)
			throws IOException {
		File cvsFolder = new File(getCollectionsCvsFolder()
				+ step.toLowerCase() + "/");
		File[] collectionFiles = cvsFolder.listFiles();
		BufferedReader reader = null;
		// String headerLine = null;
		String line = null;
		// BufferedWriter datasetWriter = getDataSetFileWriter(false);
		datasetFile.getParentFile().mkdirs();
		BufferedWriter datasetWriter = new BufferedWriter(new FileWriter(
				datasetFile));

		log.debug("Aggregating dataset: " + getDataset() + " to file: "
				+ datasetFile);

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
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}
}
