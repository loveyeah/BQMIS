package power.ejb.hr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.form.DrpCommBeanInfo;

 
@Stateless
public class HrCTechnologyTitlesFacade implements
		HrCTechnologyTitlesFacadeRemote {
	// property constants
	public static final String TECHNOLOGY_TITLES_TYPE_ID = "technologyTitlesTypeId";
	public static final String TECHNOLOGY_TITLES_NAME = "technologyTitlesName";
	public static final String TECHNOLOGY_TITLES_LEVEL = "technologyTitlesLevel";
	public static final String IS_USE = "isUse";
	public static final String RETRIEVE_CODE = "retrieveCode";
	
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	 
	public void save(HrCTechnologyTitles entity) throws CodeRepeatException{ 
		try {
			
			if(checkCodeSameForAdd(entity.getTechnologyTitlesName()))
			{
				throw new CodeRepeatException("名称已经存在!");
			} 
			if(entity.getTechnologyTitlesId() == null)
			{
				entity.setTechnologyTitlesId(bll.getMaxId("hr_c_technology_titles", "technology_titles_id"));
			}
			entityManager.persist(entity); 
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	public HrCTechnologyTitles update(HrCTechnologyTitles entity) throws CodeRepeatException {
		 
		try {
			if(checkCodeSameForUpdate(entity.getTechnologyTitlesId(),entity.getTechnologyTitlesName()))
			{
				throw new CodeRepeatException("名称已经存在!");
			} 
			HrCTechnologyTitles result = entityManager.merge(entity); 
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private boolean checkCodeSameForAdd(String technologyName)
	{
		String sql = "select count(1) from hr_c_technology_titles t where t.technology_titles_name=?  and t.is_use='Y' ";//update by sychen 20100831
//		String sql = "select count(1) from hr_c_technology_titles t where t.technology_titles_name=?  and t.is_use='U' ";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{technologyName}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}
	private boolean checkCodeSameForUpdate(Long technologyId,String technologyName)
	{
		String sql = "select count(1) from hr_c_technology_titles t where t.technology_titles_name=? and t.technology_titles_id <> ?  and t.is_use='Y' ";//update by sychen 20100831
//		String sql = "select count(1) from hr_c_technology_titles t where t.technology_titles_name=? and t.technology_titles_id <> ?  and t.is_use='U' ";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{technologyName,technologyId}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}

	 
	public void delete(HrCTechnologyTitles entity) {
		LogUtil.log("deleting HrCTechnologyTitles instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCTechnologyTitles.class,
					entity.getTechnologyTitlesId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	 
	

	public HrCTechnologyTitles findById(Long id) {
		LogUtil.log("finding HrCTechnologyTitles instance with id: " + id,
				Level.INFO, null);
		try {
			HrCTechnologyTitles instance = entityManager.find(
					HrCTechnologyTitles.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCTechnologyTitles entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCTechnologyTitles property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<HrCTechnologyTitles> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCTechnologyTitles> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrCTechnologyTitles instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCTechnologyTitles model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public List<HrCTechnologyTitles> findByPropertys(String strWhere,
			Object o, int... rowStartIdxAndCount) {
		try {
			final String queryString = "select model from HrCTechnologyTitles model where "+strWhere;
			Query query = entityManager.createQuery(queryString);
			
				query.setParameter("param",o);
			
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrCTechnologyTitles> findByTechnologyTitlesTypeId(
			Object technologyTitlesTypeId, int... rowStartIdxAndCount) {
		return findByProperty(TECHNOLOGY_TITLES_TYPE_ID,
				technologyTitlesTypeId, rowStartIdxAndCount);
	}

	public List<HrCTechnologyTitles> findByTechnologyTitlesName(
			Object technologyTitlesName, int... rowStartIdxAndCount) {
		return findByProperty(TECHNOLOGY_TITLES_NAME, technologyTitlesName,
				rowStartIdxAndCount);
	}

	public List<HrCTechnologyTitles> findByTechnologyTitlesLevel(
			Object technologyTitlesLevel, int... rowStartIdxAndCount) {
		return findByProperty(TECHNOLOGY_TITLES_LEVEL, technologyTitlesLevel,
				rowStartIdxAndCount);
	}

	public List<HrCTechnologyTitles> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	public List<HrCTechnologyTitles> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount) {
		return findByProperty(RETRIEVE_CODE, retrieveCode, rowStartIdxAndCount);
	}

	/**
	 * Find all HrCTechnologyTitles entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCTechnologyTitles> all HrCTechnologyTitles entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCTechnologyTitles> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrCTechnologyTitles instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from HrCTechnologyTitles model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public  PageObject  getTechnologyTitlesList(int... rowStartIdxAndCount){ 
		PageObject result = new PageObject();
		String sqlStr =
            "select TECHNOLOGY_TITLES_ID,\n" +
			"       TECHNOLOGY_TITLES_NAME,\n" + 
			"       TECHNOLOGY_TITLES_TYPE_NAME,\n" + 
			"       TECHNOLOGY_GRADE_NAME,\n" + 
			"       HR_C_TECHNOLOGY_TITLES.IS_USE,\n" + 
			"       HR_C_TECHNOLOGY_TITLES.RETRIEVE_CODE\n" + 
			"  from HR_C_TECHNOLOGY_TITLES,\n" + 
			"       HR_C_TECHNOLOGY_GRADE,\n" + 
			"       HR_C_TECHNOLOGY_TITLES_TYPE\n" + 
			" where HR_C_TECHNOLOGY_TITLES.TECHNOLOGY_TITLES_TYPE_ID =\n" + 
			"       HR_C_TECHNOLOGY_TITLES_TYPE.TECHNOLOGY_TITLES_TYPE_ID\n" + 
			"   and HR_C_TECHNOLOGY_TITLES.TECHNOLOGY_TITLES_LEVEL =\n" + 
			"       HR_C_TECHNOLOGY_GRADE.TECHNOLOGY_GRADE_ID\n" + 
			"       and HR_C_TECHNOLOGY_TITLES.IS_USE<>'N'\n" + //update by sychen 20100831
//			"       and HR_C_TECHNOLOGY_TITLES.IS_USE<>'D'\n" + 
			"order by HR_C_TECHNOLOGY_TITLES.Technology_Titles_Id";
		
		Query query = entityManager.createNativeQuery(sqlStr);
		if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
			
			int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
			if (rowStartIdx > 0) {
				query.setFirstResult(rowStartIdx);
			}

			if (rowStartIdxAndCount.length > 1) {
				int rowCount = Math.max(0, rowStartIdxAndCount[1]);
				if (rowCount > 0) {
					query.setMaxResults(rowCount);
				}
			}
		} 
		List list =  query.getResultList();
		if(list !=null && list.size()>0)
		{ 
			result.setList(list);
			String sqlStrCount =
	            "select count(1)\n" + 
				"  from HR_C_TECHNOLOGY_TITLES,\n" + 
				"       HR_C_TECHNOLOGY_GRADE,\n" + 
				"       HR_C_TECHNOLOGY_TITLES_TYPE\n" + 
				" where HR_C_TECHNOLOGY_TITLES.TECHNOLOGY_TITLES_TYPE_ID =\n" + 
				"       HR_C_TECHNOLOGY_TITLES_TYPE.TECHNOLOGY_TITLES_TYPE_ID\n" + 
				"   and HR_C_TECHNOLOGY_TITLES.TECHNOLOGY_TITLES_LEVEL =\n" + 
				"       HR_C_TECHNOLOGY_GRADE.TECHNOLOGY_GRADE_ID\n" + 
				"       and HR_C_TECHNOLOGY_TITLES.IS_USE<>'N'\n" + //update by sychen 20100831
//				"       and HR_C_TECHNOLOGY_TITLES.IS_USE<>'D'\n" + 
				"order by HR_C_TECHNOLOGY_TITLES.Technology_Titles_Id"; 
			result.setTotalCount(Long.parseLong(bll.getSingal(sqlStrCount).toString()));
			return result;
		} 
		return null;
	}

	
	
	/**
	 * add by liuyi 091123
	 * 查找所有技术职称
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllTechnologyTitles(String enterpriseCode){
		try{
			String sql = "SELECT E.TECHNOLOGY_TITLES_ID,E.TECHNOLOGY_TITLES_NAME \n" +
					"FROM HR_C_TECHNOLOGY_TITLES E \n" +
					//modified by liuyi 091125 数据库表问题
//					" WHERE E.IS_USE = 'Y' AND E.ENTERPRISE_CODE = '" + enterpriseCode +"'";
					" WHERE E.IS_USE = 'Y' ";//update by sychen 20100831
//			        " WHERE E.IS_USE = 'U' ";
			LogUtil.log("所有技术职称id和名称开始。SQL=" + sql, Level.INFO, null);
			List list = bll.queryByNativeSQL(sql);
			List<DrpCommBeanInfo> arraylist = new ArrayList<DrpCommBeanInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DrpCommBeanInfo model = new DrpCommBeanInfo();
				if (data[0] != null) {
					model.setId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					model.setText(data[1].toString());
				}
				arraylist.add(model);
			}
			PageObject result = new PageObject();
			result.setList(arraylist);
			LogUtil.log("查找所有技术职称id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有技术职称id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}
	public Long findTitleTypeIdByName(String name, String enterpriseCode) {
		Long titleId = null;
		String sql = 
			"select a.technology_titles_id\n" +
			"  from hr_c_technology_titles a\n" + 
			" where a.is_use = 'Y'\n" + //update by sychen 20100831
//			" where a.is_use = 'U'\n" + 
			"   and a.technology_titles_name = '"+name+"'";
		Object obj = bll.getSingal(sql);
		if(obj != null)
			titleId = Long.parseLong(obj.toString());

		return titleId;
	}
}