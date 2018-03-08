package power.ejb.run.runlog.shift;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.base.EquCBug;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity RunJEquRunStatusHis.
 * 
 * @see power.ejb.run.runlog.shift.RunJEquRunStatusHis
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJEquRunStatusHisFacade implements
		RunJEquRunStatusHisFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved RunJEquRunStatusHis
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            RunJEquRunStatusHis entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunJEquRunStatusHis entity) {
		LogUtil.log("saving RunJEquRunStatusHis instance", Level.INFO, null);
		try {
			if (entity.getRunStatusHisId() == null) {
				entity.setRunStatusHisId(bll.getMaxId(
						"run_j_equ_run_status_his", "run_status_his_id"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunJEquRunStatusHis entity.
	 * 
	 * @param entity
	 *            RunJEquRunStatusHis entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunJEquRunStatusHis entity) {
		LogUtil.log("deleting RunJEquRunStatusHis instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunJEquRunStatusHis.class,
					entity.getRunStatusHisId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunJEquRunStatusHis entity and return it or a
	 * copy of it to the sender. A copy of the RunJEquRunStatusHis entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            RunJEquRunStatusHis entity to update
	 * @return RunJEquRunStatusHis the persisted RunJEquRunStatusHis entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunJEquRunStatusHis update(RunJEquRunStatusHis entity) {
		LogUtil.log("updating RunJEquRunStatusHis instance", Level.INFO, null);
		try {
			RunJEquRunStatusHis result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJEquRunStatusHis findById(Long id) {
		LogUtil.log("finding RunJEquRunStatusHis instance with id: " + id,
				Level.INFO, null);
		try {
			RunJEquRunStatusHis instance = entityManager.find(
					RunJEquRunStatusHis.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunJEquRunStatusHis entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunJEquRunStatusHis property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunJEquRunStatusHis> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunJEquRunStatusHis> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunJEquRunStatusHis instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunJEquRunStatusHis model where model."
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
	 * Find all RunJEquRunStatusHis entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJEquRunStatusHis> all RunJEquRunStatusHis entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunJEquRunStatusHis> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunJEquRunStatusHis instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from RunJEquRunStatusHis model";
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

	@SuppressWarnings("unchecked")
	public PageObject findList(String strWhere,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select * from  run_j_equ_run_status_his \n";
			if (strWhere != "") {
				sql = sql + " where  " + strWhere;
			}
			List<RunJEquRunStatusHis> list = bll.queryByNativeSQL(sql,
					RunJEquRunStatusHis.class, rowStartIdxAndCount);
			String sqlCount = "select count(*)　from run_j_equ_run_status_his \n";
			if (strWhere != "") {
				sqlCount = sqlCount + " where  " + strWhere;
			}
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;

		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}

	}

	/**
	 * 取得某设备最近一次状态变化记录
	 * 
	 * @param equcode
	 * @param enterprisecode
	 * @return
	 */
	public List<RunJEquRunStatusHis> getModelByStatus(String equcode,
			String enterprisecode) {
		String strWhere = "attribute_code='" + equcode + "'\n"
				+ "and enterprise_code='" + enterprisecode + "'\n"
				+ "and is_use='Y'";
		PageObject result = findList(strWhere);
		return result.getList();
	}

	/**
	 * 根据设备功能码，企业编码查询设备状态变更列表
	 */
	public List getEquStatusHisList(String equCode,
			String enterpriseCode) {
		String sql = "select \n"
				+ "		 run_status_his_id,\n"
				+ "      run_logid,\n"
				+ "      run_logno,\n"
				+ "      attribute_code,\n"
				+ "      equ_name,\n"
				+ "      from_status_id,\n"
				+ "      from_status_name,\n"
				+ "      to_status_id,\n"
				+ "      to_status_name,\n"
				+ "      standing_time,\n"
				+ "      operater_by,\n"
				+ "      getworkername(operater_by) worker_name,\n"
				+ "      to_char(operate_time, 'yyyy-mm-dd hh:mi:ss') operate_time,\n"
				+ "      operate_memo ,\n" 
				+ "      enterprise_code \n"
				+ "  from run_j_equ_run_status_his h\n"
				+ " where h.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and h.attribute_code = '" + equCode + "'\n"
				+ "   and h.is_use = 'Y'\n" + " order by h.operate_time desc";

		return bll.queryByNativeSQL(sql);

	}
}