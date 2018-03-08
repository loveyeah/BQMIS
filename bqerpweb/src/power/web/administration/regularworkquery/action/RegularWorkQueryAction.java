/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.web.administration.regularworkquery.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.business.RegularWorkQueryFacadeRemote;
import power.ejb.administration.comm.ADCommonFacadeRemote;
import power.ejb.administration.comm.ComAdCRight;
import power.ejb.administration.form.RegularWorkInfo;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.ExportXsl;

/**
 * 定期工作查询Action
 * 
 * @author chaihao
 * 
 */
public class RegularWorkQueryAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 导出excel文件名前缀 */
	private static final String STR_FILE_NAME = "定期工作";
	/** 读取位置 */
	private int start = 0;
	/** 读取记录数 */
	private int limit = 0;
	/** 起始时间 */
	private String strStartDate = "";
	/** 截止时间 */
	private String strEndDate = "";
	/** 子工作类别编码 */
	private String strSubWorkTypeCode = "";
	/** 完成标志 */
	private String strCompleteFlag = "";
	/** 工作类别编码 */
	private String strWorkTypeCode = "";
	/** 定期工作查询处理远程对象 */
	private RegularWorkQueryFacadeRemote remote;
	/** 用户权限取得处理远程对象 */
	private ADCommonFacadeRemote adcremote;

	/**
	 * 构造函数
	 */
	public RegularWorkQueryAction() {
		remote = (RegularWorkQueryFacadeRemote) factory
				.getFacadeRemote("RegularWorkQueryFacade");
		adcremote = (ADCommonFacadeRemote) factory
				.getFacadeRemote("ADCommonFacade");
	}

	/**
	 * 用户权限取得
	 * @throws SQLException 
	 */
	public String getUserRight() throws SQLException {
		LogUtil.log("Action:取得用户权限开始", Level.INFO, null);
		try {
			// 用户工作类别PageObject
			PageObject userRightPob = adcremote.getUserRight(employee.getWorkerCode(), employee.getEnterpriseCode());
			if (userRightPob.getList().size() > 0) {
				ComAdCRight objTemp = (ComAdCRight) userRightPob.getList().get(0);
				String userRight = objTemp.getWorktypeCode();
				LogUtil.log("Action:取得用户权限结束", Level.INFO, null);
				return userRight;
			} else {
				LogUtil.log("Action:取得用户权限结束", Level.INFO, null);
				return "";
			}
		} catch (RuntimeException e) {
			LogUtil.log("Action:取得用户权限失败", Level.SEVERE, e);
			throw e;
		}
	}

	/**
	 * 定期工作查询
	 * 
	 */
	public void regularWorkQuery() {
		LogUtil.log("Action:定期工作查询开始", Level.INFO, null);
		try {
			// 获取用户权限
			String strWorkTypeCode = getUserRight();
			// 按画面指定属性检索数据
			PageObject pob = remote.findRegularWork(employee.getEnterpriseCode(), strStartDate, strEndDate,
					strSubWorkTypeCode, strCompleteFlag, strWorkTypeCode,
					start, limit);
			request.getSession().setAttribute("pageObjRegularWork", pob);
			if (pob.getTotalCount() != 0) {
				String strRes = JSONUtil.serialize(pob);
				write(strRes);
				LogUtil.log("Action:定期工作查询结束", Level.INFO, null);
			} else {
				write("{totalCount:0,list:[]}");
				LogUtil.log("Action:定期工作查询结束", Level.INFO, null);
			}
		} catch (JSONException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:数据格式化失败", Level.SEVERE, e);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * 导出excel文件
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings( { "unchecked", "unchecked" })
	public void exportFile() throws Exception {
		LogUtil.log("Action:导出文件开始", Level.INFO, null);
		// 操作excel文件对象
		ExportXsl exsl = new ExportXsl();
		// 设置response
		exsl.setResponse(response);
		// 当前时间作为文件名一部分
		Date dte = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String strDate = sdf.format(dte);
		// 设定文件名
		exsl.setStrFileName(STR_FILE_NAME + strDate);
		// excel文件每列标题头
		List<String> lstHeader = new ArrayList<String>();
		lstHeader.add("行号");
		lstHeader.add("工作项目名称");
		lstHeader.add("工作日期");
		lstHeader.add("星期");
		lstHeader.add("具体工作内容");
		lstHeader.add("工作结果");
		lstHeader.add("完成标志");
		lstHeader.add("操作人");
		lstHeader.add("备注");
		exsl.setLstTitle(lstHeader);

		// excel文件中的一行
		List<String> lstRow = null;
		// excel文件中的所有行集合
		List lstRowSet = new ArrayList();
		// excel文件单行实体
		RegularWorkInfo tempRegularWorkInfo = null;
		// 获得需要导出的excel文件内容
		PageObject pageObj = (PageObject) request.getSession().getAttribute(
				"pageObjRegularWork");
		for (int i = 0; i < pageObj.getList().size(); i++) {
			lstRow = new ArrayList();
			tempRegularWorkInfo = (RegularWorkInfo) pageObj.getList().get(i);
			// 设置行号
			lstRow.add(i + 1 + "");
			// 设置工作项目名称
			if (tempRegularWorkInfo.getWorkItemName() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempRegularWorkInfo.getWorkItemName());
			}
			// 设置工作日期
			if (tempRegularWorkInfo.getWorkDate() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempRegularWorkInfo.getWorkDate());
			}
			// 设置星期
			if (tempRegularWorkInfo.getWeek() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempRegularWorkInfo.getWeek());
			}
			// 设置具体工作内容
			if (tempRegularWorkInfo.getWorkExplain() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempRegularWorkInfo.getWorkExplain());
			}
			// 设置工作结果
			if (tempRegularWorkInfo.getResult() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempRegularWorkInfo.getResult());
			}
			// 设置完成标志
			if (tempRegularWorkInfo.getMark() == null) {
				lstRow.add("");
			} else {
				if (tempRegularWorkInfo.getMark().equals("Y")) {
					lstRow.add("完成");
				} else {
					lstRow.add("未完成");
				}
			}
			// 设置操作人
			if (tempRegularWorkInfo.getOperator() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempRegularWorkInfo.getOperator());
			}
			// 设置备注
			if (tempRegularWorkInfo.getMemo() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempRegularWorkInfo.getMemo());
			}
			lstRowSet.add(lstRow);
		}
		// 设置所有行内容
		exsl.setLstRow(lstRowSet);
		// 创建导出excel文件
		exsl.createXsl();
		LogUtil.log("Action:导出文件结束", Level.INFO, null);
	}

	/**
	 * @return 开始时间
	 */
	public String getStrStartDate() {
		return strStartDate;
	}

	/**
	 * @param 开始时间
	 *            设置开始时间
	 */
	public void setStrStartDate(String strStartDate) {
		this.strStartDate = strStartDate;
	}

	/**
	 * @return 截止时间
	 */
	public String getStrEndDate() {
		return strEndDate;
	}

	/**
	 * @param 设置截止时间
	 *            设置截止时间
	 */
	public void setStrEndDate(String strEndDate) {
		this.strEndDate = strEndDate;
	}

	/**
	 * @return 完成标志
	 */
	public String getStrCompleteFlag() {
		return strCompleteFlag;
	}

	/**
	 * @param 完成标志
	 *            设置完成标志
	 */
	public void setStrCompleteFlag(String strCompleteFlag) {
		this.strCompleteFlag = strCompleteFlag;
	}

	/**
	 * @return 类别编码
	 */
	public String getStrWorkTypeCode() {
		return strWorkTypeCode;
	}

	/**
	 * @param 类别编码
	 *            设置类别编码
	 */
	public void setStrWorkTypeCode(String strWorkTypeCode) {
		this.strWorkTypeCode = strWorkTypeCode;
	}

	/**
	 * @return 子类别编码
	 */
	public String getStrSubWorkTypeCode() {
		return strSubWorkTypeCode;
	}

	/**
	 * @param 子类别编码
	 *            设置子类别编码
	 */
	public void setStrSubWorkTypeCode(String strSubWorkTypeCode) {
		this.strSubWorkTypeCode = strSubWorkTypeCode;
	}

	/**
	 * @return 读取位置
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param 读取位置
	 *            设置读取位置
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
	 *            设置读取记录数
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
