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
 * Facade for entity RunCShiftWorkerStation.
 * 
 * @see power.ejb.run.runlog.shift.RunCShiftWorkerStation
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCShiftWorkerStationFacade implements
		RunCShiftWorkerStationFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved RunCShiftWorkerStation
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            RunCShiftWorkerStation entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunCShiftWorkerStation entity) {
		LogUtil.log("saving RunCShiftWorkerStation instance", Level.INFO, null);
		try {
			if(entity.getWorkerStationId() == null){
				entity.setWorkerStationId(bll.getMaxId("run_c_shift_worker_station", "worker_station_id"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunCShiftWorkerStation entity.
	 * 
	 * @param entity
	 *            RunCShiftWorkerStation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCShiftWorkerStation entity) {
		LogUtil.log("deleting RunCShiftWorkerStation instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(RunCShiftWorkerStation.class,
					entity.getWorkerStationId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunCShiftWorkerStation entity and return it or
	 * a copy of it to the sender. A copy of the RunCShiftWorkerStation entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCShiftWorkerStation entity to update
	 * @returns RunCShiftWorkerStation the persisted RunCShiftWorkerStation
	 *          entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCShiftWorkerStation update(RunCShiftWorkerStation entity) {
		LogUtil.log("updating RunCShiftWorkerStation instance", Level.INFO,
				null);
		try {
			RunCShiftWorkerStation result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCShiftWorkerStation findById(Long id) {
		LogUtil.log("finding RunCShiftWorkerStation instance with id: " + id,
				Level.INFO, null);
		try {
			RunCShiftWorkerStation instance = entityManager.find(
					RunCShiftWorkerStation.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunCShiftWorkerStation entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCShiftWorkerStation property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunCShiftWorkerStation> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunCShiftWorkerStation> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCShiftWorkerStation instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCShiftWorkerStation model where model."
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
	 * Find all RunCShiftWorkerStation entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShiftWorkerStation> all RunCShiftWorkerStation entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunCShiftWorkerStation> findAll(
			final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCShiftWorkerStation instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from RunCShiftWorkerStation model";
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
	public List<RunCShiftWorkerStation> findWStationByShiftWorker(Long workerId,String enterpriseCode)
	{
		String sql="select t.*\n" +
			"  from run_c_shift_worker_station t\n" + 
			" where t.shift_worker_id = "+workerId+"\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.is_use = 'Y' order by t.worker_station_id";
		return bll.queryByNativeSQL(sql, RunCShiftWorkerStation.class);
	}
	public boolean isExsit(Long shiftWorkerId,Long stationId,String enterpriseCode){
		String sql="select count(*)\n" +
			"  from run_c_shift_worker_station r\n" + 
			" where r.shift_worker_id = "+shiftWorkerId+"\n" + 
			"   and r.station_id = "+stationId+"\n" + 
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
	public boolean isStaionZZ(String empcode,String enterprisecode){
		String sql="select count(*)\n" +
			"  from run_c_shift_worker_station r\n" + 
			" where r.shift_worker_id in (select t.shift_worker_id\n" + 
			"                               from run_c_shift_worker t\n" + 
			"                              where t.emp_code = '"+empcode+"'\n" + 
			"                                and t.enterprise_code = '"+enterprisecode+"'\n" + 
			"                                and t.is_use = 'Y')\n" + 
			"   and (r.station_name like '%值长%' or r.station_name like '副值')\n" + 
			"   and r.is_use = 'Y'\n" + 
			"   and r.enterprise_code = '"+enterprisecode+"'";
		if(Integer.parseInt(bll.getSingal(sql).toString())  != 0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}
}