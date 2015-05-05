package eu.europeana.creative.dataset.pt.classification;

import it.cnr.isti.vir.features.mpeg7.imageanalysis.ScalableColorPlusImpl;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import eu.europeana.api.client.exception.TechnicalRuntimeException;
import eu.europeana.api.client.thumbnails.ThumbnailsAccessor;
import eu.europeana.api.client.thumbnails.processing.LargeThumbnailsetProcessing;

public class GrayScaleSepiaDetector implements
		Observer {

	Logger log = Logger.getLogger(this.getClass());
	
	File outputFile;
	File imageBaseFolder;
	int thresholdDominantColorShare, thresholdDominantColorCount;
	Map<String, String> thumbnailCategoryMap;
	//Set<String> reportedCategories; 
	
	
	public GrayScaleSepiaDetector(File imageBaseFolder, int thresholdDominantColorShare, int thresholdDominantColorCount){
		super();
		this.imageBaseFolder = imageBaseFolder;
		this.thresholdDominantColorShare = thresholdDominantColorShare;
		this.thresholdDominantColorCount = thresholdDominantColorCount;
		
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
		File imageFile;
		String imageCategorization;
		BufferedImage image;  
		
		
		ScalableColorPlusImpl featureExtractor;
		for (Map.Entry<String, String> thumbnail : thumbnailMap.entrySet()) {
			
			imageFile = getImageFile(thumbnail.getKey());
			if(!imageFile.exists()){
				imageCategorization = "not_available";
			}else{
				try{
					featureExtractor = newFeatureExtractor();
					image = ImageIO.read(imageFile); 
					featureExtractor.extract(image);
					imageCategorization = featureExtractor.getCategory().toString();
				}catch(Exception e){
					imageCategorization = "error";
					log.info( "Error: " + e.getMessage() + " for file: " + imageFile.getAbsolutePath());
					log.debug(e);
					failureCount++;
				}
			}
			
			//all categories or selected categories
			//if(getReportedCategories() == null || getReportedCategories().contains(imageCategorization))
			thumbnailCategoryMap.put(thumbnail.getKey(), imageCategorization);
		}
		
		writeCategoriesToFile(thumbnailMap);
		
		((LargeThumbnailsetProcessing) o).increaseFailureCount(failureCount);
	}

	private void writeCategoriesToFile(Map<String, String> thumbnailMap) {
		
		if(!outputFile.exists())
			outputFile.getParentFile().mkdirs();

		BufferedWriter writer = null;
		
		try{
			writer=  new BufferedWriter(new FileWriter(outputFile, true));
			
			for (Map.Entry<String, String> thumbnail : thumbnailMap.entrySet()) {
				writer.write(thumbnail.getKey());
				writer.write(";");
				writer.write(thumbnail.getValue());
				writer.write(";");
				writer.write(thumbnailCategoryMap.get(thumbnail.getKey()));
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

	private ScalableColorPlusImpl newFeatureExtractor() {
		ScalableColorPlusImpl featureExtractor = new ScalableColorPlusImpl();
		featureExtractor.setThresholdDominantColorsShare(thresholdDominantColorShare);
		featureExtractor.setThresholdDominantColorsCount(thresholdDominantColorCount);
		
		return featureExtractor;
	}

	/**
	 * this is duplication of {@link ThumbnailsAccessor#getImageFile(File, String)}
	 * @param dir
	 * @param id
	 * @return
	 */
	public File getImageFile(String id) {
		String fileName = id + ".jpg";
		File imageFile = new File(getImageBaseFolder(), fileName);
		return imageFile;
	}
	
	public File getImageBaseFolder() {
		return imageBaseFolder;
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
