/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.issueheadregister.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.InvJIssueDetails;
import power.ejb.resource.InvJIssueDetailsFacadeRemote;
import power.ejb.resource.InvJIssueHead;
import power.ejb.resource.InvJIssueHeadFacadeRemote;
import power.ejb.resource.MrpJPlanRequirementHead;
import power.ejb.resource.business.IssueHeadApprove;
import power.ejb.resource.business.IssueHeadRegister;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

/**
 * 领料单登记
 * 
 * @author qzhang
 * 
 */
@SuppressWarnings("serial")
public class IssueHeadRegisterAction extends AbstractAction {
	/** 领料单登记remote */
	private IssueHeadRegister remote;
	/** 领料单表头remote */
	private InvJIssueHeadFacadeRemote headRemote;
	/** 领料单明细remote */
	private InvJIssueDetailsFacadeRemote detailsRemote;
	/** 领料单 */
	private InvJIssueHead headInfo;
	/** 共通base */
	private BaseDataManager baseRemote;
	// /** 共通方法*/
	// private CommInterfaceFacade comm;
	/** 单位 */
	private BpCMeasureUnitFacadeRemote unitRemote;
	/** 专业 */
	private RunCSpecialsFacadeRemote speRemote;
	/** 人员 */
	private HrJEmpInfoFacadeRemote empInfoRemote;
	/** 领料单审批 */
	private IssueHeadApprove approveRemote;
	private static final String MESSAGE_ROLLBACK = "'操作数据库过程中异常终了。'";
	private static final String MESSAGE_LOCK = "'他人使用中。'";
	/** 单据状态：审批中 */
	private static final String ISSUE_STATUS_APPROVE = "1";
	/** 单据状态：待审批 */
	private static final String ISSUE_STATUS_WAIT = "0";

	/**
	 * 初始化
	 */
	public IssueHeadRegisterAction() {
		// 初始化
		remote = (IssueHeadRegister) factory
				.getFacadeRemote("IssueHeadRegisterImpl");
		headRemote = (InvJIssueHeadFacadeRemote) factory
				.getFacadeRemote("InvJIssueHeadFacade");
		detailsRemote = (InvJIssueDetailsFacadeRemote) factory
				.getFacadeRemote("InvJIssueDetailsFacade");
		empInfoRemote = (HrJEmpInfoFacadeRemote) factory
				.getFacadeRemote("HrJEmpInfoFacade");
		baseRemote = (BaseDataManager) factory
				.getFacadeRemote("BaseDataManagerImpl");
		// // 共通类初始化
		// comm = new CommInterfaceFacade();
		// 单位
		unitRemote = (BpCMeasureUnitFacadeRemote) factory
				.getFacadeRemote("BpCMeasureUnitFacade");
		// 专业
		speRemote = (RunCSpecialsFacadeRemote) factory
				.getFacadeRemote("RunCSpecialsFacade");
		approveRemote = (IssueHeadApprove) factory
				.getFacadeRemote("IssueHeadApproveImpl");
	}

	/**
	 * 领料单查询
	 * 
	 * @throws JSONException
	 * @throws ParseException
	 */
	public void getIssueHeadRigisterList() throws JSONException, ParseException {
		// 查询值
		String fuzzy = request.getParameter("fuzzy");
		// 是否显示所有领料单
		String flag = request.getParameter("flag");

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		// 部门code
		Employee person = baseRemote.getEmployeeInfo(employee.getWorkerCode());
		PageObject obj = remote.findIssueHead(fuzzy, flag,
				person.getDeptCode(), employee.getEnterpriseCode(), Integer
						.parseInt(start), Integer.parseInt(limit));
		write(JSONUtil.serialize(obj));
	}
	
