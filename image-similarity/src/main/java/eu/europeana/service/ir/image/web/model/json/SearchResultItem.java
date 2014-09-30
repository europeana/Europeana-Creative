package eu.europeana.service.ir.image.web.model.json;

public interface SearchResultItem {

	public abstract void setScore(String score);

	public abstract String getScore();

	public abstract void setCachedThmbUrl(String cachedThmbUrl);

	public abstract String getCachedThmbUrl();

	public abstract void setThmbUrl(String thmbUrl);

	public abstract String getThmbUrl();

	public abstract void setResourceId(String resourceId);

	public abstract String getResourceId();

}
