/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.employee.laboursendsearch.action;

import java.sql.SQLException;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.LabourSendSearchFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
/**
 * 劳务派遣查询
 * 
 * @author li chensheng
 * @version 1.0
 */
public class LabourSendSearchAction extends AbstractAction{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 劳务派遣查询Remote */
	private LabourSendSearchFacadeRemote remote;
	/** 查询起始时间 */
	private String strStartDate;
	/** 查询结束时间 */
	private String strEndDate;
	/** 查询协作单位 */
	private String strCooperateUnit;
	/** 查询单据状态 */
	private String strDcmStatus;
	/** 开始行 */
	private int start;
	/** 结束行 */
	private int limit;
	/**劳务派遣合同ID */
	private Long  lngBorrowcontractid;
	/** 调动类型* */
	private String strTransferType;
	
	/**
	 * 构造函数
	 */
	public LabourSendSearchAction() {
		// 取得远程接口
		remote = (LabourSendSearchFacadeRemote) factory
				.getFacadeRemote("LabourSendSearchFacade");
	}
	
	/**
	 * 取得劳务派遣信息
	 */
	public void getLabourBy() {
		try {
			LogUtil.log("Action:劳务派遣查询开始。", Level.INFO, null);
			// 查询
			PageObject object = remote.ejbGetLabourBy(strStartDate, strEndDate, strCooperateUnit,
					strDcmStatus,employee.getEnterpriseCode(),strTransferType, start, limit);
			// 要写出的数据
			String strRecord = "";
			if (object.getTotalCount() > 0) {
				strRecord = JSONUtil.serialize(object);
			} else {
				strRecord = "{\"list\":[],\"totalCount\":null}";
			}
			write(strRecord);
			LogUtil.log("Action:劳务派遣查询正常结束", Level.INFO, null);
		} catch (SQLException e) {
			LogUtil.log("Action:劳务派遣查询异常结束", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		} catch (JSONException e) {
			LogUtil.log("Action:劳务派遣查询异常结束", Level.SEVERE, null);
			write(Constants.DATA_FAILURE);
		}
	}
	/**
	 * 取得人员一览grid中信息
	 */
	public void getEmpBy() {
		try{
			LogUtil.log("Action:人员一览查询开始。", Level.INFO, null);
			// 查询
			PageObject object = remote.ejbGetEmpBy(lngBorrowcontractid,employee.getEnterpriseCode());
			// 要写出的数据
			String strRecord = "";
			if (object.getTotalCount() > 0) {
				strRecord = JSONUtil.serialize(object);
			} else {
				strRecord = "{\"list\":[],\"totalCount\":null}";
			}
			write(strRecord);
			LogUtil.log("Action:人员一览查询正常结束", Level.INFO, null);
		}catch (SQLException e) {
			LogUtil.log("Action:人员一览查询异常结束", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		} catch (JSONException e) {
			LogUtil.log("Action:人员一览查询异常结束", Level.SEVERE, null);
			write(Constants.DATA_FAILURE);
		}
		
	};

	public String getStrStartDate() {
		return strStartDate;
	}

	public void setStrStartDate(String strStartDate) {
		this.strStartDate = strStartDate;
	}

	public String getStrEndDate() {
		return strEndDate;
	}

	public void setStrEndDate(String strEndDate) {
		this.strEndDate = strEndDate;
	}

	public String getStrCooperateUnit() {
		return strCooperateUnit;
	}

	public void setStrCooperateUnit(String strCooperateUnit) {
		this.strCooperateUnit = strCooperateUnit;
	}

	public String getStrDcmStatus() {
		return strDcmStatus;
	}

	public void setStrDcmStatus(String strDcmStatus) {
		this.strDcmStatus = strDcmStatus;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	public Long getLngBorrowcontractid() {
		return lngBorrowcontractid;
	}

	public void setLngBorrowcontractid(Long lngBorrowcontractid) {
		this.lngBorrowcontractid = lngBorrowcontractid;
	}

	public String getStrTransferType() {
		return strTransferType;
	}

	public void setStrTransferType(String strTransferType) {
		this.strTransferType = strTransferType;
	}
	
}
