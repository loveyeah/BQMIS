package power.ejb.hr;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity SysCPowerPlant.
 * 
 * @see powereai.po.sys.SysCPowerPlant
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SysCPowerPlantFacade implements SysCPowerPlantFacadeRemote
{
	// property constants
	public static final String PPOWER_PLANT_ID = "ppowerPlantId";
	public static final String PPOWER_PLANT_CODE = "ppowerPlantCode";
	public static final String POWER_PLANT_CODE = "powerPlantCode";
	public static final String POWER_PLANT_NAME = "powerPlantName";
	public static final String POWER_PLANT_ALIAS = "powerPlantAlias";
	public static final String NATURE_ID = "natureId";
	public static final String POWER_TYPE_ID = "powerTypeId";
	public static final String UNITS_CAPACITY = "unitsCapacity";
	public static final String RUNSALE_STATUS_ID = "runsaleStatusId";
	public static final String SCHEDULING_RELATIONS_ID = "schedulingRelationsId";
	public static final String ACCOUNTING_METHODS_ID = "accountingMethodsId";
	public static final String MANAGER = "manager";
	public static final String IS_POWER_PLANT = "isPowerPlant";
	public static final String CURRENT_UNIT = "currentUnit";
	public static final String REGIONALISM_TYPE_ID = "regionalismTypeId";
	public static final String CITY = "city";
	public static final String IS_USE = "isUse";
	public static final String MEMO = "memo";
	public static final String RETRIEVE_CODE = "retrieveCode";
	public static final String CREATE_BY = "createBy";
	public static final String MODIFIY_BY = "modifiyBy";
	public static final String LOGOFF_BY = "logoffBy";
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved SysCPowerPlant entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            SysCPowerPlant entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(SysCPowerPlant entity)
	{
		LogUtil.log("saving SysCPowerPlant instance", Level.INFO, null);
		try
		{
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re)
		{
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent SysCPowerPlant entity.
	 * 
	 * @param entity
	 *            SysCPowerPlant entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SysCPowerPlant entity)
	{
		LogUtil.log("deleting SysCPowerPlant instance", Level.INFO, null);
		try
		{
			entity = entityManager.getReference(SysCPowerPlant.class, entity
					.getPowerPlantId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re)
		{
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved SysCPowerPlant entity and return it or a copy
	 * of it to the sender. A copy of the SysCPowerPlant entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            SysCPowerPlant entity to update
	 * @return SysCPowerPlant the persisted SysCPowerPlant entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SysCPowerPlant update(SysCPowerPlant entity)
	{
		LogUtil.log("updating SysCPowerPlant instance", Level.INFO, null);
		try
		{
			SysCPowerPlant result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re)
		{
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SysCPowerPlant findById(Long id)
	{
		LogUtil.log("finding SysCPowerPlant instance with id: " + id,
				Level.INFO, null);
		try
		{
			SysCPowerPlant instance = entityManager.find(SysCPowerPlant.class,
					id);
			return instance;
		} catch (RuntimeException re)
		{
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all SysCPowerPlant entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SysCPowerPlant property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<SysCPowerPlant> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<SysCPowerPlant> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount)
	{
		LogUtil.log("finding SysCPowerPlant instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try
		{
			final String queryString = "select model from SysCPowerPlant model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0)
			{
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0)
				{
					query.setFirstResult(rowStartIdx);
				}
				if (rowStartIdxAndCount.length > 1)
				{
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0)
					{
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re)
		{
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过指定属性，查出所有记录
	 */
	public List<SysCPowerPlant> findByPropertys(String strWhere, Object[] o,
			final int... rowStartIdxAndCount)
	{
		try
		{
			final String queryString = "select model from SysCPowerPlant model where "
					+ strWhere;
			Query query = entityManager.createQuery(queryString);
			for (int i = 0; i < o.length; i++)
			{
				query.setParameter("param" + i, o[i]);
			}
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0)
			{
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0)
				{
					query.setFirstResult(rowStartIdx);
				}
				if (rowStartIdxAndCount.length > 1)
				{
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0)
					{
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re)
		{
			LogUtil.log("find by strWhere failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<SysCPowerPlant> findByPpowerPlantId(Object ppowerPlantId,
			int... rowStartIdxAndCount)
	{
		return findByProperty(PPOWER_PLANT_ID, ppowerPlantId,
				rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByPpowerPlantCode(Object ppowerPlantCode,
			int... rowStartIdxAndCount)
	{
		return findByProperty(PPOWER_PLANT_CODE, ppowerPlantCode,
				rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByPowerPlantCode(Object powerPlantCode,
			int... rowStartIdxAndCount)
	{
		return findByProperty(POWER_PLANT_CODE, powerPlantCode,
				rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByPowerPlantName(Object powerPlantName,
			int... rowStartIdxAndCount)
	{
		return findByProperty(POWER_PLANT_NAME, powerPlantName,
				rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByPowerPlantAlias(Object powerPlantAlias,
			int... rowStartIdxAndCount)
	{
		return findByProperty(POWER_PLANT_ALIAS, powerPlantAlias,
				rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByNatureId(Object natureId,
			int... rowStartIdxAndCount)
	{
		return findByProperty(NATURE_ID, natureId, rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByPowerTypeId(Object powerTypeId,
			int... rowStartIdxAndCount)
	{
		return findByProperty(POWER_TYPE_ID, powerTypeId, rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByUnitsCapacity(Object unitsCapacity,
			int... rowStartIdxAndCount)
	{
		return findByProperty(UNITS_CAPACITY, unitsCapacity,
				rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByRunsaleStatusId(Object runsaleStatusId,
			int... rowStartIdxAndCount)
	{
		return findByProperty(RUNSALE_STATUS_ID, runsaleStatusId,
				rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findBySchedulingRelationsId(
			Object schedulingRelationsId, int... rowStartIdxAndCount)
	{
		return findByProperty(SCHEDULING_RELATIONS_ID, schedulingRelationsId,
				rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByAccountingMethodsId(
			Object accountingMethodsId, int... rowStartIdxAndCount)
	{
		return findByProperty(ACCOUNTING_METHODS_ID, accountingMethodsId,
				rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByManager(Object manager,
			int... rowStartIdxAndCount)
	{
		return findByProperty(MANAGER, manager, rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByIsPowerPlant(Object isPowerPlant,
			int... rowStartIdxAndCount)
	{
		return findByProperty(IS_POWER_PLANT, isPowerPlant, rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByCurrentUnit(Object currentUnit,
			int... rowStartIdxAndCount)
	{
		return findByProperty(CURRENT_UNIT, currentUnit, rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByRegionalismTypeId(
			Object regionalismTypeId, int... rowStartIdxAndCount)
	{
		return findByProperty(REGIONALISM_TYPE_ID, regionalismTypeId,
				rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByCity(Object city,
			int... rowStartIdxAndCount)
	{
		return findByProperty(CITY, city, rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByIsUse(Object isUse,
			int... rowStartIdxAndCount)
	{
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByMemo(Object memo,
			int... rowStartIdxAndCount)
	{
		return findByProperty(MEMO, memo, rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount)
	{
		return findByProperty(RETRIEVE_CODE, retrieveCode, rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByCreateBy(Object createBy,
			int... rowStartIdxAndCount)
	{
		return findByProperty(CREATE_BY, createBy, rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByModifiyBy(Object modifiyBy,
			int... rowStartIdxAndCount)
	{
		return findByProperty(MODIFIY_BY, modifiyBy, rowStartIdxAndCount);
	}

	public List<SysCPowerPlant> findByLogoffBy(Object logoffBy,
			int... rowStartIdxAndCount)
	{
		return findByProperty(LOGOFF_BY, logoffBy, rowStartIdxAndCount);
	}

	/**
	 * Find all SysCPowerPlant entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<SysCPowerPlant> all SysCPowerPlant entities
	 */
	@SuppressWarnings("unchecked")
	public List<SysCPowerPlant> findAll(final int... rowStartIdxAndCount)
	{
		LogUtil.log("finding all SysCPowerPlant instances", Level.INFO, null);
		try
		{
			final String queryString = "select model from SysCPowerPlant model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0)
			{
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0)
				{
					query.setFirstResult(rowStartIdx);
				}
				if (rowStartIdxAndCount.length > 1)
				{
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0)
					{
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re)
		{
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
}