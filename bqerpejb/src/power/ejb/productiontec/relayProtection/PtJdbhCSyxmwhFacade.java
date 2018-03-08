package power.ejb.productiontec.relayProtection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

@Stateless
public class PtJdbhCSyxmwhFacade implements PtJdbhCSyxmwhFacadeRemote {
	// property constants
	public static final String SYXM_NAME = "syxmName";
	public static final String DISPLAY_NO = "displayNo";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtJdbhCSyxmwh entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJdbhCSyxmwh entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJdbhCSyxmwh entity) throws CodeRepeatException {
		LogUtil.log("saving PtJdbhCSyxmwh instance", Level.INFO, null);
		try {
			if (!checkSyxmName(entity.getSyxmName(), entity.getSyxmId())) {
				if (entity.getSyxmId() == null) {
					entity.setSyxmId(bll.getMaxId("PT_JDBH_C_SYXMWH", "syxm_id"));
				}
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
			} else {
				throw new CodeRepeatException("试验名称不能重复！");
			}
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String ids) {
		String sql = "delete from PT_JDBH_C_SYXMWH t "
				+ " where t.syxm_id in (" + ids + ")";
		String sydwhSql = "delete from pt_jdbh_c_sydwh t"
			+ " where t.syxm_id in(" + ids + ")";
		bll.exeNativeSQL(sql);
		bll.exeNativeSQL(sydwhSql);
	}

	/**
	 * Persist a previously saved PtJdbhCSyxmwh entity and return it or a copy
	 * of it to the sender. A copy of the PtJdbhCSyxmwh entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PtJdbhCSyxmwh entity to update
	 * @return PtJdbhCSyxmwh the persisted PtJdbhCSyxmwh entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJdbhCSyxmwh update(PtJdbhCSyxmwh entity) throws CodeRepeatException {
		LogUtil.log("updating PtJdbhCSyxmwh instance", Level.INFO, null);
		try {
			if (!checkSyxmName(entity.getSyxmName(), entity.getSyxmId())) {
				PtJdbhCSyxmwh result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			} else {
				throw new CodeRepeatException("试验名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJdbhCSyxmwh findById(Long id) {
		LogUtil.log("finding PtJdbhCSyxmwh instance with id: " + id,
				Level.INFO, null);
		try {
			PtJdbhCSyxmwh instance = entityManager
					.find(PtJdbhCSyxmwh.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PtJdbhCSyxmwh entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJdbhCSyxmwh property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<PtJdbhCSyxmwh> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtJdbhCSyxmwh> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtJdbhCSyxmwh instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJdbhCSyxmwh model where model."
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

	public List<PtJdbhCSyxmwh> findBySyxmName(Object syxmName,
			int... rowStartIdxAndCount) {
		return findByProperty(SYXM_NAME, syxmName, rowStartIdxAndCount);
	}

	public List<PtJdbhCSyxmwh> findByDisplayNo(Object displayNo,
			int... rowStartIdxAndCount) {
		return findByProperty(DISPLAY_NO, displayNo, rowStartIdxAndCount);
	}

	public List<PtJdbhCSyxmwh> findByEnterpriseCode(Object enterpriseCode,
			int... rowStartIdxAndCount) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode,
				rowStartIdxAndCount);
	}

	/**
	 * 模糊查询所有试验项目
	 * @param fuzzyString
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	public PageObject findPtJdbhCSyxmwhList(String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject object = new PageObject();
		
		String sqlCount = "select count(*) from pt_jdbh_c_syxmwh t\n";
		Long countLong = Long.parseLong(bll.getSingal(sqlCount).toString());
		List<PtJdbhCSyxmwh> arrayList = new ArrayList<PtJdbhCSyxmwh>();
		if(countLong > 0) {
			String sql = "select * from pt_jdbh_c_syxmwh t\n";
			sql = sql + " order by t.display_no ";
			arrayList = bll.queryByNativeSQL(sql, PtJdbhCSyxmwh.class, rowStartIdxAndCount);
		}
		object.setList(arrayList);
		object.setTotalCount(countLong);
		return object;
	}
	
	public boolean checkSyxmName(String syxmName,
			Long... syxmId) {
		boolean isSame = false;
		String sql = "select count(*) from pt_jdbh_c_syxmwh t\n"
				+ "where t.SYXM_NAME = '" + syxmName + "'\n";

		if (syxmId != null && syxmId.length > 0) {
			sql += "  and t.SYXM_ID <> " + syxmId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

}