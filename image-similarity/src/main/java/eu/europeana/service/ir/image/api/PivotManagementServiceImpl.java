package eu.europeana.service.ir.image.api;

import it.cnr.isti.feature.extraction.FeatureExtractionException;
import it.cnr.isti.feature.extraction.Image2Features;
import it.cnr.isti.melampo.vir.exceptions.VIRException;
import it.cnr.isti.vir.features.FeatureClassCollector;
import it.cnr.isti.vir.features.FeaturesCollectorArr;
import it.cnr.isti.vir.features.IFeaturesCollector;
import it.cnr.isti.vir.features.IFeaturesCollector_Labeled_HasID;
import it.cnr.isti.vir.features.mpeg7.LireObject;
import it.cnr.isti.vir.file.FeaturesCollectorsArchive;
import it.cnr.isti.vir.id.IDString;
import it.cnr.isti.vir.readers.CoPhIRv2Reader;
import it.cnr.isti.vir.similarity.metric.LireMetric;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.exceptions.TechnicalRuntimeException;

public class PivotManagementServiceImpl implements PivotManagementService {

	private Logger log = Logger.getLogger(getClass());
	private IRConfiguration configuration;
	private String dataset = null;
	private Image2Features img2ftx;
	// boolean cleanPivotsFCArchive = false;
	private File pivotsFCArchiveFile;
	FeaturesCollectorsArchive pivotsFCArchive;

	private File lireObjectPivotsTopNFile;

	// private File pivotFeaturesFCArchiveFile;
	// FeaturesCollectorsArchive lireObjectPivotsArchive;

	public PivotManagementServiceImpl() {
		this(null, null);
	}

	public PivotManagementServiceImpl(IRConfiguration configuration) {
		this(configuration, null);
	}

	public PivotManagementServiceImpl(String dataset) {
		this(null, dataset);
	}

	public PivotManagementServiceImpl(IRConfiguration configuration,
			String dataset) {
		this.configuration = configuration;
		this.dataset = dataset;
	}

	@Override
	public IRConfiguration getConfiguration() {
		if (configuration == null)
			configuration = new IRConfigurationImpl();
		return configuration;
	}

	@Override
	public void init() {
		// ensure initialization of configuration attribute
		getConfiguration();
		// init index searcher bean
		// File indexFolder = getConfiguration().getIndexFolder(
		// getDataset());
		// File indexConfFolder = getConfiguration().getIndexConfFolder(
		// getDataset());
		// init feature extraction bean

		initFeaturesExtractor();
	}

	protected void initFeaturesExtractor() {
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

	protected void initPivotsFCArchive(boolean resetFile) {

		// create file path if needed
		if (!getPivotsFCArchiveFile().exists())
			getPivotsFCArchiveFile().getParentFile().mkdirs();
		else if (resetFile)
			getPivotsFCArchiveFile().delete();

		try {
			pivotsFCArchive = new FeaturesCollectorsArchive(
					getPivotsFCArchiveFile(), getFeatureClassCollector(),
					IDString.class, FeaturesCollectorArr.class);
		} catch (Exception e) {
			throw new TechnicalRuntimeException(
					"Cannot instantiate (pivots) feature collection archive!",
					e);
		}
	}

	protected FeatureClassCollector getFeatureClassCollector() {
		return new LireMetric().getRequestedFeaturesClasses();
	}

	protected File initPivotFCArchiveFile(boolean resetFile) {

		// create file path if needed
		if (!getPivotsFCArchiveFile().exists())
			getPivotsFCArchiveFile().getParentFile().mkdirs();
		else if (resetFile)
			getPivotsFCArchiveFile().delete();

		return getPivotsFCArchiveFile();
	}

	protected File initLireObjectPivotsTopNFile(boolean resetFile, int topK) {

		// create file path if needed
		final File lireObjectPivotsFile = getLireObjectPivotsFile(topK);
		if (!lireObjectPivotsFile.exists())
			lireObjectPivotsFile.getParentFile().mkdirs();
		else if (resetFile)
			lireObjectPivotsFile.delete();

		return lireObjectPivotsFile;
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	@Override
	public File getPivotsFCArchiveFile() {
		if (pivotsFCArchiveFile == null)
			pivotsFCArchiveFile = new File(getConfiguration()
					.getPivotsFCArchive(getDataset()));
		return pivotsFCArchiveFile;
	}

	public FeaturesCollectorsArchive getPivotsFCArchive() {
		return pivotsFCArchive;
	}

	@Override
	public void extractPivotFeatures(Set<String> pivotThumbnailIds)
			throws FeatureExtractionException {
		// init file
		initPivotsFCArchive(true);

		File pivotThumbnailFile = null;
		int cnt = 0;
		try {
			for (String pivotId : pivotThumbnailIds) {
				log.debug("extracting features for pivot with ID: " + pivotId);
				pivotThumbnailFile = getConfiguration().getImageFile(
						getDataset(), pivotId);
				storePivotFeatures(pivotId, new FileInputStream(
						pivotThumbnailFile));
				cnt++;
				if (cnt % 1000 == 0)
					log.debug("Features extracted for #pivots: " + cnt);
			}
			// write index files an close
			getPivotsFCArchive().close();
		} catch (Exception e) {
			throw new FeatureExtractionException(
					"Cannot write pivot Features Archives!", e);
		}

	}

	protected void storePivotFeatures(String pivotID, InputStream imageObj)
			throws FeatureExtractionException, IOException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		initFeaturesExtractor();

		String imgFeatures;

		imgFeatures = img2ftx.extractFeatures(imageObj);
		storePivotFeatures(pivotID, imgFeatures);

	}

	protected void storePivotFeatures(String docID, String imgFeatures)
			throws FeatureExtractionException {

		BufferedReader br = null;
		try {
			InputStream is = new ByteArrayInputStream(imgFeatures.getBytes());
			// read it with BufferedReader
			br = new BufferedReader(new InputStreamReader(is));
			registerFeatureClassColector();
			FeaturesCollectorArr features = CoPhIRv2Reader.getObj(br);
			// System.out.println("writting");
			// LireObject object = new LireObject(features);
			features.setID(new IDString(docID));
			getPivotsFCArchive().add(features);

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
					System.out
							.println("warning: exception occured when closing buffered reader of image features for image "
									+ docID
									+ "\nError message"
									+ e.getLocalizedMessage());
				}
		}

	}

