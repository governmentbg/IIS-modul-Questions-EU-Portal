package com.ib.euroact.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dao.EuroOtchetDAO;
import com.ib.euroact.db.dao.FilesDAO;
import com.ib.euroact.db.dao.ProgramaDAO;
import com.ib.euroact.db.dto.EuroOtchet;
import com.ib.euroact.db.dto.Files;
import com.ib.euroact.db.dto.PointPrograma;
import com.ib.euroact.db.dto.Programa;
import com.ib.euroact.search.ProgramaSearch;
import com.ib.euroact.system.SystemData;
import com.ib.system.ActiveUser;
import com.ib.system.db.JPA;
import com.ib.system.utils.FileUtils;

public class TestFilesDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestFilesDAO.class);

	private static SystemData sd;

	private static FilesDAO		dao;
	
	EuroOtchet otchet = null;
	
	
	/** @throws Exception */
	@BeforeClass
	public static void setUp() throws Exception {
		dao = new FilesDAO(ActiveUser.DEFAULT);
		sd = new SystemData();
	}
	
	
	
	@Test
	public void testLoadFileList() {
		try {
			
			List<Files> list = dao.selectByFileObject(6507, 83);
			System.out.println("FileList.size = " + list.size());
			
		} catch (Exception e) {			
			e.printStackTrace();
			fail();
		}
	}
	
	
	@Test
	public void testLoadAndSaveFile() {
		try {
			
			List<Files> list = dao.selectByFileObject(6507, 83);
			System.out.println("FileList.size = " + list.size());
			if (list.size() > 0) {
				Files f = dao.findById(list.get(0).getId());
				JPA.getUtil().runInTransaction(() -> dao.saveFile(f));
				
			}
			
			
		} catch (Exception e) {			
			e.printStackTrace();
			fail();
		}
	}
	
	
	@Test
	public void saveNewFile() {
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");			
			Files f = new Files();
			
			byte[] bytes = FileUtils.getBytesFromFile(new File("d:\\31087-1.jpg"));
			f.setContent(bytes);
			f.setFilename("pic"+sdf.format(new Date())+ ".jpg");
			f.setContentType("alabala");
			f.setIdObject(6507);
			f.setCodeObject(83);
			
			JPA.getUtil().runInTransaction(() -> dao.saveFile(f));
				
			
			
			
		} catch (Exception e) {			
			e.printStackTrace();
			fail();
		}
	}
	
	
	
}
