package org.vamdc.taverna.vamdc_taverna_suite.ui.serviceprovider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;

import org.vamdc.taverna.vamdc_taverna_suite.common.TapQueryHelperData;
import org.vamdc.taverna.vamdc_taverna_suite.common.TapQueryHelper;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Hashtable;
import org.vamdc.taverna.vamdc_taverna_suite.common.RegistryUtil;
import net.sf.taverna.raven.appconfig.ApplicationRuntime;
import eu.vamdc.registry.Registry;

public class TapXSamsQueryHelperServiceProvider implements ServiceDescriptionProvider {
	
	private static final URI providerId = URI
		.create("http://www.vamdc.org/taverna/plugin/suite");
	
	private static Hashtable<String,String> dictionaryDesc = new Hashtable<String,String>();
	
	private void loadDictionary() {
		if(dictionaryDesc.size() == 0) {
			dictionaryDesc.put("AtomInchi","The IUPAC International Chemical Identifier InChI");
			dictionaryDesc.put("AtomInchiKey","InChi key is hashed form of InChI name of an atom/ion/isotope");
			dictionaryDesc.put("AtomIonCharge","Atomic charge");
			dictionaryDesc.put("AtomMass","Atomic mass expressed in atomic mass units u.");
			dictionaryDesc.put("AtomMassNumber","Atomic mass number is the total number of protons and neutrons in atomic nucleus.");
			dictionaryDesc.put("AtomNuclearCharge","Atomic number");
			dictionaryDesc.put("AtomNuclearSpin","The total angular momentum of a nucleus.");
			dictionaryDesc.put("AtomStateCoupling","Coupling scheme used to describe the state.");
			dictionaryDesc.put("AtomStateEnergy","Level energy");
			dictionaryDesc.put("AtomStateIonizationEnergy","Ionization energy");
			dictionaryDesc.put("AtomStateKappa","Relativistic correction.");
			dictionaryDesc.put("AtomStateLandeFactor","Lande factor");
			dictionaryDesc.put("AtomStateMagneticQuantumNumber","Magnetic quantum number of a state, can be integer or half-integer, positive and negative.");
			dictionaryDesc.put("AtomStateParity","State parity. Can have values: 'even', 'odd' or 'undefined'");
			dictionaryDesc.put("AtomStatePolarizability","State polarizability.");
			dictionaryDesc.put("AtomStateQuantumDefect","Quantum defect correction.");
			dictionaryDesc.put("AtomSymbol","Atom");
			dictionaryDesc.put("CollisionCode","string or list of strings with 4-letter code describing process");
			dictionaryDesc.put("CollisionIAEACode","From the IAEA Classification of Processes, October 2003");
			dictionaryDesc.put("EnvironmentTemperature","Environment temperature");
			dictionaryDesc.put("EnvironmentTotalPressure","Environment total pressure");
			dictionaryDesc.put("MethodCategory","Method category.");
			dictionaryDesc.put("MoleculeChemicalName","Molecule name");
			dictionaryDesc.put("MoleculeIonCharge","Molecule ion charge");
			dictionaryDesc.put("MoleculeStateLifeTime","Lifetime");
			dictionaryDesc.put("MoleculeStateNuclearSpinIsomer","Nuclear spin isome");
			dictionaryDesc.put("MoleculeStateTotalStatisticalWeight","Total statistical weight");
			dictionaryDesc.put("MoleculeStoichiometricFormula","Molecular stoichiometric formula");
			dictionaryDesc.put("RadTransWavelength","Wavelength Units in 'A'");
		}
	}
	
