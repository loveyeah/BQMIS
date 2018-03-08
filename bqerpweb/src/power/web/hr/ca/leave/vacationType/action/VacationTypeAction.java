/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.ca.leave.vacationType.action;

import java.sql.SQLException;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.web.comm.Constants;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.ca.HrCVacationtype;
import power.ejb.hr.ca.HrCVacationtypeFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;

/**
 * 假别维护
 * 
 * @author wujiao
 * @version 1.0
 */
public class VacationTypeAction extends AbstractAction {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 查询，保存，修改数据远程对象 */
	private HrCVacationtypeFacadeRemote remote;
	/** 分页信息 */
	private int start;
	private int limit;
	/** 假别ID */
	private String vacationTypeId;
	/** 假别 */
	private String vacationType;
	/** 是否设置周期 */
	private String ifCycle;
	/** 是否包含周末 */
	private String ifWeekend;
	/** 假别标志（考勤标志） */
	private String vacationMark;

	/**
	 * 构造函数
	 */
	public VacationTypeAction() {
		remote = (HrCVacationtypeFacadeRemote) factory
				.getFacadeRemote("HrCVacationtypeFacade");
	}

	/**
	 * 假别维护 查询所有信息
	 * 
	 * @throws SQLException
	 * 
	 * @throws SQLException,JSONException
	 * @throws JSONException
	 * 
	 */
	public void searchVacationTypeList() throws SQLException, JSONException {
		try {
			LogUtil.log("Action:假别维护查询开始。", Level.INFO, null);
			PageObject obj = new PageObject();
			// 查询所有假别信息
			obj = remote.findAllVacation(employee.getEnterpriseCode(), start,
					limit);
			String str = JSONUtil.serialize(obj);
			write(str);
			LogUtil.log("Action:假别维护查询结束。", Level.INFO, null);
		} catch (JSONException jsone) {
			LogUtil.log("Action:假别维护查询失败。", Level.SEVERE, jsone);
			throw jsone;
		} catch (SQLException sqle) {
			LogUtil.log("Action:假别维护查询失败。", Level.SEVERE, sqle);
			throw sqle;
		}
	}

	/**
	 * 假别维护 删除信息
	 * 
	 * @throws SQLException
	 * 
	 * @throws SQLException
	 * 
	 */
	public void deleteVacationType() throws SQLException {
		try {
			LogUtil.log("Action:假别维护删除开始。", Level.INFO, null);
			// 根据流水号寻找这条假别维护信息
			HrCVacationtype entity = remote.findById(Long
					.parseLong(vacationTypeId.toString()));
			// 设置IS_USE字段为N
			entity.setIsUse(Constants.IS_USE_N);
			// 上次修改人
			entity.setLastModifiyBy(employee.getWorkerCode());
			// 取得假别id
			remote.deleteByVacationTypeId(entity);
			LogUtil.log("Action:假别维护删除结束。", Level.INFO, null);
		} catch (SQLException sqle) {
			LogUtil.log("Action:假别维护删除失败。", Level.SEVERE, sqle);
			throw sqle;
		}
	}

	/**
	 * 假别维护 修改信息
	 * 
	 * @param entity
	 * 
	 * @throws SQLException
	 * 
	 */
	public void updateVacationType() throws Exception {
		try {
			LogUtil.log("Action:假别维护修改开始。", Level.INFO, null);
			// 根据流水号寻找这条假别维护信息
			HrCVacationtype entity = remote.findById(Long
					.parseLong(vacationTypeId.toString()));
			// 设置假别
			entity.setVacationType(vacationType);
			// 设置是否设置周期
			entity.setIfCycle(ifCycle);
			// 设置是否包含周期
			entity.setIfWeekend(ifWeekend);
			// 设置更新者
			entity.setLastModifiyBy(employee.getWorkerCode());
			// 设置假别标志（考勤标志）
			entity.setVacationMark(vacationMark);
			// 更新数据
			remote.updateVacation(entity);
			LogUtil.log("Action:假别维护修改结束。", Level.INFO, null);
		} catch (Exception sqle) {
			LogUtil.log("Action:假别维护修改失败。", Level.SEVERE, sqle);
			throw sqle;
		}
	}

	/**
	 * 假别维护 增加信息
	 * 
	 * @param entity
	 * 
	 * @throws SQLException
	 * 
	 */
	public void addVacationType() throws SQLException {
		try {
			LogUtil.log("Action:假别维护新增开始。", Level.INFO, null);
			// 根据流水号寻找这条币种信息
			HrCVacationtype entity = new HrCVacationtype();
			// 设置假别
			entity.setVacationType(vacationType);
			// 设置是否设置周期
			entity.setIfCycle(ifCycle);
			// 设置是否包含周末
			entity.setIfWeekend(ifWeekend);
			// 设置假别标志(考勤标志)
			entity.setVacationMark(vacationMark);
			// 企业代码
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			// 设置创建者
			entity.setLastModifiyBy(employee.getWorkerCode());
			remote.addVacation(entity);
			LogUtil.log("Action:假别维护新增结束。", Level.INFO, null);
		} catch (SQLException sql) {
			LogUtil.log("Action:假别维护新增失败。", Level.SEVERE, sql);
			throw sql;
		}
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the remote
	 */
	public HrCVacationtypeFacadeRemote getRemote() {
		return remote;
	}

	/**
	 * @param remote
	 *            the remote to set
	 */
	public void setRemote(HrCVacationtypeFacadeRemote remote) {
		this.remote = remote;
	}

	/**
	 * @return the vacationTypeId
	 */
	public String getVacationTypeId() {
		return vacationTypeId;
	}

	/**
	 * @param vacationTypeId
	 *            the vacationTypeId to set
	 */
	public void setVacationTypeId(String vacationTypeId) {
		this.vacationTypeId = vacationTypeId;
	}

	/**
	 * @return the vacationType
	 */
	public String getVacationType() {
		return vacationType;
	}

	/**
	 * @param vacationType
	 *            the vacationType to set
	 */
	public void setVacationType(String vacationType) {
		this.vacationType = vacationType;
	}

	/**
	 * @return the ifCycle
	 */
	public String getIfCycle() {
		return ifCycle;
	}

	/**
	 * @param ifCycle
	 *            the ifCycle to set
	 */
	public void setIfCycle(String ifCycle) {
		this.ifCycle = ifCycle;
	}

	/**
	 * @return the ifWeekend
	 */
	public String getIfWeekend() {
		return ifWeekend;
	}

	/**
	 * @param ifWeekend
	 *            the ifWeekend to set
	 */
	public void setIfWeekend(String ifWeekend) {
		this.ifWeekend = ifWeekend;
	}

	/**
	 * @return the vacationMark
	 */
	public String getVacationMark() {
		return vacationMark;
	}

	/**
	 * @param vacationMark
	 *            the vacationMark to set
	 */
	public void setVacationMark(String vacationMark) {
		this.vacationMark = vacationMark;
	}
}
