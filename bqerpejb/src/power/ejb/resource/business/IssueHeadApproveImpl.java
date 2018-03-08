package power.ejb.resource.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.basedata.BaseDataManager;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.resource.InvJIssueDetails;
import power.ejb.resource.InvJIssueDetailsFacadeRemote;
import power.ejb.resource.InvJIssueHead;
import power.ejb.resource.InvJIssueHeadFacadeRemote;
import power.ejb.resource.MrpJPlanRequirementHead;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

@Stateless
public class IssueHeadApproveImpl implements IssueHeadApprove {
	@SuppressWarnings("unused")
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	WorkflowService service;
	private InvJIssueHeadFacadeRemote headRemote;
	private IssueHeadRegister remote;
	private BaseDataManager base;
	/** 领料单明细remote */
	private InvJIssueDetailsFacadeRemote detailsRemote;

	public IssueHeadApproveImpl() {
		service = new WorkflowServiceImpl();
		headRemote = (InvJIssueHeadFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("InvJIssueHeadFacade");
		remote = (IssueHeadRegister) Ejb3Factory.getInstance().getFacadeRemote(
				"IssueHeadRegisterImpl");
		base = (BaseDataManager) Ejb3Factory.getInstance().getFacadeRemote(
				"BaseDataManagerImpl");
		detailsRemote = (InvJIssueDetailsFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("InvJIssueDetailsFacade");
	}

	public void IssueHeadReport(Long issueHeadId, Long actionId,
			String approveText, String workerCode) {
		String workflowType = "";
		InvJIssueHead head = headRemote.findById(issueHeadId);
		//modify by fyyang 090618
		if(head.getPlanOriginalId()==1||head.getPlanOriginalId()==2)
		{
			//固定资产领用与专项费用领用走固定资产流程
			workflowType = "hfResourceGetGDZC";
		}
		else if(head.getPlanOriginalId()==4||head.getPlanOriginalId()==5||head.getPlanOriginalId()==12||head.getPlanOriginalId()==13)//modify ywliu 20091023
		{
			//生产类
			workflowType = "hfResourceGetSC";
		}
//		else if(head.getPlanOriginalId()==5)
//		{
//			//办公及卫生用品类---行政类流程
//			workflowType = "hfResourceGetXZ";
//		}
		else
		{
			workflowType = "hfResourceGetSC";
		}
//		if (head.getPlanOriginalId()==4||head.getPlanOriginalId()==10)// 新厂生产类
//		{
//			workflowType = "hfResourceGetSC";
//		} else if (head.getPlanOriginalId()==5||head.getPlanOriginalId()==11)// 老厂生产
//		{
//			workflowType = "hfResourceGetSC";
//		} else if (head.getPlanOriginalId()==3)// 固定资产类
//		{
//			workflowType = "hfResourceGetGDZC";
//		}  else if (head.getPlanOriginalId()==6||head.getPlanOriginalId()==7)// 行政类
//		{
//			workflowType = "hfResourceGetXZ";
//		} else {
//			workflowType = "hfResourceGetSC";
//		}
//		if (head.getMrNo() == null || head.getMrNo() == "")// 不是由计划申请单生成，将核准数量默认为申请数量
//		{
//			// MrpJPlanRequirementHead
//			// plan=remote.findPlanRequirementHeadModel(head.getMrNo(),
//			// head.getEnterpriseCode());
//			List<InvJIssueDetails> list = detailsRemote
//					.findByIssueHeadId(issueHeadId);
//			for (int i = 0; i < list.size(); i++) {
//				InvJIssueDetails detail = list.get(i);
//				if(detail.getIsUse().equals("Y"))
//				{
//				detail.setApprovedCount(detail.getAppliedCount());
//				detailsRemote.update(detail);
//				}
//			}
//		}
		Long entryId = -1l;
		if (head.getWorkFlowNo() == null) {
			entryId = service.doInitialize(workflowType, workerCode, null);
			head.setWorkFlowNo(entryId);
			head.setIssueStatus("1");// 状态修改为审批中
		} else {
			entryId = head.getWorkFlowNo();
			head.setIssueStatus("1");// 状态修改为审批中
		}
		//add by drdu 090730
		head.setDueDate(new Date()); //申请领用日期
		
		headRemote.update(head);
		service.doAction(entryId, workerCode, actionId, approveText, null, "",
				"");
	}
	
	/**   add by fyyang 090505 */
	private String getBusiStatus(String busiStatus,String eventIdentify,String planType)
	{
		if(eventIdentify.equals("TH")) return "9";
		else 
		{
			if(busiStatus.equals("1")) return "3"; //本部门领导已审批
			else if(busiStatus.equals("3"))
			{
				if(planType.equals("FD")) return "7"; //发电综合部领导已审批
				else if(planType.equals("SY")) return "8"; //实业综合部领导已审批
				else if(planType.equals("JX")) return "A"; //检修综合部领导已审批
				//else if(planType.equals("LC"))    return "2";//return "5"; //实业安生部领导已审批 //老厂 //fyyang 090515 流程变更修改
				else if(planType.equals("XC"))  return "2";//return "4"; //检修安生部领导已审批 新厂
				else if(planType.equals("GD")) return "6";
				else return "";
			}
		//	else if(busiStatus.equals("4")) return "6";//发电安生部领导已审批
			else if(busiStatus.equals("6")) return "2";//已审批结束
		//	else if(busiStatus.equals("5")) return "6";//发电安生部领导已审批
			else if(busiStatus.equals("7")||busiStatus.equals("8")||busiStatus.equals("A")) return "2";//已审批结束
			else return "";
		}
	   
	}

//	public void IssueHeadApprove(Long issueHeadId, Long actionId,
//			String approveText, String workerCode, String nrs,
//			String eventIdentify, List<Map> updateDetails) {
	@SuppressWarnings("unchecked")
	public void IssueHeadApprove(Long issueHeadId, Long actionId,
			String approveText, String workerCode, String nrs,
			String eventIdentify,String itemCode) {
		InvJIssueHead head = headRemote.findById(issueHeadId);
		String planType="";
		
		
//		if ("TYEND".equals(eventIdentify))// 审批最后一步
//		{
//			head.setIssueStatus("2");
//			headRemote.update(head);
//		}
//		List<InvJIssueDetails> details = new ArrayList<InvJIssueDetails>();
//		InvJIssueDetails detail = null;
//		if (updateDetails != null) {
//			for (Map map : updateDetails) {
//				detail = detailsRemote.findById(Long.parseLong(map.get(
//						"issueDetailsId").toString()));
//				detail.setApprovedCount(Double.parseDouble(map.get(
//						"approvedCount").toString()));
//				detailsRemote.update(detail);// 修改明细核准数量
//			}
//		}
		Map m = new java.util.HashMap();
		// MrpJPlanRequirementHead
		// plan=remote.findPlanRequirementHeadModel(head.getMrNo(),
		// head.getEnterpriseCode());
		//modify by fyyang 090618
		if (head.getPlanOriginalId()==4||head.getPlanOriginalId()==5)// 生产类
		{
			m.put("isNew", true);
			planType="XC";
		} else if (head.getPlanOriginalId()==1||head.getPlanOriginalId()==2)// 固定资产类
		{
			m.put("isNew", true);
			planType="GD";
		} 
//		else if (head.getPlanOriginalId()==5)// 行政类
//		{
//			String deptType = base.checkDeptType(head.getFeeByDep());
//			if ("FD".equals(deptType)) {
//				m.put("isFD", true);
//				m.put("isJX", false);
//				m.put("isSY", false);
//				planType="FD";
//			} else if ("JX".equals(deptType)) {
//				m.put("isFD", false);
//				m.put("isJX", true);
//				m.put("isSY", false);
//				planType="JX";
//			} else {
//				m.put("isFD", false);
//				m.put("isJX", false);
//				m.put("isSY", true);
//				planType="SY";
//			}
//		} 
		else {
			m.put("isNew", true);
			planType="XC";
		}
		if(head.getIssueStatus().equals("1"))
		{
			List<InvJIssueDetails> list = detailsRemote
			.findByIssueHeadId(issueHeadId);
	      for (int i = 0; i < list.size(); i++) {
		      InvJIssueDetails detail = list.get(i);
		      if(detail.getIsUse().equals("Y"))
		     {
			if(detail.getApprovedCount()==0)
			{
		      detail.setApprovedCount(detail.getAppliedCount());
		      detailsRemote.update(detail);
			}
		
		    }
	        }
		}
		head.setItemCode(itemCode);
		head.setIssueStatus(this.getBusiStatus(head.getIssueStatus(), eventIdentify,planType));
		headRemote.update(head);
		service.doAction(head.getWorkFlowNo(), workerCode, actionId,
				approveText, m, nrs);
	}

}
