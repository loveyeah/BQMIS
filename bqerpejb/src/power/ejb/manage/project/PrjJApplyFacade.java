package power.ejb.manage.project;

import java.util.List;
import java.util.logging.Level;
import power.ear.comm.ejb.PageObject;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;


/**
 * Facade for entity PrjJApply.
 * 
 * @see power.ejb.manage.project.PrjJApply
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PrjJApplyFacade implements PrjJApplyFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	private WorkflowService service;
	
	public PrjJApplyFacade(){
		service = new WorkflowServiceImpl();
	}
	/**
	 * 保存开工申请单信息
	 * @param entity
	 * @return PrjJApply 
	 * @throws RuntimeException
	 */
	public PrjJApply save(PrjJApply entity) {
		LogUtil.log("saving PrjJApply instance", Level.INFO, null);
		try {
			entity.setApplyId(bll.getMaxId("PRJ_J_APPLY", "APPLY_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除开工申请单信息
	 * 通过申请单id字符串
	 * @param ids 
	 * @throws RuntimeException
	 */
	public void delete(String ids) {
		LogUtil.log("deleting PrjJApply instance", Level.INFO, null);
		try {
			String sql="update PRJ_J_APPLY pja\n" +
			" set pja.Is_Use='N' where pja.apply_id in ("+ids+")";
			bll.exeNativeSQL(sql);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 修改开工申请单信息
	 * @param entity
	 * @return PrjJApply 
	 * @throws RuntimeException
	 */
	public PrjJApply update(PrjJApply entity) {
		LogUtil.log("updating PrjJApply instance", Level.INFO, null);
		try {
			PrjJApply result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	

	/**
	 *通过项目名称查找开工申请单信息
	 * 
	 * @param propertyName 项目名称
	 * @param enterpriseCode 企业编码
	 * @param status 状态
	 * @param rowStartIdxAndCount
	 * @return List<PrjJApply> found by query
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByPropertyAndStatus(String propertyName,String status, String enterpriseCode,String entryByCode, final int... rowStartIdxAndCount) {
		try {
			return findAll(propertyName, status, enterpriseCode, null,null,null,entryByCode, rowStartIdxAndCount);
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 查找开工申请单信息
	 * 通过开工申请单Id
	 * @param id
	 * @return PrjJApply
	 */
	@SuppressWarnings("unchecked")
	public PrjJApply findPrjJApplyById(Long id){
		try {
			PrjJApply pja = entityManager.find(
					PrjJApply.class, id);
			return pja;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	/**
	 * 上报
	 * @param workticketNo
	 * @param workerCode
	 * @param actionId
	 */
	public void reportTo(String busitNo,String flowCode,String workerCode,Long actionId)
	{
		WorkflowService service = new WorkflowServiceImpl();
		long entryId = service.doInitialize(flowCode,workerCode,busitNo);
		service.doAction(entryId, workerCode, actionId, "上报", null); 
		PrjJApply pja=findPrjJApplyById(Long.parseLong(busitNo));
		pja.setStatusId(1L);
		pja.setWorkFlowNo(entryId);
		update(pja);
	}
	/**
	 *通过项目名称查找开工申请单信息
	 * 
	 * @param propertyName 项目名称
	 * @param enterpriseCode 企业编码
	 * @param status 状态
	 * @param workFlowNos 工作流实例号
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String propertyName,String status, String enterpriseCode,String workFlowNos,Long deptId,
			String flag,String entryByCode,int... rowStartIdxAndCount){
		try {
			PageObject pg = new PageObject();
			String projectName="%"+propertyName+"%";
			String queryString = "select pjr.apply_id,pjr.contract_name,pjr.con_id,pjr.start_date," +
					"pjr.end_date,pjr.contractor_name,pjr.contractor_id,pjr.charge_by,pjr.test_result," +
					"pjr.authorize_item,pjr.person_register,pjr.article_register,pjr.id_card," +
					"pjr.operate_card,pjr.caution_money,pjr.hand_in_card,getworkername(pjr.entry_by) as enter_by," +
					"pjr.entry_date,pjr.work_flow_no,pjr.status_id,pjr.entry_by,prj_id,(select s.prj_name from PRJ_J_REGISTER s where s.prj_id = pjr.prj_id) as prj_name" +
					" from PRJ_J_APPLY pjr" +
					" where pjr.is_use='Y'" +
					" and pjr.enterprise_code='"+enterpriseCode+"'";
			if (status!=null&&!"".equals(status)) {
				queryString+=" and pjr.status_id in ("+status+")";
			} 
			
			if (flag != null && flag.equals("approve")) {
				if (workFlowNos != null && !"".equals(workFlowNos)) {
					queryString += "  and pjr.work_flow_no in (" + workFlowNos + ")";
				} else {
					queryString += "  and pjr.work_flow_no in ('')";
				}
				
				//add by drdu 20100824 控制status_id状态=1时，按照一级部门过滤
				queryString += "and getfirstlevelbyid((select t.dept_id from hr_j_emp_info t where t.emp_code=pjr.entry_by)) = decode(pjr.status_id,'1',getfirstlevelbyid("+deptId+"),getfirstlevelbyid((select t.dept_id from hr_j_emp_info t where t.emp_code=pjr.entry_by)))";
				
			}
			else if(flag==null)
			{
				queryString+=" and pjr.entry_by='"+entryByCode+"'";
			}

			
			queryString+=" and pjr.CONTRACT_NAME like '"+projectName+"'";
			String sqlCount = "select count(1) from (" + queryString + ") \n";
			List list=bll.queryByNativeSQL(queryString,rowStartIdxAndCount);
			pg.setList(list);
			pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
			return pg;
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public String getApplyList(String workCode,Long deptId)
	{
		String msg="";
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(new String[] {
				"prjApply"}, workCode);
		if(entryIds==null||entryIds.equals("")) return msg;
		else
		{
			String sql = "select count(*)\n" +
				"  from PRJ_J_APPLY pjr\n" + 
				" where pjr.is_use = 'Y'\n" + 
				"   and pjr.enterprise_code = 'hfdc'\n" + 
				"   and pjr.work_flow_no in ("+entryIds+")\n" + 
				"   and getfirstlevelbyid((select t.dept_id\n" + 
				"                           from hr_j_emp_info t\n" + 
				"                          where t.emp_code = pjr.entry_by)) =\n" + 
				"       decode(pjr.status_id,\n" + 
				"              '1',\n" + 
				"              getfirstlevelbyid("+deptId+"),\n" + 
				"              getfirstlevelbyid((select t.dept_id\n" + 
				"                                  from hr_j_emp_info t\n" + 
				"                                 where t.emp_code = pjr.entry_by)))\n";
//			
//			if(workCode != null && !workCode.equals("999999"))
//			{
//				sql += "   and pjr.entry_by = '"+workCode+"'";
//			}

			 Long count = Long.parseLong(bll.getSingal(sql).toString());
			if (count > 0) {
				msg = "<a target=\"_blank\"  href=\"manage/project/bussiness/prjNewApply/PrjJApplyApproval.jsp\">"
						+ count + "张工程项目开工申请单等待审批</a><br/><br/>";
			}
			return msg;
		}
	}
	/**
	 * 
	 * 工程开工申请单审批
	 * @param entryId
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param identify
	 * @param applyId
	 * @param entryByCode
	 * @return boolean
	 */
	public boolean prjNewApplyApproval(String entryId,String workerCode,String actionId,String approveText,String nextRoles,String identify,String applyId,String entryByCode){
		PrjJApplyApprove pjaa=new PrjJApplyApprove();	
		PrjJApply pja=new PrjJApply();
				pja =findPrjJApplyById(Long.parseLong(applyId));
				pjaa.setApplyId(pja.getApplyId());
				pjaa.setApproveBy(workerCode);
				pjaa.setApproveDate(new java.util.Date());
				pjaa.setApproveText(approveText);
				pjaa.setEnterpriseCode(entryByCode);
				pjaa.setIsUse("Y");
				if ("TH".equals(identify)) {
					pja.setStatusId(0L);
					pjaa.setStatusId(0L);
				}
				//add by sychen 20100910
				else if(pja.getStatusId()==1){
					pja.setStatusId(2L);
					pjaa.setStatusId(2L);
				}
				else if(pja.getStatusId()==2){
					pja.setStatusId(3L);
					pjaa.setStatusId(3L);
				}
				else if(pja.getStatusId()==3){
					if ("XYZHGSSP".equals(identify)) {
						pja.setStatusId(4L);
						pjaa.setStatusId(4L);
					}
					else if ("BXYZHGSSP".equals(identify)) {
						pja.setStatusId(5L);
						pjaa.setStatusId(5L);
					}
				}
				else if(pja.getStatusId()==4){
					pja.setStatusId(5L);
					pjaa.setStatusId(5L);
				}
				//add by sychen 20100910 end 
//				else if ("TY".equals(identify)) {
//					pja.setStatusId(pja.getStatusId()+1);
//					pjaa.setStatusId(pja.getStatusId());
//				}
			else{
					return false;
				}
				update(pja);
				prjAproveSave(pjaa);
				service.doAction(Long.parseLong(entryId), workerCode, Long.parseLong(actionId), approveText, null,
						nextRoles, "");
				return true;
	}
	/**
	 * 保存开工申请审批信息
	 * 
	 * @param entity
	 * @throws RuntimeException
	 */
	public void prjAproveSave(PrjJApplyApprove entity) {
		LogUtil.log("saving PrjJApplyApprove instance", Level.INFO, null);
		try {
			Long id=bll.getMaxId("PRJ_J_APPLY_APPROVE", "APPROVE_ID");
			entity.setApproveId(id);
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
}