package power.ejb.run.timework;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity RunCTimework.
 * 
 * @see power.ejb.run.timework.RunCTimework
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCTimeworkFacade implements RunCTimeworkFacadeRemote {
	// property constants
	public static final String WORK_TYPE = "workType";
	public static final String WORK_ITEM_CODE = "workItemCode";
	public static final String WORK_ITEM_NAME = "workItemName";
	public static final String MACHPROF_CODE = "machprofCode";
	public static final String OPERATOR = "operator";
	public static final String PROTECTOR = "protector";
	public static final String CYCLE = "cycle";
	public static final String WORK_RANGE_TYPE = "workRangeType";
	public static final String CYCLE_NUMBER = "cycleNumber";
	public static final String WEEK_NO = "weekNo";
	public static final String WEEK_DAY = "weekDay";
	public static final String CLASS_SEQUENCE = "classSequence";
	public static final String CYCLE_SEQUENCE = "cycleSequence";
	public static final String DELAYTYPE = "delaytype";
	public static final String MEMO = "memo";
	public static final String IMPORTANTLVL = "importantlvl";
	public static final String WORK_EXPLAIN = "workExplain";
	public static final String IFEXPLAIN = "ifexplain";
	public static final String IFIMAGE = "ifimage";
	public static final String IFCHECK = "ifcheck";
	public static final String IFTEST = "iftest";
	public static final String IFOPTICKET = "ifopticket";
	public static final String OPTICKET_CODE = "opticketCode";
	public static final String STATUS = "status";
	public static final String ENTERPRISECODE = "enterprisecode";
	public static final String IS_USE = "isUse";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved RunCTimework entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCTimework entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public long save(RunCTimework entity) {
		try {
			SimpleDateFormat codeFormat = new SimpleDateFormat("yyyyMMddmmss");
			Date codevalue = new Date();
			entity.setWorkItemCode("PW" + codeFormat.format(codevalue));
			if (CheckCode(entity.getWorkItemCode())) {
				if (entity.getId() == null) {
					entity.setId(dll.getMaxId("RUN_C_TIMEWORK", "ID"));
				}
				entityManager.persist(entity);
				return entity.getId();
			} else {
				return 0;
			}

		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunCTimework entity.
	 * 
	 * @param entity
	 *            RunCTimework entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCTimework entity) {
		LogUtil.log("deleting RunCTimework instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunCTimework.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunCTimework entity and return it or a copy of
	 * it to the sender. A copy of the RunCTimework entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            RunCTimework entity to update
	 * @return RunCTimework the persisted RunCTimework entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCTimework update(RunCTimework entity) {
		LogUtil.log("updating RunCTimework instance", Level.INFO, null);
		try {
			try {
				updatechild(entity.getStatus(), entity.getIsUse(), entity
						.getWorkItemCode());
			} catch (Exception e) {
			}
			RunCTimework result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	protected int updatechild(String status, String isuse, String workitemcode) {
		try {
			String sql = "update RUN_C_TIMEWORKD set status='" + status
					+ "',is_use='" + isuse + "' where work_item_code='"
					+ workitemcode + "'";
			int i = dll.exeNativeSQL(sql);
			return i;
		} catch (RuntimeException e) {
			LogUtil.log("update kids failed", Level.SEVERE, e);
			throw e;
		}
	}

	public RunCTimework findById(Long id) {
		LogUtil.log("finding RunCTimework instance with id: " + id, Level.INFO,
				null);
		try {
			RunCTimework instance = entityManager.find(RunCTimework.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunCTimework entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCTimework property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunCTimework> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunCTimework> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCTimework instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCTimework model where model."
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

	public List<RunCTimework> findByWorkType(Object workType,
			int... rowStartIdxAndCount) {
		return findByProperty(WORK_TYPE, workType, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByWorkItemCode(Object workItemCode,
			int... rowStartIdxAndCount) {
		return findByProperty(WORK_ITEM_CODE, workItemCode, rowStartIdxAndCount);
	}

	// 根据编号取定期工作主记录
	@SuppressWarnings("unchecked")
	public RunCTimework findByWorkItemCode(String workitemcode) {
		String sql = "select * from RUN_C_TIMEWORK t where t.work_item_code='"
				+ workitemcode + "' ";
		List<RunCTimework> list = dll.queryByNativeSQL(sql, RunCTimework.class);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public List<RunCTimework> findByWorkItemName(Object workItemName,
			int... rowStartIdxAndCount) {
		return findByProperty(WORK_ITEM_NAME, workItemName, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByMachprofCode(Object machprofCode,
			int... rowStartIdxAndCount) {
		return findByProperty(MACHPROF_CODE, machprofCode, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByOperator(Object operator,
			int... rowStartIdxAndCount) {
		return findByProperty(OPERATOR, operator, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByProtector(Object protector,
			int... rowStartIdxAndCount) {
		return findByProperty(PROTECTOR, protector, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByCycle(Object cycle,
			int... rowStartIdxAndCount) {
		return findByProperty(CYCLE, cycle, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByWorkRangeType(Object workRangeType,
			int... rowStartIdxAndCount) {
		return findByProperty(WORK_RANGE_TYPE, workRangeType,
				rowStartIdxAndCount);
	}

	public List<RunCTimework> findByCycleNumber(Object cycleNumber,
			int... rowStartIdxAndCount) {
		return findByProperty(CYCLE_NUMBER, cycleNumber, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByWeekNo(Object weekNo,
			int... rowStartIdxAndCount) {
		return findByProperty(WEEK_NO, weekNo, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByWeekDay(Object weekDay,
			int... rowStartIdxAndCount) {
		return findByProperty(WEEK_DAY, weekDay, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByClassSequence(Object classSequence,
			int... rowStartIdxAndCount) {
		return findByProperty(CLASS_SEQUENCE, classSequence,
				rowStartIdxAndCount);
	}

	public List<RunCTimework> findByCycleSequence(Object cycleSequence,
			int... rowStartIdxAndCount) {
		return findByProperty(CYCLE_SEQUENCE, cycleSequence,
				rowStartIdxAndCount);
	}

	public List<RunCTimework> findByDelaytype(Object delaytype,
			int... rowStartIdxAndCount) {
		return findByProperty(DELAYTYPE, delaytype, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByMemo(Object memo,
			int... rowStartIdxAndCount) {
		return findByProperty(MEMO, memo, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByImportantlvl(Object importantlvl,
			int... rowStartIdxAndCount) {
		return findByProperty(IMPORTANTLVL, importantlvl, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByWorkExplain(Object workExplain,
			int... rowStartIdxAndCount) {
		return findByProperty(WORK_EXPLAIN, workExplain, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByIfexplain(Object ifexplain,
			int... rowStartIdxAndCount) {
		return findByProperty(IFEXPLAIN, ifexplain, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByIfimage(Object ifimage,
			int... rowStartIdxAndCount) {
		return findByProperty(IFIMAGE, ifimage, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByIfcheck(Object ifcheck,
			int... rowStartIdxAndCount) {
		return findByProperty(IFCHECK, ifcheck, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByIftest(Object iftest,
			int... rowStartIdxAndCount) {
		return findByProperty(IFTEST, iftest, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByIfopticket(Object ifopticket,
			int... rowStartIdxAndCount) {
		return findByProperty(IFOPTICKET, ifopticket, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByOpticketCode(Object opticketCode,
			int... rowStartIdxAndCount) {
		return findByProperty(OPTICKET_CODE, opticketCode, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByStatus(Object status,
			int... rowStartIdxAndCount) {
		return findByProperty(STATUS, status, rowStartIdxAndCount);
	}

	public List<RunCTimework> findByEnterprisecode(Object enterprisecode,
			int... rowStartIdxAndCount) {
		return findByProperty(ENTERPRISECODE, enterprisecode,
				rowStartIdxAndCount);
	}

	public List<RunCTimework> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	@SuppressWarnings("unchecked")
	public PageObject findByIsUse(Object isUse, Object CenterWorkType,
			Object CenterMachprofCode, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select * from RUN_C_TIMEWORK t where t.is_use='Y' ";
			if (CenterWorkType != null) {
				if (!CenterWorkType.toString().equals("")) {
					sql += "and t.WORK_TYPE='" + CenterWorkType + "' ";
				}
			}
			if (CenterMachprofCode != null) {
				if (!CenterMachprofCode.toString().equals("")) {
					sql += "and t.MACHPROF_CODE='" + CenterMachprofCode + "'";
				}
			}
			sql += "order by id desc";
			String sqlCount = "select count(1) from RUN_C_TIMEWORK t where t.is_use='Y'";
			if (CenterWorkType != null) {
				if (!CenterWorkType.toString().equals("")) {
					sqlCount += "and t.WORK_TYPE='" + CenterWorkType + "' ";
				}
			}
			if (CenterMachprofCode != null) {
				if (!CenterMachprofCode.toString().equals("")) {
					sqlCount += "and t.MACHPROF_CODE='" + CenterMachprofCode
							+ "'";
				}
			}
			List<RunCTimework> list = dll.queryByNativeSQL(sql,
					RunCTimework.class, rowStartIdxAndCount);
			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RunCTimework> findToGenerate(String enterpriseCode,
			int... rowStartIdxAndCount) {
		String sql = "select * from RUN_C_TIMEWORK t where t.is_use='Y' and STATUS='C' and ENTERPRISECODE='"
				+ enterpriseCode + "' order by id desc";
		List<RunCTimework> list = dll.queryByNativeSQL(sql, RunCTimework.class,
				rowStartIdxAndCount);
		return list;
	}

	/**
	 * Find all RunCTimework entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCTimework> all RunCTimework entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunCTimework> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCTimework instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCTimework model";
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

	/**
	 * 判断定期工作编号是否重复
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param workitemcode
	 *            定期工作编号
	 * @param Id
	 *            主键
	 * @return boolean
	 */
	private boolean CheckCode(String workitemcode, Long... Id) {
		boolean isSame = true;
		String sql = "select count(1) from run_c_timework where IS_USE='Y' and WORK_ITEM_CODE='"
				+ workitemcode + "' ";
		if (Id != null && Id.length > 0) {
			sql += "Id <> " + Id[0];
		}
		if (Long.parseLong((dll.getSingal(sql).toString())) > 0) {
			isSame = false;
		}
		return isSame;
	}

	/**
	 * 根据数据判断生成定期工作
	 * 
	 * 
	 */
	public long MakeTimeworklist() {
		long i = 0;

		return i;
	}
}