package power.ejb.hr;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity HrCAssistantManagerUnits.
 * 
 * @see powereai.po.hr.HrCAssistantManagerUnits
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCAssistantManagerUnitsFacade implements
		HrCAssistantManagerUnitsFacadeRemote { 
	public static final String ASSISTANT_MANAGER_UNITS_NAME = "assistantManagerUnitsName";
	public static final String IS_USE = "isUse";
	public static final String RETRIEVE_CODE = "retrieveCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll; 
 
	public void save(HrCAssistantManagerUnits entity) throws CodeRepeatException {
	 
		try {
			if(!checkNameSameForAdd(entity.getAssistantManagerUnitsName()))
			{
			if(entity.getAssistantManagerUnitsId() == null)
			{
				entity.setAssistantManagerUnitsId(bll.getMaxId("hr_c_assistant_manager_units", "assistant_manager_units_id"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		}else{
			throw new CodeRepeatException("名称不可重复!");
		}
			
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	public HrCAssistantManagerUnits update(HrCAssistantManagerUnits entity) throws CodeRepeatException {
		 
		try {
			if(!checkNameSameForUpdate(entity.getAssistantManagerUnitsId(),entity.getAssistantManagerUnitsName()))
			{
				
			HrCAssistantManagerUnits result = entityManager.merge(entity); 
			return result;
		}else{
			throw new CodeRepeatException("名称不可重复!");
		
		}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private boolean checkNameSameForAdd(String amuName)
	{
		String sql = "select count(1) from hr_c_assistant_manager_units t where t.assistant_manager_units_name=?";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{amuName}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}
	private boolean checkNameSameForUpdate(Long amuId,String amuName)
	{
		String sql = "select count(1) from hr_c_assistant_manager_units t where t.assistant_manager_units_name=? and t.assistant_manager_units_id <> ?";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{amuName,amuId}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}

	 
	public void delete(HrCAssistantManagerUnits entity) {
		try {
			entity = entityManager.getReference(HrCAssistantManagerUnits.class,
					entity.getAssistantManagerUnitsId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	} 


	public HrCAssistantManagerUnits findById(Long id) {
		LogUtil.log("finding HrCAssistantManagerUnits instance with id: " + id,
				Level.INFO, null);
		try {
			HrCAssistantManagerUnits instance = entityManager.find(
					HrCAssistantManagerUnits.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCAssistantManagerUnits entities with a specific property
	 * value.
	 * 
	 * @param propertyName
	 *            the name of the HrCAssistantManagerUnits property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<HrCAssistantManagerUnits> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCAssistantManagerUnits> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrCAssistantManagerUnits instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCAssistantManagerUnits model where model."
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

	public List<HrCAssistantManagerUnits> findByProperties(int start, int limit) {
		try {
			final String queryString = "select model from HrCAssistantManagerUnits model";
			Query query = entityManager.createQuery(queryString);

			int rowStartIdx = Math.max(0, start);
			if (rowStartIdx > 0) {
				query.setFirstResult(rowStartIdx);
			}
			int rowCount = Math.max(0, limit);
			if (rowCount > 0) {
				query.setMaxResults(rowCount);
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by properties name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrCAssistantManagerUnits> findByAssistantManagerUnitsName(
			Object assistantManagerUnitsName, int... rowStartIdxAndCount) {
		return findByProperty(ASSISTANT_MANAGER_UNITS_NAME,
				assistantManagerUnitsName, rowStartIdxAndCount);
	}

	public List<HrCAssistantManagerUnits> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	public List<HrCAssistantManagerUnits> findByRetrieveCode(
			Object retrieveCode, int... rowStartIdxAndCount) {
		return findByProperty(RETRIEVE_CODE, retrieveCode, rowStartIdxAndCount);
	}

	/**
	 * Find all HrCAssistantManagerUnits entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCAssistantManagerUnits> all HrCAssistantManagerUnits
	 *         entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCAssistantManagerUnits> findAll(
			final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrCAssistantManagerUnits instances",
				Level.INFO, null);
		try {
			final String queryString = "select model from HrCAssistantManagerUnits model";
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

}