package eu.europeana.service.ir.image.web.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europeana.api2.web.model.json.ApiError;
import eu.europeana.api2.web.model.json.abstracts.ApiResponse;
import eu.europeana.corelib.tools.lookuptable.EuropeanaId;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.api.ImageSearchingService;
import eu.europeana.service.ir.image.domain.Tools;
import eu.europeana.service.ir.image.exceptions.ImageSearchingException;
import eu.europeana.service.ir.image.web.model.json.ImageSimilaritySearchResults;

/**
 * @author paolo
 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
 */
@Controller
public class ImageSearchingRest extends BaseRestService {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	ImageSearchingService imageSearching;

	@Autowired
	IRConfigurationImpl configuration;
	
	private static final int REQUEST_NUMBER_NOT_ACTIVE = -99;

	//QueryResultsMarshaller queryResultsMarshaller = new QueryResultsMarshaller();

	public void setImageSearchingService(ImageSearchingService imageSearching) {
		this.imageSearching = imageSearching;
	}

	/**
	 * @param configuration
	 */
	public void setConfiguration(IRConfigurationImpl configuration) {
		this.configuration = configuration;
	}

	/**
	 * @return
	 */
	public IRConfigurationImpl getConfiguration() {
		return configuration;
	}

	@Override
	public String getComponentName() {
		return getConfiguration().getComponentName();
	}

	@RequestMapping(value = "/component", method = RequestMethod.GET, produces = "text/*")
	@ResponseBody
	public String displayComponentName() {
		return getComponentName();
	}

	@RequestMapping(value = "/searchById.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ApiResponse searchById(
			@RequestParam(value = "queryImageId", required = true) String queryImageId,
			@RequestParam(value = "start", required = false, defaultValue="0") int start,
			@RequestParam(value = "rows", required = false, defaultValue="12") int rows,
			@RequestParam(value = "wskey", required = false) String wskey,
			@RequestParam(value = "profile", required = false, defaultValue="similarimage") String profile,
			HttpServletResponse response) {

		EuropeanaId id = new EuropeanaId();
		id.setNewId(queryImageId);

		try {
			imageSearching.searchSimilar(id);
			return prepareSearchResults(start, rows, wskey, "/image/searchById.json");
			
		} catch (ImageSearchingException e) {
			log.error("error searching by id " + queryImageId, e);
			// throw new WebApplicationException(e);
			return new ApiError(wskey, "/searchById.json", e.getMessage(),
					REQUEST_NUMBER_NOT_ACTIVE);
		}
	}

	private ApiResponse prepareSearchResults(int start, int rows, String wskey, String action)
			throws ImageSearchingException {
		ImageSimilaritySearchResults<EuropeanaId> results = new ImageSimilaritySearchResults<EuropeanaId>(wskey, action); 
		results.setSearchResults(imageSearching.getResults(start, rows));
		results.setTotalResults(imageSearching.getTotalResults());
		
		return results;
	}

	@RequestMapping(value = "/searchByObj.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	// @POST
	// @Path("/searchByObj")
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public @ResponseBody
	ApiResponse searchByImageFile(
			@RequestParam(value = "imgFile", required = true) InputStream queryImage,
			@RequestParam(value = "start", required = false, defaultValue="0") int start,
			@RequestParam(value = "rows", required = false, defaultValue="12") int rows,
			@RequestParam(value = "wskey", required = false) String wskey,
			@RequestParam(value = "profile", required = false, defaultValue="similarimage") String profile,
			HttpServletResponse response ) {
		
		try {
			imageSearching.searchSimilar(queryImage);
			return prepareSearchResults(start, rows, wskey, "/image/searchByObj.json");
			
		} catch (ImageSearchingException e) {
			log.error("error searching by obj ", e);
			// throw new WebApplicationException(e);
			return new ApiError(wskey, "/searchByObj.json", e.getMessage(),
					REQUEST_NUMBER_NOT_ACTIVE);
		} finally {
			if (queryImage != null) {
				try {
					queryImage.close();
				} catch (IOException e) {
					log.warn("unable to close the image source input stream");
				}
			}
		}
	}

	@RequestMapping(value = "/searchByUrl.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ApiResponse searchByUrl(
			@RequestParam(value = "queryImageUrl", required = true) String queryImageUrl,
			@RequestParam(value = "start", required = false, defaultValue="0") int start,
			@RequestParam(value = "rows", required = false, defaultValue="12") int rows,
			@RequestParam(value = "wskey", required = false) String wskey,
			@RequestParam(value = "profile", required = false, defaultValue="similarimage") String profile,
			HttpServletResponse response) {
		
		String encodedUrl = null;
		try {
			encodedUrl = Tools.encodeUrl(queryImageUrl);
			imageSearching.searchSimilar(new URL(encodedUrl));
			return prepareSearchResults(start, rows, wskey, "/image/searchByUrl.json");
					
		} catch (ImageSearchingException e) {
			log.error("error searching by URL " + encodedUrl, e);
			// throw new WebApplicationException(e1);
			return new ApiError(wskey, "/searchByUrl.json", e.getMessage(),
					REQUEST_NUMBER_NOT_ACTIVE);
		} catch (MalformedURLException e) {
			log.error("error searching by URL " + encodedUrl, e);
			// throw new WebApplicationException(e);
			return new ApiError(wskey, "/searchByUrl.json", e.getMessage(),
					REQUEST_NUMBER_NOT_ACTIVE);
		}
	}
}
