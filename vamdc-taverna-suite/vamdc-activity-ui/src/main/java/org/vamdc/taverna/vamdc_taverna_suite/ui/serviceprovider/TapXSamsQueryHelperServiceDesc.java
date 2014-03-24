package org.vamdc.taverna.vamdc_taverna_suite.ui.serviceprovider;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import org.vamdc.taverna.vamdc_taverna_suite.TapXSamsQueryHelperActivity;
import org.vamdc.taverna.vamdc_taverna_suite.TapXSamsQueryHelperActivityConfigurationBean;

import org.vamdc.taverna.vamdc_taverna_suite.common.TapQueryHelperData;

public class TapXSamsQueryHelperServiceDesc extends ServiceDescription<TapXSamsQueryHelperActivityConfigurationBean> {

	/**
	 * The subclass of Activity which should be instantiated when adding a service
	 * for this description 
	 */
	@Override
	public Class<? extends Activity<TapXSamsQueryHelperActivityConfigurationBean>> getActivityClass() {
		return TapXSamsQueryHelperActivity.class;
	}

	/**
	 * The configuration bean which is to be used for configuring the instantiated activity.
	 * Making this bean will typically require some of the fields set on this service
	 * description, like an endpoint URL or method name. 
	 * 
	 */
	@Override
	public TapXSamsQueryHelperActivityConfigurationBean getActivityConfiguration() {
		TapXSamsQueryHelperActivityConfigurationBean bean = new TapXSamsQueryHelperActivityConfigurationBean();
		System.out.println("IN GETACTIVITYCONFIGURATIN AND RESTRICTABLE = " + this.restrictAble);
		bean.setRestrictAble(this.restrictAble);
		bean.setTapQueryHelperData(this.thd);
		//bean.setExampleString(exampleString);
		//bean.setExampleUri(exampleUri);
		return bean;
	}

	/**
	 * An icon to represent this service description in the service palette.
	 */
	@Override
	public Icon getIcon() {
		return ExampleServiceIcon.getIcon();
	}

	/**
	 * The display name that will be shown in service palette and will
	 * be used as a template for processor name when added to workflow.
	 */
	@Override
	public String getName() {
		return getRestrictAble();
	}

	/**
	 * The path to this service description in the service palette. Folders
	 * will be created for each element of the returned path.
	 */
	@Override
	public List<String> getPath() {
		// For deeper paths you may return several strings
		
		return Arrays.asList("VAMDC", "TapXSAMS", "Nodes", thd.getTitle().trim());
	}

	/**
	 * Return a list of data values uniquely identifying this service
	 * description (to avoid duplicates). Include only primary key like fields,
	 * ie. ignore descriptions, icons, etc.
	 */
	@Override
	protected List<? extends Object> getIdentifyingData() {
		// FIXME: Use your fields instead of example fields
		//return Arrays.<Object>asList(exampleString, exampleUri);
		return Arrays.<Object>asList("vamdcTAPQueryHelper",thd.getIdentifier(),this.restrictAble);
	}
	
	private TapQueryHelperData thd;
	private String restrictAble;
	
	public TapQueryHelperData getTapQueryHelperData() {
		return this.thd;
	}
	
	public void setTapQueryHelperData(TapQueryHelperData thd) {
		this.thd = thd;
	}
	
	public void setRestrictAble(String restrictAble) {
		this.restrictAble = restrictAble;
	}
	
	public String getRestrictAble() {
		return this.restrictAble;
	}

	
	// FIXME: Replace example fields and getters/setters with any required
	// and optional fields. (All fields are searchable in the Service palette,
	// for instance try a search for exampleString:3)
	/*
	private String exampleString;
	private URI exampleUri;
	public String getExampleString() {
		return exampleString;
	}
	public URI getExampleUri() {
		return exampleUri;
	}
	public void setExampleString(String exampleString) {
		this.exampleString = exampleString;
	}
	public void setExampleUri(URI exampleUri) {
		this.exampleUri = exampleUri;
	}
	*/

}
