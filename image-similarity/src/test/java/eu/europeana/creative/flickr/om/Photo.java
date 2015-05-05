package eu.europeana.creative.flickr.om;

public class Photo {

	private String id, secret, server, title, isprimary;
	private int farm, ispublic, isfriend, isfamily;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIsprimary() {
		return isprimary;
	}
	public void setIsprimary(String isprimary) {
		this.isprimary = isprimary;
	}
	public int getFarm() {
		return farm;
	}
	public void setFarm(int farm) {
		this.farm = farm;
	}
	public int getIspublic() {
		return ispublic;
	}
	public void setIspublic(int ispublic) {
		this.ispublic = ispublic;
	}
	public int getIsfriend() {
		return isfriend;
	}
	public void setIsfriend(int isfriend) {
		this.isfriend = isfriend;
	}
	public int getIsfamily() {
		return isfamily;
	}
	public void setIsfamily(int isfamily) {
		this.isfamily = isfamily;
	}
}
