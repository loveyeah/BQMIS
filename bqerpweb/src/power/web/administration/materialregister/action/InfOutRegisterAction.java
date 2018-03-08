/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.materialregister.action;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import bsh.ParseException;

import power.ear.comm.DataChangeException;
import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJOutreg;
import power.ejb.administration.AdJOutregFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 物资出门登记action
 * 
 * @author daichunlin
 * @version 1.0
 */
public class InfOutRegisterAction extends AbstractAction {
	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 物质出门登记entity */
	private AdJOutreg adJOutreg;
	/** 物质出门登记remote */
	private AdJOutregFacadeRemote typeRemote;
	/** 画面参数开始页 */
	public Long start;
	/** 画面参数页面最大值 */
	public Long limit;
	/** 画面参数序号 */
	public Long id;

	/**
	 * @return 序号
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param 序号
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return 画面参数开始页
	 */
	public Long getStart() {
		return start;
	}

	/**
	 * @param 画面参数开始页
	 */
	public void setStart(Long start) {
		this.start = start;
	}

	/**
	 * @return 画面参数页面最大值
	 */
	public Long getLimit() {
		return limit;
	}

	/**
	 * @param 画面参数页面最大值
	 */
	public void setLimit(Long limit) {
		this.limit = limit;
	}

	/**
	 * @return 物质出门登记
	 */
	public AdJOutreg getAdJOutreg() {
		return adJOutreg;
	}

	/**
	 * @param 物质出门登记
	 */
	public void setAdJOutreg(AdJOutreg adJOutreg) {
		this.adJOutreg = adJOutreg;
	}

	/**
	 * 构造函数
	 */
	public InfOutRegisterAction() {
		typeRemote = (AdJOutregFacadeRemote) factory
				.getFacadeRemote("AdJOutregFacade");
	}

	/**
	 * 页面加载
	 * 
	 * @throws JSONException
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	public void getInfoList() throws JSONException, ParseException,
			java.text.ParseException {
		LogUtil.log("Action:物资出门登记页面加载正常开始", Level.INFO, null);
		try {
			// 页面显示
			int intStart = Integer.parseInt(start.toString());
			int intLimit = Integer.parseInt(limit.toString());
			// 企业code
			employee = (Employee) session.getAttribute("employee");
			String strEnterpriseCode = employee.getEnterpriseCode();
			// 查询操作
			PageObject obj = typeRemote.getInfoList(strEnterpriseCode,
					intStart, intLimit);
			// 查询结果为null,设置页面显示
			if (obj.getTotalCount() < 0) {
				String str = "{\"list\":[],\"totalCount\":0}";
				write(str);
			} else {
				String str = JSONUtil.serialize(obj);
				write(str);
			}
			LogUtil.log("Action:物资出门登记页面加载正常结束", Level.INFO, null);

		} catch (JSONException je) {
			LogUtil.log("Action:物资出门登记页面加载失败。", Level.SEVERE, je);
		} catch (SQLException e) {
			LogUtil.log("Action:物资出门登记页面加载失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 增加信息
	 */
	public void addData() {
		LogUtil.log("Action:物资出门登记增加正常开始", Level.INFO, null);
		try {
			employee = (Employee) session.getAttribute("employee");
			// 企业code
			String strEnterpriseCode = employee.getEnterpriseCode();
			adJOutreg.setEnterpriseCode(strEnterpriseCode);
			// 添加人
			adJOutreg.setUpdateUser(employee.getWorkerCode().toString());
			// 是否使用
			adJOutreg.setIsUse("Y");
			String dteRequest = request.getParameter("outDate");
			DateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date;
			try {
				date = simpleDate.parse(dteRequest);
				adJOutreg.setOutDate(date);
			} catch (java.text.ParseException pe) {
				throw new SQLException();
			}
			// 增加一条记录
			typeRemote.save(adJOutreg);
			// 显示成功信息
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:物资出门登记增加正常结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:物资出门登记增加异常结束", Level.SEVERE, e);
		}
	}

