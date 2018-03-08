/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.workticket.workticketpattern.action;

import java.util.Date;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketType;
import power.ejb.workticket.RunCWorkticketTypeFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 工作票维护Action
 * 
 * @author fangjihu
 * 
 */
public class WorkTicketAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 工作票 */
	private RunCWorkticketType bean;
	/** 工作票远程对象 */
	protected RunCWorkticketTypeFacadeRemote remote;
	/** 工作票类型id */
	private String workticketTypeId;

	/**
	 * 构造函数
	 */
	public WorkTicketAction() {
		/** 工作票远程对象 */
		remote = (RunCWorkticketTypeFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketTypeFacade");
	}

	/**
	 * 查询工作票
	 * 
	 * @throws JSONException
	 */
	public void getWorkTicket() throws JSONException {
		PageObject obj = new PageObject();
		// 企业编码
		obj = remote.findAll(Constants.ENTERPRISE_CODE);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 添加工作票
	 * 
	 * @throws CodeRepeatException
	 */
	public void addWorkTicket() throws CodeRepeatException {
		try {
			// 企业编码
			bean.setEnterpriseCode(Constants.ENTERPRISE_CODE);
			// 设定修改时间
			bean.setModifyDate(new Date());
			// 设定修改者
			bean.setModifyBy(employee.getWorkerCode());
			// 添加
			RunCWorkticketType type = remote.save(bean);
			if (type != null) {
				write(Constants.ADD_SUCCESS);
			} else {
				write(Constants.ADD_CODE_FAILURE);
			}
		} catch (CodeRepeatException ce) {
			write(Constants.ADD_CODE_FAILURE);
			throw (ce);
		}
	}

	/**
	 * 更新工作票
	 * 
	 * @throws CodeRepeatException
	 */
	public void updateWorkTicket() throws CodeRepeatException {
		try {
			RunCWorkticketType workticket = remote.findById(Long
					.parseLong(getWorkticketTypeId()));
			// 设定更新时间
			workticket.setModifyDate(new Date());
			// 企业编码
			workticket.setEnterpriseCode(Constants.ENTERPRISE_CODE);
			// 更新者
			workticket.setModifyBy(employee.getWorkerCode());
			// 工作票类型编码
			workticket.setWorkticketTypeCode(bean.getWorkticketTypeCode());
			// 工作票类型名称
			workticket.setWorkticketTypeName(bean.getWorkticketTypeName());
			// 更新
			RunCWorkticketType type = remote.update(workticket);
			if (type != null) {
				write(Constants.MODIFY_SUCCESS);
			} else {
				write(Constants.MODIFY_CODE_FAILURE);
			}
		} catch (CodeRepeatException ce) {
			write(Constants.MODIFY_CODE_FAILURE);
			throw (ce);
		}
	}

	/**
	 * 删除工作票
	 * 
	 * @throws CodeRepeatException
	 * @throws NumberFormatException
	 */
	public void deleteWorkTicket() throws NumberFormatException,
			CodeRepeatException {
		// 获取要删去工作票类型id
		String strIds = request.getParameter("ids");
		String[] strUnitIds = strIds.split(",");
		if (strUnitIds.length == 1) {
			remote.delete(Long.parseLong(strIds));
		} else {
			remote.deleteMulti(strIds);
		}
		write(Constants.DELETE_SUCCESS);
	}

	/**
	 * 工作票
	 * 
	 * @return 工作票
	 */
	public RunCWorkticketType getBean() {
		return bean;
	}

	/**
	 * 工作票
	 * 
	 * @param bean
	 *            工作票
	 */
	public void setBean(RunCWorkticketType bean) {
		this.bean = bean;
	}

	/**
	 * 工作票类型id
	 * 
	 * @return 工作票类型id
	 */
	public String getWorkticketTypeId() {
		return workticketTypeId;
	}

	/**
	 * 工作票类型id
	 * 
	 * @param workticketTypeId
	 *            工作票类型id
	 */
	public void setWorkticketTypeId(String workticketTypeId) {
		this.workticketTypeId = workticketTypeId;

	}
}
