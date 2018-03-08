package power.ejb.equ.standardpackage;

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
 * Facade for entity EquCStandardjob.
 * 
 * @see power.ejb.equ.standardpackage.EquCStandardjob
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCStandardjobFacade implements EquCStandardjobFacadeRemote {
	// property constants
	public static final String JOB_CODE = "jobCode";
	public static final String CODE = "code";
	public static final String DESCRIPTION = "description";
	public static final String PRIORITY = "priority";
	public static final String JOP_DURATION = "jopDuration";
	public static final String MAINT_DEP = "maintDep";
	public static final String SPECIALITY = "speciality";
	public static final String CREW_ID = "crewId";
	public static final String INTERRUPTABLE = "interruptable";
	public static final String DOWN_TIME = "downTime";
	public static final String SUPERVISOR = "supervisor";
	public static final String LABOR_CODE = "laborCode";
	public static final String CALNUM = "calnum";
	public static final String STATUS = "status";
	public static final String ENTERPRISECODE = "enterprisecode";
	public static final String IS_USE = "isUse";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	protected boolean checkcode(String jobcode, Long... Id) {
		boolean isSame = true;
		String sql = "select count(1) from EQU_C_STANDARDJOB where IS_USE='Y' and JOB_CODE='"
				+ jobcode + "' ";
		if (Id != null && Id.length > 0) {
			sql += "and JOB_ID <> " + Id[0];
		}
		if (Long.parseLong((dll.getSingal(sql).toString())) > 0) {
			isSame = false;
		}
		return isSame;
	}

	/**
	 * Perform an initial save of a previously unsaved EquCStandardjob entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCStandardjob entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public long save(EquCStandardjob entity) {
		try {
			if (checkcode(entity.getJobCode())) {
				if (entity.getJobId() == null) {
					entity
							.setJobId(dll.getMaxId("EQU_C_STANDARDJOB",
									"JOB_ID"));
				}
				if (entity.getPriority() == null) {
					entity.setPriority(entity.getJobId());
				}
				SimpleDateFormat codeFormat = new SimpleDateFormat(
						"yyyyMMddmmss");
				Date codevalue = new Date();
				entity.setCode("SP" + codeFormat.format(codevalue));
				entityManager.persist(entity);
				return entity.getJobId();
			} else {
				return 0;
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent EquCStandardjob entity.
	 * 
	 * @param entity
	 *            EquCStandardjob entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCStandardjob entity) {
		LogUtil.log("deleting EquCStandardjob instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(EquCStandardjob.class, entity
					.getJobId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved EquCStandardjob entity and return it or a copy
	 * of it to the sender. A copy of the EquCStandardjob entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardjob entity to update
	 * @return EquCStandardjob the persisted EquCStandardjob entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public long update(EquCStandardjob entity) {
		try {
			if (checkcode(entity.getJobCode(), entity.getJobId())) {
				entityManager.merge(entity);
				return entity.getJobId();
			} else {
				return 0;
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquCStandardjob findById(Long id) {
		LogUtil.log("finding EquCStandardjob instance with id: " + id,
				Level.INFO, null);
		try {
			EquCStandardjob instance = entityManager.find(
					EquCStandardjob.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquCStandardjob entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardjob property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<EquCStandardjob> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardjob> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding EquCStandardjob instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquCStandardjob model where model."
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

	public List<EquCStandardjob> findByJobCode(Object jobCode,
			int... rowStartIdxAndCount) {
		return findByProperty(JOB_CODE, jobCode, rowStartIdxAndCount);
	}

	public List<EquCStandardjob> findByCode(Object code,
			int... rowStartIdxAndCount) {
		return findByProperty(CODE, code, rowStartIdxAndCount);
	}

	public List<EquCStandardjob> findByDescription(Object description,
			int... rowStartIdxAndCount) {
		return findByProperty(DESCRIPTION, description, rowStartIdxAndCount);
	}

	public List<EquCStandardjob> findByPriority(Object priority,
			int... rowStartIdxAndCount) {
		return findByProperty(PRIORITY, priority, rowStartIdxAndCount);
	}

	public List<EquCStandardjob> findByJopDuration(Object jopDuration,
			int... rowStartIdxAndCount) {
		return findByProperty(JOP_DURATION, jopDuration, rowStartIdxAndCount);
	}

	public List<EquCStandardjob> findByMaintDep(Object maintDep,
			int... rowStartIdxAndCount) {
		return findByProperty(MAINT_DEP, maintDep, rowStartIdxAndCount);
	}

	public List<EquCStandardjob> findBySpeciality(Object speciality,
			int... rowStartIdxAndCount) {
		return findByProperty(SPECIALITY, speciality, rowStartIdxAndCount);
	}

	public List<EquCStandardjob> findByCrewId(Object crewId,
			int... rowStartIdxAndCount) {
		return findByProperty(CREW_ID, crewId, rowStartIdxAndCount);
	}

	public List<EquCStandardjob> findByInterruptable(Object interruptable,
			int... rowStartIdxAndCount) {
		return findByProperty(INTERRUPTABLE, interruptable, rowStartIdxAndCount);
	}

	public List<EquCStandardjob> findByDownTime(Object downTime,
			int... rowStartIdxAndCount) {
		return findByProperty(DOWN_TIME, downTime, rowStartIdxAndCount);
	}

	public List<EquCStandardjob> findBySupervisor(Object supervisor,
			int... rowStartIdxAndCount) {
		return findByProperty(SUPERVISOR, supervisor, rowStartIdxAndCount);
	}

	public List<EquCStandardjob> findByLaborCode(Object laborCode,
			int... rowStartIdxAndCount) {
		return findByProperty(LABOR_CODE, laborCode, rowStartIdxAndCount);
	}

	public List<EquCStandardjob> findByCalnum(Object calnum,
			int... rowStartIdxAndCount) {
		return findByProperty(CALNUM, calnum, rowStartIdxAndCount);
	}

	public List<EquCStandardjob> findByStatus(Object status,
			int... rowStartIdxAndCount) {
		return findByProperty(STATUS, status, rowStartIdxAndCount);
	}

	public List<EquCStandardjob> findByEnterprisecode(Object enterprisecode,
			int... rowStartIdxAndCount) {
		return findByProperty(ENTERPRISECODE, enterprisecode,
				rowStartIdxAndCount);
	}

	public List<EquCStandardjob> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	@SuppressWarnings("unchecked")
	public PageObject findByIsuse(Object isUse, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select t.*,GETSPECIALNAME(t.speciality) specialityname from equ_c_standardjob t where t.IS_USE='Y' order by PRIORITY,job_id desc";
			String sqlCount = "select count(1) from equ_c_standardjob t where t.IS_USE='Y'";
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

	/**
	 * Find all EquCStandardjob entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquCStandardjob> all EquCStandardjob entities
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardjob> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquCStandardjob instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquCStandardjob model";
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