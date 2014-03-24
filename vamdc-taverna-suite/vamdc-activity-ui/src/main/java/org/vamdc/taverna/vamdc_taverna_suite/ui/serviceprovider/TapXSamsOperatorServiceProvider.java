package org.vamdc.taverna.vamdc_taverna_suite.ui.serviceprovider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;

public class TapXSamsOperatorServiceProvider implements ServiceDescriptionProvider {
	
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

		List<ServiceDescription> results = new ArrayList<ServiceDescription>();

		// FIXME: Implement the actual service search/lookup instead
		// of dummy for-loop
		TapXSamsOperatorServiceDesc tapxsamsService = new TapXSamsOperatorServiceDesc();
		tapxsamsService.setOperator("AND");
		results.add(tapxsamsService);
		
		TapXSamsOperatorServiceDesc tapxsamsServiceOR = new TapXSamsOperatorServiceDesc();
		tapxsamsServiceOR.setOperator("OR");
		results.add(tapxsamsServiceOR);

		/*
		for (int i = 1; i <= 5; i++) {
			ExampleServiceDesc service = new ExampleServiceDesc();
			// Populate the service description bean
			service.setExampleString("Example " + i);
			service.setExampleUri(URI.create("http://localhost:8192/service"));

			// Optional: set description
			service.setDescription("Service example number " + i);
			results.add(service);
		}
		*/

		// partialResults() can also be called several times from inside
		// for-loop if the full search takes a long time
		callBack.partialResults(results);

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
		return "VAMDC TAP";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public String getId() {
		return providerId.toASCIIString();
	}

}
