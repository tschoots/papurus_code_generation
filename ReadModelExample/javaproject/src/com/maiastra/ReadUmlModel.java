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
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
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

	private static void iterateElement(Element elem , String identString) {
		Iterator<Element> it = elem.getOwnedElements().iterator();
		while (it.hasNext()) {
			Element el = it.next();
			if (el instanceof Class) {
				Class cl = (Class) el;
				System.out.printf("%sclass : %s\n", identString, cl.getName());
				System.out.printf("%s\tin package : %s\n", identString, cl.getPackage().getName());

				List<Property> pl = cl.getAllAttributes();
				for (Property p : pl) {
					System.out.printf("%s\tattribute : %s\n", identString, p.getName());
				}

				List<Operation> ol = cl.getAllOperations();
				for (Operation o : ol) {
					System.out.printf("%s\toperation : %s\n", identString, o.getName());
				}
				List<Stereotype> st = cl.getAppliedStereotypes();
				for (Stereotype ster : st) {
					System.out.printf("%s\tapplied stereotype : %s\n", identString,  ster.getQualifiedName());
				}
				
			}

			if (el instanceof Package) {
				Package p = (Package) el;
				System.out.printf("%spackage : %s\n", identString, p.getName());
				iterateElement(el, new StringBuilder().append(identString).append("\t").toString());

			}
			
			if (el instanceof Association) {
				Association a = (Association) el;
//				List<Property> ends = a.getOwnedEnds();
//				
//				System.out.printf("%snumber of ends : %d\n", identString, ends.size());
//				for (Property p : ends){
//					System.out.printf("%sässoc end %s\n",  identString, p.getName());
//				}
				List<Property> mems = a.getMemberEnds();
				System.out.printf("%sAssociation number of members %s\n", identString, mems.size());
				for (Property mem : mems){
					System.out.printf("%s\tmember end: %s\n", identString, mem.getName());
					if (mem.isNavigable()){
						System.out.printf("%s\tisnavigable.\n", identString);
					}else{
						System.out.printf("%s\tisNOTnavigable.\n", identString);
					}
					AggregationKind ak = mem.getAggregation();
					System.out.printf("%s\tagregation : %s\n", identString,  ak.getName());
					System.out.printf("%s\towner : %s\n",identString,  mem.getOwner().getClass().getName());
					if (mem.isMultivalued()){						
						System.out.printf("%s\tmultivalued\n", identString);
						
					}
					System.out.printf("%s\tlower : %d\n", identString, mem.getLower());
					System.out.printf("%s\tupper : %d\n", identString, mem.getUpper());
					
					System.out.println("");
				}
				System.out.println("\n");
			}
			
			if (el instanceof Dependency){
				System.out.printf("%sdepenency : ", identString);
				Dependency d = (Dependency)el;
				List<NamedElement> nelems =  d.getClients();
				for (NamedElement nelem : nelems) {
					System.out.printf("%sclient : %s\n", identString,  nelem.getQualifiedName());
				}
				List<NamedElement> suplElems = d.getSuppliers();
				for (NamedElement suplElem : suplElems){
					System.out.printf("%ssupplier : ", identString, suplElem.getQualifiedName());
				}
			}
			
			System.out.printf("***** type: %s\n", el.getClass().getName());

		}

	}

	public static void main(String[] args) {

		System.out.println("hallo hallo");

		String workingDirectory = System.getProperty("user.dir");
		String seperator = System.getProperty("file.separator");
		String modelPathURI = "file://" + workingDirectory + seperator + "model" + seperator + "model.uml";

		System.out.printf("model file : %s\n", modelPathURI);

		ResourceSet rSet = new ResourceSetImpl();
		rSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		rSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
		// org.eclipse.emf.common.util.URI modelUri =
		// URI.createURI("file:///home/tschoots/MAIASTRA_development/papyrus_java_ws/org.eclipse.uml2.examples.gettingstarted/ExtendedPO2.uml");
		org.eclipse.emf.common.util.URI modelUri = URI.createURI(modelPathURI);
		// java.net.URI modelUri = new
		// java.net.URI("file:///home/tschoots/MAIASTRA_development/papyrus_java_ws/org.eclipse.uml2.examples.gettingstarted/ExtendedPO2.uml");
		UMLResource r = (UMLResource) rSet.getResource(modelUri, true);
		EcoreUtil.resolveAll(r);
		UMLResourcesUtil.init(rSet);

		System.out.println(r.getEncoding());
		System.out.println(r.getXMINamespace());
		Model umlModel = (Model) r.getContents().get(0);

		System.out.println(umlModel.getName());

		List<Profile> appliedProfiles = umlModel.getAllAppliedProfiles();
		for (Profile p : appliedProfiles) {
			System.out.printf("applied profile : %s\n", p.getName());
		}

		// iterate over the model elements

//		Iterator<Element> it = umlModel.getOwnedElements().iterator();
//		while (it.hasNext()) {
//			Element el = it.next();
//			if (el instanceof Class) {
//				Class cl = (Class) el;
//				System.out.printf("class : %s\n", cl.getName());
//				System.out.printf("\tin package : %s\n", cl.getPackage().getName());
//
//				List<Property> pl = cl.getAllAttributes();
//				for (Property p : pl) {
//					System.out.printf("\tattribute : %s\n", p.getName());
//				}
//
//				List<Operation> ol = cl.getAllOperations();
//				for (Operation o : ol) {
//					System.out.printf("\toperation : %s\n", o.getName());
//				}
//				List<Stereotype> st = cl.getAppliedStereotypes();
//				for (Stereotype ster : st) {
//					System.out.printf("\tapplied stereotype : %s\n", ster.getQualifiedName());
//				}
//			}
//
//			if (el instanceof Package) {
//				Package p = (Package) el;
//				System.out.printf("package : %s\n", p.getName());
//
//			}
//
//		}
	
	
		
		iterateElement(umlModel, "");

		System.out.println("the end");

	}

}
