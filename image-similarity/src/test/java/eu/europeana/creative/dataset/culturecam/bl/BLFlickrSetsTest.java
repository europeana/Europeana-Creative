package eu.europeana.creative.dataset.culturecam.bl;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import eu.europeana.api.client.EuropeanaApi2Client;
import eu.europeana.api.client.dataset.DatasetDescriptor;
import eu.europeana.api.client.exception.TechnicalRuntimeException;
import eu.europeana.api.client.model.EuropeanaApi2Results;
import eu.europeana.api.client.model.search.EuropeanaApi2Item;
import eu.europeana.api.client.thumbnails.ThumbnailsForCollectionAccessor;
import eu.europeana.creative.flickr.connection.FlickrApiClient;
import eu.europeana.creative.flickr.om.FlickrSetResponse;
import eu.europeana.creative.flickr.om.Photo;
import eu.europeana.creative.flickr.om.PhotoSet;

public class BLFlickrSetsTest extends BaseBlTest{

	EuropeanaApi2Client euClient = new EuropeanaApi2Client();
	
	
	//1: @Test 
	public void buildBlFlickrSets() throws IOException{
		setDataset("culturecam");
		
		//String setId;
		FlickrApiClient flickrClient = new FlickrApiClient();
		//String setAsJson; 
		FlickrSetResponse response;
		int pageNr = 1;
		Map<String, String> flickrPhotosMap;
		int totalResults;
		
		for (String setId : blSets.keySet()) {
			pageNr = 1;
			//setAsJson = flickrClient.getJsonFlickrSet(setId);
			response = getFlickrSetResponse(flickrClient, setId, pageNr);
			totalResults = Integer.parseInt(response.getPhotoset().getTotal());
			
			flickrPhotosMap = new HashMap<String, String>(totalResults); 
			assertTrue(pageNr == response.getPhotoset().getPage());
			addToPhotosMap(flickrPhotosMap, response.getPhotoset());
			
			//read second page for collections with more than 500 items
			pageNr = 2;
			if("72157641857515565".equals(setId)){
				response = getFlickrSetResponse(flickrClient, setId, 2);
				assertTrue(pageNr == response.getPhotoset().getPage());
				addToPhotosMap(flickrPhotosMap, response.getPhotoset());		
			}
			
			writePhotosToCsvFile(setId, flickrPhotosMap);
		}
		
		System.out.println("Done");
	}

	//2: @Test
	public void buildBlEuSets() throws IOException{
		setDataset("culturecam");
		
		for (String setId : blSets.keySet()) {
			
			buildEuSetForFlickr(setId);
		}
		
		System.out.println("Done");
	}
	
	//3: @Test 
	public void downloadBlEuSets() throws IOException{
		setDataset("culturecam");
		
		for (String setId : blSets.keySet()) {
			
			System.out.println("downloading set: " + setId);
			downloadBlEuSet(setId);
		}
		
		System.out.println("Done");
	}
	
	
	private void downloadBlEuSet(String setId) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		DatasetDescriptor dataset = new DatasetDescriptor(blSets.get(setId), setId);
		String thumbnailSetFile = getCvsFileForStep(dataset, STEP_THUMBNAILS);
		Map<String, String> thumbnailsMap = readThumbnailsMap(new File(thumbnailSetFile));
		
//		String europeanaId;
//		String thumbnailUrl;
		
