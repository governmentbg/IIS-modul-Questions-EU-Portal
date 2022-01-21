package com.ib.euroact.migration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.aspose.words.CompositeNode;
import com.aspose.words.Document;
import com.aspose.words.ImportFormatMode;
import com.aspose.words.Node;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.Run;
import com.aspose.words.SaveFormat;
import com.ib.system.utils.FileUtils;

public class TestBoldItalic {
	
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testIt() {
		
		try {
			
			byte[] byteLic = FileUtils.getBytesFromFile(new File("D:\\Bullshit.iv"));
			ByteArrayInputStream bisL = new ByteArrayInputStream(byteLic);
				
			com.aspose.words.License license = new com.aspose.words.License();
			license.setLicense(bisL);
			
			byte[] bytes = FileUtils.getBytesFromFile(new File("D:\\_HARMONIZACIA\\format.docx"));
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			Document doc = new Document(bis);			
			
			NodeCollection<Paragraph> pars = ((CompositeNode<Node>) doc).getChildNodes(NodeType.PARAGRAPH, true);
			
			for (Paragraph curNode : pars) {
				
				//System.out.println(curNode.getChildNodes().getCount());
				System.out.println("ПАРАГРАФ: " + curNode.getText());
				
				NodeCollection<Node> nodes = curNode.getChildNodes();
				for (Node n : nodes) {
					if (n.getNodeType() == NodeType.RUN ) {						
						Run r = (Run)n;
						System.out.println(r.getText() + "\t-->\t BOLD:" + r.getFont().getBold() + "\tITALIC:" + r.getFont().getItalic() + "\t UNDERLINE:" + r.getFont().getUnderline());
					}else {
						System.out.println("TIP:" + n.getNodeType());
					}
				}
				
			}
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
	}
	
	
	

}
