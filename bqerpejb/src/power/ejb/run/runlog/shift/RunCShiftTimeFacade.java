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
 * Facade for entity RunCShiftTime.
 * 
 * @see power.ejb.run.runlog.shift.RunCShiftTime
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCShiftTimeFacade implements RunCShiftTimeFacadeRemote {
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved RunCShiftTime entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCShiftTime entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunCShiftTime entity) {
		LogUtil.log("saving RunCShiftTime instance", Level.INFO, null);
		try {
			if(entity.getShiftTimeId() == null)
			{
				entity.setShiftTimeId(bll.getMaxId("run_c_shift_time", "shift_time_id"));
			}
			if(entity.getShiftSerial() == null){
				entity.setShiftSerial(entity.getShiftTimeId());
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunCShiftTime entity.
	 * 
	 * @param entity
	 *            RunCShiftTime entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCShiftTime entity) {
		LogUtil.log("deleting RunCShiftTime instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunCShiftTime.class, entity
					.getShiftTimeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunCShiftTime entity and return it or a copy
	 * of it to the sender. A copy of the RunCShiftTime entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCShiftTime entity to update
	 * @returns RunCShiftTime the persisted RunCShiftTime entity instance, may
	 *          not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCShiftTime update(RunCShiftTime entity) {
		LogUtil.log("updating RunCShiftTime instance", Level.INFO, null);
		try {
			RunCShiftTime result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCShiftTime findById(Long id) {
		LogUtil.log("finding RunCShiftTime instance with id: " + id,
				Level.INFO, null);
		try {
			RunCShiftTime instance = entityManager
					.find(RunCShiftTime.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunCShiftTime entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCShiftTime property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunCShiftTime> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunCShiftTime> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCShiftTime instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCShiftTime model where model."
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
	 * Find all RunCShiftTime entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShiftTime> all RunCShiftTime entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunCShiftTime> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCShiftTime instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCShiftTime model";
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
	public List findShfitTimeListBySpecial(String specialCode,String enterpriseCode){
		String sql="select r.shift_time_id,\n" +
			"       r.initial_no,\n" + 
			"       s.initial_name,\n" + 
			"       r.shift_time_name,\n" + 
			"       to_char(r.on_duty_time, 'hh24:mi:ss') on_duty_time,\n" + 
			"       to_char(r.off_duty_time, 'hh24:mi:ss') off_duty_time,\n" + 
			"       r.shift_serial,\n" + 
			"       r.shift_time_desc,\n" + 
			"       r.is_use,\n" + 
			"       decode(r.is_rest, 'Y', '是', 'N', '否') is_rest,\n" + 
			"       r.enterprise_code,\n" + 
			"       s.speciality_code\n" + 
			"  from run_c_shift_time r, run_c_shift_initial s\n" + 
			" where r.initial_no = s.initial_no\n" + 
			"   and s.speciality_code = '"+specialCode+"'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and r.is_use = 'Y'\n" + 
			"   and s.is_use = 'Y'\n" + 
			" order by r.initial_no, r.shift_serial";
		return bll.queryByNativeSQL(sql);
	}
	public List<RunCShiftTime> findIsShfitTimeListBySpecial(String enterpriseCode,String specialCode){
		String sql="select r.*\n" +
//			"       r.initial_no,\n" + 
//			"       s.initial_name,\n" + 
//			"       r.shift_time_name,\n" + 
//			"       to_char(r.on_duty_time, 'hh24:mi:ss') on_duty_time,\n" + 
//			"       to_char(r.off_duty_time, 'hh24:mi:ss') off_duty_time,\n" + 
//			"       r.shift_serial,\n" + 
//			"       r.shift_time_desc,\n" + 
//			"       r.is_use,\n" + 
//			"       decode(r.is_rest, 'Y', '是', 'N', '否') is_rest,\n" + 
//			"       r.enterprise_code,\n" + 
//			"       s.speciality_code\n" + 
			"  from run_c_shift_time r, run_c_shift_initial s\n" + 
			" where r.initial_no = s.initial_no\n" + 
			"   and s.speciality_code = '"+specialCode+"'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" +
			"   and r.is_rest = 'N'\n" + 
			"   and r.is_use = 'Y'\n" + 
			"   and s.is_use = 'Y'\n" + 
			" order by r.initial_no, r.shift_serial";
		return bll.queryByNativeSQL(sql,RunCShiftTime.class);
	}
	public boolean isTimeNameExsit(String name,Long initalNo,Long id,String enterpriseCode){
		String sql;
		if(id != null)
		{
		  sql="select count(*)\n" +
			"  from run_c_shift_time r\n" + 
			" where r.initial_no = "+initalNo+"\n" + 
			"   and r.shift_time_name = '"+name+"'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and r.shift_time_id <> "+id+"\n" + 
			"   and r.is_use = 'Y'";
		}
		else
		{
			sql="select count(*)\n" +
			"  from run_c_shift_time r\n" + 
			" where r.initial_no = "+initalNo+"\n" + 
			"   and r.shift_time_name = '"+name+"'\n" + 
			"   and r.is_use = 'Y'";
		}
		if(Long.parseLong(bll.getSingal(sql).toString()) == 0){
			return false;
		}
		else
		{
			return true;
		}
	}
	public Long tiemCount(Long intialNo,String enterpriseCode){
		String sql="select count(*)\n" +
			"  from run_c_shift_time t\n" + 
			" where t.initial_no = "+intialNo+"\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.is_rest= 'N'\n" + 
			"   and t.is_use = 'Y'";
		return Long.parseLong(bll.getSingal(sql).toString());
	}
	public Object findTimeModel(Long id){
		String sql="select r.shift_time_id,\n" +
			"       r.initial_no,\n" + 
			"       r.shift_time_name,\n" + 
			"       to_char(r.on_duty_time, 'hh24:mi:ss') on_duty_time,\n" + 
			"       to_char(r.off_duty_time, 'hh24:mi:ss'),\n" + 
			"       r.shift_serial,\n" + 
			"       r.shift_time_desc,\n" + 
			"       r.is_rest\n" + 
			"  from run_c_shift_time r\n" + 
			" where r.shift_time_id = "+id+"";
		return bll.getSingal(sql);
	}
	/*
	 * 包括休息班
	 */
	public List<RunCShiftTime> findTimeListByNo(Long intialNo,String enterpriseCode){
		String sql="select t.*\n" +
			"  from run_c_shift_time t\n" + 
			" where t.initial_no = "+intialNo+"\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.is_use = 'Y'\n" +
			" order by t.shift_serial";
		return bll.queryByNativeSQL(sql, RunCShiftTime.class);
	}
	public List<RunCShiftTime> findTimeListByNoEr(Long intialNo,String enterpriseCode){
		String sql="select t.*\n" +
			"  from run_c_shift_time t\n" + 
			" where t.initial_no = "+intialNo+"\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.is_use = 'Y'\n" +
			"   and t.is_rest = 'N'\n" +
			" order by t.shift_serial";
		return bll.queryByNativeSQL(sql, RunCShiftTime.class);
	}
	public int getShiftTimeAmount(Long shifttimeId){
		String sql="select r.time_amount\n" +
			"  from run_c_shift_initial r\n" + 
			" where r.initial_no =\n" + 
			"       (select initial_no from run_c_shift_time where shift_time_id = "+shifttimeId+")\n" + 
			"   and r.is_use = 'Y'";
		int timeAmount=Integer.parseInt(bll.getSingal(sql).toString());
		return timeAmount;
	}
}