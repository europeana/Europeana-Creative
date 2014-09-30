package eu.europeana.service.ir.image.web.model.json;

public class ImageSearchResultItem  implements SearchResultItem{

	private String resourceId;
	private String thmbUrl;
	private String cachedThmbUrl;
	private String score;
	
	@Override
	public String getResourceId() {
		return resourceId;
	}
	@Override
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	@Override
	public String getThmbUrl() {
		return thmbUrl;
	}
	@Override
	public void setThmbUrl(String thmbUrl) {
		this.thmbUrl = thmbUrl;
	}
	@Override
	public String getCachedThmbUrl() {
		return cachedThmbUrl;
	}
	@Override
	public void setCachedThmbUrl(String cachedThmbUrl) {
		this.cachedThmbUrl = cachedThmbUrl;
	}
	@Override
	public String getScore() {
		return score;
	}
	@Override
	public void setScore(String score) {
		this.score = score;
	}
	
}
