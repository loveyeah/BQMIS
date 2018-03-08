/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.materialMove;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.resource.InvCTransaction;
import power.ejb.resource.InvCTransactionFacadeRemote;
import power.ejb.resource.InvJTransactionHisFacadeRemote;
import power.ejb.resource.form.TransActionPartByCode;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
/**
 * 事务异动明细查询
 * 
 * @author wujiao
 * @version 1.0
 */
public class MaterialMove extends AbstractAction {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 事务类型远程对象 */
	private InvCTransactionFacadeRemote transTypeRemote;	
	/** 事务异动明细查询 */
	private InvJTransactionHisFacadeRemote transHisRemote;
	/**
	 * 构造函数
	 */
	public MaterialMove() {
		transTypeRemote = (InvCTransactionFacadeRemote) factory
		.getFacadeRemote("InvCTransactionFacade");
		transHisRemote = (InvJTransactionHisFacadeRemote) factory
		.getFacadeRemote("InvJTransactionHisFacade");
	}
	/**
	 * 获取事务类型
	 * 
	 * @throws JSONException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void getTransListForCo() throws JSONException, Exception {
		PageObject obj = new PageObject();
		// 添加"所有"选项
		TransActionPartByCode transType = new TransActionPartByCode();
		// 种类为"所有"
		transType.setTransName(Constants.ALL_SELECT);
		transType.setTransCode(Constants.BLANK_STRING);
		// 查找运行类型
		obj = (transTypeRemote.findTransCodeByAll(employee.getEnterpriseCode()));
		// 添加"所有"
		obj.getList().add(0, transType);
		String string = null;
		 if(obj == null) {
			 string = "{\"list\":[],\"totalCount\":0}";
		 } else {
			 string = JSONUtil.serialize(obj);
		 }
		write(string);
	}
	
	/**
	 * 查询出库操作明细  add by drdu 20090514
	 * @throws JSONException
	 */
	public void queryForissuList() throws JSONException
	{
		String materialCode = request.getParameter("materialNo");
		String issueNo = request.getParameter("issueNo");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		
		PageObject obj = transHisRemote.queryForissuList(employee.getEnterpriseCode(), materialCode, issueNo, start,limit);
		String string = null;
		 if(obj == null) {
			 string = "{\"list\":[],\"totalCount\":0}";
		 } else {
			 string = JSONUtil.serialize(obj);
		 }
		write(string);
		
	}
	
	/**
	 * 查询入库操作明细
	 * @throws JSONException
	 */
	public void queryForArrivalList() throws JSONException
	{
		String materialCode = request.getParameter("materialNo");
		String arrivalNo = request.getParameter("arrivalNo");
		String purNo = request.getParameter("purNo");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		PageObject obj = transHisRemote.queryForArrivalList(employee.getEnterpriseCode(), materialCode, arrivalNo,purNo, start,limit);
		
		String string = null;
		 if(obj == null) {
			 string = "{\"list\":[],\"totalCount\":0}";
		 } else {
			 string = JSONUtil.serialize(obj);
		 }
		write(string);
	}
	/**
	 * 查询
	 * 
	 * @throws JSONException
	 * 
	 */
	public void searchMsg() throws JSONException, Exception {
		// 获取前台数据
		PageObject object = new PageObject();
		// 计划开始时间
		String sdate = request.getParameter("sdate");
		// 计划结束时间
		String edate = request.getParameter("edate");
		// 物料id	
		String materialId = request.getParameter("materialId");
		// 事务类型
		String transType = request.getParameter("transType");
		// 分页信息
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		
        object = transHisRemote.queryForMaterialMove(employee.getEnterpriseCode(), sdate, edate, materialId, transType, start,limit);
		
		// 解析字符串
		String string = null;
		 if(object == null) {
			 string = "{\"list\":[],\"totalCount\":0}";
		 } else {
			 string = JSONUtil.serialize(object);
		 }
		write(string);
	}
}
