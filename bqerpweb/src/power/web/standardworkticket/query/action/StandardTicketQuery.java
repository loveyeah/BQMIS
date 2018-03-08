package power.web.standardworkticket.query.action;

import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquCBlockFacadeRemote;
import power.ejb.workticket.RunCWorkticketTypeFacadeRemote;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.ejb.workticket.business.StandardTicketApprove;
import power.ejb.workticket.form.WorkticketBusiStatus;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class StandardTicketQuery extends AbstractAction{
	/** 工作票类型 */
	private RunCWorkticketTypeFacadeRemote workticketTypeCodeRemote;
	/** 机组或系统 */
	private EquCBlockFacadeRemote systemRemote;
	private StandardTicketApprove approveRemote;
	private RunJWorkticketsFacadeRemote runJRemote;
	public StandardTicketQuery()
	{
		workticketTypeCodeRemote = (RunCWorkticketTypeFacadeRemote) factory
		.getFacadeRemote("RunCWorkticketTypeFacade");
         systemRemote = (EquCBlockFacadeRemote) factory
		.getFacadeRemote("EquCBlockFacade");
         approveRemote=(StandardTicketApprove)factory.getFacadeRemote("StandardTicketApproveImpl");
         runJRemote = (RunJWorkticketsFacadeRemote)
			factory.getFacadeRemote("RunJWorkticketsFacade");
	}
	
	/**
	 * 获得审批状态
	 * @throws JSONException
	 */
	public void getTypeForApprove() throws JSONException
	{
		
		List<WorkticketBusiStatus> list=approveRemote.findAllBusiStatusList();
		WorkticketBusiStatus workticketBusiStatus = new WorkticketBusiStatus();
		workticketBusiStatus.setWorkticketStatusName("所有");
		list.add(0, workticketBusiStatus);
		   PageObject object = new PageObject();
	        // 设置list
	        object.setList(list);
	        // 设置长度
	        object.setTotalCount(new Long(list.size()));
	        write(JSONUtil.serialize(object));
	}
	
	
	/**
	 * 工作票查询
	 * 
	 * @throws JSONException
	 */
	public void getQueryStandticketList() throws JSONException {
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
		String repairSpecailCode=request.getParameter("repairC");
		
		String newOrOld = request.getParameter("newOrOld");
		//填写人 add by fyyang 090513
		String entryBy=request.getParameter("entryBy");
		//工作内容 add by fyyang 090513
		String workticketContent=request.getParameter("content");
		//
		String workticketNo=request.getParameter("ticketNo");
		// 根据查询条件，取得工作票
		 PageObject object = runJRemote.queryStandTicketList(employee.getEnterpriseCode(),newOrOld,
				 strStartDate, strEndDate, strWorkticketTypeCode, strWorkticketStatusId,
				 strBlock,repairSpecailCode, entryBy,workticketContent,workticketNo, intStart, intLimit);
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
	

	
	
}
