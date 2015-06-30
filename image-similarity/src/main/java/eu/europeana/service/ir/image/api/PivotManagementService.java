package eu.europeana.service.ir.image.api;

import it.cnr.isti.feature.extraction.FeatureExtractionException;
import it.cnr.isti.melampo.vir.exceptions.VIRException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import eu.europeana.service.ir.image.IRConfiguration;

public interface PivotManagementService {

	public IRConfiguration getConfiguration();

	public void init();

	public void extractPivotFeatures() throws IOException,
	FeatureExtractionException;

	public void extractPivotFeatures(Map<String, String> pivotThumbnails) throws FileNotFoundException, FeatureExtractionException;

	public void extractPivotFeatures(Set<String> pivotThumbnailIds) throws FileNotFoundException, FeatureExtractionException;

	public File getPivotsFCArchiveFile();

	public void generateLireObjectPivotsBin() throws FileNotFoundException,
			FeatureExtractionException, IOException, VIRException;

	public void generateLireObjectPivotsBin(int topK, boolean forceFeatureExtraction)
			throws FileNotFoundException, FeatureExtractionException, IOException;

	public void generateLirePivotsBinWithOrder(Integer[] order) throws IOException,
			FeatureExtractionException;

	public void generateLirePivotsBinWithOrder(File orderCsvFile) throws IOException,
	FeatureExtractionException;

	public int getTopN() throws IOException, VIRException;

}