		ThumbnailsForCollectionAccessor tca = new ThumbnailsForCollectionAccessor(getDataset());
		// getConfiguration().getImageFolder(getDataset());
		tca.copyThumbnails(thumbnailsMap, new File(IMAGE_FOLDER_NAME));
		
		
//		for (Map.Entry<String, String> thumbnail : thumbnailsMap.entrySet()) {
//			europeanaId.thu
//			flickrId = flickrPhoto.getKey();
//			flickrUrl = flickrPhoto.getValue().split(";")[0];
//			addThumbnail(flickrId, flickrUrl, euBlSetMap);
		//}
		
	}

	private void buildEuSetForFlickr(String setId) throws FileNotFoundException, IOException {
		
		DatasetDescriptor dataset = new DatasetDescriptor(blSets.get(setId), setId);
		String flickrSetFile = getCvsFileForStep(dataset, STEP_FLICKR);
		String thumbnailSetFile = getCvsFileForStep(dataset, STEP_THUMBNAILS);
		
		if((new File(thumbnailSetFile)).exists())
			return;
		
		Map<String, String> flickrSetMap = readThumbnailsMap(new File(flickrSetFile));
		Map<String, String> euBlSetMap = new HashMap<String, String>(flickrSetMap.size());
		
		String flickrId;
		String flickrUrl;
		
		for (Map.Entry<String, String> flickrPhoto : flickrSetMap.entrySet()) {
			flickrId = flickrPhoto.getKey();
			flickrUrl = flickrPhoto.getValue().split(";")[0];
			addThumbnail(flickrId, flickrUrl, euBlSetMap);
		}
		
		
		
		writeThumbnailsToCsvFile(dataset, euBlSetMap, new File(thumbnailSetFile));
		System.out.println("written: " + thumbnailSetFile);
		
	}

	private void addThumbnail(String flickrId, String flickrUrl, Map<String, String> euBlSetMap) throws UnsupportedEncodingException {
		
//		String flickrPhotoUrl = "https://flickr.com/photos/britishlibrary/";
//		String portalUrl =	"http://europeana.eu/portal/search.html?query=proxy_dc_relation:" + URLEncoder.encode(flickrPhotoUrl, "UTF-8") +
//		  flickrId;
		
		//String flickrPhotoUrl = "https://flickr.com/photos/britishlibrary/";
		String portalUrl =	"http://europeana.eu/portal/search.html?query=proxy_dc_relation:" 
				+ "*flickr.com/photos/britishlibrary/" 
				+ flickrId;
		
		try {
			EuropeanaApi2Results results = euClient.searchApi2(portalUrl, 2, -1);
			if(!results.getSuccess()){
				//System.out.println("cannot ");
				throw new TechnicalRuntimeException("Search with portalUrl not sucessfull:" + results.getError());
			}
			
			if(results.getItems().size() != 1){
				//throw new TechnicalRuntimeException("Search with portalUrl must return one result, but got:" + results.getItems().size());
				System.out.println("ERROR: Search with portalUrl must return one result, but got:" + results.getItems().size());
				return;
			}
			
			EuropeanaApi2Item item = results.getItems().get(0);
			euBlSetMap.put(item.getId(), item.getEdmPreview().get(0) + ";" + flickrUrl);
			
		} catch (Exception e) {
			//throw new TechnicalRuntimeException("cannot retrieve europeana results for portalUrl:" + portalUrl, e);
			System.out.println("cannot retrieve europeana results for portalUrl:" + portalUrl + "\n");
			e.printStackTrace();
		} 
	}

	private void writePhotosToCsvFile(String setId,
			Map<String, String> flickrPhotosMap) throws IOException {
		
		DatasetDescriptor dataset = new DatasetDescriptor(blSets.get(setId), setId);
		
		String fileName = getCvsFileForStep(dataset, STEP_FLICKR);
		File csvFile = new File(fileName);
		
		writeThumbnailsToCsvFile(dataset, flickrPhotosMap, csvFile, POLICY_OVERWRITE_FILE);
		
	}

	private void addToPhotosMap(Map<String, String> flickrPhotosMap,
			PhotoSet photoset) {
		
		for (Photo photo : photoset.getPhoto()) {
			flickrPhotosMap.put(photo.getId(), getUrlAndTitle(photo));
		}
		
	}

	private String getUrlAndTitle(Photo photo) {
		return "https://www.flickr.com/photos/britishlibrary/"+ photo.getId() + ";" + photo.getTitle();
	}

	protected FlickrSetResponse getFlickrSetResponse(FlickrApiClient flickrClient,
			String setId, int pageNr) {
		FlickrSetResponse response;
		response = flickrClient.getFlickrResponse(setId, pageNr);
		System.out.println("\nSET: " + setId);
		System.out.println("pageNr: " + pageNr);
		System.out.println("\nStatus: " + response.getStat());
		System.out.println("Flickr SetId: " + response.getPhotoset().getId());
		System.out.println("Title: " + response.getPhotoset().getTitle());
		System.out.println("Total Images: " + response.getPhotoset().getTotal());
		System.out.println("Retrieved Photos: " + response.getPhotoset().getPhoto().size());
		
		return response;
	}
	
}
