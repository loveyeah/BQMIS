/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.ca.leave.leaveManagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.CodeConstants;
import power.ejb.hr.LogUtil;
import power.ejb.hr.ca.HrCExchangetorest;
import power.ejb.hr.ca.HrCExchangetorestFacadeRemote;
import power.ejb.hr.ca.HrCExchangetorestId;
import power.ejb.hr.ca.OverTimeRegiste;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 加班换休登记Action
 * 
 * @author liuxin
 * 
 */

public class LeaveManagementAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private String yearMonth;
	private String empId;
	private String actualTime;
	private String signState;
	private String deptId;
	private HrCExchangetorestFacadeRemote remote;
	private String overTimeStore;
	private String btnKind;
	/** 日期形式字符串 DATE_FORMAT_YYYYMMDD_HHMM */
	private static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

	public String getBtnKind() {
		return btnKind;
	}

	public void setBtnKind(String btnKind) {
		this.btnKind = btnKind;
	}

	public String getOverTimeStore() {
		return overTimeStore;
	}

	public void setOverTimeStore(String overTimeStore) {
		this.overTimeStore = overTimeStore;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getActualTime() {
		return actualTime;
	}

	public void setActualTime(String actualTime) {
		this.actualTime = actualTime;
	}

	public String getSignState() {
		return signState;
	}

	public void setSignState(String signState) {
		this.signState = signState;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	// 构造函数
	public LeaveManagementAction() {
		remote = (HrCExchangetorestFacadeRemote) factory
				.getFacadeRemote("HrCExchangetorestFacade");
	}

	/**
	 * 加班换休登记查询
	 */
	public void searchLastMonth() {
		LogUtil.log("Action:加班换休登记查询开始。", Level.INFO, null);
		PageObject poResult = new PageObject();
		String year = yearMonth.substring(0, 4);
		String month = yearMonth.substring(5);
		List<OverTimeRegiste> list = remote.searchLastMonth(deptId, year,
				month, employee.getEnterpriseCode());
		poResult.setList(list);
		String str = null;
		try {
			str = JSONUtil.serialize(poResult);
		} catch (JSONException e) {
			LogUtil.log("Action:加班换休登记查询失败。", Level.SEVERE, null);
		}
		write(str);
		LogUtil.log("Action:加班换休登记查询结束。", Level.INFO, null);
	}

	/**
	 * 加班换休登记表插入
	 */
	@SuppressWarnings("unchecked")
	public void saveLastMonth() {
		LogUtil.log("Action:加班换休登记表插入开始。", Level.INFO, null);
		List<HrCExchangetorest> list = new ArrayList<HrCExchangetorest>();
		// 获取前台传来的加班换休的信息
		try {
			List<Map> overTimeInfo = (List<Map>) JSONUtil
					.deserialize(overTimeStore);
			for (int i = 0; i < overTimeInfo.size(); i++) {
				HrCExchangetorest exchangetorestBean = new HrCExchangetorest();
				HrCExchangetorestId exchangetorestBeanId = new HrCExchangetorestId();
				Map<Object, Object> info = overTimeInfo.get(i);
				// 人员id
				if (info.get("empId") != null
						&& !Constants.BLANK_STRING.equals(info.get("empId")))
					exchangetorestBeanId.setEmpId(Long.parseLong(info.get(
							"empId").toString()));
				// 考勤年份
				if (info.get("attendanceYear") != null
						&& !Constants.BLANK_STRING.equals(info
								.get("attendanceYear")))
					exchangetorestBeanId.setAttendanceYear(info.get(
							"attendanceYear").toString());
				// 考勤月份
				if (info.get("attendanceMonth") != null
						&& !Constants.BLANK_STRING.equals(info
								.get("attendanceMonth")))
					exchangetorestBeanId.setAttendanceMonth(info.get(
							"attendanceMonth").toString());
				// 换休时间
				if (info.get("exchangerestHours") != null
						&& !Constants.BLANK_STRING.equals(info
								.get("exchangerestHours")))
					exchangetorestBean.setExchangerestHours(Double
							.parseDouble(info.get("exchangerestHours")
									.toString()));
				// 审批状态
				if (info.get("signState") != null
						&& !Constants.BLANK_STRING
								.equals(info.get("signState")))
					exchangetorestBean.setSignState(info.get("signState")
							.toString());
				// 按下的是上报按钮
				if ("1".equals(btnKind)) {
					if (info.get("signState") != null
							&& !Constants.BLANK_STRING.equals(info
									.get("signState"))) {
						// 上报状态为"未上报"和"已退回"的情况,将上报状态改为已上报
						if (info.get("signState").equals(CodeConstants.FROM_STATUS_0)
								|| info.get("signState").equals(CodeConstants.FROM_STATUS_3))
							exchangetorestBean.setSignState(CodeConstants.FROM_STATUS_1);
					}
				}
				// 上次修改时间
				if (info.get("lastModifyTime") != null
						&& !Constants.BLANK_STRING.equals(info
								.get("lastModifyTime"))) {
					String strDate = info.get("lastModifyTime").toString();
					SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT_YYYYMMDD_HHMMSS); 
					exchangetorestBean.setLastModifiyDate(sdf.parse(strDate));
				}
				exchangetorestBean.setId(exchangetorestBeanId);
				exchangetorestBean.setEnterpriseCode(employee
						.getEnterpriseCode());
				exchangetorestBean.setIsUse(Constants.IS_USE_Y);
				exchangetorestBean.setLastModifiyBy(employee.getWorkerCode());
				list.add(exchangetorestBean);
			}
			remote.saveOrUpdate(list);
			LogUtil.log("Action:加班换休登记表插入成功。", Level.INFO, null);
			write("S");
		}
		catch (DataChangeException de) {
			write("U");
			LogUtil.log("Action:加班换休登记表插入失败。", Level.SEVERE, null);
		} catch (JSONException e) {
			write("E");
			LogUtil.log("Action:加班换休登记表插入失败。", Level.SEVERE, null);
		} catch (Exception e) {
			write("E");
			LogUtil.log("Action:加班换休登记表插入失败。", Level.SEVERE, null);
		}
		
	}
}