	/**
	 * 修改信息
	 * 
	 * @throws ParseException
	 */
	public void updateData() throws ParseException {
		LogUtil.log("Action:物资出门登记修改正常开始", Level.INFO, null);
		try {
			employee = (Employee) session.getAttribute("employee");
			AdJOutreg adJOutregUpdate = new AdJOutreg();
			adJOutregUpdate = typeRemote.findById(adJOutreg.getId());
			// 添加人
			adJOutregUpdate.setUpdateUser(employee.getWorkerCode().toString());
			// 出门时间
			String dteoutDate = request.getParameter("outDate");
			DateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date;
			try {
				date = simpleDate.parse(dteoutDate);
				adJOutregUpdate.setOutDate(date);
			} catch (java.text.ParseException pe) {
				pe.printStackTrace();
			}
			// 页面传回时间
			String lastmodifiedDate = request.getParameter("updateTime")
					.substring(0, 10)
					+ " " + request.getParameter("updateTime").substring(11);
			// 经办人
			adJOutregUpdate.setAgent(adJOutreg.getAgent());
			// 物资规格
			adJOutregUpdate.setStandard(adJOutreg.getStandard());
			// 经办人所在单位
			adJOutregUpdate.setFirm(adJOutreg.getFirm());
			// 物资单位
			adJOutregUpdate.setUnit(adJOutreg.getUnit());
			// 物资数量			
			adJOutregUpdate.setNum(adJOutreg.getNum());
			// 物资名称
			adJOutregUpdate.setWpName(adJOutreg.getWpName());
			// 备注
			adJOutregUpdate.setNote(adJOutreg.getNote());
			// 物资出入原因
			adJOutregUpdate.setReason(adJOutreg.getReason());

			// 修改一条记录
			typeRemote.update(adJOutregUpdate, lastmodifiedDate);
			// 显示成功信息
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:物资出门登记修改结束", Level.INFO, null);
		} catch (SQLException se) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:物资出门登记修改失败", Level.SEVERE, se);
		} catch (DataChangeException de) {
			write(Constants.DATA_USING);
			LogUtil.log("Action:物资出门登记修改失败", Level.SEVERE, de);
		}
	}

	/**
	 * 上报时修改单据状态信息
	 * 
	 */
	public void sendData() {
		try {
			LogUtil.log("Action:物资出门登记单据状态开始", Level.INFO, null);
			employee = (Employee) session.getAttribute("employee");
			// 添加人
			String updater = employee.getWorkerCode().toString();
			// ID
			Long sendId = Long.parseLong(id.toString());
			// 更新时间
			String dteRequest = request.getParameter("updateTime").substring(0,
					10)
					+ " " + request.getParameter("updateTime").substring(11);

			// 修改一条记录
			typeRemote.updateState(updater, sendId, dteRequest);

			// 显示成功信息
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:物资出门登记单据状态结束", Level.INFO, null);
		} catch (DataChangeException e) {
			write(Constants.SQL_FAILURE);
		} catch (SQLException e) {
			write(Constants.DATA_USING);
		} catch (Exception e) {
			LogUtil.log("Action:物资出门登记单据状态结束", Level.SEVERE, e);
		}
	}

	/**
	 * 删除信息
	 * 
	 * @throws ParseException
	 */
	public void deleteData() throws ParseException {
		LogUtil.log("Action:物资出门登记删除正常开始", Level.INFO, null);
		employee = (Employee) session.getAttribute("employee");
		// 取得序号
		Long lngId = Long.parseLong(request.getParameter("id").toString());
		// 取得参数-上次修改时间
		AdJOutreg adJOutregFind = new AdJOutreg();
		adJOutregFind = typeRemote.findById(lngId);
		String modifiedDate = adJOutregFind.getUpdateTime().toString()
				.substring(0, 19);
		String lastModifiedDate = request.getParameter("updateTime").substring(
				0, 10)
				+ " " + request.getParameter("updateTime").substring(11);
		if (lngId != null) {
			// 排他检查
			if ((adJOutregFind.getIsUse().equals("Y"))
					&& (modifiedDate.equals(lastModifiedDate))) {
				String strEmployee = employee.getWorkerCode().toString();
				// 删除
				typeRemote.delete(lngId, strEmployee);
				// 显示删除成功信息
				write(Constants.DELETE_SUCCESS);
				LogUtil.log("Action:物资出门登记删除正常结束", Level.INFO, null);
			} else {
				write(Constants.DATA_USING);
				LogUtil.log("Action:物资出门登记删除异常结束", Level.INFO, null);
			}
		}
	}
}