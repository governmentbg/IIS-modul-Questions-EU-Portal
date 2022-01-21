package com.ib.euroact.migration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.ib.system.exceptions.InvalidParameterException;
import com.ib.system.utils.FileUtils;
import com.itextpdf.styledxmlparser.jsoup.Jsoup;

public class ProccessURLs {

	public static void main(String[] args) throws InterruptedException {

		int i = 1;
		try {
			byte[] bytes = FileUtils.getBytesFromFile(new File("D:\\_HARMONIZACIA\\help\\link.txt"));
			String[] urls = new String(bytes).split("\r\n");
			System.out.println(urls.length);
			String newString = "";
			for (String url : urls) {
				String html = "";
				try {
					com.itextpdf.styledxmlparser.jsoup.nodes.Document doc;
					doc = Jsoup.parse(readHttpInfo(url));
					html = doc.html();									
				} catch (Exception e) {
					System.out.println("Error in URL: " + url);
				}
				String fileName = "D:\\_HARMONIZACIA\\help\\"+i+".html";
				FileUtils.writeBytesToFile(fileName, html.getBytes());
				newString += url + "\t" +  fileName + "\r\n";
				System.out.println(fileName);
				i++;
				Thread.sleep(1000);	
				
			}
			FileUtils.writeBytesToFile("D:\\_HARMONIZACIA\\help\\linkFull.txt", newString.getBytes());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static String readHttpInfo(String address) throws IOException{
		
		String result = "";
		URL url = new URL(address);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		
		InputStream is = connection.getInputStream();
		String newUrl = connection.getURL().toString();
		newUrl = newUrl.replace("http:", "https:");
		URL url2 = new URL(newUrl);
		System.out.println(address + " -->" + newUrl);
		HttpURLConnection connection2 = (HttpURLConnection)url2.openConnection();
		connection2.setRequestMethod("GET");
		connection2.connect();
		InputStream is2 = connection2.getInputStream();
		
		byte[] buffer = new byte[1024];
		int read;
		while ((read = is2.read(buffer)) != -1) {
			String tek = new String(buffer, 0, read);
			result += tek;
			
		}		
		
		is.close();
		connection.disconnect();
		
		return result;
	
	}

}
