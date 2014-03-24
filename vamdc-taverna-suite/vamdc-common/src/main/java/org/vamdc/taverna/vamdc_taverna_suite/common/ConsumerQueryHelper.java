package org.vamdc.taverna.vamdc_taverna_suite.common;

import java.util.ArrayList;
import java.util.Collection;

import org.astrogrid.registry.RegistryException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.Hashtable;


import eu.vamdc.registry.Registry;

public class ConsumerQueryHelper {
	
	//public static String getRepositoryDir() {
	//	return ApplicationRuntime.getInstance().getLocalRepositoryDir().toString();
	//}
	
	public static QueryHelperData[] getQueryHelperInfo(Registry reggie) {
		try {
			//System.out.println("instantiating registry with ivoaid = " + ivoaID);
			
			String query = "for $x in //RootResource where $x/@status='active' and exists($x/capability[@standardID='ivo://vamdc/std/XSAMS-consumer'])";
			query += " return <MyResults><ResourceInfo>";
			query += "<Identifier>{$x/identifier}</Identifier>";
			query += "<titledata>{$x/title}</titledata>";
			query += "<url>{$x/capability[@standardID='ivo://vamdc/std/XSAMS-consumer']/interface/accessURL}</url>";

			query += "</ResourceInfo></MyResults>";
			System.out.println("xquery: " + query);
		    Document results = reggie.executeXquery(query);
		    NodeList nlRes = results.getDocumentElement().getElementsByTagName("ResourceInfo");
		    QueryHelperData []tq = new QueryHelperData[nlRes.getLength()];
		    for(int ii = 0;ii < nlRes.getLength();ii++) {
			    NodeList nl =  ((Element)nlRes.item(ii)).getElementsByTagName("title");			    
			    String title = null;
			    if(nl.getLength() > 0) {
			   		title = nl.item(0).getTextContent();
			   	}
			    
			    nl =  ((Element)nlRes.item(ii)).getElementsByTagName("identifier");			    
			    String ivoaID = null;
			    if(nl.getLength() > 0) {
			    	ivoaID = nl.item(0).getTextContent();
			   	}
			    
				nl =  ((Element)nlRes.item(ii)).getElementsByTagName("accessURL");
				String url = null;
				System.out.println("nl length for url: " + nl.getLength());
				if(nl.getLength() > 0) {
					for(int rr = 0;rr < nl.getLength();rr++) {
						if(nl.item(rr).getTextContent() != null &&
						   nl.item(rr).getTextContent().endsWith("service")) {
							url = nl.item(rr).getTextContent().trim();
						}
					}
			   	}
				tq[ii] = new QueryHelperData(title,ivoaID,url);
		    }
		   
		    return tq;

		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	
	
	
}