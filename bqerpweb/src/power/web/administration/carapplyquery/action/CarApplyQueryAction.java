/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.carapplyquery.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJApplycarFacadeRemote;
import power.ejb.administration.form.VehicleDispatchBean;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.ExportXsl;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 用车申请查询Action
 * @author zhaozhijie
 * @version 1.0
 */

public class CarApplyQueryAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	/** 用车申请远程接口 */
	private AdJApplycarFacadeRemote remote;
	/** 导出excel文件名前缀 */
	private static final String STR_FILE_NAME = "用车申请查询";
		
	/**
	 * 构造函数
	 */
	public CarApplyQueryAction() {
		// 远程处理对象的取得
		remote = (AdJApplycarFacadeRemote) factory.getFacadeRemote("AdJApplycarFacade");
	}
	/**
	 * 查询
	 */
	public void getApplyCarInfo() throws JSONException {
		try {
			LogUtil.log("Action:用车申请查询开始", Level.INFO, null);
			// 取得查询参数: 开始时间
			String strStartDate = request.getParameter("startDate");
			// 取得查询参数: 结束时间
			String strEndDate = request.getParameter("endDate");
			// 取得查询参数: 部门编码
			String strdeptCode = request.getParameter("dept");
			// 取得查询参数: 人员编码
			String strWorkerCode = request.getParameter("worker");
			// 取得查询参数: 司机
			String strDriver = request.getParameter("driver");
			// 单据状态
			String strDcmStatus = request.getParameter("dcmStatus");
			// 开始行
			String strStart = request.getParameter("start");
			// 行数
			String strLimit = request.getParameter("limit");
			
			// 根据查询条件，取得相应信息
			PageObject object;
			
			object = remote.getApplyCarInfo(strStartDate, strEndDate, strdeptCode,strWorkerCode,
					strDriver, strDcmStatus, employee.getEnterpriseCode(), Integer.parseInt(strStart), Integer.parseInt(strLimit));
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
			LogUtil.log("Action:用车申请查询结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("Action:用车申请查询失败。", Level.SEVERE, re);
			throw re;
		}catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:用车申请查询失败。", Level.SEVERE, e);
		}

	}
	/**
	 * 导出文件
	 * 
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void exportApplyCarInfoFile() throws Exception{

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
		lstHeader.add("申请人");
		lstHeader.add("用车部门");
		lstHeader.add("用车日期");
		lstHeader.add("用车人数(人)");
		lstHeader.add("是否出省");
		lstHeader.add("路桥费(元)");
		lstHeader.add("油费(元)");
		lstHeader.add("行车里程(公里)");
		lstHeader.add("用车事由");
		lstHeader.add("发车时间");
		lstHeader.add("收车时间");
		lstHeader.add("到达地点");
		lstHeader.add("车牌号");
		lstHeader.add("司机");
		lstHeader.add("上报状态");

		exsl.setLstTitle(lstHeader);

		// excel文件中的一行
		List lstRow = null;
		// excel文件中的所有行集合
		List lstRowSet = new ArrayList();
		// excel文件单行实体
		VehicleDispatchBean tempInfo = null;
		// 获得需要导出的excel文件内容
		PageObject pageObj = (PageObject) request.getSession().getAttribute("pageObjReceptionInfo");
	    // 共计行数据
		double lqPayTotal = 0;
		double useOilTotal = 0;
		double distanceTotal = 0;
		int intCnt = pageObj.getList().size();
		for (int i = 0; i < intCnt; i++) {
			lstRow = new ArrayList();
			tempInfo = (VehicleDispatchBean) pageObj.getList().get(i);
			// 设置行号
			lstRow.add(i + 1 + "");
			// 设置申请人
			if (tempInfo.getApplyMan() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getApplyMan());
			}
			// 设置用车部门
			if (tempInfo.getDep() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getDep());
			}
			// 设置用车日期
			if (tempInfo.getUseDate() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getUseDate());
			}
			// 设置用车人数
			if (tempInfo.getUseNum() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getUseNum());
			}
			// 设置是否出省
			if (tempInfo.getIfOut() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getIfOut());
			}
			// 设置路桥费
			if (tempInfo.getLqPay() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getLqPay());
				double douLqPay = 0;
				if (tempInfo.getLqPay() != null && !"".equals(tempInfo.getLqPay())) {
					douLqPay = Double.parseDouble(tempInfo.getLqPay());
				}
				lqPayTotal += douLqPay;
			}
			// 设置油费
			if (tempInfo.getUseOil() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getUseOil());
				double douUseOil = 0;
				if (tempInfo.getUseOil() != null && !"".equals(tempInfo.getUseOil())) {
					douUseOil = Double.parseDouble(tempInfo.getUseOil());
				}
				useOilTotal += douUseOil;
			}
			// 设置行车里程
			if (tempInfo.getDistance() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getDistance());
				double douDistance = 0;
				if (tempInfo.getDistance() != null && !"".equals(tempInfo.getDistance())) {
					douDistance = Double.parseDouble(tempInfo.getDistance());
				}
				distanceTotal += douDistance;
			}
			// 设置用车事由
			if (tempInfo.getReason() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getReason());
			}
			// 设置发车时间
			if (tempInfo.getStartTime() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getStartTime());
			}
			// 设置收车时间
			if (tempInfo.getEndTime() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getEndTime());
			}
			// 设置到达地点
			if (tempInfo.getAim() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getAim());
			}
			// 设置车牌号
			if (tempInfo.getCarNo() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getCarNo());
			}
			// 设置司机
			if (tempInfo.getDriver() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getDriver());
			}
			// 设置上报状态
			if (tempInfo.getDcmStatus() == null) {
				lstRow.add("");
			} else {
				lstRow.add(tempInfo.getDcmStatus());
			}

			lstRowSet.add(lstRow);
		}
		// 添加共计行数据
		List lstTotalRow = new ArrayList();
		lstTotalRow.add("共计");
		lstTotalRow.add("");
		lstTotalRow.add("");
		lstTotalRow.add("");
		lstTotalRow.add("");
		lstTotalRow.add("");
		lstTotalRow.add(lqPayTotal);
		lstTotalRow.add(useOilTotal);
		lstTotalRow.add(distanceTotal);
		lstTotalRow.add("");
		lstTotalRow.add("");
		lstTotalRow.add("");
		lstTotalRow.add("");
		lstTotalRow.add("");
		lstTotalRow.add("");
		lstTotalRow.add("");
		lstRowSet.add(lstTotalRow);
		// 设置所有行内容
		exsl.setLstRow(lstRowSet);
		// 创建导出excel文件
		try {
			exsl.createXsl();
		} catch (IOException e) {
			write(Constants.IO_FAILURE);
			LogUtil.log("Action:导出文件失败。", Level.SEVERE, e);
		}
		LogUtil.log("Action:导出文件结束", Level.INFO, null);
	}
}
