package power.ejb.run.runlog;

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
 * Facade for entity RunJShiftParm.
 * 
 * @see power.ejb.run.runlog.RunJShiftParm
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJShiftParmFacade implements RunJShiftParmFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public void save(RunJShiftParm entity) {
		if(entity.getShiftParmId() == null)
		{
			entity.setShiftParmId(bll.getMaxId("run_j_shift_parm", "shift_parm_id"));
		}
		entity.setIsUse("Y");
		entityManager.persist(entity);
	}

	
	public void delete(RunJShiftParm entity) {
		LogUtil.log("deleting RunJShiftParm instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunJShiftParm.class, entity
					.getShiftParmId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public RunJShiftParm update(RunJShiftParm entity) {
		LogUtil.log("updating RunJShiftParm instance", Level.INFO, null);
		try {
			RunJShiftParm result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJShiftParm findById(Long id) {
		LogUtil.log("finding RunJShiftParm instance with id: " + id,
				Level.INFO, null);
		try {
			RunJShiftParm instance = entityManager
					.find(RunJShiftParm.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}




	@SuppressWarnings("unchecked")
	public List<RunJShiftParm> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunJShiftParm instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunJShiftParm model";
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
	
	public PageObject findParmList(Long runLogId,final int... rowStartIdxAndCount)
	{
		try {
			PageObject result = new PageObject(); 
			String sql=
				"select * from run_j_shift_parm tt\n" +
				"where tt.run_logid="+runLogId+"\n" + 
				"and tt.is_use='Y'";


			List<RunJShiftParm> list=bll.queryByNativeSQL(sql, RunJShiftParm.class, rowStartIdxAndCount);
			String sqlCount=
				"select count(*) from run_j_shift_parm tt\n" +
				"where tt.run_logid="+runLogId+"\n" + 
				"and tt.is_use='Y'";
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void insertNewShiftParm(Long oldRunLogId,Long newRunLongId)
	{
		PageObject result=this.findParmList(oldRunLogId);
		List<RunJShiftParm> list=result.getList();
		if(list!=null)
		{
			for(int i=0;i<list.size();i++)
			{
				RunJShiftParm model=list.get(0);
				if(model!=null)
				{
				model.setRunLogid(newRunLongId);
				this.save(model);
				}
			}
			
		}
	}
	public Long findMaxLogid(String specialCode,String enterprisecode){
		String sql="select max(r.run_logid)\n" +
			"  from run_j_shift_parm r\n" + 
			" where r.run_logid in (select t.run_logid\n" + 
			"                         from run_j_runlog_main t\n" + 
			"                        where t.speciality_code = '"+specialCode+"'\n" + 
			"                          and t.enterprise_code = '"+enterprisecode+"'\n" + 
			"                          and t.is_use = 'Y')\n" + 
			"  and r.enterprise_code='"+enterprisecode+"' and r.is_use='Y'";
		if(bll.getSingal(sql) != null)
		{
			return Long.parseLong(bll.getSingal(sql).toString());
		}
		else
		{
			return null;
		}
	}
	public Long findIdByparmid(Long runlogid,Long parmId,String enterpriseCode){
		String sql="select r.shift_parm_id\n" +
			"  from run_j_shift_parm r\n" + 
			" where r.run_logid = "+runlogid+"\n" + 
			"   and r.runlog_parm_id = "+parmId+"\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and r.is_use = 'Y'";
		if(bll.getSingal(sql) != null)
		{
			return Long.parseLong(bll.getSingal(sql).toString());
		}
		else
		{
			return null;
		}
	}
	public List getDistinctRunlog(String specialcode,String enterprisecode,String fromdate,String todate){
		String sql="select distinct (t.run_logno)\n" +
			"  from run_j_shift_parm r, run_j_runlog_main t\n" + 
			" where t.run_logid = r.run_logid\n" + 
			"   and t.speciality_code = '"+specialcode+"'\n" + 
			"   and to_date(substr(t.run_logno, 0, 4) || '-' ||\n" + 
			"               substr(t.run_logno, 5, 2) || '-' ||\n" + 
			"               substr(t.run_logno, 7, 2),\n" + 
			"               'yyyy-mm-dd') between to_date('"+fromdate+"', 'yyyy-mm-dd') and\n" + 
			"       to_date('"+todate+"', 'yyyy-mm-dd')\n" + 
			"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
			"   and r.is_use = 'Y'\n" + 
			"   and t.enterprise_code = '"+enterprisecode+"'\n" + 
			"   and t.is_use = 'Y'\n" + 
			" order by t.run_logno DESC";
		return bll.queryByNativeSQL(sql);
	}
	public List getDistinctParm(String specialcode,String enterprisecode,String fromdate,String todate){
		String sql=
			"select distinct (r.runlog_parm_id),\n" +
			"                m.item_name,\n" + 
			"                getspecialname(t.speciality_code) speciality_name,\n" + 
			"                (select n.unit_name\n" + 
			"                   from bp_c_measure_unit n\n" + 
			"                  where n.unit_id = m.unit_messure_id) unit_name\n" + 
			"  from run_j_shift_parm r, run_j_runlog_main t, run_c_runlog_parm m\n" + 
			" where t.run_logid = r.run_logid\n" + 
			"   and t.speciality_code = '"+specialcode+"'\n" + 
			"   and to_date(substr(t.run_logno, 0, 4) || '-' ||\n" + 
			"               substr(t.run_logno, 5, 2) || '-' ||\n" + 
			"               substr(t.run_logno, 7, 2),\n" + 
			"               'yyyy-mm-dd') between to_date('"+fromdate+"', 'yyyy-mm-dd') and\n" + 
			"       to_date('"+todate+"', 'yyyy-mm-dd')\n" + 
			"   and m.runlog_parm_id = r.runlog_parm_id\n" + 
			"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
			"   and r.is_use = 'Y'\n" + 
			"   and t.enterprise_code = '"+enterprisecode+"'\n" + 
			"   and t.is_use = 'Y'\n" + 
			" order by r.runlog_parm_id ASC";
		return bll.queryByNativeSQL(sql);
	}
	public RunJShiftParm queryRunlogParm(String enterprisecode,Long parmId,String runlogno){
		String sql="select r.*\n" +
			"    from run_j_shift_parm r, run_j_runlog_main t\n" + 
			"   where t.run_logid = r.run_logid\n" + 
			"     and r.runlog_parm_id = "+parmId+"\n" + 
			"     and t.run_logno = '"+runlogno+"'\n" + 
			"     and t.enterprise_code='"+enterprisecode+"'\n" + 
			"     and t.is_use='Y'\n" + 
			"     and r.is_use='Y'\n" + 
			"     and r.enterprise_code='"+enterprisecode+"'";
		List<RunJShiftParm> list=bll.queryByNativeSQL(sql, RunJShiftParm.class);
		if(list.size() > 0){
			return list.get(0);
		}
		else
		{
			return null;
		}
	}
	public List queryParmList(String specialcode,String enterprisecode,String fromdate,String todate)
	{
		String sql="select c.speciality_name,\n" +
			"       c.item_name,\n" + 
			"       c.unit_name" ;
			List<String> list=this.getDistinctRunlog(specialcode, enterprisecode, fromdate, todate);
			if(list.size() > 0)
			{
				for(int i=0;i<list.size();i++)
				{
					String o=list.get(i);
					sql=sql+",\n";
					sql=sql+
					"       sum(nvl(decode(c.run_logno, '"+o+"', c.item_number_value), ''))";
				}
				sql=sql+"\n";
				sql=sql+"  from (select getspecialname(t.speciality_code) speciality_name,\n" + 
				"               t.run_logno,\n" + 
				"               r.runlog_parm_id,\n" + 
				"               (select a.item_name\n" + 
				"                  from run_c_runlog_parm a\n" + 
				"                 where a.runlog_parm_id = r.runlog_parm_id) item_name,\n" + 
				"               (select b.unit_name\n" + 
				"                  from bp_c_measure_unit b\n" + 
				"                 where b.unit_id =\n" + 
				"                       (select a.unit_messure_id\n" + 
				"                          from run_c_runlog_parm a\n" + 
				"                         where a.runlog_parm_id = r.runlog_parm_id)) unit_name,\n" + 
				"               r.item_number_value\n" + 
				"          from run_j_shift_parm r, run_j_runlog_main t\n" + 
				"         where t.run_logid = r.run_logid\n" + 
				"           and t.speciality_code = 'ZZ'\n" + 
				"           and to_date(substr(t.run_logno, 0, 4) || '-' ||\n" + 
				"                       substr(t.run_logno, 5, 2) || '-' ||\n" + 
				"                       substr(t.run_logno, 7, 2),\n" + 
				"                       'yyyy-mm-dd') between\n" + 
				"               to_date('2008-11-01', 'yyyy-mm-dd') and\n" + 
				"               to_date('2008-11-30', 'yyyy-mm-dd')\n" + 
				"           and r.enterprise_code = 'hfdc'\n" + 
				"           and r.is_use = 'Y'\n" + 
				"           and t.enterprise_code = 'hfdc'\n" + 
				"           and t.is_use = 'Y'\n" + 
				"         order by t.run_logno ASC) c\n" + 
				" group by c.speciality_name, c.item_name, c.unit_name";
				return bll.queryByNativeSQL(sql);
			}
			else
			{
				return null;
			}

	}
}