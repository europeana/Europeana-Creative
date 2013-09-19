package eu.europeana.service.ir.image.model;

public class IndexingStatus {

	private String collectionId;
	private int totalObjects;
	private int indexedObjects;
	private int skippedObjects;
	
	public String getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	public int getTotalObjects() {
		return totalObjects;
	}
	public void setTotalObjects(int totalObjects) {
		this.totalObjects = totalObjects;
	}
	public int getIndexedObjects() {
		return indexedObjects;
	}
	public void setIndexedObjects(int indexedObjects) {
		this.indexedObjects = indexedObjects;
	}
	public int getSkippedObjects() {
		return skippedObjects;
	}
	public void setSkippedObjects(int skippedObjects) {
		this.skippedObjects = skippedObjects;
	}
	
}
