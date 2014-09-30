package eu.europeana.creative.dataset.pt.classification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import eu.europeana.api.client.exception.TechnicalRuntimeException;

public class ThumbnailSelector implements
		Observer {

	Logger log = Logger.getLogger(this.getClass());
	
	File outputFile;
	Map<String, String> selectedThumbnailsMap;
	final int percentage;
	int itemCounter;
	int selectedItemsCount;
	
	
	
	public int getItemCounter() {
		return itemCounter;
	}

	public int getSelectedItemsCount() {
		return selectedItemsCount;
	}

	/**
	 * 
	 * @param percentage integre between 1 and 99
	 */
	public ThumbnailSelector(int percentage){
		super();
		if(percentage < 1 || percentage > 99)
			throw new IllegalArgumentException("pecentage must be in range 1...99. Provided Value :" + percentage);
		
		this.percentage = percentage;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(! (arg instanceof Map))
			throw new TechnicalRuntimeException("Wrong argument type. Expected map but invoked with " + arg.getClass());
		
		@SuppressWarnings("unchecked")
		Map<String, String> thumbnailMap = (Map<String, String>) arg; 
		if(selectedThumbnailsMap == null){
			selectedThumbnailsMap = new HashMap<String, String>(thumbnailMap.size());
					
		}else{
			selectedThumbnailsMap.clear();
		}
		
		for (Map.Entry<String, String> thumbnail : thumbnailMap.entrySet()) {
			itemCounter++;
			//keep the first <percentage> items from each 100 
			if( (itemCounter % 100) <= percentage){
				selectedThumbnailsMap.put(thumbnail.getKey(), thumbnail.getValue());
				selectedItemsCount++;
			}
		}
		
		writeFilteredItemsToFile();
		
		//((LargeThumbnailsetProcessing) o).increaseFailureCount(failureCount);
	}

	private void writeFilteredItemsToFile() {
		
		//skip execution if no data to write
		if(selectedThumbnailsMap.isEmpty()){
			log.info("No data available for writing to outputfile!");
			return;
		}
		
		if(!outputFile.exists())
			outputFile.getParentFile().mkdirs();

		BufferedWriter writer = null;
		
		try{
			writer=  new BufferedWriter(new FileWriter(outputFile, true));
			
			for (Map.Entry<String, String> thumbnail : selectedThumbnailsMap.entrySet()) {
				writer.write(thumbnail.getKey());
				writer.write(";");
				writer.write(thumbnail.getValue());
				writer.write("\n");
			}
			writer.flush();
			
		}catch(Exception e){
			log.info("Failed to write thumbnail category to file", e);
		}finally{
			try {
				if(writer != null)
					writer.close();
			} catch (IOException e) {
				log.warn("cannot close file writer:", e);
			}	
		}
		
				
	}

	
	public Map<String, String> getselectedThumbnailsMap() {
		return selectedThumbnailsMap;
	}
	
	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	
}
