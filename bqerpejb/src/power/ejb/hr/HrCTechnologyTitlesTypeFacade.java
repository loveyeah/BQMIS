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
 * Facade for entity HrCTechnologyTitlesType.
 * 
 * @see powereai.po.hr.HrCTechnologyTitlesType
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCTechnologyTitlesTypeFacade implements
		HrCTechnologyTitlesTypeFacadeRemote {
	// property constants
	public static final String TECHNOLOGY_TITLES_TYPE_NAME = "technologyTitlesTypeName";
	public static final String IS_USE = "isUse";
	public static final String RETRIEVE_CODE = "retrieveCode";
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved HrCTechnologyTitlesType
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            HrCTechnologyTitlesType entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCTechnologyTitlesType entity) throws CodeRepeatException{
		LogUtil
				.log("saving HrCTechnologyTitlesType instance", Level.INFO,
						null);
		if(checkNameSameForAdd(entity.getTechnologyTitlesTypeName(),entity.getIsUse()))
		{
			throw new CodeRepeatException("职称类别名称已经存在!");
		} 
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCTechnologyTitlesType entity.
	 * 
	 * @param entity
	 *            HrCTechnologyTitlesType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCTechnologyTitlesType entity) {
		LogUtil.log("deleting HrCTechnologyTitlesType instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(HrCTechnologyTitlesType.class,
					entity.getTechnologyTitlesTypeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCTechnologyTitlesType entity and return it
	 * or a copy of it to the sender. A copy of the HrCTechnologyTitlesType
	 * entity parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrCTechnologyTitlesType entity to update
	 * @return HrCTechnologyTitlesType the persisted HrCTechnologyTitlesType
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCTechnologyTitlesType update(HrCTechnologyTitlesType entity) throws CodeRepeatException{
		LogUtil.log("updating HrCTechnologyTitlesType instance", Level.INFO,
				null);
		
		try {
			if(checkNameSameForUpdate(entity.getTechnologyTitlesTypeName(),entity.getIsUse(),entity.getTechnologyTitlesTypeId()))
			{
				throw new CodeRepeatException("职称类别名称已经存在!");
			} 
			HrCTechnologyTitlesType result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCTechnologyTitlesType findById(Long id) {
		LogUtil.log("finding HrCTechnologyTitlesType instance with id: " + id,
				Level.INFO, null);
		try {
			HrCTechnologyTitlesType instance = entityManager.find(
					HrCTechnologyTitlesType.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCTechnologyTitlesType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCTechnologyTitlesType property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<HrCTechnologyTitlesType> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCTechnologyTitlesType> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrCTechnologyTitlesType instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCTechnologyTitlesType model where model."
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
	public List<HrCTechnologyTitlesType> findByPropertys(String strWhere,
			Object o, int... rowStartIdxAndCount) {
		try {
			final String queryString = "select model from HrCTechnologyTitlesType model where "+strWhere;
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

	public List<HrCTechnologyTitlesType> findByTechnologyTitlesTypeName(
			Object technologyTitlesTypeName, int... rowStartIdxAndCount) {
		return findByProperty(TECHNOLOGY_TITLES_TYPE_NAME,
				technologyTitlesTypeName, rowStartIdxAndCount);
	}

	public List<HrCTechnologyTitlesType> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	public List<HrCTechnologyTitlesType> findByRetrieveCode(
			Object retrieveCode, int... rowStartIdxAndCount) {
		return findByProperty(RETRIEVE_CODE, retrieveCode, rowStartIdxAndCount);
	}

	/**
	 * Find all HrCTechnologyTitlesType entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCTechnologyTitlesType> all HrCTechnologyTitlesType
	 *         entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCTechnologyTitlesType> findAll(
			final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrCTechnologyTitlesType instances",
				Level.INFO, null);
		try {
			final String queryString = "select model from HrCTechnologyTitlesType model";
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
	private boolean checkNameSameForAdd(String technologyTitlesTypeName,String isUse)
	{
		String sql = "select count(1) from HR_C_TECHNOLOGY_TITLES_TYPE t where t.TECHNOLOGY_TITLES_TYPE_NAME=? and t.IS_USE=?";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{technologyTitlesTypeName,isUse}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}
	private boolean checkNameSameForUpdate(String technologyTitlesTypeName,String isUse,Long technologyTitlesTypeId)
	{
		String sql = "select count(1) from HR_C_TECHNOLOGY_TITLES_TYPE t where t.TECHNOLOGY_TITLES_TYPE_NAME=? and t.IS_USE=? and t.technology_titles_type_id<>?";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{technologyTitlesTypeName,isUse,technologyTitlesTypeId}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}
}