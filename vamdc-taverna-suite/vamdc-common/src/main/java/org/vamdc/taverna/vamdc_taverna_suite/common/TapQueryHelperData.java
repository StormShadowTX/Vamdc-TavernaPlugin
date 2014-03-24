package org.vamdc.taverna.vamdc_taverna_suite.common;

public class TapQueryHelperData extends QueryHelperData {

	private String title;
	private String []returnAbles;
	private String []restrictAbles;
	private String identifier;
	private String url;
	
	public TapQueryHelperData(String title,String []returnAbles, String []restrictAbles, 
			String identifier, String url) {
		super(title,identifier,url);
	 	this.returnAbles = returnAbles;
	 	this.restrictAbles = restrictAbles;
	}
	
	
	public String[] getReturnAbles() {return this.returnAbles;}
	public String[] getRestrictAbles() {return this.restrictAbles;}
	
}