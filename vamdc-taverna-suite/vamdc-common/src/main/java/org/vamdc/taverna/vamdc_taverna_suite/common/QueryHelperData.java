package org.vamdc.taverna.vamdc_taverna_suite.common;

public class QueryHelperData {

	private String title;
	private String []returnAbles;
	private String []restrictAbles;
	private String identifier;
	private String url;
	
	public QueryHelperData(String title, String identifier, String url) {
	 	this.title = title;
	 	this.identifier = identifier;
	 	this.url = url;
	}
	
	public String getTitle(){return this.title;}
	public String getIdentifier(){return this.identifier;}
	public String getURL() {return this.url;}
	
}