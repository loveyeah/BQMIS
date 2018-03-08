/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.workticket.contentkey.action;

import java.util.List;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketContentKey;
import power.ejb.workticket.RunCWorkticketContentKeyFacadeRemote;
import power.ejb.workticket.RunCWorkticketType;
import power.ejb.workticket.RunCWorkticketTypeFacadeRemote;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 工作票内容关键词维护
 * 
 * @author pbao
 * @version 1.0
 */
public class ContentKeyMaintenanceAction extends AbstractAction {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 工作票内容关键词远程对象 */
	private RunCWorkticketContentKeyFacadeRemote remote;
	/** 工作票类型远程对象 */
	private RunCWorkticketTypeFacadeRemote workticketTypeCodeRemote;
	/** 工作票内容关键词 */
	private RunCWorkticketContentKey contentKey;

	/**
	 * 构造函数
	 */
	public ContentKeyMaintenanceAction() {
		remote = (RunCWorkticketContentKeyFacadeRemote) factory
		    .getFacadeRemote("RunCWorkticketContentKeyFacade");
		
		workticketTypeCodeRemote = (RunCWorkticketTypeFacadeRemote) factory
	    .getFacadeRemote("RunCWorkticketTypeFacade");
	}

	/**
	 * @return the contentKey
	 */
	public RunCWorkticketContentKey getContentKey() {
		return contentKey;
	}

	/**
	 * @param contentKey
	 *            the contentKey to set
	 */
	public void setContentKey(RunCWorkticketContentKey contentKey) {
		this.contentKey = contentKey;
	}

	/**
	 * 获取工作票类型
	 * 
	 * @throws JSONException
	 */
	public void getTicketTypeCodeList() throws JSONException {

		PageObject obj = new PageObject();

		obj = workticketTypeCodeRemote.findAll(Constants.ENTERPRISE_CODE);
		List<RunCWorkticketType> list = obj.getList();
		RunCWorkticketType opt = new RunCWorkticketType();
		// 显示为“公用”
		opt.setWorkticketTypeName("公用");
		// value为空值
		opt.setWorkticketTypeCode("C");
		// 添加到list
		list.add(0, opt);
		// 添加到obj
		obj.setList(list);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 获得工作票内容关键词列表
	 * 
	 * @throws JSONException
	 */
	public void getContentKeyList() throws JSONException {

		// 取得查询参数-工作票类型
		String workTicketTypeCode = (String) request.getParameter("typeCode");
		// 取得参数-前置后置标识
		String keyType = (String) request.getParameter("keyType");
		// 取得参数-起始条数
	    Object objstart=request.getParameter("start");
	    // 取得参数-条数
	    Object objlimit=request.getParameter("limit");
	    PageObject obj = new PageObject();
	    if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findAllWithComm(workTicketTypeCode, Constants.ENTERPRISE_CODE, keyType, start, limit);
		} else {
			obj = remote.findAllWithComm(workTicketTypeCode, Constants.ENTERPRISE_CODE, keyType);
		}
	    
		String str = JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 * 增加工作票内容关键词
	 */
	public void addContentKey() {
		// 企业编码
		contentKey.setEnterpriseCode(Constants.ENTERPRISE_CODE);
		// 填写人
		contentKey.setModifyBy(employee.getWorkerCode());
		try {
			remote.save(contentKey);
			// 显示成功信息
			write(Constants.ADD_SUCCESS);
		} catch (CodeRepeatException ce) {
			// 显示错误信息
			write(Constants.ADD_FAILURE);
		}
	}
	
	/**
	 * 修改工作票内容关键词
	 */
	public void updateContentKey() {
		RunCWorkticketContentKey model = remote.findById(contentKey.getContentKeyId());
		// ID
		model.setContentKeyId(contentKey.getContentKeyId());
		// 关键词名称
		model.setContentKeyName(contentKey.getContentKeyName());
		// 显示顺序
		model.setOrderBy(contentKey.getOrderBy());
		// 工作票类型编码
		model.setWorkticketTypeCode(contentKey.getWorkticketTypeCode());
		// 填写人
		model.setModifyBy(employee.getWorkerCode());
		try {
			remote.update(model);
			write(Constants.MODIFY_SUCCESS);
		} catch (CodeRepeatException ce) {
			write(Constants.MODIFY_FAILURE);
		}
	}
	
	/**
	 * 删除工作票内容关键词
	 */
	public void deleteContentKey() {
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		// 显示删除成功信息
		write(Constants.DELETE_SUCCESS);
	}
}
