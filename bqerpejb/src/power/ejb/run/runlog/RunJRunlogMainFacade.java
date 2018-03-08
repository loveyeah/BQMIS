package power.ejb.run.runlog;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import power.ejb.run.runlog.shift.RunCShiftWorker;
import power.ejb.run.runlog.shift.RunCShiftWorkerFacadeRemote;
import power.ejb.run.runlog.shift.RunJShiftEqustatus;
import power.ejb.run.runlog.shift.RunJShiftEqustatusFacadeRemote;
import power.ejb.run.runlog.shift.RunJShiftRecord; 
import power.ejb.run.runlog.shift.RunJShiftRecordFacadeRemote;

/**
 * Facade for entity RunJRunlogMain.
 * 
 * @see power.ejb.run.runlog.RunJRunlogMain
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJRunlogMainFacade implements RunJRunlogMainFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB (beanName="RunCShiftWorkerFacade")
	protected RunCShiftWorkerFacadeRemote wremote;
	@EJB (beanName="RunJRunlogWorkerFacade")
	protected RunJRunlogWorkerFacadeRemote workremote;
	@EJB (beanName="RunJShiftRecordFacade")
	protected RunJShiftRecordFacadeRemote recordremote;
	@EJB (beanName="RunJShiftEqustatusFacade")
	protected RunJShiftEqustatusFacadeRemote equremote;
	@EJB (beanName="RunJShiftParmFacade")
	protected RunJShiftParmFacadeRemote parmremote;
	
	/**
	 * Perform an initial save of a previously unsaved RunJRunlogMain entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunJRunlogMain entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public Long save(RunJRunlogMain entity) {
		LogUtil.log("saving RunJRunlogMain instance", Level.INFO, null);
		try {
			if(entity.getRunLogid() == null)
			{
				entity.setRunLogid(bll.getMaxId("run_j_runlog_main", "run_logid"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity.getRunLogid();
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunJRunlogMain entity.
	 * 
	 * @param entity
	 *            RunJRunlogMain entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunJRunlogMain entity) {
		LogUtil.log("deleting RunJRunlogMain instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunJRunlogMain.class, entity
					.getRunLogid());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunJRunlogMain entity and return it or a copy
	 * of it to the sender. A copy of the RunJRunlogMain entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunJRunlogMain entity to update
	 * @returns RunJRunlogMain the persisted RunJRunlogMain entity instance, may
	 *          not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunJRunlogMain update(RunJRunlogMain entity) {
		LogUtil.log("updating RunJRunlogMain instance", Level.INFO, null);
		try {
			RunJRunlogMain result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJRunlogMain findById(Long id) {
		LogUtil.log("finding RunJRunlogMain instance with id: " + id,
				Level.INFO, null);
		try {
			RunJRunlogMain instance = entityManager.find(RunJRunlogMain.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunJRunlogMain entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunJRunlogMain property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunJRunlogMain> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunJRunlogMain> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunJRunlogMain instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunJRunlogMain model where model."
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
	 * Find all RunJRunlogMain entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJRunlogMain> all RunJRunlogMain entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunJRunlogMain> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunJRunlogMain instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunJRunlogMain model";
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
	public List<RunJRunLogMainModel> findLatesRunLogList(String enterpriseCode){
		String sql="select a.run_logid,\n" +
			"       a.run_logno,\n" + 
			"       a.shift_id,\n" + 
			"       m.shift_name,\n" + 
			"       a.shift_time_id,\n" + 
			"       n.shift_time_name,\n" + 
			"       a.speciality_code,\n" + 
			"       r.speciality_name,\n" + 
			"       a.weather_key_id,\n" + 
			"       a.shift_charge,\n" + 
			"       a.away_class_leader,\n" + 
			"       a.take_class_leader,\n" + 
			"       to_char(a.away_class_time,'yyyy-MM-dd hh24:mi:ss') ,\n" + 
			//"       a.away_class_time,\n" + 
			"       to_char(a.take_class_time,'yyyy-MM-dd hh24:mi:ss') ,\n" + 
			//"       a.take_class_time,\n" + 
			"       a.approve_man,\n" + 
			"       a.approve_content,\n" + 
			"       a.is_delay,\n" + 
			"       a.delay_man,\n" + 
			"       a.delay_content,\n" + 
			"       getworkername(a.away_class_leader) name,\n"+
			"       (select p.weather_name from run_c_log_weather p where p.weather_key_id=a.weather_key_id) weather_name \n"+
			"  from run_j_runlog_main    a,\n" + 
			"       run_c_shift          m,\n" + 
			"       run_c_shift_time     n,\n" + 
			"       run_c_unitprofession r\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.Enterprise_Code = '"+enterpriseCode+"'\n" + 
			"   and a.run_logno =\n" + 
			"       (select max(run_logno)\n" + 
			"          from run_j_runlog_main b\n" + 
			"         where b.is_use = 'Y'\n" + 
			"           and b.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and b.speciality_code = a.speciality_code)\n" + 
			"   and m.shift_id = a.shift_id\n" + 
			"   and n.shift_time_id = a.shift_time_id\n" + 
			"   and r.speciality_code = a.speciality_code\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and r.is_use = 'Y'";
		List list= bll.queryByNativeSQL(sql);
		List<RunJRunLogMainModel> arraylist=new ArrayList();
		Iterator it=list.iterator();
		while(it.hasNext())
 		{
			Object[] data=(Object[])it.next();
			RunJRunLogMainModel model=new RunJRunLogMainModel();
			if(data[0]!=null)
			{
				model.setRunLogid(Long.parseLong(data[0].toString()));
			}
			if(data[1]!=null)
			{
				model.setRunLogno((data[1].toString()));
			}
			if(data[2]!=null)
			{
				model.setShiftId(Long.parseLong(data[2].toString()));
			}
			if(data[3]!=null)
			{
				model.setShiftName(data[3].toString());
			}
			if(data[4]!=null)
			{
				model.setShiftTimeId(Long.parseLong(data[4].toString()));
			}
			if(data[5]!=null)
			{
				model.setShiftTimeName(data[5].toString());
			}
			if(data[6]!=null)
			{
				model.setSpecialityCode(data[6].toString());
			}
			if(data[7]!=null)
			{
				model.setSpecialityName(data[7].toString());
			}
			if(data[8]!=null)
			{
				model.setWeatherKeyId(Long.parseLong(data[8].toString()));
			}
			if(data[9]!=null)
			{
				model.setShiftCharge(data[9].toString());
			}
			if(data[10]!=null)
			{
				model.setAwayClassLeader(data[10].toString());
			}
			if(data[11]!=null)
			{
				model.setTakeClassLeader(data[11].toString());
			}
			if(data[12]!=null)
			{
				model.setAwayClassTime(data[12].toString());
			}
			if(data[13]!=null)
			{
				model.setTakeClassTime(data[13].toString());
			}
			
			if(data[19]!=null)
			{
				model.setAwayLeaderName(data[19].toString());
			}
			if(data[20]!=null)
			{
				model.setWeathername(data[20].toString());
			}
			arraylist.add(model);
 		}
		return arraylist;
		
	}
	public List findRunLogByWorker(String enterpriseCode,String worker){
		String sql="select r.run_logid,\n" +
			"       r.run_logno,\n" + 
			"       r.shift_id,\n" + 
			"       m.shift_name,\n" + 
			"       r.shift_time_id,\n" + 
			"       n.shift_time_name,\n" + 
			"       r.speciality_code,\n" + 
			"       s.speciality_name,\n" + 
			"       r.weather_key_id,\n" + 
			"       r.away_class_leader,\n" + 
			"       to_char(r.take_class_time,'yyyy-MM-dd hh24:mi:ss') ,\n" + 
			"       r.take_class_leader,\n" + 
			"       to_char(r.away_class_time,'yyyy-MM-dd hh24:mi:ss')\n" + 
			"  from run_j_runlog_main r,\n" + 
			"       (select a.speciality_code, a.run_logno\n" + 
			"          from (select r.speciality_code, max(r.run_logno) run_logno\n" + 
			"                  from run_j_runlog_main r\n" + 
			"                 where r.away_class_leader = '"+worker+"'\n" + 
			"                   and r.is_use = 'Y'\n" + 
			"                   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"                 group by r.speciality_code) a,\n" + 
			"               (select r.speciality_code, max(r.run_logno) run_logno\n" + 
			"                  from run_j_runlog_main r\n" + 
			"                 where r.is_use = 'Y'\n" + 
			"                   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"                 group by r.speciality_code) b\n" + 
			"         where a.run_logno = b.run_logno\n" + 
			"           and a.speciality_code = b.speciality_code) c,\n" + 
			"       run_c_shift m,\n" + 
			"       run_c_shift_time n,\n" + 
			"       run_c_unitprofession s\n" + 
			" where r.run_logno = c.run_logno\n" + 
			"   and r.speciality_code = c.speciality_code\n" + 
			"   and r.is_use = 'Y'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and r.shift_id = m.shift_id\n" + 
			"   and r.shift_time_id = n.shift_time_id\n" + 
			"   and r.speciality_code = s.speciality_code\n" + 
			"   and s.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and s.is_use = 'Y'";
		return bll.queryByNativeSQL(sql);
	}
	
	public List<RunJRunlogMain> findLowSpecial(String enterpriseCode,String specialcode,String runlogNo){
		String sql="select m.*\n" +
			"  from run_j_runlog_main m\n" + 
			" where m.speciality_code in (select n.speciality_code\n" + 
			"                               from run_c_unitprofession n\n" + 
			"                              where n.h_speciality_code = '"+specialcode+"'\n" + 
			"                                and n.enterprise_code = '"+enterpriseCode+"'\n" + 
			"                                and n.is_use = 'Y')\n" + 
			"   and m.take_class_leader is null\n" + 
			"   and m.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and (m.run_logno = '"+runlogNo+"' or m.run_logno < '"+runlogNo+"')\n" + 
			"   and m.is_use = 'Y'";
			return bll.queryByNativeSQL(sql, RunJRunlogMain.class);

	}
	public Long findPreviousLogid(Long latestId,String enterpriseCode){
		String sql=
			"select r.*\n" +
			"  from run_j_runlog_main r\n" + 
			" where r.speciality_code = (select t.speciality_code\n" + 
			"                              from run_j_runlog_main t\n" + 
			"                             where t.run_logid = "+latestId+")\n" + 
			"   and r.run_logid <> "+latestId+"\n" + 
			"   and r.is_use = 'Y'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			" order by r.run_logid desc";

		List<RunJRunlogMain> list=bll.queryByNativeSQL(sql, RunJRunlogMain.class);
		if(list.size()>0)
		{
			return list.get(0).getRunLogid();
		}
		else
		{
			return null;
		}
		
	}
	public boolean isRunlogExsit(String runlogno,String specialcode,String enterprisecode)
	{
		String sql=
			"select count(*)\n" +
			"  from run_j_runlog_main r\n" + 
			" where r.run_logno = '"+runlogno+"'\n" + 
			"   and r.speciality_code = '"+specialcode+"'\n" + 
			"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
			"   and r.is_use = 'Y'";
		int count=Integer.parseInt(bll.getSingal(sql).toString());
		if(count > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public Long findLogidByShift(String enterprisecode,Long shiftid){
		String sql="select max(r.run_logid)\n" +
			"  from run_j_runlog_main r\n" + 
			" where r.shift_id = "+shiftid+"\n" + 
			"   and r.is_use = 'Y'\n" + 
			"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
			"   and r.take_class_leader is null";
		if(bll.getSingal(sql) != null)
		{
			return Long.parseLong(bll.getSingal(sql).toString());
		}
		else
		{
			return null;
		}
	}
	public PageObject getRunLogList(String specialcode,String enterprisecode,String fromdate,String enddate,final int...rowStartIdxAndCount){
		long count=0;
		String sql1="select count(1)\n" +
			"  from run_j_runlog_main r\n" + 
			" where r.speciality_code = '"+specialcode+"'\n" + 
			"   and to_date(substr(r.run_logno, 0, 4) || '-' ||\n" + 
			"               substr(r.run_logno, 5, 2) || '-' ||\n" + 
			"               substr(r.run_logno, 7, 2),\n" + 
			"               'yyyy-mm-dd') between to_date('"+fromdate+"', 'yyyy-mm-dd') and\n" + 
			"       to_date('"+enddate+"', 'yyyy-mm-dd')\n" + 
			"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
			"   and r.is_use = 'Y'";
		Object objcount=bll.getSingal(sql1);
		if(objcount != null)
		{
			count=Long.parseLong(objcount.toString());
		}
		String sql2=
		"select r.run_logid,\n" +
		"       r.run_logno,\n" + 
		"       r.shift_id,\n" + 
		"       (select m.shift_name from run_c_shift m where m.shift_id = r.shift_id) shift_name,\n" + 
		"       r.shift_time_id,\n" + 
		"       (select n.shift_time_name\n" + 
		"          from run_c_shift_time n\n" + 
		"         where n.shift_time_id = r.shift_time_id) shift_time_name,\n" + 
		"       r.speciality_code,\n" + 
		"       r.weather_key_id,\n" + 
		"       (select v.weather_name\n" + 
		"          from run_c_log_weather v\n" + 
		"         where v.weather_key_id = r.weather_key_id) weather_key_name,\n" + 
		"       r.take_class_leader,\n" + 
		"       to_char(r.away_class_time, 'yyyy-MM-dd hh24:mi:ss'),\n" + 
		"       to_char(r.take_class_time, 'yyyy-MM-dd hh24:mi:ss'),\n" + 
		"       r.approve_man,\n" + 
		"       r.approve_content,\n" + 
		"       getspecialname(r.speciality_code) speciality_name,\n" + 
		"       getworkername(r.away_class_leader) away_name,\n" + 
		"       getworkername(r.take_class_leader) take_name\n" + 
		"  from run_j_runlog_main r\n" + 
		" where r.speciality_code = '"+specialcode+"'\n" + 
		"   and to_date(substr(r.run_logno, 0, 4) || '-' ||\n" + 
		"               substr(r.run_logno, 5, 2) || '-' ||\n" + 
		"               substr(r.run_logno, 7, 2),\n" + 
		"               'yyyy-mm-dd') between to_date('"+fromdate+"', 'yyyy-mm-dd') and\n" + 
		"       to_date('"+enddate+"', 'yyyy-mm-dd')\n" + 
		"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
		"   and r.is_use = 'Y'\n" + 
		" order by r.run_logno desc, r.run_logid desc";
		List list= bll.queryByNativeSQL(sql2, rowStartIdxAndCount);
		List<RunJRunLogMainModel> arraylist=new ArrayList();
		Iterator it=list.iterator();
		while(it.hasNext())
 		{
			Object[] data=(Object[])it.next();
			RunJRunLogMainModel model=new RunJRunLogMainModel();
			if(data[0]!=null)
			{
				model.setRunLogid(Long.parseLong(data[0].toString()));
			}
			if(data[1]!=null)
			{
				model.setRunLogno((data[1].toString()));
			}
			if(data[2]!=null)
			{
				model.setShiftId(Long.parseLong(data[2].toString()));
			}
			if(data[3]!=null)
			{
				model.setShiftName(data[3].toString());
			}
			if(data[4]!=null)
			{
				model.setShiftTimeId(Long.parseLong(data[4].toString()));
			}
			if(data[5]!=null)
			{
				model.setShiftTimeName(data[5].toString());
			}
			if(data[6]!=null)
			{
				model.setSpecialityCode(data[6].toString());
			}
			if(data[7]!=null)
			{
				model.setWeatherKeyId(Long.parseLong(data[7].toString()));
			}
			if(data[8]!=null)
			{
				model.setWeathername(data[8].toString());
			}
			if(data[10]!=null)
			{
				model.setAwayClassTime(data[10].toString());
			}
			if(data[11]!=null)
			{
				model.setTakeClassTime(data[11].toString());
			}
			
			if(data[14]!=null)
			{
				model.setSpecialityName(data[14].toString());
			}
			
			if(data[15]!=null)
			{
				model.setAwayLeaderName(data[15].toString());
			}
			if(data[16]!=null)
			{
				model.setTakeLeaderName(data[16].toString());
			}
			arraylist.add(model);
 		}
		PageObject poj=new PageObject();
		poj.setList(arraylist);
		poj.setTotalCount(count);
		return poj;
	}
	public RunJRunlogMain findLatestModelBySpecial(String specialcode,String enterprisecode){
		String sql="select t.*\n" +
			"  from run_j_runlog_main t\n" + 
			" where t.speciality_code = '"+specialcode+"'\n" + 
			"   and t.enterprise_code = '"+enterprisecode+"'\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and t.run_logid =\n" + 
			"       (select max(m.run_logid)\n" + 
			"          from run_j_runlog_main m\n" + 
			"         where m.speciality_code = '"+specialcode+"'\n" + 
			"           and m.enterprise_code = '"+enterprisecode+"' and m.is_use='Y')";
		List<RunJRunlogMain> list=bll.queryByNativeSQL(sql, RunJRunlogMain.class);
		if(list.size()>0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}
	
	public RunJRunlogMain shiftHandOperation(Long oldLogid,String newlogno,Long newshiftid,Long newtimeid,String handworker)
	{
		RunJRunlogMain oldLog=this.findById(oldLogid);
		RunJRunlogMain newLog=new RunJRunlogMain();
		newLog.setRunLogno(newlogno);
    	newLog.setShiftId(newshiftid);
        newLog.setShiftTimeId(newtimeid);
        newLog.setSpecialityCode(oldLog.getSpecialityCode());
        newLog.setAwayClassLeader(handworker);
        newLog.setTakeClassTime(new Date());
        newLog.setEnterpriseCode(oldLog.getEnterpriseCode());
        newLog.setIsUse("Y");
        newLog.setWeatherKeyId(oldLog.getWeatherKeyId());
        
        oldLog.setTakeClassLeader(handworker);
        oldLog.setAwayClassTime(new Date());
        //修改运行日志信息
        this.update(oldLog);
        //增加新的运行日志记录
        Long newLogId=bll.getMaxId("run_j_runlog_main", "run_logid");
        newLog.setRunLogid(newLogId);
        this.save(newLog);
        //增加值班人员
        List<RunCShiftWorker> list=wremote.findListByShift(newshiftid, oldLog.getEnterpriseCode());
        RunJRunlogWorker workermodel=new RunJRunlogWorker();
        RunCShiftWorker swmodel=wremote.getShiftWorker(oldLog.getEnterpriseCode(), handworker, newshiftid);
        Long maxworkerid=bll.getMaxId("run_j_runlog_worker", "runlog_worker_id");
        if(swmodel == null){//不是本班人接的班
        	for(int i=0;i<list.size();i++)
	            {
	            	RunCShiftWorker omodel=list.get(i);
	            	if("Y".equals(omodel.getIsHand()))
	            	{
	            		workermodel.setRunlogWorkerId(maxworkerid+i+1);
	            		workermodel.setRunLogid(newLogId);
		            	workermodel.setBookedEmployee(list.get(i).getEmpCode());
		            	workermodel.setEnterpriseCode(oldLog.getEnterpriseCode());
		            	workermodel.setIsUse("Y");
		            	workermodel.setOperateBy(oldLog.getAwayClassLeader());
		            	workermodel.setOperateTime(new Date());
		            	workermodel.setWoWorktype("LOGABS");
	            	}
	            	else
	            	{
	            		workermodel.setRunlogWorkerId(maxworkerid+i+1);
	            		workermodel.setRunLogid(newLogId);
		            	workermodel.setBookedEmployee(list.get(i).getEmpCode());
		            	workermodel.setEnterpriseCode(oldLog.getEnterpriseCode());
		            	workermodel.setIsUse("Y");
		            	workermodel.setOperateBy(oldLog.getAwayClassLeader());
		            	workermodel.setOperateTime(new Date());
		            	workermodel.setWoWorktype("LOGONS");
	            	}
	            	workremote.save(workermodel);
	            }
        	 workermodel.setRunlogWorkerId(maxworkerid);
        	 workermodel.setRunLogid(newLogId);
             workermodel.setBookedEmployee(handworker);
             workermodel.setEnterpriseCode(oldLog.getEnterpriseCode());
             workermodel.setIsUse("Y");
             workermodel.setOperateBy(oldLog.getAwayClassLeader());
             workermodel.setOperateTime(new Date());
             workermodel.setWoWorktype("LOGSUC");
             workremote.save(workermodel);
        }
        else
        {
    	 for(int i=0;i<list.size();i++)
	            {
	            	workermodel.setRunlogWorkerId(maxworkerid+i);
    		 		workermodel.setRunLogid(newLogId);
		            workermodel.setBookedEmployee(list.get(i).getEmpCode());
		            workermodel.setEnterpriseCode(oldLog.getEnterpriseCode());
		            workermodel.setIsUse("Y");
		            workermodel.setOperateBy(oldLog.getAwayClassLeader());
		            workermodel.setOperateTime(new Date());
		            if(list.get(i).getEmpCode().equals(handworker))
		            {
		            	workermodel.setWoWorktype("LOGSUC");
		            }
		            else
		            {
		            	workermodel.setWoWorktype("LOGONS");
		            }
	            	workremote.save(workermodel);
	            }
        }
       
        //增加值班记事未完成项
        List<RunJShiftRecord> recordlist=recordremote.findNotCompletion(oldLogid, oldLog.getEnterpriseCode());
        Long maxrecordid=bll.getMaxId("run_j_shift_record","shift_record_id");
        for(int x=0;x<recordlist.size();x++){
        	RunJShiftRecord model=recordlist.get(x);
        	model.setRunLogId(newLogId);
        	model.setRecordBy(handworker);
        	model.setNotCompletionId(model.getShiftRecordId());
        	model.setShiftRecordId(maxrecordid+x);
        	recordremote.save(model);
        }
        recordlist=recordremote.findNoStatus(oldLogid, oldLog.getEnterpriseCode());
        for(int x=0;x<recordlist.size();x++){
        	RunJShiftRecord model=recordlist.get(x);
        	model.setIsCompletion("Y");
        	recordremote.update(model);
        }
        //增加运行方式
        PageObject equresult = equremote.findEquStatusByRunLog(oldLogid);
		List<RunJShiftEqustatus> equlist = equresult.getList();
		Long maxshiftequid=bll.getMaxId("RUN_J_SHIFT_EQUSTATUS", "shift_equstatus_id");
		if(equlist != null)
		{
			for(int i =0; i<equlist.size();i++)
			{
				RunJShiftEqustatus model = equlist.get(i);
				if(model != null)
				{
					model.setShiftEqustatusId(maxshiftequid+i);
					model.setRunLogid(newLogId);
					model.setRunLogno(newLog.getRunLogno());
					equremote.save(model);
				}
			}
		}
        
       // 增加运行参数
        PageObject result=parmremote.findParmList(oldLogid);
		List<RunJShiftParm> parmlist=result.getList();
		Long maxparmid=bll.getMaxId("run_j_shift_parm", "shift_parm_id");
		if(parmlist!=null)
		{
			for(int i=0;i<parmlist.size();i++)
			{
				RunJShiftParm model=parmlist.get(i);
				if(model!=null)
				{
					model.setShiftParmId(maxparmid+i);
					model.setRunLogid(newLogId);
					parmremote.save(model);
				}
			}
			
		}
		return newLog;
		
	}
}