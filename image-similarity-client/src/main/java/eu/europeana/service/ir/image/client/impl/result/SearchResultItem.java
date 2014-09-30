package eu.europeana.service.ir.image.client.impl.result;

public class SearchResultItem {
	private String resourceId;
	private String thmbUrl;
	private String cachedThmbUrl;
	private String score;
	
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getThmbUrl() {
		return thmbUrl;
	}
	public void setThmbUrl(String thmbUrl) {
		this.thmbUrl = thmbUrl;
	}
	public String getCachedThmbUrl() {
		return cachedThmbUrl;
	}
	public void setCachedThmbUrl(String cachedThmbUrl) {
		this.cachedThmbUrl = cachedThmbUrl;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
}
