/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.businessapproval.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.business.MeetApproveFacadeRemote;
import power.ejb.administration.form.MeetApprove;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.ExportXsl;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会务审批查询
 * @author chen shoujiang
 *  
 */
public class BusinessApprovalAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	/** 导出excel文件名前缀 */
	private static final String STR_FILE_NAME = "会务审批查询";
	/** 会议审批**/
	private MeetApproveFacadeRemote meetApproveRemote;
	/** 开始日期 */
	private String startDate;
	/** 结束日期*/
	private String endDate;
	/** 部门编码 */
	private String depCode;
	/** 人员 */
	private String member;
	/** 是否超支 */
	private String overSpend;
	/** 单据状态 */
	private String dcmStatus;
	/**
	 * 构造函数
	 */
	public BusinessApprovalAction() {
		meetApproveRemote = (MeetApproveFacadeRemote) factory
				.getFacadeRemote("MeetApproveFacade");
	}
	/**
	 *  查询会议审批 
	 * @throws JSONException
	 * 
	 */
	public void getMeetApproveList() throws JSONException, Exception {
		try{
			LogUtil.log("Action:查询会议审批信息开始", Level.INFO, null);
			// 分页信息
			PageObject obj = new PageObject();
				// 无分页信息时执行
			obj = meetApproveRemote.getMeetApproveInfo(startDate, endDate,
					depCode, member, overSpend, dcmStatus,employee.getEnterpriseCode());
			request.getSession().setAttribute("pageObjRegularWork", obj);
			// 输出
			String strOutput = "";
			//　要是查询结果不为空的话，就赋值
			if(obj.getList() != null && obj.getList().size() > 0) {
				strOutput = JSONUtil.serialize(obj);
			} else {
				// 否则设为空值
				strOutput = "{\"list\":[],\"totalCount\":0}";
			}
			write(strOutput);
			LogUtil.log("Action:查询会议审批信息结束", Level.INFO, null);
		} catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询会议审批信息失败", Level.INFO, null);
		} catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:查询会议审批信息失败", Level.INFO, null);
		}
	}
	
	/**
	 * 导出文件
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void exportBusinessApprovalFile() throws Exception {
		try{
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
		lstHeader.add("会议审批单");
		lstHeader.add("申请人");
		lstHeader.add("申请部门");
		lstHeader.add("会议名称");
		lstHeader.add("会议开始时间");
		lstHeader.add("会议结束时间");
		lstHeader.add("会议费用名称");
		lstHeader.add("费用预算（元）");
		lstHeader.add("实际费用（元）");
		lstHeader.add("费用预算合计（元）");
		lstHeader.add("实际费用合计（元）");
		lstHeader.add("差额（元）");
		lstHeader.add("会议其他要求");
		lstHeader.add("单据状态");
		exsl.setLstTitle(lstHeader);

		// excel文件中的一行
		List<String> lstRow = null;
		// excel文件中的所有行集合
		List lstRowSet = new ArrayList();
		// excel文件单行实体
		MeetApprove tempVisitorInfo = null;
        /** 费用预算合计**/
		Double totalPayBudget = 0.00;
        /** 实际费用合计*/
		Double totalPayReal = 0.00;
        /** 差额合计*/
		Double totalBalance = 0.00;
        /** 预计费用汇总 */
		Double	totalBudpayInall = 0.00;
		/** 实际费用汇总 */
		Double totalRealpayInall = 0.00;
		// 获得需要导出的excel文件内容
		PageObject pageObj = (PageObject) request.getSession().getAttribute("pageObjRegularWork");
		for (int i = 0; i < pageObj.getList().size(); i++) {
			lstRow = new ArrayList();
			tempVisitorInfo = (MeetApprove) pageObj.getList().get(i);
			// 设置行号
			lstRow.add(i + 1 + "");
			// 设置会议审批单
			if (tempVisitorInfo.getMeetId() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempVisitorInfo.getMeetId());
			}
			// 设置申请人
			if (tempVisitorInfo.getName() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempVisitorInfo.getName());
			}
			// 设置申请部门
			if (tempVisitorInfo.getDepName()== null) {
				lstRow.add("");
			} else {
				lstRow.add(tempVisitorInfo.getDepName());
			}
			// 设置会议名称
			if (tempVisitorInfo.getMeetName() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempVisitorInfo.getMeetName());
			}
			// 设置会议开始时间
			if (tempVisitorInfo.getStartMeetDate() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempVisitorInfo.getStartMeetDate());
			}
			// 设置会议结束时间
			if (tempVisitorInfo.getEndMeetDate() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempVisitorInfo.getEndMeetDate());
			}
			// 设置会议费用名称
			if (tempVisitorInfo.getPayName() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempVisitorInfo.getPayName());
			}
			// 设置费用预算
			if (tempVisitorInfo.getPayBudget() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempVisitorInfo.getPayBudget());
				totalPayBudget += Double.parseDouble(tempVisitorInfo.getPayBudget());
			}
			// 设置实际费用
			if (tempVisitorInfo.getPayReal() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempVisitorInfo.getPayReal());
				totalPayReal += Double.parseDouble(tempVisitorInfo.getPayReal());
			}
			// 设置费用预算合计
			if (tempVisitorInfo.getBudpayInall() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempVisitorInfo.getBudpayInall());
				totalBudpayInall += Double.parseDouble(tempVisitorInfo.getBudpayInall());
			}
			// 设置实际预算合计
			if (tempVisitorInfo.getRealpayInall() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempVisitorInfo.getRealpayInall());
				totalRealpayInall += Double.parseDouble(tempVisitorInfo.getRealpayInall());
				
			}
			// 设置差额
			if (tempVisitorInfo.getBalance() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempVisitorInfo.getBalance());
				totalBalance += Double.parseDouble(tempVisitorInfo.getBalance());
			}
			// 设置会议其他要求
			if (tempVisitorInfo.getMeetOther() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempVisitorInfo.getMeetOther());
			}
			// 设置单据状态
			if (tempVisitorInfo.getDcmStatus() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempVisitorInfo.getDcmStatus());
			}
			lstRowSet.add(lstRow);
		}
		lstRow = new ArrayList();
		lstRow.add("合计");
		lstRow.add("");
		lstRow.add("");
		lstRow.add("");
		lstRow.add("");
		lstRow.add("");
		lstRow.add("");
		lstRow.add("");
		lstRow.add(String.valueOf(totalPayBudget));
		lstRow.add(String.valueOf(totalPayReal));
		lstRow.add(String.valueOf(totalBudpayInall));
		lstRow.add(String.valueOf(totalRealpayInall));
		lstRow.add(String.valueOf(totalBalance));
		lstRowSet.add(lstRow);
		// 设置所有行内容
		exsl.setLstRow(lstRowSet);
		// 创建导出excel文件
		exsl.createXsl();
		LogUtil.log("Action:导出文件结束", Level.INFO, null);
		}catch (IOException e) {
            write(Constants.IO_FAILURE);
            LogUtil.log("Action:导出文件结束", Level.INFO, null);
		}catch (Exception e) {
            write(Constants.IO_FAILURE);
            LogUtil.log("Action:导出文件失败。", Level.SEVERE, e);
        }
	}
	/**
	 *  获得开始时间
	 * @return
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * 设置开始时间
	 * @param startDate
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * 获得结束时间
	 * @return
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 *  设置结束时间
	 * @param endDate
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * 获得部门编码
	 * @return
	 */
	public String getDepCode() {
		return depCode;
	}
	/**
	 * 设置部门编码
	 * @param depCode
	 */
	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	public String getOverSpend() {
		return overSpend;
	}
	public void setOverSpend(String overSpend) {
		this.overSpend = overSpend;
	}
	public String getDcmStatus() {
		return dcmStatus;
	}
	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}
}
