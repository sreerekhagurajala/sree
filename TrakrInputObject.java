package com.metricstream.systemi.dao;

public class TrakrInputObject {

	private int trakrId;
	private String component;
	private String summary;
	private String description;
	private String status;
	private String rcaDetails;
	private String rcaCategory;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRcaDetails() {
		return rcaDetails;
	}

	public void setRcaDetails(String rcaDetails) {
		this.rcaDetails = rcaDetails;
	}

	public String getRcaCategory() {
		return rcaCategory;
	}

	public void setRcaCategory(String rcaCategory) {
		this.rcaCategory = rcaCategory;
	}

	
}
