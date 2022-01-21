package com.ib.euroact.system;

import java.util.HashMap;
import java.util.List;

import com.ib.system.BaseSystemData;
import com.ib.system.db.JPA;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.utils.SearchUtils;

public class SystemData extends BaseSystemData{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7904334043269288530L;
	
	
	HashMap<Integer, Integer> admVraz = new HashMap<Integer, Integer>();
	
	
	
	@SuppressWarnings("unchecked")
	private void  initVrazMap () throws DbErrorException {
		
		
		try {
			List<Object[]> list = JPA.getUtil().getEntityManager().createNativeQuery("select role1, role2 from EURO_CONN_SETTINGS").getResultList();
			for (Object[] tek : list) {
				Integer role1 = SearchUtils.asInteger(tek[0]);
				Integer role2 = SearchUtils.asInteger(tek[1]);
				admVraz.put(role1, role2);
				admVraz.put(role2, role1);
				
			}
			
		} catch (Exception e) {
			throw new DbErrorException("Грешка при зареждане на списък с обратни връзки", e);
		}
		
	}
	
	
//	@Override
//	public List<BaseSystemOption> selectOptionsList() throws DbErrorException {
//		// ако сме влезли тука в активна транзакция накрая не трябва да се вика close()
//		boolean isActive = JPA.getUtil().getEntityManager().getTransaction().isActive();
//		try {
//			List<SystemOption> findAll =  new SystemOptionDAO(ActiveUser.DEFAULT).findAll(); // тези от БД
//			return new ArrayList<>(findAll);
//		} finally {
//			if (!isActive) {
//				JPA.getUtil().closeConnection();
//			}
//		}
//	}
	
	
	@Override
	protected Object createDynamicAdapterInstance() {
		return new EuroActSysClassifAdapter(this);
	}
	
	
	public Integer getOtherRole(Integer role) throws DbErrorException {
		
		if (role == null) {
			return null;
		}
		
		if (admVraz.size() == 0) {
			initVrazMap();
		}
		
		return admVraz.get(role);
	}
	

}
