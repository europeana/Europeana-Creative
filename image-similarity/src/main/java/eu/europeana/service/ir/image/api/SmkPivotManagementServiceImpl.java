package eu.europeana.service.ir.image.api;

import it.cnr.isti.vir.features.FeatureClassCollector;
import it.cnr.isti.vir.features.lire.vd.CcDominantColor;
import it.cnr.isti.vir.features.lire.vd.LireColorLayout;
import it.cnr.isti.vir.features.lire.vd.LireEdgeHistogram;
import it.cnr.isti.vir.features.lire.vd.LireScalableColor;
import it.cnr.isti.vir.readers.CoPhIRv2Reader;
import eu.europeana.service.ir.image.IRConfiguration;

public class SmkPivotManagementServiceImpl extends PivotManagementServiceImpl{

	FeatureClassCollector featureClasses = new FeatureClassCollector(
			LireColorLayout.class,
			LireScalableColor.class,
			LireEdgeHistogram.class
			//,CcDominantColor.class 
			);
	
	public SmkPivotManagementServiceImpl(IRConfiguration configuration, String dataset){
		
		super(configuration, dataset);
	}

	protected void registerFeatureClassColector() {
		CoPhIRv2Reader.setFeatures(featureClasses);
	}
	
	@Override
	protected FeatureClassCollector getFeatureClassCollector() {
		//return super.getFeatureClassCollector();
		return featureClasses;
	}
}
