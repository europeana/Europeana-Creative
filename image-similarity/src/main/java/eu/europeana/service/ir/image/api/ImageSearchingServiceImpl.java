package eu.europeana.service.ir.image.api;

import it.cnr.isti.exception.TechnicalRuntimeException;
import it.cnr.isti.feature.extraction.FeatureExtractionException;
import it.cnr.isti.feature.extraction.Image2Features;
import it.cnr.isti.melampo.index.searching.MelampoSearcherHub;
import it.cnr.isti.melampo.tools.thumbnail.locator.ImageLocator;
import it.cnr.isti.melampo.vir.exceptions.BoFException;
import it.cnr.isti.melampo.vir.exceptions.VIRException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.domain.QueryResults;
import eu.europeana.service.ir.image.exceptions.ImageSearchingException;
import eu.europeana.service.ir.image.web.model.json.ImageSearchResultItem;
import eu.europeana.service.ir.image.web.model.json.SearchResultItem;

/**
 * @author Paolo Bolettieri <paolo.bolettieri@isti.cnr.it>
 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
 */
public class ImageSearchingServiceImpl implements ImageSearchingService {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	private IRConfiguration configuration;

	private String dataset = null;

	private MelampoSearcherHub index;

	private QueryResults queryResults = new QueryResults();

	private static final int NUM_RESULTS = 100;

	private Image2Features img2ftx;

	private ImageLocator imageLocator;

	public ImageSearchingServiceImpl(IRConfiguration configuration) {
		this(configuration.getDefaultDataset(), configuration);
	}

	public ImageSearchingServiceImpl(String dataset,
			IRConfiguration configuration) {
		this.configuration = configuration;
		this.dataset = dataset;
	}

