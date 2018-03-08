/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.web.administration.visitorquery.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.business.VisitorQueryFacadeRemote;
import power.ejb.administration.form.VisitorInfo;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.ExportXsl;

/**
 * 来访人员查询Action
 * 
 * @author chaihao
 * 
 */
public class VisitorQueryAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 导出excel文件名前缀 */
	private static final String STR_FILE_NAME = "来访人员";
	/** 读取位置 */
	private int start = 0;
	/** 读取记录数 */
	private int limit = 0;
	/** 开始日期 */
	private String strStartDate = "";
	/** 截止日期 */
	private String strEndDate = "";
	/** 部门序号 */
	private String strDepCode = "";
	/** 来访人员查询处理远程对象 */
	VisitorQueryFacadeRemote remote;

	/**
	 * 构造函数
	 */
	public VisitorQueryAction() {
		remote = (VisitorQueryFacadeRemote) factory
				.getFacadeRemote("VisitorQueryFacade");
	}

	/**
	 * 来访人员查询
	 * 
	 */
	public void visitorQuery() {
		LogUtil.log("Action:来访人员查询开始", Level.INFO, null);
		try {
			employee =(Employee) session.getAttribute("employee");
			// 按日期部门编号检索数据
			PageObject pob = remote.findVisitor(employee.getEnterpriseCode(), strStartDate, strEndDate,
					strDepCode, start, limit);
			request.getSession().setAttribute("pageObjRegularWork", pob);
			if (pob.getTotalCount() != 0) {
				String strRes = JSONUtil.serialize(pob);
				write(strRes);
			} else {
				write("{totalCount:0,list:[]}");
			}
			LogUtil.log("Action:来访人员查询结束", Level.INFO, null);
		} catch (JSONException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:数据格式化失败", Level.SEVERE, e);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * 导出文件
	 * 
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void exportFile() throws Exception {
		try {
			LogUtil.log("Action:导出文件开始", Level.INFO, null);
			// 操作excel文件对象
			ExportXsl exsl = new ExportXsl();
			// 设置response
			exsl.setResponse(response);
			// 当前时间作为文件名以部分
			Date dte = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			String strDate = sdf.format(dte);
			// 设定文件名
			exsl.setStrFileName(STR_FILE_NAME + strDate);
			// excel文件每列标题头
			List<String> lstHeader = new ArrayList<String>();
			lstHeader.add("行号");
			lstHeader.add("来访人");
			lstHeader.add("来访时间");
			lstHeader.add("证件类别");
			lstHeader.add("证件号");
			lstHeader.add("来访人单位");
			lstHeader.add("被访人");
			lstHeader.add("被访人部门");
			lstHeader.add("进厂时间");
			lstHeader.add("出厂时间");
			lstHeader.add("备注");
			lstHeader.add("值班人");
			exsl.setLstTitle(lstHeader);

			// excel文件中的一行
			List<String> lstRow = null;
			// excel文件中的所有行集合
			List lstRowSet = new ArrayList();
			// excel文件单行实体
			VisitorInfo tempVisitorInfo = null;
			// 获得需要导出的excel文件内容
			PageObject pageObj = (PageObject) request.getSession()
					.getAttribute("pageObjRegularWork");
			for (int i = 0; i < pageObj.getList().size(); i++) {
				lstRow = new ArrayList();
				tempVisitorInfo = (VisitorInfo) pageObj.getList().get(i);
				// 设置行号
				lstRow.add(i + 1 + "");
				// 设置来访人
				if (tempVisitorInfo.getInsertBy() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempVisitorInfo.getInsertBy());
				}
				// 设置来访日期
				if (tempVisitorInfo.getInsertDate() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempVisitorInfo.getInsertDate());
				}
				// 设置证件类别
				if (tempVisitorInfo.getPaperTypeName() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempVisitorInfo.getPaperTypeName());
				}
				// 设置证件号
				if (tempVisitorInfo.getPaperId() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempVisitorInfo.getPaperId());
				}
				// 设置来访人单位
				if (tempVisitorInfo.getFirm() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempVisitorInfo.getFirm());
				}
				// 设置被访人
				if (tempVisitorInfo.getName() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempVisitorInfo.getName());
				}
				// 设置被访人部门
				if (tempVisitorInfo.getDepName() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempVisitorInfo.getDepName());
				}
				// 设置进厂时间
				if (tempVisitorInfo.getInDate() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempVisitorInfo.getInDate());
				}
				// 设置出厂时间
				if (tempVisitorInfo.getOutDate() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempVisitorInfo.getOutDate());
				}
				// 设置备注
				if (tempVisitorInfo.getNote() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempVisitorInfo.getNote());
				}
				// 设置值班人
				if (tempVisitorInfo.getOnDuty() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempVisitorInfo.getOnDuty());
				}
				lstRowSet.add(lstRow);
			}
			// 设置所有行内容
			exsl.setLstRow(lstRowSet);
			// 创建导出excel文件
			exsl.createXsl();
			LogUtil.log("Action:导出文件结束", Level.INFO, null);
		} catch (IOException e) {
			write(Constants.IO_FAILURE);
			LogUtil.log("Action:导出文件IO异常", Level.SEVERE, e);
		}
	}

	/**
	 * @return 起始时间
	 */
	public String getStrStartDate() {
		return strStartDate;
	}

	/**
	 * @param 起始时间
	 *            设置起始时间
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
	 * @param 截止时间
	 *            设置截止时间
	 */
	public void setStrEndDate(String strEndDate) {
		this.strEndDate = strEndDate;
	}

	/**
	 * @return 部门编码
	 */
	public String getStrDepCode() {
		return strDepCode;
	}

	/**
	 * @param 部门编码
	 *            设置部门编码
	 */
	public void setStrDepCode(String strDepCode) {
		this.strDepCode = strDepCode;
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