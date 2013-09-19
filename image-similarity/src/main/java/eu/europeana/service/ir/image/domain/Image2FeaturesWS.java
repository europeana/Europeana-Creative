package eu.europeana.service.ir.image.domain;

//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//
//import org.springframework.http.MediaType;
//
//import eu.europeana.corelib.definitions.solr.entity.WebResource;

public class Image2FeaturesWS {

//	public static String BASE_URI = "http://aimar.isti.cnr.it/FeaturesExtractionService/services";
//	
//	private Client c;
//	private WebResource service;
//	private String queryURL;
//	
//	public Image2FeaturesWS() {
//		c = Client.create();
//		service = c.resource(BASE_URI);
//	}
//
//	public String image2Features(InputStream imgStream) throws IOException {
//		byte[] imgBytes = Tools.inputStream2ByteArray(imgStream);
//		String res = imgByte2Features(imgBytes);
//		return res;
//	}
//
//	public String image2Features(File imgFile) throws IOException {
//		byte[] imgBytes = Tools.getBytesFromFile(imgFile);
//		String res = imgByte2Features(imgBytes);
//		return res;
//	}
//
//	private String imgByte2Features(byte[] imgBytes) {
//		queryURL = null;
//		
//		FormDataMultiPart fdmp = new FormDataMultiPart(); 
//		FormDataBodyPart fdp = new FormDataBodyPart("imgFile", imgBytes, MediaType.MULTIPART_FORM_DATA_TYPE);  
//		FormDataBodyPart formDataBodyPart = new FormDataBodyPart("features", "MPEG7");
//		fdmp.bodyPart(fdp);
//		fdmp.bodyPart(formDataBodyPart);
//	    
//	    // POST the request 
//	    ClientResponse response = service.path("/FeatureExtractionService"). 
//	      type(MediaType.MULTIPART_FORM_DATA).post(ClientResponse.class, fdmp); 
//		
//		String res = response.getEntity(String.class);
//		System.out.println("Response Status : " + res);
//		String features = null;
//		
//		if (res != null) {
//			String[] values = res.split("axrdstyaityfajhgf");
//			if (values != null && values.length == 2) {
//				queryURL = values[0];
//				features = values[1];
//			}
//		}
//		
//		System.out.println("queryURL: " + queryURL);
//		
//		// System.out.println("Response Status : " + res);
//		return features;
//	}
//
//	public String imgURL2Features(String imgURL) {
//		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
//		queryParams.add("url", imgURL);
//		queryParams.add("features", "MPEG7");
//		ClientResponse response = service.path("/FeatureExtractionService/url")
//				.queryParams(queryParams).get(ClientResponse.class);
//		String res = response.getEntity(String.class);
//		System.out.println("Response Status : " + res);
//		
//		String features = null;
//		
//		if (res != null) {
//			String[] values = res.split("axrdstyaityfajhgf");
//			if (values != null && values.length == 2) {
//				queryURL = values[0];
//				features = values[1];
//			}
//		}
//		
//		System.out.println("queryURL: " + queryURL);
//		
//		// System.out.println("Response Status : " + res);
//		return features;
//	}
}