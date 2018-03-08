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
 * Facade for entity RunCShiftInitial.
 * 
 * @see power.ejb.run.runlog.shift.RunCShiftInitial
 * @author lyu
 */
@Stateless
public class RunCShiftInitialFacade implements RunCShiftInitialFacadeRemote {
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved RunCShiftInitial entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCShiftInitial entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public Long save(RunCShiftInitial entity) {
		LogUtil.log("saving RunCShiftInitial instance", Level.INFO, null);
		try {
			if(entity.getInitialNo() == null){
				entity.setInitialNo(bll.getMaxId("run_c_shift_initial", "initial_no"));
			}
			entityManager.persist(entity);
			return entity.getInitialNo();
			//LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunCShiftInitial entity.
	 * 
	 * @param entity
	 *            RunCShiftInitial entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCShiftInitial entity) {
		LogUtil.log("deleting RunCShiftInitial instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunCShiftInitial.class, entity
					.getInitialNo());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunCShiftInitial entity and return it or a
	 * copy of it to the sender. A copy of the RunCShiftInitial entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCShiftInitial entity to update
	 * @returns RunCShiftInitial the persisted RunCShiftInitial entity instance,
	 *          may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCShiftInitial update(RunCShiftInitial entity) {
		LogUtil.log("updating RunCShiftInitial instance", Level.INFO, null);
		try {
			RunCShiftInitial result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCShiftInitial findById(Long id) {
		LogUtil.log("finding RunCShiftInitial instance with id: " + id,
				Level.INFO, null);
		try {
			RunCShiftInitial instance = entityManager.find(
					RunCShiftInitial.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunCShiftInitial entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCShiftInitial property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunCShiftInitial> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunCShiftInitial> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCShiftInitial instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCShiftInitial model where model."
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
	 * Find all RunCShiftInitial entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShiftInitial> all RunCShiftInitial entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunCShiftInitial> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCShiftInitial instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCShiftInitial model";
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
	public List findInitialList(String enterpriseCode){
		String sql="select r.initial_no,\n" +
		"       r.initial_name,\n" + 
		"       r.speciality_code,\n" + 
		"       t.speciality_name,\n" + 
		"       r.shift_amount,\n" + 
		"       r.time_amount,\n" + 
		"       to_char(r.active_date, 'yyyy-MM-dd') active_date,\n" + 
		"       to_char(r.disconnect_date, 'yyyy-MM-dd') disconnect_date\n" + 
		"  from run_c_shift_initial r, run_c_unitprofession t\n" + 
		" where r.is_use = 'Y'\n" + 
		"   and r.speciality_code = t.speciality_code\n" + 
		"   and t.is_use = 'Y'\n" + 
		"   and t.enterprise_code = r.enterprise_code\n" + 
		"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
		"   and r.is_use = 'Y'\n" + 
		" order by r.initial_no";
		return bll.queryByNativeSQL(sql);
	}
	public List<RunCShiftInitial> findInitialBySpecial(String specialCode,String enterpriseCode){
		String sql="select r.*\n" +
			"  from run_c_shift_initial r\n" + 
			" where r.is_use = 'Y'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and r.speciality_code = '"+specialCode+"'\n" + 
			" order by r.initial_no";
		return bll.queryByNativeSQL(sql, RunCShiftInitial.class);
	}
	public Object findModel(Long id)
	{
		String sql="select r.initial_no,\n" +
			"       r.initial_name,\n" + 
			"       r.speciality_code,\n" + 
			"       r.shift_amount,\n" + 
			"       r.time_amount,\n" + 
			"       to_char(r.active_date, 'yyyy-MM-dd') active_date,\n" + 
			"       to_char(r.disconnect_date, 'yyyy-MM-dd') disconnect_date\n" + 
			"  from run_c_shift_initial r\n" + 
			" where r.initial_no = "+id+"\n" + 
			" and r.is_use='Y'";
		return bll.getSingal(sql);
	}
	public boolean isInitialNameExsit(String enterpriseCode,String initialName,Long id){
		String sql;
		if(id != null)
		{
		 sql="select count(*)\n" +
			"  from run_c_shift_initial r\n" + 
			" where r.initial_name = '"+initialName+"'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and r.initial_no <> "+id+"\n" +
			"   and r.is_use = 'Y'";
		}
		else
		{
			sql="select count(*)\n" +
			"  from run_c_shift_initial r\n" + 
			" where r.initial_name = '"+initialName+"'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and r.is_use = 'Y'";
		}
		if(Long.parseLong(bll.getSingal(sql).toString()) ==0 )
		{
			return false;
		}
		else
		{
			return true;
		}
	}
public List<RunCShiftInitial> findListByDate(String specialCode,String enterpriseCode,String startDate,String endDate){
	String sql="select r.*\n" +
		"  from run_c_shift_initial r\n" + 
		" where r.speciality_code = '"+specialCode+"'\n" + 
		"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
		"   and r.is_use = 'Y'\n" + 
		"   and r.disconnect_date >= to_date('"+startDate+"', 'yyyy-mm-dd')\n" + 
		"   and r.active_date <= to_date('"+startDate+"', 'yyyy-mm-dd')\n" + 
		"   and r.disconnect_date >= to_date('"+endDate+"', 'yyyy-mm-dd')\n" + 
		"   and r.active_date <= to_date('"+endDate+"', 'yyyy-mm-dd')\n" + 
		" order by r.active_date asc";
	return bll.queryByNativeSQL(sql, RunCShiftInitial.class);
}
public List findActiveSpecialList(String enterpriseCode,String date){
	String sql="select r.speciality_code, t.speciality_name\n" +
		"  from run_c_shift_initial r, run_c_unitprofession t\n" + 
		" where r.enterprise_code = '"+enterpriseCode+"'\n" + 
		"   and t.speciality_code = r.speciality_code\n" + 
		"   and r.is_use = 'Y'\n" + 
		"   and r.disconnect_date >= to_date('"+date+"', 'yyyy-mm-dd')\n" + 
		"   and r.active_date <= to_date('"+date+"', 'yyyy-mm-dd')\n" + 
		"   and r.disconnect_date >= to_date('"+date+"', 'yyyy-mm-dd')\n" + 
		"   and r.active_date <= to_date('"+date+"', 'yyyy-mm-dd')\n" + 
		"   and t.is_use = 'Y'\n" + 
		"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
		" order by r.initial_no asc";
	return bll.queryByNativeSQL(sql);
}
}