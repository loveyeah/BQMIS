package power.ejb.manage.stat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity ZbJReportLoad.
 * 
 * @see power.ejb.manage.stat.ZbJReportLoad
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ZbJReportLoadFacade implements ZbJReportLoadFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	public void  deleteReportLoad(String ids,String deleteCode)//add by wpzhu 20100605
	{
		Date deleteDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql=
			"update  zb_j_report_load j " +
			" set j.is_use='N',\n " +
			" j.delete_by = '"+deleteCode+"'," +
			" j.delete_date = to_date('"+sdf.format(deleteDate)+"','yyyy-mm-dd hh24:mi:ss') " +
			"where j.load_id  in ("+ids+")";
		bll.exeNativeSQL(sql);
	}
	public void save(ZbJReportLoad entity) throws CodeRepeatException {
		LogUtil.log("saving ZbJReportLoad instance", Level.INFO, null);
		try {
			if(this.isUnique(entity.getLoadName(), entity.getReportTime()) >0) {
				throw new CodeRepeatException("同一个报表每天只能上传一次，如果想重新上传，请先将原来的删除!");
			} else {
				entity.setLoadId(bll.getMaxId("ZB_J_REPORT_LOAD", "load_id"));
				entity.setIsUse("Y");
				entity.setLoadDate(new Date());
				entityManager.persist(entity);
			}
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(ZbJReportLoad entity) {
		LogUtil.log("deleting ZbJReportLoad instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(ZbJReportLoad.class, entity
					.getLoadId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ZbJReportLoad update(ZbJReportLoad entity) {
		LogUtil.log("updating ZbJReportLoad instance", Level.INFO, null);
		try {
			ZbJReportLoad result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ZbJReportLoad findById(Long id) {
		LogUtil.log("finding ZbJReportLoad instance with id: " + id,
				Level.INFO, null);
		try {
			ZbJReportLoad instance = entityManager
					.find(ZbJReportLoad.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ZbJReportLoad> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding ZbJReportLoad instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from ZbJReportLoad model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAllByReportCode(String startTime,String endTime,String loadName,String reportCode,String enterpriseCode,int... rowStartIdxAndCount) {
		LogUtil.log("finding all ZbJReportLoad instances", Level.INFO, null);
//		try {
//			final String queryString = "select model from ZbJReportLoad model";
//			Query query = entityManager.createQuery(queryString);
//			return query.getResultList();
//		} catch (RuntimeException re) {
//			LogUtil.log("find all failed", Level.SEVERE, re);
//			throw re;
//		}
		PageObject obj = new PageObject();
		String sqlWhere = "";
		if(loadName != null && !"".equals(loadName)) {
			sqlWhere += "and t.load_name like '%" + loadName + "%'";
		}
		if(reportCode != null && !"".equals(reportCode)) {
			sqlWhere += "and t.report_code = '" + reportCode + "'";
		}
		if(startTime != null && !"".equals(startTime) ) {//add by wpzhu 20100605  增加加载时间查询条件
			sqlWhere += "and  to_char(t.load_date ,'yyyy-MM-dd')>='" + startTime + "'";
		}
		if( endTime != null && !"".equals(endTime))
		{
			sqlWhere += "and  to_char(t.load_date ,'yyyy-MM-dd')<='" + endTime + "'";
		}
		sqlWhere+=" order by t.REPORT_TIME desc, t.load_date desc";
		String sqlCount = "select count(*) from zb_j_report_load t where t.is_use = 'Y'";
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount+sqlWhere).toString());
		List<ZbJReportLoad> loadList = new ArrayList<ZbJReportLoad>();
		if(totalCount >0) {
			String sql = "select t.load_id,t.load_code,t.load_name,t.report_code,t.report_time,t.annex_address,getworkername(t.load_by) load_by,t.load_date,t.first_dept_code,t.is_use,t.enterprise_code,t.delete_by,t.delete_date from zb_j_report_load t where t.is_use = 'Y'";
		
			loadList = bll.queryByNativeSQL(sql+sqlWhere,ZbJReportLoad.class, rowStartIdxAndCount);
		}
		obj.setList(loadList);
		obj.setTotalCount(totalCount);
		return obj;
	}
	
	public int isUnique(String loadName,String reportTime) {
		String isUniqueSql = "select count(*) from zb_j_report_load t where t.is_use = 'Y' and t.load_name = '"+loadName+"' and t.report_time = '"+reportTime+"'"; 
		int count = Integer.parseInt(bll.getSingal(isUniqueSql).toString());
		return count;
	}

}