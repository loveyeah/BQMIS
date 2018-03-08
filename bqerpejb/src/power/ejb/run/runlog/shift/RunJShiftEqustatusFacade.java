package power.ejb.run.runlog.shift;

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
import power.ejb.equ.base.EquCBug;
import power.ejb.hr.LogUtil;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.run.runlog.RunJRunlogWorkerModel;
import power.ejb.run.runlog.RunJShiftParm;

/**
 * Facade for entity RunJShiftEqustatus.
 * 
 * @see power.ejb.run.runlog.shift.RunJShiftEqustatus
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJShiftEqustatusFacade implements RunJShiftEqustatusFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public void save(RunJShiftEqustatus entity) {
		LogUtil.log("saving RunJShiftEqustatus instance", Level.INFO, null);
		try {
			if(entity.getShiftEqustatusId()==null)
			{
				entity.setShiftEqustatusId(bll.getMaxId("RUN_J_SHIFT_EQUSTATUS", "shift_equstatus_id"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(RunJShiftEqustatus entity) {
		LogUtil.log("deleting RunJShiftEqustatus instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunJShiftEqustatus.class,
					entity.getShiftEqustatusId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public RunJShiftEqustatus update(RunJShiftEqustatus entity) {
		LogUtil.log("updating RunJShiftEqustatus instance", Level.INFO, null);
		try {
			RunJShiftEqustatus result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJShiftEqustatus findById(Long id) {
		LogUtil.log("finding RunJShiftEqustatus instance with id: " + id,
				Level.INFO, null);
		try {
			RunJShiftEqustatus instance = entityManager.find(
					RunJShiftEqustatus.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<RunJShiftEqustatus> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunJShiftEqustatus instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunJShiftEqustatus model where model."
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

	
	@SuppressWarnings("unchecked")
	public List<RunJShiftEqustatus> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunJShiftEqustatus instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from RunJShiftEqustatus model";
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
 * 根据专业查询某种运行方式下设备状态信息
 */
	public PageObject getShiftEquStatusList(String fuzzy,String specialcode,Long runlogId,Long runKeyId,String enterpriseCode,final int... rowStartIdxAndCount)
	{
		try {
			PageObject result = new PageObject(); 
			String sql = 

				"select a.*,\n" +
				"        (select c.color_value from run_c_equstatus c where\n" + 
				"               c.equstatus_id = a.equ_status_id and\n" + 
				"               c.enterprise_code = '"+enterpriseCode+"' and c.is_use = 'Y') color_value,\n" + 
				"              (row_number() over(order by a.shift_equstatus_id)) rn\n" + 
				"         from run_j_shift_equstatus a\n" + 
				"        where a.run_logid = "+runlogId+"\n" + 
				"          and a.attribute_code in\n" + 
				"              (select d.attribute_code\n" + 
				"                 from run_c_shift_equ d\n" + 
				"                where d.run_key_id = "+runKeyId+"\n" + 
				"                  and d.speciality_code = '"+specialcode+"'\n" + 
				"                  and d.enterprise_code = '"+enterpriseCode+"'\n" + 
				"                  and d.is_use = 'Y')\n" + 
				"          and a.enterprise_code = '"+enterpriseCode+"'\n" + 
				"          and a.equ_name like '%%'\n" + 
				"          and a.is_use = 'Y'";

			List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			String sqlCount=

				"select count(1)\n" +
				"     from (select a.*,\n" + 
				"       (select c.color_value from run_c_equstatus c where\n" + 
				"              c.equstatus_id = a.equ_status_id and\n" + 
				"              c.enterprise_code = '"+enterpriseCode+"' and c.is_use = 'Y') color_value,\n" + 
				"             (row_number() over(order by a.shift_equstatus_id)) rn\n" + 
				"        from run_j_shift_equstatus a\n" + 
				"       where a.run_logid = "+runlogId+"\n" + 
				"         and a.attribute_code in\n" + 
				"             (select d.attribute_code\n" + 
				"                from run_c_shift_equ d\n" + 
				"               where d.run_key_id = "+runKeyId+"\n" + 
				"                 and d.speciality_code = '"+specialcode+"'\n" + 
				"                 and d.enterprise_code = '"+enterpriseCode+"'\n" + 
				"                 and d.is_use = 'Y')\n" + 
				"         and a.enterprise_code = '"+enterpriseCode+"'\n" + 
				"         and a.equ_name like '%%'\n" + 
				"         and a.is_use = 'Y') b";
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
			
	}
		
		@SuppressWarnings("unchecked")
		public PageObject findList(String strWhere,final int... rowStartIdxAndCount)
		{
			try {
				PageObject result = new PageObject(); 
				String sql="select * from  run_j_shift_equstatus \n";
				if(strWhere!="")
				{
					sql=sql+" where  "+strWhere;
				}
				List<RunJShiftEqustatus> list=bll.queryByNativeSQL(sql, RunJShiftEqustatus.class, rowStartIdxAndCount);
				String sqlCount="select count(*)　from run_j_shift_equstatus \n";
				if(strWhere!="")
				{
					sqlCount=sqlCount+" where  "+strWhere;
				}
				Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
				result.setList(list);
				result.setTotalCount(totalCount);
				return result;
				
			}catch (RuntimeException re) {
				LogUtil.log("find all failed", Level.SEVERE, re);
				throw re;
			}
			
		}
		
		/**
		 *  根据运行日志id和设备功能码得到一个对象实体
		 * @param runlogid
		 * @param equcode
		 * @param enterprisecode
		 * @return
		 */
		public RunJShiftEqustatus getModelByRunLog(Long runlogid,String equcode,String enterprisecode)
		{
			String strWhere = 
				"run_logid='"+runlogid+"'\n" +
				" and attribute_code='"+runlogid+"'\n" +
				"and enterprise_code='"+enterprisecode+"'\n" + 
				"and is_use='Y'";
			PageObject result=findList(strWhere);
			if(result.getList()!=null)
			{
				if(result.getList().size()>0)
				{
				return (RunJShiftEqustatus)result.getList().get(0);
				}
			}
			return null;
		}
		
		
		/**
		 * 根据运行日志ID，运行日志号查询交接班设备状态记录
		 * @param runLogId
		 * @param runlogNo
		 * @param rowStartIdxAndCount
		 * @return
		 */
		public PageObject findEquStatusByRunLog(Long runLogId,final int... rowStartIdxAndCount)
		{
			try
			{
				PageObject result = new PageObject(); 
				String sql = 
							"select *\n" +
							"from run_j_shift_equstatus t\n" + 
							"where t.run_logid = "+runLogId+"\n" + 
							"and t.is_use= 'Y'";
				List<RunJShiftEqustatus> list = bll.queryByNativeSQL(sql, RunJShiftEqustatus.class, rowStartIdxAndCount);

				String sqlCount = 
								"select count(1)\n" +
								"from run_j_shift_equstatus t\n" + 
								"where t.run_logid = "+runLogId+"\n" + 
								"and t.is_use= 'Y'";
				Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
				result.setList(list);
				result.setTotalCount(totalCount);
				return result;
			}catch (RuntimeException re) {
				LogUtil.log("find all failed", Level.SEVERE, re);
				throw re;
			}
		}
		public List<RunJShiftEqustatus> findBySpecial(String specialCode,String enterpriseCode)
		{
			String sql="select *\n" +
				"  from run_j_shift_equstatus r\n" + 
				" where r.speciality_code = '"+specialCode+"'\n" + 
				"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
				"   and r.is_use = 'Y'\n" + 
				" order by r.shift_equstatus_id desc";
			return bll.queryByNativeSQL(sql, RunJShiftEqustatus.class);
		}
		public RunJShiftEqustatus findModle(String specialCode,String enterpriseCoce,String equcode){
			String sql="select *\n" +
				"  from run_j_shift_equstatus r\n" + 
				" where r.speciality_code = '"+specialCode+"'\n" + 
				"   and r.enterprise_code = '"+enterpriseCoce+"'\n" + 
				"   and r.attribute_code='"+equcode+"'\n" + 
				"   and r.is_use = 'Y'\n" + 
				" order by r.shift_equstatus_id desc";
			List<RunJShiftEqustatus> list=bll.queryByNativeSQL(sql, RunJShiftEqustatus.class);
			if(list.size()>0)
			{
				return list.get(0);
			}
			else
			{
				return null;
			}
		}
		public PageObject queryEquStatusList(String specialcode,Long runkeyid,String enterprisecode,String fromdate,String todate,final int... rowStartIdxAndCount){
			Long count=0L;
			String sql1="select count(1)\n" +
			"  from run_j_shift_equstatus r\n" + 
			" where r.speciality_code = '"+specialcode+"'\n" + 
			"   and to_date(substr(r.run_logno, 0, 4) || '-' ||\n" + 
			"               substr(r.run_logno, 5, 2) || '-' ||\n" + 
			"               substr(r.run_logno, 7, 2),\n" + 
			"               'yyyy-mm-dd') between to_date('"+fromdate+"', 'yyyy-mm-dd') and\n" + 
			"       to_date('"+todate+"', 'yyyy-mm-dd')\n" + 
			"   and r.attribute_code in (select t.attribute_code\n" + 
			"                              from run_c_shift_equ t\n" + 
			"                             where t.run_key_id = "+runkeyid+"\n" + 
			"                               and t.speciality_code = '"+specialcode+"'\n" + 
			"                               and t.enterprise_code = '"+enterprisecode+"'\n" + 
			"                               )\n" + 
			"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
			"   and r.is_use = 'Y'\n" + 
			" order by r.run_logno desc, r.attribute_code ";
			Object objcount=bll.getSingal(sql1);
			if(objcount != null)
			{
				count=Long.parseLong(objcount.toString());
			}
			String sql2="select r.shift_equstatus_id,\n" +
				"       r.run_logid,\n" + 
				"       r.run_logno,\n" + 
				"       r.speciality_code,\n" + 
				"       r.attribute_code,\n" + 
				"       r.equ_name,\n" + 
				"       r.equ_status_id,\n" + 
				"       r.equ_status_name,\n" + 
				"       (select m.status_name\n" + 
				"          from run_c_equstatus m\n" + 
				"         where m.equstatus_id = r.equ_status_id\n" + 
				"           and m.is_use = 'Y'\n" + 
				"           and m.enterprise_code = '"+enterprisecode+"') status_name,\n" + 
				"       (select m.color_value\n" + 
				"          from run_c_equstatus m\n" + 
				"         where m.equstatus_id = r.equ_status_id\n" + 
				"           and m.is_use = 'Y'\n" + 
				"           and m.enterprise_code = '"+enterprisecode+"') color_value,\n" + 
				"       getspecialname(r.speciality_code) speciality_name\n" + 
				"  from run_j_shift_equstatus r\n" + 
				" where r.speciality_code = '"+specialcode+"'\n" + 
				"   and to_date(substr(r.run_logno, 0, 4) || '-' ||\n" + 
				"               substr(r.run_logno, 5, 2) || '-' ||\n" + 
				"               substr(r.run_logno, 7, 2),\n" + 
				"               'yyyy-mm-dd') between to_date('"+fromdate+"', 'yyyy-mm-dd') and\n" + 
				"       to_date('"+todate+"', 'yyyy-mm-dd')\n" + 
				"   and r.attribute_code in (select t.attribute_code\n" + 
				"                              from run_c_shift_equ t\n" + 
				"                             where t.run_key_id = "+runkeyid+"\n" + 
				"                               and t.speciality_code = '"+specialcode+"'\n" + 
				"                               and t.enterprise_code = '"+enterprisecode+"'\n" + 
				"                               )\n" + 
				"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
				"   and r.is_use = 'Y'\n" + 
				" order by r.run_logno desc, r.attribute_code ";
			List list= bll.queryByNativeSQL(sql2, rowStartIdxAndCount);
			List<RunJShiftEqustatusModel> arrayList=new ArrayList();
			Iterator it=list.iterator();
			while(it.hasNext())
			{
				Object[] data=(Object[])it.next();
				RunJShiftEqustatusModel model=new RunJShiftEqustatusModel();
				if(data[0]!=null)
				{
					model.setShiftEqustatusId(Long.parseLong(data[0].toString()));
				}
				if(data[1]!=null)
				{
					model.setRunLogid(Long.parseLong(data[1].toString()));
				}
				if(data[2]!=null)
				{
					model.setRunLogno(data[2].toString());
				}
				if(data[3]!=null)
				{
					model.setSpecialityCode(data[3].toString());
				}
				if(data[4]!=null)
				{
					model.setAttributeCode(data[4].toString());
				}
				if(data[5]!=null)
				{
					model.setEquName(data[5].toString());
				}
				if(data[6]!=null)
				{
					model.setEquStatusId(Long.parseLong(data[6].toString()));
				}
				if(data[8]!=null)
				{
					model.setEquStatusName(data[8].toString());
				}
				if(data[9]!=null)
				{
					model.setColorValue(data[9].toString());
				}
				if(data[10]!=null)
				{
					model.setSpecialityName(data[10].toString());
				}
				arrayList.add(model);
			}
			PageObject pageobj=new PageObject();
			pageobj.setList(arrayList);
			pageobj.setTotalCount(count);
			return pageobj;
		}
		
}