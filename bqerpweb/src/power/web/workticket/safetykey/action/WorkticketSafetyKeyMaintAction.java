/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.workticket.safetykey.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

import power.ejb.workticket.RunCWorkticketSafetyKey;
import power.ejb.workticket.RunCWorkticketSafetyKeyFacadeRemote;
import power.ejb.workticket.RunCWorkticketTypeFacadeRemote;

/**
 * 工作票安全关键字维护Action
 * 
 * @author wangpeng
 */
public class WorkticketSafetyKeyMaintAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 工作票类型远程处理对象 */
	private RunCWorkticketTypeFacadeRemote remoteTicketType;
	/** 安全关键字内容远程取得对象 */
	private RunCWorkticketSafetyKeyFacadeRemote remoteSafetyKey;
	/** 安全关键词对象 */
	private RunCWorkticketSafetyKey entity;

	/**
	 * 构造函数
	 */
	public WorkticketSafetyKeyMaintAction() {

		// 工作票类型远程处理对象
		remoteTicketType = (RunCWorkticketTypeFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketTypeFacade");
		// 安全关键字内容远程取得对象
		remoteSafetyKey = (RunCWorkticketSafetyKeyFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketSafetyKeyFacade");
	}

	/**
	 * 获取工作票类型
	 * 
	 * @throws JSONException
	 */
	public void getTicketKeywordList() throws JSONException {

		PageObject obj = new PageObject();
		obj = remoteTicketType.findAll(Constants.ENTERPRISE_CODE);
		// 记录
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 获取安全关键词
	 * 
	 * @throws JSONException
	 */
	public void getDataList() throws JSONException {

		PageObject obj = new PageObject();
		// 工作票类型
		String workticketTypeCode = (String) request.getParameter("typeCode");
		// 工作票安全关键词类型
		String keyType = (String) request.getParameter("keyType");
		// 开始行
		Object objstart = request.getParameter("start");
		// 分页行
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remoteSafetyKey.findAllWithComm(Constants.ENTERPRISE_CODE,
					workticketTypeCode, keyType, start, limit);
			String str = JSONUtil.serialize(obj);
			write(str);
		}

	}

	/**
	 * 增加安全关键词
	 * 
	 * @throws CodeRepeatException
	 */
	public void addSafetyKey() throws CodeRepeatException {

		RunCWorkticketSafetyKey safetyKey = new RunCWorkticketSafetyKey();
		// 企业编码
		safetyKey.setEnterpriseCode(Constants.ENTERPRISE_CODE);
		// 关键词名称
		safetyKey.setSafetyKeyName(entity.getSafetyKeyName());
		// 显示顺序
		safetyKey.setOrderBy(entity.getOrderBy());
		// 工作票类型编码
		safetyKey.setWorkticketTypeCode(entity.getWorkticketTypeCode());
		// 工作票安全关键字类型
		safetyKey.setKeyType(entity.getKeyType());
		// 反义词
		safetyKey.setReverseKeyId(entity.getReverseKeyId());
		// 设定修改者
		safetyKey.setModifyBy(employee.getWorkerCode());
		try {
			RunCWorkticketSafetyKey type = remoteSafetyKey.save(safetyKey);
			if (type != null) {
				write(Constants.ADD_SUCCESS);
			}
		} catch (CodeRepeatException ce) {
			write(Constants.ADD_FAILURE);
			throw (ce);
		}
	}

	/**
	 * 修改安全关键词
	 * 
	 * @throws CodeRepeatException
	 */
	public void updateSafetyKey() throws CodeRepeatException {

		RunCWorkticketSafetyKey safetyKey = remoteSafetyKey.findById(entity
				.getSafetyKeyId());
		;
		// 安全关键字
		safetyKey.setSafetyKeyName(entity.getSafetyKeyName());
		// 显示顺序
		if (entity.getOrderBy() != null) {
			safetyKey.setOrderBy(entity.getOrderBy());
		}
		// 反义词ID
		if (entity.getReverseKeyId() != null) {
			safetyKey.setReverseKeyId(entity.getReverseKeyId());
		}
		try {
			RunCWorkticketSafetyKey type = remoteSafetyKey.update(safetyKey);
			if (type != null) {
				write(Constants.MODIFY_SUCCESS);
			}
		} catch (CodeRepeatException ce) {
			write(Constants.MODIFY_FAILURE);
			throw (ce);
		}
	}

	/**
	 * 删除安全关键词
	 */
	public void deleteSafetyKey() {
		String safetyKeyIds = (String) request.getParameter("ids");
		remoteSafetyKey.deleteMulti(safetyKeyIds);
	}

	/**
	 * 查询页面双击后获取反义词
	 * 
	 * @throws JSONException
	 */
	public void getReverseWorkType() throws JSONException {

		// 安全关键词编码
		String safetyKeyId = (String) request.getParameter("id");
		long id = Long.parseLong(safetyKeyId);
		RunCWorkticketSafetyKey obj = new RunCWorkticketSafetyKey();
		obj = remoteSafetyKey.findById(id);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 取得安全关键词对象
	 * 
	 * @return entity
	 */
	public RunCWorkticketSafetyKey getEntity() {
		return entity;
	}

	/**
	 * 设置安全关键词对象
	 * 
	 * @param entity
	 */
	public void setEntity(RunCWorkticketSafetyKey entity) {
		this.entity = entity;
	}

}
