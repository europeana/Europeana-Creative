package eu.europeana.service.ir.image.api;

import it.cnr.isti.feature.extraction.FeatureExtractionException;
import it.cnr.isti.feature.extraction.Image2Features;
import it.cnr.isti.vir.features.FeaturesCollectorArr;
import it.cnr.isti.vir.features.IFeaturesCollector;
import it.cnr.isti.vir.features.mpeg7.LireObject;
import it.cnr.isti.vir.file.FeaturesCollectorsArchive;
import it.cnr.isti.vir.id.IDString;
import it.cnr.isti.vir.readers.CoPhIRv2Reader;
import it.cnr.isti.vir.similarity.metric.LireMetric;
import it.cnr.isti.vir.util.Reordering;

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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.exceptions.TechnicalRuntimeException;

public class PivotManagementServiceImpl implements PivotManagementService {

	private Logger log = Logger.getLogger(getClass());
	private IRConfiguration configuration;
	private String dataset = null;
	private Image2Features img2ftx;
	private File pivotsFCArchiveFile;
	boolean cleanPivotsFCArchive = false;
	FeaturesCollectorsArchive pivotsFCArchive;
	private File lireObjectPivotsFile;
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

	protected void initPivotsFCArchive() {
		initPivotsFCArchive(cleanPivotsFCArchive);
	}

	protected void initPivotsFCArchive(boolean resetFile) {

		// create file path if needed
		if (!getPivotsFCArchiveFile().exists())
			getPivotsFCArchiveFile().getParentFile().mkdirs();
		else if (resetFile)
			getPivotsFCArchiveFile().delete();

		try {
			pivotsFCArchive = new FeaturesCollectorsArchive(
					getPivotsFCArchiveFile(),
					new LireMetric().getRequestedFeaturesClasses(),
					IDString.class, FeaturesCollectorArr.class);
		} catch (Exception e) {
			throw new TechnicalRuntimeException(
					"Cannot instantiate (pivots) feature collection archive!",
					e);
		}
	}

	protected File initLireObjectPivotFile(boolean resetFile) {

		// create file path if needed
		if (!getLireObjectPivotsFile().exists())
			getLireObjectPivotsFile().getParentFile().mkdirs();
		else if (resetFile)
			getLireObjectPivotsFile().delete();

		return getLireObjectPivotsFile();
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

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
		initPivotsFCArchive();

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
				if(cnt % 1000 == 0)
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
			throws FeatureExtractionException {

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

	@Override
	public void extractPivotFeatures(Map<String, String> pivotThumbnails)
			throws FileNotFoundException, FeatureExtractionException {
		// TODO update implementation
		extractPivotFeatures(pivotThumbnails.keySet());

	}

	@Override
	public void generateLireObjectPivots(Integer[] order)
			throws FileNotFoundException, FeatureExtractionException {

		File pivotsFile = initLireObjectPivotFile(true);
		// initPivotsFCArchive(false);

		DataOutputStream out = null;

		try {
			List<IFeaturesCollector> pivotFeatures = FeaturesCollectorsArchive
					.getAll(getPivotsFCArchiveFile());
			// getPivotsFCArchive().getAll();
			if (order != null && pivotFeatures.size() < order.length)
				throw new ArrayIndexOutOfBoundsException(
						"The feature collector list was expected to have more than "
								+ order.length + " elements, but only found: "
								+ pivotFeatures.size());

			LireObject pivot;
			// IFeaturesCollector pivot;
			int positionAsId = 0;

			out = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(pivotsFile)));

			Object[] features = pivotFeatures.toArray().clone();
			//int indexingPivots =
			int pivotsCount = features.length;
			
			if (order != null){
				Reordering.reorder(Arrays.asList(order), features);
				pivotsCount = order.length;
			}
			
			for (int i = 0; i < pivotsCount; i++) {
				positionAsId = i + 1;
				pivot = new LireObject(positionAsId, (IFeaturesCollector) features[i]);
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

	public File getLireObjectPivotsFile() {
		if (lireObjectPivotsFile == null)
			lireObjectPivotsFile = new File(getConfiguration().getPivotsFolder(
					getDataset()), "LireObjectPivots.dat");

		return lireObjectPivotsFile;
	}

	@Override
	public void generateLireObjectPivots() throws FileNotFoundException,
			FeatureExtractionException {
		generateLireObjectPivots(null);
	}

	// protected FeaturesCollectorsArchive getLireobjectPivotsArchive() {
	// return lireObjectPivotsArchive;
	// }
}
