package com.ib.euroact.parsers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.DossierDAO;


import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;


public class ZipUtils {
	
private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtils.class);
	
public static HashMap<String, byte[]>  unZipIt(byte[] ziped) {
		
		int BUFF_SIZE = 4096;
		HashMap<String, byte[]> result = new HashMap<String, byte[]>();
		
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			//ZipFile zipFile = new ZipFile("D:\\APIPNR\\2018-07-16_11-04-12_COM(2018)535___eGreffe___Official_electronic_version_(Zip-file).zip");
			//byte[] bytes = "hellooo".getBytes();
			
			
			
					
			File f = File.createTempFile("file", ".zip");
			
			FileOutputStream fos = new FileOutputStream(f.getAbsolutePath());
			fos.write(ziped);
			fos.close();
			
			ZipFile zipFile = new ZipFile(f);
			
			List fileHeaderList = zipFile.getFileHeaders();
			for (int i = 0; i < fileHeaderList.size(); i++) {
				FileHeader fileHeader = (FileHeader)fileHeaderList.get(i);
				
				
				ZipInputStream is = zipFile.getInputStream(fileHeader);
				
				ByteArrayOutputStream  os = new ByteArrayOutputStream ();
				
				int readLen = -1;
				byte[] buff = new byte[BUFF_SIZE];
				
				try {
					//Loop until End of File and write the contents to the output stream
					while ((readLen = is.read(buff)) != -1) {
						os.write(buff, 0, readLen);
					}
					
					is.close();
					
					byte[] bytes= os.toByteArray();
					
					result.put(fileHeader.getFileName(), bytes);
										
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			f.delete();
			
			
			
						
		} catch (ZipException e) {
			e.printStackTrace();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
