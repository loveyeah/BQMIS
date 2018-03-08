package power.ejb.manage.project;

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
 * Facade for entity PrjJStartReport.
 * 
 * @see power.ejb.manage.project.PrjJStartReport
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PrjJStartReportFacade implements PrjJStartReportFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved PrjJStartReport entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PrjJStartReport entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public PrjJStartReport save(PrjJStartReport entity) {
		LogUtil.log("saving PrjJStartReport instance", Level.INFO, null);
		try {
			entity.setReportId(bll.getMaxId("PRJ_J_START_REPORT", "REPORT_ID"));
			entityManager.persist(entity);
			String sql = "update PRJ_J_REGISTER t set t.status_id = 1 where t.prj_id = "+entity.getPrjId();
			bll.exeNativeSQL(sql);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PrjJStartReport entity.
	 * 
	 * @param entity
	 *            PrjJStartReport entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(String ids) {
		String sql = "update PRJ_J_START_REPORT set is_use = 'N' where report_id in ("+ids+")";
		bll.exeNativeSQL(sql);
	}

	/**
	 * Persist a previously saved PrjJStartReport entity and return it or a copy
	 * of it to the sender. A copy of the PrjJStartReport entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PrjJStartReport entity to update
	 * @return PrjJStartReport the persisted PrjJStartReport entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PrjJStartReport update(PrjJStartReport entity) {
		LogUtil.log("updating PrjJStartReport instance", Level.INFO, null);
		try {
			PrjJStartReport result = entityManager.merge(entity);
			String sql = "update PRJ_J_REGISTER t set t.status_id = 1 where t.prj_id = "+entity.getPrjId();
			bll.exeNativeSQL(sql);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 功能：查询开工报告或竣工报告列表
	 * add by qxjiao 20100816
	 */
	public PageObject findProjectList(String workerCode,String prjSort, String start_date,
			String end_date,String enterprise_code,String reportType,String workCode, int... rowStartIdxAndCount) {
		String sql = "SELECT t.report_id, " +
						  "	t. conttrees_no,"+
					      " t. contract_name,"+
					      " t.prj_funds,"+
					      " t2.prj_type_name,"+
					      " t.start_date,"+
					      " t.end_date, " +
					      "	t.report_code," +
					      "	t.prj_location," +
					      "	t.entry_by," +
					      "	t.entry_date," +
					      "	GETWORKERNAME(t.entry_by)," +
					      " GETWORKERNAME(t.approve_charge_by)," +
					      "	t.approve_text," +
					      " t.approve_date," +
					      " t.work_charge_by," +
					      " t.work_operate_by," +
					      " t.work_approve_date," +
					      " t.back_entry_by," +
					      " t.prj_id," +
					      " (select a.prj_name from PRJ_J_REGISTER a where a.prj_id = t.prj_id) as prj_name,"+
					      " t.approve_charge_by" +
					 " FROM PRJ_J_START_REPORT t,"+
					      " PRJ_C_TYPE         t2"+
					" WHERE t.prj_type_id = t2.prj_type_id " +
					" and t.is_use = 'Y' " +
					" and t.report_type = '"+reportType+"' " +
					" and t.enterprise_code = '"+enterprise_code+"' " 
					;
		String sqlCount = "SELECT count(*) "+
						 " FROM PRJ_J_START_REPORT t,"+
						      " PRJ_C_TYPE         t2"+
						" WHERE t.prj_type_id = t2.prj_type_id" +
						" and t.is_use = 'Y' " +
						" and t.report_type = '"+reportType+"' " +
						" and t.enterprise_code = '"+enterprise_code+"' " 
						;
		if(start_date!=null&&!"".equals(start_date)){
			sql += " and to_char(t.start_date,'yyyy-MM-dd')>'"+start_date+"' ";
			sqlCount += " and to_char(t.start_date,'yyyy-MM-dd')>'"+start_date+"' ";
		}
		if(end_date!=null&&!"".equals(end_date)){
			sql += " and to_char(t.end_date,'yyyy-MM-dd')<'"+end_date+"' ";
			sqlCount += " and to_char(t.end_date,'yyyy-MM-dd')<'"+end_date+"' ";
		}
		if(prjSort!=null&&!"".equals(prjSort)){
			sql +=" and t.prj_type_id = '"+prjSort+"'";
			sqlCount +=" and t.prj_type_id = '"+prjSort+"'";
		}
		if(workCode != null && !workCode.equals(""))
		{
			sql += " and t.entry_by = '"+workCode+"'";
			sqlCount += " and t.entry_by = '"+workCode+"'";
		}
		//add by ypan 20100915
		if (workerCode != null && !workerCode.equals("")
				&& !workerCode.equals("999999"))
			sql += " and t.entry_by='" + workerCode + "' \n";
		sqlCount += " and t.entry_by = '"+workCode+"'";
		sql += " order by t. conttrees_no asc ";
		PageObject obj = new PageObject();
		List result = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long count  = Long.parseLong(bll.getSingal(sqlCount).toString());
		obj.setList(result);
		obj.setTotalCount(count);
		return obj;
	}


	/**
	 * 功能：生成开工报告或竣工工程流水号
	 * add by qxjiao 20100816
	 */
	public String findProjectNo(String dept, String date,String reporttype) {
		int num=1;
		String deptNameSql = "select GETDEPTNAME(GETFirstLevelBYID("+dept+")) FROM dual ";
		String deptName = bll.getSingal(deptNameSql).toString();
		String sql1 = String
				.format("select fun_spellcode('%s') from dual", deptName);
		String deptname = bll.getSingal(sql1).toString();
		String keyword = "Q/CDT-"+deptname+"-"+date;
		String sql="select max(REPORT_CODE) from" +
		" PRJ_J_START_REPORT" +
		" where report_type='"+reporttype+"'" +
		"and REPORT_CODE like '%"+keyword+"%' " +
				"and is_use='Y'";
		List list=bll.queryByNativeSQL(sql);
		if (list!=null&&list.size()>0&&list.get(0)!=null) {
			String applyCode=list.get(0).toString();
			applyCode=applyCode.substring(applyCode.lastIndexOf("-")+1, applyCode.length());
			num=Integer.parseInt(applyCode)+1;
		}
		//System.out.println("编号序号为："+num);
		if (num<10) {
			keyword+="-00"+num;
		} else if(num<100){
			keyword+="-0"+num;
		}else{
			keyword+="-"+num;
		}
		return keyword;
	}
	/**
	 * 功能：根据id查询开工报告或竣工报告
	 * add by qxjiao 20100816
	 */
	public PrjJStartReport findReportById(Long id){
		PrjJStartReport report =  entityManager.find(PrjJStartReport.class, id);
		return report;
	}
	/**
	 * 功能：根据合同编号查询竣工报告
	 * add by qxjiao 20100816
	 */
	public PrjJStartReport findByCon_no(String con_no) {
		String sql = "select * from PRJ_J_START_REPORT where conttrees_no = '"+con_no+"'";
		List<PrjJStartReport> result = bll.queryByNativeSQL(sql, PrjJStartReport.class);
		if(result.size()>0){
			return result.get(0);
		}else{
			return null;
		}
	}

}