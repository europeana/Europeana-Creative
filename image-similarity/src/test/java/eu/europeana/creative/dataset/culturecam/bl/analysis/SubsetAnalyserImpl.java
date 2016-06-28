package eu.europeana.creative.dataset.culturecam.bl.analysis;

import it.cnr.isti.feature.extraction.FeatureExtractionException;
import it.cnr.isti.feature.extraction.Image2Features;
import it.cnr.isti.vir.features.FeaturesCollectorArr;
import it.cnr.isti.vir.features.IFeaturesCollector;
import it.cnr.isti.vir.file.FeaturesCollectorsArchive;
import it.cnr.isti.vir.id.IDString;
import it.cnr.isti.vir.id.IHasID;
import it.cnr.isti.vir.readers.CoPhIRv2Reader;
import it.cnr.isti.vir.similarity.knn.IntDoubleString;
import it.cnr.isti.vir.similarity.metric.LireMetric;
import it.cnr.isti.vir.similarity.metric.Metric;
import it.cnr.isti.vir.util.Pivots;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.exceptions.TechnicalRuntimeException;

public class SubsetAnalyserImpl<F> {

	private Logger log = Logger.getLogger(getClass());
	private IRConfiguration configuration;
	private String dataset = null;
	private String subset = null;

	private Image2Features img2ftx;
	private File subsetFCArchiveFile;
	boolean resetFeaturesArchive = false;
	FeaturesCollectorsArchive subsetFCArchive;
	List<IFeaturesCollector> subsetFeatures;

	// private IFeaturesCollector[] qObj;

	private final Metric<F> comp;

	// private File lireObjectPivotsFile;
	// FeaturesCollectorsArchive lireObjectPivotsArchive;

	// SubsetAnalyserImpl() {
	// this(null, null);
	// }

	// SubsetAnalyserImpl(IRConfiguration configuration, String subset) {
	// this(configuration, null, );
	// }

	public SubsetAnalyserImpl(String dataset, String subset, Metric<F> comp,
			boolean resetFeaturesArchive) {
		this(null, dataset, subset, comp, resetFeaturesArchive);
	}

	public SubsetAnalyserImpl(IRConfiguration configuration, String dataset,
			String subset, Metric<F> comp, boolean resetFeaturesArchive) {
		this.configuration = configuration;
		this.dataset = dataset;
		this.subset = subset;
		this.comp = comp;
		this.resetFeaturesArchive = resetFeaturesArchive;
	}

	// @Override
	public IRConfiguration getConfiguration() {
		if (configuration == null)
			configuration = new IRConfigurationImpl();
		return configuration;
	}

	// @Override
	public void init() {
		// ensure initialization of configuration attribute
		getConfiguration();
		// init index searcher bean
		// File indexFolder = getConfiguration().getIndexFolder(
		// getDataset());
		// File indexConfFolder = getConfiguration().getIndexConfFolder(
		// getDataset());
		// init feature extraction bean
		try {
			if (img2ftx == null)
				img2ftx = new Image2Features(getConfiguration()
						.getIndexConfFolder(getDataset()));
		} catch (Exception e) {
			throw new TechnicalRuntimeException(
					"Cannot instantiate feature extractor!", e);
			// log.warn("Cannot instantiate feature extractor!", e);
		}

	}

	protected void initSubsetFCArchive() {
		initSubsetFCArchive(resetFeaturesArchive);
	}

