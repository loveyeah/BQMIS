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

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity RunJTimework.
 * 
 * @see power.ejb.run.timework.RunJTimework
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJTimeworkFacade implements RunJTimeworkFacadeRemote {
	// property constants
	public static final String WORK_ITEM_CODE = "workItemCode";
	public static final String MACHPROF_CODE = "machprofCode";
	public static final String WORK_TYPE = "workType";
	public static final String WORK_ITEM_NAME = "workItemName";
	public static final String CYCLE = "cycle";
	public static final String CLASS_SEQUENCE = "classSequence";
	public static final String CLASS_TEAM = "classTeam";
	public static final String DUTYTYPE = "dutytype";
	public static final String IFDELAY = "ifdelay";
	public static final String DELAYRESULT = "delayresult";
	public static final String DELAYMAN = "delayman";
	public static final String DELAYTYPE = "delaytype";
	public static final String OP_TICKET = "opTicket";
	public static final String WORKRESULT = "workresult";
	public static final String WORK_EXPLAIN = "workExplain";
	public static final String IFCHECK = "ifcheck";
	public static final String CHECKMAN = "checkman";
	public static final String CHECKRESULT = "checkresult";
	public static final String IFIMAGE = "ifimage";
	public static final String IMAGECODE = "imagecode";
	public static final String IF_EXPLAIN = "ifExplain";
	public static final String MEMO = "memo";
	public static final String OPERATOR = "operator";
	public static final String PROTECTOR = "protector";
	public static final String IF_OPTICKET = "ifOpticket";
	public static final String CONMAN = "conman";
	public static final String STATUS = "status";
	public static final String ENTERPRISECODE = "enterprisecode";
	public static final String IS_USE = "isUse";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved RunJTimework entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunJTimework entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public long save(RunJTimework entity) {
		try {
			if (entity.getId() == null) {
				entity.setId(dll.getMaxId("RUN_J_TIMEWORK", "ID"));
			}
			entityManager.persist(entity);
			return entity.getId();
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public long saveTemp(RunJTimework entity) {
		try {
			if (entity.getId() == null) {
				entity.setId(dll.getMaxId("RUN_J_TIMEWORK", "ID"));
			}
			SimpleDateFormat codeFormat = new SimpleDateFormat("yyyyMMddmmss");
			Date codevalue = new Date();
			entity.setWorkDate(new java.util.Date());
			entity.setWorkItemCode("PWT" + codeFormat.format(codevalue));
			entityManager.persist(entity);
			return entity.getId();
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunJTimework entity.
	 * 
	 * @param entity
	 *            RunJTimework entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunJTimework entity) {
		LogUtil.log("deleting RunJTimework instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunJTimework.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunJTimework entity and return it or a copy of
	 * it to the sender. A copy of the RunJTimework entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            RunJTimework entity to update
	 * @return RunJTimework the persisted RunJTimework entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunJTimework update(RunJTimework entity) {
		LogUtil.log("updating RunJTimework instance", Level.INFO, null);
		try {
			RunJTimework result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJTimework findById(Long id) {
		LogUtil.log("finding RunJTimework instance with id: " + id, Level.INFO,
				null);
		try {
			RunJTimework instance = entityManager.find(RunJTimework.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunJTimework entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunJTimework property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunJTimework> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunJTimework> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunJTimework instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunJTimework model where model."
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

	public List<RunJTimework> findByWorkItemCode(Object workItemCode,
			int... rowStartIdxAndCount) {
		return findByProperty(WORK_ITEM_CODE, workItemCode, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByMachprofCode(Object machprofCode,
			int... rowStartIdxAndCount) {
		return findByProperty(MACHPROF_CODE, machprofCode, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByWorkType(Object workType,
			int... rowStartIdxAndCount) {
		return findByProperty(WORK_TYPE, workType, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByWorkItemName(Object workItemName,
			int... rowStartIdxAndCount) {
		return findByProperty(WORK_ITEM_NAME, workItemName, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByCycle(Object cycle,
			int... rowStartIdxAndCount) {
		return findByProperty(CYCLE, cycle, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByClassSequence(Object classSequence,
			int... rowStartIdxAndCount) {
		return findByProperty(CLASS_SEQUENCE, classSequence,
				rowStartIdxAndCount);
	}

	public List<RunJTimework> findByClassTeam(Object classTeam,
			int... rowStartIdxAndCount) {
		return findByProperty(CLASS_TEAM, classTeam, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByDutytype(Object dutytype,
			int... rowStartIdxAndCount) {
		return findByProperty(DUTYTYPE, dutytype, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByIfdelay(Object ifdelay,
			int... rowStartIdxAndCount) {
		return findByProperty(IFDELAY, ifdelay, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByDelayresult(Object delayresult,
			int... rowStartIdxAndCount) {
		return findByProperty(DELAYRESULT, delayresult, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByDelayman(Object delayman,
			int... rowStartIdxAndCount) {
		return findByProperty(DELAYMAN, delayman, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByDelaytype(Object delaytype,
			int... rowStartIdxAndCount) {
		return findByProperty(DELAYTYPE, delaytype, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByOpTicket(Object opTicket,
			int... rowStartIdxAndCount) {
		return findByProperty(OP_TICKET, opTicket, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByWorkresult(Object workresult,
			int... rowStartIdxAndCount) {
		return findByProperty(WORKRESULT, workresult, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByWorkExplain(Object workExplain,
			int... rowStartIdxAndCount) {
		return findByProperty(WORK_EXPLAIN, workExplain, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByIfcheck(Object ifcheck,
			int... rowStartIdxAndCount) {
		return findByProperty(IFCHECK, ifcheck, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByCheckman(Object checkman,
			int... rowStartIdxAndCount) {
		return findByProperty(CHECKMAN, checkman, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByCheckresult(Object checkresult,
			int... rowStartIdxAndCount) {
		return findByProperty(CHECKRESULT, checkresult, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByIfimage(Object ifimage,
			int... rowStartIdxAndCount) {
		return findByProperty(IFIMAGE, ifimage, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByImagecode(Object imagecode,
			int... rowStartIdxAndCount) {
		return findByProperty(IMAGECODE, imagecode, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByIfExplain(Object ifExplain,
			int... rowStartIdxAndCount) {
		return findByProperty(IF_EXPLAIN, ifExplain, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByMemo(Object memo,
			int... rowStartIdxAndCount) {
		return findByProperty(MEMO, memo, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByOperator(Object operator,
			int... rowStartIdxAndCount) {
		return findByProperty(OPERATOR, operator, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByProtector(Object protector,
			int... rowStartIdxAndCount) {
		return findByProperty(PROTECTOR, protector, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByIfOpticket(Object ifOpticket,
			int... rowStartIdxAndCount) {
		return findByProperty(IF_OPTICKET, ifOpticket, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByConman(Object conman,
			int... rowStartIdxAndCount) {
		return findByProperty(CONMAN, conman, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByStatus(Object status,
			int... rowStartIdxAndCount) {
		return findByProperty(STATUS, status, rowStartIdxAndCount);
	}

	public List<RunJTimework> findByEnterprisecode(Object enterprisecode,
			int... rowStartIdxAndCount) {
		return findByProperty(ENTERPRISECODE, enterprisecode,
				rowStartIdxAndCount);
	}

	public List<RunJTimework> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	/**
	 * Find all RunJTimework entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJTimework> all RunJTimework entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunJTimework> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunJTimework instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunJTimework model";
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
	public PageObject findByIsUse(Object object, String queryWorkType,
			String queryRunType, String queryApproveType,
			String queryDelayType, String querystimeDate,
			String queryetimeDate, String queryWorkitemName,
			String queryMachprofCode, String queryWorkResult,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();

			String sql = "SELECT t.*,\n"
					+ "       getspecialname(t.machprof_code) machprofName,\n"
					+ "       gettimeworktypename(t.work_type) worktypeName,\n"
					+ "       (SELECT r.shift_time_name\n"
					+ "          FROM run_c_shift_time r\n"
					+ "         WHERE r.shift_time_id = t.class_sequence) classsequenceName,\n"
					+ "       (SELECT s.shift_name\n"
					+ "          FROM run_c_shift s\n"
					+ "         WHERE to_char(s.shift_id) = t.dutytype) dutytypeName,\n"
					+ "       decode(t.ifdelay, 'Y', '已申请延迟', 'N', '', '', '') ifdelayNm,\n"
					+ "       decode(t.delayresult, '1', '待延期', '2', '已延期', '3', '不可延期','','') delayresultNm,\n"
					+ "       GETWORKERNAME(t.delayman) delaymanName,\n"
					+ "       decode(t.workresult, '1', '正常', '2', '不正常', '3', '因故未作', '4', '取消', '', '') workresultNm,\n"
					+ "       decode(t.ifcheck, 'Y', '要审批', 'N', '直接执行', '', '') ifcheckNm,\n"
					+ "       GETWORKERNAME(t.checkman) checkmanName,\n"
					+ "       decode(t.checkresult, '0', '未审批', '1', '同意', '2', '不同意') checkresultNm,\n"
					+ "       decode(t.ifimage, 'Y', '要做事故预想', 'N', '') ifimageNm,\n"
					+ "       decode(t.if_explain, 'Y', '必填实验说明', 'N', '') ifexplainNm,\n"
					+ "       GETWORKERNAME(t.operator) operatorName,\n"
					+ "       GETWORKERNAME(t.protector) protectorName,\n"
					+ "       decode(t.if_opticket, 'Y', '关联操作票', 'N', '') ifopticketNm,\n"
					+ "       GETWORKERNAME(t.conman) conmanName,\n"
					+ "       decode(t.status,'0','待执行','1','未上报','2','已上报','3','已结束','4','已发送','5','已下发','6','未执行','7','已执行') statusNm,\n"
					+ "		  to_char(t.work_date, 'YYYY-MM-DD HH24:MI:SS'),\n"
					+ "		  to_char(t.delaydate, 'YYYY-MM-DD HH24:MI:SS'),\n"
					+ "		  to_char(t.checkdate, 'YYYY-MM-DD HH24:MI:SS'),\n"
					+ "		  to_char(t.deling_date, 'YYYY-MM-DD HH24:MI:SS'),\n"
					+ "		  to_char(t.delay_date, 'YYYY-MM-DD HH24:MI:SS'),\n"
					+ "		  to_char(t.condate, 'YYYY-MM-DD HH24:MI:SS')\n"
					+ "  FROM run_j_timework t\n" + " where t.is_use='Y'";
			String sqlCount = "select count(1) from RUN_J_TIMEWORK t where t.is_use='Y'";
			if (queryWorkType != null) {
				if (!queryWorkType.toString().equals("")) {
					sql += "and t.WORK_TYPE='" + queryWorkType + "' ";
					sqlCount += "and t.WORK_TYPE='" + queryWorkType + "' ";
				}
			}
			if (queryRunType != null) {
				if (!queryRunType.toString().equals("")) {
					sql += "and t.IFCHECK='" + queryRunType + "'";
					sqlCount += "and t.IFCHECK='" + queryRunType + "' ";
				}
			}
			if (queryApproveType != null) {
				if (!queryApproveType.toString().equals("")) {
					sql += "and t.STATUS='" + queryApproveType + "'";
					sqlCount += "and t.STATUS='" + queryApproveType + "' ";
				}
			}
			if (queryDelayType != null) {
				if (!queryDelayType.toString().equals("")) {
					sql += "and t.IFDELAY='" + queryDelayType + "'";
					sqlCount += "and t.IFDELAY='" + queryDelayType + "' ";
				}
			}
			if (querystimeDate != null && !querystimeDate.equals("")) {
				sql += "  and t.work_date >=to_date('" + querystimeDate
						+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
				sqlCount += "  and t.work_date >=to_date('" + querystimeDate
						+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
			}
			if (queryetimeDate != null && !queryetimeDate.equals("")) {
				sql += "  and t.work_date <=to_date('" + queryetimeDate
						+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
				sqlCount += "  and t.work_date <=to_date('" + queryetimeDate
						+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
			}
			if (queryWorkitemName != null) {
				if (!queryWorkitemName.toString().equals("")) {
					sql += "and t.WORK_ITEM_NAME='" + queryWorkitemName + "'";
					sqlCount += "and t.WORK_ITEM_NAME='" + queryWorkitemName
							+ "' ";
				}
			}
			if (queryMachprofCode != null) {
				if (!queryMachprofCode.toString().equals("")) {
					sql += "and t.MACHPROF_CODE='" + queryMachprofCode + "'";
					sqlCount += "and t.MACHPROF_CODE='" + queryMachprofCode
							+ "' ";
				}
			}
			if (queryWorkResult != null) {
				if (!queryWorkResult.toString().equals("")) {
					sql += "and t.WORKRESULT='" + queryWorkResult + "'";
					sqlCount += "and t.WORKRESULT='" + queryWorkResult + "' ";
				}
			}
			sql += "order by id desc";
			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
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
	public PageObject findByIsOther(Object object, String queryWorkType,
			String queryRunType, String queryApproveType,
			String queryDelayType, String querystimeDate,
			String queryetimeDate, String queryWorkitemName,
			String queryMachprofCode, String queryWorkResult,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();

			String sql = "SELECT t.*,\n"
					+ "       getspecialname(t.machprof_code) machprofName,\n"
					+ "       gettimeworktypename(t.work_type) worktypeName,\n"
					+ "       (SELECT r.shift_time_name\n"
					+ "          FROM run_c_shift_time r\n"
					+ "         WHERE r.shift_time_id = t.class_sequence) classsequenceName,\n"
					+ "       (SELECT s.shift_name\n"
					+ "          FROM run_c_shift s\n"
					+ "         WHERE to_char(s.shift_id) = t.dutytype) dutytypeName,\n"
					+ "       decode(t.ifdelay, 'Y', '已申请延迟', 'N', '', '', '') ifdelayNm,\n"
					+ "       decode(t.delayresult, '1', '待延期', '2', '已延期', '3', '不可延期','','') delayresultNm,\n"
					+ "       GETWORKERNAME(t.delayman) delaymanName,\n"
					+ "       decode(t.workresult, '1', '正常', '2', '不正常', '3', '因故未作', '4', '取消', '', '') workresultNm,\n"
					+ "       decode(t.ifcheck, 'Y', '要审批', 'N', '直接执行', '', '') ifcheckNm,\n"
					+ "       GETWORKERNAME(t.checkman) checkmanName,\n"
					+ "       decode(t.checkresult, '0', '未审批', '1', '同意', '2', '不同意') checkresultNm,\n"
					+ "       decode(t.ifimage, 'Y', '要做事故预想', 'N', '') ifimageNm,\n"
					+ "       decode(t.if_explain, 'Y', '必填实验说明', 'N', '') ifexplainNm,\n"
					+ "       GETWORKERNAME(t.operator) operatorName,\n"
					+ "       GETWORKERNAME(t.protector) protectorName,\n"
					+ "       decode(t.if_opticket, 'Y', '关联操作票', 'N', '') ifopticketNm,\n"
					+ "       GETWORKERNAME(t.conman) conmanName,\n"
					+ "       decode(t.status,'0','待执行','1','未上报','2','已上报','3','已结束','4','已发送','5','已下发','6','未执行','7','已执行') statusNm,\n"
					+ "		  to_char(t.work_date, 'YYYY-MM-DD HH24:MI:SS'),\n"
					+ "		  to_char(t.delaydate, 'YYYY-MM-DD HH24:MI:SS'),\n"
					+ "		  to_char(t.checkdate, 'YYYY-MM-DD HH24:MI:SS'),\n"
					+ "		  to_char(t.deling_date, 'YYYY-MM-DD HH24:MI:SS'),\n"
					+ "		  to_char(t.delay_date, 'YYYY-MM-DD HH24:MI:SS'),\n"
					+ "		  to_char(t.condate, 'YYYY-MM-DD HH24:MI:SS')\n"
					+ "  FROM run_j_timework t\n" + " where t.is_use='Y' ";
			String sqlCount = "select count(1) from RUN_J_TIMEWORK t where t.is_use='Y' ";

			if (!("").equals(queryWorkType.toString())) {
				if (("delay").equals(queryWorkType)) {
					sql += "and t.IFDELAY='Y' ";
					sqlCount += "and t.IFDELAY='Y' ";
				} else if (("temp").equals(queryWorkType)) {
					sql += "and t.CLASS_TEAM='Y' ";
					sqlCount += "and t.CLASS_TEAM='Y' ";
				} else if (("ed").equals(queryWorkType)) {
					sql += "and t.STATUS in (3,6,7) ";
					sqlCount += "and t.STATUS in (3,6,7) ";
				} else {
				}
			}
			if (queryRunType != null) {
				if (!queryRunType.toString().equals("")) {
					sql += "and t.IFCHECK='" + queryRunType + "'";
					sqlCount += "and t.IFCHECK='" + queryRunType + "' ";
				}
			}
			if (queryApproveType != null) {
				if (!queryApproveType.toString().equals("")) {
					sql += "and t.STATUS='" + queryApproveType + "'";
					sqlCount += "and t.STATUS='" + queryApproveType + "' ";
				}
			}
			if (queryDelayType != null) {
				if (!queryDelayType.toString().equals("")) {
					sql += "and t.IFDELAY='" + queryDelayType + "'";
					sqlCount += "and t.IFDELAY='" + queryDelayType + "' ";
				}
			}
			if (querystimeDate != null && !querystimeDate.equals("")) {
				sql += "  and t.work_date >=to_date('" + querystimeDate
						+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
				sqlCount += "  and t.work_date >=to_date('" + querystimeDate
						+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
			}
			if (queryetimeDate != null && !queryetimeDate.equals("")) {
				sql += "  and t.work_date <=to_date('" + queryetimeDate
						+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
				sqlCount += "  and t.work_date <=to_date('" + queryetimeDate
						+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
			}
			if (queryWorkitemName != null) {
				if (!queryWorkitemName.toString().equals("")) {
					sql += "and t.WORK_ITEM_NAME='" + queryWorkitemName + "'";
					sqlCount += "and t.WORK_ITEM_NAME='" + queryWorkitemName
							+ "' ";
				}
			}
			if (queryMachprofCode != null) {
				if (!queryMachprofCode.toString().equals("")) {
					sql += "and t.MACHPROF_CODE='" + queryMachprofCode + "'";
					sqlCount += "and t.MACHPROF_CODE='" + queryMachprofCode
							+ "' ";
				}
			}
			if (queryWorkResult != null) {
				if (!queryWorkResult.toString().equals("")) {
					sql += "and t.WORKRESULT='" + queryWorkResult + "'";
					sqlCount += "and t.WORKRESULT='" + queryWorkResult + "' ";
				}
			}
			sql += "order by id desc";
			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
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
	public PageObject getlistRun(Object object, String queryWorkType,
			String queryRunType, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select * from RUN_J_TIMEWORK t where t.is_use='Y' ";
			String sqlCount = "select count(1) from RUN_J_TIMEWORK t where t.is_use='Y'";
			if (queryWorkType != null) {
				if (!queryWorkType.toString().equals("")) {
					sql += "and t.WORK_TYPE='" + queryWorkType + "' ";
					sqlCount += "and t.WORK_TYPE='" + queryWorkType + "' ";
				}
			}
			if (queryRunType != null) {
				if (!queryRunType.toString().equals("")) {
					sql += "and t.IFCHECK='" + queryRunType + "'";
					sqlCount += "and t.IFCHECK='" + queryRunType + "' ";
				}
			}
			sql += "order by id desc";
			List<RunJTimework> list = dll.queryByNativeSQL(sql,
					RunJTimework.class, rowStartIdxAndCount);
			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}
}