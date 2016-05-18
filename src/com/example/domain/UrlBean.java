package com.example.domain;


public class UrlBean {
	private String downloadUrl;// apk的下载路径
	private int versionCode;//app版本号
	private String versionName;
	private String description;
	
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "UrlBean [downloadUrl=" + downloadUrl + ", versionCode="
				+ versionCode + ", versionName=" + versionName
				+ ", description=" + description + "]";
	}
	
	
}
