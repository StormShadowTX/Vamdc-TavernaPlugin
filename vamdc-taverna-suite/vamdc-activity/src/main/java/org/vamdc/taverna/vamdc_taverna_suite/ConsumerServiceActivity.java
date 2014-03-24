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

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.HeaderElement;


public class ConsumerServiceActivity extends
		AbstractAsynchronousActivity<ConsumerServiceActivityConfigurationBean>
		implements AsynchronousActivity<ConsumerServiceActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	
	private ConsumerServiceActivityConfigurationBean configBean;

	@Override
	public void configure(ConsumerServiceActivityConfigurationBean configBean)
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
		describeInputs();
		describeOutputs();

	}
	
	private void describeInputs()  {
		addInput("XSAMS URL",1,true,null,String.class);		
	}
	
	private void describeOutputs()  {
		addOutput("Results", 1);		
	}
	
	
		
	@SuppressWarnings("unchecked")
	@Override
	public void executeAsynch(final Map<String, T2Reference> inputs,
			final AsynchronousActivityCallback callback) {
		// Don't execute service directly now, request to be run ask to be run
		// from thread pool and return asynchronously
		callback.requestRun(new Runnable() {
			
			private String resultFromService(String xsamurl) {
				try {
					PostMethod post = new PostMethod(configBean.getURL());
					//post.setFollowRedirects(true);
					//post.setFollowRedirects(true);
			    	post.setParameter("url", xsamurl);
			    	
			    	 //
			    	HttpClient httpclient = new HttpClient();
					//post.setFollowRedirects(true);

			        int result = httpclient.executeMethod(post);
			        int statuscode = post.getStatusCode();
			        System.out.println(post.getResponseBodyAsString());
			        String redirectLocation;
			        Header hd = post.getResponseHeader("location");
			        if (hd != null) {
			            redirectLocation = hd.getValue();
			        } else {
			        	redirectLocation = "No Location Result Found";
			            // The response is invalid and did not provide the new location for
			            // the resource.  Report an error or possibly handle the response
			            // like a 404 Not Found error.
			        }
			        return redirectLocation;
			        /*
			         * 			        //Header []hd = post.getResponseHeaders();
			        for(int k = 0;k < hd.length;k++) {
			        	System.out.println("tostring: " + hd[k].toString() + " toext: " + hd[k].toExternalForm());
			        	HeaderElement []he = hd[k].getElements();
			        	if(he != null)
				        	for(int r = 0;r < he.length;r++) {
				        		System.out.println("he r: " + r + " tostring: " + he[r].toString());
				        		NameValuePair []nvp = he[r].getParameters();
				        		if(nvp != null)
					        		for(int p = 0;p < nvp.length;p++) {
					        			System.out.println("p: " + p + " name: " + nvp[p].getName() + nvp[p].getValue() + " and tostring: " + nvp[p].toString());
					        		}//for
				        	}//for
			        }//for
			        */
				}catch(Exception e) {
					e.printStackTrace();
					System.out.println("exc: " + e.toString());
				}
		         
				return "";
			}
			
			public void run() {
				InvocationContext context = callback.getContext();
				ReferenceService referenceService = context.getReferenceService();
				// Resolve inputs 
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();

	    		List<String> xsams_url = (List<String>)referenceService.renderIdentifier(inputs.get("XSAMS URL"), String.class, context);
			    List<String> resultFromService = new ArrayList();

	    		for(int j = 0;j < xsams_url.size();j++) {
	    			String res = xsams_url.get(j);
	    			
	    			System.out.println("call consumer service with url: " + res);
	    			try {
		    			String tmp = java.net.URLDecoder.decode(res,"UTF-8");
		    			if(tmp.equals(res)) {
		    				//not encoded
		    				res = java.net.URLEncoder.encode(res,"UTF-8");
		    			}
	    			}catch(java.io.UnsupportedEncodingException ue) {
	    				ue.printStackTrace();
	    			}
	    			System.out.println("call consumer service with url2: " + res);
	    			res = resultFromService(res);
	    			System.out.println("call consumer service with url3: " + res);
	    			resultFromService.add(res);
	    			
	    		}
	    		T2Reference simpleRef2 = referenceService.register(resultFromService , 1, true, context);
				outputs.put("Results", simpleRef2);
				
				// return map of output data, with empty index array as this is
				// the only and final result (this index parameter is used if
				// pipelining output)
				callback.receiveResult(outputs, new int[0]);
			}
		});
	}

	@Override
	public ConsumerServiceActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
