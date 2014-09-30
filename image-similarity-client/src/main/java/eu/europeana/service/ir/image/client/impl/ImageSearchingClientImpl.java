package eu.europeana.service.ir.image.client.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import eu.europeana.service.ir.image.client.IRImageConfiguration;
import eu.europeana.service.ir.image.client.ImageSearchingClient;
import eu.europeana.service.ir.image.client.exception.ApiConnectionException;
import eu.europeana.service.ir.image.client.exception.ImageSearchApiException;
import eu.europeana.service.ir.image.client.impl.abstracts.BaseIrApiConnection;
import eu.europeana.service.ir.image.client.impl.result.ImageSearchResults;

public class ImageSearchingClientImpl extends BaseIrApiConnection implements ImageSearchingClient{

	public ImageSearchingClientImpl(String irServiceUri, String apiKey) {
		super(irServiceUri, apiKey);
		initConfiguration();
	}

	public ImageSearchingClientImpl() {
		this(null, null);
		setApiKey(getConfiguration().getApiKey());
		setIrServiceUri(getConfiguration().getIrImageSearchUri());
	}
	
	protected void initConfiguration() {
		configuration = new IRImageConfigurationImpl();
		configuration.init();
		
	}

	private IRImageConfiguration configuration;
	
//	//private RestClientProperties clientProperties;
//	private DefaultHttpClient httpClient;

	public void setConfiguration(IRImageConfiguration configuration) {
		this.configuration = configuration;
	}

	public IRImageConfiguration getConfiguration() {
		return configuration;
	} 
	
	@Override
	public ImageSearchResults searchById(String resourceId, int startPos, int rows) throws ImageSearchApiException{
		logger.info("Search by id: " + resourceId);
		String url = buildInvokationUrl("/searchById.json", getApiKey());
		url = appendStartAndRows(url, startPos, rows);
		
		url+=  "&queryImageId=" + resourceId;
		
		ImageSearchResults results = null;
		try {
			results = getImageSearchResults(url);
		} catch (IOException e) {
			throw new ImageSearchApiException("Cannot invoke search by imageId: " + url, e);
		}
		
		logger.trace("Number of retrieved results: " + results.getItemsCount());
		logger.debug("Total results: " + results.getTotalResults());
		
		return results;
	}
	
	@Override
	public ImageSearchResults searchByUrl(String imageUrl, int startPos, int rows ) throws ImageSearchApiException, UnsupportedEncodingException{
		logger.info("Search by url: " + imageUrl);
		String encodedImageUrl = URLEncoder.encode(imageUrl, "UTF-8");
		logger.trace("Encoded url: " + encodedImageUrl);
		
		String url = buildInvokationUrl("/searchByUrl.json", getApiKey());
		url = appendStartAndRows(url, startPos, rows);
		url+=  "&queryImageUrl=" + encodedImageUrl;
		
		ImageSearchResults results = null;
		try {
			results = getImageSearchResults(url);
		} catch (IOException e) {
			throw new ImageSearchApiException("Cannot invoke search by imageUrl : " + url, e);
		}
		
		logger.trace("Number of retrieved results: " + results.getItemsCount());
		logger.debug("Total results: " + results.getTotalResults());
		
		return results;
	}

	protected String appendStartAndRows(String url, int startPos, int rows) {
		if(startPos > 0)
			url+= "&start=" + startPos;
			
		if(rows > 0)
			url+= "&rows=" + rows;
		return url;
	}
	
	@Override
	public String getComponentNameFromRest(){

		String url = buildInvokationUrl("/component", null);
		logger.trace("Get component name: " + url);
		try {
//			Map<String, String> acceptHeader = new HashMap<String, String>(1);
//			acceptHeader.put("Content-Type", "text/*");
//			acceptHeader.put("Accept", "text/*");
//			assert (acceptHeader.size() == 2); 
			
			return getHttpConnection().getURLContent(url, null);
		} catch (IOException e) {
			throw new ApiConnectionException("cannot retrieve component name from the API interface!", e);
		}
    }
	
	 /**
     * Creates URL based on the URI passed in.
     */
    protected String buildInvokationUrl(String action, String apiKey) {
        StringBuilder sb = new StringBuilder();
        sb.append(getConfiguration().getIrImageSearchUri());
        //sb.append("/").append(getConfiguration().getComponentName());
        //sb.append(uri);
        sb.append(action);
        sb.append("?wsKey=").append(apiKey);
        
        return sb.toString();
    }
	
}
