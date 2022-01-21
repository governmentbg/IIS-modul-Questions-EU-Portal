package com.ib.euroact.db.dao;

import java.util.ArrayList;

import javax.persistence.Query;

import com.ib.euroact.db.dto.Dossier;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.EuroDAO;
import com.ib.system.ActiveUser;
import com.ib.system.db.DialectConstructor;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.SearchUtils;

public class DossierDAO extends EuroDAO<Dossier> {

	//private static final Logger LOGGER = LoggerFactory.getLogger(DossierDAO.class);
	
	
	
	public DossierDAO(ActiveUser user){
		super(user);		
	}
	
	
	@SuppressWarnings("unchecked")
	public void fillDossierConnections(Dossier dossier) {
		String dialect = JPA.getUtil().getDbVendorName();
		String sql = "select EURO_DOSSIER.id edid, EURO_ACT_NEW.id eaid, EURO_ACT_NEW.VID_ACT, "+DialectConstructor.limitBigString(dialect, "EURO_DOSSIER.NАМЕ", 3000)+" from  EURO_DOSSIER join EURO_ACT_NEW on EURO_DOSSIER.EURO_ACT_NEW_ID = EURO_ACT_NEW.id where trim(ZAKON_NAME_V) = :zname and ZAKON_DV_BR_V = :brdv and ZAKON_DV_GOD_V = :goddv and EURO_DOSSIER.id <> :did";
		
		if (dossier.getNoteV()  != null && !dossier.getNoteV().trim().isEmpty()) {
			sql += " and NOTE_V like :notv";
		}else {
			sql += " and NOTE_V is null";
		}
		
		Query q = JPA.getUtil().getEntityManager().createNativeQuery(sql);
		q.setParameter("zname", dossier.getZakonNameV().trim());
		q.setParameter("brdv", dossier.getZakonDvBrV());
		q.setParameter("goddv", dossier.getZakonDvGodV());
		q.setParameter("did", dossier.getId());
		
		if (dossier.getNoteV()  != null && !dossier.getNoteV().trim().isEmpty()) {
			q.setParameter("notv", dossier.getNoteV());
		}
		
		
		ArrayList<Object[]> result = (ArrayList<Object[]>) q.getResultList();
		
		for (Object[] row : result) {
			Dossier d = new Dossier(); 
			d.setId(SearchUtils.asInteger(row[0]));
			d.setEuroActNewId(SearchUtils.asInteger(row[1]));
			d.setVidAct(SearchUtils.asInteger(row[2]));
			d.setNameAct(SearchUtils.asString(row[3]));
			
			if (d.getVidAct().equals(EuroConstants.TIP_ACT_DIREKTIVA) || 
				d.getVidAct().equals(EuroConstants.TIP_ACT_DIREKTIVA_ZA_IZPALNENIE) ||
				d.getVidAct().equals(EuroConstants.TIP_ACT_POPRAVKA_NA_DIREKTIVA) ||
				d.getVidAct().equals(EuroConstants.TIP_ACT_RAMKOVO_RESHENIE) ||
				d.getVidAct().equals(EuroConstants.TIP_ACT_DELEGIRANA_DIREKTIVA) )
			{
				dossier.getLinksDir().add(d);
			}else {
				dossier.getLinksReg().add(d);
			}
			
		}
		
	}
	
	public Dossier save(Dossier dossier) throws DbErrorException {
		
		
		if (dossier.getName() != null) {
			dossier.setName(dossier.getName().trim());
		}
		
		if (dossier.getZakonName() != null) {
			dossier.setZakonName(dossier.getZakonName().trim());
		}
		
		if (dossier.getZakonNameV() != null) {
			dossier.setZakonNameV(dossier.getZakonNameV().trim());
		}
		
		Dossier saved = super.save(dossier);
		
		saved.setVidAct(dossier.getVidAct());
		saved.setNameAct(dossier.getNameAct());
		saved.setLinksDir(dossier.getLinksDir());
		saved.setLinksReg(dossier.getLinksReg());
		
		return saved;
	}
	
	
	
}
