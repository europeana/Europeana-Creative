package eu.europeana.creative.dataset.pt.classification;

import it.cnr.isti.vir.features.mpeg7.imageanalysis.ScalableColorPlusImpl.ImageType;

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
import eu.europeana.api.client.thumbnails.processing.LargeThumbnailsetProcessing;

public class ColorImageFiltering implements
		Observer {

	Logger log = Logger.getLogger(this.getClass());
	
	File outputFile;
	Map<String, String> thumbnailCategoryMap;
	
	
	public ColorImageFiltering(){
		super();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(! (arg instanceof Map))
			throw new TechnicalRuntimeException("Wrong argument type. Expected map but invoked with " + arg.getClass());
		
		@SuppressWarnings("unchecked")
		Map<String, String> thumbnailMap = (Map<String, String>) arg; 
		if(thumbnailCategoryMap == null){
			thumbnailCategoryMap = new HashMap<String, String>(thumbnailMap.size());
					
		}else{
			thumbnailCategoryMap.clear();
		}
		
		int failureCount = 0;
		
		for (Map.Entry<String, String> thumbnail : thumbnailMap.entrySet()) {
			try{
				if(thumbnail.getValue().endsWith(";" + ImageType.COLOR.toString()))
					thumbnailCategoryMap.put(thumbnail.getKey(), thumbnail.getValue());
				
			}catch(Exception e){
					log.info( "Error: " + e.getMessage() + " for thumbnail: " + thumbnail.getKey() + ";" + thumbnail.getValue());
					log.debug(e);
					failureCount++;
			}
			
			
		}
		
		writeFilteredItemsToFile();
		
		((LargeThumbnailsetProcessing) o).increaseFailureCount(failureCount);
	}

	private void writeFilteredItemsToFile() {
		
		//skip execution if no data to write
		if(thumbnailCategoryMap.isEmpty()){
			log.info("No data available for writing to outputfile!");
			return;
		}
		
		if(!outputFile.exists())
			outputFile.getParentFile().mkdirs();

		BufferedWriter writer = null;
		
		try{
			writer=  new BufferedWriter(new FileWriter(outputFile, true));
			
			for (Map.Entry<String, String> thumbnail : thumbnailCategoryMap.entrySet()) {
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

	
	public Map<String, String> getThumbnailCategoryMap() {
		return thumbnailCategoryMap;
	}
	
	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	
}
