package eu.europeana.creative.dataset.culturecam.v2.download;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import eu.europeana.api.client.model.search.EuropeanaObject;
import eu.europeana.api.client.thumbnails.download.ThumbnailDownloader;

public class OnbImageDownloader extends ThumbnailDownloader {

	public OnbImageDownloader(File downloadFolder) {
		super(downloadFolder);
	}
	
	
	public void downloadImages(Map<String, String> thumbnailMap) throws IOException {
		
		int failureCount = 0;
		boolean successful;
		//final File downloadFolder = new File(getDownloadFolder().getParentFile(), "cimec/");
		
		for (Map.Entry<String, String> thumbnail : thumbnailMap.entrySet()) {
			successful = writeThumbnailToFolder(thumbnail.getKey(), thumbnail.getValue(), getDownloadFolder());
			
			if(!successful){
				System.out.println("Cannot download: " + thumbnail.getKey() + ";" + thumbnail.getValue());
				failureCount++;
			}
		}
		System.out.println("Total items: " + thumbnailMap.size());
		System.out.println("Failed for items: " + failureCount);
	}


	
}
