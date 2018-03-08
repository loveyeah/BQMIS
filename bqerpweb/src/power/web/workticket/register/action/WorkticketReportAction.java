/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.workticket.register.action;

import java.util.ArrayList;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquCBlock;
import power.ejb.equ.base.EquCBlockFacadeRemote;
import power.ejb.workticket.RunCWorkticketType;
import power.ejb.workticket.RunCWorkticketTypeFacadeRemote;
import power.ejb.workticket.business.BqWorkticketApprove;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote; 
import power.ejb.workticket.business.WorkticketManager;
import power.ejb.workticket.form.WorkticketBusiStatus;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

/**
 * 工作票上报列表Action
 * 
 * @author zhujie
 * 
 */
public class WorkticketReportAction extends AbstractAction {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 查找所属机组或系统用 远程服务 */
	protected EquCBlockFacadeRemote equBlockRemote;
	/** 查找Grid信息用 远程服务 */
	protected RunJWorkticketsFacadeRemote workticketsRemote;
	/** 删除用 远程服务 */
	protected WorkticketManager managerRemote;
	/** 上报用 远程服务 */
	protected BqWorkticketApprove reportRemote;
	/** 查找工作票类型用 远程服务 */
	protected RunCWorkticketTypeFacadeRemote workticketsTyperemote;
	/** 机组编码或名称 */
	private String fuzzy = Constants.ALL_DATA;
	/** 计划开始时间 */
	private String sdate;
	/** 计划结束时间 */
	private String edate;
	/** 工作票种类 */
	private String workticketTypeCode;
	/** 工作票状态ID */
	private String workticketStausId;
	/** 所属机组或系统 */
	private String equAttributeCode;
	/** 需要处理的工作票号 */
	private String workticketNo;

