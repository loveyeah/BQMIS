package power.ejb.manage.budget;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity CbmCCenterUse.
 * 
 * @see power.ejb.manage.budget.CbmCCenterUse
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCCenterUseFacade implements CbmCCenterUseFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved CbmCCenterUse entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            CbmCCenterUse entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(CbmCCenterUse entity) {
		LogUtil.log("saving CbmCCenterUse instance", Level.INFO, null);
		try {
			entity.setUseId(bll.getMaxId("CBM_C_CENTER_USE",
			"USE_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent CbmCCenterUse entity.
	 * 
	 * @param entity
	 *            CbmCCenterUse entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	
	//add by ypan 20100906
	
	public void delete(String ids) {
		String sql = " update CBM_C_CENTER_USE a set a.is_use='N' \n"
				+ " where a.use_id in (" + ids + ")";
		   bll.exeNativeSQL(sql);
	}
	
	public void saveCenter(List<CbmCCenterUse> addList,
			List<CbmCCenterUse> updateList, String ids){
		
		if (addList.size() > 0) {
			for (CbmCCenterUse entity : addList) {
				this.save(entity);
				entityManager.flush();
			}
		}
		if (updateList.size() > 0) {
			for (CbmCCenterUse entity : updateList) {
				this.update(entity);
			}
		}
		if (ids.length() > 0) {
			this.delete(ids);
		}
	}
	
	public void delete(CbmCCenterUse entity) {
		LogUtil.log("deleting CbmCCenterUse instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmCCenterUse.class, entity
					.getUseId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved CbmCCenterUse entity and return it or a copy
	 * of it to the sender. A copy of the CbmCCenterUse entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            CbmCCenterUse entity to update
	 * @return CbmCCenterUse the persisted CbmCCenterUse entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public CbmCCenterUse update(CbmCCenterUse entity) {
		LogUtil.log("updating CbmCCenterUse instance", Level.INFO, null);
		try {
			CbmCCenterUse result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmCCenterUse findById(Long id) {
		LogUtil.log("finding CbmCCenterUse instance with id: " + id,
				Level.INFO, null);
		try {
			CbmCCenterUse instance = entityManager
					.find(CbmCCenterUse.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all CbmCCenterUse entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CbmCCenterUse property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<CbmCCenterUse> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<CbmCCenterUse> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding CbmCCenterUse instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from CbmCCenterUse model where model."
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

	/**
	 * Find all CbmCCenterUse entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CbmCCenterUse> all CbmCCenterUse entities
	 */
	@SuppressWarnings("unchecked")
	public List<CbmCCenterUse> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all CbmCCenterUse instances", Level.INFO, null);
		try {
			final String queryString = "select model from CbmCCenterUse model";
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
	
	//add by ypan 20100906
	public PageObject findCenterList(String useId,
			String enterpriseCode, int... rowStartIdxAndCount){
		PageObject page = new PageObject();
		String sql="select a.use_id,\n"+
		"c.dept_name, \n"+
		"c.dept_code,\n"+
		"a.center_id,\n"+
		"a.is_use,\n"+
		"a.item_id\n"+
		  "from CBM_C_CENTER_USE a, cbm_c_item b, hr_c_dept c\n"+
		   "where a.item_id = b.item_id\n"+
		    "and a.is_use = 'Y'\n"+
		    "and b.is_use = 'Y'\n"+
		    "and c.is_use = 'Y'\n"+
		    "and c.dept_id = a.center_id\n"+
		    "and a.item_id='"+useId+"'\n"+
		    "and a.enterprise_code='"+enterpriseCode+"'\n"+
		    "and b.enterprise_code='"+enterpriseCode+"'\n"+
		    "and c.enterprise_code='"+enterpriseCode+"'\n";
		String sqlCount="select count(1) from ("+sql+")";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long
		.parseLong(bll.getSingal(sqlCount).toString());  
		   page.setList(list);
		   page.setTotalCount(totalCount);
		return page;
	}

	public boolean checkDepName(long itemIds,String depName,String enterpriseCode,long useId){
		String st = "select a.use_id,\n"+
		"c.dept_name, \n"+
		"c.dept_code,\n"+
		"a.center_id,\n"+
		"a.is_use,\n"+
		"a.item_id\n"+
		  "from CBM_C_CENTER_USE a, cbm_c_item b, hr_c_dept c\n"+
		   "where a.item_id = b.item_id\n"+
		    "and a.is_use = 'Y'\n"+
		    "and b.is_use = 'Y'\n"+
		    "and c.is_use = 'Y'\n"+
		    "and c.dept_id = a.center_id\n"+
		    "and a.item_id='"+itemIds+"'\n"+
		    "and a.enterprise_code='"+enterpriseCode+"'\n"+
		    "and b.enterprise_code='"+enterpriseCode+"'\n"+
		    "and c.enterprise_code='"+enterpriseCode+"'\n"+
		    "and c.dept_name='"+depName+"'";
	if (useId!= 0l)
		st += " and a.use_id <> useId";
	int a = bll.exeNativeSQL(st);
	if (a > 0) {
		return true;
	} else
		return false;
	}
	
}