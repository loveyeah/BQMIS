/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.outquestquery.action;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.servlet.ServletOutputStream;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.business.OutQuestQueryFacadeRemote;
import power.ejb.administration.form.AdJOutQuestFileInfo;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 签报申请查询Action
 * 
 * @author jincong
 * @version 1.0
 */
public class OutQuestQueryAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 签报申请查询Remote */
	private OutQuestQueryFacadeRemote remote;
	/** 查询起始时间 */
	private String startDate;
	/** 查询结束时间 */
	private String endDate;
	/** 呈报部门code */
	private String deptCode;
	/** 申请人code */
	private String applierCode;
	/** 单据状态 */
	private String reportStatus;
	/** 申请单号 */
	private String applyId;
	/** 开始行 */
	private String start;
	/** 查询行 */
	private String limit;
	/** ID */
	private String id;

	/**
	 * 构造函数
	 */
	public OutQuestQueryAction() {
		// 取得远程接口
		remote = (OutQuestQueryFacadeRemote) factory
				.getFacadeRemote("OutQuestQueryFacade");
	}

	/**
	 * 取得签报申请信息
	 */
	public void getOutQueryApply() {
		try {
			LogUtil.log("Action:取得签报申请信息开始。", Level.INFO, null);
			// 开始行
			int intStart = Integer.parseInt(start);
			// 查询行
			int intLimit = Integer.parseInt(limit);
			// 查询
			PageObject object = remote.findOutQueryApply(startDate, endDate, deptCode,
					applierCode, reportStatus, employee.getEnterpriseCode(), intStart, intLimit);
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
	 * 取得签报申请附件信息
	 */
	public void getOutQueryFile() {
		try {
			LogUtil.log("Action:取得签报申请附件信息开始。", Level.INFO, null);
			// 开始行
			int intStart = Integer.parseInt(start);
			// 查询行
			int intLimit = Integer.parseInt(limit);
			// 查询
			PageObject object = remote.findOutQueryFile(applyId, intStart, intLimit);
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
	 * 下载签报申请附件
	 * @throws IOException 
	 */
	public void downloadOutQueryFile() throws IOException {
		try {
			LogUtil.log("Action:下载签报申请附件开始。", Level.INFO, null);
			// 根据id查找签报申请附件
			PageObject object = remote.findOutQueryFileById(id);
			if(object.getList().size() != 0) {
				AdJOutQuestFileInfo adJOutQuestFile = (AdJOutQuestFileInfo)object.getList().get(0);
				 // 情報設定
		        String name = URLEncoder.encode(adJOutQuestFile.getFileName(), "UTF-8");
	
		        response.setHeader("Content-Disposition", 
		                           "attachment; filename=" + 
		                           name);
		        response.setContentType(adJOutQuestFile.getFileType());
		        ServletOutputStream os = response.getOutputStream();
		        BufferedOutputStream out = new BufferedOutputStream(os);
		        out.write(adJOutQuestFile.getFileText());
		        out.flush();
		        out.close();
		        os.flush();
		        os.close();
		        LogUtil.log("Action:下载签报申请附件结束。", Level.INFO, null);
			}
		} catch (IOException e) {
			LogUtil.log("Action:下载签报申请附件失败。", Level.SEVERE, e);
		} catch (SQLException e) {
			LogUtil.log("Action:下载签报申请附件失败。", Level.SEVERE, e);
		}
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the deptCode
	 */
	public String getDeptCode() {
		return deptCode;
	}

	/**
	 * @param deptCode
	 *            the deptCode to set
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	/**
	 * @return the applierCode
	 */
	public String getApplierCode() {
		return applierCode;
	}

	/**
	 * @param applierCode
	 *            the applierCode to set
	 */
	public void setApplierCode(String applierCode) {
		this.applierCode = applierCode;
	}

	/**
	 * @return the reportStatus
	 */
	public String getReportStatus() {
		return reportStatus;
	}

	/**
	 * @param reportStatus
	 *            the reportStatus to set
	 */
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
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
	 * @return the applyId
	 */
	public String getApplyId() {
		return applyId;
	}

	/**
	 * @param applyId
	 *            the applyId to set
	 */
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
}
