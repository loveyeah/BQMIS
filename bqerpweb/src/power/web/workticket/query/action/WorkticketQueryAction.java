/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.workticket.query.action;

import java.util.ArrayList;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquCBlock;
import power.ejb.equ.base.EquCBlockFacadeRemote;
import power.ejb.workticket.RunCWorkticketType;
import power.ejb.workticket.RunCWorkticketTypeFacadeRemote;
import power.ejb.workticket.business.BqWorkticketApprove;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.ejb.workticket.business.WorkticketPrint;
import power.ejb.workticket.form.WorkticketBusiStatus;
import power.ejb.workticket.form.WorkticketCountForm;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 工作票查询Action
 * 
 * @author jincong
 * @version 1.0
 */
public class WorkticketQueryAction extends AbstractAction { 
	private static final long serialVersionUID = 1L;   
	/**
	 * 工作票类型的取得
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getQueryWorkticketType() throws JSONException {
		// 根据企业编码取得所有工作票类型 
		RunCWorkticketTypeFacadeRemote runCTypeRemote = (RunCWorkticketTypeFacadeRemote)
				factory.getFacadeRemote("RunCWorkticketTypeFacade");
		PageObject object = runCTypeRemote.findAll(
				Constants.ENTERPRISE_CODE);
		// 查询结果为null
		if(object == null) {
			object.setList(new ArrayList<RunCWorkticketType>());
		}
		// 添加"所有"选项
		RunCWorkticketType runCWorkticketType = new RunCWorkticketType();
		// 种类为"所有"
		runCWorkticketType.setWorkticketTypeName(Constants.ALL_SELECT);
		// 编码为空
		runCWorkticketType.setWorkticketTypeCode(Constants.BLANK_STRING);
		// 添加
		object.getList().add(0, runCWorkticketType);
		write(JSONUtil.serialize(object));
	}
	
	/**
	 * 所属机组或系统的取得
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getQueryEquBlock() throws JSONException {
		/** 所属机组或系统远程接口 */
		 EquCBlockFacadeRemote equCBlockRemote = (EquCBlockFacadeRemote)
				factory.getFacadeRemote("EquCBlockFacade");
		// 根据企业编码取得所遇机组或系统
		PageObject object = equCBlockRemote.findEquList(
				Constants.ALL_DATA, Constants.ENTERPRISE_CODE);
		// 查询结果为null
		if(object == null) {
			object.setList(new ArrayList<EquCBlock>());
		}
		// 添加"所有"选项
		EquCBlock equCBlock = new EquCBlock();
		// 种类为"所有"
		equCBlock.setBlockName(Constants.ALL_SELECT);
		// 编码为空
		equCBlock.setBlockCode(Constants.BLANK_STRING);
		// 添加
		object.getList().add(0, equCBlock);
		write(JSONUtil.serialize(object));
	}
	
	/**
	 * 工作票状态的取得
	 * 
	 * @throws JSONException
	 */
	public void getQueryWorkticketStatus() throws JSONException {
		/** 工作票状态远程接口 */
	    BqWorkticketApprove  workticketApprove = (BqWorkticketApprove)
				factory.getFacadeRemote("BqWorkticketApproveImpl"); 
		// 取得工作票状态List
		List<WorkticketBusiStatus> list =
			workticketApprove.findBusiStatusList();
		// 查询结果为null
		if(list == null) {
			list = new ArrayList<WorkticketBusiStatus>();
		}
		// 添加"所有"选项
		WorkticketBusiStatus workticketBusiStatus = new WorkticketBusiStatus();
		// 种类为"所有"
		workticketBusiStatus.setWorkticketStatusName(Constants.ALL_SELECT);
		// Id为null
		workticketBusiStatus.setWorkticketStausId(null);
		// 添加
		list.add(0, workticketBusiStatus);
		// 转化为PageObject
	    PageObject object = new PageObject();
	    // 设置list
		object.setList(list);
		write(JSONUtil.serialize(object));
	}
	
	/**
	 * 工作票查询
	 * 
	 * @throws JSONException
	 */
	public void getQueryWorkticketList() throws JSONException {
		// 取得查询参数: 开始时间
		String strStartDate = request.getParameter("startDate");
		// 取得查询参数: 结束时间
		String strEndDate = request.getParameter("endDate");
		// 取得查询参数: 工作票种类
		String strWorkticketTypeCode = request.getParameter("workticketTypeCode");
		// 取得查询参数: 工作票状态
		String strWorkticketStatusId = request.getParameter("workticketStatusId");
		// 取得查询参数: 所属机组或系统
		String strBlock = request.getParameter("block");
		// 取得查询参数: 开始行
		int intStart = Integer.parseInt(request.getParameter("start"));
		// 取得查询参数: 结束行
		int intLimit = Integer.parseInt(request.getParameter("limit"));
		//add by fyyang 090423 检修专业
		String repairCode=request.getParameter("repairC");
		// add by fyyang 090423 所属班组
		String deptId=request.getParameter("deptId");
		// add by fyyang 090423 检修专业
		String content=request.getParameter("content");
		//工作票编号
		String wticketNo = request.getParameter("wticketNo");
		
		//String wticketNo ="";
		 RunJWorkticketsFacadeRemote runJRemote  = (RunJWorkticketsFacadeRemote)
					factory.getFacadeRemote("RunJWorkticketsFacade");
		// 根据查询条件，取得工作票
		 PageObject object = runJRemote.getWorkticketMainList(Constants.ENTERPRISE_CODE,
				 strStartDate, strEndDate, strWorkticketTypeCode, strWorkticketStatusId,
				 strBlock,repairCode,deptId,content,wticketNo, intStart, intLimit);
		 // 输出结果
		 String strOutput = "";
		 // 查询结果为null,设置页面显示
		 if(object == null || object.getList() == null) {
			 strOutput = "{\"list\":[],\"totalCount\":0}";
		 // 不为null
		 } else {
			 strOutput = JSONUtil.serialize(object);
		 }
		 write(strOutput);
	}
	/**
	 * 工作票各种状态统计一览表
	 */
	public void getStatusStatData() {
		try {
			WorkticketPrint workticketApprove = (WorkticketPrint) factory
					.getFacadeRemote("WorkticketPrintImp");
			String date = request.getParameter("date");
			String depts = request.getParameter("dept");
			String stop = request.getParameter("stop");
			
			List<WorkticketCountForm> list = workticketApprove
					.getStatusDataOfDept(stop,employee.getEnterpriseCode(), date,
							depts);
			write(JSONUtil.serialize(list));
		} catch (Exception exc) {
			write("[]");
		}
	}
	/**
	 * 工作票部门合格率统计
	 */
	public void getRateStatData()
	{
		try {
			WorkticketPrint workticketApprove = (WorkticketPrint) factory
					.getFacadeRemote("WorkticketPrintImp");
			String date = request.getParameter("date");
			String depts = request.getParameter("dept");
			String stop = request.getParameter("stop");
			List<WorkticketCountForm> list = workticketApprove
					.getRateDataOfDept(stop,employee.getEnterpriseCode(), date,depts);
			write(JSONUtil.serialize(list));
		} catch (Exception exc) {
			write("[]");
		}
	}
}
