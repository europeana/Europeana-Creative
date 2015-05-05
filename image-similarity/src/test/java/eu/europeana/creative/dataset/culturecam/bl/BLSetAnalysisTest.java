package eu.europeana.creative.dataset.culturecam.bl;

import it.cnr.isti.feature.extraction.FeatureExtractionException;
import it.cnr.isti.vir.features.IFeaturesCollector;
import it.cnr.isti.vir.features.mpeg7.imageanalysis.ScalableColorPlusImpl.ImageType;
import it.cnr.isti.vir.similarity.knn.IntDouble;
import it.cnr.isti.vir.similarity.knn.IntDoubleString;
import it.cnr.isti.vir.similarity.metric.LireMetric;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.junit.Test;

import eu.europeana.api.client.dataset.DatasetDescriptor;
import eu.europeana.api.client.exception.TechnicalRuntimeException;
import eu.europeana.api.client.thumbnails.processing.LargeThumbnailsetProcessing;
import eu.europeana.creative.dataset.culturecam.bl.analysis.SubsetAnalyserImpl;
import eu.europeana.creative.dataset.pt.classification.GrayScaleSepiaDetector;

public class BLSetAnalysisTest extends BaseBlTest {

	@Test
	public void performDatasetAggregation() throws IOException {
		
		String datasetName = "culturecam";
		setDataset(datasetName);
		
		File cvsFolder = new File(getBlCvsFolder(STEP_FILTERED_ORDERED));
		File[] collectionFiles = cvsFolder.listFiles();
		
		File outFolder = new File(getBlCvsFolder(STEP_AGGREGATED));
		File outFile = new File(outFolder, "BL_FLickr.csv");
		
		
		BufferedReader reader = null;
		// String headerLine = null;
		String line = null;
		
		outFile.getParentFile().mkdirs();
		BufferedWriter datasetWriter = new BufferedWriter(new FileWriter(outFile));

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
	
	
	//@Test
	public void analyseBlSets() throws Exception {
		for (String setId : blSets.keySet()) {
			log.debug("Analyzing set: " + setId);
			analyseBlSet(setId);
			// break;
		}
	}

	private void analyseBlSet(String setId) throws FileNotFoundException,
			IOException, FeatureExtractionException {

		String datasetName = "culturecam";
		setDataset(datasetName);
		final String subsetName = blSets.get(setId);

		DatasetDescriptor datasetDescriptor = new DatasetDescriptor(subsetName,
				setId);
		blockSize = 1000;

		String thumbnailsFile = getCvsFileForStep(datasetDescriptor,
				STEP_THUMBNAILS);
		File thumbnailsCvsFile = new File(thumbnailsFile);

		//order by avg distance descending
		SortedSet<IntDoubleString> order = generateOrder(datasetName,
				subsetName, datasetDescriptor, thumbnailsCvsFile);

		if(order.isEmpty()){
			log.warn("No items in dataset. Processing stoped for dataset: " + datasetName);
			return;
			//System.out.println("No items dataset: " + );
		}
		
		//categorize by color-fullness 
		File categorizationFile = categorizeThumbnails(datasetDescriptor,
				thumbnailsCvsFile);
		Map<String, String> categorizedMap = readThumbnailsMap(categorizationFile);
		
		//filter definition
		String[] filter = new String[] { ImageType.GRAYSCALE.name() };
		List<String> filterOut = Arrays.asList(filter); 
		
		//generate filtered ordered
		File filteredOrderedFile = writeFilteredOrderedSubset(datasetDescriptor,
				order, categorizedMap, filterOut);
		
		//read filtered ordered map
		Map<String, String> filteredOrderedMap = readThumbnailsMap(filteredOrderedFile);
		log.debug(datasetDescriptor.toString() + " : " + filteredOrderedMap.size());
	}

	private File writeFilteredOrderedSubset(DatasetDescriptor datasetDescriptor,
			SortedSet<IntDoubleString> order,
			Map<String, String> categorizedMap, List<String> filterOut) {
		
		String filteredOrderedFileName = getCvsFileForStep(datasetDescriptor,
				STEP_FILTERED_ORDERED);
		
		File filteredOrderedFile = new File(filteredOrderedFileName);
		
		try {
			// create parent dirs
			filteredOrderedFile.getParentFile().mkdirs();

			log.warn("Existing files will be overwritten! " + filteredOrderedFile);

			BufferedWriter writer = new BufferedWriter(new FileWriter(filteredOrderedFile));

			writeCvsFileHeader(writer, datasetDescriptor.getImageSetName(), -1,
					datasetDescriptor.getClassifications());

			int count = 0;
			String thumbnailId;
			String category;
			String thumbnailData;
			
			for (IntDoubleString intDoubleString : order) {

				thumbnailId = intDoubleString.getStringId();
				thumbnailData = categorizedMap.get(thumbnailId);
				category = thumbnailData.substring(thumbnailData.lastIndexOf(';') +1);
				
				if( filterOut.contains(category.trim())){
					log.debug("Filter Out:" + thumbnailData + " : " + category);
					continue;
				}
				
				// intDoubleString,
				// subsetMap.get(intDoubleString.getStringId()));
				//csvOrder = intDoubleString.toString().replaceAll(" ", ";");
				writer.write(thumbnailId);
				writer.write(";");
				writer.write(thumbnailData);
				writer.write(";");
				writer.write(intDoubleString.getDistance().toString());
				writer.write(";");
				writer.write("\n");
				count++;
				if (count % 1000 == 0)
					writer.flush();
			}
			writer.flush();
			writer.close();

		} catch (Exception e) {
			throw new TechnicalRuntimeException("cannot write cvs file");
		}

		return filteredOrderedFile;
	}

	protected SortedSet<IntDoubleString> generateOrder(String datasetName,
			final String subsetName, DatasetDescriptor datasetDescriptor,
			File thumbnailsCvsFile) throws FileNotFoundException, IOException,
			FeatureExtractionException {
		Map<String, String> subsetMap = readThumbnailsMap(thumbnailsCvsFile);

		SubsetAnalyserImpl<IFeaturesCollector> analyser = new SubsetAnalyserImpl<IFeaturesCollector>(
				datasetName, subsetName, new LireMetric(), false);
		analyser.init();

		analyser.extractSubsetFeatures(subsetMap.keySet());

		SortedSet<IntDoubleString> order = analyser.generateOrder();

		String orderedThumbnailsFile = getCvsFileForStep(datasetDescriptor,
				STEP_ORDERED);
		File outFile = new File(orderedThumbnailsFile);

		writeOrderedSubset(datasetDescriptor, subsetMap, order, outFile);

//		for (IntDouble intDouble : order) {
//			System.out.println(intDouble);
//		}

		return order;
	}

	// @Test
	public File categorizeThumbnails(DatasetDescriptor datasetDescriptor,
			File thumbnailsFile) throws FileNotFoundException, IOException {

		// String thumbnailsFile = getCvsFileForStep(datasetDescriptor,
		// STEP_THUMBNAILS);
		// new File(thumbnailsFile)
		String outFile = getCvsFileForStep(datasetDescriptor, STEP_CLASSIFIED);

		LargeThumbnailsetProcessing datasetCategorization = new LargeThumbnailsetProcessing(
				thumbnailsFile);
		// String imageFolder = getConfiguration().getImageFolder(getDataset());
		String imageFolder = IMAGE_FOLDER_NAME;

		GrayScaleSepiaDetector observer = new GrayScaleSepiaDetector(new File(
				imageFolder), 85, 3);
		final File outputFile = new File(outFile);
		observer.setOutputFile(outputFile);

		datasetCategorization.addObserver(observer);
		datasetCategorization.processThumbnailset(start, limit, blockSize);

		System.out.println("Skipped items: "
				+ datasetCategorization.getFailureCount());
		return outputFile;

	}

	private void writeOrderedSubset(DatasetDescriptor dataset,
			Map<String, String> subsetMap, SortedSet<IntDoubleString> order,
			File file) {
		try {
			// create parent dirs
			file.getParentFile().mkdirs();

			log.warn("Existing files will be overwritten! " + file);

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			writeCvsFileHeader(writer, dataset.getImageSetName(), order.size(),
					dataset.getClassifications());

			int count = 0;
			String csvOrder;

			for (IntDoubleString intDoubleString : order) {

				// intDoubleString,
				// subsetMap.get(intDoubleString.getStringId()));
				csvOrder = intDoubleString.toString().replaceAll(" ", ";");
				writer.write(csvOrder);
				writer.write(";");
				writer.write("\n");
				count++;
				if (count % 1000 == 0)
					writer.flush();
			}
			writer.flush();
			writer.close();

		} catch (Exception e) {
			throw new TechnicalRuntimeException("cannot write cvs file");
		}

	}

	protected void writeThumbnailsToCsvFile(DatasetDescriptor dataset,
			Map<String, String> thumbnails, File file, int fileWritePolicy)
			throws IOException {

	}

}
