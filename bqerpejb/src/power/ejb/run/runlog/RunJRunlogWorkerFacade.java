package power.ejb.run.runlog;

import java.util.ArrayList;
import java.util.Iterator;
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
 * Facade for entity RunJRunlogWorker.
 * 
 * @see power.ejb.run.runlog.RunJRunlogWorker
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJRunlogWorkerFacade implements RunJRunlogWorkerFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved RunJRunlogWorker entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunJRunlogWorker entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunJRunlogWorker entity) {
		LogUtil.log("saving RunJRunlogWorker instance", Level.INFO, null);
		try {
			if(entity.getRunlogWorkerId() == null)
			{
				entity.setRunlogWorkerId(bll.getMaxId("run_j_runlog_worker", "runlog_worker_id"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunJRunlogWorker entity.
	 * 
	 * @param entity
	 *            RunJRunlogWorker entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunJRunlogWorker entity) {
		LogUtil.log("deleting RunJRunlogWorker instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunJRunlogWorker.class, entity
					.getRunlogWorkerId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunJRunlogWorker entity and return it or a
	 * copy of it to the sender. A copy of the RunJRunlogWorker entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunJRunlogWorker entity to update
	 * @returns RunJRunlogWorker the persisted RunJRunlogWorker entity instance,
	 *          may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunJRunlogWorker update(RunJRunlogWorker entity) {
		LogUtil.log("updating RunJRunlogWorker instance", Level.INFO, null);
		try {
			RunJRunlogWorker result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJRunlogWorker findById(Long id) {
		LogUtil.log("finding RunJRunlogWorker instance with id: " + id,
				Level.INFO, null);
		try {
			RunJRunlogWorker instance = entityManager.find(
					RunJRunlogWorker.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunJRunlogWorker entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunJRunlogWorker property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunJRunlogWorker> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunJRunlogWorker> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunJRunlogWorker instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunJRunlogWorker model where model."
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
	 * Find all RunJRunlogWorker entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJRunlogWorker> all RunJRunlogWorker entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunJRunlogWorker> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunJRunlogWorker instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunJRunlogWorker model";
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
public List<RunJRunlogWorkerModel> findRunLogWorkerAll(String enterpriseCode,String runlogno,String specialcode){
	String sql=
		"select r.runlog_worker_id,\n" +
		"       r.run_logid,\n" + 
		"       r.wo_worktype,\n" + 
		"       r.booked_employee,\n" +
		"       r.operate_memo,\n" + 
		"       x.speciality_code,\n" + 
		"       getworkername(r.booked_employee),\n" +
		"       getspecialname(x.speciality_code)\n" +
		"  from run_j_runlog_worker r, run_j_runlog_main x\n" + 
		" where r.run_logid in (select t.run_logid\n" + 
		"                         from run_j_runlog_main t\n" + 
		"                        where t.speciality_code in\n" + 
		"                              (select m.speciality_code\n" + 
		"                                 from run_c_unitprofession m\n" + 
		"                                where m.speciality_code = '"+specialcode+"'\n" + 
		"                                   or m.h_speciality_code = '"+specialcode+"'\n" + 
		"                                  and m.is_use = 'Y'\n" + 
		"                                  and m.enterprise_code = '"+enterpriseCode+"')\n" + 
		"                          and t.run_logno = '"+runlogno+"'\n" + 
		"                          and t.is_use = 'Y'\n" + 
		"                          and t.enterprise_code = '"+enterpriseCode+"')\n" + 
		"   and x.run_logid = r.run_logid and r.is_use='Y' and r.enterprise_code='"+enterpriseCode+"'";
	List list= bll.queryByNativeSQL(sql);
	List<RunJRunlogWorkerModel> arraylist=new ArrayList();
	Iterator it=list.iterator();
	while(it.hasNext())
		{
		Object[] data=(Object[])it.next();
		RunJRunlogWorkerModel model=new RunJRunlogWorkerModel();
		if(data[0]!=null)
		{
			model.setRunlogWorkerId(Long.parseLong(data[0].toString()));
		}
		if(data[2]!=null)
		{
			model.setWoWorktype(data[2].toString());
		}
		if(data[3]!=null)
		{
			model.setBookedEmployee(data[3].toString());
		}
		if(data[4]!=null)
		{
			model.setOperateMemo(data[4].toString());
		}
		if(data[5]!=null)
		{
			model.setSpecialCode(data[5].toString());
		}
		if(data[6]!=null)
		{
			model.setWorkerName(data[6].toString());
		}
		if(data[7]!=null)
		{
			model.setSpecialName(data[7].toString());
		}
		arraylist.add(model);
		}
	return arraylist;
	}
public boolean isExsit(Long runlogid,String empcode,String enterprisecode){
		String sql="select count(*)\n" +
			"  from run_j_runlog_worker r\n" + 
			" where r.run_logid = "+runlogid+"\n" + 
			"   and r.booked_employee = '"+empcode+"'\n" + 
			"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
			"   and r.is_use = 'Y'";
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
public RunJRunlogWorker findModelByemp(Long runlogid,String workercode,String enterprisecode){
	String sql="select r.*\n" +
		"  from run_j_runlog_worker r\n" + 
		" where r.run_logid = "+runlogid+"\n" + 
		"   and r.booked_employee = '"+workercode+"'\n" + 
		"   and r.is_use = 'Y'\n" + 
		"   and r.enterprise_code = '"+enterprisecode+"'";
	List<RunJRunlogWorker> list= bll.queryByNativeSQL(sql, RunJRunlogWorker.class);
	if(list.size()>0)
	{
		return list.get(0);
	}
	else
	{
		return null;
	}
}
public PageObject queryWorkerList(String specialcode,String enterprisecode,String fromdate,String todate,final int... rowStartIdxAndCount){
	Long count=0L;
	String sql1=
		"select count(1)\n" +
		"  from run_j_runlog_worker r, run_j_runlog_main t\n" + 
		" where t.run_logid = r.run_logid\n" + 
		"   and to_date(substr(t.run_logno, 0, 4) || '-' ||\n" + 
		"               substr(t.run_logno, 5, 2) || '-' ||\n" + 
		"               substr(t.run_logno, 7, 2),\n" + 
		"               'yyyy-mm-dd') between to_date('"+fromdate+"', 'yyyy-mm-dd') and\n" + 
		"       to_date('"+todate+"', 'yyyy-mm-dd')\n" + 
		"   and t.speciality_code = '"+specialcode+"'\n" + 
		"   and r.is_use = 'Y'\n" + 
		"   and t.is_use = 'Y'\n" + 
		"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
		"   and t.enterprise_code = '"+enterprisecode+"' order by t.run_logno desc";
	Object cobj=bll.getSingal(sql1);
	if(cobj != null)
	{
		count=Long.parseLong(cobj.toString());
	}
	String sql2="select r.runlog_worker_id,\n" +
		"       r.run_logid,\n" + 
		"       r.wo_worktype,\n" + 
		"       r.booked_employee,\n" + 
		"       getworkername(r.booked_employee) worker_name,\n" + 
		"       r.operate_memo,\n" + 
		"       t.run_logno,\n" + 
		"       t.speciality_code,\n" + 
		"       getspecialname(t.speciality_code) speciality_name,\n" + 
		"       (select a.shift_name from run_c_shift a where a.shift_id=t.shift_id) shift_name,\n" +
		"       t.shift_id\n" + 
		"  from run_j_runlog_worker r, run_j_runlog_main t\n" + 
		" where t.run_logid = r.run_logid\n" + 
		"   and to_date(substr(t.run_logno, 0, 4) || '-' ||\n" + 
		"               substr(t.run_logno, 5, 2) || '-' ||\n" + 
		"               substr(t.run_logno, 7, 2),\n" + 
		"               'yyyy-mm-dd') between to_date('"+fromdate+"', 'yyyy-mm-dd') and\n" + 
		"       to_date('"+todate+"', 'yyyy-mm-dd')\n" + 
		"   and t.speciality_code = '"+specialcode+"'\n" + 
		"   and r.is_use = 'Y'\n" + 
		"   and t.is_use = 'Y'\n" + 
		"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
		"   and t.enterprise_code = '"+enterprisecode+"' order by t.run_logno desc,r.runlog_worker_id";
	List list= bll.queryByNativeSQL(sql2, rowStartIdxAndCount);
	List<RunJRunlogWorkerModel> arraylist=new ArrayList();
	Iterator it=list.iterator();
	while(it.hasNext())
		{
			Object[] data=(Object[])it.next();
			RunJRunlogWorkerModel model=new RunJRunlogWorkerModel();
			if(data[0]!=null)
			{
				model.setRunlogWorkerId(Long.parseLong(data[0].toString()));
			}
			if(data[2]!=null)
			{
				model.setWoWorktype(data[2].toString());
			}
			if(data[3]!=null)
			{
				model.setBookedEmployee(data[3].toString());
			}
			if(data[4]!=null)
			{
				model.setWorkerName(data[4].toString());
			}
			if(data[5]!=null)
			{
				model.setOperateMemo(data[5].toString());
			}
			if(data[6]!=null)
			{
				model.setRunLogno(data[6].toString());
			}
			if(data[7]!=null)
			{
				model.setSpecialCode(data[7].toString());
			}
			if(data[8]!=null)
			{
				model.setSpecialName(data[8].toString());
			}
			if(data[9]!=null)
			{
				model.setShiftName(data[9].toString());
			}
			if(data[10]!=null)
			{
				model.setShiftId(Long.parseLong(data[10].toString()));
			}
		arraylist.add(model);
		}
	PageObject pobj=new PageObject();
	pobj.setList(arraylist);
	pobj.setTotalCount(count);
	return pobj;
	}
public PageObject queryAbsentWorkerList(String specialcode,String enterprisecode,String fromdate,String todate,final int... rowStartIdxAndCount){
	Long count=0L;
	String sql1=
		"select count(1)\n" +
		"  from run_j_runlog_worker r, run_j_runlog_main t\n" + 
		" where t.run_logid = r.run_logid\n" + 
		"   and to_date(substr(t.run_logno, 0, 4) || '-' ||\n" + 
		"               substr(t.run_logno, 5, 2) || '-' ||\n" + 
		"               substr(t.run_logno, 7, 2),\n" + 
		"               'yyyy-mm-dd') between to_date('"+fromdate+"', 'yyyy-mm-dd') and\n" + 
		"       to_date('"+todate+"', 'yyyy-mm-dd')\n" + 
		"   and t.speciality_code = '"+specialcode+"'\n" + 
		"   and (r.wo_worktype='LOGABS' or r.wo_worktype='LOGABG')\n" +
		"   and r.is_use = 'Y'\n" + 
		"   and t.is_use = 'Y'\n" + 
		"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
		"   and t.enterprise_code = '"+enterprisecode+"' order by t.run_logno desc";
	Object cobj=bll.getSingal(sql1);
	if(cobj != null)
	{
		count=Long.parseLong(cobj.toString());
	}
	String sql2="select r.runlog_worker_id,\n" +
		"       r.run_logid,\n" + 
		"       r.wo_worktype,\n" + 
		"       r.booked_employee,\n" + 
		"       getworkername(r.booked_employee) worker_name,\n" + 
		"       r.operate_memo,\n" + 
		"       t.run_logno,\n" + 
		"       t.speciality_code,\n" + 
		"       getspecialname(t.speciality_code) speciality_name,\n" + 
		"       (select a.shift_name from run_c_shift a where a.shift_id=t.shift_id) shift_name,\n" +
		"       t.shift_id\n" + 
		"  from run_j_runlog_worker r, run_j_runlog_main t\n" + 
		" where t.run_logid = r.run_logid\n" + 
		"   and to_date(substr(t.run_logno, 0, 4) || '-' ||\n" + 
		"               substr(t.run_logno, 5, 2) || '-' ||\n" + 
		"               substr(t.run_logno, 7, 2),\n" + 
		"               'yyyy-mm-dd') between to_date('"+fromdate+"', 'yyyy-mm-dd') and\n" + 
		"       to_date('"+todate+"', 'yyyy-mm-dd')\n" + 
		"   and t.speciality_code = '"+specialcode+"'\n" + 
		"   and (r.wo_worktype='LOGABS' or r.wo_worktype='LOGABG')\n" +
		"   and r.is_use = 'Y'\n" + 
		"   and t.is_use = 'Y'\n" + 
		"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
		"   and t.enterprise_code = '"+enterprisecode+"' order by t.run_logno desc,r.runlog_worker_id";
	List list= bll.queryByNativeSQL(sql2, rowStartIdxAndCount);
	List<RunJRunlogWorkerModel> arraylist=new ArrayList();
	Iterator it=list.iterator();
	while(it.hasNext())
		{
			Object[] data=(Object[])it.next();
			RunJRunlogWorkerModel model=new RunJRunlogWorkerModel();
			if(data[0]!=null)
			{
				model.setRunlogWorkerId(Long.parseLong(data[0].toString()));
			}
			if(data[2]!=null)
			{
				model.setWoWorktype(data[2].toString());
			}
			if(data[3]!=null)
			{
				model.setBookedEmployee(data[3].toString());
			}
			if(data[4]!=null)
			{
				model.setWorkerName(data[4].toString());
			}
			if(data[5]!=null)
			{
				model.setOperateMemo(data[5].toString());
			}
			if(data[6]!=null)
			{
				model.setRunLogno(data[6].toString());
			}
			if(data[7]!=null)
			{
				model.setSpecialCode(data[7].toString());
			}
			if(data[8]!=null)
			{
				model.setSpecialName(data[8].toString());
			}
			if(data[9]!=null)
			{
				model.setShiftName(data[9].toString());
			}
			if(data[10]!=null)
			{
				model.setShiftId(Long.parseLong(data[10].toString()));
			}
		arraylist.add(model);
		}
	PageObject pobj=new PageObject();
	pobj.setList(arraylist);
	pobj.setTotalCount(count);
	return pobj;
	}
}