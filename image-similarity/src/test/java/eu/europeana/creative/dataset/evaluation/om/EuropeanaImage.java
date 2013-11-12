package eu.europeana.creative.dataset.evaluation.om;

public class EuropeanaImage extends CategorizedObject {

	private String id;
	private String imageUrl;
	private String collection;

	public EuropeanaImage() {

	}

	public EuropeanaImage(String id, String imageUrl, String contentClass, String contentSubClass, String collection) {
		this.id = id;
		this.imageUrl = imageUrl;
		this.contentClass = contentClass;
		this.contentSubClass = contentSubClass;
		this.collection = collection;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String url) {
		this.imageUrl = url;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

}
