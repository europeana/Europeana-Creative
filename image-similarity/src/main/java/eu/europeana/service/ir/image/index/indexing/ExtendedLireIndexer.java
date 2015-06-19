package eu.europeana.service.ir.image.index.indexing;

import it.cnr.isti.melampo.index.LireObjectFieldAdder;
import it.cnr.isti.melampo.index.indexing.LireIndexer;
import it.cnr.isti.melampo.vir.exceptions.BoFException;
import it.cnr.isti.melampo.vir.exceptions.VIRException;
import it.cnr.isti.vir.features.mpeg7.LireObject;

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;

public class ExtendedLireIndexer extends LireIndexer {

	IndexWriter m_w_ref = null;
	LireObjectFieldAdder m_sfaALL_ref = null;
	Integer m_toppivs_ref = null;

	@Override
	public void addDocument(LireObject s, String id)
			throws CorruptIndexException, IOException, VIRException {

		Document doc = buildDocument(s, id);

		// access private field by reflection
		getM_w().addDocument(doc);


		System.out.println("indexed doc (URL)" + s.getThmbURL());

	}

	protected Document buildDocument(LireObject s, String id)
			throws IOException, CorruptIndexException, BoFException {
		Document doc = new Document();

		// access private field by reflection
		// MPEG-7
		getM_sfaALL().addFieldToDoc(doc, s, getM_toppivs());

		// ID
		getM_sfaALL().AddIDField(doc, id);
		System.out.println("id " + id);

		// add URL to doc

		doc.add(new org.apache.lucene.document.Field("THMBURL", s.getThmbURL(),
				org.apache.lucene.document.Field.Store.YES,
				org.apache.lucene.document.Field.Index.NOT_ANALYZED,
				org.apache.lucene.document.Field.TermVector.NO));
		return doc;
	}

	private LireObjectFieldAdder getM_sfaALL() {

//		if (m_sfaALL_ref == null)
//			try {
//				Field field = this.getClass().getSuperclass().getDeclaredField("m_sfaALL");
//				field.setAccessible(true);
//				m_sfaALL_ref = (LireObjectFieldAdder) field.get(this);
//			} catch (Exception e) {
//				throw new RuntimeException("", e);
//			}
//
//		return m_sfaALL_ref;
		return m_sfaALL;
	}

	private IndexWriter getM_w() {
//		Field field;
//		if (m_w_ref == null) {
//			try {
//				field = this.getClass().getSuperclass().getDeclaredField("m_w");
//				field.setAccessible(true);
//				m_w_ref = (IndexWriter) field.get(this);
//			} catch (Exception e) {
//				throw new RuntimeException("cannot access m_w field", e);
//			}
//		}
//
//		return m_w_ref;
		return m_w;
	}

	private Integer getM_toppivs() {
//		if (m_toppivs_ref == null) {
//			try {
//				Field field;
//				field = this.getClass().getSuperclass().getDeclaredField("m_toppivs");
//				field.setAccessible(true);
//				m_toppivs_ref = (Integer) field.get(this);
//			} catch (Exception e) {
//				throw new RuntimeException("cannot access m_toppivs");
//			}
//		}
//		return m_toppivs_ref;
		return m_toppivs;
	}

}
