package eu.europeana.creative.dataset.culturecam.v1;

import static org.junit.Assert.assertEquals;
import it.cnr.isti.vir.features.IFeaturesCollector;
import it.cnr.isti.vir.similarity.knn.IntDoubleString;
import it.cnr.isti.vir.similarity.metric.LireMetric;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.api.client.exception.TechnicalRuntimeException;
import eu.europeana.api.client.thumbnails.ThumbnailAccessorUtils;
import eu.europeana.creative.dataset.culturecam.v1.analysis.DatasetAnalyzer;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.api.PivotManagementService;
import eu.europeana.service.ir.image.api.PivotManagementServiceImpl;

public class CultureCamV1PivotsTest extends ThumbnailAccessorUtils {

//	PivotManagementService pivotManager;

	@Before
	public void init() {
		String dataset = "culturecam";
		setDataset(dataset);
		blockSize = 1000;
//		pivotManager = new PivotManagementServiceImpl(getDataset());
//		pivotManager.init();
	}

	//1@Test
	public void generatePivotCvsFile() throws Exception {
		
		IRConfiguration irConfig = new IRConfigurationImpl();
		File thumbnailsCvsFile = irConfig.getDatasetFile(getDataset());
		Map<String, String> datasetMap = readThumbnailsMap(thumbnailsCvsFile);

		DatasetAnalyzer<IFeaturesCollector> analyser = new DatasetAnalyzer<IFeaturesCollector>(
				getDataset(), new LireMetric(), false);
		analyser.init();

		analyser.extractDatasetFeatures(datasetMap.keySet());

		SortedSet<IntDoubleString> order = analyser.generateOrderWithInterDist();

		String pivotsCvsFile = irConfig.getPivotsCsvFile(getDataset());
		String pivots300  = pivotsCvsFile.replace(".csv", "_300.csv");
		File outFile = new File(pivots300);

		writeOrderedSubset(order, outFile, 300);
		Map<String, String> candidates = readThumbnailsMap(outFile);

		SortedSet<IntDoubleString> orderedCandidates = analyser.generateOrder(candidates.keySet());
		String reordered  = pivotsCvsFile.replace(".csv", "_reordered.csv");
		writeOrderedSubset(orderedCandidates, new File(reordered), 300);
	}
	
	@Test
	public void extractPivotFeatures() throws Exception {
			PivotManagementService pivotManager = new PivotManagementServiceImpl(getDataset());
			pivotManager.init();
			
			File pivotCsvFile = new File(pivotManager.getConfiguration()
					.getPivotsCsvFile(getDataset()));
			Map<String, String> pivotThumbnails = readThumbnailsMap(pivotCsvFile);
			pivotManager.extractPivotFeatures(pivotThumbnails.keySet());
			int pivots = ((PivotManagementServiceImpl) pivotManager)
					.getPivotsFCArchive().size();
			assertEquals(pivotThumbnails.size(), pivots);
			System.out.println("successfully extracted features for pivots: "
					+ pivots);

			pivotManager.generateLireObjectPivots();
			
			
			List<IFeaturesCollector> features = ((PivotManagementServiceImpl) pivotManager)
					.getPivotsFCArchive().getAll();
			assertEquals(pivotThumbnails.size(), features.size());

			
			// copyPivotsFeaturesArchive();
		}
	
	
	
	private void writeOrderedSubset(SortedSet<IntDoubleString> order,
			File file, int TopN) {
		try {
			// create parent dirs
			file.getParentFile().mkdirs();

			log.warn("Existing files will be overwritten! " + file);

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

//			writeCvsFileHeader(writer, dataset.getImageSetName(), order.size(),
//					dataset.getClassifications());

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
				//only TopN
				if(count == TopN)
					break;
			}
			writer.flush();
			writer.close();

		} catch (Exception e) {
			throw new TechnicalRuntimeException("cannot write cvs file");
		}

	}

//	@Test
//	public void generateLireObjectPivotsArchive() throws Exception {
//		// test copy FeatureArchives
//		File inFile = new File(pivotManager.getConfiguration()
//				.getPivotsFCArchive(getDataset()));
//		assertTrue(inFile.exists());
//
//		pivotManager.generateLireObjectPivots();
//
//		checkTopNPivots();
//
//	}
	
