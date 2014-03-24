package org.vamdc.taverna.vamdc_taverna_suite.common;

import java.util.ArrayList;
import java.util.Collection;

import org.astrogrid.registry.RegistryException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.Hashtable;


import eu.vamdc.registry.Registry;

public class TapQueryHelper {
	
	//public static String getRepositoryDir() {
	//	return ApplicationRuntime.getInstance().getLocalRepositoryDir().toString();
	//}
	

	public static TapQueryHelperData getQueryHelperInfo(String ivoaID, Registry reggie) {
		try {
			//System.out.println("instantiating registry with ivoaid = " + ivoaID);
			
			String query = "for $x in //RootResource where $x/@status='active' and $x/identifier='"+ ivoaID + "' ";
			query += " return <MyResults><ResourceInfo>";
			query += "<Identifier>{$x/identifier}</Identifier>";
			query += "<titledata>{$x/title}</titledata>";
			query += "<returnable>{$x/capability[@standardID='ivo://vamdc/std/VAMDC-TAP']/returnable}</returnable>";
			query += "<restrictabledata>{$x/capability[@standardID='ivo://vamdc/std/VAMDC-TAP']/restrictable}</restrictabledata>";
			query += "<returnable>{$x/capability[@standardID='ivo://vamdc/std/TAP-XSAMS']/returnable}</returnable>";
			query += "<restrictabledata>{$x/capability[@standardID='ivo://vamdc/std/TAP-XSAMS']/restrictable}</restrictabledata>";
			query += "<url>{$x/capability[@standardID='ivo://vamdc/std/VAMDC-TAP']/interface/accessURL}</url>";
			query += "<url>{$x/capability[@standardID='ivo://vamdc/std/TAP-XSAMS']/interface/accessURL}</url>";

			query += "</ResourceInfo></MyResults>";
			
		    Document results = reggie.executeXquery(query);
		    NodeList nl = results.getDocumentElement().getElementsByTagName("title");
		    NodeList nlReturn = results.getDocumentElement().getElementsByTagName("returnable");
		    NodeList nlRestrict = results.getDocumentElement().getElementsByTagName("restrictable");
		    
		    String title = null;
		    if(nl.getLength() > 0) {
		   		title = nl.item(0).getTextContent();
		   	}
			nl = results.getDocumentElement().getElementsByTagName("accessURL");
			String url = null;
			System.out.println("nl length for url: " + nl.getLength());
			if(nl.getLength() > 0) {
		   		url = nl.item(0).getTextContent().trim();
		   	}
			System.out.println("nl length for accessurl: " + nl.getLength());
			String []returnAbles = new String[nlReturn.getLength()];
			for(int i = 0;i < nlReturn.getLength();i++) {
				returnAbles[i] = nlReturn.item(i).getTextContent();
			}
			
			String []restrictAbles = new String[nlRestrict.getLength()];
			for(int i = 0;i < nlRestrict.getLength();i++) {
				restrictAbles[i] = nlRestrict.item(i).getTextContent();
			}
		   
		    return new TapQueryHelperData(title,returnAbles,restrictAbles,ivoaID,url);

		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static TapQueryHelperData[] getQueryHelperInfo(Registry reggie) {
		try {
			//System.out.println("instantiating registry with ivoaid = " + ivoaID);
			
			String query = "for $x in //RootResource where $x/@status='active' and exists($x/capability[@standardID='ivo://vamdc/std/VAMDC-TAP'])";
			query += " return <MyResults><ResourceInfo>";
			query += "<Identifier>{$x/identifier}</Identifier>";
			query += "<titledata>{$x/title}</titledata>";
			query += "<returnable>{$x/capability[@standardID='ivo://vamdc/std/VAMDC-TAP']/returnable}</returnable>";
			query += "<restrictabledata>{$x/capability[@standardID='ivo://vamdc/std/VAMDC-TAP']/restrictable}</restrictabledata>";
			query += "<returnable>{$x/capability[@standardID='ivo://vamdc/std/TAP-XSAMS']/returnable}</returnable>";
			query += "<restrictabledata>{$x/capability[@standardID='ivo://vamdc/std/TAP-XSAMS']/restrictable}</restrictabledata>";
			query += "<url>{$x/capability[@standardID='ivo://vamdc/std/VAMDC-TAP']/interface/accessURL}</url>";
			query += "<url>{$x/capability[@standardID='ivo://vamdc/std/TAP-XSAMS']/interface/accessURL}</url>";
			query += "</ResourceInfo></MyResults>";
			System.out.println("xquery: " + query);
		    Document results = reggie.executeXquery(query);
		    NodeList nlRes = results.getDocumentElement().getElementsByTagName("ResourceInfo");
		    TapQueryHelperData []tq = new TapQueryHelperData[nlRes.getLength()];
		    for(int ii = 0;ii < nlRes.getLength();ii++) {
			    NodeList nl = ((Element)nlRes.item(ii)).getElementsByTagName("title");
			    NodeList nlReturn =  ((Element)nlRes.item(ii)).getElementsByTagName("returnable");
			    NodeList nlRestrict =  ((Element)nlRes.item(ii)).getElementsByTagName("restrictable");
			    
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
			   		url = nl.item(0).getTextContent().trim();
			   	}
				System.out.println("nl length for accessurl: " + nl.getLength());
				String []returnAbles = new String[nlReturn.getLength()];
				for(int i = 0;i < nlReturn.getLength();i++) {
					returnAbles[i] = nlReturn.item(i).getTextContent();
				}
				
				String []restrictAbles = new String[nlRestrict.getLength()];
				for(int i = 0;i < nlRestrict.getLength();i++) {
					restrictAbles[i] = nlRestrict.item(i).getTextContent();
				}
				tq[ii] = new TapQueryHelperData(title,returnAbles,restrictAbles,ivoaID,url);
		    }
		   
		    return tq;

		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	
	
	
}