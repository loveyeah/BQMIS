package power.web.administration.comm.action;

import java.sql.SQLException;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.ejb.hr.LogUtil;
import power.ejb.administration.comm.ADCommonFacadeRemote;
import power.ejb.administration.comm.ComAdCRight;

public class CommAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	/** 行政管理共通远程接口 Remote */
	ADCommonFacadeRemote remote;
	/** 读取位置 */
	private int start = 0;
	/** 读取记录数 */
	private int limit = 0;
	/** 部门编码 */
	private String strDeptCode = "";


	/**
	 * @return 读取位置
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param 读取位置
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return 读取记录数
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param 读取记录数
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return 部门编码
	 */
	public String getStrDeptCode() {
		return strDeptCode;
	}

	/**
	 * @param 部门编码
	 */
	public void setStrDeptCode(String strDeptCode) {
		this.strDeptCode = strDeptCode;
	}

	public CommAction() {
		remote = (ADCommonFacadeRemote) factory
				.getFacadeRemote("ADCommonFacade");
	}

	/**
	 * 证件类别编码表数据检索
	 * 
	 * @throws JSONException
	 */
	public void getPaperInfoList() throws JSONException, SQLException {
		try {
			LogUtil.log("Action:证件类别编码表数据检索开始。", Level.INFO, null);
			// 企业代码
			String strEnterPriseCode = employee.getEnterpriseCode();
			// 证件类别编码表数据检索
			PageObject pobj = remote.getPaperInfoList(strEnterPriseCode);
			String strRes;
			strRes = JSONUtil.serialize(pobj);
			write(strRes);
			LogUtil.log("Action:证件类别编码表数据检索结束。", Level.INFO, null);
		} catch (JSONException jsone) {
			LogUtil.log("Action:证件类别编码表数据检索失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException sqle) {
			LogUtil.log("Action:证件类别编码表数据检索失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 根据类别编码，检索此类别下的所有子类别编码和子类别名称。
	 * 
	 * @throws JSONException
	 */
	public void getWorkTypeInfoList() throws JSONException, SQLException {
		try {
			LogUtil.log("Action:根据类别编码，检索此类别下的所有子类别编码和子类别名称开始。", Level.INFO, null);
			// 登陆用户ID
			employee = (Employee) session.getAttribute("employee");
			String strUserID = employee.getWorkerCode();
			// 企业代码
			String strEnterPriseCode = employee.getEnterpriseCode();
			PageObject pobjWorkTypeCode = remote.getUserRight(strUserID, strEnterPriseCode);
			// 类别编码
			String strWorkTypeCode = ((ComAdCRight)pobjWorkTypeCode.getList().get(0)).getWorktypeCode();
			// 工作类别取得
			PageObject pobj = remote.getWorkTypeInfoList(strWorkTypeCode, strEnterPriseCode);
			String strRes = JSONUtil.serialize(pobj);
			write(strRes);
			LogUtil.log("Action:根据类别编码，检索此类别下的所有子类别编码和子类别名称结束。", Level.INFO, null);
		} catch (JSONException jsone) {
			LogUtil.log("Action:根据类别编码，检索此类别下的所有子类别编码和子类别名称失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException sqle) {
			LogUtil.log("Action:根据类别编码，检索此类别下的所有子类别编码和子类别名称失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 根据部门编码，检索出该部门内的人员
	 * 
	 * @throws JSONException
	 */
	public void getWorkerByDept() throws JSONException, SQLException {
		try {
			LogUtil.log("Action:根据部门编码，检索出该部门内的人员开始。", Level.INFO, null);
			// 企业代码
			String strEnterPriseCode = employee.getEnterpriseCode();
			// 工作类别取得
			PageObject pobj = remote.getWorkerByDept(strDeptCode,strEnterPriseCode);
			String strRes;
			strRes = JSONUtil.serialize(pobj);
			write(strRes);
			LogUtil.log("Action:根据部门编码，检索出该部门内的人员结束。", Level.INFO, null);
		} catch (JSONException jsone) {
			LogUtil.log("Action:根据部门编码，检索出该部门内的人员失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException sqle) {
			LogUtil.log("Action:根据部门编码，检索出该部门内的人员失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 根据当前用户的工作类别取得对应的所有值别
	 * 
	 * @throws JSONException
	 */
	public void getDutyTypeInfoList() throws JSONException, SQLException {
		try {
			LogUtil.log("Action:根据当前用户的工作类别取得对应的所有值别开始。", Level.INFO, null);
			// 登陆用户ID
			employee = (Employee) session.getAttribute("employee");
			// 企业代码
			String strEnterPriseCode = employee.getEnterpriseCode();
			String strUserID = employee.getWorkerCode();
			PageObject pobjWorkTypeCode = remote.getUserRight(strUserID, strEnterPriseCode);
			// 类别编码
			String strWorkTypeCode = ((ComAdCRight)pobjWorkTypeCode.getList().get(0)).getWorktypeCode();
			// 工作类别取得
			PageObject pobj = remote.getDutyTypeInfoList(strWorkTypeCode, strEnterPriseCode);
			String strRes;
			strRes = JSONUtil.serialize(pobj);
			write(strRes);
			LogUtil.log("Action:根据当前用户的工作类别取得对应的所有值别结束。", Level.INFO, null);
		} catch (JSONException jsone) {
			LogUtil.log("Action:根据当前用户的工作类别取得对应的所有值别失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException sqle) {
			LogUtil.log("Action:根据当前用户的工作类别取得对应的所有值别失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 根据司机档案，检索出司机编码和姓名
	 * 
	 * @throws JSONException
	 */
	public void getDriverInfoList() throws JSONException, SQLException {
		try {
			LogUtil.log("Action:检索司机编码和姓名开始。", Level.INFO, null);
			// 企业代码
			String strEnterPriseCode = employee.getEnterpriseCode();
			// 司机档案表数据检索
			PageObject pobj = remote.getDriverInfoList(strEnterPriseCode);
			String strRes;
			strRes = JSONUtil.serialize(pobj);
			write(strRes);
			LogUtil.log("Action:检索司机编码和姓名结束。", Level.INFO, null);
		} catch (JSONException jsone) {
			LogUtil.log("Action:检索司机编码和姓名失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException sqle) {
			LogUtil.log("Action:检索司机编码和姓名失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 车辆档案查询
	 * 
	 * @throws JSONException
	 */
	public void getCarInfoList() throws JSONException, SQLException {
		try {
			LogUtil.log("Action:车辆档案查询开始。", Level.INFO, null);
			// 企业代码
			String strEnterPriseCode = employee.getEnterpriseCode();
			// 查询车辆档案
			PageObject pobj = remote.getCarInfoList(strEnterPriseCode, start, limit);
			String strRes;
			if ((pobj.getTotalCount() == null) || (pobj.getTotalCount() == 0)) {
				// 没有检索到 返回空list
				strRes = "{\"list\":[],\"totalCount\":0}";
			} else {
				strRes = JSONUtil.serialize(pobj);
			}
			write(strRes);
			LogUtil.log("Action:车辆档案查询结束。", Level.INFO, null);
		} catch (JSONException jsone) {
			LogUtil.log("Action:车辆档案查询失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException sqle) {
			LogUtil.log("Action:车辆档案查询失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 检索所有的计量单位名称和计量单位标志
	 * 
	 * @throws JSONException
	 */
	public void getUnit() throws JSONException, SQLException {
		try {
			LogUtil.log("Action:检索所有的计量单位名称和计量单位标志开始。", Level.INFO, null);
			// 检索所有的计量单位名称和计量单位标志
			PageObject pobj = remote.getUnit();
			String strRes;
			strRes = JSONUtil.serialize(pobj);
			write(strRes);
			LogUtil.log("Action:检索所有的计量单位名称和计量单位标志结束。", Level.INFO, null);
		} catch (JSONException jsone) {
			LogUtil.log("Action:检索所有的计量单位名称和计量单位标志失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException sqle) {
			LogUtil.log("Action:检索所有的计量单位名称和计量单位标志失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 菜谱类别编码表数据检索
	 * 
	 * @throws JSONException
	 */
	public void getAllCMenuType() throws JSONException, SQLException {
		try {
			LogUtil.log("Action:菜谱类别编码表数据检索开始。", Level.INFO, null);
			// 企业代码
			String strEnterPriseCode = employee.getEnterpriseCode();
			// 检索所有的计量单位名称和计量单位标志
			PageObject pobj = remote.getAllCMenuType(strEnterPriseCode);
			String strRes;
			strRes = JSONUtil.serialize(pobj);
			write(strRes);
			LogUtil.log("Action:菜谱类别编码表数据检索结束。", Level.INFO, null);
		} catch (JSONException jsone) {
			LogUtil.log("Action:菜谱类别编码表数据检索失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException sqle) {
			LogUtil.log("Action:菜谱类别编码表数据检索失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 检索抄送人员的人员编码和姓名
	 * 
	 * @throws JSONException
	 */
	public void getCCUserIDAndName() throws JSONException, SQLException {
		try {
			LogUtil.log("Action:检索抄送人员的人员编码和姓名开始。", Level.INFO, null);
			// 企业代码
			String strEnterPriseCode = employee.getEnterpriseCode();
			// 检索抄送人员的人员编码和姓名
			PageObject pobj = remote.getCCUserIDAndName(strEnterPriseCode);
			String strRes;
			strRes = JSONUtil.serialize(pobj);
			write(strRes);
			LogUtil.log("Action:检索抄送人员的人员编码和姓名结束。", Level.INFO, null);
		} catch (JSONException jsone) {
			LogUtil.log("Action:检索抄送人员的人员编码和姓名失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException sqle) {
			LogUtil.log("Action:检索抄送人员的人员编码和姓名失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}
	}
}