	protected void initSubsetFCArchive(boolean resetFile) {

		// create file path if needed
		if (!getSubsetFCArchiveFile().exists())
			getSubsetFCArchiveFile().getParentFile().mkdirs();
		else if (resetFile)
			getSubsetFCArchiveFile().delete();

		try {
			subsetFCArchive = new FeaturesCollectorsArchive(
					getSubsetFCArchiveFile(),
					new LireMetric().getRequestedFeaturesClasses(),
					IDString.class, FeaturesCollectorArr.class);
		} catch (Exception e) {
			throw new TechnicalRuntimeException(
					"Cannot instantiate (pivots) feature collection archive!",
					e);
		}
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public File getSubsetFCArchiveFile() {
		if (subsetFCArchiveFile == null)
			subsetFCArchiveFile = new File(getConfiguration()
					.getSubsetFCArchive(getDataset(), getSubset()));
		return subsetFCArchiveFile;
	}

	public FeaturesCollectorsArchive getSubsetFCArchive() {
		return subsetFCArchive;
	}

	// @Override
	public void extractSubsetFeatures(Set<String> subsetThumbnailIds)
			throws FeatureExtractionException {
		// init file
		initSubsetFCArchive();

		// TODO: move resetFeaturesArchive from constructor to this method
		// skip feature extraction
		if (!resetFeaturesArchive && getSubsetFCArchiveFile().exists())
			return;

		File thumbnailFile = null;
		int cnt = 0;
		try {
			for (String thumbnailId : subsetThumbnailIds) {
				log.debug("extracting features for pivot with ID: "
						+ thumbnailId);
				thumbnailFile = getConfiguration().getImageFile(getDataset(),
						thumbnailId);
				
				final int PLACEHOLDER_SIZE = 3583;
				if(thumbnailFile.length() == PLACEHOLDER_SIZE){
					log.debug("Skip placeholder thumbnail: " + thumbnailFile.getAbsolutePath());
					continue;
				}
				
				storeImageFeatures(thumbnailId, new FileInputStream(
						thumbnailFile));
				cnt++;
				if (cnt % 1000 == 0)
					log.debug("Features extracted for #pivots: " + cnt);
			}
			// write index files an close
			getSubsetFCArchive().close();
		} catch (Exception e) {
			throw new FeatureExtractionException(
					"Cannot write pivot Features Archives!", e);
		}

	}

	public SortedSet<IntDoubleString> generateOrder() {

		double[][] interDist = evalInterDistances();
		TreeSet<IntDoubleString> res = new TreeSet<IntDoubleString>(
				new Comparator<IntDoubleString>() {

					@Override
					public int compare(IntDoubleString o1, IntDoubleString o2) {
						return o2.compareTo(o1);
					}

				});

		log.debug("Avg inter-dist before ordering: "
				+ Pivots.getTrMatrixAvg(interDist));
		log.debug("Avg inter-dist before ordering(50): "
				+ Pivots.getTrMatrixAvg(interDist,
						Math.min(interDist.length, 50)));

		double sum;
		String stringId;
		
		for (int i = 0; i < interDist.length; i++) {
			sum = 0;
			for (int j = 0; j < interDist[i].length; j++) {
				sum += Math.abs(interDist[i][j]);
			}
			stringId = ((IHasID)getSubsetFeatures().get(i)).getID().toString();
			res.add(new IntDoubleString(i, sum, stringId));
		}

		return res;
	}

	protected final double[][] evalInterDistances() {

		getSubsetFeatures();

		double temp[][] = new double[subsetFeatures.size()][subsetFeatures
				.size()];
		// for ( int i=0; i<temp.length; i++ ) {
		// temp[i] = new double[i];
		// }
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[i].length; j++) {
				temp[i][j] = comp.distance(subsetFeatures.get(i),
						subsetFeatures.get(j));
			}
		}
		return temp;
	}

	protected List<IFeaturesCollector> getSubsetFeatures() {
		if (subsetFeatures == null) {

			try {
				subsetFeatures = getSubsetFCArchive().getAll();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new TechnicalRuntimeException(
						"Cannot get features from subset archive", e);
			}
		}
		return subsetFeatures;
	}

	protected void storeImageFeatures(String thumbnailId, InputStream imageObj)
			throws FeatureExtractionException {

		String imgFeatures;

		imgFeatures = img2ftx.extractFeatures(imageObj);
		storeImageFeatures(thumbnailId, imgFeatures);

	}

	protected void storeImageFeatures(String docID, String imgFeatures)
			throws FeatureExtractionException {

		BufferedReader br = null;
		try {
			InputStream is = new ByteArrayInputStream(imgFeatures.getBytes());
			// read it with BufferedReader
			br = new BufferedReader(new InputStreamReader(is));
			FeaturesCollectorArr features = CoPhIRv2Reader.getObj(br);
			// System.out.println("writting");
			// LireObject object = new LireObject(features);
			features.setID(new IDString(docID));
			getSubsetFCArchive().add(features);

		} catch (Exception e) {
			throw new FeatureExtractionException(
					"Cannot store pivot features: " + docID, e);
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					// this exception should not occur
					// if it occurs nothing harmful should occur
					log.warn("warning: exception occured when closing buffered reader of image features for image "
									+ docID
									+ "\nError message"
									+ e.getLocalizedMessage());
				}
		}

	}

	// public void generateSubsetOrder() throws SecurityException,
	// IllegalArgumentException, IOException, NoSuchMethodException,
	// InstantiationException, IllegalAccessException,
	// InvocationTargetException {
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

	// File inFile = new File(pivotManager.getConfiguration()
	// .getPivotsFCArchive(getDataset()));
	// List<IFeaturesCollector> featuresCollection = FeaturesCollectorsArchive
	// .getAll(inFile);

	// List<IFeaturesCollector> featuresCollection =
	// getSubsetFCArchive().getAll();
	// // FeaturesCollectorsArchive.
	//
	// final int k = 100;
	// final int tries = 30;
	// MultipleKNNPQueueID multipleKnnQueue = new MultipleKNNPQueueID(
	// featuresCollection, // Collection queryColl,
	// k, // Integer k,
	// new LireMetric(), // Metric comp,
	// true, // boolean useInterDistances,
	// new QueriesOrder3(tries, k), // (available Queriesorder1-3)
	// // IQueriesOrdering ordering,
	// -1, // (not used) - Integer nRecents,
	// false, // (distance overflow) boolean distET,
	// false, // (search by ID or features?) boolean storeID,
	// SimPQueue_kNN.class, // (same as SimPQueueDMax) Class
	// // pQueueClass,
	// false /* boolean silent */);
	//
	// System.out.println(multipleKnnQueue.getAvgIntDist());
	//
	// // System.out.println(multipleKnnQueue.getAvgLastDist());
	// ISimilarityResults[] results = multipleKnnQueue.getResults();
	// // multipleKnnQueue.writeResultsIDs();
	//
	// for (int i = 0; i < results.length; i++) {
	// System.out.println("ISimilarityResult [" + i + "]: " + results[i]);
	// }
	//
	// // System.out.println("Top k results : " + multipleKnnQueue.get(k));
	// // for (int i = 0; i < k; i++) {
	// // System.out.println("Top k results : " + multipleKnnQueue.get(k));
	// // }
	//
	// // multipleKnnQueue.get(index);
	//
	// }

	// @Override
	// public void extractPivotFeatures(Map<String, String> pivotThumbnails)
	// throws FileNotFoundException, FeatureExtractionException {
	// // TODO update implementation
	// extractPivotFeatures(pivotThumbnails.keySet());
	//
	// }

	// @Override
	// public void generateLireObjectPivots(Integer[] order)
	// throws FileNotFoundException, FeatureExtractionException {
	//
	// File pivotsFile = initLireObjectPivotFile(true);
	// // initPivotsFCArchive(false);
	//
	// DataOutputStream out = null;
	//
	// try {
	// List<IFeaturesCollector> pivotFeatures = FeaturesCollectorsArchive
	// .getAll(getSubsetFCArchiveFile());
	// // getPivotsFCArchive().getAll();
	// if (order != null && pivotFeatures.size() < order.length)
	// throw new ArrayIndexOutOfBoundsException(
	// "The feature collector list was expected to have more than "
	// + order.length + " elements, but only found: "
	// + pivotFeatures.size());
	//
	// LireObject pivot;
	// // IFeaturesCollector pivot;
	// int positionAsId = 0;
	//
	// out = new DataOutputStream(new BufferedOutputStream(
	// new FileOutputStream(pivotsFile)));
	//
	// Object[] features = pivotFeatures.toArray().clone();
	// //int indexingPivots =
	// int pivotsCount = features.length;
	//
	// if (order != null){
	// Reordering.reorder(Arrays.asList(order), features);
	// pivotsCount = order.length;
	// }
	//
	// for (int i = 0; i < pivotsCount; i++) {
	// positionAsId = i + 1;
	// pivot = new LireObject(positionAsId, (IFeaturesCollector) features[i]);
	// pivot.writeData(out);
	// }
	//
	// out.flush();
	// // lireObjectPivotsArchive.close();
	//
	// } catch (Exception e) {
	// throw new FeatureExtractionException(
	// "cannot generate lire pivots file from feature collection archive!",
	// e);
	// } finally {
	// try {
	// if (out != null)
	// out.close();
	// } catch (Exception e) {
	// log.warn("Cannot close out Stream for file: " + pivotsFile, e);
	// }
	// }
	// }

	// public File getLireObjectPivotsFile() {
	// if (lireObjectPivotsFile == null)
	// lireObjectPivotsFile = new File(getConfiguration().getPivotsFolder(
	// getDataset()), "LireObjectPivots.dat");
	//
	// return lireObjectPivotsFile;
	// }

	// @Override
	// public void generateLireObjectPivots() throws FileNotFoundException,
	// FeatureExtractionException {
	// generateLireObjectPivots(null);
	// }

	protected String getSubset() {
		return subset;
	}

	protected void setSubset(String subset) {
		this.subset = subset;
	}

	// protected FeaturesCollectorsArchive getLireobjectPivotsArchive() {
	// return lireObjectPivotsArchive;
	// }
}
