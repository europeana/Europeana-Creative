package eu.europeana.service.ir.image.api;

import it.cnr.isti.feature.extraction.FeatureExtractionException;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.exceptions.ImageIndexingException;

public interface PivotManagementService {

	public IRConfiguration getConfiguration();

	public void init();

	public void extractPivotFeatures(Map<String, String> pivotThumbnails) throws FileNotFoundException, FeatureExtractionException;

	public void extractPivotFeatures(Set<String> pivotThumbnailIds) throws FileNotFoundException, FeatureExtractionException;

	void generateLireObjectPivots() throws FileNotFoundException,
			FeatureExtractionException;
	
	void generateLireObjectPivots(Integer[] order) throws FileNotFoundException,
		FeatureExtractionException;

}
