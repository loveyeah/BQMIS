/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.workticket.firelevel.action;

import java.util.ArrayList;
import java.util.List;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketFirelevel;
import power.ejb.workticket.RunCWorkticketFirelevelFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 动火票级别页面Action
 * 
 * @author fangjihu
 * 
 */
public class WorkTicketFireLevelAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 动火票级别编码 */
	private String firelevelId;
	/** 动火票 */
	private RunCWorkticketFirelevel bean;
	/** 动火票级别远程对象 */
	protected RunCWorkticketFirelevelFacadeRemote remote;

	/**
	 * 动火票级别编码
	 * 
	 * @return 动火票级别编码
	 */
	public String getFirelevelId() {
		return firelevelId;
	}

	/**
	 * 动火票级别编码
	 * 
	 * @param firelevelId
	 */
	public void setFirelevelId(String firelevelId) {
		this.firelevelId = firelevelId;
	}

	/**
	 * 
	 * @return 动火票级别
	 */
	public RunCWorkticketFirelevel getBean() {
		return bean;
	}

	/**
	 * 设定动火票级别
	 * 
	 * @param bean
	 *            动火票级别
	 */
	public void setBean(RunCWorkticketFirelevel bean) {
		this.bean = bean;
	}

	/**
	 * 构造函数
	 */
	public WorkTicketFireLevelAction() {
		// 动火票级别远程对象
		remote = (RunCWorkticketFirelevelFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketFirelevelFacade");
	}

	/**
	 * 查询
	 * 
	 * @throws JSONException
	 */
	public void getWorkTicketFireLevel() throws JSONException {
		// 查询值
		String strLevelLike = "";
		Object myobj = request.getParameter("fuzzy");
		if (myobj != null) {
			strLevelLike = myobj.toString();
		}
		List<RunCWorkticketFirelevel> list = new ArrayList<RunCWorkticketFirelevel>();
		list = remote.findByNameOrId(Constants.ENTERPRISE_CODE, strLevelLike);
		PageObject obj = new PageObject();
		obj.setList(list);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 添加
	 * 
	 * @throws CodeRepeatException
	 */
	public void addWorkTicketFireLevel() throws CodeRepeatException {
		try {
			// 企业编码
			bean.setEnterpriseCode(Constants.ENTERPRISE_CODE);
			// 设定修改者
			bean.setModifyBy(employee.getWorkerCode());
			// 添加
			RunCWorkticketFirelevel workTicketFirelevel = remote.save(bean);
			if (workTicketFirelevel != null) {
				write(Constants.ADD_SUCCESS);
			} else {
				write(Constants.ADD_FAILURE);
			}
		} catch (CodeRepeatException ce) {
			write(Constants.ADD_FAILURE);
			throw (ce);
		}
	}

	/**
	 * 更新
	 * 
	 * @throws CodeRepeatException
	 */
	public void updateWorkTicketFireLevel() throws CodeRepeatException {
		try {
			RunCWorkticketFirelevel firelevel = remote.findById(Long
					.parseLong(getFirelevelId()));
			// 更新者
			firelevel.setModifyBy(employee.getWorkerCode());
			// 动火票级别编码
			firelevel.setFirelevelId(Long.parseLong(firelevelId));
			// 名称
			firelevel.setFirelevelName(bean.getFirelevelName());
			// 显示顺序
			firelevel.setOrderBy(bean.getOrderBy());
			// 更新
			RunCWorkticketFirelevel workTicketFirelevel = remote
					.update(firelevel);
			if (workTicketFirelevel != null) {
				write(Constants.MODIFY_SUCCESS);
			} else {
				write(Constants.MODIFY_FAILURE);
			}
		} catch (CodeRepeatException ce) {
			write(Constants.MODIFY_FAILURE);
			throw (ce);
		}
	}

	/**
	 * 删除
	 */
	public void deleteWorkTicketFireLevel() {
		// 获取要删去动火票级别id
		String strIds = request.getParameter("ids");
		remote.deleteMulti(strIds);
		write(Constants.DELETE_SUCCESS);
	}
}
