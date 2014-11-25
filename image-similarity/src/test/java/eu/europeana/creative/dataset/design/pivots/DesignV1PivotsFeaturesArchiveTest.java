package eu.europeana.creative.dataset.design.pivots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.cnr.isti.vir.features.IFeaturesCollector;
import it.cnr.isti.vir.features.mpeg7.LireObject;
import it.cnr.isti.vir.file.FeaturesCollectorsArchive;
import it.cnr.isti.vir.similarity.ISimilarityResults;
import it.cnr.isti.vir.similarity.knn.MultipleKNNPQueueID;
import it.cnr.isti.vir.similarity.knn.QueriesOrder3;
import it.cnr.isti.vir.similarity.metric.LireMetric;
import it.cnr.isti.vir.similarity.pqueues.SimPQueue_kNN;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.api.client.thumbnails.ThumbnailAccessorUtils;
import eu.europeana.service.ir.image.api.PivotManagementService;
import eu.europeana.service.ir.image.api.PivotManagementServiceImpl;

public class DesignV1PivotsFeaturesArchiveTest extends ThumbnailAccessorUtils {

	PivotManagementService pivotManager;

	@Before
	public void init() {
		String dataset = "design_v1";
		setDataset(dataset);
		blockSize = 1000;
		pivotManager = new PivotManagementServiceImpl(getDataset());
		pivotManager.init();
	}

	// @Test
	public void extractPivotFeatures() throws Exception {
		File pivotCsvFile = new File(pivotManager.getConfiguration()
				.getPivotsCsvFile(getDataset()));
		Map<String, String> pivotThumbnails = readThumbnailsMap(pivotCsvFile);
		pivotManager.extractPivotFeatures(pivotThumbnails.keySet());
		int pivots = ((PivotManagementServiceImpl) pivotManager)
				.getPivotsFCArchive().size();
		assertEquals(pivotThumbnails.size(), pivots);
		System.out.println("successfully extracted features for pivots: "
				+ pivots);

		List<IFeaturesCollector> features = ((PivotManagementServiceImpl) pivotManager)
				.getPivotsFCArchive().getAll();
		assertEquals(pivotThumbnails.size(), features.size());

		// copyPivotsFeaturesArchive();
	}

	// @Test
	public void generateLireObjectPivotsArchive() throws Exception {
		// test copy FeatureArchives
		File inFile = new File(pivotManager.getConfiguration()
				.getPivotsFCArchive(getDataset()));
		assertTrue(inFile.exists());

		pivotManager.generateLireObjectPivots();

		checkTopNPivots();

	}
	
	@Test
	public void generateLireObjectPivotsArchiveWithOrder() throws Exception {
		// test copy FeatureArchives
		File inFile = new File(pivotManager.getConfiguration()
				.getPivotsFCArchive(getDataset()));
		assertTrue(inFile.exists());
		Integer[] order = new Integer[] { 8, 46, 19, 4, 32, 42, 38, 33, 5, 28,
				29, 14, 34, 39, 30, 0, 47, 41, 11, 18, 48, 20, 15, 44, 27, 7,
				35, 9, 10, 40, 49, 2, 26, 31, 25, 12, 3, 24, 6, 16, 13, 23, 1,
				43, 36, 37, 45, 21, 17, 61, 56, 62, 53, 66, 70, 51, 64, 58, 60,
				54, 67, 63, 69, 52, 59, 55, 57, 50, 68, 22, 71, 65 };

		pivotManager.generateLireObjectPivots(order);

		checkTopNPivots();

	}

	// @Test
	public void checkTopNPivots() throws FileNotFoundException, IOException {
		File outFile = ((PivotManagementServiceImpl) pivotManager)
				.getLireObjectPivotsFile();

		// readTop5 pivots
		int topN = 5;
		LireObject[] pivots = readTopNPivots(outFile, topN);
		for (int i = 0; i < pivots.length; i++) {
			LireObject lireObject = pivots[i];
			assertNotNull(lireObject);
			System.out.println(lireObject);
			System.out.println(lireObject.getFeatures());
		}
	}

	protected LireObject[] readTopNPivots(File outFile, int topN)
			throws FileNotFoundException, IOException {
		DataInputStream in = null;
		in = new DataInputStream(new BufferedInputStream(new FileInputStream(
				outFile)));
		// ObjectInputStream ros_file=new ObjectInputStream(new
		// FileInputStream(filename));
		LireObject[] pivots = new LireObject[5];

		for (int i = 0; i < topN; i++) {
			pivots[i] = new LireObject(in);
		}
		in.close();

		return pivots;
	}

	@Test
	public void generatePivotOrder() throws SecurityException,
			IllegalArgumentException, IOException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		// public MultipleKNNPQueueID( Collection queryColl,
		// Integer k,
		// Metric comp,
		// Class pQueueClass
		// ) {
		//
		// this (queryColl, k, comp, false, null, null, true, false,
		// pQueueClass, true );
		// }
		// TODO: test also with lire pivots archive

		File inFile = new File(pivotManager.getConfiguration()
				.getPivotsFCArchive(getDataset()));
		List<IFeaturesCollector> featuresCollection = FeaturesCollectorsArchive
				.getAll(inFile);
		// FeaturesCollectorsArchive.

		final int k = 50;
		final int tries = 40;
		MultipleKNNPQueueID multipleKnnQueue = new MultipleKNNPQueueID(
				featuresCollection, // Collection queryColl,
				k, // Integer k,
				new LireMetric(), // Metric comp,
				true, // boolean useInterDistances,
				new QueriesOrder3(tries, k), // (available Queriesorder1-3)
												// IQueriesOrdering ordering,
				-1, // (not used) - Integer nRecents,
				false, // (distance overflow) boolean distET,
				false, // (search by ID or features?) boolean storeID,
				SimPQueue_kNN.class, // (same as SimPQueueDMax) Class
										// pQueueClass,
				false /* boolean silent */);

		System.out.println(multipleKnnQueue.getAvgIntDist());

		// System.out.println(multipleKnnQueue.getAvgLastDist());
		ISimilarityResults[] results = multipleKnnQueue.getResults();
		// multipleKnnQueue.writeResultsIDs();

		for (int i = 0; i < results.length; i++) {
			System.out.println("ISimilarityResult [" + i + "]: " + results[i]);
		}

		// System.out.println("Top k results : " + multipleKnnQueue.get(k));
		// for (int i = 0; i < k; i++) {
		// System.out.println("Top k results : " + multipleKnnQueue.get(k));
		// }

		// multipleKnnQueue.get(index);

	}

}
