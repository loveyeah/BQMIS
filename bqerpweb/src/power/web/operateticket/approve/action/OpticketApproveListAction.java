/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.operateticket.approve.action;

import power.ear.comm.ejb.PageObject;
import power.ejb.opticket.RunCOpticketTask;
import power.ejb.opticket.RunCOpticketTaskFacadeRemote;
import power.ejb.opticket.RunJOpticketFacadeRemote;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 工作票审批列表Action
 * @author zhengzhipeng
 * @version 1.0
 */

public class OpticketApproveListAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
 
	/** 操作票类型远程接口 */
	private RunCOpticketTaskFacadeRemote opticketTypeRemote;
	/** 操作票专业远程接口 */
	private RunCSpecialsFacadeRemote opticketSpecialityRemote;
	/** 操作票查询远程接口 */
	private RunJOpticketFacadeRemote queryRemote;
	
	/**
	 * 构造函数
	 */
	public OpticketApproveListAction() {
		// 远程处理对象的取得
		queryRemote = (RunJOpticketFacadeRemote) factory.getFacadeRemote("RunJOpticketFacade");
		opticketTypeRemote = (RunCOpticketTaskFacadeRemote) factory.getFacadeRemote("RunCOpticketTaskFacade");
	    opticketSpecialityRemote = (RunCSpecialsFacadeRemote) factory.getFacadeRemote("RunCSpecialsFacade");
	}
	/**
	 * 操作票类型
	 */
	@SuppressWarnings("unchecked")
	public void getOpticketType() throws JSONException {
		// 根据企业编码取得所有操作票类型
		PageObject obj = new PageObject();
        obj.setList(opticketTypeRemote.findByOTaskCodeLength(Constants.ENTERPRISE_CODE));
        RunCOpticketTask addToList = new RunCOpticketTask();
        addToList.setOperateTaskName("所有");
        addToList.setOperateTaskCode("");
        obj.getList().add(0, addToList);
        String str=JSONUtil.serialize(obj);
        write(str);		
	}
	/**
	 * 操作票专业
	 */
	@SuppressWarnings("unchecked")
	public void getOpticketSpeciality() throws JSONException {
		// 根据企业编码取得所有操作票专业
		PageObject obj = new PageObject();
		obj.setList(opticketSpecialityRemote.findSpeList(Constants.ENTERPRISE_CODE));
		RunCSpecials addToList = new RunCSpecials();
		addToList.setSpecialityName("所有");
		addToList.setSpecialityCode("");
		obj.getList().add(0, addToList);
		String str=JSONUtil.serialize(obj);
        write(str);
	}
	/**
	 * 操作票查询
	 */
	public void getOpticketApproveList() throws JSONException {
		// 取得查询参数: 开始时间
		String startDate = request.getParameter("startDate");
		// 取得查询参数: 结束时间
		String endDate = request.getParameter("endDate");
		// 取得查询参数: 操作票类型
		String opticketType = request.getParameter("opticketType");
		// 取得查询参数: 操作票专业
		String opticketSpeciality = request.getParameter("opticketSpeciality");
		String isStandar = request.getParameter("isStandar");
		// 取得查询参数: 开始行
		int intStart = Integer.parseInt(request.getParameter("start"));
		// 取得查询参数: 结束行
		int intLimit = Integer.parseInt(request.getParameter("limit"));	
		String taskName=request.getParameter("optaskName");
		String taskNo=request.getParameter("optaskNo");
		// 根据查询条件，取得操作票
		PageObject object = queryRemote.getOpticketApproveList(employee.getWorkerCode(),Constants.ENTERPRISE_CODE, startDate,
				endDate, opticketType, opticketSpeciality, null, isStandar,taskName,taskNo,intStart,intLimit);
		
		// 查询结果为null,设置页面显示
		 if(object == null) {
			 String str = "{\"list\":[],\"totalCount\":0}";
			 write(str);
		 // 不为null
		 } else
		  {
			 if(object.getList() == null) {
				 String str = "{\"list\":[],\"totalCount\":0}";
				 write(str);
			 }
			 String str = JSONUtil.serialize(object);
			 write(str);
		 }
	}
}
