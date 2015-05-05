package eu.europeana.creative.dataset.culturecam.v1.analysis;

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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.exceptions.TechnicalRuntimeException;

public class DatasetAnalyzer<F> {

	private final class DistanceIdComparator implements
			Comparator<IntDoubleString> {
		@Override
		public int compare(IntDoubleString o1, IntDoubleString o2) {
			return o2.compareTo(o1);
		}
	}

	private Logger log = Logger.getLogger(getClass());
	private IRConfiguration configuration;
	private String dataset = null;
	
	private Image2Features img2ftx;
	private File datasetFCArchiveFile;
	boolean resetFeaturesArchive = false;
	FeaturesCollectorsArchive datasetFCArchive;
	List<IFeaturesCollector> datasetFeatures;

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

	public DatasetAnalyzer(String dataset, Metric<F> comp,
			boolean resetFeaturesArchive) {
		this(null, dataset, comp, resetFeaturesArchive);
	}

	public DatasetAnalyzer(IRConfiguration configuration, String dataset,
			Metric<F> comp, boolean resetFeaturesArchive) {
		this.configuration = configuration;
		this.dataset = dataset;
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

	protected void initDatasetFCArchive() {
		initDatasetFCArchive(resetFeaturesArchive);
	}

	protected void initDatasetFCArchive(boolean resetFile) {

		// create file path if needed
		if (!getDatasetFCArchiveFile().exists())
			getDatasetFCArchiveFile().getParentFile().mkdirs();
		else if (resetFile)
			getDatasetFCArchiveFile().delete();

		try {
			datasetFCArchive = new FeaturesCollectorsArchive(
					getDatasetFCArchiveFile(),
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

	public File getDatasetFCArchiveFile() {
		if (datasetFCArchiveFile == null)
			datasetFCArchiveFile = getConfiguration()
					.getFeaturesArchiveFile(getDataset());
		return datasetFCArchiveFile;
	}

	public FeaturesCollectorsArchive getDatasetFCArchive() {
		return datasetFCArchive;
	}

	// @Override
	public void extractDatasetFeatures(Set<String> ThumbnailIds)
			throws FeatureExtractionException {
		// init file
		initDatasetFCArchive();

		// TODO: move resetFeaturesArchive from constructor to this method
		//todo 
		if (!resetFeaturesArchive && getDatasetFCArchiveFile().exists() && getDatasetFCArchiveFile().length() > 50)
			return;// skip feature extraction
		
		File thumbnailFile = null;
		int cnt = 0;
		try {
			for (String thumbnailId : ThumbnailIds) {
				log.debug("extracting features for item with ID: "
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
			getDatasetFCArchive().close();
		} catch (Exception e) {
			throw new FeatureExtractionException(
					"Cannot write pivot Features Archives!", e);
		}

	}
		
	public SortedSet<IntDoubleString> generateOrder(Set<String> thumbnailIds) {

		List<String> thumbnails = new ArrayList<String>();
		thumbnails.addAll(thumbnailIds);
		
		double[][] interDist = evalInterDistances(thumbnails);
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
			stringId = thumbnails.get(i);
			res.add(new IntDoubleString(i, sum, stringId));
		}

		return res;
	}

	
	public SortedSet<IntDoubleString> generateOrderNoInterDist(){
//		if(useInterDist)
//			return 
		TreeSet<IntDoubleString> res = new TreeSet<IntDoubleString>(
				new DistanceIdComparator());
		String stringId;
		int i = 0;
		double avgDistance; 
		final double datasetSize = (double)getDatasetFeatures().size();
		
		for(IFeaturesCollector features : getDatasetFeatures()){
			stringId = ((FeaturesCollectorArr) features).getID().toString();
			avgDistance = evalDistSum(features)/datasetSize;
			
			res.add(new IntDoubleString(i, avgDistance, stringId));
			System.out.println("adding item to set: " + stringId + " dist: " + avgDistance);
			i++;
		}
		
		return res;
	}
	
	private double evalDistSum(IFeaturesCollector queryfeatures) {
		
		double sum = 0;
		double[] dist = evalDistances(queryfeatures);
		for (int i = 0; i < dist.length; i++) {
			sum+=dist[i];
		}
		
		return sum;
	}

	public SortedSet<IntDoubleString> generateOrderWithInterDist() {

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
			stringId = ((IHasID)getDatasetFeatures().get(i)).getID().toString();
			res.add(new IntDoubleString(i, sum, stringId));
		}

		return res;
	}

	protected final double[][] evalInterDistances(List<String> ids) {

		getDatasetFeatures();

		double temp[][] = new double[ids.size()][ids
				.size()];
		// for ( int i=0; i<temp.length; i++ ) {
		// temp[i] = new double[i];
		// }
		String idi;
		String idj;
		
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[i].length; j++) {
				idi = ids.get(i);
				idj = ids.get(j);
				
				temp[i][j] = comp.distance(getFeaturesForId(idi),
						getFeaturesForId(idj));
			}
		}
		return temp;
	}
	
	protected final double[] evalDistances(String id) {

		getDatasetFeatures();
		final IFeaturesCollector features = getFeaturesForId(id);
		return evalDistances(features);
	}

	protected double[] evalDistances(final IFeaturesCollector features) {
		double temp[] = new double[getDatasetFeatures().size()];
		
		for (int j = 0; j < temp.length; j++) {
				//idj = ids.get(j);
				temp[j] = comp.distance(features,
						getDatasetFeatures().get(j));
			
		}
		return temp;
	}
	
	protected IFeaturesCollector getFeaturesForId(String id){
		for (IFeaturesCollector features : getDatasetFeatures()) {
			if(id.equals(((IHasID) features).getID().toString()))
				return features;
		}
		
		throw new RuntimeException("Cannot find features colector for thumbnailId: " + id);
	}
	
	
	protected final double[][] evalInterDistances() {

		getDatasetFeatures();

		double temp[][] = new double[datasetFeatures.size()][datasetFeatures
				.size()];
		// for ( int i=0; i<temp.length; i++ ) {
		// temp[i] = new double[i];
		// }
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[i].length; j++) {
				temp[i][j] = comp.distance(datasetFeatures.get(i),
						datasetFeatures.get(j));
			}
			System.out.println("i=" + i);
		}
		return temp;
	}

	protected List<IFeaturesCollector> getDatasetFeatures() {
		if (datasetFeatures == null) {

			try {
				datasetFeatures = getDatasetFCArchive().getAll();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new TechnicalRuntimeException(
						"Cannot get features from subset archive", e);
			}
		}
		return datasetFeatures;
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
			getDatasetFCArchive().add(features);

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

	
}