	/**
	 * 构造函数，初始化remote
	 */
	public WorkticketReportAction() {
		equBlockRemote = (EquCBlockFacadeRemote) factory
				.getFacadeRemote("EquCBlockFacade");
		workticketsRemote = (RunJWorkticketsFacadeRemote) factory
				.getFacadeRemote("RunJWorkticketsFacade");
		managerRemote = (WorkticketManager) factory
				.getFacadeRemote("WorkticketManagerImpl");
		reportRemote = (BqWorkticketApprove) factory
				.getFacadeRemote("BqWorkticketApproveImpl");
		workticketsTyperemote = (RunCWorkticketTypeFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketTypeFacade");
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 */
	public void getWorkticketApproveList() throws Exception {
		//add by fyyang 090110 是否标准票
		String isStandard=request.getParameter("isStandard");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if(workticketStausId==null||workticketStausId.equals(""))
		{
			//上报列表所有只查未上报和已退回的票
			if(isStandard.equals("Y"))
			{
				workticketStausId="1,5";
			}
			else
			{
			workticketStausId="1,9";
			}
		}
//		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
//		String entryIds = workflowService.getAvailableWorkflow(new String[] {
//				"workticket1", "workticket2","workticketrljx", "workticketdh","workticketrk" }, employee.getWorkerCode());
		
		PageObject result = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			result = workticketsRemote.getWorkticketReportList(
					Constants.ENTERPRISE_CODE, sdate, edate,
					workticketTypeCode, workticketStausId, equAttributeCode,isStandard, employee.getWorkerCode(),
					start, limit);
		} else {
			result = workticketsRemote.getWorkticketReportList(
					Constants.ENTERPRISE_CODE, sdate, edate,
					workticketTypeCode, workticketStausId, equAttributeCode,isStandard,employee.getWorkerCode());
		}
		if (result == null) {
			String string = "{\"list\":[],\"totalCount\":0}";
			write(string);
		} else {
			String string = JSONUtil.serialize(result);
			write(string);
		}
	}

	/**
	 * 删除
	 */
	public void deleteWorkticketNo() {
		managerRemote.deleteWorkticket(workticketNo);
	}

	/**
	 * 上报
	 */
	public void reportWorkticketNo() {
		
		String actionId=request.getParameter("actionId");
		String  approveText=request.getParameter("approveText");
		String workflowType=request.getParameter("flowCode");
		String nextRoles=request.getParameter("nextRoles");
		String eventIdentify=request.getParameter("eventIdentify");
	
		
			//灞桥上报
			
			BqWorkticketApprove bqapprove=(BqWorkticketApprove)factory.getFacadeRemote("BqWorkticketApproveImpl");
			bqapprove.reportTo(workticketNo, workflowType,employee.getWorkerCode(),Long.parseLong(actionId), approveText,nextRoles,eventIdentify);
		
	
		 write("{success:true,msg:'&nbsp&nbsp&nbsp上报成功&nbsp&nbsp&nbsp'}");
	}

	/**
	 * 获得机组编码或名称
	 */
	@SuppressWarnings("unchecked")
	public void getEquBlock() throws Exception {
		PageObject result = equBlockRemote.findEquList(fuzzy,
				Constants.ENTERPRISE_CODE);
		// 给List增加{"所有"，""}这样一条数据
		List<EquCBlock> list = result.getList();
		EquCBlock obj = new EquCBlock();
		obj.setBlockName(Constants.ALL_SELECT);
		obj.setBlockCode(Constants.BLANK_STRING);
		list.add(0, obj);
		result.setTotalCount((long) list.size());
		result.setList(list);
		List<EquCBlock> resultList = result.getList();
		write(JSONUtil.serialize(resultList));
	}

	/**
	 * 获得工作票类型
	 */
	@SuppressWarnings("unchecked")
	public void getWorkticketTypeForReport() throws Exception {
		PageObject result = new PageObject();
		result = workticketsTyperemote.findAll(Constants.ENTERPRISE_CODE);
		List<RunCWorkticketType> list = result.getList();
		RunCWorkticketType obj = new RunCWorkticketType();
		obj.setWorkticketTypeName(Constants.ALL_SELECT);
		obj.setWorkticketTypeCode(Constants.BLANK_STRING);
		list.add(0, obj);
		result.setTotalCount((long) list.size());
		result.setList(list);
		String str = JSONUtil.serialize(result);
		write(str);
	}
	
	/**
	 * 获得工作票上报页面的审批业务状态
	 * add by fyyang 081230
	 * @throws JSONException
	 */
	public void getWorkticketStatusForReport() throws JSONException
	{
	   
//	     // 工作票状态取得远程处理对象
//	    WorkticketApprove   workticketApprove = (WorkticketApprove) factory
//                .getFacadeRemote("WorkticketApproveImpl");
        // 取得工作票状态List
        List<WorkticketBusiStatus> list = reportRemote.findBusiStatuForReport();
     
        // 结果为null
        if (list == null) {
            list = new ArrayList<WorkticketBusiStatus>();
        }
        // 添加"所有"选项
        WorkticketBusiStatus workticketBusiStatus = new WorkticketBusiStatus();
        // 种类为"所有"
        workticketBusiStatus.setWorkticketStatusName("所有");
        // 编码为空
        workticketBusiStatus.setWorkticketStausId(null);
        // 添加
        list.add(0, workticketBusiStatus);
        // 转化为PageObject
        PageObject object = new PageObject();
        // 设置list
        object.setList(list);
        // 设置长度
        object.setTotalCount(new Long(list.size()));
        write(JSONUtil.serialize(object));
	}

	/**
	 * 获取计划开始时间
	 */
	public String getSdate() {
		return sdate;
	}

	/**
	 * 设置计划开始时间
	 * 
	 * @param sdate
	 *            计划开始时间
	 */
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	/**
	 * 获取计划结束时间
	 */
	public String getEdate() {
		return edate;
	}

	/**
	 * 设置计划结束时间
	 * 
	 * @param edate
	 *            计划结束时间
	 */
	public void setEdate(String edate) {
		this.edate = edate;
	}

	/**
	 * 获取工作票种类
	 */
	public String getWorkticketTypeCode() {
		return workticketTypeCode;
	}

	/**
	 * 设置工作票种类
	 * 
	 * @param workticketTypeCode
	 *            工作票种类
	 */
	public void setWorkticketTypeCode(String workticketTypeCode) {
		this.workticketTypeCode = workticketTypeCode;
	}

	/**
	 * 获取工作票状态ID
	 */
	public String getWorkticketStausId() {
		return workticketStausId;
	}

	/**
	 * 设置工作票状态ID
	 * 
	 * @param workticketStausId
	 *            工作票状态ID
	 */
	public void setWorkticketStausId(String workticketStausId) {
		this.workticketStausId = workticketStausId;
	}

	/**
	 * 获取工作票状态ID
	 */
	public String getEquAttributeCode() {
		return equAttributeCode;
	}

	/**
	 * 设置工作票状态ID
	 * 
	 * @param equAttributeCode
	 *            工作票状态ID
	 */
	public void setEquAttributeCode(String equAttributeCode) {
		this.equAttributeCode = equAttributeCode;
	}

	/**
	 * 获取需要处理的工作票号
	 */
	public String getWorkticketNo() {
		return workticketNo;
	}

	/**
	 * 设置需要处理的工作票号
	 * 
	 * @param workticketNo
	 *            接收专业
	 */
	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}
}
