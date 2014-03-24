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

public class TapXSamsActivity extends
		AbstractAsynchronousActivity<TapXSamsActivityConfigurationBean>
		implements AsynchronousActivity<TapXSamsActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	
	private TapXSamsActivityConfigurationBean configBean;

	@Override
	public void configure(TapXSamsActivityConfigurationBean configBean)
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
		removeOutputs();
		// FIXME: Replace with your input and output port definitions
		describeTapInputs();
		describeTapOutputs();

	}
	
	private void describeTapInputs()  {
		addInput("TAP URL or Registry Ivorn",1,true,null,String.class);
		addInput("Lang",0,true,null,String.class);
		addInput("Query",0,true,null,String.class);
		addInput("Format",0,true,null,String.class);
		addInput("Reference",0,true,null,Boolean.class);
	}
	
	
	private void describeTapOutputs(){	
		addOutput("XSamsResult", 1);		
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

				Set<String> inputKeys = inputs.keySet();
	    		String []tmpKeys = inputKeys.toArray(new String[0]);
	    		for(int k = 0;k < tmpKeys.length;k++) {
	    			System.out.println("keyset k = " + k + " value = " + tmpKeys[k]);
	    		}

				
				String queryString = (String) referenceService.renderIdentifier(inputs.get("Query"), String.class, context);
				String formatString = (String) referenceService.renderIdentifier(inputs.get("Format"), String.class, context);
				String langString = (String) referenceService.renderIdentifier(inputs.get("Lang"), String.class, context);
	    		System.out.println("querystr = " + queryString +  " and format = " + formatString);
	    		Boolean ref = (Boolean) referenceService.renderIdentifier(inputs.get("Reference"), Boolean.class, context);
	    		System.out.println("ref = " + ref);
	    		List<String> url_ivorn = (List<String>)referenceService.renderIdentifier(inputs.get("TAP URL or Registry Ivorn"), String.class, context);
	    		System.out.println("urlivorn length = " + url_ivorn.size());
			    List<String> resultXSams = new ArrayList();

			    String urlQuery = "";
	    		for(int j = 0;j < url_ivorn.size();j++) {
	    			boolean error = false;
	    			String testLook = url_ivorn.get(j);
	    			System.out.println("testlook: " + testLook);
	    			urlQuery="http://www.test.com/test.jsp";
	    			//System.out.println("SIZE OF STRING ARRAY: "  + testLook.length);
	    			
		    		if(url_ivorn.get(j).startsWith("ivo")) {
		    			TapQueryHelperData td = TapQueryHelper.getQueryHelperInfo(url_ivorn.get(j),RegistryUtil.getRegistry());
		    			if(td != null) {
		    				urlQuery = td.getURL();
		    			}else {
		    				//throw something could not find a tap ivorn
		    			}
		    			
		    			if(url_ivorn == null || url_ivorn.size() == 0) {
		    				//thorw something could not discover a url from ivorn given.
		    			}
		    		}else if(url_ivorn.get(j).startsWith("http")) {
		    			urlQuery = url_ivorn.get(j);
		    		}else {
		    			urlQuery = "MUST HAVE http:// or ivo:// for URL/IVORN parameter";
		    			error = true;
		    			ref = true;
		    		}
		    		
		    		if(urlQuery != null && !error && !urlQuery.endsWith("sync") ) {
	    				if(!urlQuery.endsWith("sync/") ) {
		    				if(!urlQuery.endsWith("/")) {
		    					urlQuery +=  "/sync";
		    				}else {
		    					urlQuery +=  "sync";
		    				}//else
	    				}//if
	    			}//if
		    		
					try {
	
						if(!error) {
							urlQuery += "?REQUEST=doQuery&LANG=" + langString + "&FORMAT=" + formatString + "&QUERY=" + java.net.URLEncoder.encode(queryString,"UTF-8");
						}
					    System.out.println("Resulting URL:  " + urlQuery.toString().toString());

						if(ref.equals(true)) {
							System.out.println("ok adding urlquery, ref true");
							resultXSams.add(urlQuery.toString());
							//T2Reference simpleRef = referenceService.register(url_ivorn.toString(), 1, true, context);
							//outputs.put("XSamsResult", simpleRef);
						}else {
							System.out.println("ref false so grabbign votable.");
						    StringWriter resultOutput = new StringWriter();
						    TransformerFactory.newInstance().newTransformer().transform(new StreamSource(new URL(urlQuery.toString()).openStream()), new StreamResult(resultOutput));
						    resultXSams.add(resultOutput.toString());
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
	    		}//for
	    		
				T2Reference simpleRef2 = referenceService.register(resultXSams , 1, true, context);
				outputs.put("XSamsResult", simpleRef2);
				
				// return map of output data, with empty index array as this is
				// the only and final result (this index parameter is used if
				// pipelining output)
				callback.receiveResult(outputs, new int[0]);
			}
		});
	}

	@Override
	public TapXSamsActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
