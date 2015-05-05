package eu.europeana.creative.flickr.om;

public class FlickrSetResponse {

	private String stat;
	private PhotoSet photoset;
	
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public PhotoSet getPhotoset() {
		return photoset;
	}
	public void setPhotoset(PhotoSet photoset) {
		this.photoset = photoset;
	}
}