//	@Test
//	public void generateLireObjectPivotsArchiveWithOrder() throws Exception {
//		// test copy FeatureArchives
//		File inFile = new File(pivotManager.getConfiguration()
//				.getPivotsFCArchive(getDataset()));
//		assertTrue(inFile.exists());
//		Integer[] order = new Integer[] { 4815, 298, 9392, 7858, 5686, 9945, 2693, 6742, 1761, 4555, 
//		                              	  3642, 271, 1047, 6489, 2133, 6272, 9729, 6381, 2608, 7968, 
//		                            	  57, 279, 8513, 2314, 54, 61, 1125, 1906, 809, 10309, 317, 
//		                            	  2515, 5278, 321, 4152, 9887, 4554, 8341, 3143, 2869, 9997, 
//		                            	  3138, 1494, 7, 67, 363, 364, 1190, 4308, 10001, 10485, 
//		                            	  299, 9817, 9264, 9687, 8514, 5430, 5512, 7652, 5894, 8044,
//		                            	  7436, 11243, 10813, 9821, 6630, 1026, 285, 6279, 2727, 1290, 
//		                            	  5509, 34, 280, 1673, 17, 346, 56, 3410, 1776, 274, 1517, 7417, 
//		                            	  7311, 310, 270, 286, 91, 4352, 5522, 11203, 8447, 10209, 3232, 
//		                            	  886, 3209, 8915, 8540, 344, 361, 277, 25, 3897, 77, 10994, 11249};
//
//		pivotManager.generateLireObjectPivots(order);
//
//		checkTopNPivots();
//
//	}

	// @Test
//	public void checkTopNPivots() throws FileNotFoundException, IOException {
//		File outFile = ((PivotManagementServiceImpl) pivotManager)
//				.getLireObjectPivotsFile();
//
//		// readTop5 pivots
//		int topN = 5;
//		LireObject[] pivots = readTopNPivots(outFile, topN);
//		for (int i = 0; i < pivots.length; i++) {
//			LireObject lireObject = pivots[i];
//			assertNotNull(lireObject);
//			System.out.println(lireObject);
//			System.out.println(lireObject.getFeatures());
//		}
//	}

//	protected LireObject[] readTopNPivots(File outFile, int topN)
//			throws FileNotFoundException, IOException {
//		DataInputStream in = null;
//		in = new DataInputStream(new BufferedInputStream(new FileInputStream(
//				outFile)));
//		// ObjectInputStream ros_file=new ObjectInputStream(new
//		// FileInputStream(filename));
//		LireObject[] pivots = new LireObject[5];
//
//		for (int i = 0; i < topN; i++) {
//			pivots[i] = new LireObject(in);
//		}
//		in.close();
//
//		return pivots;
//	}

//	@Test
//	public void generatePivotOrder() throws SecurityException,
//			IllegalArgumentException, IOException, NoSuchMethodException,
//			InstantiationException, IllegalAccessException,
//			InvocationTargetException {
//		// public MultipleKNNPQueueID( Collection queryColl,
//		// Integer k,
//		// Metric comp,
//		// Class pQueueClass
//		// ) {
//		//
//		// this (queryColl, k, comp, false, null, null, true, false,
//		// pQueueClass, true );
//		// }
//		// TODO: test also with lire pivots archive
//
//		File inFile = new File(pivotManager.getConfiguration()
//				.getPivotsFCArchive(getDataset()));
//		List<IFeaturesCollector> featuresCollection = FeaturesCollectorsArchive
//				.getAll(inFile);
//		// FeaturesCollectorsArchive.
//
//		final int k = 100;
//		final int tries = 30;
//		MultipleKNNPQueueID multipleKnnQueue = new MultipleKNNPQueueID(
//				featuresCollection, // Collection queryColl,
//				k, // Integer k,
//				new LireMetric(), // Metric comp,
//				true, // boolean useInterDistances,
//				new QueriesOrder3(tries, k), // (available Queriesorder1-3)
//												// IQueriesOrdering ordering,
//				-1, // (not used) - Integer nRecents,
//				false, // (distance overflow) boolean distET,
//				false, // (search by ID or features?) boolean storeID,
//				SimPQueue_kNN.class, // (same as SimPQueueDMax) Class
//										// pQueueClass,
//				false /* boolean silent */);
//
//		System.out.println(multipleKnnQueue.getAvgIntDist());
//
//		// System.out.println(multipleKnnQueue.getAvgLastDist());
//		ISimilarityResults[] results = multipleKnnQueue.getResults();
//		// multipleKnnQueue.writeResultsIDs();
//
//		for (int i = 0; i < results.length; i++) {
//			System.out.println("ISimilarityResult [" + i + "]: " + results[i]);
//		}
//
//		// System.out.println("Top k results : " + multipleKnnQueue.get(k));
//		// for (int i = 0; i < k; i++) {
//		// System.out.println("Top k results : " + multipleKnnQueue.get(k));
//		// }
//
//		// multipleKnnQueue.get(index);
//
//	}

}
