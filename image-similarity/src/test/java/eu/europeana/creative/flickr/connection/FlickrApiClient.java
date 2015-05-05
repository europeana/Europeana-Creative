package eu.europeana.creative.flickr.connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import eu.europeana.api.client.connection.EuropeanaConnection;
import eu.europeana.api.client.connection.HttpConnector;
import eu.europeana.api.client.exception.TechnicalRuntimeException;
import eu.europeana.api.client.model.ProvidersResponse;
import eu.europeana.creative.flickr.om.FlickrSetResponse;

public class FlickrApiClient {

	private static final Log log = LogFactory.getLog(FlickrApiClient.class);

	// private static final int MAX_RESULTS_PAGE = 100;

	private String apiKey;
//	private String authToken;
//	private String apiSig;
	public static final String FLICKR_SET_URI = "https://api.flickr.com/services/rest/?method=flickr.photosets.getPhotos&format=json&nojsoncallback=1&per_page=500";

	// public static String DEFAULT_API_KEY = "";
	private String flickrApiUri = "";
	private HttpConnector http = new HttpConnector();

	public final static String FLICKR_SETS_FOLDER = "./src/test/resources/flickr/sets/";

	// public final static String SEARCH_DESCRIPTIONS_FOLDER =
	// "./src/test/resources/search/descriptions";
	// public final static String SEARCH_NAMES_FOLDER =
	// "./src/test/resources/search/names";
	// public final static String SEARCH_ALIASES_FOLDER =
	// "./src/test/resources/search/aliases";
	// public final static String SEARCH_I18N_FOLDER =
	// "./src/test/resources/search/i18n";
	// public final static String SEARCH_PARENT_SUBGENRES_FOLDER =
	// "./src/test/resources/search/parentandsubgerens";
	// public final static String SEARCH_WIKIPEDIATITLE_FOLDER =
	// "./src/test/resources/search/wikipediatitle";

	/**
	 * Create a new connection to the Freebase API.
	 * 
	 * @param apiKey
	 *            API Key provided by Freebase to access the API
	 */
	public FlickrApiClient(String apiUri, String apiKey) {
		this.flickrApiUri = apiUri;
		if (apiKey != null)
			this.setApiKey(apiKey);
		else
			setApiKey(getDefaultApiKey());

//		setAuthToken(getDefaultAuthToken());
//		setApiSig(getDefaultApiSig());

	}

	public FlickrApiClient() {
		this(FLICKR_SET_URI, null);
		// setApiKey(getDefaultApiKey());

	}

	String getJSONResult(String url) throws IOException {
		log.trace("Call API: " + url);
		return http.getURLContent(url);

	}

	/**
	 * Returns the Freebase API URI for JSON calls
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getFlickrSetUrl(String setId, int pageNr)
			throws UnsupportedEncodingException {
		return getApiUri() + "&api_key=" + getApiKey() + "&photoset_id="
				+ setId + "&page=" + pageNr;
		// + "&auth_token=" +getAuthToken() + "&api_sig=" + getApiSig()

	}

//	private String getApiSig() {
//		return apiSig;
//	}

	/**
	 * Modifies the Europeana API URI for JSON calls. The default value points
	 * to the "http://api.europeana.eu/api/opensearch.json"
	 * 
	 * @param apiUri
	 */
	public void setApiUri(String apiUri) {
		this.flickrApiUri = apiUri;
	}

	/**
	 * @return the Europeana apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @return the Europeana apiKey
	 */
	public String getDefaultApiKey() {
		return FlickrClientConfiguration.getInstance().getApiKey();
	}

	public String getDefaultAuthToken() {
		return FlickrClientConfiguration.getInstance().getAuthToken();
	}

	public String getDefaultApiSig() {
		return FlickrClientConfiguration.getInstance().getApiSig();
	}

	/**
	 * @param apiKey
	 *            the Europeana apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiUri() {
		return flickrApiUri;
	}

	public void saveSetResponse(String setId, int pageNr, String folder) throws IOException {
		//File flickrSetResponseFile = new File(folder + setId, pageNr + ".json");
		File flickrSetResponseFile = getFlickrSetFile(setId, pageNr);
		
		// create parent dirs
		flickrSetResponseFile.getParentFile().mkdirs();
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(flickrSetResponseFile));
			writer.write(getJSONResult(getFlickrSetUrl(setId, pageNr)));
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				log.warn("cannot close results writer for file: "
						+ flickrSetResponseFile);
			}
		}
	}

	// protected String getSearchResultFromFile(String setId) throws IOException
	// {
	// String localFolder = FLICKR_SETS_FOLDER;
	// return readJsonResultFromFile(setId, localFolder);
	//
	// }

//	private String readJsonResultFromFile(String filename, String localFolder)
//			throws FileNotFoundException, IOException {
//		File queryResultsFile = new File(localFolder, filename + ".json");
//		return readJsonFile(queryResultsFile);
//	}

	/**
	 * 
	 * @param freebaseId
	 *            - mid attribute in freebase
	 * @return
	 * @throws IOException
	 */
	public String getJsonFlickrSet(String setId, int pageNr) throws IOException {

		File flickrSetFile = getFlickrSetFile(setId, pageNr);
		String flickrSetApi = getFlickrSetUrl(setId, pageNr);

		saveFlickrSet(flickrSetApi, flickrSetFile, false);

		return readJsonFile(flickrSetFile);
	}

	public FlickrSetResponse getFlickrResponse(String setId, int pageNr) {

		Gson gson = new GsonBuilder().create();
		FlickrSetResponse response = null;
		String jsonResult = null;
		
		try {
			jsonResult = getJsonFlickrSet(setId, pageNr);
			response = gson.fromJson(jsonResult, FlickrSetResponse.class);
		} catch (JsonSyntaxException e) {
			throw new TechnicalRuntimeException("Cannot parse Json Response"
					+ jsonResult, e);
		} catch (IOException e) {
			throw new TechnicalRuntimeException("Cannot invoke flickr API for set:"
					+ setId, e);
		}

		return response;
	}

	private void savetoJsonFile(File localFile, String url) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(localFile), "UTF-8"));
			// new FileWriter(queryResultsFile));
			writer.write(getJSONResult(url));
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				log.warn("cannot close results writer for file: " + localFile);
			}
		}
	}

	private String readJsonFile(File localFile) throws FileNotFoundException,
			IOException {
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		String line;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(localFile), "UTF-8"));
			// new FileReader(queryResultsFile));
			while ((line = reader.readLine()) != null)
				builder.append(line);

		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				log.warn("cannot close results writer for file: " + localFile);
			}
		}

		return builder.toString();
	}

	private void saveFlickrSet(String flickrUrl, File localFile,
			boolean overwrite) throws IOException {
		// do not read the file multiple times
		if (!overwrite && localFile.exists())
			return;

		// create parent dirs
		localFile.getParentFile().mkdirs();
		savetoJsonFile(localFile, flickrUrl);
	}

	private File getFlickrSetFile(String setId, int pageNr) {
		//File flickrSetResponseFile = new File(FLICKR_SETS_FOLDER + setId, pageNr + ".json");
		
		return new File(FLICKR_SETS_FOLDER + setId, pageNr + ".json");
	}

//	public String getAuthToken() {
//		return authToken;
//	}
//
//	public void setAuthToken(String authToken) {
//		this.authToken = authToken;
//	}
//
//	public void setApiSig(String apiSig) {
//		this.apiSig = apiSig;
//	}
}
