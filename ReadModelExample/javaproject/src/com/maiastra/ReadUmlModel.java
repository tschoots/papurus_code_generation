

package com.maiastra;

import java.io.File; 
import java.io.FilenameFilter; 
import java.io.IOException; 
import java.net.URISyntaxException; 
import java.net.URL; 
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.Iterator;
import java.util.List; 
import java.util.Map; 
import java.util.Properties; 
 
//import org.eclipse.core.runtime.CoreException; 
//import org.eclipse.core.runtime.FileLocator; 
//import org.eclipse.core.runtime.Platform; 
import org.eclipse.emf.common.util.URI; 
import org.eclipse.emf.ecore.EAttribute; 
import org.eclipse.emf.ecore.EClass; 
import org.eclipse.emf.ecore.EOperation; 
import org.eclipse.emf.ecore.EPackage; 
import org.eclipse.emf.ecore.resource.Resource; 
import org.eclipse.emf.ecore.resource.ResourceSet; 
import org.eclipse.emf.ecore.resource.URIConverter; 
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl; 
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage; 
import org.eclipse.uml2.uml.resource.UMLResource; 
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil; 
//import org.osgi.framework.FrameworkUtil;

public class ReadUmlModel {

	public static void main(String[] args) {
		
		System.out.println("hallo hallo");
		
		ResourceSet rSet = new ResourceSetImpl();
		rSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		rSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,  UMLResource.Factory.INSTANCE);
		org.eclipse.emf.common.util.URI modelUri = URI.createURI("file:///home/tschoots/MAIASTRA_development/papyrus_java_ws/org.eclipse.uml2.examples.gettingstarted/ExtendedPO2.uml");
		//java.net.URI modelUri = new java.net.URI("file:///home/tschoots/MAIASTRA_development/papyrus_java_ws/org.eclipse.uml2.examples.gettingstarted/ExtendedPO2.uml");
		UMLResource r = (UMLResource) rSet.getResource(modelUri, true);
		EcoreUtil.resolveAll(r);
		UMLResourcesUtil.init(rSet);
		
		System.out.println(r.getEncoding());
		System.out.println(r.getXMINamespace());
		Model umlModel = (Model) r.getContents().get(0);
		
		
		
		Iterator<Element> it = umlModel.getOwnedElements().iterator();
		while(it.hasNext()){
			Element el = it.next();
			if (el instanceof Class) {
				Class cl = (Class) el;
				System.out.printf("class : %s\n", cl.getName());
				System.out.printf("\tin package : %s\n", cl.getPackage().getName());
				
				List<Property> pl = cl.getAllAttributes();
				for (Property p : pl){
					System.out.printf("\tattribute : %s\n", p.getName());
				}
				
				List<Operation> ol = cl.getAllOperations();
				for (Operation o : ol){
					System.out.printf("operation : %s\n", o.getName());
				}
				
				
			}
		}
		
		System.out.println(umlModel.getName());
		
		
		System.out.println("the end");


	}

}
