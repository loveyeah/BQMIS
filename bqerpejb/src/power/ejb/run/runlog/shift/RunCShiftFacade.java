package power.ejb.run.runlog.shift;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity RunCShift.
 * 
 * @see power.ejb.run.runlog.shift.RunCShift
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCShiftFacade implements RunCShiftFacadeRemote {
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved RunCShift entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCShift entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunCShift entity) {
		LogUtil.log("saving RunCShift instance", Level.INFO, null);
		try {
			if(entity.getShiftId() == null){
				entity.setShiftId(bll.getMaxId("run_c_shift", "shift_id"));
			}
			if(entity.getShiftSequence()==null)
			{
				entity.setShiftSequence(entity.getShiftId());
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunCShift entity.
	 * 
	 * @param entity
	 *            RunCShift entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCShift entity) {
		LogUtil.log("deleting RunCShift instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunCShift.class, entity
					.getShiftId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunCShift entity and return it or a copy of it
	 * to the sender. A copy of the RunCShift entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            RunCShift entity to update
	 * @returns RunCShift the persisted RunCShift entity instance, may not be
	 *          the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCShift update(RunCShift entity) {
		LogUtil.log("updating RunCShift instance", Level.INFO, null);
		try {
			RunCShift result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCShift findById(Long id) {
		LogUtil.log("finding RunCShift instance with id: " + id, Level.INFO,
				null);
		try {
			RunCShift instance = entityManager.find(RunCShift.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunCShift entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCShift property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunCShift> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunCShift> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCShift instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCShift model where model."
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
	 * Find all RunCShift entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShift> all RunCShift entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunCShift> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCShift instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCShift model";
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
	public List findShiftList(String enterpriseCode,String specialCode){
		String sql="select r.shift_id,\n" +
			"       r.initial_no,\n" + 
			"       s.initial_name,\n" + 
			"       r.shift_name,\n" + 
			"       r.shift_sequence,\n" + 
			"       decode(r.is_shift, '1', '是', '0', '否','2','空') is_shift,\n" + 
			"       r.is_use,\n" + 
			"       r.enterprise_code,\n" + 
			"       s.speciality_code\n" + 
			"  from run_c_shift r, run_c_shift_initial s\n" + 
			" where r.initial_no = s.initial_no\n" + 
			"   and s.speciality_code = '"+specialCode+"'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and r.is_use = 'Y'\n" + 
			"   and s.is_use = 'Y'\n" + 
			" order by r.shift_sequence";
		return bll.queryByNativeSQL(sql);
	}
	public List findIsShiftList(String enterpriseCode,String specialCode){
		String sql="select r.shift_id,\n" +
			"       r.initial_no,\n" + 
			"       s.initial_name,\n" + 
			"       r.shift_name,\n" + 
			"       r.shift_sequence,\n" + 
			"       decode(r.is_shift, '1', '是', '0', '否','2','空') is_shift,\n" + 
			"       r.is_use,\n" + 
			"       r.enterprise_code,\n" + 
			"       s.speciality_code\n" + 
			"  from run_c_shift r, run_c_shift_initial s\n" + 
			" where r.initial_no = s.initial_no\n" + 
			"   and s.speciality_code = '"+specialCode+"'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and r.is_shift = '1'\n" +
			"   and r.is_use = 'Y'\n" + 
			"   and s.is_use = 'Y'\n" + 
			" order by r.shift_sequence";
		return bll.queryByNativeSQL(sql);
	}
public boolean isShiftNameExsit(Long initalNo,String shiftName,String enterpriseCode,Long id){
	String sql;
	if(id != null)
	{
		sql="select count(*)\n" +
		"  from run_c_shift r\n" + 
		" where r.initial_no = "+initalNo+"\n" + 
		"   and r.shift_name = '"+shiftName+"'\n" + 
		"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
		"   and r.shift_id <> "+id+"\n" + 
		"   and r.is_use = 'Y'";
	}
	else
	{
		sql="select count(*)\n" +
		"  from run_c_shift r\n" + 
		" where r.initial_no = "+initalNo+"\n" + 
		"   and r.shift_name = '"+shiftName+"'\n" + 
		"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
		"   and r.is_use = 'Y'";
	}
	if(Long.parseLong(bll.getSingal(sql).toString()) == 0)
	{
		return false;
	}
	else
	{
		return true;
	}
}
public Long shiftCount(Long initalNO,String enterpriseCode){
	String sql="select count(*)\n" +
		"  from run_c_shift t\n" + 
		" where t.initial_no = "+initalNO+"\n" + 
		"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
		"   and t.is_shift = '1'\n" + 
		"   and t.is_use = 'Y'";
 return Long.parseLong(bll.getSingal(sql).toString());
}
	public List<RunCShift> findListByNO(Long initalNO,String enterpriseCode){
		String sql="select t.*\n" +
			"  from run_c_shift t\n" + 
			" where t.initial_no = "+initalNO+"\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.is_shift = '1'\n" + 
			"   and t.is_use = 'Y'\n"+
			" order by t.shift_sequence";
	 return bll.queryByNativeSQL(sql, RunCShift.class);
	}
	public List<RunCShift> findListByNoC(Long initalNO,String enterpriseCode)
	{
		String sql="select t.*\n" +
		"  from run_c_shift t\n" + 
		" where t.initial_no = "+initalNO+"\n" + 
		"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
		"   and t.is_shift <> '0'\n" + 
		"   and t.is_use = 'Y'\n"+
		" order by t.shift_sequence";
		return bll.queryByNativeSQL(sql, RunCShift.class);
	}
}