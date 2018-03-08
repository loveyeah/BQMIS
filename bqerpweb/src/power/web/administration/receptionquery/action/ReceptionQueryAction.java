/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.receptionquery.action;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.business.ReceptionQueryInfoFacadeRemote;
import power.ejb.administration.form.ReceptionQueryInfo;
import power.ejb.hr.LogUtil;
import power.web.comm.*;

/**
 * 接待审批查询Action
 * @author zhengzhipeng
 * @version 1.0
 */

public class ReceptionQueryAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
 
	/** 接待审批远程接口 */
	private ReceptionQueryInfoFacadeRemote remote;
	/** 导出excel文件名前缀 */
	private static final String STR_FILE_NAME = "接待审批查询";
    /** 查询参数: 开始时间 */
	private String startDate;
	/** 查询参数: 结束时间 */
	private String endDate;
	/** 查询参数: 部门编码 */
	private String dept;
	/** 查询参数: 人员编码 */
	private String worker;
	/** 查询参数: 是否超支 */
	private String isOver;
	/** 查询参数: 单据状态 */
	private String dcmStatus;
	/**
	 * 构造函数
	 */
	public ReceptionQueryAction() {
		// 远程处理对象的取得
		remote = (ReceptionQueryInfoFacadeRemote) factory.getFacadeRemote("ReceptionQueryInfoFacade");
	}
	/**
	 * 查询
	 */
	public void getReceptionInfoList(){
		try {
			LogUtil.log("Action:接待审批查询开始", Level.INFO, null);
			// 根据查询条件，取得相应信息
			PageObject object = remote.getReceptionQueryInfo(startDate, endDate, dept,
					worker, isOver, dcmStatus, employee.getEnterpriseCode());
			// 供导出
			request.getSession().setAttribute("pageObjReceptionInfo", object);
			// 查询结果为null,设置页面显示
			if (object.getList() != null) {
				String str = JSONUtil.serialize(object);
						 write(str);
			} else {
				String str = "{\"list\":[],\"totalCount\":0}";
				write(str);
			}
			LogUtil.log("Action:接待审批查询结束", Level.INFO, null);
		} catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:接待审批查询失败", Level.SEVERE, jsone);
        } catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:接待审批查询失败。", Level.SEVERE, sqle);
        }
	}
	/**
	 * 导出文件
	 * 
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void exportReceptionInfoFile() throws Exception {

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
		lstHeader.add("审批单号");
		lstHeader.add("申请部门");
		lstHeader.add("申请人");
		lstHeader.add("登记日期");
		lstHeader.add("接待日期");
		lstHeader.add("标准支出(元)");
		lstHeader.add("实际支出(元)");
		lstHeader.add("差额(元)");
		lstHeader.add("上报状态");
		exsl.setLstTitle(lstHeader);

		// excel文件中的一行
		List<String> lstRow = null;
		// excel文件中的所有行集合
		List lstRowSet = new ArrayList();
		// excel文件单行实体
		ReceptionQueryInfo tempInfo = null;
		// 获得需要导出的excel文件内容
		PageObject pageObj = (PageObject) request.getSession().getAttribute("pageObjReceptionInfo");
	    // 共计行数据
		Double payoutBzTotal = 0.00;
		Double payoutTotal = 0.00;
		Double balanceTotal = 0.00;
		int intCnt = pageObj.getList().size();
		for (int i = 0; i < intCnt; i++) {
			lstRow = new ArrayList();
			tempInfo = (ReceptionQueryInfo) pageObj.getList().get(i);
			// 设置行号
			lstRow.add(i + 1 + "");
			// 设置审批单号
			if (tempInfo.getApplyId() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getApplyId());
			}
			// 设置申请部门
			if (tempInfo.getApplyDeptName() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getApplyDeptName());
			}
			// 设置申请人
			if (tempInfo.getApplyManName() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getApplyManName());
			}
			// 设置登记日期
			if (tempInfo.getLogDate() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getLogDate());
			}
			// 设置接待日期
			if (tempInfo.getMeetDate() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getMeetDate());
			}
			// 设置标准支出
			if (tempInfo.getPayoutBz() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getPayoutBz());
				payoutBzTotal += Double.parseDouble(tempInfo.getPayoutBz());
			}
			// 设置实际支出
			if (tempInfo.getPayout() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getPayout());
				payoutTotal += Double.parseDouble(tempInfo.getPayout());
			}
			// 设置差额
			if (tempInfo.getBalance() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getBalance());
				balanceTotal += Double.parseDouble(tempInfo.getBalance());
			}
			// 设置单据状态
			if (tempInfo.getDcmStatus() == null) {
				lstRow.add("");
			} else {
				String dcmStatus = dcmStatusFormat(tempInfo.getDcmStatus());
				lstRow.add(dcmStatus);
			}
			lstRowSet.add(lstRow);
		}
		// 添加共计行数据
		List<String> lstTotalRow = new ArrayList();
		lstTotalRow.add("合计");
		lstTotalRow.add("");
		lstTotalRow.add("");
		lstTotalRow.add("");
		lstTotalRow.add("");
		lstTotalRow.add("");
		lstTotalRow.add(doubleFormat(payoutBzTotal));
		lstTotalRow.add(doubleFormat(payoutTotal));
		lstTotalRow.add(doubleFormat(balanceTotal));
		lstTotalRow.add("");
		lstRowSet.add(lstTotalRow);
		// 设置所有行内容
		exsl.setLstRow(lstRowSet);
		// 创建导出excel文件
		exsl.createXsl();
		LogUtil.log("Action:导出文件结束", Level.INFO, null);
	}
	/**
     * 单据状态名称化
     */
	private String dcmStatusFormat(String value){
		String newValue = "";
    	if(CodeConstants.FROM_STATUS_0.equals(value)){
    		newValue = "未上报";
    	}else if(CodeConstants.FROM_STATUS_1.equals(value)){
    		newValue = "已上报";
    	}else if(CodeConstants.FROM_STATUS_2.equals(value)){
    		newValue = "已终结";
    	}else if(CodeConstants.FROM_STATUS_3.equals(value)){
    		newValue = "已退回";
    	}
    	return newValue;
    }
	/**
	 * @return 开始时间
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param 开始时间
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return 结束时间
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param 结束时间
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return 部门编码
	 */
	public String getDept() {
		return dept;
	}
	/**
	 * @param 部门编码
	 */
	public void setDept(String dept) {
		this.dept = dept;
	}
	/**
	 * @return 人员编码
	 */
	public String getWorker() {
		return worker;
	}
	/**
	 * @param 人员编码
	 */
	public void setWorker(String worker) {
		this.worker = worker;
	}
	/**
	 * @return 是否超支
	 */
	public String getIsOver() {
		return isOver;
	}
	/**
	 * @param 是否超支
	 */
	public void setIsOver(String isOver) {
		this.isOver = isOver;
	}
	/**
	 * @return 单据状态
	 */
	public String getDcmStatus() {
		return dcmStatus;
	}
	/**
	 * @param 单据状态
	 */
	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}
    /**
     * double转string保留2位小数
     * @param d
     * @return
     */
    public static String doubleFormat(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }
}
