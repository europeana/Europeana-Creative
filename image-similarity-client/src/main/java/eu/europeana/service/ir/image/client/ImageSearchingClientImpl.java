package eu.europeana.service.ir.image.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import eu.europeana.service.ir.image.client.abstracts.IRImageConfiguration;
import eu.europeana.service.ir.image.client.abstracts.ImageSearchingClient;

@Component
public class ImageSearchingClientImpl implements ImageSearchingClient{

	@Autowired
	private IRImageConfiguration configuration;
	
	@Autowired
	RestTemplate restTemplate;
	
	protected final String DEFAULT_ACTION = "get";
	
//	//private RestClientProperties clientProperties;
//	private DefaultHttpClient httpClient;

	public void setConfiguration(IRImageConfiguration configuration) {
		this.configuration = configuration;
	}

	public IRImageConfiguration getConfiguration() {
		return configuration;
	} 
	

//	
//	InputStream is = new ByteArrayInputStream(restTemplate.getForObject(
//			URI.create(Config.URI_FAVORITES_GET), byte[].class));
//	try {
//		return new ObjectMapper().readValue(is, UserFavorites.class);
//	} catch (JsonProcessingException e) {
//		e.printStackTrace();
//	} catch (IOException e) {
//		e.printStackTrace();
//	}
//	return null;
	
	
	@Override
	public String getComponentNameFromRest() {
//        ResponseObj obj = (ResponseObj) restTemplate. getForObject("http://url/myService/{param}", ResponseObj.class, "myParameterWord");

		String url = buildInvokationUrl("/component", DEFAULT_ACTION, null);
		//String value = restTemplate.getForObject(url, String.class);
		ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
		HttpStatus statusCode = entity.getStatusCode();
//      MediaType contentType = entity.getHeaders().getContentType();
		if(HttpStatus.OK.equals(statusCode))
			return entity.getBody();
		else
			return "error when retrieving the component name. Error code: "+ statusCode;
		
    }
	
	 /**
     * Creates URL based on the URI passed in.
     */
    protected String buildInvokationUrl(String uri, String action, String apiKey) {
        StringBuilder sb = new StringBuilder();
        sb.append(getConfiguration().getServerUrl());
        sb.append("/").append(getConfiguration().getComponentName());
        sb.append(uri);
        sb.append("?action=").append(action);
        if(apiKey != null){
        	sb.append("&wsKey=").append(apiKey);
        }
        
//        getConfiguration().getServerUrl();
//        
//        sb.append(clientProperties.getUrl());
//        sb.append(clientProperties.getApiPath());
//        sb.append(uri);
//        
//        logger.debug("URL is '{}'.", sb.toString());
        
        return sb.toString();
    }
	
}
