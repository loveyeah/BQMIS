package power.ejb.manage.budget;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.project.PrjJApply;
import power.ejb.manage.project.PrjJCheck;

/**
 * Facade for entity CbmJCostReport.
 * 
 * @see power.ejb.manage.budget.CbmJCostReport
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmJCostReportFacade implements CbmJCostReportFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	private WorkflowService service;
	public CbmJCostReportFacade(){
		service = new WorkflowServiceImpl();
	}

	/**
	 * 增加费用报销单信息
	 * @param entity
	 * @return CbmJCostReport
	 */
	public CbmJCostReport save(CbmJCostReport entity) {
			Long reportId=bll.getMaxId("CBM_J_COST_REPORT", "report_id");
			entity.setReportId(reportId);
			entity.setIsUse("Y");
			entity.setWorkFlowStatus("0");
			entityManager.persist(entity);
			return entity;
		
	}

	/**
	 * 删除费用报销单信息
	 * @param ids
	 */
	public void delete(String ids) {
			String sql="update CBM_J_COST_REPORT set is_use='N' where report_id in ("+ids+")";
			bll.exeNativeSQL(sql);
	}
	/**
	 * 保存费用报销单修改
	 * @param entity
	 * @return CbmJCostReport
	 */
	public CbmJCostReport update(CbmJCostReport entity) {
		LogUtil.log("updating CbmJCostReport instance", Level.INFO, null);
		try {
			CbmJCostReport result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 按照费用报销单Id查询
	 * @param id
	 * @return CbmJCostReport
	 */
	public CbmJCostReport findById(Long id) {
		LogUtil.log("finding CbmJCostReport instance with id: " + id,
				Level.INFO, null);
		try {
			CbmJCostReport instance = entityManager.find(CbmJCostReport.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 费用报销单查询
	 * @param flag 是否审批 1 审批  2费用报销单查询
	 * @param startDate	开始时间
	 * @param endDate 结束时间
	 * @param reportorName 报销人名称模糊
	 * @param reportUse 用途模糊
	 * @param status 工作流状态
	 * @param deptId 当前登录人部门Id
	 * @param entryIds 工作流实例号
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String flag,String startDate,String endDate,String reportorName,String reportUse,String status,long deptId,String entryIds,String enterpriseCode,int... rowStartIdxAndCount){
		PageObject pg=new PageObject();
		try {
			String queryString = "select t.report_id, \n"+
								 "      t.report_money_upper, \n"+
								 "      t.report_money_lower, \n"+
								 "      t.report_use, \n"+
								 "      t.memo, \n"+
								 "      getworkername(t.report_by) reportBy, \n"+
								 "      t.report_date, \n"+
								 "      t.work_flow_status,\n"+
								 "      t.work_flow_no, \n"+
								 "      GETDEPTNAME(t.report_dept) reportDept, \n"+
								 "      GETCBMCITEMNAME(t.item_id), \n" +
								 "      t.item_id \n" +
								 " from CBM_J_COST_REPORT t \n"+
								 " where t.is_use='Y' \n" +
								 " and t.enterprise_code='"+enterpriseCode+"' \n" ;
			if (status!=null&&!status.equals("")) {
				queryString += "  and t.work_flow_status in (" + status + ") \n";
			}
			if (startDate!=null&&!startDate.equals("")&&endDate!=null&&!endDate.equals("")) {
				queryString += "  and t.report_date > to_date('"+startDate+"','yyyy-mm-dd') \n" +
						"and t.report_date < to_date('"+endDate+"','yyyy-mm-dd') \n";
			}
			if ("1".equals(flag)) {//审批查询
				if (entryIds != null && !"".equals(entryIds)) {
					queryString += "  and t.work_flow_no in (" + entryIds + ") \n";
				} else {
					queryString += "  and t.work_flow_no in ('') \n";
				}
			}else if("2".equals(flag)){//费用报销单查询查询
				if(reportorName!=null&&!reportorName.equals("")){
					queryString += "  and getworkername(t.report_by) like '%"+reportorName+"%' \n";
				}
				if(reportUse!=null&&!reportUse.equals("")){
					queryString += "  and t.report_use like '%"+reportUse+"%' \n";
				}
			}
			String totalCount="select count(*) from ("+queryString+")";
			queryString+=" order by t.report_id \n";
			List list=bll.queryByNativeSQL(queryString, rowStartIdxAndCount);
			long count=Long.parseLong(bll.getSingal(totalCount).toString());
			pg.setList(list);
			pg.setTotalCount(count);
			return pg;
		} catch (Exception re) {
			return null;
		}
	}
	/**
	 * 费用报销单审批
	 * @param applyId 费用报销单Id
	 * @param entryId 
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param eventIdentify
	 */
	@SuppressWarnings("unchecked")
	public void expRApprove(String applyId, String entryId, String workerCode,
			String actionId, String approveText, String nextRoles,
			String eventIdentify) {
			CbmJCostReport model=findById(Long.parseLong(applyId));
			if ("TH".equals(eventIdentify)) {
				model.setWorkFlowStatus("10");
			}else{
				if ("45".equals(actionId)) {
					model.setWorkFlowStatus("2");
				}
				if ("47".equals(actionId)) {
					model.setWorkFlowStatus("3");
				}
				if ("46".equals(actionId)) {
					model.setWorkFlowStatus("4");
				}
				if ("78".equals(actionId)) {
					model.setWorkFlowStatus("5");
				}
				if ("59".equals(actionId)) {
					model.setWorkFlowStatus("6");
				}
				if ("89".equals(actionId)) {
					model.setWorkFlowStatus("7");
				}
				if ("69".equals(actionId)) {
					model.setWorkFlowStatus("8");
				}
				if ("93".equals(actionId)) {
					model.setWorkFlowStatus("9");
				}
			}
			update(model);
			service.doAction(Long.parseLong(entryId), workerCode, Long.parseLong(actionId), approveText, null,
					nextRoles, "");
	}
	/**
	 * 费用报销单上报
	 * @param checkId
	 * @param workercode
	 * @param actionId
	 * @param approveText
	 * @param workFlowCode
	 */
	public void endCheckReportTo(String checkId, String workercode,String actionId, String approveText,String workFlowCode){
		WorkflowService service = new WorkflowServiceImpl();
		long entryId = service.doInitialize(workFlowCode,workercode,checkId);
		service.doAction(entryId, workercode,Long.parseLong(actionId),approveText, null); 
		CbmJCostReport model=findById(Long.parseLong(checkId));
		model.setWorkFlowStatus("1");
		model.setWorkFlowNo(entryId);
		update(model);
	}
	@SuppressWarnings("unchecked")
	public PageObject getCostReportList(String fuzzy, String enterpriseCode, String workerCode, int... rowStartIdxAndCount)
	{ 
		//return findAll("2","","",fuzzy,"","0,10",0L,"",enterpriseCode,rowStartIdxAndCount);
		
	//String sql="select * from cbm_j_cost_report";
    String sql = "SELECT t.report_id,\n" +
	"  t.report_by,\n" + 
	"  t.report_money_lower,\n" + 
	"  t.report_use,\n" + 
	"  t.memo,\n" + 
	"  getworkername(t.report_by),\n" + 
	"  GETDEPTNAME(t.report_dept),\n" + 
	 " GETCBMCITEMNAME(t.item_id), \n" +
	 
	"  t.report_money_upper,\n" + 
	 "      t.work_flow_status,t.item_id\n"+
	"  FROM  cbm_j_cost_report  t\n"+ 
	" WHERE t.is" +
	"_use = 'Y'\n" + 
	"  AND getworkername(t.report_by) like '%"+fuzzy+"%'\n" + 
	"   AND t.enterprise_code = '"+enterpriseCode+"'\n" +
	"   AND t.report_by = '"+workerCode+"'\n" + 
	"   AND  t.work_flow_status in (0,10)" + 

	" ORDER BY t.report_id"; 
    String sqlCount="select count(1) from ("+sql+")";
    System.out.println("sqlC="+sqlCount);

 List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		System.out.println("list="+list);
		PageObject object = new PageObject();
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		//object.setList(arraylist);
		object.setList(list);
		object.setTotalCount(totalCount);
		return object;

	}

}