	/**
	 * Do the actual search for services. Return using the callBack parameter.
	 */
	@SuppressWarnings("unchecked")
	public void findServiceDescriptionsAsync(
			FindServiceDescriptionsCallBack callBack) {
		// Use callback.status() for long-running searches
		// callBack.status("Resolving example services");
		loadDictionary();
		List<ServiceDescription> results = new ArrayList<ServiceDescription>();

		// FIXME: Implement the actual service search/lookup instead
		// of dummy for-loop
		
		try {
			/*
			Properties p = new Properties();
		  File homeDir = ApplicationRuntime.getInstance().getApplicationHomeDir();
          File userConf = new File(homeDir,"conf");
          boolean myceaListExists = false;
          if(userConf.exists()) {
                  File myceaList = new File(userConf,"my_cealist.properties");
                  if(myceaList.exists()) {
                          System.out.println("loaded from my_cealist.properties");
                          p.load(new FileInputStream(myceaList));
                          myceaListExists = true;
                  }
          }
          if(!myceaListExists) {
                  System.out.println("could not find a loaded my_cealist.properties file in the taverna.home/conf");
                  
                  p.setProperty("my.tapnodes.hitran", "ivo://vamdc/hitran");
                  p.setProperty("my.tapnodes.vald", "ivo://vamdc/vald/uu/django");
                  p.setProperty("my.tapnodes.basecol", "ivo://vamdc/basecol/tap-xsams");
                  p.setProperty("my.tapnodes.cdsd", "ivo://vamdc/cdsd_node");
                  p.setProperty("my.tapnodes.reims", "ivo://vamdc/reims-ethylene");
                  p.setProperty("my.tapnodes.chianti", "ivo://vamdc/chianti/django");
                  p.setProperty("my.tapnodes.udfa", "ivo://vamdc/UDFA");
                  p.setProperty("my.tapnodes.cdms", "ivo://vamdc/CDMS/Django");
                  p.setProperty("my.tapnodes.lund", "ivo://vamdc/lund");
                  p.setProperty("my.tapnodes.smpo", "ivo://vamdc/smpo-sample");
                  
          }
          */
         
			String val;
			Registry reggie =  RegistryUtil.getRegistry();
			
			
			//Iterator iter = p.values().iterator();
			//Enumeration propertyKeys = p.keys();
			
			Properties pr = new Properties();
			pr.setProperty("return.soapbody","false");
			pr.setProperty("registry.useCache","true");
			//do some looping through all the apps and interface beans.
			/*
			 while(propertyKeys.hasMoreElements()) {
				val = (String)propertyKeys.nextElement();
				if(val.startsWith("my.tapnodes")) {
					System.out.println("look up registry app: " + val);
					TapQueryHelperData thd = TapQueryHelper.getQueryHelperInfo(p.getProperty(val),reggie);
					//System.out.println("app interfaces found:" + ai.length);
					for(int i = 0;i < thd.getRestrictAbles().length;i++) {
						if(i == 0) {
							TapXSamsQueryHelperServiceDesc tapxsamsServiceCheat = new TapXSamsQueryHelperServiceDesc();
							tapxsamsServiceCheat.setTapQueryHelperData(thd);
							tapxsamsServiceCheat.setRestrictAble("URL");
							tapxsamsServiceCheat.setDescription("Can be used for TapXSams Service");
							results.add(tapxsamsServiceCheat);
						}
						TapXSamsQueryHelperServiceDesc tapxsamsService = new TapXSamsQueryHelperServiceDesc();
						tapxsamsService.setTapQueryHelperData(thd);
						System.out.println("adding tapxsamsnodeservice resticable: " + thd.getRestrictAbles()[i] + " i: " + i);
						tapxsamsService.setRestrictAble(thd.getRestrictAbles()[i]);
						if(dictionaryDesc.containsKey(thd.getRestrictAbles()[i])) {
							tapxsamsService.setDescription(dictionaryDesc.get(thd.getRestrictAbles()[i]));
						}else {
							//tapxsamsService.setDescription(thd.getTitle().trim());
						}
						results.add(tapxsamsService);
					}
					
				}
			}
			*/
			
						TapQueryHelperData []thd = TapQueryHelper.getQueryHelperInfo(reggie);
						for(int k = 0;k < thd.length;k++) {
							//System.out.println("app interfaces found:" + ai.length);
							for(int i = 0;i < thd[k].getRestrictAbles().length;i++) {
								if(i == 0) {
									TapXSamsQueryHelperServiceDesc tapxsamsServiceCheat = new TapXSamsQueryHelperServiceDesc();
									tapxsamsServiceCheat.setTapQueryHelperData(thd[k]);
									tapxsamsServiceCheat.setRestrictAble("URL");
									tapxsamsServiceCheat.setDescription("Can be used for TapXSams Service");
									results.add(tapxsamsServiceCheat);
								}
								TapXSamsQueryHelperServiceDesc tapxsamsService = new TapXSamsQueryHelperServiceDesc();
								tapxsamsService.setTapQueryHelperData(thd[k]);
								System.out.println("adding tapxsamsnodeservice resticable: " + thd[k].getRestrictAbles()[i] + " i: " + i);
								tapxsamsService.setRestrictAble(thd[k].getRestrictAbles()[i]);
								if(dictionaryDesc.containsKey(thd[k].getRestrictAbles()[i])) {
									tapxsamsService.setDescription(dictionaryDesc.get(thd[k].getRestrictAbles()[i]));
								}else {
									//tapxsamsService.setDescription(thd.getTitle().trim());
								}
								results.add(tapxsamsService);
							}
						}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
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
