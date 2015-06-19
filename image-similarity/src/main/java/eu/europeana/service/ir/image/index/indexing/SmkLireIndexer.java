package eu.europeana.service.ir.image.index.indexing;

import java.io.IOException;

import it.cnr.isti.melampo.index.Parameters;
import it.cnr.isti.melampo.vir.exceptions.BoFException;
import it.cnr.isti.vir.features.lire.vd.CcDominantColor;
import it.cnr.isti.vir.features.mpeg7.LireObject;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;

public class SmkLireIndexer extends ExtendedLireIndexer{
	
	@Override
	protected void registerAnalyzers() {
		// TODO Auto-generated method stub
		super.registerAnalyzers();
		WhitespaceAnalyzer wsa = new WhitespaceAnalyzer();
		wrapper.addAnalyzer(Parameters.CC_DCD, wsa);
	}
	
	@Override
	protected Document buildDocument(LireObject s, String id)
			throws IOException, CorruptIndexException, BoFException {
		// TODO Auto-generated method stub
		Document doc = super.buildDocument(s, id);
		
		CcDominantColor dominantColor = (CcDominantColor) s.getFeature(CcDominantColor.class);
		String fieldValue = buildFiledValue(dominantColor);
		doc.add(new Field(Parameters.CC_DCD, fieldValue, Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES));
		return doc;		
	}

	private String buildFiledValue(CcDominantColor dominantColor) {
		final short[] scores = dominantColor.getScore();
		StringBuilder builder = new StringBuilder();
		String centroidValue = ""; 
		
		for(int i = 0; i < scores.length; i++){
			if(scores[i] > 0){
				//apend centroid value score[i] times
				for(int k = 0; k < scores[i]; k++){ 
					centroidValue = dominantColor.getCentroids().get(i);
					builder.append(centroidValue).append(" ");
				}
			}
		}
		
		return builder.toString();
	}

}
