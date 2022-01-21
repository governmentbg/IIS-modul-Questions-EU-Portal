package com.ib.euroact.db.dao;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.PointPrograma;
import com.ib.euroact.db.dto.Programa;
import com.ib.euroact.system.EuroDAO;
import com.ib.system.ActiveUser;
import com.ib.system.exceptions.DbErrorException;

public class ProgramaDAO extends EuroDAO<Programa> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProgramaDAO.class);
	
	
	
	public ProgramaDAO(ActiveUser user){
		super(user);		
	}
	
	
	public List<PointPrograma> findProgramaPoints(Integer programaId) throws DbErrorException {
		
		try {
			
			
			Query query = createQuery("from PointPrograma where idPrograma = :pid order by pored")
				.setParameter("pid", programaId);
						

			@SuppressWarnings("unchecked")
			List<PointPrograma> result = query.getResultList();
			
			return result;	
			
		} catch (Exception e) {
			throw new DbErrorException("Грешка при търсене на точки от програма!", e);
		}
	
	}	
	
	
	public boolean findDubl(Programa programa) throws DbErrorException {
		
		try {
			
			String sql = "select id from EURO_PROGRAMA where godina = :god";
			if (programa.getId() != null) {
				sql += " and id <> :pid";
			}
			
			
			Query query = createNativeQuery(sql).setParameter("god", programa.getGodina());
			
			if (programa.getId() != null) {
				query.setParameter("pid", programa.getId());
			}
			
			
			List<Integer> result = query.getResultList();
			if (result.size() > 0) {
				return true;
			}else {
				return false;
			}
			
			
			
		} catch (Exception e) {
			throw new DbErrorException("Грешка при търсене на точки от програма!", e);
		}
	
	}	
	
	
	
	
}