	protected void registerFeatureClassColector() {
		CoPhIRv2Reader.setFeatures(null);
	}

	@Override
	public void extractPivotFeatures(Map<String, String> pivotThumbnails)
			throws FileNotFoundException, FeatureExtractionException {
		// TODO update implementation
		extractPivotFeatures(pivotThumbnails.keySet());

	}

	@Override
	public void generateLirePivotsBinWithOrder(File orderCsvFile)
			throws IOException, FeatureExtractionException {
		
		if(getPivotsFCArchive() == null)
			initPivotsFCArchive(false);
		
		DataOutputStream out = null;
		File lirePivotsBinFile = null;
		
		try {
			//read position map and features collector list
			Map<String, Integer> pivotPositionMap = readPivotPositionsMap(orderCsvFile);
			List<IFeaturesCollector> allPivotFeatures = getPivotsFCArchive().getAll();
			//Object[] features = allPivotFeatures.toArray().clone();
			
			if (allPivotFeatures.size() < pivotPositionMap.size())
				throw new ArrayIndexOutOfBoundsException(
						"The feature collector list was expected to have more than "
								+ pivotPositionMap.size()
								+ " elements, but only found: "
						+ allPivotFeatures.size());
	
			//top N pivots file and output stream
			int topK = pivotPositionMap.size();
			lirePivotsBinFile = initLireObjectPivotsTopNFile(true, topK);
			out = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(lirePivotsBinFile)));
			
