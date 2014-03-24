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

import java.net.URL;
import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

import org.vamdc.taverna.vamdc_taverna_suite.common.TapQueryHelperData;

public class TapXSamsQueryHelperActivity extends
		AbstractAsynchronousActivity<TapXSamsQueryHelperActivityConfigurationBean>
		implements AsynchronousActivity<TapXSamsQueryHelperActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	
	private TapXSamsQueryHelperActivityConfigurationBean configBean;

	@Override
	public void configure(TapXSamsQueryHelperActivityConfigurationBean configBean)
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
		if(this.configBean == null) {
			System.out.println("configbean is null in queryhelperconst");
		}else {
			System.out.println("good: configbean is not null in queryhelperconst");
		}

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
		if(configBean.getRestrictAble().equals("URL")) {
			addOutput("URLForTapXSAMSQuery", 0);
		}else {
			describeTapQueryHelperInputs();
			describeTapQueryHelperOutputs();
		}
	}
	
	private void describeTapQueryHelperInputs()  {
		addInput("Value",0,true,null,String.class);		
		addInput("Operator",0,true,null,String.class);
		addInput("Optional Restricable Prepend",0,true,null,String.class);
	}
	
	
	private void describeTapQueryHelperOutputs(){	
		addOutput("QueryString", 0);
		//addOutput("URLForTapXSAMSQuery", 0);
		
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
			
				String where = "";
				String []tmp;
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();

				System.out.println("url ref: " + configBean.getTapQueryHelperData().getURL().trim());
				T2Reference urlRef = referenceService.register(configBean.getTapQueryHelperData().getURL().trim(), 0, true, context);
				outputs.put("URLForTapXSAMSQuery", urlRef);

				if(!configBean.getRestrictAble().equals("URL")) {
					String value = (String) referenceService.renderIdentifier(inputs.get("Value"), String.class, context);
					String operator = (String) referenceService.renderIdentifier(inputs.get("Operator"), String.class, context);
					String optPrepend = "";
					if(inputs.containsKey("Optional Restricable Prepend")) {
						optPrepend = (String) referenceService.renderIdentifier(inputs.get("Optional Restricable Prepend"), String.class, context);
					}
					if(configBean == null) {
						System.out.println("2configbean is null in queryhelperconst");
					}else {
						System.out.println("2good: configbean is not null in queryhelperconst");
					}
					try {
						String whereStr = "SELECT ALL WHERE " + optPrepend + configBean.getRestrictAble() + " " + operator + value + " ";
						T2Reference simpleRef = referenceService.register(whereStr, 0, true, context);
						outputs.put("QueryString", simpleRef);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				// the only and final result (this index parameter is used if
				// pipelining output)
				callback.receiveResult(outputs, new int[0]);
			}
		});
	}

	@Override
	public TapXSamsQueryHelperActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
