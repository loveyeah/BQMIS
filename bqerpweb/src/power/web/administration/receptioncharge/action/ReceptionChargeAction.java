/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.receptioncharge.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import power.ear.comm.DataChangeException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJReception;
import power.ejb.administration.AdJReceptionFacadeRemote;
import power.ejb.administration.business.ReceptionChargeFacadeRemote;
import power.ejb.administration.form.ReceptionChargeInfo;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 接待费用管理 Action
 * 
 * @author wangyun
 * 
 */
public class ReceptionChargeAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	/** 日期形式字符串 yyyy-MM-dd */
	private static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	/** 接待费用管理接口 */
	private ReceptionChargeFacadeRemote receptionChargeRemote;
	/** 来宾接待审批单接口 */
	private AdJReceptionFacadeRemote receptionRemote;
	/** 分页起始 */
	private String start;
	/** 每页记录数 */
	private String limit;
	/** 修改时间 */
	private String updateTime;
	/** 来宾接待审批单 */
	private ReceptionChargeInfo reception;

	/**
	 * 构造函数
	 */
	public ReceptionChargeAction() {
		// 接待费用管理接口
		receptionChargeRemote = (ReceptionChargeFacadeRemote) factory
				.getFacadeRemote("ReceptionChargeFacade");
		// 来宾接待审批单接口
		receptionRemote = (AdJReceptionFacadeRemote) factory
				.getFacadeRemote("AdJReceptionFacade");

	}

	/**
	 * 接待费用管理一览初始化
	 * 
	 * @throws JSONException
	 */
	public void getReceptionCharge() throws JSONException {
		LogUtil.log("Action:接待费用管理一览初始化开始", Level.INFO, null);
		PageObject pobj;
		try {
			String strEnterpriseCode = employee.getEnterpriseCode();
			pobj = receptionChargeRemote.getReceptionCharge(strEnterpriseCode,
					Integer.parseInt(start), Integer.parseInt(limit));
			write(JSONUtil.serialize(pobj));
			LogUtil.log("Action:接待费用管理一览初始化结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			e.printStackTrace();
		}

	}

	/**
	 * 接待费用管理更新
	 */
	public void updateReceptionCharge() {
		try {
			LogUtil.log("Action:接待费用管理更新开始", Level.INFO, null);
			String strUpdateTime = reception.getUpdateTime();
			// 序列号
			Long lngId = reception.getId();
			// 取出需要修改的记录
			AdJReception entity = receptionRemote.findById(lngId);
			// 接待日期
			entity.setMeetDate(formatStringToDate(reception.getMeetDate(),
					DATE_FORMAT_YYYYMMDD));
			// 就餐人数
			entity.setRepastNum(reception.getRepastNum());
			// 住宿人数
			entity.setRoomNum(reception.getRoomNum());
			// 接待说明
			entity.setMeetNote(reception.getMeetNote());
			// 就餐标准
			entity.setRepastBz(reception.getRepastBz());
			// 住宿标准
			entity.setRoomBz(reception.getRoomBz());
			// 就餐安排
			entity.setRepastPlan(reception.getRepastPlan());
			// 住宿安排
			entity.setRoomPlan(reception.getRoomPlan());
			// 其他
			entity.setOther(reception.getOther());
			// 实际金额
			entity.setPayout(reception.getPayout());
			// 标准金额
			entity.setPayoutBz(reception.getPayoutBz());
			// 差额
			entity.setBalance(reception.getBalance());
			// 修改人
			entity.setUpdateUser(employee.getWorkerCode());
			// 更新处理
			receptionChargeRemote.update(entity, strUpdateTime);
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:接待费用管理更新结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:接待费用管理更新失败", Level.SEVERE, e);
			// 排他操作
			write(Constants.DATA_USING);
		} catch (SQLException e) {
			LogUtil.log("Action:接待费用管理更新失败", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
		} catch (DataFormatException e) {
			LogUtil.log("Action:接待费用管理更新失败", Level.SEVERE, e);
			write(Constants.DATA_FAILURE);
		} catch (Exception e) {
			LogUtil.log("Action:接待费用管理更新失败", Level.SEVERE, e);
		}
	}

	/**
	 * 根据日期日期字符串和形式返回日期
	 * 
	 * @param argDateStr
	 *            日期字符串
	 * @param argFormat
	 *            日期形式字符串
	 * @return 日期
	 */
	private Date formatStringToDate(String argDateStr, String argFormat)
			throws DataFormatException {
		if (argDateStr == null || argDateStr.trim().length() < 1) {
			return null;
		}
		// 日期形式
		SimpleDateFormat sdfFrom = null;
		// 返回日期
		Date dtresult = null;

		try {
			sdfFrom = new SimpleDateFormat(argFormat);
			// 格式化日期
			dtresult = sdfFrom.parse(argDateStr);
		} catch (Exception e) {
			dtresult = null;
		} finally {
			sdfFrom = null;
		}

		return dtresult;
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
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the reception
	 */
	public ReceptionChargeInfo getReception() {
		return reception;
	}

	/**
	 * @param reception
	 *            the reception to set
	 */
	public void setReception(ReceptionChargeInfo reception) {
		this.reception = reception;
	}
}