			LireObject pivot;
			// IFeaturesCollector pivot;
			int positionAsId = 0;
			IFeaturesCollector_Labeled_HasID pivotFeaturesCollector;
			
			
			for (Map.Entry<String, Integer> pivotPosition : pivotPositionMap.entrySet()) {
				//take the features at the position indicated by the order
				pivotFeaturesCollector = (IFeaturesCollector_Labeled_HasID) allPivotFeatures.get(pivotPosition.getValue());
				
				//check thumbnail id
				if(!pivotPosition.getKey().equals(pivotFeaturesCollector.getID().toString())){
						throw new IllegalArgumentException("pivotPosition doesn't match pivot feature archive! PivotPosition " + pivotPosition + "Feature Collector IID:" +  pivotFeaturesCollector.getID().toString());
				}
				
				//increment pivot numeric id, 
				pivot = new LireObject(++positionAsId, pivotFeaturesCollector);
				//write pivot to file
				pivot.writeData(out);
			}
		} catch (Exception e) {
			throw new FeatureExtractionException(
					"cannot generate lire pivots file from feature collection archive!",
					e);
		} finally {
			out.flush();
			
			try {
				if (out != null)
					out.close();
			} catch (Exception e) {
				log.warn("Cannot close out Stream for file: " + lirePivotsBinFile, e);
			}
		}
		
		
		

				
	}

	@Override
	public void generateLirePivotsBinWithOrder(Integer[] order)
			throws IOException, FeatureExtractionException {

		File featureArchiveFile = initPivotFCArchiveFile(false);

		int topK = order.length;

		// top N pivots file
		File lirePivotsBinFile = initLireObjectPivotsTopNFile(true, topK);

		DataOutputStream out = null;

		try {
			List<IFeaturesCollector> allPivotFeatures = FeaturesCollectorsArchive
					.getAll(featureArchiveFile);
			// getPivotsFCArchive().getAll();
			if (order != null && allPivotFeatures.size() < order.length)
				throw new ArrayIndexOutOfBoundsException(
						"The feature collector list was expected to have more than "
								+ order.length + " elements, but only found: "
								+ allPivotFeatures.size());

			LireObject pivot;
			// IFeaturesCollector pivot;
			int positionAsId = 0;

			out = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(lirePivotsBinFile)));

			Object[] features = allPivotFeatures.toArray().clone();
			// int indexingPivots =
			int pivotsCount = features.length;
			IFeaturesCollector curentPivot;

			for (int i = 0; i < order.length; i++) {
				positionAsId = i + 1;
				// take the features at the position indicated by the order
				curentPivot = (IFeaturesCollector) features[order[i]];
				pivot = new LireObject(positionAsId, curentPivot);
				// write pivot to file
				pivot.writeData(out);
			}

			// if (order != null){
			// Reordering.reorder(Arrays.asList(order), features);
			// pivotsCount = order.length;
			// }
			//
			out.flush();
			// lireObjectPivotsArchive.close();

		} catch (Exception e) {
			throw new FeatureExtractionException(
					"cannot generate lire pivots file from feature collection archive!",
					e);
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (Exception e) {
				log.warn("Cannot close out Stream for file: "
						+ lirePivotsBinFile, e);
			}
		}
	}

	@Override
	public void generateLireObjectPivotsBin(int topK,
			boolean forceFeatureExtraction) throws IOException,
			FeatureExtractionException {

		// might delete (reset) file
		File featureArchiveFile = initPivotFCArchiveFile(forceFeatureExtraction);
		// ensure feature extraction
		if (!featureArchiveFile.exists())
			extractPivotFeatures();

		// top N pivots file
		File pivotsFile = initLireObjectPivotsTopNFile(true, topK);

		DataOutputStream out = null;

		try {
			List<IFeaturesCollector> pivotFeatures = FeaturesCollectorsArchive
					.getAll(featureArchiveFile);
			// getPivotsFCArchive().getAll();
			// if (order != null && pivotFeatures.size() < order.length)
			// throw new ArrayIndexOutOfBoundsException(
			// "The feature collector list was expected to have more than "
			// + order.length + " elements, but only found: "
			// + pivotFeatures.size());

			LireObject pivot;
			// IFeaturesCollector pivot;
			int positionAsId = 0;

			out = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(pivotsFile)));

			Object[] features = pivotFeatures.toArray().clone();
			// int indexingPivots =
			int pivotsCount = features.length;

			// if (order != null){
			// Reordering.reorder(Arrays.asList(order), features);
			// pivotsCount = order.length;
			// }

			for (int i = 0; i < pivotsCount; i++) {
				positionAsId = i + 1;
				pivot = new LireObject(positionAsId,
						(IFeaturesCollector) features[i]);
				pivot.writeData(out);
			}

			out.flush();
			// lireObjectPivotsArchive.close();

		} catch (Exception e) {
			throw new FeatureExtractionException(
					"cannot generate lire pivots file from feature collection archive!",
					e);
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (Exception e) {
				log.warn("Cannot close out Stream for file: " + pivotsFile, e);
			}
		}
	}

	@Override
	public void extractPivotFeatures() throws IOException,
			FeatureExtractionException {
		Set<String> ids = readThumbnailIds();
		// extract
		extractPivotFeatures(ids);
	}

	Set<String> readThumbnailIds() throws IOException {
		List<String> items = FileUtils.readLines(getConfiguration()
				.getDatasetFile(getDataset()));
		Set<String> ids = new TreeSet<String>();
		for (String thumbnail : items) {
			// id is on the first position
			ids.add(thumbnail.split(";", 2)[0]);
		}
		// release memory
		items = null;

		return ids;
	}

	protected Map<String, Integer> readPivotPositionsMap(File pivotPositionMap)
			throws IOException {
		List<String> items = FileUtils.readLines(pivotPositionMap);
		Map<String, Integer> pivotPositionsMap = new LinkedHashMap<String, Integer>();
		String[] parts;
		Integer positionInCollection;
		// String thumbnailId;
		// Integer position;
		for (String pivotPosition : items) {
			parts = pivotPosition.split(";", 3);
			// id is on the first position
			// pivot position is on the second position
			positionInCollection = Integer.parseInt(parts[1]);
			pivotPositionsMap.put(parts[0], positionInCollection);
		}
		// release memory
		//items = null;

		return pivotPositionsMap;
	}

	public File getLireObjectPivotsFile(int topK) {
		if (lireObjectPivotsTopNFile == null)
			lireObjectPivotsTopNFile = new File(getConfiguration()
					.getPivotsFolder(getDataset()), "LireObjectPivots_" + topK
					+ ".dat");

		return lireObjectPivotsTopNFile;
	}

	public File getLireObjectPivotsFile() throws IOException, VIRException {
		return getLireObjectPivotsFile(getTopN());
	}

	@Override
	public int getTopN() throws IOException, VIRException {
		return getConfiguration().getLireSettings(
				getDataset()).getnPivots();
	}

	@Override
	public void generateLireObjectPivotsBin()
			throws FeatureExtractionException, IOException, VIRException {
		generateLireObjectPivotsBin(
				getTopN(),
				true);
	}

}
