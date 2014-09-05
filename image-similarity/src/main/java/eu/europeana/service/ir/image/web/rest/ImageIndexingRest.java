package eu.europeana.service.ir.image.web.rest;

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
import eu.europeana.api2.web.model.json.abstracts.IndexingStatusResponse;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.api.ImageIndexingService;
import eu.europeana.service.ir.image.exceptions.ImageIndexingException;
import eu.europeana.service.ir.image.model.IndexingStatus;

/**
 * This is the class implementing the write functionality of this component.
 * It provides web URLs as access points for remote invocation of the image analysis and indexing service. 
 * @see ImageIndexingService
 * @see IRConfiguration   
 * @author  paolo
 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
 */
@Controller
public class ImageIndexingRest extends BaseRestService {

	private Logger log = Logger.getLogger(getClass());
	
	private static final int REQUEST_NUMBER_NOT_ACTIVE = -99;

	@Autowired
	ImageIndexingService imageIndexing;
	
	@Autowired
	IRConfigurationImpl configuration;

	public void setImageIndexingService(ImageIndexingService imageIndexing) {
		this.imageIndexing = imageIndexing;
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
		if (configuration == null)
			configuration = new IRConfigurationImpl();

		return configuration;
	}
	
	@Override
	public String getComponentName() {
		return getConfiguration().getComponentName();
	}
	
	/**
	 * This method returns the name of the current component. This is the basic method to test if the deployment was completed correctly 
	 * and the service is able to accept answer web requests. 
	 * @return the name of the current web component 
	 */
	@RequestMapping(value = "/index/component", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String displayComponentName() {
		return getComponentName()+"-indexing";
	}
	
//	@POST
//	@Path("/insertImageObj")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	public void insertImgObj(
//			@FormDataParam("imageObj") InputStream imageObj,
//			@FormDataParam("imageId") String imageId) {
//		try {
//			imageIndexing.insertImage(imageId, imageObj);
//		} catch (ImageIndexingException e) {
//			log.error("error indexing obj " + imageId, e);
//			throw new WebApplicationException(e);
//		} finally {
//			if (imageObj != null) {
//				try {
//					imageObj.close();
//				} catch (IOException e) {
//					log.warn("unable to close the image source input stream");
//				}
//			}
//		}
//	}
	
	/**
	 * This method is used to insert into the index the image identified by the given europeanaId and located at the imageUrl location 
	 * @param imageUrl
	 * @param europeanaId
	 * @param wskey
	 * @param profile
	 * @param response 
	 * @return - the notification of successful execution of the indexing operation or the appropriate error message  
	 */
	@RequestMapping(value = "/index/imageUrl", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String insertImgUrl(
			@RequestParam(value = "imageUrl", required = true) String imageUrl,
			@RequestParam(value = "europeanaId", required = false, defaultValue="0") String europeanaId,
			@RequestParam(value = "wskey", required = false) String wskey,
			@RequestParam(value = "profile", required = false, defaultValue="indeximage") String profile,
			HttpServletResponse response) {
		try {
			imageIndexing.insertImage(europeanaId, new URL(imageUrl));
			return "OK: image added to index for the object " + europeanaId;
		} catch (ImageIndexingException e) {
			log.error("error indexing obj " + europeanaId, e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return (new ApiError(wskey, "/index/imageUrl", e.getMessage(),
					REQUEST_NUMBER_NOT_ACTIVE)).toString();
		} catch (MalformedURLException e) {
			log.error("error indexing obj " + europeanaId, e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return (new ApiError(wskey, "/index/imageUrl", e.getMessage(),
					REQUEST_NUMBER_NOT_ACTIVE)).toString();
		}
		
	}
	
	/**
	 * This method is used to insert into the index all image found within the collection identified by the given collectionId 
	 *  
	 * @param imageUrl
	 * @param europeanaId
	 * @param wskey
	 * @param profile
	 * @param response 
	 * @return - the notification of successful execution of the indexing operation or the appropriate error message  
	 */
	@RequestMapping(value = "/index/collection", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String insertCollection(
			@RequestParam(value = "collectionId", required = true) String collectionId,
			@RequestParam(value = "wskey", required = false) String wskey,
			@RequestParam(value = "profile", required = false, defaultValue="indeximage") String profile,
			HttpServletResponse response) {
		try {
			return "OK:" + imageIndexing.insertCollection(collectionId);
		} catch (ImageIndexingException e) {
			log.error("error indexing obj " + collectionId, e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return (new ApiError(wskey, "/index/insertImageUrl", e.getMessage(),
					REQUEST_NUMBER_NOT_ACTIVE)).toString();
		}
	}
	/**
	 * This method is used to check the progress of indexing by collectionId
	 * @param collectionId
	 * @param wskey
	 * @param profile
	 * @param response
	 * @return - the status of the indexing process. See also {@link IndexingStatus}
	 */
	@RequestMapping(value = "/index/progress", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody ApiResponse getProgressStatus(
			@RequestParam(value = "collectionId", required = true) String collectionId,
			@RequestParam(value = "wskey", required = false) String wskey,
			@RequestParam(value = "profile", required = false, defaultValue="indeximage") String profile,
			HttpServletResponse response) {
		try {
			IndexingStatus status = imageIndexing.getIndexingStatus(collectionId);
			IndexingStatusResponse apiResponse = new IndexingStatusResponse(wskey, "/index/progress");
			apiResponse.setIndexingStatus(status);
			return apiResponse;
		} catch (ImageIndexingException e) {
			log.error("error indexing obj " + collectionId, e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new ApiError(wskey, "/index/insertImageUrl", e.getMessage(),
					REQUEST_NUMBER_NOT_ACTIVE);
		}
	}
	
//	@GET
//	@Path("/openIndex")
//	public void openIndex(@QueryParam("imageUrl") @DefaultValue("true") boolean append) {
//		try {
//			imageIndexing.openIndex();
//		} catch (ImageIndexingException e) {
//			log.error("error opening index ", e);
//			throw new WebApplicationException(e);
//		}
//	}	
	
//	@GET
//	@Path("/closeIndex")
//	public void closeIndex() {
//		try {
//			imageIndexing.closeIndex();
//		} catch (ImageIndexingException e) {
//			log.error("error closing index ", e);
//			throw new WebApplicationException(e);
//		}
//	}
	
}

