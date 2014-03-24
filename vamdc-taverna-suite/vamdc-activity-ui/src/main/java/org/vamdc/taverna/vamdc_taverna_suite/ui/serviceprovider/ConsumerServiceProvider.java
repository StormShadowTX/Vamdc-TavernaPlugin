package org.vamdc.taverna.vamdc_taverna_suite.ui.serviceprovider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;

import eu.vamdc.registry.Registry;
import org.vamdc.taverna.vamdc_taverna_suite.common.QueryHelperData;
import org.vamdc.taverna.vamdc_taverna_suite.common.ConsumerQueryHelper;
import org.vamdc.taverna.vamdc_taverna_suite.common.RegistryUtil;



public class ConsumerServiceProvider implements ServiceDescriptionProvider {
	
	private String[] viewNames = {"HTML-Service","SME-Service"};
	private static final URI providerId = URI
		.create("http://www.vamdc.org/taverna/plugin/suite");
	
	/**
	 * Do the actual search for services. Return using the callBack parameter.
	 */
	@SuppressWarnings("unchecked")
	public void findServiceDescriptionsAsync(
			FindServiceDescriptionsCallBack callBack) {
		// Use callback.status() for long-running searches
		// callBack.status("Resolving example services");
//System.out.println("SYSTEM PATH = " + System.getProperty("java.class.path"));

		List<ServiceDescription> results = new ArrayList<ServiceDescription>();

		// FIXME: Implement the actual service search/lookup instead
		// of dummy for-loop
		Registry reggie =  RegistryUtil.getRegistry();

		QueryHelperData []qhd = ConsumerQueryHelper.getQueryHelperInfo(reggie);
		for(int i = 0;i < qhd.length;i++) {
			ConsumerServiceDesc consumerService = new ConsumerServiceDesc();
			consumerService.setViewName(qhd[i].getTitle());
			/*
			System.out.println("should be adding " + viewNames[i]);
			if(viewNames[i].equals("HTML-Service")) {
				consumerService.setConsumerURL("http://casx019-zone1.ast.cam.ac.uk:80/XSAMS-views/service");
			}else {
				consumerService.setConsumerURL("http://vamdc.tmy.se/applyXSL/xsams2sme/service");
			}
			*/
			System.out.println("should be adding " + qhd[i].getTitle() + " url: " + qhd[i].getURL());

			consumerService.setConsumerURL(qhd[i].getURL());
			consumerService.setDescription("Consumer Service");
			System.out.println("results size: " + results.size());
			results.add(consumerService);
			System.out.println("results sizeb: " + results.size() + " name: " + consumerService.getViewName() + " name2: " + consumerService.getName());

			// partialResults() can also be called several times from inside
			// for-loop if the full search takes a long time
			callBack.partialResults(results);

		}
		

		// No more results will be coming
		callBack.finished();
	}

	/**
	 * Icon for service provider
	 */
	public Icon getIcon() {
		return ExampleServiceIcon.getIcon();
	}

	/**
	 * Name of service provider, appears in right click for 'Remove service
	 * provider'
	 */
	public String getName() {
		return "Consumer Service";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public String getId() {
		return providerId.toASCIIString();
	}

}
