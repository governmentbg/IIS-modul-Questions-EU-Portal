package com.ib.euroact.db.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.EuroOtchet;
import com.ib.euroact.db.dto.Files;
import com.ib.euroact.system.EuroDAO;
import com.ib.system.ActiveUser;
import com.ib.system.db.AbstractDAO;
import com.ib.system.db.JPA;
import com.ib.system.db.PersistentEntity;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.exceptions.ObjectInUseException;

/**
 * DAO for {@link Files}
 *
 * @author belev
 */
public class FilesDAO extends EuroDAO<Files> {

	
	

	private static final String SELECT_BY_FILE_OBJECT_SQL = //
		" select NEW Files(f.id, f.codeObject, f.idObject, f.filename, f.contentType, f.rolia, 	f.vidDoc) " //
			+ " from Files f " //			
			+ " where f.idObject = :objectIdArg and f.codeObject = :objectCodeArg ";

	private static final Logger LOGGER = LoggerFactory.getLogger(FilesDAO.class);

	/** @param user */
	public FilesDAO(ActiveUser user) {
		super(Files.class, user);
	}

	/**
	 * Изтрива връзката на файла с обекта (таблица FILE_OBJECTS). Ако файла не се използва от други обекти се трие и той.
	 *
	 * @param files
	 * @throws DbErrorException
	 * @throws ObjectInUseException
	 */
	public void deleteFile(Files files) throws DbErrorException, ObjectInUseException {
		if (files.getId() == null) {
			return; // файлът все още не е записан. най-вероятно autoSave=false
		}
		super.delete(files);		
	}

	

	

	/**
	 * Запис на файла като го свърза с обекта чрез таблица FILE_OBJECTS
	 *
	 * @param entity
	 * @param objectId
	 * @param objectCode
	 * @return
	 * @throws DbErrorException
	 */
	public Files saveFile(Files entity) throws DbErrorException {
		
		if (entity.getId() == null) { // само за прикачане на нов файл, иначе ще направи само връзка
			super.save(entity);

			JPA.getUtil().getEntityManager().flush();
		}

		return entity;
	}

	

	/**
	 * Намира данни за файлове по обекта, към койта са.
	 *
	 * @param objectId
	 * @param objectCode
	 * @return
	 * @throws DbErrorException
	 */
	public List<Files> selectByFileObject(Integer objectId, Integer objectCode) throws DbErrorException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("selectByFileObject(objectId={},objectCode={})", objectId, objectCode);
		}
		try {
			TypedQuery<Files> typedQuery = getEntityManager().createQuery(SELECT_BY_FILE_OBJECT_SQL, Files.class) //
				.setParameter("objectIdArg", objectId).setParameter("objectCodeArg", objectCode);

			return typedQuery.getResultList();

		} catch (Exception e) {
			throw new DbErrorException("Грешка при търсене на файлове.", e);
		}
	}

	

	

	



	
	
	
	
}