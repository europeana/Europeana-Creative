package eu.europeana.assets.ir.image.api.evaluation;

public class EuropeanaImage {

	private String id;
	private String imageUrl;
	private String contentClass;
	private String contentSubClass;
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

	public String getContentClass() {
		return contentClass;
	}

	public void setContentClass(String contentClass) {
		this.contentClass = contentClass;
	}

	public String getContentSubClass() {
		return contentSubClass;
	}

	public void setContentSubClass(String contentSubClass) {
		this.contentSubClass = contentSubClass;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

}
