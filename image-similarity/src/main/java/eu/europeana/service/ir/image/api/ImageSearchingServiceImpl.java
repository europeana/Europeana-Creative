package eu.europeana.service.ir.image.api;

import it.cnr.isti.melampo.index.searching.MelampoSearcherHub;
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

import eu.europeana.corelib.tools.lookuptable.EuropeanaId;
import eu.europeana.service.ir.image.IRImageConfiguration;
import eu.europeana.service.ir.image.domain.Image2Features;
import eu.europeana.service.ir.image.domain.QueryResults;
import eu.europeana.service.ir.image.exceptions.FeaturesExtractionException;
import eu.europeana.service.ir.image.exceptions.ImageSearchingException;

/**
 * @author Paolo Bolettieri <paolo.bolettieri@isti.cnr.it>
 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
 */
public class ImageSearchingServiceImpl implements ImageSearchingService {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	private IRImageConfiguration configuration;

	private String dataset = null;

	private MelampoSearcherHub index;

	private QueryResults queryResults = new QueryResults();

	private static final int NUM_RESULTS = 100;

	private Image2Features img2ftx;

	public ImageSearchingServiceImpl(IRImageConfiguration configuration) {
		this(configuration.getDefaultDataset(), configuration);
	}
	
	public ImageSearchingServiceImpl(String dataset, IRImageConfiguration configuration) {
		this.configuration = configuration;
		this.dataset = dataset;
	}

//	public ImageSearchingServiceImpl() {
//		this(null);
//	}

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
				File indexFolder = getConfiguration().getIndexFolder(getDataset());
				File indexConfFolder = getConfiguration().getIndexConfFolder(getDataset());
				if (indexFolder.exists()) {
					log.trace("loading image index from following location: "
							+ indexFolder.getAbsolutePath());
					// the indices.properties and LIRE_MP7ALL properties
					index.openIndices(indexConfFolder);
					// System.out.println("test");
				} else{ // prepare folder
					indexFolder.mkdirs();
				}

			}
		} catch (BoFException e) {
			log.trace("No image index available!");
			//e.printStackTrace();
		} catch (VIRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// init feature extraction bean
		try {
			if (img2ftx == null)
				img2ftx = new Image2Features(getDataset(), getConfiguration());
		} catch (FeaturesExtractionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void searchSimilar(EuropeanaId imageQueryId)
			throws ImageSearchingException {

		// try {
		// List<EuropeanaCollection> coll = dataManagement.getCollections();
		// for (int j = 0; j < coll.size(); j++) {
		// long collId = coll.get(j).getId();
		//
		// List<EuropeanaId> objects =
		// dataManagement.getCollectionObjects(collId);
		//
		// for (int i = 0; i < objects.size(); i++) {
		// EuropeanaId id = objects.get(i);
		// System.out.println("uri " + i + ": " + id.getEuropeanaUri());
		// }
		// }
		//
		//
		//
		// } catch (AssetsClientException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		ArrayList<String> vals = new ArrayList<String>();
		ArrayList<String> flds = new ArrayList<String>();

		flds.add(it.cnr.isti.melampo.index.Parameters.LIRE_MP7ALL);
		vals.add(imageQueryId.getNewId());

		try {
			index.query(vals, flds, true);
			queryResults.setResults(index.getResults(0, NUM_RESULTS));
		} catch (VIRException e) {
			throw new ImageSearchingException("Error performing search by id "
					+ imageQueryId, e);
		} catch (IOException e) {
			throw new ImageSearchingException("Error performing search by id "
					+ imageQueryId, e);
		}
	}

	public void searchSimilar(InputStream imageQueryObj)
			throws ImageSearchingException {
		log.info("searching by obj ");
		try {
			String features = img2ftx.image2Features(imageQueryObj);
			ArrayList<String> vals = new ArrayList<String>();
			ArrayList<String> flds = new ArrayList<String>();

			flds.add(it.cnr.isti.melampo.index.Parameters.LIRE_MP7ALL);
			vals.add(features);
			index.query(vals, flds, false);
			queryResults.setResults(index.getResults(0, NUM_RESULTS));
		} catch (VIRException e) {
			throw new ImageSearchingException("Error performing search by obj",
					e);
		} catch (IOException e) {
			throw new ImageSearchingException("Error performing search by obj",
					e);
		} catch (FeaturesExtractionException e) {
			throw new ImageSearchingException("Error performing search by obj",
					e);
		}
	}

	public void searchSimilar(URL imageQueryURL) throws ImageSearchingException {
		log.info("searching by URL " + imageQueryURL.toString());
		try {
			String features = img2ftx.image2Features(imageQueryURL);
			ArrayList<String> vals = new ArrayList<String>();
			ArrayList<String> flds = new ArrayList<String>();

			flds.add(it.cnr.isti.melampo.index.Parameters.LIRE_MP7ALL);
			vals.add(features);
			index.query(vals, flds, false);
			queryResults.setResults(index.getResults(0, NUM_RESULTS));
		} catch (VIRException e) {
			throw new ImageSearchingException("Error performing search by URL "
					+ imageQueryURL.toString(), e);
		} catch (IOException e) {
			throw new ImageSearchingException("Error performing search by URL "
					+ imageQueryURL.toString(), e);
		} catch (FeaturesExtractionException e) {
			throw new ImageSearchingException("Error performing search by URL "
					+ imageQueryURL.toString(), e);
		}
	}

	public List<EuropeanaId> getResults(int startFrom, int numResults) {
		return queryResults.getResults(startFrom, numResults);
	}

	@Override
	public int getTotalResults() {
		return queryResults.getResults(0, -1).size();
	}

	@Override
	public IRImageConfiguration getConfiguration() {
		if (configuration == null)
			configuration = new IRImageConfiguration();
		return configuration;
	}

	protected String getDataset() {
		return dataset;
	}

	
}
