package com.ib.euroact.migration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;

import com.ib.system.utils.FileUtils;

import eu2.*;

public class ParseTestKG {

	@Test
	public void testParse() {
	
		try {
			byte[] bytes = FileUtils.getBytesFromFile(new File("/home/krasi/Downloads/COM_2021_17_transmission_sheet.xml"));
			
//			byte[] bytes = FileUtils.getBytesFromFile(new File("D:\\_zip\\аааааа\\delme.xml"));
			
			 File xml = new File("/home/krasi/Downloads/COM_2021_17_transmission_sheet.xml");
			
			InputStream stream = new ByteArrayInputStream(bytes);
			JAXBContext jaxbContext = JAXBContext.newInstance("eu2");//TransmissionRequestType.class.getPackage().getName());

			
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//            System.out.println("=============="+TransmissionRequestType.class.getProtectionDomain().getCodeSource().getLocation().getPath());
//
//            
////            System.out.println("=== 222======="+jaxbContext.toString());
////            jaxbUnmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
//            
			JAXBElement<TransmissionRequestType> root = jaxbUnmarshaller.unmarshal(new StreamSource(stream), TransmissionRequestType.class);
			TransmissionRequestType trans = root.getValue();

//			JAXBElement<TransmissionRequestType> root =  jaxbUnmarshaller.unmarshal(xml);
//			JAXBElement<TransmissionRequestType> root = (JAXBElement<TransmissionRequestType>) jaxbUnmarshaller.unmarshal(xml);
//			TransmissionRequestType trans = root.getValue();
			
			List<WorkflowExtensionBaseType> extension = trans.getTransmission().getWorkflow().getExtension();
			System.out.println("111="+extension.size());
            System.out.println("2222="+(extension.get(0)==null));
            System.out.println("333="+(((WorkflowExtensionType)extension.get(0)).getWorkflowNumber()==null));
            System.out.println("444="+(((WorkflowExtensionType)extension.get(0)).getWorkflowNumber().getCode()==null));
            
			extractSignature(trans);
			
			//System.out.println((TWork)(trans.getTransmission().getWorkflow().getWorkOrWorkReference().get(О)).);
			
			System.out.println("---------------------");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
public static void extractSignature(TransmissionRequestType trans){
        
        String err = "";
        WorkflowType workflow = trans.getTransmission().getWorkflow();
        List<Object> works = workflow.getWorkOrWorkReference();
        boolean found = false;
        for (Object tekW : works){
        	
        	Long year = null;
        	String type = null;
        	Long pored = null;
        	if (tekW instanceof TWork ){
			   TWork temp = (TWork)tekW;
			   //System.out.println("--->"+temp.getIdentifierWork().getValue());
			   List<TWorkExtensionBase> allExt = temp.getExtension();
			   for (TWorkExtensionBase ext : allExt){
				   if (((WorkExtensionType)ext).getParentWorkReference() == null){
					   WorkNumberType num = ((WorkExtensionType)ext).getInternalNumber();
					   //System.out.println("....-......"+num);
					   //System.out.println("..........."+num.getYear());
					   year = new Long(num.getYear().getValue().longValue());
					   type = num.getCode().getValue();
					   pored = new Long(num.getNumber().getValue().longValue());
					   System.out.println("Pored: " + pored);
					   break;
				   }
			   }

			 }
        }
        
    }
//		
		
		
}
