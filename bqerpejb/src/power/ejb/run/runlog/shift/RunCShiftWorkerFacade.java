package power.ejb.run.runlog.shift;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity RunCShiftWorker.
 * 
 * @see power.ejb.run.runlog.shift.RunCShiftWorker
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCShiftWorkerFacade implements RunCShiftWorkerFacadeRemote {
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved RunCShiftWorker entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCShiftWorker entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunCShiftWorker entity) {
		LogUtil.log("saving RunCShiftWorker instance", Level.INFO, null);
		try {
			if(entity.getShiftWorkerId() == null)
			{
				entity.setShiftWorkerId(bll.getMaxId("run_c_shift_worker", "shift_worker_id"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunCShiftWorker entity.
	 * 
	 * @param entity
	 *            RunCShiftWorker entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCShiftWorker entity) {
		entity.setIsUse("N");
		String sql = "update run_c_shift_worker_station t set t.is_use='N' where t.shift_worker_id=?";
		bll.exeNativeSQL(sql, new Object[] { entity.getShiftWorkerId() });
		this.update(entity);
	}

	/**
	 * Persist a previously saved RunCShiftWorker entity and return it or a copy
	 * of it to the sender. A copy of the RunCShiftWorker entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCShiftWorker entity to update
	 * @returns RunCShiftWorker the persisted RunCShiftWorker entity instance,
	 *          may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCShiftWorker update(RunCShiftWorker entity) {
		LogUtil.log("updating RunCShiftWorker instance", Level.INFO, null);
		try {
			RunCShiftWorker result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCShiftWorker findById(Long id) {
		LogUtil.log("finding RunCShiftWorker instance with id: " + id,
				Level.INFO, null);
		try {
			RunCShiftWorker instance = entityManager.find(
					RunCShiftWorker.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunCShiftWorker entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCShiftWorker property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunCShiftWorker> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunCShiftWorker> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCShiftWorker instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCShiftWorker model where model."
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
	 * Find all RunCShiftWorker entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShiftWorker> all RunCShiftWorker entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunCShiftWorker> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCShiftWorker instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCShiftWorker model";
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
	public List<RunCShiftWorker> findListByShift(Long shiftId,String enterpriseCode)
	{
		String sql=
			"select r.*\n" +
			"  from run_c_shift_worker r\n" + 
			" where r.shift_id = "+shiftId+"\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and r.is_use = 'Y'";
		return bll.queryByNativeSQL(sql, RunCShiftWorker.class);
	}
	public List findWorkerList(Long shiftId,String enterpriseCode)
	{
		String sql="select r.emp_code, getworkername(r.emp_code),r.shift_worker_id,r.is_hand\n" +
			"  from run_c_shift_worker r\n" + 
			" where r.shift_id = "+shiftId+"\n" + 
			"   and r.is_use = 'Y'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"' order by r.shift_worker_id";
		return bll.queryByNativeSQL(sql);
	}
	public boolean isWorkerExsit(Long shiftId,String empCode,String enterpriseCode){
		String sql="select count(*)\n" +
			"  from run_c_shift_worker r\n" + 
			" where r.shift_id = "+shiftId+"\n" + 
			"   and r.emp_code = '"+empCode+"'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and r.is_use = 'Y'";
		int num=Integer.parseInt(bll.getSingal(sql).toString());
		if(num == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public String takeWorkerStation(Long shiftId,String worker,String enterpriseCode){
		String sql="select t.station_name\n" +
			"  from run_c_shift_worker r, run_c_shift_worker_station t\n" + 
			" where t.shift_worker_id = r.shift_worker_id\n" + 
			"   and r.emp_code = '"+worker+"'\n" + 
			"   and r.shift_id = "+shiftId+"\n" + 
			"   and r.is_use = 'Y'\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'";
		Object o = bll.getSingal(sql);
		if(o !=null)
		{
			return bll.getSingal(sql).toString();
		} 
		else
		{
			return null;
		}
	}
	public boolean isPrShiftWorker(Long shiftid,String enterpriseCode,String workerCode){
		String sql="select count(*)\n" +
			"  from run_c_shift_worker r\n" + 
			" where r.shift_id in\n" + 
			"       (select m.shift_id\n" + 
			"          from run_c_shift m\n" + 
			"         where m.initial_no =\n" + 
			"               (select t.initial_no from run_c_shift t where t.shift_id = "+shiftid+")\n" + 
			"           and m.is_use = 'Y'\n" + 
			"           and m.enterprise_code = '"+enterpriseCode+"')\n" + 
			"   and r.emp_code = '"+workerCode+"'\n" + 
			"   and r.is_use = 'Y'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'";
		int count=Integer.parseInt(bll.getSingal(sql).toString());
		if(count == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public RunCShiftWorker getShiftWorker(String enterprisecode,String workercode,Long shiftid){
		String sql="select r.*\n" +
			"  from run_c_shift_worker r\n" + 
			" where r.shift_id = "+shiftid+"\n" + 
			"   and r.is_use = 'Y'\n" + 
			"   and r.emp_code = '"+workercode+"'\n" + 
			"   and r.enterprise_code='"+enterprisecode+"'";
		List<RunCShiftWorker> list=bll.queryByNativeSQL(sql, RunCShiftWorker.class);
		if(list.size() > 0){
			return list.get(0);
		}
		else
		{
			return null;
		}
	}
	public boolean isHand(String workercode,String enterprisecode,Long shiftid){
		String sql="select count(*)\n" +
		"  from run_c_shift_worker r\n" + 
		" where r.shift_id in\n" + 
		"       (select m.shift_id\n" + 
		"          from run_c_shift m\n" + 
		"         where m.initial_no =\n" + 
		"               (select t.initial_no from run_c_shift t where t.shift_id = "+shiftid+")\n" + 
		"           and m.is_use = 'Y'\n" + 
		"           and m.enterprise_code = '"+enterprisecode+"')\n" + 
		"   and r.emp_code = '"+workercode+"'\n" + 
		"   and r.is_use = 'Y'\n" + 
		"   and r.is_hand = 'Y'\n" + 
		"   and r.enterprise_code = '"+enterprisecode+"'";
	int count=Integer.parseInt(bll.getSingal(sql).toString());
	if(count == 0)
	{
		return false;
	}
	else
	{
		return true;
	}
	}
	
	public String getRunSpecialByEmp(String workercode,String enterprisecode)
	{
		String sql="select m.speciality_code\n" +
			"  from run_c_shift_worker r, run_c_shift t,run_c_shift_initial m\n" + 
			" where r.emp_code = '"+workercode+"'\n" + 
			"   and t.shift_id = r.shift_id\n" + 
			"   and m.initial_no=t.initial_no\n" + 
			"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
			"   and r.is_use = 'Y'\n" + 
			"   and t.enterprise_code = r.enterprise_code\n" + 
			"   and t.is_use = 'Y'\n" + 
			"    and m.enterprise_code= r.enterprise_code\n" + 
			"   and m.is_use='Y'";
		List list=bll.queryByNativeSQL(sql);
		if(list.size() >0)
		{
			Object o=list.get(0);
			return o.toString();
		}
		else
		{
			return null;
		}
	}
}