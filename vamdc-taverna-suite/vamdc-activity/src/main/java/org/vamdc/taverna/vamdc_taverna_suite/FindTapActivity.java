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

import eu.vamdc.registry.Registry;
import org.vamdc.taverna.vamdc_taverna_suite.common.RegistryUtil;

import org.w3c.dom.*;

import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class FindTapActivity extends
		AbstractAsynchronousActivity<FindTapActivityConfigurationBean>
		implements AsynchronousActivity<FindTapActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	
	private FindTapActivityConfigurationBean configBean;

	@Override
	public void configure(FindTapActivityConfigurationBean configBean)
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
		describeTapQueryHelperInputs();
		describeTapQueryHelperOutputs();

	}
	
	private void describeTapQueryHelperInputs()  {
		addInput("Optional Keywords",0,true,null,String.class);		
	}
	
	
	private void describeTapQueryHelperOutputs(){	
		addOutput("Identifiers", 1);
		addOutput("Description", 1);
		
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
				String keywords = null;
				
				if(inputs.containsKey("Optional Keywords")) {
					keywords = (String) referenceService.renderIdentifier(inputs.get("Optional Keywords"), String.class, context);
				}
				System.out.println("run for findtap called: keywords: " + keywords);
				
				if(keywords != null && keywords.trim().length() == 0) {
					keywords = null;
				}
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();

				try {
				Registry reg = RegistryUtil.getRegistry();
				System.out.println("XQuery: " + buildXQuery(keywords));
				Document doc = reg.executeXquery(buildXQuery(keywords));
				if(doc == null) {
					System.out.println("doc was null from executexquery");
				}
				System.out.println("localname: " + doc.getDocumentElement().getLocalName());
				System.out.println("xml result: " + xmlToString(doc));
				NodeList nl = doc.getElementsByTagName("ResourceInfo");
				System.out.println("Number of ResourceInfo: " + nl.getLength());
				List<String> idents = new ArrayList<String>();
				List<String> desc = new ArrayList<String>();
				for(int i = 0;i < nl.getLength();i++) {
					String identDesc = xmlToString(((Element)nl.item(i)).getElementsByTagName("Identifier").item(0));
					String outputDescription = xmlToString(nl.item(i));
					idents.add(identDesc);
					desc.add(outputDescription);
				}
				// Resolve inputs 
	    		//queryString += "REQUEST=doQuery&LANG=VSS1&FORMAT=XSAMS&QUERY" + queryString;
				
					T2Reference identRef = referenceService.register(idents, 1, true, context);
					T2Reference descRef = referenceService.register(desc, 1, true, context);
					outputs.put("Identifiers", identRef);
					outputs.put("Description", descRef);
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				// return map of output data, with empty index array as this is
				// the only and final result (this index parameter is used if
				// pipelining output)
				callback.receiveResult(outputs, new int[0]);
			}
		});
	}
	
	public static String buildXQuery(String keywords) {
		String query = "for $x in //RootResource where ";
		if(keywords != null) {
			String []tmp = keywords.split(" ");
			for(int i = 0;i < tmp.length;i++) {
				query +=  "(matches($x/*,'" + tmp[i] + "','i'))";
				//if(i != tmp.length - 1) {
					query += " and ";
				//}//if
			}//for
		}//if
		query += " $x/capability[@standardID='ivo://vamdc/std/VAMDC-TAP'] and $x/@status='active'";
		query += " return <MyResults><ResourceInfo>";
		query += "<Identifier>{$x/identifier}</Identifier>";
		query += "<Description>{$x/content/description}</Description>";
		query += "<Returnables>{$x/capability[@standardID='ivo://vamdc/std/VAMDC-TAP']/returnable}</Returnables>";
		query += "</ResourceInfo></MyResults>";
		
		return query;
	}
	
	public static String xmlToString(Node node) {
        try {
            Source source = new DOMSource(node);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

	@Override
	public FindTapActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