	/*
	 * 查询领料单
	 * @author yiliu
	 */
	public void getIssueInfoList() throws JSONException, ParseException {
		//领料单号 add by fyyang 20100113
		String issueNo=request.getParameter("issueNo");
		
		String queryType=request.getParameter("queryType");
		// 起始时间
		String fromDate = request.getParameter("fromDate");
		// 直到时间
		String toDate = request.getParameter("toDate");
		// 申请人
		String appBy = request.getParameter("applicationBy");
		
		//领料部门
		String acceptDept = request.getParameter("acceptDept");
		
		//物资名称
		String materialName = request.getParameter("materialName");
		
		//接收状态
		String receiveStatus = request.getParameter("receiveStatus");
		
		//审批状态 
		String issueStatus = request.getParameter("issueStatus");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		// 部门code
		Employee person = baseRemote.getEmployeeInfo(employee.getWorkerCode());
		PageObject obj = remote.getIssueInfo(fromDate, toDate,appBy,acceptDept,materialName,
				receiveStatus,issueStatus,queryType,employee.getWorkerCode(), employee.getEnterpriseCode(),issueNo, Integer
						.parseInt(start), Integer.parseInt(limit));
		write(JSONUtil.serialize(obj));
	}
	
	
	/*
	 * 登陆人领料单查询
	 * @author yiliu
	 */
	public void getIssueListByLogin() throws JSONException, ParseException {
		PageObject obj = null;
		/** 计划开始时间 */
        String dateFrom = request.getParameter("dateFrom");
        /** 计划结束时间 */
        String dateTo = request.getParameter("dateTo");
		Object objStatus=request.getParameter("status");
        String status="";
        if(objStatus!=null)
        {
        	status=objStatus.toString();
        }
        /** 申请人 */
        String appBy = request.getParameter("mrBy");
        String issueNo = request.getParameter("issueNo");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		obj = remote.findIssueListByLogin(dateFrom, dateTo,appBy,status,issueNo,employee.getEnterpriseCode(), Integer.parseInt(start), Integer.parseInt(limit));
		write(JSONUtil.serialize(obj));
	}
	
