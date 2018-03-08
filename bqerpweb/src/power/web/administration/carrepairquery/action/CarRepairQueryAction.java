/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.web.administration.carrepairquery.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.business.CarRepairQueryFacadeRemote;
import power.ejb.administration.form.CarRepairQueryInfo;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.ExportXsl;

/**
 * @author chaihao
 * 
 */
public class CarRepairQueryAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 导出文件名前缀 */
	private static final String STR_FILE_NAME = "车辆维修";
	/** 读取位置 */
	private int start = 0;
	/** 读取记录数 */
	private int limit = 0;
	/** 车辆维护序号 */
	private String strWhId = "";
	/** 项目编码 */
	private String strProCode = "";
	/** 起始日期 */
	private String strStartDate = "";
	/** 截止日期 */
	private String strEndDate = "";
	/** 经办人 */
	private String strManager = "";
	/** 车牌号 */
	private String strCarNo = "";
	/** 单据状态 */
	private String strDcmStatus = "";
	/** 预算费用合计 */
	private String strPrePrice = "";
	/** 实际费用合计 */
	private String strRealPrice = "";
	/** 车辆维修查询远程对象 */
	CarRepairQueryFacadeRemote remote;

	/**
	 * 构造函数
	 */
	public CarRepairQueryAction() {
		remote = (CarRepairQueryFacadeRemote) factory
				.getFacadeRemote("CarRepairQueryFacade");
	}
	
	/**
	 * 车辆维修查询
	 */
	public void carRepairQuery() {
		LogUtil.log("Action:车辆维修查询开始", Level.INFO, null);
		try {
			PageObject pob = remote.getCarRepair(employee.getEnterpriseCode(), strStartDate, strEndDate,
					strManager, strCarNo, strDcmStatus);
			request.getSession().setAttribute("pageObjCarRepairInfo", pob);
			if(pob.getTotalCount() != 0){
				String strRes = JSONUtil.serialize(pob);
				write(strRes);
			} else {
				write("{totalCount:0,list:[]}");
			}
			LogUtil.log("Action:车辆维修查询结束", Level.INFO, null);
		} catch (SQLException e) {
			LogUtil.log("Action:数据库检索失败", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
		} catch (JSONException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:格式化数据失败", Level.SEVERE, e);
		}
	}

	/**
	 * 车辆维修明细查询
	 */
	public void carRepairListQuery(){
		LogUtil.log("Action:车辆维修明细查询开始", Level.INFO, null);
		try{
			PageObject pob = remote.getCarRepairList(employee.getEnterpriseCode(), strWhId, strProCode, start, limit);
			if(pob.getTotalCount() != 0){
				String strRes = JSONUtil.serialize(pob);
				write(strRes);
			} else {
				write("{totalCount:0,list:[]}");
			}
			LogUtil.log("Action:车辆维修明细查询结束", Level.INFO, null);
		} catch (SQLException e) {
			LogUtil.log("Action:数据库检索失败", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
		} catch (JSONException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:格式化数据失败", Level.SEVERE, e);
		}
	}
	
	/**
	 * 检索车牌号
	 */
	public void carRepairCarNoGet() {
		LogUtil.log("Action:检索车牌号开始", Level.INFO, null);
		try {
			LogUtil.log("Action:检索车牌号开始", Level.INFO, null);
			// 检索车牌号
			PageObject pobj = remote.getCarNo(employee.getEnterpriseCode());
			String strRes;
			strRes = JSONUtil.serialize(pobj);
			write(strRes);
			LogUtil.log("Action:检索车牌号结束", Level.INFO, null);
		} catch (JSONException jsone) {
			LogUtil.log("Action:数据格式化失败", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException e) {
			LogUtil.log("Action:数据库检索失败", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 导出文件
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void exportCarRepairInfo() throws Exception{

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
			lstHeader.add("维修申请单号");
			lstHeader.add("上报状态");
			lstHeader.add("车牌号");
			lstHeader.add("车名");
			lstHeader.add("修理日期");
			lstHeader.add("维修项目");
			lstHeader.add("预算费用(元)");
			lstHeader.add("实际费用(元)");
			lstHeader.add("实际费用合计(元)");
			lstHeader.add("维修单位");
			lstHeader.add("维修里程(公里)");
			lstHeader.add("经办人");
			lstHeader.add("支出事由");
			lstHeader.add("备注");
			exsl.setLstTitle(lstHeader);

			// excel文件中的一行
			List<String> lstRow = null;
			// excel文件中的所有行集合
			List lstRowSet = new ArrayList();
			// excel文件单行实体
			CarRepairQueryInfo tempCarRepairInfo = null;
			// 获得需要导出的excel文件内容
			PageObject pageObj = (PageObject) request.getSession()
					.getAttribute("pageObjCarRepairInfo");
			for (int i = 0; i < pageObj.getList().size(); i++) {
				lstRow = new ArrayList();
				tempCarRepairInfo = (CarRepairQueryInfo) pageObj.getList().get(i);
				// 设置行号
				lstRow.add(i + 1 + "");
				// 设置车辆维护序号
				if (tempCarRepairInfo.getWhId() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarRepairInfo.getWhId());
				}
				// 设置单据状态
				if (tempCarRepairInfo.getDcmStatus() == null || tempCarRepairInfo.getDcmStatus().equals("")) {
					lstRow.add("");
				} else {
					if (tempCarRepairInfo.getDcmStatus().equals("0")) {
						lstRow.add("未上报");
					} else if (tempCarRepairInfo.getDcmStatus().equals("1")) {
						lstRow.add("已上报");
					} else if (tempCarRepairInfo.getDcmStatus().equals("2")) {
						lstRow.add("已终结");
					} else {
						lstRow.add("已退回");
					}
				}
				// 设置车牌号
				if (tempCarRepairInfo.getCarNo() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarRepairInfo.getCarNo());
				}
				// 设置车名
				if (tempCarRepairInfo.getCarName() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarRepairInfo.getCarName());
				}
				// 设置维修日期
				if (tempCarRepairInfo.getRepairDate() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarRepairInfo.getRepairDate());
				}
				// 设置项目名称
				if (tempCarRepairInfo.getProName() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarRepairInfo.getProName());
				}
				// 设置预算费用
				if (tempCarRepairInfo.getPrice() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarRepairInfo.getPrice());
				}
				// 设置实际费用
				if (tempCarRepairInfo.getRealPrice() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarRepairInfo.getRealPrice());
				}
				// 设置实际费用合计
				if (tempCarRepairInfo.getRealSum() == null || tempCarRepairInfo.getRealSum().equals("")) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarRepairInfo.getRealSum());
				}
				// 设置维修单位
				if (tempCarRepairInfo.getCpName() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarRepairInfo.getCpName());
				}
				// 设置维修里程
				if (tempCarRepairInfo.getDriveMile() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarRepairInfo.getDriveMile());
				}
				// 设置经办人
				if (tempCarRepairInfo.getChsName() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarRepairInfo.getChsName());
				}
				// 设置支出事由
				if (tempCarRepairInfo.getReason() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarRepairInfo.getReason());
				}
				// 设置备注
				if (tempCarRepairInfo.getMemo() == null) {
					lstRow.add("");
				} else {
					lstRow.add(tempCarRepairInfo.getMemo());
				}
				lstRowSet.add(lstRow);
			}
			// 添加合计行
			List<String> lstSum = new ArrayList<String>(15);
			lstSum.add("合计");
			int i = 1;
			while (i < 7) {
				lstSum.add("");
				i++;
			}
			lstSum.add(strPrePrice);
			lstSum.add(strRealPrice);
			i = 0;
			while (i < 5) {
				lstSum.add("");
				i++;
			}
			lstRowSet.add(lstSum);
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
	 * @return the strStartDate
	 */
	public String getStrStartDate() {
		return strStartDate;
	}

	/**
	 * @param strStartDate the strStartDate to set
	 */
	public void setStrStartDate(String strStartDate) {
		this.strStartDate = strStartDate;
	}

	/**
	 * @return the strEndDate
	 */
	public String getStrEndDate() {
		return strEndDate;
	}

	/**
	 * @param strEndDate the strEndDate to set
	 */
	public void setStrEndDate(String strEndDate) {
		this.strEndDate = strEndDate;
	}

	/**
	 * @return the strManager
	 */
	public String getStrManager() {
		return strManager;
	}

	/**
	 * @param strManager the strManager to set
	 */
	public void setStrManager(String strManager) {
		this.strManager = strManager;
	}

	/**
	 * @return the strCarNo
	 */
	public String getStrCarNo() {
		return strCarNo;
	}

	/**
	 * @param strCarNo the strCarNo to set
	 */
	public void setStrCarNo(String strCarNo) {
		this.strCarNo = strCarNo;
	}

	/**
	 * @return the strDcmStatus
	 */
	public String getStrDcmStatus() {
		return strDcmStatus;
	}

	/**
	 * @param strDcmStatus the strDcmStatus to set
	 */
	public void setStrDcmStatus(String strDcmStatus) {
		this.strDcmStatus = strDcmStatus;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
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
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the strWhId
	 */
	public String getStrWhId() {
		return strWhId;
	}

	/**
	 * @param strWhId the strWhId to set
	 */
	public void setStrWhId(String strWhId) {
		this.strWhId = strWhId;
	}

	/**
	 * @return the strProCode
	 */
	public String getStrProCode() {
		return strProCode;
	}

	/**
	 * @param strProCode the strProCode to set
	 */
	public void setStrProCode(String strProCode) {
		this.strProCode = strProCode;
	}

	/**
	 * @return the strPrePrice
	 */
	public String getStrPrePrice() {
		return strPrePrice;
	}

	/**
	 * @param strPrePrice the strPrePrice to set
	 */
	public void setStrPrePrice(String strPrePrice) {
		this.strPrePrice = strPrePrice;
	}

	/**
	 * @return the strRealPrice
	 */
	public String getStrRealPrice() {
		return strRealPrice;
	}

	/**
	 * @param strRealPrice the strRealPrice to set
	 */
	public void setStrRealPrice(String strRealPrice) {
		this.strRealPrice = strRealPrice;
	}
}
