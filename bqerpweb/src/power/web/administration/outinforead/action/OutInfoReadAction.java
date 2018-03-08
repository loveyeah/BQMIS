/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.outinforead.action;

import java.sql.SQLException;
import java.util.logging.Level;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJOutQuest;
import power.ejb.administration.AdJOutQuestFacadeRemote;
import power.ejb.administration.business.OutInfoReadFacadeRemote;
import power.ejb.administration.business.OutQuestQueryFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 签报信息查阅Action
 * 
 * @author jincong
 * @version 1.0
 */
public class OutInfoReadAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 签报信息查阅Remote */
	private OutInfoReadFacadeRemote remote;
	/** 签报信息Remote */
	private AdJOutQuestFacadeRemote questRemote;
	/** 签报申请查询Remote */
	private OutQuestQueryFacadeRemote queryRemote;
	/** 开始行 */
	private String start;
	/** 查询行 */
	private String limit;
	/** 申请单号 */
	private String applyId;
	/** 阅读状态 */
	private String status;
	/** ID */
	private String id;
	/** 修改时间 */
	private String updateTime;

	/**
	 * 构造函数
	 */
	public OutInfoReadAction() {
		// 签报信息查阅Remote
		remote = (OutInfoReadFacadeRemote) factory
				.getFacadeRemote("OutInfoReadFacade");
		// 签报信息Remote
		questRemote = (AdJOutQuestFacadeRemote) factory
				.getFacadeRemote("AdJOutQuestFacade");
		// 签报申请查询Remote
		queryRemote = (OutQuestQueryFacadeRemote) factory
				.getFacadeRemote("OutQuestQueryFacade");
	}

	/**
	 * 查询签报申请信息
	 */
	public void getOutInfoRead() {
		try {
			LogUtil.log("Action:取得签报申请信息开始。", Level.INFO, null);
			// 开始行
			int intStart = Integer.parseInt(start);
			// 查询行
			int intLimit = Integer.parseInt(limit);
			PageObject object = remote.findOutInfo(status, null, employee
					.getWorkerCode(), employee.getEnterpriseCode(), intStart, intLimit);
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:取得签报申请信息结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:取得签报申请信息失败。", Level.SEVERE, e);
		} catch (SQLException e) {
			LogUtil.log("Action:取得签报申请信息失败。", Level.SEVERE, e);
		}
	}
	
	/**
	 * 修改签报申请信息
	 */
	public void updateOutInfoRead() {
		try {
			LogUtil.log("Action:修改签报申请信息开始。", Level.INFO, null);
			AdJOutQuest adJOutQuest = questRemote.findById(Long.parseLong(id));
			// 设置阅读状态为已阅读
			adJOutQuest.setIfRead(Constants.FLAG_Y);
			// 设置修改认
			adJOutQuest.setUpdateUser(employee.getWorkerCode());
			// 更新
			questRemote.update(adJOutQuest, updateTime);
			write(Constants.UPLOAD_SUCCESS);
			LogUtil.log("Action:修改签报申请信息结束。", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:修改签报申请信息失败。", Level.SEVERE, e);
		} catch (DataChangeException e) {
			write(Constants.DATA_USING);
			LogUtil.log("Action:修改签报申请信息失败。", Level.SEVERE, e);
		}
	}
	
	/**
	 * 取得签报申请附件信息
	 */
	public void getOutQueryFileRead() {
		try {
			LogUtil.log("Action:取得签报申请附件信息开始。", Level.INFO, null);
			// 查询
			PageObject object = queryRemote.findOutQueryFile(applyId);
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:取得签报申请附件信息结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:取得签报申请附件信息失败。", Level.SEVERE, e);
		} catch (SQLException e) {
			LogUtil.log("Action:取得签报申请附件信息失败。", Level.SEVERE, e);
		}
	}

	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public String getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(String limit) {
		this.limit = limit;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the applyId
	 */
	public String getApplyId() {
		return applyId;
	}

	/**
	 * @param applyId the applyId to set
	 */
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
