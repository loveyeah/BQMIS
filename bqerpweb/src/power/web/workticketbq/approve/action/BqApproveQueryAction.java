package power.web.workticketbq.approve.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquCBlock;
import power.ejb.equ.base.EquCBlockFacadeRemote;
import power.ejb.workticket.RunCWorkticketType;
import power.ejb.workticket.RunCWorkticketTypeFacadeRemote;
import power.ejb.workticket.business.BqWorkticketApprove;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.ejb.workticket.form.WorkticketBusiStatus;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

public class BqApproveQueryAction extends AbstractAction{

	/** 定义null */
	private static final String NULL = "null";
	/** 查询远程对象 */
	private RunJWorkticketsFacadeRemote queryRmote;
	/** 机组或系统远程对象 */
	private EquCBlockFacadeRemote systemRemote;

	/** 工作票类型远程对象 */
	private RunCWorkticketTypeFacadeRemote workticketTypeCodeRemote;
	
	private BqWorkticketApprove approve;
	WorkflowService service;
	
	/**
	 * 构造函数
	 */
	public BqApproveQueryAction() {
		workticketTypeCodeRemote = (RunCWorkticketTypeFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketTypeFacade");
		systemRemote = (EquCBlockFacadeRemote) factory
				.getFacadeRemote("EquCBlockFacade");
	
		queryRmote = (RunJWorkticketsFacadeRemote) factory
				.getFacadeRemote("RunJWorkticketsFacade");
		approve=(BqWorkticketApprove)factory.getFacadeRemote("BqWorkticketApproveImpl");
		service = new WorkflowServiceImpl();
	}
	
	/**
	 * 获取工作票类型
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getWorkticketTypeList() throws JSONException {
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
	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("unchecked")
	public void getApproveStatusList() throws JSONException {
		PageObject obj = new PageObject();
		List<WorkticketBusiStatus> list=approve.findBusiStatusForApprove();
		obj.setList(list);
		WorkticketBusiStatus workticketBusiStatus = new WorkticketBusiStatus();
		workticketBusiStatus.setWorkticketStatusName("所有");
		
		obj.getList().add(0, workticketBusiStatus);
		String str = JSONUtil.serialize(obj);
		write(str);
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
		String repairCode="";
		String deptId="";
		String content="";
		String wticketNo="";
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
		//add by fyyang 090423 检修专业  所属班组  工作内容
		Object repairC=request.getParameter("repairC");
		Object dept=request.getParameter("deptId");
		Object objcontent=request.getParameter("content");
		Object objwticketNo=request.getParameter("wticketNo");
		if(repairC!=null)
		{
			repairCode=repairC.toString();
		}
		if(dept!=null)
		{
			deptId=dept.toString();
		}
		if(objcontent!=null)
		{
			content=objcontent.toString();
		}
		if(objwticketNo!=null)
		{
			wticketNo=objwticketNo.toString();
		}
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
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(new String[] {
				"bqWorkticket1", "bqWorkticket2","bqWorkticketRJ1", "bqWorkticketDH1","bqWorkticketDH2","bqWorkticketRK1","bqWorkticketRJ2","bqWorkticketRK2"}, employee.getWorkerCode());
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = queryRmote.getWorkticketApproveList(enterpriseCode, strStart,
					strEnd, strType, strState, strSystem,entryIds,repairCode,deptId,content,wticketNo,start, limit);
		} else {
			obj = queryRmote.getWorkticketApproveList(enterpriseCode, strStart,
					strEnd, strType, strState, strSystem,entryIds,repairCode,deptId,content,wticketNo);
		}
		String str = JSONUtil.serialize(obj);
		if (NULL.equals(str)) {
			str = "{\"list\":[],\"totalCount\":null}";
		}
		write(str);
	}
}
