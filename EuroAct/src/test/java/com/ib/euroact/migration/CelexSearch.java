package com.ib.euroact.migration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ib.euroact.db.dao.EuroActNewDAO;
import com.ib.euroact.db.dto.EuroActNew;
import com.ib.system.ActiveUser;

//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;

import com.ib.system.db.JPA;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.SearchUtils;

public class CelexSearch {

	public static void main(String[] args) {
		
		HashMap<Integer, String> numbers = new HashMap<Integer, String>();
		System.setProperty("webdriver.gecko.driver", "D:/Java/geckodriver/geckodriver.exe");
		WebDriver driver = new FirefoxDriver();
		
		ArrayList<Object[]> rows = (ArrayList<Object[]>) JPA.getUtil().getEntityManager().createNativeQuery("select id, URL from EURO_ACT_NEW where url is not null and CELEX is null").getResultList();
		//ArrayList<Object[]> rows = (ArrayList<Object[]>) JPA.getUtil().getEntityManager().createNativeQuery("select id, URL, celex from EURO_ACT_NEW where url is not null").getResultList();
		System.out.println(rows.size());
		int tek = 1;
		for (Object[] row : rows) {
			String url = SearchUtils.asString(row[1]);
			Integer id = SearchUtils.asInteger(row[0]);
			//String celex = SearchUtils.asString(row[2]);
			
			url = url.replace(" ",  "");
			
			System.out.println("Обработка на " + tek + " от " + rows.size());
			tek++;
			EuroActNew act = new EuroActNew();
			act.setUrl(url);		
			//new EuroActNewDAO(ActiveUser.DEFAULT).proccessUrl(act);
			//System.out.println(url + "\t\t" + act.getCelex() +"<-->"+ celex);
			
		
			
			driver.get(url);	
			try {
				WebElement el = driver.findElement(By.className("DocumentTitle"));
				String celex = el.getText().replace("Document ", ""); 
				System.out.println(url + "\t--->\t" + celex);
				
				try {
					
					JPA.getUtil().begin();
					String sql = "update EURO_ACT_NEW set CELEX = '"+celex+"' where id = " + id;
					//System.out.println(sql);
					JPA.getUtil().getEntityManager().createNativeQuery(sql).executeUpdate();
					JPA.getUtil().commit();		
				} catch (DbErrorException e) {
					JPA.getUtil().rollback();
					e.printStackTrace();
				}finally {
					JPA.getUtil().closeConnection();
				}
				
				
				
			}catch (Exception e) {
				System.out.println("URL: " + url);
				e.printStackTrace();
			}
			
		}
		
		
		
		
		
		
		
		
	}

}
