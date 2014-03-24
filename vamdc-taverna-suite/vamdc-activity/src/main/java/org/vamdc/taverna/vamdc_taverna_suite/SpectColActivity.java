package org.vamdc.taverna.vamdc_taverna_suite;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.vamdc.taverna.vamdc_taverna_suite.common.*;

import java.net.URL;
import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;
import net.sf.taverna.raven.appconfig.ApplicationRuntime;

public class SpectColActivity extends
		AbstractAsynchronousActivity<SpectColActivityConfigurationBean>
		implements AsynchronousActivity<SpectColActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	
	private SpectColActivityConfigurationBean configBean;

	@Override
	public void configure(SpectColActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		// Any pre-config sanity checks
		/*
		if (configBean.getExampleString().equals("invalidExample")) {
			throw new ActivityConfigurationException(
					"Example string can't be 'invalidExample'");
		}
		*/
		// Store for getConfiguration(), but you could also make
		// getConfiguration() return a new bean from other sources
		this.configBean = configBean;

		// OPTIONAL: 
		// Do any server-side lookups and configuration, like resolving WSDLs

		// myClient = new MyClient(configBean.getExampleUri());
		// this.service = myClient.getService(configBean.getExampleString());

		
		// REQUIRED: (Re)create input/output ports depending on configuration
		configurePorts();
	}

	protected void configurePorts() {
		// In case we are being reconfigured - remove existing ports first
		// to avoid duplicates
		removeInputs();
		// FIXME: Replace with your input and output port definitions
		describeSpectColInputs();

	}
	
	private void describeSpectColInputs()  {
		addInput("Optional URL",1,true,null,String.class);		
	}
	
		
	@SuppressWarnings("unchecked")
	@Override
	public void executeAsynch(final Map<String, T2Reference> inputs,
			final AsynchronousActivityCallback callback) {
		// Don't execute service directly now, request to be run ask to be run
		// from thread pool and return asynchronously
		callback.requestRun(new Runnable() {
			
			public void run() {
				InvocationContext context = callback.getContext();
				ReferenceService referenceService = context.getReferenceService();
				// Resolve inputs 
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();

				/*
				Set<String> inputKeys = inputs.keySet();
	    		String []tmpKeys = inputKeys.toArray(new String[0]);
	    		for(int k = 0;k < tmpKeys.length;k++) {
	    			System.out.println("keyset k = " + k + " value = " + tmpKeys[k]);
	    		}
	    		*/
				System.out.println("REPODIR: " + ApplicationRuntime.getInstance().getLocalRepositoryDir().toString());
				String spectColJarPath = ApplicationRuntime.getInstance().getLocalRepositoryDir().toString() +
				"/org/vamdc/SPECTCOL/12-02-SNAPSHOT/SPECTCOL-12-02-SNAPSHOT.jar";
				
	    		String []command = null;
				if(inputs.containsKey("Optional URL")) {
					List<String> optionalURL =  (List<String>) referenceService.renderIdentifier(inputs.get("Optional URL"), String.class, context);
					System.out.println("optionalURL = " + optionalURL);
					
					if(optionalURL != null && optionalURL.size() > 0) {
						
						command = new String[3+optionalURL.size()];
						command[0] = "java";
						command[1] = "-jar";
						command[2] = spectColJarPath;
			    		for(int j = 0;j < optionalURL.size();j++) {
			    			String url = optionalURL.get(j);
			    			try {
				    			String tmp = java.net.URLDecoder.decode(url,"UTF-8");
				    			if(tmp.equals(url)) {
				    				//not encoded
				    				url = java.net.URLEncoder.encode(url,"UTF-8");
				    			}
			    			}catch(java.io.UnsupportedEncodingException ue) {
			    				ue.printStackTrace();
			    			}
			    			command[j+3] = url;
			    			/*
			    			if(!url.startsWith("\"")) {
			    				command[j+3] = "\"" + url + "\"";
			    			}else {
			    				command[j+3] = url;
			    			}
			    			*/
			    			
			    		}
					}else {
						command = new String[]{"java","-jar",spectColJarPath};
					}
				}else {
					command = new String[]{"java","-jar",spectColJarPath};
				}
				
				//String []cmd2;
				//cmd2 = {"java","-jar",topCatJarPath};
				try {	
					Process spectColRun = Runtime.getRuntime().exec(command);
				}catch(Exception e) {
					System.out.println("Exception message: " + e.getMessage());
					e.printStackTrace();
				}
				//topcatRun
				//uk.ac.starlink.topcat.Driver.main(arg);
				//uk.ac.starlink.topcat.Driver.setStandalone(false);
				
				// return map of output data, with empty index array as this is
				// the only and final result (this index parameter is used if
				// pipelining output)
				callback.receiveResult(outputs, new int[0]);
			}
		});
	}

	@Override
	public SpectColActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
