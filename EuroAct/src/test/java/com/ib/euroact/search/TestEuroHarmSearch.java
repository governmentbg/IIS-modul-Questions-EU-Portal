package com.ib.euroact.search;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.system.SystemData;
import com.ib.system.db.JPA;
import com.ib.system.utils.SearchUtils;

public class TestEuroHarmSearch {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestEuroHarmSearch.class);

	/***/
	@Before
	public void setUp() {
		JPA.getUtil();
	}

	/** */
	@Test
	public void testBuildQueryComp() {
		try {
			EuroHarmSearch search = new EuroHarmSearch();
			
//			search.setPriority(true);
//			search.setRnDoc("902-01-28");
//			search.setGlava(6962);
//			search.setSastoianie(6166);
//			search.setOtgovariast(11668);
//			search.setSastoianieEtap(374);
//			search.setNaim("гарантиране банките");
			
			search.setPrietiOnly(true);
			
			search.buildQuery(new SystemData());
			
			 Query query = JPA.getUtil().getEntityManager().createNativeQuery(search.getSqlCount());
			 Set<Entry<String, Object>> entrySet = search.getSqlParameters().entrySet();
			 for (Entry<String, Object> entry : entrySet) {
				 query.setParameter(entry.getKey(), entry.getValue());
			 }
			 
			 Integer cnt = SearchUtils.asInteger(query.getSingleResult());
			 System.out.println("SizeCount: " + cnt);
			 
			 query = JPA.getUtil().getEntityManager().createNativeQuery(search.getSql());
			 entrySet = search.getSqlParameters().entrySet();
			 for (Entry<String, Object> entry : entrySet) {
				 query.setParameter(entry.getKey(), entry.getValue());
			 }
			 
			 List<Object[]> result = query.getResultList();
			 System.out.println("SizeSelect: " + result.size());
			 
			 
			 
			for (Object[] row : result) {
				LOGGER.info(Arrays.toString(row));
			}
			LOGGER.info("{}", result.size());

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}
	}

}
