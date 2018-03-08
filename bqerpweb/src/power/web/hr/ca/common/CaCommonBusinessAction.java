/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.ca.common;

import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.LogUtil;
import power.ejb.hr.ca.DeptAttendanceFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 共通Action
 *
 * @author jincong
 * @version 1.0
 */
public class CaCommonBusinessAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/** 部门考勤登记Remote */
	private DeptAttendanceFacadeRemote deptAttendanceRemote;
	
	/**
	 * 构造函数
	 */
	public CaCommonBusinessAction() {
		// 取得接口
		deptAttendanceRemote = (DeptAttendanceFacadeRemote) factory
				.getFacadeRemote("DeptAttendanceFacade");
	}
	
	/**
	 * 查询所有假别
	 */
	public void getVacationTypeCommon() {
		try {
			LogUtil.log("Action:所有假别查询开始。", Level.INFO, null);
			// 查询
			PageObject object = deptAttendanceRemote.getVacationTypeCommon(
					employee.getEnterpriseCode());
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:所有假别查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:所有假别查询失败。", Level.SEVERE, e);
		}
	}
	
	/**
	 * 查询所有基本天数
	 */
	public void getBasicDaysCommon()
	{
		try {
			LogUtil.log("Action:所有基本天数查询开始。", Level.INFO, null);
			// 查询
			PageObject object = deptAttendanceRemote.getBasicDaysCommon(
					employee.getEnterpriseCode());
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:所有基本天数查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:所有基本天数查询失败。", Level.SEVERE, e);
		}
	}
	
	/**
	 * 查询所有加班类别
	 */
	public void getOvertimeTypeCommon() {
		try {
			LogUtil.log("Action:所有加班类别查询开始。", Level.INFO, null);
			// 查询
			PageObject object = deptAttendanceRemote.getOvertimeTypeCommon(
					employee.getEnterpriseCode());
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:所有加班类别查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:所有加班类别查询失败。", Level.SEVERE, e);
		}
	}
	
	/**
	 * 查询所有运行班类别
	 */
	public void getWorkshiftTypeCommon() {
		try {
			LogUtil.log("Action:所有运行班类别查询开始。", Level.INFO, null);
			// 查询
			PageObject object = deptAttendanceRemote.getWorkshiftTypeCommon(
					employee.getEnterpriseCode());
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:所有运行班类别查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:所有运行班类别查询失败。", Level.SEVERE, e);
		}
	}

}
