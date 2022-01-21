package com.ib.euroact.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.system.BaseSystemData;
import com.ib.system.SysClassifAdapter;
import com.ib.system.db.JPA;
import com.ib.system.db.dto.SystemClassif;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.DateUtils;
import com.ib.system.utils.SearchUtils;

public class EuroActSysClassifAdapter extends SysClassifAdapter {
	
	
	// Индекс на спецификите на класификацията със законите
	
	/** Индекс на наименование на закон */
	public static final int	INDEX_ZAKONI_NAME			= 0;
	/** Индекс на брой на ДВ */
	public static final int	INDEX_ZAKONI_BR_DV			= 1;
	/** Индекс на година на ДВ*/
	public static final int	INDEX_ZAKONI_GOD_DV		= 2;
	/** Индекс на ID на закон */
	public static final int	INDEX_ZAKONI_ID		= 3;
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EuroActSysClassifAdapter.class);
	
	  protected EuroActSysClassifAdapter(BaseSystemData sd) {
		  super(sd);		
	   }
	  
	  
	  
	@SuppressWarnings("unchecked")
	public List<SystemClassif> buildClassEuroAct(Integer codeClassif, Integer lang) throws DbErrorException {
			LOGGER.debug("buildClassEuroAct(codeClassif={},lang={})", codeClassif, lang);
			
			List<SystemClassif> classif = new ArrayList<>();
			Date systemMinDate = DateUtils.systemMinDate();
					
			
			try {
				
				String sql = "select id, dbms_lob.substr(ime,8000) from EURO_ACT_NEW where ime is not null  and vid_act not in (select code from ADM_SYSTEM_CLASSIF where CODE_CLASSIF = 200 and CODE_PARENT = 30274)";
				List<Object[]> rows = JPA.getUtil().getEntityManager().createNativeQuery(sql) .getResultList();
				int codePrev = 0;
				int codeParent = 0;
				for (Object[] tek : rows) {
					SystemClassif item = new SystemClassif();
					item.setCode(SearchUtils.asInteger(tek[0]));
					item.setCodeClassif(codeClassif);
					item.setCodeParent(codeParent);
					item.setCodePrev(codePrev);
					item.setDateOt(systemMinDate);
					item.setId(item.getCode());
					item.setLevelNumber(1);
					item.setTekst(SearchUtils.asString(tek[1]));
										
					classif.add(item);
					codePrev = item.getCode();
				}
				
				
			
			} catch (Exception e) {
				e.printStackTrace();
				throw new DbErrorException(e);
			}

			return classif;
			
	  }
	  
	
	  // 
	@SuppressWarnings("unchecked")
	public List<SystemClassif> buildClassZakoni(Integer codeClassif, Integer lang) throws DbErrorException {
			LOGGER.debug("buildClassZakoni(codeClassif={},lang={})", codeClassif, lang);
			
			List<SystemClassif> classif = new ArrayList<>();
			Date systemMinDate = DateUtils.systemMinDate();
					
			
			try {
				
				String sql = "select distinct ZAKON_NAME_V, ZAKON_DV_BR_V, ZAKON_DV_GOD_V, ZAKON_ID_V  from EURO_DOSSIER union all select distinct ZAKON_NAME, null, null, ZAKON_ID  from EURO_DOSSIER";
				List<Object[]> rows = JPA.getUtil().getEntityManager().createNativeQuery(sql) .getResultList();
				int code = 0;
				int codePrev = 0;
				int codeParent = 0;
				for (Object[] tek : rows) {
					code++;
					SystemClassif item = new SystemClassif();
					item.setId(code);
					item.setCode(code);
					item.setCodeClassif(codeClassif);
					item.setCodeParent(codeParent);
					item.setCodePrev(codePrev);
					item.setDateOt(systemMinDate);
					item.setLevelNumber(1);
					
					String name = SearchUtils.asString(tek[0]);
					Integer brDv = SearchUtils.asInteger(tek[1]);
					Integer godDv = SearchUtils.asInteger(tek[2]);
					Integer zid = SearchUtils.asInteger(tek[3]);
					if (brDv != null) {
						item.setTekst(name + "(ДВ, бр. "+brDv+" от "+godDv+" г.)" );
						
					}else{
						item.setTekst(name);	
					}
					
					
					item.setSpecifics(new Object[] { name , brDv, godDv, zid});
					
										
					classif.add(item);
					codePrev = item.getCode();
				}
				
				
			
			} catch (Exception e) {
				e.printStackTrace();
				throw new DbErrorException(e);
			}

			return classif;
			
	  }
	
	
	@SuppressWarnings("unchecked")
	public List<SystemClassif> buildClassSadAct(Integer codeClassif, Integer lang) throws DbErrorException {
			LOGGER.debug("buildClassEuroAct(codeClassif={},lang={})", codeClassif, lang);
			
			List<SystemClassif> classif = new ArrayList<>();
			Date systemMinDate = DateUtils.systemMinDate();
					
			
			try {
				
				String sql = "select id, dbms_lob.substr(ime,8000) from EURO_ACT_NEW where ime is not null and vid_act in (select code from ADM_SYSTEM_CLASSIF where CODE_CLASSIF = 200 and CODE_PARENT = 30274)";
				List<Object[]> rows = JPA.getUtil().getEntityManager().createNativeQuery(sql) .getResultList();
				int codePrev = 0;
				int codeParent = 0;
				for (Object[] tek : rows) {
					SystemClassif item = new SystemClassif();
					item.setCode(SearchUtils.asInteger(tek[0]));
					item.setCodeClassif(codeClassif);
					item.setCodeParent(codeParent);
					item.setCodePrev(codePrev);
					item.setDateOt(systemMinDate);
					item.setId(item.getCode());
					item.setLevelNumber(1);
					item.setTekst(SearchUtils.asString(tek[1]));
										
					classif.add(item);
					codePrev = item.getCode();
				}
				
				
			
			} catch (Exception e) {
				e.printStackTrace();
				throw new DbErrorException(e);
			}

			return classif;
			
	  }
	
	
	@SuppressWarnings("unchecked")
	public List<SystemClassif> buildClassDirEs(Integer codeClassif, Integer lang) throws DbErrorException {
		
		LOGGER.debug("buildClassDirEs(codeClassif={},lang={})", codeClassif, lang);
		
		List<SystemClassif> classif = new ArrayList<>();
		Date systemMinDate = DateUtils.systemMinDate();
				
		
		try {
			
			String sql = "select id, ime || ' ' || PREZIME || ' ' || FAMILIA ime from PS_LICE where id in (select distinct HARMLICE from ZD_ZAKONOPROEKTI where HARMLICE is not null)";
			List<Object[]> rows = JPA.getUtil().getEntityManager().createNativeQuery(sql) .getResultList();
			int codePrev = 0;
			int codeParent = 0;
			for (Object[] tek : rows) {
				SystemClassif item = new SystemClassif();
				item.setCode(SearchUtils.asInteger(tek[0]));
				item.setCodeClassif(codeClassif);
				item.setCodeParent(codeParent);
				item.setCodePrev(codePrev);
				item.setDateOt(systemMinDate);
				item.setId(item.getCode());
				item.setLevelNumber(1);
				item.setTekst(SearchUtils.asString(tek[1]));
									
				classif.add(item);
				codePrev = item.getCode();
			}
			
			
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new DbErrorException(e);
		}

		return classif;
	}
	

}
