package power.web.standardworkticket.approve.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.engineassistant.po.WorkflowActivity;
import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquCBlock;
import power.ejb.equ.base.EquCBlockFacadeRemote;
import power.ejb.workticket.RunCWorkticketType;
import power.ejb.workticket.RunCWorkticketTypeFacadeRemote;
import power.ejb.workticket.business.RunJWorktickets;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.ejb.workticket.business.StandardTicketApprove;
import power.ejb.workticket.form.WorkticketBusiStatus;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 工作票审批查询
 * @author fyyang
 *
 */
@SuppressWarnings("serial")
public class ApproveQueryAction extends AbstractAction{

	/** 工作票类型 */
	private RunCWorkticketTypeFacadeRemote workticketTypeCodeRemote;
	/** 机组或系统 */
	private EquCBlockFacadeRemote systemRemote;
	/** 查询 */
	private RunJWorkticketsFacadeRemote queryRmote;
	
	private StandardTicketApprove approveRemote;
	WorkflowService service;
	/** 定义null */
	private static final String NULL = "null";
	public ApproveQueryAction()
	{
		workticketTypeCodeRemote = (RunCWorkticketTypeFacadeRemote) factory
		.getFacadeRemote("RunCWorkticketTypeFacade");
         systemRemote = (EquCBlockFacadeRemote) factory
		.getFacadeRemote("EquCBlockFacade");
         queryRmote = (RunJWorkticketsFacadeRemote) factory
			.getFacadeRemote("RunJWorkticketsFacade");
         approveRemote=(StandardTicketApprove)factory.getFacadeRemote("StandardTicketApproveImpl");
         service = new WorkflowServiceImpl();
	}
	
	
	/**
	 * 获取工作票类型
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getWorkTypeList() throws JSONException {
		String enterpriseCode = Constants.ENTERPRISE_CODE;
		PageObject obj = new PageObject();
		// 添加"所有"选项
		RunCWorkticketType runCWorkticketType = new RunCWorkticketType();
		// 种类为"所有"
		runCWorkticketType.setWorkticketTypeName("所有");
		// 添加
		obj = workticketTypeCodeRemote.findAll(enterpriseCode);
		obj.getList().add(0, runCWorkticketType);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 获取机组或系统类型
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getSystemList() throws JSONException {
		String enterpriseCode = Constants.ENTERPRISE_CODE;
		PageObject obj = new PageObject();
		// 添加"所有"选项
		EquCBlock equCBlock = new EquCBlock();
		// 种类为"所有"
		equCBlock.setBlockName("所有");
		obj = systemRemote.findEquList(Constants.ALL_DATA, enterpriseCode);
		// 添加
		obj.getList().add(0, equCBlock);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	
	/**
	 * 获得审批状态
	 * @throws JSONException
	 */
	public void getTypeForApprove() throws JSONException
	{
		
		List<WorkticketBusiStatus> list=approveRemote.findBusiStatuForApprove();
		WorkticketBusiStatus workticketBusiStatus = new WorkticketBusiStatus();
		workticketBusiStatus.setWorkticketStatusName("所有");
		list.add(0, workticketBusiStatus);
		   PageObject object = new PageObject();
	        // 设置list
	        object.setList(list);
	        // 设置长度
	        object.setTotalCount(new Long(list.size()));
	        write(JSONUtil.serialize(object));
//		String str = JSONUtil.serialize(list);
//		write(str);
	}
	
	public void getTypeForReport() throws JSONException
	{
		List<WorkticketBusiStatus> list=approveRemote.findBusiStatuForReport();
		WorkticketBusiStatus workticketBusiStatus = new WorkticketBusiStatus();
		workticketBusiStatus.setWorkticketStatusName("所有");
		list.add(0, workticketBusiStatus);
		   // 转化为PageObject
        PageObject object = new PageObject();
        // 设置list
        object.setList(list);
        // 设置长度
        object.setTotalCount(new Long(list.size()));
        write(JSONUtil.serialize(object));
//		String str = JSONUtil.serialize(list);
//		write(str);
		
	}
	
	
	public void getApproveUrl() {
		String workticketNo = request.getParameter("workticketNo");

		Long workFlowNo = queryRmote.findById(workticketNo).getWorkFlowNo();
		RunJWorktickets info = queryRmote.findById(workticketNo);

		Long workticketStatusId = info.getWorkticketStausId();

		// service.getNextStep(arg0, arg1)
		String next = "1";
		if (workticketStatusId == 1l) {
			write("{flag:'0',msg:'该工作票未上报'}");
		} else if (workticketStatusId == 8l) {
			write("{flag:'0',msg:'该工作票已结束'}");
		} else if (workticketStatusId == 9l) {
			write("{flag:'0',msg:'该工作票已退票'}");
		} else if (workticketStatusId == 14l) {
			write("{flag:'0',msg:'该工作票已作废'}");
		} else {
			Long firelevelId = info.getFirelevelId();

			String strFirelevelId = "";
			if (firelevelId != null) {
				strFirelevelId = firelevelId.toString();
			}
			WorkflowActivity step = service.getCurrentStep(workFlowNo);
			write("{url:" + "'" + step.getUrl() + "'" + ",next:" + next
					+ ",flag:'" + "1'" + ",firelevelId:'" + strFirelevelId
					+ "'" + "}");
		}
	}
	
	
	
	/**
	 * 查询数据
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getApproveList() throws JSONException {
		// 企业编码
		String enterpriseCode = Constants.ENTERPRISE_CODE;
		// 计划开始时间
		String strStart = "";
		// 计划结束时间
		String strEnd = "";
		// 类型
		String strType = "";
		// 所属机组或系统
		String strSystem = "";
		// 状态
		String strState = "";
		//检修专业
		String repairSpecialCode="";
		// 获取前台数据
		Object sd = request.getParameter("startD");
		Object ed = request.getParameter("endD");
		Object tc = request.getParameter("typeC");
		Object syc = request.getParameter("systemC");
		Object stc = request.getParameter("stateC");
		// 取得查询条件: 开始行
		Object objstart = request.getParameter("start");
		// 取得查询条件：结束行
		Object objlimit = request.getParameter("limit");
		Object repc=request.getParameter("repairC");
		if (sd != null) {
			strStart = sd.toString();
		}
		if (ed != null) {
			strEnd = ed.toString();
		}
		if (tc != null) {
			strType = tc.toString();
		}
		if (syc != null) {
			strSystem = syc.toString();
		}
		if (stc != null) {
			strState = stc.toString();
		}
		if(repc!=null)
		{
			repairSpecialCode=repc.toString();
		}
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(new String[] {
				"standticket"}, employee.getWorkerCode());
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = queryRmote.getStandardTicketApproveList(enterpriseCode, strStart,
					strEnd, strType, strState, strSystem,entryIds,repairSpecialCode, start, limit);
		} else {
			obj = queryRmote.getStandardTicketApproveList(enterpriseCode, strStart,
					strEnd, strType, strState, strSystem,entryIds,repairSpecialCode);
		}
		String str = JSONUtil.serialize(obj);
		if (NULL.equals(str)) {
			str = "{\"list\":[],\"totalCount\":null}";
		}
		write(str);
	}
}
