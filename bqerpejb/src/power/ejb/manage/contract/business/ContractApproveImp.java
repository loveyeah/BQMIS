package power.ejb.manage.contract.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.opensymphony.engineassistant.po.WorkflowActivity;
import com.opensymphony.engineassistant.po.WorkflowEvent;
import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.contract.ConCStatus;
import power.ejb.manage.contract.ConCStatusFacadeRemote;
import power.ejb.manage.contract.form.ContractFullInfo;
import power.ejb.manage.contract.form.ConApproveBean;
import power.ejb.manage.plan.BpJPlanJobDepMain;

@Stateless
public class ContractApproveImp implements ContractApprove {
	@SuppressWarnings("unused")
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	WorkflowService service;
	protected ConJContractInfoFacadeRemote conremote;
	protected ConJModifyFacadeRemote modremote;
	protected ConJBalanceFacadeRemote balremote;
	protected ConJBalaApproveFacadeRemote bhisremote;
	protected ConCStatusFacadeRemote csremote;

	public ContractApproveImp() {
		conremote = (ConJContractInfoFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("ConJContractInfoFacade");
		modremote = (ConJModifyFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("ConJModifyFacade");
		balremote = (ConJBalanceFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("ConJBalanceFacade");
		bhisremote = (ConJBalaApproveFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("ConJBalaApproveFacade");
		csremote = (ConCStatusFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("ConCStatusFacade");
		service = new WorkflowServiceImpl();
	}

	// 采购合同上报
	public void contractReport(Long conId, String workercode,
			String approveText, String nextRoles) throws CodeRepeatException {
		ConJContractInfo model = conremote.findById(conId);
		String workflowType = "bqCGContract";
		// if (model.getActAmount() < 20000) {
		// Long actionId = 210l;
		// long entryId = service.doInitialize(workflowType, workercode, conId
		// .toString());
		// service.doAction(entryId, workercode, actionId, approveText, null,
		// nextRoles);
		// model.setWorkflowNo(entryId);
		// // model.setExecFlag(1L);
		// model.setWorkflowStatus(2L);
		// model.setSignStartDate(new Date());
		// model.setSignEndDate(new Date());
		// conremote.update(model, null, null);
		// } else {
		Long actionId = 221l;
		long entryId;
		if (model.getWorkflowNo() != null) {
			entryId = model.getWorkflowNo();
		} else {
			entryId = service.doInitialize(workflowType, workercode, conId
					.toString());
		}
		service.doAction(entryId, workercode, actionId, approveText, null,
				nextRoles);
		model.setWorkflowNo(entryId);
		model.setSignStartDate(new Date());
		model.setWorkflowStatus(1L);
		// 判断是否物资管理选择部门审批
		model.setJyjhAdvice("W");
		conremote.update(model, null, null);
		// }

	}

	// 采购合同审批
	@SuppressWarnings("unchecked")
	public void contractApprove(Long entryId, String workerCode, Long actionId,
			String approveText, String nextRoles, Long conid,
			String approveDepts, String stepId) throws CodeRepeatException {
		ConJContractInfo model = conremote.findById(conid);
		String conType = model.getConTypeId().toString();

		String appoingSteps = null;
		if (actionId == 2122l) {
			// jbxu 保存部门
			List<ConCStatus> list = csremote.findByProperty("workFlowNo", entryId);
			if(list.size() > 0){
				ConCStatus Cmodel = csremote.findById(list.get(0).getId());
				Cmodel.setDeptCodes(approveDepts);
				csremote.update(Cmodel);
			}else{
				ConCStatus entity = new ConCStatus();
				entity.setDeptCodes(approveDepts);
				entity.setWorkFlowNo(entryId);
				csremote.save(entity);
			}
			model.setJyjhAdvice(null);
			conremote.update(model, null, null);
			appoingSteps = approveDepts;
		}
		// boolean sumAbove2w = false;
		// boolean sumAbove10w = false;
		// boolean sumAbove20w = false;
		// if (model.getIsSum().equals("Y")) {
		// if (model.getActAmount() != null) {
		// double sum = model.getActAmount();
		// if (sum <= 100000 && sum > 20000) {
		// sumAbove2w = true;
		// sumAbove10w = false;
		// sumAbove20w = false;
		// } else if (sum <= 200000 && sum > 100000) {
		// sumAbove2w = false;
		// sumAbove10w = true;
		// sumAbove20w = false;
		// } else {
		// sumAbove2w = false;
		// sumAbove10w = false;
		// sumAbove20w = true;
		// }
		// }
		// }
		// Map m = new java.util.HashMap();
		// m.put("sumAbove2w", sumAbove2w);
		// m.put("sumAbove10w", sumAbove10w);
		// m.put("sumAbove20w", sumAbove20w);
		// service.doAction(entryId, workerCode, actionId, approveText, m,
		// nextRoles, "");
		// 当法律顾问审批时 自动处理指定长主管审批人
		if (actionId == 3738l) {
			service.doAction(entryId, workerCode, actionId, approveText, null,
					"", model.getProsy_by());
			// updateWfJRrs(entryId, model.getProsy_by());
		} else {
			service.doAction(entryId, workerCode, actionId, approveText, null,
					nextRoles, "");
		}
		String sql = "select t.DEPT_CODES from con_c_status t where t.WORK_FLOW_NO = "
				+ entryId + " ";
		if (actionId != 2122l) {
			appoingSteps = (bll.getSingal(sql) != null) ? bll.getSingal(sql)
					.toString() : "";
		}
		// 自动审批
		if (actionId.toString().endsWith("2") && actionId != 2122l && actionId != 3132l) {
		} else {
			autoDoAction(entryId, appoingSteps, conType);
		}

		// 设置计划经营部修改费用来源
		String[] deptTemp = appoingSteps.split(",");
		if (stepId.equals(deptTemp[deptTemp.length - 1])) {
			model.setJyjhAdvice("Y");
		} else if (actionId == 2122l && appoingSteps.equals("")) {
			model.setJyjhAdvice("Y");
		}
		if (actionId == 3334l) {
			model.setJyjhAdvice(null);
		}

		if (actionId == 393) {
			// 委托上报 091109 bjxu
			long entryIdDg = service.doInitialize("bqContractDelegation",
					workerCode, conid.toString());
			service.doAction(entryIdDg, workerCode, 24l, approveText, null, "");

			model.setExecFlag(1L);
			model.setWorkflowStatus(2L);
			model.setSignEndDate(new Date());
			model.setWorkflowNoDg(entryIdDg);
			model.setWorkflowDgStatus(1l);
		} else if (actionId.toString().endsWith("2") && actionId != 2122 && actionId != 3132l) {
			model.setJyjhAdvice(null);
			model.setWorkflowStatus(3L);
			model.setSignEndDate(new Date());

		}
		conremote.update(model, null, null);
	}

	// 项目合同判断审批部门
	@SuppressWarnings("unchecked")
	private void autoDoAction(Long entryId, String appoingSteps, String conType) {
		WorkflowActivity s = service.getCurrentStep(entryId);
		if (s == null)
			return;
		if (conType.equals("2")) {
			if (s.getStepId() > 12)
				return;
		} else if (conType.equals("1")) {
			if (s.getStepId() > 32)
				return;
		} else if (conType.equals("3")) {
			if (s.getStepId() > 6)
				return;
		}
		String[] as = appoingSteps.split(",");
		for (String _s : as) {
			if (s.getStepId().toString().equals(_s)) {
				return;
			}
		}
		service.doAction(entryId, "-1", ((WorkflowEvent) s.getActions().get(0))
				.getActionId(), "自动审批", null, null);
		autoDoAction(entryId, appoingSteps, conType);
		String sqlDel = "delete from wf_j_historyoperation t where t.caller='-1' and  t.entry_id= '"
				+ entryId + "'";
		bll.exeNativeSQL(sqlDel);
	}

	// 上报确定审批人 注:同一角色都可审批(无用)
	private void updateWfJRrs(Long entryId, String workerCode) {
		String getIdBysql = "select max(w.id) from wf_j_rrs_cp w";
		Long id = (bll.getSingal(getIdBysql) != null) ? Long.parseLong(bll
				.getSingal(getIdBysql).toString()) + 1l : 1l;
		String sqlinsert = "INSERT INTO wf_j_rrs_cp(id,ENTRY_ID,STEP_ID,WORKER_CODE) VALUES('"
				+ id + "','" + entryId + "','17','" + workerCode + "')";

		bll.exeNativeSQL(sqlinsert);

	}

	// 项目合同上报
	
	public void prjContractReport(Long conId,String workercode,String actionId,//modify by qpzhu 20100607
			String secondcharge, String approveText, String nextRolePs,String nextRoles,String enterPriseCode)
			throws CodeRepeatException {
		ConJContractInfo model = conremote.findById(conId);
		String workflowType = "bqProContract";
//		Long actionId = 219l;
		if (model.getWorkflowNo() == null) {
			Long actionId1=Long.parseLong(actionId);
			long entryId = service.doInitialize(workflowType, workercode, conId
					.toString());
			// 下步审批人 1317
			service.doAction(entryId, workercode, actionId1, approveText, null,
					nextRoles, nextRolePs);
			// updateWfJRrs(entryId,model.getProsy_by());
			// autoDoAction(entryId, approveDept);
			model.setWorkflowNo(entryId);
			// model.setExecFlag(1l);
			model.setSignStartDate(new Date());
			model.setWorkflowStatus(1L);
			conremote.update(model, null, secondcharge);
			entityManager.flush();
			

		} else {
			// 处理已退回
			//
			// long entryId = service.doInitialize(workflowType, workercode,
			// model
			// .getConId().toString());
			long entryId = model.getWorkflowNo();
			Long actionId1=Long.parseLong(actionId);
			service.doAction(entryId, workercode, actionId1, approveText, null,
					nextRoles,nextRolePs);
			// autoDoAction(entryId, approveDept);
			// updateWfJRrs(entryId,model.getProsy_by());
			model.setWorkflowNo(entryId);
			model.setSignStartDate(new Date());
			model.setWorkflowStatus(1l);
			// model.setExecFlag(1l);
			conremote.update(model, null, secondcharge);
			entityManager.flush();
		}
	}

	// add bjxu
	// 项目合同审批
	@SuppressWarnings("unchecked")
	public void prjContractApprove(Long entryId, String workerCode,
			Long actionId, String approveText, String nextRoles, Long conid,
			String approveDepts) throws CodeRepeatException {
		ConJContractInfo model = conremote.findById(conid);
		String conType = model.getConTypeId().toString();

		if (actionId == 1920) {
			model.setJyjhAdvice("Y");
			conremote.update(model, null, null);
		}
		String appoingSteps = null;
		if (actionId == 204) {
			// jbxu 保存部门
			ConCStatus entity = new ConCStatus();
			entity.setDeptCodes(approveDepts);
			entity.setWorkFlowNo(entryId);
			csremote.save(entity);
			model.setJyjhAdvice(null);
			conremote.update(model, null, null);
			appoingSteps = approveDepts;
		}
		if (actionId == 1617) {
			// 当法律顾问审批时 自动处理指定长主管审批人
			service.doAction(entryId, workerCode, actionId, approveText, null,
					"", model.getProsy_by());
			// updateWfJRrs(entryId, model.getProsy_by());
		} else {
			service.doAction(entryId, workerCode, actionId, approveText, null,
					nextRoles, "");
		}
		String sql = "select t.DEPT_CODES from con_c_status t where t.WORK_FLOW_NO = "
				+ entryId + " ";
		if (actionId != 204) {
			appoingSteps = (bll.getSingal(sql) != null) ? bll.getSingal(sql)
					.toString() : "";
		}
		// 自动审批
		if (actionId.toString().endsWith("2") && actionId != 1112) {
			// 退回不走自动审批
		} else {
			autoDoAction(entryId, appoingSteps, conType);
		}

		if (actionId == 183) {
			// 委托上报 091109 bjxu
			long entryIdDg = service.doInitialize("bqConDelegationManage",
					workerCode, conid.toString());
			service.doAction(entryIdDg, workerCode, 24l, approveText, null, "");
			// service.doAction(entryIdDg, workerCode, 24l, approveText, null,
			// nextRoles);

			model.setExecFlag(1L);
			model.setWorkflowStatus(2L);
			model.setSignEndDate(new Date());
			model.setWorkflowNoDg(entryIdDg);
			model.setWorkflowDgStatus(1l);

			conremote.update(model, null, null);
		} else if (actionId.toString().endsWith("2") && actionId != 1112) {
			model.setJyjhAdvice(null);
			model.setWorkflowStatus(3L);
			model.setSignEndDate(new Date());
			conremote.update(model, null, null);
		}
	}

	public ConApproveBean getSignReportData(Long conid) {
		ConApproveBean model = new ConApproveBean();
		ContractFullInfo cmodel = conremote.getConFullInfoById(conid);
		model.setConId(cmodel.getConId());
		model.setContractName(cmodel.getContractName());
		model.setConttreesNo(cmodel.getConttreesNo());
		model.setConAbstract(cmodel.getConAbstract());
		model.setCliendId(cmodel.getCliendId());
		model.setClientName(cmodel.getClientName());
		model.setConTypeId(cmodel.getConTypeId());
		model.setConTypeName(cmodel.getConTypeName());
		model.setOperateBy(cmodel.getOperateBy());
		model.setOperateName(cmodel.getOperateName());
		model.setOperateDepCode(cmodel.getOperateDepCode());
		model.setOperateDepName(cmodel.getOperateLeadName());
		model.setOperateLeadBy(cmodel.getOperateLeadBy());
		model.setOperateLeadName(cmodel.getOperateLeadName());
		if (cmodel.getWorkflowNo() != null) {
			model
					.setApproveList(service.getApproveList(cmodel
							.getWorkflowNo()));
		}
		return model;
	}

	// 采购变更上报
	public void conModifyReport(Long conModId, String workercode,
			String approveText, String nextRoles) {
		ConJModify model = modremote.findById(conModId);

		ConJContractInfo model_1 = conremote.findById(model.getConId());

		String workflowType = "bqCGContract";
		// if (model.getModiyActAmount() < 20000) {
		// Long actionId = 23l;
		// long entryId = service.doInitialize(workflowType, workercode,
		// conModId.toString());
		// service.doAction(entryId, workercode, actionId, approveText, null,
		// nextRoles);
		// model_1.setActAmount(model.getModiyActAmount());
		// model.setWorkFlowNo(entryId);
		// model.setSignStartDate(new Date());
		// model.setWorkflowStatus(2L);
		// model.setSignEndDate(new Date());
		// modremote.update(model);
		// } else {
		// Long actionId = 221l;
		// long entryId = service.doInitialize(workflowType, workercode, conId
		// .toString());
		// service.doAction(entryId, workercode, actionId, approveText, null,
		// nextRoles);
		// model.setWorkflowNo(entryId);
		// model.setSignStartDate(new Date());
		// model.setWorkflowStatus(1L);
		// //判断是否物资管理选择部门审批
		// model.setJyjhAdvice("W");
		// conremote.update(model, null, null);

		Long actionId = 221l;
		long entryId;
		if (model.getWorkFlowNo() != null) {
			entryId = model.getWorkFlowNo();
		} else {
			entryId = service.doInitialize(workflowType, workercode, conModId
					.toString());
		}
		service.doAction(entryId, workercode, actionId, approveText, null,
				nextRoles);
		model.setWorkFlowNo(entryId);
		model.setSignStartDate(new Date());
		model.setWorkflowStatus(1L);
		model.setDeptFlg("W");
		modremote.update(model);
		// }
		if (model.getConomodifyType() == 1) {
			model_1.setExecFlag(1L);
			try {
				conremote.update(model_1, null, null);
			} catch (CodeRepeatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			model_1.setExecFlag(3L);
			try {
				conremote.update(model_1, null, null);
			} catch (CodeRepeatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// 采购变更审批
	@SuppressWarnings("unchecked")
	public void conModApprove(Long entryId, String workerCode, Long actionId,
			String approveText, String nextRoles, Long conModId,
			String approveDepts, String stepId) throws CodeRepeatException {

		ConJModify model = modremote.findById(conModId);

		ConJContractInfo modelinfo = conremote.findById(model.getConId());

		String appoingSteps = null;
		if (actionId == 2122l) {
			// jbxu 保存部门
			ConCStatus entity = new ConCStatus();
			entity.setDeptCodes(approveDepts);
			entity.setWorkFlowNo(entryId);
			csremote.save(entity);
			model.setDeptFlg(null);
			modremote.update(model);
			appoingSteps = approveDepts;
		}
		// boolean sumAbove2w = false;
		// boolean sumAbove10w = false;
		// boolean sumAbove20w = false;
		// if (model.getActAmount() != null) {
		// double sum = model.getModiyActAmount();
		// if (sum <= 100000 && sum > 20000) {
		// sumAbove2w = true;
		// sumAbove10w = false;
		// sumAbove20w = false;
		// } else if (sum <= 200000 && sum > 100000) {
		// sumAbove2w = false;
		// sumAbove10w = true;
		// sumAbove20w = false;
		// } else {
		// sumAbove2w = false;
		// sumAbove10w = false;
		// sumAbove20w = true;
		// }
		// }
		// Map m = new java.util.HashMap();
		// m.put("sumAbove2w", sumAbove2w);
		// m.put("sumAbove10w", sumAbove10w);
		// m.put("sumAbove20w", sumAbove20w);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		String sql = "select t.DEPT_CODES from con_c_status t where t.WORK_FLOW_NO = "
				+ entryId + " ";
		if (actionId != 2122l) {
			appoingSteps = (bll.getSingal(sql) != null) ? bll.getSingal(sql)
					.toString() : "";
		}
		// 自动审批
		autoDoAction(entryId, appoingSteps, modelinfo.getConTypeId().toString());

		// 当法律顾问审批时 自动处理指定长主管审批人
		// if (actionId == 3839l) {
		// updateWfJRrs(entryId, modelinfo.getProsy_by());
		// }

		// 设置计划经营部修改费用来源
		// String[] deptTemp = appoingSteps.split(",");
		// if (stepId.equals(deptTemp[deptTemp.length - 1])) {
		// model.setDeptFlg("Y");
		// } else if (actionId == 2122l && deptTemp.length == 0) {
		// model.setDeptFlg("Y");
		// }
		// if (actionId == 3334l) {
		// model.setDeptFlg(null);
		// }
		if (actionId == 393) {
			model.setWorkflowStatus(2L);
			model.setSignEndDate(new Date());
			// model.setActAmount(modelinfo.getActAmount());
			modelinfo.setActAmount(model.getModiyActAmount());
			if (model.getConomodifyType() == 1)
				modelinfo.setExecFlag(1l);
			else
				modelinfo.setExecFlag(3l);
		} else if (actionId.toString().endsWith("2") && actionId != 2122) {
			model.setWorkflowStatus(3L);
			model.setSignEndDate(new Date());
			modelinfo.setExecFlag(1l);
		}
		conremote.update(modelinfo, null, null);
		modremote.update(model);
	}

	// 采购结算上报
	public void conBalanceReport(ConJBalance bal, String workercode) {
		ConJBalance model = balremote.findById(bal.getBalanceId());
		model.setPassPrice(bal.getApplicatPrice());
		model.setPassDate(new Date());
		model.setWorkflowStatus(1L);
		model.setBalaFlag("1");
		String workflowType = "bubConBalance";
		Long actionId = 24l;
		long entryId;
		if (bal.getWorkflowNo() == null) {
			entryId = service.doInitialize(workflowType, workercode, bal
					.getBalanceId().toString());
		} else {
			entryId = bal.getWorkflowNo();
		}
		service.doAction(entryId, workercode, actionId, "上报", null);
		model.setWorkflowNo(entryId);
		balremote.updateBalance(model);
	}

	// 采购结算审批
	public void conBalanceApprove(Long entryId, String workerCode,
			Long actionId, String approveText, String nextRoles,
			ConJBalance bal, ConJBalaApprove hismodel)
			throws CodeRepeatException {
		balremote.update(bal);
		bhisremote.save(hismodel);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
	}

	// 项目合同变更上报
	public void prjConModifyReport(Long conModId, String workercode,
			String approveText, String nextRoles) {
		ConJModify model = modremote.findById(conModId);

		ConJContractInfo model_1 = conremote.findById(model.getConId());

		String workflowType = "prjConModify";
		Long actionId = 24l;
		if (model.getWorkFlowNo() == null) {
			long entryId = service.doInitialize(workflowType, workercode,
					conModId.toString());
			service.doAction(entryId, workercode, actionId, approveText, null,
					nextRoles);
			model.setWorkFlowNo(entryId);
			model.setSignStartDate(new Date());
			model.setWorkflowStatus(1L);
			modremote.update(model);
		} else {
			long entryId = service.doInitialize(workflowType, workercode, model
					.getConModifyId().toString());
			service.doAction(entryId, workercode, actionId, approveText, null,
					nextRoles);
			model.setWorkFlowNo(entryId);
			model.setSignStartDate(new Date());
			model.setWorkflowStatus(1l);
			modremote.update(model);
		}

		if (model.getConomodifyType() == 1) {
			model_1.setExecFlag(2L);
			try {
				conremote.update(model_1, null, null);
			} catch (CodeRepeatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			model_1.setExecFlag(3L);
			try {
				conremote.update(model_1, null, null);
			} catch (CodeRepeatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 项目合同变更审批
	public void prjConModApprove(Long entryId, String workerCode,
			Long actionId, String approveText, String nextRoles, Long conModId)
			throws CodeRepeatException {
		ConJModify model = modremote.findById(conModId);
		ConJContractInfo modelinfo = conremote.findById(model.getConId());
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		if (actionId == 83) {
			model.setWorkflowStatus(2L);
			model.setSignEndDate(new Date());
			// model.setActAmount(model.getModiyActAmount());
			modelinfo.setActAmount(model.getModiyActAmount());
			if (model.getConomodifyType() == 1)
				modelinfo.setExecFlag(1l);
			else
				modelinfo.setExecFlag(3l);
			conremote.update(modelinfo, null, null);
			modremote.update(model);
		} else if (actionId == 42 || actionId == 52 || actionId == 62
				|| actionId == 72 || actionId == 82 || actionId == 92) {
			model.setWorkflowStatus(3L);
			model.setSignEndDate(new Date());
			modelinfo.setExecFlag(1l);
			conremote.update(modelinfo, null, null);
			modremote.update(model);
		}
	}

	// 项目合同结算上报
	public void prjConBalanceReport(String balanceId, String workercode,
			String approveText, String nextRoles, String passPrice,
			String workflowType) {
		ConJBalance model = balremote.findById(Long.parseLong(balanceId));
		ConJContractInfo conmodel = conremote.findById(model.getConId());
		if (conmodel.getAppliedAmount() != null)
			conmodel.setAppliedAmount(conmodel.getAppliedAmount()
					+ model.getApplicatPrice());
		else
			conmodel.setAppliedAmount(model.getApplicatPrice());
		try {
			conremote.update(conmodel, null, null);
		} catch (CodeRepeatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// model.setPassPrice(Double.valueOf(passPrice));
		// 判断上报是质保金、非质保金
		if (workflowType.equals("prjConQuality")) {
			model.setMemo("YES");
		} else if (workflowType.equals("prjConBalance")) {
			model.setMemo("NO");
		}
		model.setPassDate(new Date());
		model.setWorkflowStatus(1L);
		model.setBalaFlag("1");
		Long actionId = 24l;
		long entryId;
		if (model.getWorkflowNo() == null) {
			entryId = service.doInitialize(workflowType, workercode, balanceId
					.toString());
		} else {
			entryId = model.getWorkflowNo();
		}
//		entityManager.flush();
		service.doAction(entryId, workercode, actionId, approveText, null,
				"",nextRoles);
		model.setWorkflowNo(entryId);
		balremote.updateBalance(model);
	}

	// 项目合同结算审批
	public void prjConBalanceApprove(Long entryId, String workerCode,
			Long actionId, String approveText, String nextRoles,
			ConJBalance bal, ConJBalaApprove hismodel)
			throws CodeRepeatException {
		if (actionId == 67) {
			ConJContractInfo conmodel = conremote.findById(bal.getConId());
			if (conmodel.getApprovedAmount() != null)
				conmodel.setApprovedAmount(conmodel.getApprovedAmount()
						+ bal.getApplicatPrice());
			else
				conmodel.setApprovedAmount(bal.getApplicatPrice());
			try {
				conremote.update(conmodel, null, null);
			} catch (CodeRepeatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		balremote.update(bal);
		bhisremote.save(hismodel);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
	}

	// 项目合同委托管理审批 add by drdu 091109
	public void delegationApproveSign(Long entryId, String workerCode,
			Long actionId, String approveText, String nextPerson, Long conid,
			String prosyBy, String sDate, String eDate)
			throws CodeRepeatException, ParseException {

		ConJContractInfo entity = conremote.findById(conid);
		String sFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
		if (actionId == 57l) {
			service.doAction(entryId, workerCode, actionId, approveText, null,
					"", entity.getOperateBy());
			entity.setWorkflowDgStatus(2l);
		} else {
			if (actionId == 45l) {
				entity.setProxyCode(nextPerson);
			}
			service.doAction(entryId, workerCode, actionId, approveText, null,
					"", nextPerson);
		}
		if (actionId == 52) {
			entity.setWorkflowDgStatus(1l);
			long entryIdDg = service.doInitialize("bqConDelegationManage",
					workerCode, conid.toString());
			service.doAction(entryIdDg, workerCode, 24l, approveText, null, "",
					"");
			entity.setWorkflowNoDg(entryIdDg);
		} else if (actionId == 73) {
			entity.setWorkflowDgStatus(3l);
		} else {
			// entity.setProsy_by(prosyBy);
			entity.setProsyStartDate(sdf.parse(sDate));
			entity.setProsyEndDate(sdf.parse(eDate));
		}
		conremote.update(entity, null, null);
	}

	// 采购合同委托管理审批 add by drdu 091119
	public void conDelegationApproveSign(Long entryId, String workerCode,
			Long actionId, String approveText, String nextPerson, Long conid,
			String prosyBy, String sDate, String eDate)
			throws CodeRepeatException, ParseException {

		ConJContractInfo entity = conremote.findById(conid);
		String sFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(sFormat);

		if (actionId == 56l) {
			service.doAction(entryId, workerCode, actionId, approveText, null,
					"", entity.getOperateBy());
			entity.setWorkflowDgStatus(2l);
		} else {
			if (actionId == 45l) {
				entity.setProxyCode(nextPerson);
			}
			service.doAction(entryId, workerCode, actionId, approveText, null,
					"", nextPerson);
		}
		if (actionId == 52) {
			entity.setWorkflowDgStatus(1l);
			long entryIdDg = service.doInitialize("bqContractDelegation",
					workerCode, conid.toString());
			service.doAction(entryIdDg, workerCode, 24l, approveText, null, "",
					"");
			entity.setWorkflowNoDg(entryIdDg);
		} else if (actionId == 63) {
			entity.setWorkflowDgStatus(3l);
		} else {
//			entity.setProsy_by(prosyBy);
			entity.setProsyStartDate(sdf.parse(sDate));
			entity.setProsyEndDate(sdf.parse(eDate));
		}
		conremote.update(entity, null, null);
	}

	// 采购合同灞桥结算上报
	@SuppressWarnings("unchecked")
	public void bqConBalanceReport(String appId, String enterpriseCode,
			String workercode) {
		List<ConJBalance> updetList = balremote.getBalaceListByAppId(appId,
				enterpriseCode);
		String workflowType = "bqConBalance";
		Long actionId = 24l;
		Long entryId;
		if (updetList.get(0).getWorkflowNo() != null) {
			entryId = updetList.get(0).getWorkflowNo();
		} else {
			entryId = service.doInitialize(workflowType, workercode, appId);
		}
		service.doAction(entryId, workercode, actionId, "", null, "");
		for (ConJBalance model : updetList) {
			model.setElsePrice(1d);
			model.setWorkflowNo(entryId);
			model.setWorkflowStatus(1L);
			model.setBalaFlag("1");
			balremote.updateBalance(model);
		}
	}

	// 采购合同灞桥结算审批
	public void bqConBalanceApprove(String appId, Long entryId, Long actionId,
			String approveText, String nextRoles, String approveDepts,
			String enterpriseCode, String workercode) {
		String appoingSteps = null;
		if (actionId == 45) {
			// jbxu 保存部门
			ConCStatus entity = new ConCStatus();
			entity.setDeptCodes(approveDepts);
			entity.setWorkFlowNo(entryId);
			csremote.save(entity);
			appoingSteps = approveDepts;
			List<ConJBalance> updetList = balremote.getBalaceListByAppId(appId,
					enterpriseCode);
			for (ConJBalance model : updetList) {
				model.setElsePrice(null);
				balremote.updateBalance(model);
			}
		}

		service.doAction(entryId, workercode, actionId, approveText, null,
				nextRoles);

		String sql = "select t.DEPT_CODES from con_c_status t where t.WORK_FLOW_NO = "
				+ entryId + " ";
		if (actionId != 45) {
			appoingSteps = (bll.getSingal(sql) != null) ? bll.getSingal(sql)
					.toString() : "";
		}
		if (actionId.toString().endsWith("2")) {
		} else {
			autoDoAction(entryId, appoingSteps, "3");
		}

		if (actionId == 73) {
			List<ConJBalance> updetList = balremote.getBalaceListByAppId(appId,
					enterpriseCode);
			for (ConJBalance model : updetList) {
				model.setBalaFlag("2");
				model.setWorkflowStatus(2L);
				balremote.updateBalance(model);
			}
		} else if (actionId.toString().endsWith("2")) {
			List<ConJBalance> updetList = balremote.getBalaceListByAppId(appId,
					enterpriseCode);
			for (ConJBalance model : updetList) {
				model.setBalaFlag("0");
				model.setWorkflowStatus(3L);
				balremote.updateBalance(model);
			}
		}
	}
}