	/*
	 * 登陆人领料单查询
	 * @author yiliu
	 */
	public void getIssueListByLoginJoin() throws JSONException, ParseException {
		PageObject obj = null;
		/** 计划开始时间 */
        String dateFrom = request.getParameter("dateFrom");
        /** 计划结束时间 */
        String dateTo = request.getParameter("dateTo");
		Object objStatus=request.getParameter("status");
        String status="";
        if(objStatus!=null)
        {
        	status=objStatus.toString();
        }
        /** 申请人 */
        String appBy = request.getParameter("mrBy");
        String issueNo = request.getParameter("issueNo");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(appBy != null && !"".equals(appBy)) {
			 obj = remote.findIssueListByLoginJoin(dateFrom, dateTo,appBy,status,issueNo,employee.getEnterpriseCode(), Integer.parseInt(start), Integer.parseInt(limit));
		} else {
			obj = remote.findIssueListByLogin(dateFrom, dateTo,appBy,status,issueNo,employee.getEnterpriseCode(), Integer.parseInt(start), Integer.parseInt(limit));
		}
		
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 领料单审批列表查询
	 * 
	 * @throws JSONException
	 */
	public void getIssueHeadApproveList() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String issueNo=request.getParameter("issueNo");
		String status=request.getParameter("status");
		
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String nos = workflowService.getAvailableWorkflow(new String[] {
				"hfResourceGetGDZC", "hfResourceGetXZ", "hfResourceGetSC" },
				employee.getWorkerCode());
		PageObject obj = remote.findIssueHeadApproveList(
				employee.getDeptCode(), employee.getEnterpriseCode(),issueNo,status, nos,employee.getWorkerCode(),
				Integer.parseInt(start), Integer.parseInt(limit));
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 根据领料单流水号查询领料单数据
	 * 
	 * @throws JSONException
	 */
	public void getIssueHeadById() throws JSONException {
		// 领料单编号ID
		String issueHeadId = request.getParameter("issueHeadId");
		InvJIssueHead head = headRemote.findById(Long.parseLong(issueHeadId));
		write(JSONUtil.serialize(head));
	}

	/**
	 * 专业查询
	 * 
	 * @throws JSONException
	 */
	public void getAllFeeBySpecial() throws JSONException {
		// 查询所有专业
		// List<RunCSpecials> specials =
		// comm.findSpeList(employee.getEnterpriseCode());
		List<RunCSpecials> specials = speRemote.findSpeList(employee
				.getEnterpriseCode());
		PageObject obj = new PageObject();
		obj.setList(specials);
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 检索物料需求计划主表
	 * only for 领料单登记页面需求计划查询
	 * modify by fyyang 090623 增加了申请人查询条件
	 * @throws JSONException
	 */
	public void getPlanRequirementHeadDatas() throws JSONException {
		String applyBy=request.getParameter("applyBy");
		// 开始行号
		String strStart = request.getParameter("start");
		// 行数
		String strlimit = request.getParameter("limit");
		int start = 0;
		int limit = 0;
		if (strStart != null && strStart.length() > 0) {
			start = Integer.parseInt(strStart);
		}
		if (strlimit != null && strlimit.length() > 0) {
			limit = Integer.parseInt(strlimit);
		}
		// 部门code
		Employee person = baseRemote.getEmployeeInfo(employee.getWorkerCode());
		PageObject obj = remote.findPlanRequirementHead(person.getDeptCode(),
				employee.getEnterpriseCode(),applyBy, employee.getWorkerCode(),start, limit);
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 获取单位名称
	 */
	public void getIssueRegisterUnitName() {
		// 单位id
		String strStockUmId = request.getParameter("stockUmId");
		// 单位名称
		// PageObject units =
		// comm.findUnitList(strStockUmId,employee.getEnterpriseCode());
		// if(units.getList().size() > 0){
		// BpCMeasureUnit unit = (BpCMeasureUnit)units.getList().get(0);
		// write(unit.getUnitName());
		// return;
		// }
		try {
			Long unitId = Long.parseLong(strStockUmId);
			BpCMeasureUnit unit = unitRemote.findById(unitId);
			if (unit != null) {
				write(unit.getUnitName());
				return;
			} else {
				write("");
				return;
			}
		} catch (NumberFormatException e) {
			write("");
			return;
		}
	}

	/**
	 * 根据工号查询用户信息，得到用户的部门code和部门name
	 * 
	 * @throws JSONException
	 */
	public void getDeptInfoByEmpCode() throws JSONException {
		// 工号
		String empCode = request.getParameter("empCode");
		Employee person = baseRemote.getEmployeeInfo(empCode);
		write(JSONUtil.serialize(person));
	}

	/**
	 * 人员编码对应的名字
	 * 
	 * @throws JSONException
	 */
	public void getReceiptByName() throws JSONException {
		// 申请领料人
		String receiptBy = request.getParameter("receiptBy");
		Employee person = baseRemote.getEmployeeInfo(receiptBy);
		if (person != null) {
			write(person.getWorkerName());
		} else {
			write("");
		}
	}

	/**
	 * 申请领用人信息list
	 * 
	 * @throws JSONException
	 */
	public void getReceiptByList() throws JSONException {
		// 查询所有人员信息
		List<HrJEmpInfo> empInfos = empInfoRemote.findEmpListByDept("%");
		PageObject obj = new PageObject();
		obj.setList(empInfos);
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 获取初始化用户信息
	 * 
	 * @throws JSONException
	 */
	public void getInitUserCode() throws JSONException {
		write(JSONUtil.serialize(employee));
	}

	/**
	 * 删除领料单
	 */
	public void deleteIssueRecords() {
		// 领料单编号ID
		String issueHeadId = request.getParameter("issueHeadId");
		// 上次修改时间
		String lastModifiedDate = request.getParameter("lastModifiedDate");
		// 如果时间里有T，去除
		lastModifiedDate = lastModifiedDate.replace("T", " ");
		try {
			// entity
			InvJIssueHead head = headRemote.findById(Long
					.parseLong(issueHeadId));
			if (head == null) {
				throw new RuntimeException();
			}
			String dbDate = DateToString(head.getLastModifiedDate());
			// 时间不相同
			if (!dbDate.equals(lastModifiedDate)) {
				throw new Exception();
			}
			// 删除
			remote.deleteIssueRecords(employee.getEnterpriseCode(), employee
					.getWorkerCode(), issueHeadId);
			write("{success:true}");
		} catch (RuntimeException e) {
			write("{success:false,msg:" + MESSAGE_ROLLBACK + "}");
		} catch (Exception e) {
			write("{success:false,msg:" + MESSAGE_LOCK + "}");
		}
	}

	/**
	 * @throws JSONException
	 * 
	 */
	public void getIssueHeadRegisterDetailList() throws JSONException {
		// 标记是否要检索领料单明细表还是要检索物料需求计划明细表
		String flag = request.getParameter("flag");
		// 领料单ID
		String issueHeadId = request.getParameter("issueHeadId");
		// 申请单id
		String strRequimentDetailIds = request.getParameter("strRequimentDetailIds");
//		Long requimentHeadId = null;
//		if (strRequimentHeadId != null && strRequimentHeadId.length() > 0) {
//			requimentHeadId = Long.parseLong(strRequimentHeadId);
//		}
		// 开始行号
		String strStart = request.getParameter("start");
		// 行数
		String strlimit = request.getParameter("limit");
		int start = 0;
		int limit = 0;
		if (strStart != null && strStart.length() > 0) {
			start = Integer.parseInt(strStart);
		}
		if (strlimit != null && strlimit.length() > 0) {
			limit = Integer.parseInt(strlimit);
		}
		// 查询领料单明细表
		if ("1".equals(flag)) {
			PageObject obj = remote.findIssueHeadDetails(issueHeadId, employee
					.getEnterpriseCode(), start, limit);
			write(JSONUtil.serialize(obj));
		} else {
			// 查询物资需求明细表
			//modify by fyyang 20100408
			PageObject obj = remote.findIssueRequimentDetail(strRequimentDetailIds,
					employee.getEnterpriseCode(), start, limit);
			write(JSONUtil.serialize(obj));
		}
	}

	/**
	 * 增加领料单和领料单明细数据
	 * 
	 * @throws JSONException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public void addIssueHeadAndDetails() throws JSONException, ParseException {
		// 新增明细数据
		String strNewDetails = request.getParameter("newDetails");
		try {
			// 明细数据
			List<Map> newDetails = (List<Map>) JSONUtil
					.deserialize(strNewDetails);
			// 表头数据
			InvJIssueHead head = addIssueHeadData();
			// 明细数据
			List<InvJIssueDetails> details = addDetailsDatas(newDetails);
			// 获得流水号
			Long issueId = remote.addIssueHeadAndDetails(head, details);
			write("{success:true,msg:" + issueId + "}");
		} catch (Exception e) {
			write("{success:false,msg:" + MESSAGE_ROLLBACK + "}");
		}
	}

	/**
	 * 修改领料单和领料单明细
	 */
	@SuppressWarnings("unchecked")
	public void updateIssueHeadAndDetails() {
		// 新增明细数据
		String strNewDetails = request.getParameter("newDetails");
		// 修改明细数据
		String strUpdateDetails = request.getParameter("updateDetails");
		// 删除明细数据
		String strDeleteDetails = request.getParameter("deletedDetails");
		try {
			// 新增明细数据
			List<Map> newDetails = (List<Map>) JSONUtil
					.deserialize(strNewDetails);
			// 更新明细数据
			List<Map> updateDetails = (List<Map>) JSONUtil
					.deserialize(strUpdateDetails);
			// 删除数据
			List<Map> deletedDetails = (List<Map>) JSONUtil
					.deserialize(strDeleteDetails);
			// 表头数据
			InvJIssueHead head = updateIssueHeadData();
			// 新增明细数据
			List<InvJIssueDetails> addDetails = addDetailsDatas(newDetails);
			// 修改明细数据
			List<InvJIssueDetails> upDetails = updateDetailsDatas(updateDetails);
			// 删除明细数据
			List<InvJIssueDetails> delDetails = deleteDetailsDatas(deletedDetails);
			// 保存
			remote.updateIssueHeadAndDetails(head, addDetails, upDetails,
					delDetails);
			write("{success:true}");
		} catch (RuntimeException e) {
			write("{success:false,msg:" + MESSAGE_ROLLBACK + "}");
		} catch (Exception e) {
			write("{success:false,msg:" + MESSAGE_LOCK + "}");
		}
	}

	/**
	 * 领料单上报操作
	 */
	public void reportIssueHeadRecord() {
		// 领料单数据
		String strIssueHeadId = request.getParameter("issueHeadId");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		// 上次修改时间
		// String lastModifiedDate = request.getParameter("lastModifiedDate");
		try {
			approveRemote
					.IssueHeadReport(Long.parseLong(strIssueHeadId), Long
							.parseLong(actionId), approveText, employee
							.getWorkerCode());
			// // 领料单数据
			// InvJIssueHead head =
			// headRemote.findById(Long.parseLong(strIssueHeadId));
			// // 如果时间里有T，去除
			// lastModifiedDate = lastModifiedDate.replace("T", " ");
			// // db中现在日期
			// String date = DateToString(head.getLastModifiedDate());
			// // 日期不相等，返回出错
			// if (!date.equals(lastModifiedDate)) {
			// throw new Exception();
			// }
			// // 单据状态
			// head.setIssueStatus(ISSUE_STATUS_APPROVE);
			// // 上次修改人
			// head.setLastModifiedBy(employee.getWorkerCode());
			// // 上次修改时间
			// head.setLastModifiedDate(new Date());
			// // 更新
			// headRemote.update(head);
			write("{success:true}");
		} catch (RuntimeException e) {
			write("{success:false,msg:" + MESSAGE_ROLLBACK + "}");
		} catch (Exception e) {
			write("{success:false,msg:" + MESSAGE_LOCK + "}");
		}
	}

	public void approveIssueHeadRecord() throws JSONException {
		String strIssueHeadId = request.getParameter("issueHeadId");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String workerCode = request.getParameter("workerCode");
		String nrs = request.getParameter("nrs");
		String eventIdentify = request.getParameter("eventIdentify");
		// 修改明细核准数量
		String strUpdateDetails = request.getParameter("updateDetails");
		
		String itemCode=request.getParameter("itemCode");
		// List<Map> updateDetails=(List<Map>)
		// JSONUtil.deserialize(strUpdateDetails);
		try {
			// approveRemote.IssueHeadApprove(Long.parseLong(strIssueHeadId),
			// Long
			// .parseLong(actionId), approveText, workerCode, nrs,
			// eventIdentify, updateDetails);
			approveRemote.IssueHeadApprove(Long.parseLong(strIssueHeadId), Long.parseLong(actionId), approveText, workerCode, nrs,eventIdentify, itemCode);
			write("{success:true}");
		} catch (RuntimeException e) {
			write("{success:false,msg:" + MESSAGE_ROLLBACK + "}");
		} catch (Exception e) {
			write("{success:false,msg:" + MESSAGE_LOCK + "}");
		}
	}

	/**
	 * 新增领料单数据
	 * 
	 * @param form
	 *            表单
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	private InvJIssueHead addIssueHeadData() throws ParseException {
		// 新增领料单数据
		InvJIssueHead head = new InvJIssueHead();
		// 需求计划单编号
		head.setMrNo(headInfo.getMrNo());
		// 项目编号
		head.setProjectCode(headInfo.getProjectCode());
		// 设置
		head = setIssueData(head);
		return head;
	}

	/**
	 * 更新领料单表头数据
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private InvJIssueHead updateIssueHeadData() throws Exception {
		// 领料单数据
		InvJIssueHead head = headRemote.findById(headInfo.getIssueHeadId());
		// 上次修改时间
		String lastModifiedDate = DateToString(headInfo.getLastModifiedDate());
		// 如果时间里有T，去除
		lastModifiedDate = lastModifiedDate.replace("T", " ");
		// db中现在日期
		String date = DateToString(head.getLastModifiedDate());
		// 日期不相等，返回出错
		if (!date.equals(lastModifiedDate)) {
			throw new Exception();
		}
		head = setIssueData(head);
		return head;
	}

	/**
	 * 领料单数据设值
	 * 
	 * @throws ParseException
	 */
	private InvJIssueHead setIssueData(InvJIssueHead head)
			throws ParseException {
		// 成本分摊项目
		head.setCostItemId(headInfo.getCostItemId());
		// 费用来源
		if(headInfo.getItemCode() != null && !"".equals(headInfo.getItemCode())) {
			head.setItemCode(headInfo.getItemCode());//modify by ywliu  2009/7/6
		}
		
		// 申请领料人
		head.setReceiptBy(headInfo.getReceiptBy());
		// 领用部门
		head.setReceiptDep(headInfo.getReceiptDep());
		// 申请领用日期
		// modified by liuyi 091209 保存时将申请领用日期设为当前时间
//		head.setDueDate(headInfo.getDueDate());
		head.setDueDate(new Date());
		// 费用归口部门
		head.setFeeByDep(headInfo.getFeeByDep());
		// 费用归口专业
		head.setFeeBySpecial(headInfo.getFeeBySpecial());
		// 备注
		head.setMemo(headInfo.getMemo());
		// 企业编码
		head.setEnterpriseCode(employee.getEnterpriseCode());
		// 计划来源 addby ywliu
		head.setPlanOriginalId(headInfo.getPlanOriginalId());
		// 是否紧急领用
		if (headInfo.getIsEmergency() == null) {
			head.setIsEmergency(Constants.FLAG_N);
		} else {
			head.setIsEmergency(Constants.FLAG_Y);
		}
		// 上次修改人
		head.setLastModifiedBy(employee.getWorkerCode());
		// 是否使用
		head.setIsUse(Constants.IS_USE_Y);
		// 上次修改日期
		head.setLastModifiedDate(new Date());
		// 单据状态
		if (headInfo.getIssueStatus() == null
				|| headInfo.getIssueStatus().length() == 0) {
			head.setIssueStatus(ISSUE_STATUS_WAIT);
		} else {
			head.setIssueStatus(headInfo.getIssueStatus());
		}
		return head;
	}

	/**
	 * 新增明细数据
	 * 
	 * @param newDetails
	 *            明细数据
	 */
	@SuppressWarnings("unchecked")
	private List<InvJIssueDetails> addDetailsDatas(List<Map> newDetails) {
		List<InvJIssueDetails> details = new ArrayList<InvJIssueDetails>();
		InvJIssueDetails detail = null;
		for (Map map : newDetails) {
			detail = new InvJIssueDetails();
			// 明细数据
			detail = setIssueDetail(detail, map);
			details.add(detail);
		}
		return details;
	}

	/**
	 * 修改明细数据
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<InvJIssueDetails> updateDetailsDatas(List<Map> updateDetails)
			throws Exception {
		List<InvJIssueDetails> details = new ArrayList<InvJIssueDetails>();
		InvJIssueDetails detail = null;
		for (Map map : updateDetails) {
			// 检索
			detail = detailsRemote.findById(getLong(map.get("issueDetailsId")));
			// 上次修改时间
			String lastModifiedDate = getString(map.get("lastModifiedDate"));
			// 如果时间里有T，去除
			lastModifiedDate = lastModifiedDate.replace("T", " ");
			// db中现在日期
			String date = DateToString(detail.getLastModifiedDate());
			if (!date.equals(lastModifiedDate)) {
				throw new Exception();
			}
			detail = setIssueDetail(detail, map);
			details.add(detail);
		}
		return details;
	}

	/**
	 * 删除明细记录
	 * 
	 * @param deletedDetails
	 *            要删除的明细记录集(包含上次修改时间和流水号)
	 * @return 流水号集合
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<InvJIssueDetails> deleteDetailsDatas(List<Map> deletedDetails)
			throws Exception {
		List<InvJIssueDetails> deleteIds = new ArrayList<InvJIssueDetails>();
		Long id = null;
		InvJIssueDetails detail = null;
		for (Map map : deletedDetails) {
			// 流水号
			id = getLong(map.get("issueDetailsId"));
			// 检索
			detail = detailsRemote.findById(id);
			// 上次修改时间
			String lastModifiedDate = getString(map.get("lastModifiedDate"));
			// 如果时间里有T，去除
			lastModifiedDate = lastModifiedDate.replace("T", " ");
			// db中现在日期
			String date = DateToString(detail.getLastModifiedDate());
			if (!date.equals(lastModifiedDate)) {
				throw new Exception();
			}
			// 上次修改人
			detail.setLastModifiedBy(employee.getWorkerCode());
			// 是否使用
			detail.setIsUse("N");
			deleteIds.add(detail);
		}
		return deleteIds;
	}

	/**
	 * 明细数据设置
	 */
	@SuppressWarnings("unchecked")
	private InvJIssueDetails setIssueDetail(InvJIssueDetails detail,
			Map mapDetail) {
		// 计划需求明细单id
		detail.setRequirementDetailId(getLong(mapDetail
				.get("requirementDetailId")));
		// 物料id
		detail.setMaterialId(getLong(mapDetail.get("materialId")));
		// 成本分摊项目
		if (!"".equals(mapDetail.get("costItemId"))) {
			detail.setCostItemId(getLong(mapDetail.get("costItemId")));
		}
		// 费用来源
		if (headInfo.getItemCode() != null && !"".equals(headInfo.getItemCode())) {
			detail.setItemCode(headInfo.getItemCode());//modify by ywliu  2009/7/6
		}
		// 申请数量
		detail.setAppliedCount(getDouble(mapDetail.get("appliedCount")));
		// 核准数量
		detail.setApprovedCount(getDouble(mapDetail.get("approvedCount")));
		// 实际数量
		detail.setActIssuedCount(getDouble(mapDetail.get("actIssuedCount")));
		// 企业编码
		detail.setEnterpriseCode(employee.getEnterpriseCode());
		// 上次修改人
		detail.setLastModifiedBy(employee.getWorkerCode());
		// 是否使用
		detail.setIsUse("Y");
		// 上次修改日期
		detail.setLastModifiedDate(new Date());
		return detail;
	}

	/**
	 * 获取map里面的long型值
	 * 
	 * @return 结果
	 */
	private Long getLong(Object obj) {
		if (obj != null) {
			return Long.parseLong(obj.toString());
		}
		return null;
	}

	/**
	 * 获取map里面的double型值
	 * 
	 * @return 结果
	 */
	private Double getDouble(Object obj) {
		if (obj != null) {
			try {
				return Double.parseDouble(obj.toString());
			} catch (NumberFormatException e) {
				return null;
			}

		}
		return null;
	}

	/**
	 * 获取map里面的long型值
	 * 
	 * @return 结果
	 */
	private String getString(Object obj) {
		if (obj != null) {
			return String.valueOf(obj);
		}
		return null;
	}

	/**
	 * 日期格式化为字符串
	 * 
	 * @param date
	 *            日期
	 * @return date对应的字符串
	 */
	private String DateToString(Date date) {
		SimpleDateFormat defaultFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String sysDate = defaultFormat.format(date);
		return sysDate;
	}

	/**
	 * 取领料单对应的需求计划的计划来源
	 * 
	 * @throws JSONException
	 */
	public void getIssueOrginalId() throws JSONException {
		// 领料单编号ID
		String issueHeadId = request.getParameter("issueHeadId");
		InvJIssueHead head = headRemote.findById(Long.parseLong(issueHeadId));
		if (head.getMrNo() != null && head.getMrNo() != "") {
			MrpJPlanRequirementHead plan = remote.findPlanRequirementHeadModel(
					head.getMrNo(), head.getEnterpriseCode());
			write(plan.getPlanOriginalId().toString());
		} else {
			write("");
		}
	}

	public void getFeeDeptType() throws JSONException {
		// 领料单编号ID
		String issueHeadId = request.getParameter("issueHeadId");
		InvJIssueHead head = headRemote.findById(Long.parseLong(issueHeadId));
		if (head.getFeeByDep() != null && head.getFeeByDep() != "") {
			String deptType = baseRemote.checkDeptType(head.getFeeByDep());
			write(deptType);
		} else {
			write("");
		}
	}

	/**
	 * 获取
	 * 
	 * @return headInfo
	 */
	public InvJIssueHead getHeadInfo() {
		return headInfo;
	}

	/**
	 * 设置
	 * 
	 * @param headInfo
	 */
	public void setHeadInfo(InvJIssueHead headInfo) {
		this.headInfo = headInfo;
	}

	/**
	 * add by slTang
	 * 
	 * @throws JSONException
	 */
	public void saveDetailIssue() throws JSONException {
		String str = request.getParameter("updateDetails");
		List<Map> updateDetails = (List<Map>) JSONUtil.deserialize(str);
		InvJIssueDetails detail = null;
		for (Map map : updateDetails) {
			detail = detailsRemote.findById(Long.parseLong(map.get(
					"issueDetailsId").toString()));
			 //-----add by fyyang 20100112---------
			   if(detail.getApprovedCount()!=Double.parseDouble(map.get("approvedCount").toString()))
			   {
				   SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
				   String modifyInfo="由"+employee.getWorkerName()+"于"+sf.format(new Date())+"将核准数量由"+detail.getApprovedCount()+"改为："+Double.parseDouble(map.get("approvedCount").toString())+";";
			       if(detail.getModifyMemo()!=null)
			       {
			    	   detail.setModifyMemo(detail.getModifyMemo()+modifyInfo);
			       }
			       else
			       {
			    	   detail.setModifyMemo(modifyInfo);
			       }
			   }
			   //----------add end -------------------
			detail.setApprovedCount(Double.parseDouble(map.get("approvedCount")
					.toString()));
			detailsRemote.update(detail);// 修改明细核准数量
		}
	}
	
	/** 
	 *  删除领料单
	 *  add by fyyang 090727
	 */
	
	public void deleteIssueHead()
	{
		String entryId=request.getParameter("entryId");
		String issueId=request.getParameter("issueId");
		if(entryId!=null&&!entryId.equals(""))
		{
		 headRemote.deleteIssueHead(Long.parseLong(entryId),Long.parseLong(issueId));
		}
		else
		{
		headRemote.deleteIssueHead(null,Long.parseLong(issueId));
		}
	}
	
	/**
	 * add by liuyi 091126 发料统计查询
	 * @throws JSONException 
	 */
	public void getSendMaterialsAccout() throws JSONException{
		//按审核时间查询 add by fyyang 091223
		String sdate=request.getParameter("sdate");
		String edate=request.getParameter("edate");
		// 开始行号
		String strStart = request.getParameter("start");
		// 行数
		String strlimit = request.getParameter("limit");
		int start = 0;
		int limit = 0;
		if (strStart != null && strStart.length() > 0) {
			start = Integer.parseInt(strStart);
		}
		if (strlimit != null && strlimit.length() > 0) {
			limit = Integer.parseInt(strlimit);
		}
		
		PageObject pg = new PageObject();
		if(strlimit != null && strlimit != null){
			pg = detailsRemote.getSendMaterialAccoun(sdate,edate,start,limit);
		}else
			pg = detailsRemote.getSendMaterialAccoun(sdate,edate);
		write(JSONUtil.serialize(pg));
	}
	
	
	public void getYearIssuePriceByDept()
	{
		String itemCode=request.getParameter("itemCode");
		String deptCode = request.getParameter("deptCode");
	 String price=	remote.getYearIssuePriceByDept(employee.getEnterpriseCode(),deptCode,itemCode);
	 write(price+"");
	}
	
	/**
	 * add by fyyang 20100408
	 * 根据需求计划单id查询出已经入库的明细
	 * @throws JSONException
	 */
	   public void getMaterialDetailForIssueSelect() throws JSONException {
	        /** 主表流水号 */
	        String headId = request.getParameter("headId");
	        PageObject obj = new PageObject();
	        if (!Constants.BLANK_STRING.equals(headId)) {
	           
	            obj = remote.getMaterialDetailForIssueSelect(employee.getEnterpriseCode(),Long.parseLong(headId));
	           
	            if (null == obj.getList()) {
	                List<Object> list = new ArrayList<Object>();
	                obj.setList(list);
	            }
	          
	            String str = JSONUtil.serialize(obj);
	            
	            if (Constants.BLANK_STRING.equals(str) || null == str) {
	                str = "{\"list\":[],\"totalCount\":null}";
	            }
	            
	            write(str);
	        } else {
	            
	            write("false");
	        }
	    }
}
