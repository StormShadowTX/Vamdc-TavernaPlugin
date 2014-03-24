package org.vamdc.taverna.vamdc_taverna_suite.ui.serviceprovider;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import org.vamdc.taverna.vamdc_taverna_suite.ConsumerServiceActivity;
import org.vamdc.taverna.vamdc_taverna_suite.ConsumerServiceActivityConfigurationBean;
//import org.vamdc.taverna.vamdc_taverna_suite.common.TapQueryHelper;
import net.sf.taverna.raven.appconfig.ApplicationRuntime;

public class ConsumerServiceDesc extends ServiceDescription<ConsumerServiceActivityConfigurationBean> {

	private String viewName = null;
	
	private String consumerURL = null;
	/**
	 * The subclass of Activity which should be instantiated when adding a service
	 * for this description 
	 */
	@Override
	public Class<? extends Activity<ConsumerServiceActivityConfigurationBean>> getActivityClass() {
		return ConsumerServiceActivity.class;
	}

	/**
	 * The configuration bean which is to be used for configuring the instantiated activity.
	 * Making this bean will typically require some of the fields set on this service
	 * description, like an endpoint URL or method name. 
	 * 
	 */
	@Override
	public ConsumerServiceActivityConfigurationBean getActivityConfiguration() {
		ConsumerServiceActivityConfigurationBean bean = new ConsumerServiceActivityConfigurationBean();
		bean.setURL(this.consumerURL);
		bean.setViewName(this.viewName);
		//bean.setRepositoryDir(TapQueryHelper.getRepositoryDir());
		//bean.setRepositoryDir(ApplicationRuntime.getInstance().getLocalRepositoryDir().toString());
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
		return getViewName();
	}
	
	public String getViewName() {
		System.out.println("the viewname = " + this.viewName);
		return this.viewName;
	}
	
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	
	public String getConsumerURL() {
		return this.consumerURL;
	}
	
	public void setConsumerURL(String consumerURL) {
		this.consumerURL = consumerURL;
	}


	/**
	 * The path to this service description in the service palette. Folders
	 * will be created for each element of the returned path.
	 */
	@Override
	public List<String> getPath() {
		// For deeper paths you may return several strings
		
		return Arrays.asList("VAMDC", "Utils");
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
		return Arrays.<Object>asList("vamdcConsumerService",this.viewName);
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
