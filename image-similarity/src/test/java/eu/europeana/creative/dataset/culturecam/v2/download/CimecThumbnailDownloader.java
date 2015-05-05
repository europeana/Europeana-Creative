package eu.europeana.creative.dataset.culturecam.v2.download;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import eu.europeana.api.client.model.search.EuropeanaObject;
import eu.europeana.api.client.thumbnails.download.ThumbnailDownloader;

public class CimecThumbnailDownloader extends ThumbnailDownloader {

	public CimecThumbnailDownloader(File downloadFolder) {
		super(downloadFolder);
	}
	
	
	public void downloadCimecIds(Map<String, String> thumbnailMap) throws IOException {
		
		int failureCount = 0;
		boolean successful;
		EuropeanaObject object;
		String relation;
		String imageUrl;
		//final File downloadFolder = new File(getDownloadFolder().getParentFile(), "cimec/");
		final File downloadFolder = new File("/tmp/eucreative/cimec/");
		
		
		for (Map.Entry<String, String> thumbnail : thumbnailMap.entrySet()) {
			object = getEuropeanaClient(). getObject(thumbnail.getKey());
			relation = getRelation(object);
			imageUrl = buildCimecUrl(relation);
			
			successful = writeThumbnailToFolder(thumbnail.getKey(), imageUrl, downloadFolder);
			
			if(!successful){
				System.out.println("Cannot download: " + thumbnail.getKey() + ";" + thumbnail.getValue());
				failureCount++;
			}
		}
		System.out.println("Total items: " + thumbnailMap.size());
		System.out.println("Failed for items: " + failureCount);
		
		
	}


	private String buildCimecUrl(String relation) {
		return "http://clasate.cimec.ro/medium/" + relation+".jpg";
	}


	private String getRelation(EuropeanaObject object) {
		// TODO Auto-generated method stub
		List<String> relations = object.getProxies().get(0).getDcRelation().get("def");
		for (String relation : relations) {
			if(relation.indexOf(" ")< 0)
				return relation;
		}
		
		return null;
	}

}
