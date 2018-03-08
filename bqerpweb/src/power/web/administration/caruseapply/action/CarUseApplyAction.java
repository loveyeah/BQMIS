/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.caruseapply.action;

import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJApplycar;
import power.ejb.administration.AdJApplycarFacadeRemote;
import power.ejb.administration.comm.CodeCommonFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 用车申请
 * 
 * @author li chensheng
 * @version 1.0
 */
public class CarUseApplyAction extends AbstractAction {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 查询，保存，修改数据远程对象 */
	private AdJApplycarFacadeRemote carApplyRemote;
	/** 编码生成远程对象 */
	private CodeCommonFacadeRemote codeCommon;
	/** 分页 START */
	private int start;
	/** 分页 LIMIT */
	private int limit;
	/** 流水号 */
	private Long carIdValue;
	/** 用车申请entity */
	private AdJApplycar applyCar;
	/** 是否出省标记 */
	private static final String IF_OUT_VALUE = "drpIfOutValue";

	/**
	 * 构造函数
	 */
	public CarUseApplyAction() {
		carApplyRemote = (AdJApplycarFacadeRemote) factory
				.getFacadeRemote("AdJApplycarFacade");
		codeCommon = (CodeCommonFacadeRemote)factory
               .getFacadeRemote("CodeCommonFacade");
	}

	/**
	 * @return 用车申请entity
	 */
	public AdJApplycar getApplyCar() {
		return applyCar;
	}

	/**
	 * @param 用车申请entity
	 */
	public void setApplyCar(AdJApplycar applyCar) {
		this.applyCar = applyCar;
	}

	/**
	 * @return 分页START
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param 分页START
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return 分页LIMIT
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param 分页LIMIT
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return 流水号
	 */
	public Long getCarIdValue() {
		return carIdValue;
	}

	/**
	 * @param 流水号
	 */
	public void setCarIdValue(Long carIdValue) {
		this.carIdValue = carIdValue;
	}

	/**
	 * 取当前用户信息
	 */
	public void getUserInfo(){
		String strUserCode = employee.getWorkerCode();
		String strUserName = employee.getWorkerName();
		String strDepName = employee.getDeptName();
		write("{userCode:'" + strUserCode + "',userName:'"
		+ strUserName + "',depName:'" + strDepName + "'}");
	}

	/**
	 * 查询用车申请信息
	 * 
	 * @throws JSONException
	 * 
	 */
	public void searchCarApply() throws JSONException {
		try {
			LogUtil.log("Action:查询用车申请信息开始。", Level.INFO, null);
			// 取得菜谱类别编码
			PageObject obj = carApplyRemote.findCarApply(employee
					.getWorkerCode(),employee.getEnterpriseCode(), start, limit);
			if (obj != null) {
				String retStr = JSONUtil.serialize(obj.getList());
				write("{data:{totalCount:" + obj.getTotalCount() + ",list:"
						+ retStr + "}}");
			} else
				write("{data:{totalCount:0,list:[]}");
			LogUtil.log("Action:查询用车申请信息结束。", Level.INFO, null);
		} catch (SQLException e) {
			// 显示失败
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:查询用车申请信息失败", Level.SEVERE, null);
		} catch (JSONException je) {
			// 显示失败
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询用车申请信息失败", Level.SEVERE, null);
		}
	}

	/**
	 * 删除用车申请信息
	 * 
	 * @throws JSONException
	 * 
	 */
	public void deleteCarApply() throws JSONException {
		LogUtil.log("Action:删除用车申请信息开始。", Level.INFO, null);
		try {
			carApplyRemote.delete(carIdValue, employee.getWorkerCode());
			// 删除成功
			write(Constants.DELETE_SUCCESS);
		} catch (SQLException e) {
			// 显示失败
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:删除用车申请信息失败。", Level.SEVERE, null);
		}
		LogUtil.log("Action:删除用车申请信息结束。", Level.INFO, null);
	}

	/**
	 * 增加用车申请信息
	 */
	public void addCarApply() throws JSONException {
		LogUtil.log("Action:增加用车申请信息开始。", Level.INFO, null);
		try {
			// 取得是否出省标记
			String ifOut = request.getParameter(IF_OUT_VALUE);
			try {
				//共通用车申请单号  
				String applyCode;
				applyCode = codeCommon.getCarAppNo(employee.getWorkerCode().toString());
				//添加用车申请单号 
				applyCar.setCarApplyId(applyCode);
			} catch (SQLException e) {
			    // 显示失败
				write(Constants.SQL_FAILURE);
			}
			// 添加出省标记
			applyCar.setIfOut(ifOut);
			// 是否使用
			applyCar.setIsUse("Y");
			// 设置单据状态
			applyCar.setDcmStatus("0");
			// 设定更新者
			applyCar.setUpdateTime(new Date());
			// 添加修改人
			applyCar.setUpdateUser(employee.getWorkerCode());
			// 增加企业代码
			applyCar.setEnterpriseCode(employee.getEnterpriseCode());
			// 增加一条记录
			carApplyRemote.save(applyCar);
			// 显示成功信息
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:增加用车申请信息结束。", Level.INFO, null);
		} catch (SQLException ce) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:增加用车申请信息失败。", Level.SEVERE, ce);
		}
	}

	/*
	 * 修改用车申请信息 @param
	 */
	public void updateCarApply() throws JSONException {
		LogUtil.log("Action:修改用车申请信息开始。", Level.INFO, null);
		try {
			// 取得是否出省标记
			String ifOut = request.getParameter(IF_OUT_VALUE);
			// 取得entity
			AdJApplycar applyCarEntity = carApplyRemote.findById(applyCar
					.getId());
			// 设定用车人数
			applyCarEntity.setUseNum(this.applyCar.getUseNum());
			// 设定是否出省
			applyCarEntity.setIfOut(ifOut);
			// 设定用车时间
			applyCarEntity.setUseDate(this.applyCar.getUseDate());
			// 设定用车天数
			applyCarEntity.setUseDays(this.applyCar.getUseDays());
			// 设定到达地点
			applyCarEntity.setAim(this.applyCar.getAim());
			// 设定用车事由
			applyCarEntity.setReason(this.applyCar.getReason());
			// 设定更新时间
			applyCarEntity.setUpdateTime(new Date());
			// 设定更新者code
			applyCarEntity.setUpdateUser(employee.getWorkerCode());
			// 修改一条记录
			carApplyRemote.update(applyCarEntity);
			// 显示成功信息
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:修改用车申请信息成功。", Level.INFO, null);
		} catch (Exception ce) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:修改用车申请信息失败。", Level.SEVERE, ce);
		}
	}

	/*
	 * 上报用车申请
	 * 
	 */
	public void reportCarApply() throws JSONException {
		LogUtil.log("Action:上报用车申请单据状态开始", Level.INFO, null);
		try {
			// 上报处理
			carApplyRemote.updateState(carIdValue, employee.getWorkerCode());
			// 上报成功
			write(Constants.SIGN_SUCCESS);
			LogUtil.log("Action:上报用车申请单据状态结束", Level.INFO, null);
		} catch (SQLException e) {
			// 显示失败
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:上报用车申请单据状态失败", Level.SEVERE, null);
		}
	}

}