	// public ImageSearchingServiceImpl() {
	// this(null);
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.service.ir.image.api.ImageSearchingService#init()
	 */
	@Override
	public void init() {
		// ensure initialization of configuration attribute
		getConfiguration();
		// init index searcher bean
		try {
			if (index == null) {
				index = new MelampoSearcherHub();
				// File test = new File(".");
				// System.out.println(">>>>>> " + test.getAbsolutePath());
				File indexFolder = getConfiguration().getIndexFolder(
						getDataset());
				File indexConfFolder = getConfiguration().getIndexConfFolder(
						getDataset());
				if (indexFolder.exists()) {
					log.trace("loading image index from following location: "
							+ indexFolder.getAbsolutePath());
					// the indices.properties and LIRE_MP7ALL properties
					index.openIndices(indexConfFolder);
					// System.out.println("test");
				} else { // prepare folder
					indexFolder.mkdirs();
				}

			}
		} catch (BoFException e) {
			log.warn("No image index available!", e);
			// e.printStackTrace();
		} catch (VIRException e) {
			log.warn("Cannot open image index!", e);
		}

		// init feature extraction bean
		try {
			if (img2ftx == null)
				img2ftx = new Image2Features(getConfiguration()
						.getIndexConfFolder(getDataset()));
		} catch (Exception e) {
			log.warn("Cannot instantiate feature extractor!", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.service.ir.image.api.ImageSearchingService#searchSimilar
	 * (java.lang.String)
	 */
	public void searchSimilar(String resourceId) throws ImageSearchingException {

		ArrayList<String> vals = new ArrayList<String>();
		ArrayList<String> flds = new ArrayList<String>();

		flds.add(it.cnr.isti.melampo.index.Parameters.LIRE_MP7ALL);
		vals.add(resourceId);

		try {
			index.query(vals, flds, true);
			queryResults = new QueryResults();
			List<SearchResultItem> results = getSearchResultsList(index
					.getResults(0, NUM_RESULTS));
			queryResults.setResults(results);

		} catch (VIRException e) {
			throw new ImageSearchingException("Error performing search by id "
					+ resourceId, e);
		} catch (IOException e) {
			throw new ImageSearchingException("Error performing search by id "
					+ resourceId, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.service.ir.image.api.ImageSearchingService#searchSimilar
	 * (java.io.InputStream)
	 */
	public void searchSimilar(InputStream imageQueryObj)
			throws ImageSearchingException {
		log.info("searching by obj ");

		try {
			String features = img2ftx.extractFeatures(imageQueryObj);
			searchByImageFeatures(features);

		} catch (FeatureExtractionException e) {
			throw new ImageSearchingException(
					"Cannot extract features from (image) input stream! ", e);
		}

	}

	/**
	 * this method is the basis for index searching
	 * 
	 * @param features
	 * @throws ImageSearchingException
	 */
	private void searchByImageFeatures(String features)
			throws ImageSearchingException {
		try {
			ArrayList<String> vals = new ArrayList<String>();
			ArrayList<String> flds = new ArrayList<String>();

			flds.add(it.cnr.isti.melampo.index.Parameters.LIRE_MP7ALL);
			vals.add(features);
			index.query(vals, flds, false);

			queryResults = new QueryResults();
			List<SearchResultItem> results = getSearchResultsList(index
					.getResults(0, NUM_RESULTS));
			queryResults.setResults(results);
		} catch (VIRException e) {
			throw new ImageSearchingException("Error performing search by obj",
					e);
		} catch (IOException e) {
			throw new ImageSearchingException("Error performing search by obj",
					e);
		}
	}

	protected ArrayList<SearchResultItem> getSearchResultsList(String[][] retval) {
		ArrayList<SearchResultItem> results = new ArrayList<SearchResultItem>(
				retval.length);

		for (int i = 0; i < retval.length; i++) {
			SearchResultItem resultItem = new ImageSearchResultItem();
			resultItem.setScore(retval[i][0]);
			resultItem.setResourceId(retval[i][1]);
			resultItem.setThmbUrl(retval[i][2]);
			resultItem.setCachedThmbUrl(getCachedThumbnailUrl(resultItem));

			if (resultItem.getResourceId() == null)
				break; // in the case that the search returns less than
						// retval.length results

			results.add(resultItem);
			log.info("add to results: " + resultItem.getResourceId());
			log.info("score:" + resultItem.getScore());
		}
		log.debug("results from index service: " + results.toString());

		return results;
	}

	private String getCachedThumbnailUrl(SearchResultItem resultItem) {
		if(getThumbnailLocator() == null)
			return null;
		else{
			return getThumbnailLocator().getImageUrl(getDataset(), resultItem.getResourceId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.service.ir.image.api.ImageSearchingService#searchSimilar
	 * (java.net.URL)
	 */
	public void searchSimilar(URL imageQueryURL) throws ImageSearchingException {
		log.info("searching by URL " + imageQueryURL.toString());
		try {
			String features = img2ftx.extractFeatures(imageQueryURL);
			searchByImageFeatures(features);

		} catch (FeatureExtractionException e) {
			throw new ImageSearchingException(
					"Cannot extract features from (image) input stream! ", e);
		}

	}

	public List<SearchResultItem> getResults(int startFrom, int numResults) {
		return queryResults.getResults(startFrom, numResults);
	}

	@Override
	public int getTotalResults() throws ImageSearchingException {
		try {
			return queryResults.getResults(0, -1).size();
		} catch (NullPointerException e) {
			throw new ImageSearchingException(
					ImageSearchingException.MESSAGE_NO_RESULTS, e);
		}
	}

	@Override
	public IRConfiguration getConfiguration() {
		if (configuration == null)
			configuration = new IRConfigurationImpl();
		return configuration;
	}

	protected String getDataset() {
		return dataset;
	}

	public ImageLocator getThumbnailLocator() {
		if (imageLocator == null) {
			String imageLocatorClass = getConfiguration()
					.getImageLocatorClass();
			if (imageLocatorClass != null) {
				try {
					imageLocator = (ImageLocator) Class.forName(
							imageLocatorClass).newInstance();
					imageLocator.setConfigParams(getConfiguration().getLocatorConfigurations());
				} catch (Exception e) {
					throw new TechnicalRuntimeException(
							"Cannot instantiate image controller! check project configurations!",
							e);
				}
			}
		}

		return imageLocator;
	}
}
