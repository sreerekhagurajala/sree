package com.metricstream.systemi.dao;

public class WebdavNlpObject {

	private int trakrId;
	private String component;
	private String summary;
	private String description;
	private String category_nlp;
	private String sub_category_nlp;
	private String bug_status;
	private String resolution;
	
	
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public int getTrakrId() {
		return trakrId;
	}
	public void setTrakrId(int trakrId) {
		this.trakrId = trakrId;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory_nlp() {
		return category_nlp;
	}
	public void setCategory_nlp(String category_nlp) {
		this.category_nlp = category_nlp;
	}
	public String getSub_category_nlp() {
		return sub_category_nlp;
	}
	public void setSub_category_nlp(String sub_category_nlp) {
		this.sub_category_nlp = sub_category_nlp;
	}
	
	public String getBug_status() {
		return bug_status;
	}
	public void setBug_status(String bug_status) {
		this.bug_status = bug_status;
	}
}
