package power.ejb.productiontec.relayProtection;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity PtJdbhCSydwh.
 * 
 * @see power.ejb.productiontec.relayProtection.PtJdbhCSydwh
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJdbhCSydwhFacade implements PtJdbhCSydwhFacadeRemote {
	// property constants
	public static final String SYXM_ID = "syxmId";
	public static final String SYD_NAME = "sydName";
	public static final String UNIT_ID = "unitId";
	public static final String MINIMUM = "minimum";
	public static final String MAXIMUM = "maximum";
	public static final String DISPLAY_NO = "displayNo";
	public static final String MEMO = "memo";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtJdbhCSydwh entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJdbhCSydwh entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJdbhCSydwh entity) {
		LogUtil.log("saving PtJdbhCSydwh instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PtJdbhCSydwh entity.
	 * 
	 * @param entity
	 *            PtJdbhCSydwh entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJdbhCSydwh entity) {
		LogUtil.log("deleting PtJdbhCSydwh instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJdbhCSydwh.class, entity
					.getSydId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PtJdbhCSydwh entity and return it or a copy of
	 * it to the sender. A copy of the PtJdbhCSydwh entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJdbhCSydwh entity to update
	 * @return PtJdbhCSydwh the persisted PtJdbhCSydwh entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJdbhCSydwh update(PtJdbhCSydwh entity) {
		LogUtil.log("updating PtJdbhCSydwh instance", Level.INFO, null);
		try {
			PtJdbhCSydwh result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJdbhCSydwh findById(Long id) {
		LogUtil.log("finding PtJdbhCSydwh instance with id: " + id, Level.INFO,
				null);
		try {
			PtJdbhCSydwh instance = entityManager.find(PtJdbhCSydwh.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void save(List<PtJdbhCSydwh> addList,
			List<PtJdbhCSydwh> updateList, String delIds) throws CodeRepeatException{
		if (addList.size() > 0) {
			Long idLong = bll.getMaxId("pt_jdbh_c_sydwh", "SYD_ID");
			for (PtJdbhCSydwh entity : addList) {
				if(checkSydName(entity.getSydName())) {
					entity.setSydId(idLong++);
					this.save(entity);
				} else {
					throw new CodeRepeatException("试验点名称不能重复！");
				} 
				
			}
		}
		if (updateList.size() > 0) {
			for (PtJdbhCSydwh entity : updateList) {
				if(checkSydName(entity.getSydName(), entity.getSydId())){
					this.update(entity);
				}else {
					throw new CodeRepeatException("试验点名称不能重复！");
				} 
			}
		}
		if (delIds.length() > 0) {
			String sqlString = "delete from pt_jdbh_c_sydwh t"
					+ " where t.SYD_ID in(" + delIds + ")";
			bll.exeNativeSQL(sqlString);
		}

	}

	/**
	 * Find all PtJdbhCSydwh entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJdbhCSydwh property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<PtJdbhCSydwh> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtJdbhCSydwh> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtJdbhCSydwh instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJdbhCSydwh model where model."
					+ propertyName + "= :propertyValue order by display_no";
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

	public List<PtJdbhCSydwh> findBySyxmId(Object syxmId,
			int... rowStartIdxAndCount) {
		return findByProperty(SYXM_ID, syxmId, rowStartIdxAndCount);
	}

	public List<PtJdbhCSydwh> findBySydName(Object sydName,
			int... rowStartIdxAndCount) {
		return findByProperty(SYD_NAME, sydName, rowStartIdxAndCount);
	}

	public List<PtJdbhCSydwh> findByUnitId(Object unitId,
			int... rowStartIdxAndCount) {
		return findByProperty(UNIT_ID, unitId, rowStartIdxAndCount);
	}

	public List<PtJdbhCSydwh> findByMinimum(Object minimum,
			int... rowStartIdxAndCount) {
		return findByProperty(MINIMUM, minimum, rowStartIdxAndCount);
	}

	public List<PtJdbhCSydwh> findByMaximum(Object maximum,
			int... rowStartIdxAndCount) {
		return findByProperty(MAXIMUM, maximum, rowStartIdxAndCount);
	}

	public List<PtJdbhCSydwh> findByDisplayNo(Object displayNo,
			int... rowStartIdxAndCount) {
		return findByProperty(DISPLAY_NO, displayNo, rowStartIdxAndCount);
	}

	public List<PtJdbhCSydwh> findByMemo(Object memo,
			int... rowStartIdxAndCount) {
		return findByProperty(MEMO, memo, rowStartIdxAndCount);
	}

	public List<PtJdbhCSydwh> findByEnterpriseCode(Object enterpriseCode,
			int... rowStartIdxAndCount) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode,
				rowStartIdxAndCount);
	}

	/**
	 * Find all PtJdbhCSydwh entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJdbhCSydwh> all PtJdbhCSydwh entities
	 */
	@SuppressWarnings("unchecked")
	public List<PtJdbhCSydwh> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtJdbhCSydwh instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtJdbhCSydwh model";
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
	
	private Boolean checkSydName(String sydName, Long... sydId) {
		String sql = "select count(*) from PT_JDBH_C_SYDWH t where t.SYD_NAME ='" +sydName+"'";
		if(sydId != null && sydId.length > 0) {                         
			sql += " and t.SYD_ID <> " + sydId[0];                                
		}
		if(Long.parseLong((bll.getSingal(sql).toString()))>0) {
			return false;
		}
		return true;
	}

}