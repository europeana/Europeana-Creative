package eu.europeana.creative.dataset.pt.indexing;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import eu.europeana.api.client.exception.TechnicalRuntimeException;
import eu.europeana.api.client.thumbnails.processing.LargeThumbnailsetProcessing;
import eu.europeana.service.ir.image.api.ImageIndexingService;
import eu.europeana.service.ir.image.exceptions.ImageIndexingException;

public class ImageIndexingObserver implements
		Observer {

	Logger log = Logger.getLogger(this.getClass());
	ImageIndexingService imageIndexingService;
	
	public ImageIndexingService getImageIndexingService() {
		return imageIndexingService;
	}

	public ImageIndexingObserver(ImageIndexingService imageIndexingService){
		super();
		this.imageIndexingService = imageIndexingService; 
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(! (arg instanceof Map))
			throw new TechnicalRuntimeException("Wrong argument type. Expected map but invoked with " + arg.getClass());
		
		@SuppressWarnings("unchecked")
		Map<String, String> thumbnailMap = (Map<String, String>) arg; 
		
		
		int indexedCount = 0;
		try {
			indexedCount = getImageIndexingService().insertDatasetByIds(thumbnailMap.keySet());
		} catch (ImageIndexingException e) {
			e.printStackTrace();
		}
		
		int failureCount = thumbnailMap.size() - indexedCount;
		((LargeThumbnailsetProcessing) o).increaseFailureCount(failureCount);
	}

	
	
}
