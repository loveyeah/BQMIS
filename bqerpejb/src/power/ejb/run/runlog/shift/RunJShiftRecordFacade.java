package power.ejb.run.runlog.shift;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity RunJShiftRecord.
 * 
 * @see power.ejb.run.runlog.shift.RunJShiftRecord
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJShiftRecordFacade implements RunJShiftRecordFacadeRemote {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	public Long getMaxId() {
		return bll.getMaxId("run_j_shift_record", "shift_record_id");
	}

	/**
	 * Perform an initial save of a previously unsaved RunJShiftRecord entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunJShiftRecord entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunJShiftRecord entity) {
		LogUtil.log("saving RunJShiftRecord instance", Level.INFO, null);
		try {
			if (entity.getShiftRecordId() == null) {
				entity.setShiftRecordId(bll.getMaxId("run_j_shift_record",
						"shift_record_id"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Perform an initial save of a previously unsaved RunJShiftRecord entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunJShiftRecord entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void saves(RunJShiftRecord entity) {
		LogUtil.log("saving RunJShiftRecord instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunJShiftRecord entity.
	 * 
	 * @param entity
	 *            RunJShiftRecord entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunJShiftRecord entity) {
		LogUtil.log("deleting RunJShiftRecord instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunJShiftRecord.class, entity
					.getShiftRecordId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunJShiftRecord entity and return it or a copy
	 * of it to the sender. A copy of the RunJShiftRecord entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunJShiftRecord entity to update
	 * @return RunJShiftRecord the persisted RunJShiftRecord entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunJShiftRecord update(RunJShiftRecord entity) {
		LogUtil.log("updating RunJShiftRecord instance", Level.INFO, null);
		try {
			RunJShiftRecord result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJShiftRecord findById(Long id) {
		LogUtil.log("finding RunJShiftRecord instance with id: " + id,
				Level.INFO, null);
		try {
			RunJShiftRecord instance = entityManager.find(
					RunJShiftRecord.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunJShiftRecord entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunJShiftRecord property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunJShiftRecord> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunJShiftRecord> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunJShiftRecord instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunJShiftRecord model where model."
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
	 * Find all RunJShiftRecord entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJShiftRecord> all RunJShiftRecord entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunJShiftRecord> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunJShiftRecord instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunJShiftRecord model";
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
	 * 查找没有状态的值班记事
	 * 
	 * @param runLogId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJShiftRecord> all RunJShiftRecord entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunJShiftRecord> findNoStatus(Long runLogId,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunJShiftRecord instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunJShiftRecord model where model.runLogId = '"
					+ runLogId
					+ "' and model.isUse = 'Y' and model.isCompletion is null and model.enterpriseCode = '"
					+ enterpriseCode + "'";
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
			List<RunJShiftRecord> list = query.getResultList();

			for (int i = 0; i < list.size(); i++) {
				list.get(i).setIsCompletion("Y");
				this.update(list.get(i));
			}

			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunJShiftRecord entities.
	 * 
	 * @param runLogId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJShiftRecord> all RunJShiftRecord entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunJShiftRecord> findNotCompletion(Long runLogId,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunJShiftRecord instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunJShiftRecord model where model.runLogId = '"
					+ runLogId
					+ "' and model.isUse = 'Y' and model.isCompletion = 'N' and model.enterpriseCode = '"
					+ enterpriseCode + "'";
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
	 * Find all RunJShiftRecord entities.
	 * 
	 * @param runLogId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJShiftRecord> all RunJShiftRecord entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunJShiftRecord> findAll(Long runLogId, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunJShiftRecord instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunJShiftRecord model where model.runLogId = '"
					+ runLogId
					+ "' and model.isUse = 'Y' and model.enterpriseCode = '"
					+ enterpriseCode + "'";
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

	// /**
	// * Find all RunJShiftRecord entities.
	// *
	// * @param enterpriseCode
	// * @param rowStartIdxAndCount
	// * Optional int varargs. rowStartIdxAndCount[0] specifies the the
	// * row index in the query result-set to begin collecting the
	// * results. rowStartIdxAndCount[1] specifies the the maximum
	// * count of results to return.
	// * @return List<RunJShiftRecord> all RunJShiftRecord entities
	// */
	// @SuppressWarnings("unchecked")
	// public List<RunJShiftRecord> findListByRunLogId(String runlogid,
	// String enterpriseCode, final int... rowStartIdxAndCount) {
	// LogUtil.log("finding all RunJShiftRecord instances", Level.INFO, null);
	// try {
	// final String queryString = "select model from RunJShiftRecord model where
	// model.isUse = 'Y' and model.enterpriseCode = '"
	// + enterpriseCode
	// + "' and model.runLogId = '"
	// + runlogid
	// + "'";
	// Query query = entityManager.createQuery(queryString);
	// if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
	// int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
	// if (rowStartIdx > 0) {
	// query.setFirstResult(rowStartIdx);
	// }
	//
	// if (rowStartIdxAndCount.length > 1) {
	// int rowCount = Math.max(0, rowStartIdxAndCount[1]);
	// if (rowCount > 0) {
	// query.setMaxResults(rowCount);
	// }
	// }
	// }
	// return query.getResultList();
	// } catch (RuntimeException re) {
	// LogUtil.log("find all failed", Level.SEVERE, re);
	// throw re;
	// }
	// }

	/**
	 * 查找未完成项
	 * 
	 * @param runlogid
	 * @param enterpriseCode
	 * @param start
	 * @param limit
	 * @return List<RunJShiftRecrodForm>
	 */
	public PageObject findNotCompletionList(Long runLogId,
			String enterpriseCode, final int... rowStartIdxAndCount) {

		long count = 0;
		String strSql = "select count(1)\n" + "  from run_j_shift_record t\n"
				+ " where t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterpriseCode + "'\n" + "   and t.is_completion = 'N'\n"
				+ "   and t.run_log_id = '" + runLogId + "'\n";

		Object objCount = bll.getSingal(strSql);
		if (objCount != null) {
			count = Long.parseLong(objCount.toString());
		}

		strSql = "select (select t2.run_logno\n"
				+ "          from run_j_runlog_main t2\n"
				+ "         where t2.is_use = 'Y'\n"
				+ "           and t2.run_logid = '"
				+ runLogId
				+ "') run_logno,\n"
				+ "       t.shift_record_id,\n"
				+ "       t.run_log_id,\n"
				+ "       t.main_item_code,\n"
				+ "       (select t1.main_item_name\n"
				+ "          from run_c_main_item t1\n"
				+ "         where t1.is_use = 'Y'\n"
				+ "           and t1.main_item_code = t.main_item_code) main_item_name,\n"
				+ "       t.record_content,\n"
				+ "       t.file_path,\n"
				+ "(select t5.chs_name\n"
				+ "          from hr_j_emp_info t5\n"
				+ "         where t5.emp_code = t.record_by) record_by_name,"
				+ "       t.record_by,\n"
				+ "       to_char(t.record_time,'yyyy-mm-dd hh24:mi:ss'),\n"
				+ "       t.review_no,\n"
				+ "       t.review_type,\n"
				+ "       t.check_by,\n"
				+ "       t.check_memo,\n"
				+ "       t.check_time,\n"
				+ "       t.is_completion,\n"
				+ "       t.is_use,\n"
				+ "       t.enterprise_code,\n"
				+ "       t.not_completion_id\n"
				+ "  from run_j_shift_record t\n"
				+ " where t.is_use = 'Y'\n"
				+ "   and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "   and t.is_completion = 'N'\n"
				+ "   and t.run_log_id = '"
				+ runLogId + "'\n";

		List list = bll.queryByNativeSQL(strSql, rowStartIdxAndCount);
		List<RunJShiftRecrodForm> formlist = new ArrayList();
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			RunJShiftRecrodForm form = new RunJShiftRecrodForm();
			Object[] obj = (Object[]) list.get(i);
			if (obj[0] != null) {
				form.setRun_logno(obj[0].toString());
				str = str + "{'run_logno':'" + obj[0].toString() + "',";
			} else {
				form.setRun_logno("");
				str = str + "{'run_logno':'',";
			}
			if (obj[1] != null) {
				form.setShift_record_id(obj[1].toString());
				str = str + "'shift_record_id':'" + obj[1].toString() + "',";
			} else {
				form.setShift_record_id("");
				str = str + "'shift_record_id':'',";
			}
			if (obj[2] != null) {
				form.setRun_log_id(obj[2].toString());
				str = str + "'run_log_id':'" + obj[2].toString() + "',";
			} else {
				form.setRun_log_id("");
				str = str + "'run_log_id':'',";
			}
			if (obj[3] != null) {
				form.setMain_item_code(obj[3].toString());
				str = str + "'main_item_code':'" + obj[3].toString() + "',";
			} else {
				form.setMain_item_code("");
				str = str + "'main_item_code':'',";
			}
			if (obj[4] != null) {
				form.setMain_item_name(obj[4].toString());
				str = str + "'main_item_name':'" + obj[4].toString() + "',";
			} else {
				form.setMain_item_name("");
				str = str + "'main_item_name':'',";
			}
			if (obj[5] != null) {
				form.setRecord_content(obj[5].toString());
				str = str + "'record_content':'" + obj[5].toString() + "',";
			} else {
				form.setRecord_content("");
				str = str + "'record_content':'',";
			}
			if (obj[6] != null) {
				form.setFile_path(obj[6].toString());
				str = str + "'file_path':'" + obj[6].toString() + "',";
			} else {
				form.setFile_path("");
				str = str + "'file_path':'',";
			}
			if (obj[7] != null) {
				form.setRecord_by_name(obj[7].toString());
				str = str + "'record_by_name':'" + obj[7].toString() + "',";
			} else {
				form.setRecord_by_name("");
				str = str + "'record_by_name':'',";
			}
			if (obj[8] != null) {
				form.setRecord_by(obj[8].toString());
				str = str + "'record_by':'" + obj[8].toString() + "',";
			} else {
				form.setRecord_by("");
				str = str + "'record_by':'',";
			}
			if (obj[9] != null) {
				form.setRecord_time(obj[9].toString());
				str = str + "'record_time':'" + obj[9].toString() + "',";
			} else {
				form.setRecord_time("");
				str = str + "'record_time':'',";
			}
			if (obj[10] != null) {
				form.setReview_no(obj[10].toString());
				str = str + "'review_no':'" + obj[10].toString() + "',";
			} else {
				form.setReview_no("");
				str = str + "'review_no':'',";
			}
			if (obj[11] != null) {
				form.setReview_type(obj[11].toString());
				str = str + "'review_type':'" + obj[11].toString() + "',";
			} else {
				form.setReview_type("");
				str = str + "'review_type':'',";
			}
			if (obj[12] != null) {
				form.setCheck_by(obj[12].toString());
				str = str + "'check_by':'" + obj[12].toString() + "',";
			} else {
				form.setCheck_by("");
				str = str + "'check_by':'',";
			}
			if (obj[13] != null) {
				form.setCheck_memo(obj[13].toString());
				str = str + "'check_memo':'" + obj[13].toString() + "',";
			} else {
				form.setCheck_memo("");
				str = str + "'check_memo':'',";
			}
			if (obj[14] != null) {
				form.setCheck_time(obj[14].toString());
				str = str + "'check_time':'" + obj[14].toString() + "',";
			} else {
				form.setCheck_time("");
				str = str + "'check_time':'',";
			}
			if (obj[15] != null) {
				form.setIs_completion(obj[15].toString());
				str = str + "'is_completion':'" + obj[15].toString() + "',";
			} else {
				form.setIs_completion("");
				str = str + "'is_completion':'',";
			}
			if (obj[16] != null) {
				form.setIs_use(obj[16].toString());
				str = str + "'is_use':'" + obj[16].toString() + "',";
			} else {
				form.setIs_use("");
				str = str + "'is_use':'',";
			}
			if (obj[17] != null) {
				form.setEnterprise_code(obj[17].toString());
				str = str + "'enterprise_code':'" + obj[17].toString() + "'},";
			} else {
				form.setEnterprise_code("");
				str = str + "'enterprise_code':''},";
			}
			if (obj[18] != null) {
				form.setNot_completion_id(obj[18].toString());
				str = str + "'not_completion_id':'" + obj[18].toString()
						+ "'},";
			} else {
				form.setNot_completion_id("");
				str = str + "'not_completion_id':''},";
			}
			formlist.add(form);
		}
		PageObject po = new PageObject();
		po.setList(formlist);
		po.setTotalCount(count);
		return po;
	}

	/**
	 * 根据运行日志id，企业编码查找值班记事列表
	 * 
	 * @param runLogId
	 * @param enterpriseCode
	 * @return String
	 */
	public PageObject findListByRunLogId(Long runLogId, String enterpriseCode,
			final int... rowStartIdxAndCount) {

		long count = 0;
		String strSql = "select count(1)\n" + "  from run_j_shift_record t\n"
				+ " where t.is_use = 'Y'\n"
				+ "   and t.is_completion is null\n"
				+ "   and t.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and t.run_log_id = '" + runLogId + "'\n";

		Object objCount = bll.getSingal(strSql);
		if (objCount != null) {
			count = Long.parseLong(objCount.toString());
		}

		strSql = "select (select t2.run_logno\n"
				+ "          from run_j_runlog_main t2\n"
				+ "         where t2.is_use = 'Y'\n"
				+ "           and t2.run_logid = '"
				+ runLogId
				+ "') run_logno,\n"
				+ "       t.shift_record_id,\n"
				+ "       t.run_log_id,\n"
				+ "       t.main_item_code,\n"
				+ "       (select t1.main_item_name\n"
				+ "          from run_c_main_item t1\n"
				+ "         where t1.is_use = 'Y'\n"
				+ "           and t1.main_item_code = t.main_item_code) main_item_name,\n"
				+ "       t.record_content,\n"
				+ "       t.file_path,\n"
				+ "(select t5.chs_name\n"
				+ "          from hr_j_emp_info t5\n"
				+ "         where t5.emp_code = t.record_by) record_by_name,"
				+ "       t.record_by,\n"
				+ "       to_char(t.record_time,'yyyy-mm-dd hh24:mi:ss'),\n"
				+ "       t.review_no,\n"
				+ "       t.review_type,\n"
				+ "       t.check_by,\n"
				+ "       t.check_memo,\n"
				+ "       t.check_time,\n"
				+ "       t.is_completion,\n"
				+ "       t.is_use,\n"
				+ "       t.enterprise_code,\n"
				+ "       t.not_completion_id\n"
				+ "  from run_j_shift_record t\n"
				+ " where t.is_use = 'Y'\n"
				+ "   and t.is_completion is null\n"
				+ "   and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "   and t.run_log_id = '" + runLogId + "'\n";

		List list = bll.queryByNativeSQL(strSql, rowStartIdxAndCount);
		List<RunJShiftRecrodForm> formlist = new ArrayList();
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			RunJShiftRecrodForm form = new RunJShiftRecrodForm();
			Object[] obj = (Object[]) list.get(i);
			if (obj[0] != null) {
				form.setRun_logno(obj[0].toString());
				str = str + "{'run_logno':'" + obj[0].toString() + "',";
			} else {
				form.setRun_logno("");
				str = str + "{'run_logno':'',";
			}
			if (obj[1] != null) {
				form.setShift_record_id(obj[1].toString());
				str = str + "'shift_record_id':'" + obj[1].toString() + "',";
			} else {
				form.setShift_record_id("");
				str = str + "'shift_record_id':'',";
			}
			if (obj[2] != null) {
				form.setRun_log_id(obj[2].toString());
				str = str + "'run_log_id':'" + obj[2].toString() + "',";
			} else {
				form.setRun_log_id("");
				str = str + "'run_log_id':'',";
			}
			if (obj[3] != null) {
				form.setMain_item_code(obj[3].toString());
				str = str + "'main_item_code':'" + obj[3].toString() + "',";
			} else {
				form.setMain_item_code("");
				str = str + "'main_item_code':'',";
			}
			if (obj[4] != null) {
				form.setMain_item_name(obj[4].toString());
				str = str + "'main_item_name':'" + obj[4].toString() + "',";
			} else {
				form.setMain_item_name("");
				str = str + "'main_item_name':'',";
			}
			if (obj[5] != null) {
				form.setRecord_content(obj[5].toString());
				str = str + "'record_content':'" + obj[5].toString() + "',";
			} else {
				form.setRecord_content("");
				str = str + "'record_content':'',";
			}
			if (obj[6] != null) {
				form.setFile_path(obj[6].toString());
				str = str + "'file_path':'" + obj[6].toString() + "',";
			} else {
				form.setFile_path("");
				str = str + "'file_path':'',";
			}
			if (obj[7] != null) {
				form.setRecord_by_name(obj[7].toString());
				str = str + "'record_by_name':'" + obj[7].toString() + "',";
			} else {
				form.setRecord_by_name("");
				str = str + "'record_by_name':'',";
			}
			if (obj[8] != null) {
				form.setRecord_by(obj[8].toString());
				str = str + "'record_by':'" + obj[8].toString() + "',";
			} else {
				form.setRecord_by("");
				str = str + "'record_by':'',";
			}
			if (obj[9] != null) {
				form.setRecord_time(obj[9].toString());
				str = str + "'record_time':'" + obj[9].toString() + "',";
			} else {
				form.setRecord_time("");
				str = str + "'record_time':'',";
			}
			if (obj[10] != null) {
				form.setReview_no(obj[10].toString());
				str = str + "'review_no':'" + obj[10].toString() + "',";
			} else {
				form.setReview_no("");
				str = str + "'review_no':'',";
			}
			if (obj[11] != null) {
				form.setReview_type(obj[11].toString());
				str = str + "'review_type':'" + obj[11].toString() + "',";
			} else {
				form.setReview_type("");
				str = str + "'review_type':'',";
			}
			if (obj[12] != null) {
				form.setCheck_by(obj[12].toString());
				str = str + "'check_by':'" + obj[12].toString() + "',";
			} else {
				form.setCheck_by("");
				str = str + "'check_by':'',";
			}
			if (obj[13] != null) {
				form.setCheck_memo(obj[13].toString());
				str = str + "'check_memo':'" + obj[13].toString() + "',";
			} else {
				form.setCheck_memo("");
				str = str + "'check_memo':'',";
			}
			if (obj[14] != null) {
				form.setCheck_time(obj[14].toString());
				str = str + "'check_time':'" + obj[14].toString() + "',";
			} else {
				form.setCheck_time("");
				str = str + "'check_time':'',";
			}
			if (obj[15] != null) {
				form.setIs_completion(obj[15].toString());
				str = str + "'is_completion':'" + obj[15].toString() + "',";
			} else {
				form.setIs_completion("");
				str = str + "'is_completion':'',";
			}
			if (obj[16] != null) {
				form.setIs_use(obj[16].toString());
				str = str + "'is_use':'" + obj[16].toString() + "',";
			} else {
				form.setIs_use("");
				str = str + "'is_use':'',";
			}
			if (obj[17] != null) {
				form.setEnterprise_code(obj[17].toString());
				str = str + "'enterprise_code':'" + obj[17].toString() + "'},";
			} else {
				form.setEnterprise_code("");
				str = str + "'enterprise_code':''},";
			}
			if (obj[18] != null) {
				form.setNot_completion_id(obj[18].toString());
				str = str + "'not_completion_id':'" + obj[18].toString()
						+ "'},";
			} else {
				form.setNot_completion_id("");
				str = str + "'not_completion_id':''},";
			}
			formlist.add(form);
		}
		PageObject po = new PageObject();
		po.setList(formlist);
		po.setTotalCount(count);
		return po;
	}

	/**
	 * 运行日志查询---值班记事查询
	 * 
	 * @param startDate
	 * @param endDate
	 * @param specialityCode
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject getShiftRecordListByDate(String startDate,
			String endDate, String specialityCode, String enterpriseCode,
			final int... rowStartIdxAndCount) {

		long count = 0;
		String strSql = "select count(1)\n"
				+ "  from run_j_shift_record t, run_j_runlog_main t3\n"
				+ " where t.is_use = 'Y'\n"
				+ "   and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "   and t3.is_use = 'Y'\n"
				+ "   and t3.speciality_code = '"
				+ specialityCode
				+ "'\n"
				+ "   and t3.run_logid = t.run_log_id\n"
				+ "   and to_date(substr(t3.run_logno, 0, 8), 'yyyymmdd') between\n"
				+ "       to_date('" + startDate
				+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') and\n"
				+ "       to_date('" + endDate
				+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss')\n"
				+ " order by t3.run_logno";

		Object objCount = bll.getSingal(strSql);
		if (objCount != null) {
			count = Long.parseLong(objCount.toString());
		}

		strSql = "select * from (select t3.run_logno,\n"
				+ "       t.shift_record_id,\n"
				+ "       t.run_log_id,\n"
				+ "       t.main_item_code,\n"
				+ "       (select t1.main_item_name\n"
				+ "          from run_c_main_item t1\n"
				+ "         where t1.is_use = 'Y'\n"
				+ "           and t1.main_item_code = t.main_item_code) main_item_name,\n"
				+ "       t.record_content,\n"
				+ "       t.file_path,\n"
				+ "       (select t5.chs_name\n"
				+ "          from hr_j_emp_info t5\n"
				+ "         where t5.emp_code = t.record_by) record_by_name,\n"
				+ "       t.record_by,\n"
				+ "       to_char(t.record_time,'yyyy-mm-dd hh24:mi:ss') record_time,\n"
				+ "       t.review_no,\n"
				+ "       t.review_type,\n"
				+ "       t.check_by,\n"
				+ "       t.check_memo,\n"
				+ "       t.check_time,\n"
				+ "       t.is_completion,\n"
				+ "       t.is_use,\n"
				+ "       t.enterprise_code,\n"
				+ "       t.not_completion_id,\n"
				+ "		  (select t6.shift_name\n"
				+ "          from run_c_shift t6\n"
				+ "         where t6.shift_id = t3.shift_id) shift_name,"
				+ "       row_number() over(order by t3.run_logno desc, record_time desc) r\n"
				+ "  from run_j_shift_record t, run_j_runlog_main t3\n"
				+ " where t.is_use = 'Y'\n"
				+ "   and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "   and t3.is_use = 'Y'\n"
				+ "   and t3.speciality_code = '"
				+ specialityCode
				+ "'\n"
				+ "   and t3.run_logid = t.run_log_id\n"
				+ "   and to_date(substr(t3.run_logno, 0, 8), 'yyyymmdd') between\n"
				+ "       to_date('" + startDate
				+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') and\n"
				+ "       to_date('" + endDate
				+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss')) b\n" + " where b.r > "
				+ rowStartIdxAndCount[0] + " and b.r<="
				+ (rowStartIdxAndCount[0] + rowStartIdxAndCount[1]);

		List list = bll.queryByNativeSQL(strSql);
		List<RunJShiftRecrodForm> formlist = new ArrayList();
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			RunJShiftRecrodForm form = new RunJShiftRecrodForm();
			Object[] obj = (Object[]) list.get(i);
			if (obj[0] != null) {
				form.setRun_logno(obj[0].toString());
				str = str + "{'run_logno':'" + obj[0].toString() + "',";
			} else {
				form.setRun_logno("");
				str = str + "{'run_logno':'',";
			}
			if (obj[1] != null) {
				form.setShift_record_id(obj[1].toString());
				str = str + "'shift_record_id':'" + obj[1].toString() + "',";
			} else {
				form.setShift_record_id("");
				str = str + "'shift_record_id':'',";
			}
			if (obj[2] != null) {
				form.setRun_log_id(obj[2].toString());
				str = str + "'run_log_id':'" + obj[2].toString() + "',";
			} else {
				form.setRun_log_id("");
				str = str + "'run_log_id':'',";
			}
			if (obj[3] != null) {
				form.setMain_item_code(obj[3].toString());
				str = str + "'main_item_code':'" + obj[3].toString() + "',";
			} else {
				form.setMain_item_code("");
				str = str + "'main_item_code':'',";
			}
			if (obj[4] != null) {
				form.setMain_item_name(obj[4].toString());
				str = str + "'main_item_name':'" + obj[4].toString() + "',";
			} else {
				form.setMain_item_name("");
				str = str + "'main_item_name':'',";
			}
			if (obj[5] != null) {
				form.setRecord_content(obj[5].toString());
				str = str + "'record_content':'" + obj[5].toString() + "',";
			} else {
				form.setRecord_content("");
				str = str + "'record_content':'',";
			}
			if (obj[6] != null) {
				form.setFile_path(obj[6].toString());
				str = str + "'file_path':'" + obj[6].toString() + "',";
			} else {
				form.setFile_path("");
				str = str + "'file_path':'',";
			}
			if (obj[7] != null) {
				form.setRecord_by_name(obj[7].toString());
				str = str + "'record_by_name':'" + obj[7].toString() + "',";
			} else {
				form.setRecord_by_name("");
				str = str + "'record_by_name':'',";
			}
			if (obj[8] != null) {
				form.setRecord_by(obj[8].toString());
				str = str + "'record_by':'" + obj[8].toString() + "',";
			} else {
				form.setRecord_by("");
				str = str + "'record_by':'',";
			}
			if (obj[9] != null) {
				form.setRecord_time(obj[9].toString());
				str = str + "'record_time':'" + obj[9].toString() + "',";
			} else {
				form.setRecord_time("");
				str = str + "'record_time':'',";
			}
			if (obj[10] != null) {
				form.setReview_no(obj[10].toString());
				str = str + "'review_no':'" + obj[10].toString() + "',";
			} else {
				form.setReview_no("");
				str = str + "'review_no':'',";
			}
			if (obj[11] != null) {
				form.setReview_type(obj[11].toString());
				str = str + "'review_type':'" + obj[11].toString() + "',";
			} else {
				form.setReview_type("");
				str = str + "'review_type':'',";
			}
			if (obj[12] != null) {
				form.setCheck_by(obj[12].toString());
				str = str + "'check_by':'" + obj[12].toString() + "',";
			} else {
				form.setCheck_by("");
				str = str + "'check_by':'',";
			}
			if (obj[13] != null) {
				form.setCheck_memo(obj[13].toString());
				str = str + "'check_memo':'" + obj[13].toString() + "',";
			} else {
				form.setCheck_memo("");
				str = str + "'check_memo':'',";
			}
			if (obj[14] != null) {
				form.setCheck_time(obj[14].toString());
				str = str + "'check_time':'" + obj[14].toString() + "',";
			} else {
				form.setCheck_time("");
				str = str + "'check_time':'',";
			}
			if (obj[15] != null) {
				form.setIs_completion(obj[15].toString());
				str = str + "'is_completion':'" + obj[15].toString() + "',";
			} else {
				form.setIs_completion("");
				str = str + "'is_completion':'',";
			}
			if (obj[16] != null) {
				form.setIs_use(obj[16].toString());
				str = str + "'is_use':'" + obj[16].toString() + "',";
			} else {
				form.setIs_use("");
				str = str + "'is_use':'',";
			}
			if (obj[17] != null) {
				form.setEnterprise_code(obj[17].toString());
				str = str + "'enterprise_code':'" + obj[17].toString() + "'},";
			} else {
				form.setEnterprise_code("");
				str = str + "'enterprise_code':''},";
			}
			if (obj[18] != null) {
				form.setNot_completion_id(obj[18].toString());
				str = str + "'not_completion_id':'" + obj[18].toString()
						+ "'},";
			} else {
				form.setNot_completion_id("");
				str = str + "'not_completion_id':''},";
			}
			if (obj[19] != null) {
				form.setShift_name(obj[19].toString());
			} else {
				form.setShift_name("");
			}
			formlist.add(form);
		}
		PageObject po = new PageObject();
		po.setList(formlist);
		po.setTotalCount(count);
		return po;
	}

	/**
	 * 运行日志查询---未完成项查询
	 * 
	 * @param startDate
	 * @param endDate
	 * @param specialityCode
	 * @param enterpriseCode
	 * @param start
	 * @param limit
	 * @return PageObject
	 */
	public PageObject getNotCompletionListByDate(String startDate,
			String endDate, String specialityCode, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		long count = 0;
		String strSql = "select count(1)\n"
				+ "  from ((select t3.run_logno,\n"
				+ "                t.shift_record_id,\n"
				+ "                t.run_log_id,\n"
				+ "                t.main_item_code,\n"
				+ "                (select t1.main_item_name\n"
				+ "                   from run_c_main_item t1\n"
				+ "                  where t1.is_use = 'Y'\n"
				+ "                    and t1.main_item_code = t.main_item_code) main_item_name,\n"
				+ "                t.record_content,\n"
				+ "                t.file_path,\n"
				+ "                (select t5.chs_name\n"
				+ "                   from hr_j_emp_info t5\n"
				+ "                  where t5.emp_code = t.record_by) record_by_name,\n"
				+ "                t.record_by,\n"
				+ "                to_char(t.record_time,'yyyy-mm-dd hh24:mi:ss'),\n"
				+ "                t.review_no,\n"
				+ "                t.review_type,\n"
				+ "                t.check_by,\n"
				+ "                t.check_memo,\n"
				+ "                t.check_time,\n"
				+ "                t.is_completion,\n"
				+ "                t.is_use,\n"
				+ "       t.enterprise_code,\n"
				+ "       t.not_completion_id,\n"
				+ "		  (select t6.shift_name\n"
				+ "          from run_c_shift t6\n"
				+ "         where t6.shift_id = t3.shift_id) shift_name"
				+ "           from run_j_shift_record t, run_j_runlog_main t3\n"
				+ "          where t.is_use = 'Y'\n"
				+ "            and t.is_completion = 'N'\n"
				+ "            and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and t3.is_use = 'Y'\n"
				+ "            and t3.speciality_code = '"
				+ specialityCode
				+ "'\n"
				+ "            and t3.run_logid = t.run_log_id\n"
				+ "            and to_date(substr(t3.run_logno, 0, 8), 'yyyymmdd') between\n"
				+ "                to_date('"
				+ startDate
				+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') and\n"
				+ "                to_date('"
				+ endDate
				+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss')) union\n"
				+ "        (select t3.run_logno,\n"
				+ "                t.shift_record_id,\n"
				+ "                t.run_log_id,\n"
				+ "                t.main_item_code,\n"
				+ "                (select t1.main_item_name\n"
				+ "                   from run_c_main_item t1\n"
				+ "                  where t1.is_use = 'Y'\n"
				+ "                    and t1.main_item_code = t.main_item_code) main_item_name,\n"
				+ "                t.record_content,\n"
				+ "                t.file_path,\n"
				+ "                (select t5.chs_name\n"
				+ "                   from hr_j_emp_info t5\n"
				+ "                  where t5.emp_code = t.record_by) record_by_name,\n"
				+ "                t.record_by,\n"
				+ "                to_char(t.record_time,'yyyy-mm-dd hh24:mi:ss'),\n"
				+ "                t.review_no,\n"
				+ "                t.review_type,\n"
				+ "                t.check_by,\n"
				+ "                t.check_memo,\n"
				+ "                t.check_time,\n"
				+ "                t.is_completion,\n"
				+ "                t.is_use,\n"
				+ "       t.enterprise_code,\n"
				+ "       t.not_completion_id,\n"
				+ "		  (select t6.shift_name\n"
				+ "          from run_c_shift t6\n"
				+ "         where t6.shift_id = t3.shift_id) shift_name"
				+ "           from run_j_shift_record t, run_j_runlog_main t3\n"
				+ "          where t.is_use = 'Y'\n"
				+ "            and t.not_completion_id is not null\n"
				+ "            and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and t3.is_use = 'Y'\n"
				+ "            and t3.speciality_code = '"
				+ specialityCode
				+ "'\n"
				+ "            and t3.run_logid = t.run_log_id\n"
				+ "            and to_date(substr(t3.run_logno, 0, 8), 'yyyymmdd') between\n"
				+ "                to_date('"
				+ startDate
				+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') and\n"
				+ "                to_date('"
				+ endDate
				+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss')))";

		Object objCount = bll.getSingal(strSql);
		if (objCount != null) {
			count = Long.parseLong(objCount.toString());
		}

		strSql = "select *\n"
				+ "  from (select a.*,\n"
				+ "               row_number() over(order by a.run_logno desc, a.record_time desc) r\n"
				+ "          from ((select t3.run_logno,\n"
				+ "                        t.shift_record_id,\n"
				+ "                        t.run_log_id,\n"
				+ "                        t.main_item_code,\n"
				+ "                        (select t1.main_item_name\n"
				+ "                           from run_c_main_item t1\n"
				+ "                          where t1.is_use = 'Y'\n"
				+ "                            and t1.main_item_code = t.main_item_code) main_item_name,\n"
				+ "                        t.record_content,\n"
				+ "                        t.file_path,\n"
				+ "                        (select t5.chs_name\n"
				+ "                           from hr_j_emp_info t5\n"
				+ "                          where t5.emp_code = t.record_by) record_by_name,\n"
				+ "                        t.record_by,\n"
				+ "                        to_char(t.record_time, 'yyyy-mm-dd hh24:mi:ss') record_time,\n"
				+ "                        t.review_no,\n"
				+ "                        t.review_type,\n"
				+ "                        t.check_by,\n"
				+ "                        t.check_memo,\n"
				+ "                        t.check_time,\n"
				+ "                        t.is_completion,\n"
				+ "                        t.is_use,\n"
				+ "                        t.enterprise_code,\n"
				+ "                        t.not_completion_id,\n"
				+ "		  				   (select t6.shift_name\n"
				+ "          				  from run_c_shift t6\n"
				+ "         				 where t6.shift_id = t3.shift_id) shift_name"
				+ "                   from run_j_shift_record t, run_j_runlog_main t3\n"
				+ "                  where t.is_use = 'Y'\n"
				+ "                    and t.is_completion = 'N'\n"
				+ "                    and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and t3.is_use = 'Y'\n"
				+ "                    and t3.speciality_code = '"
				+ specialityCode
				+ "'\n"
				+ "                    and t3.run_logid = t.run_log_id\n"
				+ "                    and to_date(substr(t3.run_logno, 0, 8), 'yyyymmdd') between\n"
				+ "                        to_date('"
				+ startDate
				+ " 00:00:00',\n"
				+ "                                'yyyy-mm-dd hh24:mi:ss') and\n"
				+ "                        to_date('"
				+ endDate
				+ " 23:59:59',\n"
				+ "                                'yyyy-mm-dd hh24:mi:ss')) union\n"
				+ "                (select t3.run_logno,\n"
				+ "                        t.shift_record_id,\n"
				+ "                        t.run_log_id,\n"
				+ "                        t.main_item_code,\n"
				+ "                        (select t1.main_item_name\n"
				+ "                           from run_c_main_item t1\n"
				+ "                          where t1.is_use = 'Y'\n"
				+ "                            and t1.main_item_code = t.main_item_code) main_item_name,\n"
				+ "                        t.record_content,\n"
				+ "                        t.file_path,\n"
				+ "                        (select t5.chs_name\n"
				+ "                           from hr_j_emp_info t5\n"
				+ "                          where t5.emp_code = t.record_by) record_by_name,\n"
				+ "                        t.record_by,\n"
				+ "                        to_char(t.record_time, 'yyyy-mm-dd hh24:mi:ss') record_time,\n"
				+ "                        t.review_no,\n"
				+ "                        t.review_type,\n"
				+ "                        t.check_by,\n"
				+ "                        t.check_memo,\n"
				+ "                        t.check_time,\n"
				+ "                        t.is_completion,\n"
				+ "                        t.is_use,\n"
				+ "                        t.enterprise_code,\n"
				+ "                        t.not_completion_id,\n"
				+ "		  				   (select t6.shift_name\n"
				+ "          				  from run_c_shift t6\n"
				+ "         				 where t6.shift_id = t3.shift_id) shift_name"
				+ "                   from run_j_shift_record t, run_j_runlog_main t3\n"
				+ "                  where t.is_use = 'Y'\n"
				+ "                    and t.not_completion_id is not null\n"
				+ "                    and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and t3.is_use = 'Y'\n"
				+ "                    and t3.speciality_code = '"
				+ specialityCode
				+ "'\n"
				+ "                    and t3.run_logid = t.run_log_id\n"
				+ "                    and to_date(substr(t3.run_logno, 0, 8), 'yyyymmdd') between\n"
				+ "                        to_date('"
				+ startDate
				+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') and\n"
				+ "                        to_date('"
				+ endDate
				+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss'))) a) b\n"
				+ " where b.r > "
				+ rowStartIdxAndCount[0]
				+ "\n"
				+ "   and b.r <= "
				+ (rowStartIdxAndCount[0] + rowStartIdxAndCount[1]);

		List list = bll.queryByNativeSQL(strSql);
		List<RunJShiftRecrodForm> formlist = new ArrayList();
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			RunJShiftRecrodForm form = new RunJShiftRecrodForm();
			Object[] obj = (Object[]) list.get(i);
			if (obj[0] != null) {
				form.setRun_logno(obj[0].toString());
				str = str + "{'run_logno':'" + obj[0].toString() + "',";
			} else {
				form.setRun_logno("");
				str = str + "{'run_logno':'',";
			}
			if (obj[1] != null) {
				form.setShift_record_id(obj[1].toString());
				str = str + "'shift_record_id':'" + obj[1].toString() + "',";
			} else {
				form.setShift_record_id("");
				str = str + "'shift_record_id':'',";
			}
			if (obj[2] != null) {
				form.setRun_log_id(obj[2].toString());
				str = str + "'run_log_id':'" + obj[2].toString() + "',";
			} else {
				form.setRun_log_id("");
				str = str + "'run_log_id':'',";
			}
			if (obj[3] != null) {
				form.setMain_item_code(obj[3].toString());
				str = str + "'main_item_code':'" + obj[3].toString() + "',";
			} else {
				form.setMain_item_code("");
				str = str + "'main_item_code':'',";
			}
			if (obj[4] != null) {
				form.setMain_item_name(obj[4].toString());
				str = str + "'main_item_name':'" + obj[4].toString() + "',";
			} else {
				form.setMain_item_name("");
				str = str + "'main_item_name':'',";
			}
			if (obj[5] != null) {
				form.setRecord_content(obj[5].toString());
				str = str + "'record_content':'" + obj[5].toString() + "',";
			} else {
				form.setRecord_content("");
				str = str + "'record_content':'',";
			}
			if (obj[6] != null) {
				form.setFile_path(obj[6].toString());
				str = str + "'file_path':'" + obj[6].toString() + "',";
			} else {
				form.setFile_path("");
				str = str + "'file_path':'',";
			}
			if (obj[7] != null) {
				form.setRecord_by_name(obj[7].toString());
				str = str + "'record_by_name':'" + obj[7].toString() + "',";
			} else {
				form.setRecord_by_name("");
				str = str + "'record_by_name':'',";
			}
			if (obj[8] != null) {
				form.setRecord_by(obj[8].toString());
				str = str + "'record_by':'" + obj[8].toString() + "',";
			} else {
				form.setRecord_by("");
				str = str + "'record_by':'',";
			}
			if (obj[9] != null) {
				form.setRecord_time(obj[9].toString());
				str = str + "'record_time':'" + obj[9].toString() + "',";
			} else {
				form.setRecord_time("");
				str = str + "'record_time':'',";
			}
			if (obj[10] != null) {
				form.setReview_no(obj[10].toString());
				str = str + "'review_no':'" + obj[10].toString() + "',";
			} else {
				form.setReview_no("");
				str = str + "'review_no':'',";
			}
			if (obj[11] != null) {
				form.setReview_type(obj[11].toString());
				str = str + "'review_type':'" + obj[11].toString() + "',";
			} else {
				form.setReview_type("");
				str = str + "'review_type':'',";
			}
			if (obj[12] != null) {
				form.setCheck_by(obj[12].toString());
				str = str + "'check_by':'" + obj[12].toString() + "',";
			} else {
				form.setCheck_by("");
				str = str + "'check_by':'',";
			}
			if (obj[13] != null) {
				form.setCheck_memo(obj[13].toString());
				str = str + "'check_memo':'" + obj[13].toString() + "',";
			} else {
				form.setCheck_memo("");
				str = str + "'check_memo':'',";
			}
			if (obj[14] != null) {
				form.setCheck_time(obj[14].toString());
				str = str + "'check_time':'" + obj[14].toString() + "',";
			} else {
				form.setCheck_time("");
				str = str + "'check_time':'',";
			}
			if (obj[15] != null) {
				form.setIs_completion(obj[15].toString());
				str = str + "'is_completion':'" + obj[15].toString() + "',";
			} else {
				form.setIs_completion("");
				str = str + "'is_completion':'',";
			}
			if (obj[16] != null) {
				form.setIs_use(obj[16].toString());
				str = str + "'is_use':'" + obj[16].toString() + "',";
			} else {
				form.setIs_use("");
				str = str + "'is_use':'',";
			}
			if (obj[17] != null) {
				form.setEnterprise_code(obj[17].toString());
				str = str + "'enterprise_code':'" + obj[17].toString() + "'},";
			} else {
				form.setEnterprise_code("");
				str = str + "'enterprise_code':''},";
			}
			if (obj[18] != null) {
				form.setNot_completion_id(obj[18].toString());
				str = str + "'not_completion_id':'" + obj[18].toString()
						+ "'},";
			} else {
				form.setNot_completion_id("");
				str = str + "'not_completion_id':''},";
			}
			if (obj[19] != null) {
				form.setShift_name(obj[19].toString());
			} else {
				form.setShift_name("");
			}
			formlist.add(form);
		}

		PageObject po = new PageObject();
		po.setList(formlist);
		po.setTotalCount(count);
		return po;
	}

	/**
	 * 运行日志查询---其他班组值班记事查询
	 * 
	 * @param specialityCode
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject getOtherRecordListByDate(Long runLogId,
			String specialityCode, String enterpriseCode,
			final int... rowStartIdxAndCount) {

		long count = 0;
		String strSql = "select count(1)\n"
				+ "  from run_j_shift_record t, run_j_runlog_main t3\n"
				+ " where t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterpriseCode + "'\n" + "   and t3.is_use = 'Y'\n"
				+ "   and t3.speciality_code = '" + specialityCode + "'\n"
				+ "   and t3.run_logid = t.run_log_id\n"
				+ "   and t3.run_logno = (select t4.run_logno\n"
				+ "      from run_j_runlog_main t4\n"
				+ "     where t4.run_logid = '" + runLogId + "')\n"
				+ " order by t3.run_logno";

		Object objCount = bll.getSingal(strSql);
		if (objCount != null) {
			count = Long.parseLong(objCount.toString());
		}

		strSql = "select t3.run_logno,\n"
				+ "       t.shift_record_id,\n"
				+ "       t.run_log_id,\n"
				+ "       t.main_item_code,\n"
				+ "       (select t1.main_item_name\n"
				+ "          from run_c_main_item t1\n"
				+ "         where t1.is_use = 'Y'\n"
				+ "           and t1.main_item_code = t.main_item_code) main_item_name,\n"
				+ "       t.record_content,\n" + "       t.file_path,\n"
				+ "       (select t5.chs_name\n"
				+ "          from hr_j_emp_info t5\n"
				+ "         where t5.emp_code = t.record_by) record_by_name,\n"
				+ "       t.record_by,\n"
				+ "       to_char(t.record_time,'yyyy-mm-dd hh24:mi:ss'),\n"
				+ "       t.review_no,\n" + "       t.review_type,\n"
				+ "       t.check_by,\n" + "       t.check_memo,\n"
				+ "       t.check_time,\n" + "       t.is_completion,\n"
				+ "       t.is_use,\n" + "       t.enterprise_code,\n"
				+ "       t.not_completion_id\n"
				+ "  from run_j_shift_record t, run_j_runlog_main t3\n"
				+ " where t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterpriseCode + "'\n" + "   and t3.is_use = 'Y'\n"
				+ "   and t3.speciality_code = '" + specialityCode + "'\n"
				+ "   and t3.run_logid = t.run_log_id\n"
				+ "   and t3.run_logno =  (select t4.run_logno\n"
				+ "      from run_j_runlog_main t4\n"
				+ "     where t4.run_logid = '" + runLogId + "')\n"
				+ " order by t3.run_logno";

		List list = bll.queryByNativeSQL(strSql, rowStartIdxAndCount);
		List<RunJShiftRecrodForm> formlist = new ArrayList();
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			RunJShiftRecrodForm form = new RunJShiftRecrodForm();
			Object[] obj = (Object[]) list.get(i);
			if (obj[0] != null) {
				form.setRun_logno(obj[0].toString());
				str = str + "{'run_logno':'" + obj[0].toString() + "',";
			} else {
				form.setRun_logno("");
				str = str + "{'run_logno':'',";
			}
			if (obj[1] != null) {
				form.setShift_record_id(obj[1].toString());
				str = str + "'shift_record_id':'" + obj[1].toString() + "',";
			} else {
				form.setShift_record_id("");
				str = str + "'shift_record_id':'',";
			}
			if (obj[2] != null) {
				form.setRun_log_id(obj[2].toString());
				str = str + "'run_log_id':'" + obj[2].toString() + "',";
			} else {
				form.setRun_log_id("");
				str = str + "'run_log_id':'',";
			}
			if (obj[3] != null) {
				form.setMain_item_code(obj[3].toString());
				str = str + "'main_item_code':'" + obj[3].toString() + "',";
			} else {
				form.setMain_item_code("");
				str = str + "'main_item_code':'',";
			}
			if (obj[4] != null) {
				form.setMain_item_name(obj[4].toString());
				str = str + "'main_item_name':'" + obj[4].toString() + "',";
			} else {
				form.setMain_item_name("");
				str = str + "'main_item_name':'',";
			}
			if (obj[5] != null) {
				form.setRecord_content(obj[5].toString());
				str = str + "'record_content':'" + obj[5].toString() + "',";
			} else {
				form.setRecord_content("");
				str = str + "'record_content':'',";
			}
			if (obj[6] != null) {
				form.setFile_path(obj[6].toString());
				str = str + "'file_path':'" + obj[6].toString() + "',";
			} else {
				form.setFile_path("");
				str = str + "'file_path':'',";
			}
			if (obj[7] != null) {
				form.setRecord_by_name(obj[7].toString());
				str = str + "'record_by_name':'" + obj[7].toString() + "',";
			} else {
				form.setRecord_by_name("");
				str = str + "'record_by_name':'',";
			}
			if (obj[8] != null) {
				form.setRecord_by(obj[8].toString());
				str = str + "'record_by':'" + obj[8].toString() + "',";
			} else {
				form.setRecord_by("");
				str = str + "'record_by':'',";
			}
			if (obj[9] != null) {
				form.setRecord_time(obj[9].toString());
				str = str + "'record_time':'" + obj[9].toString() + "',";
			} else {
				form.setRecord_time("");
				str = str + "'record_time':'',";
			}
			if (obj[10] != null) {
				form.setReview_no(obj[10].toString());
				str = str + "'review_no':'" + obj[10].toString() + "',";
			} else {
				form.setReview_no("");
				str = str + "'review_no':'',";
			}
			if (obj[11] != null) {
				form.setReview_type(obj[11].toString());
				str = str + "'review_type':'" + obj[11].toString() + "',";
			} else {
				form.setReview_type("");
				str = str + "'review_type':'',";
			}
			if (obj[12] != null) {
				form.setCheck_by(obj[12].toString());
				str = str + "'check_by':'" + obj[12].toString() + "',";
			} else {
				form.setCheck_by("");
				str = str + "'check_by':'',";
			}
			if (obj[13] != null) {
				form.setCheck_memo(obj[13].toString());
				str = str + "'check_memo':'" + obj[13].toString() + "',";
			} else {
				form.setCheck_memo("");
				str = str + "'check_memo':'',";
			}
			if (obj[14] != null) {
				form.setCheck_time(obj[14].toString());
				str = str + "'check_time':'" + obj[14].toString() + "',";
			} else {
				form.setCheck_time("");
				str = str + "'check_time':'',";
			}
			if (obj[15] != null) {
				form.setIs_completion(obj[15].toString());
				str = str + "'is_completion':'" + obj[15].toString() + "',";
			} else {
				form.setIs_completion("");
				str = str + "'is_completion':'',";
			}
			if (obj[16] != null) {
				form.setIs_use(obj[16].toString());
				str = str + "'is_use':'" + obj[16].toString() + "',";
			} else {
				form.setIs_use("");
				str = str + "'is_use':'',";
			}
			if (obj[17] != null) {
				form.setEnterprise_code(obj[17].toString());
				str = str + "'enterprise_code':'" + obj[17].toString() + "'},";
			} else {
				form.setEnterprise_code("");
				str = str + "'enterprise_code':''},";
			}
			if (obj[18] != null) {
				form.setNot_completion_id(obj[18].toString());
				str = str + "'not_completion_id':'" + obj[18].toString()
						+ "'},";
			} else {
				form.setNot_completion_id("");
				str = str + "'not_completion_id':''},";
			}
			formlist.add(form);
		}

		PageObject po = new PageObject();
		po.setList(formlist);
		po.setTotalCount(count);
		return po;
	}

	/**
	 * 导入其它班组值班记事
	 * 
	 * @param workerCode
	 * @param runLogId
	 * @param ids
	 */
	public void impOtherRecord(String workerCode, Long runLogId, String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			RunJShiftRecord model = this.findById(Long.parseLong(ids[i]));
			RunJShiftRecord tmp = new RunJShiftRecord();
			Long id = this.getMaxId();
			tmp.setShiftRecordId(id + Long.parseLong(String.valueOf(i)));
			tmp.setRunLogId(runLogId);
			tmp.setIsUse("Y");
			tmp.setRecordContent(model.getRecordContent());
			tmp.setMainItemCode(model.getMainItemCode());
			tmp.setEnterpriseCode(model.getEnterpriseCode());
			tmp.setRecordBy(workerCode);
			tmp.setRecordTime(new java.util.Date());
			this.saves(tmp);
		}
	}

	/**
	 * 未完成项追溯
	 * 
	 * @param recordId
	 * @param enterpriseCode
	 * @param start
	 * @param limit
	 * @return String
	 */
	public String reviewNotCompletion(Long recordId, String enterpriseCode,
			final int... rowStartIdxAndCount) {

		int count = 0;
		String strSql = "select count(1)\n" + "  from run_j_shift_record t\n"
				+ " start with t.shift_record_id = '" + recordId + "'\n"
				+ "connect by prior t.not_completion_id = t.shift_record_id\n"
				+ " order by t.record_content, t.run_log_id";

		Object objCount = bll.getSingal(strSql);
		if (objCount != null) {
			count = Integer.parseInt(objCount.toString());
		}

		strSql = "select (select t2.run_logno\n"
				+ "          from run_j_runlog_main t2\n"
				+ "         where t2.is_use = 'Y'\n"
				+ "           and t2.run_logid = t.run_log_id) run_logno,\n"
				+ "       t.shift_record_id,\n"
				+ "       t.run_log_id,\n"
				+ "       t.main_item_code,\n"
				+ "       (select t1.main_item_name\n"
				+ "          from run_c_main_item t1\n"
				+ "         where t1.is_use = 'Y'\n"
				+ "           and t1.main_item_code = t.main_item_code) main_item_name,\n"
				+ "       t.record_content,\n" + "       t.file_path,\n"
				+ "       getworkername(t.record_by) record_by_name,\n"
				+ "       t.record_by,\n"
				+ "       to_char(t.record_time,'yyyy-mm-dd hh24:mi:ss'),\n"
				+ "       t.review_no,\n" + "       t.review_type,\n"
				+ "       t.check_by,\n" + "       t.check_memo,\n"
				+ "       t.check_time,\n" + "       t.is_completion,\n"
				+ "       t.is_use,\n" + "       t.enterprise_code\n"
				+ "  from run_j_shift_record t\n"
				+ " start with t.shift_record_id = '" + recordId + "'\n"
				+ "connect by prior t.not_completion_id = t.shift_record_id\n"
				+ " order by t.record_content, t.run_log_id";

		List list = bll.queryByNativeSQL(strSql, rowStartIdxAndCount);
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			if (obj[0] != null) {
				str = str + "{'run_logno':'" + obj[0].toString() + "',";
			} else {
				str = str + "{'run_logno':'',";
			}
			if (obj[1] != null) {
				str = str + "'shift_record_id':'" + obj[1].toString() + "',";
			} else {
				str = str + "'shift_record_id':'',";
			}
			if (obj[2] != null) {
				str = str + "'run_log_id':'" + obj[2].toString() + "',";
			} else {
				str = str + "'run_log_id':'',";
			}
			if (obj[3] != null) {
				str = str + "'main_item_code':'" + obj[3].toString() + "',";
			} else {
				str = str + "'main_item_code':'',";
			}
			if (obj[4] != null) {
				str = str + "'main_item_name':'" + obj[4].toString() + "',";
			} else {
				str = str + "'main_item_name':'',";
			}
			if (obj[5] != null) {
				str = str + "'record_content':'" + obj[5].toString() + "',";
			} else {
				str = str + "'record_content':'',";
			}
			if (obj[6] != null) {
				str = str + "'file_path':'" + obj[6].toString() + "',";
			} else {
				str = str + "'file_path':'',";
			}
			if (obj[7] != null) {
				str = str + "'record_by_name':'" + obj[7].toString() + "',";
			} else {
				str = str + "'record_by_name':'',";
			}
			if (obj[8] != null) {
				str = str + "'record_by':'" + obj[8].toString() + "',";
			} else {
				str = str + "'record_by':'',";
			}
			if (obj[9] != null) {
				str = str + "'record_time':'" + obj[9].toString() + "',";
			} else {
				str = str + "'record_time':'',";
			}
			if (obj[10] != null) {
				str = str + "'review_no':'" + obj[10].toString() + "',";
			} else {
				str = str + "'review_no':'',";
			}
			if (obj[11] != null) {
				str = str + "'review_type':'" + obj[11].toString() + "',";
			} else {
				str = str + "'review_type':'',";
			}
			if (obj[12] != null) {
				str = str + "'check_by':'" + obj[12].toString() + "',";
			} else {
				str = str + "'check_by':'',";
			}
			if (obj[13] != null) {
				str = str + "'check_memo':'" + obj[13].toString() + "',";
			} else {
				str = str + "'check_memo':'',";
			}
			if (obj[14] != null) {
				str = str + "'check_time':'" + obj[14].toString() + "',";
			} else {
				str = str + "'check_time':'',";
			}
			if (obj[15] != null) {
				str = str + "'is_completion':'" + obj[15].toString() + "',";
			} else {
				str = str + "'is_completion':'',";
			}
			if (obj[16] != null) {
				str = str + "'is_use':'" + obj[16].toString() + "',";
			} else {
				str = str + "'is_use':'',";
			}
			if (obj[17] != null) {
				str = str + "'enterprise_code':'" + obj[17].toString() + "'},";
			} else {
				str = str + "'enterprise_code':''},";
			}
		}
		if (str != "") {
			str = "{total:" + count + ",root:["
					+ str.substring(0, str.length() - 1) + "]}";
		}
		return str;
	}

}