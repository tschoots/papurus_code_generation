package com.maiastra;

import java.util.Iterator;
import java.util.List;

//import org.eclipse.core.runtime.CoreException; 
//import org.eclipse.core.runtime.FileLocator; 
//import org.eclipse.core.runtime.Platform; 
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil; 
//import org.osgi.framework.FrameworkUtil;

public class ReadUmlModel {

	public static void main(String[] args) {
		
		System.out.println("hallo hallo");
		
		
		String workingDirectory = System.getProperty("user.dir");
		String seperator = System.getProperty("file.separator");
		String modelPathURI = "file://" + workingDirectory + seperator + "model" + seperator + "model.uml";
		
		System.out.printf("model file : %s\n", modelPathURI);
		
		ResourceSet rSet = new ResourceSetImpl();
		rSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		rSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,  UMLResource.Factory.INSTANCE);
		//org.eclipse.emf.common.util.URI modelUri = URI.createURI("file:///home/tschoots/MAIASTRA_development/papyrus_java_ws/org.eclipse.uml2.examples.gettingstarted/ExtendedPO2.uml");
		org.eclipse.emf.common.util.URI modelUri = URI.createURI(modelPathURI);
		//java.net.URI modelUri = new java.net.URI("file:///home/tschoots/MAIASTRA_development/papyrus_java_ws/org.eclipse.uml2.examples.gettingstarted/ExtendedPO2.uml");
		UMLResource r = (UMLResource) rSet.getResource(modelUri, true);
		EcoreUtil.resolveAll(r);
		UMLResourcesUtil.init(rSet);
		
		System.out.println(r.getEncoding());
		System.out.println(r.getXMINamespace());
		Model umlModel = (Model) r.getContents().get(0);
		
		System.out.println(umlModel.getName());

		List<Profile> appliedProfiles = umlModel.getAllAppliedProfiles();
		for (Profile p: appliedProfiles){
			System.out.printf("applied profile : %s\n", p.getName());
		}

		Iterator<Element> it = umlModel.getOwnedElements().iterator();
		while(it.hasNext()) {
			Element el = it.next();
			processElement(el);
		}
		
		System.out.println("the end");

	}

	private static void processElement(Element el) {
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
				System.out.printf("\toperation : %s\n", o.getName());
			}				

			List <Stereotype> sl = cl.getAppliedStereotypes();
			for (Stereotype s : sl){
				System.out.printf("\tstereotype : %s\n", s.getName());
				List <Property> al = s.getOwnedAttributes();
				for (Property a : al){
					System.out.printf("\tstereotype property : (%s:%s)\n", a.getName(), cl.getValue(s, a.getName()));
				}
			}
		}
		
		if (el instanceof Package) {
			Package p = (Package) el;
			System.out.printf("package : %s\n", p.getName());
			Iterator<Element> it = p.getOwnedElements().iterator();
			while(it.hasNext()) {
				Element subEl = it.next();
				processElement(subEl);
			}			
		}
	}

}
