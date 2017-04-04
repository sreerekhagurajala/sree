package com.metricstream.systemi.dao;

public class NlpCategoriesObject {

	
	private String component;
	private String bugStatus;
	private String shortDesc;
	private String longDesc;
	private String resolution;
	private String nlpCategory;
	private String nlpSubCategory;
	private String tsUpdated;
	private int bugId;
	
	public int getBugId() {
		return bugId;
	}
	public void setBugId(int bugId) {
		this.bugId = bugId;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getBugStatus() {
		return bugStatus;
	}
	public void setBugStatus(String bugStatus) {
		this.bugStatus = bugStatus;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public String getLongDesc() {
		return longDesc;
	}
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getNlpCategory() {
		return nlpCategory;
	}
	public void setNlpCategory(String nlpCategory) {
		this.nlpCategory = nlpCategory;
	}
	public String getNlpSubCategory() {
		return nlpSubCategory;
	}
	public void setNlpSubCategory(String nlpSubCategory) {
		this.nlpSubCategory = nlpSubCategory;
	}
	public String getTsUpdated() {
		return tsUpdated;
	}
	public void setTsUpdated(String tsUpdated) {
		this.tsUpdated = tsUpdated;
	}
	

	
}
