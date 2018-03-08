/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.web.administration.dutytypemainten.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdCDutytype;
import power.ejb.administration.AdCDutytypeFacadeRemote;
import power.ejb.administration.comm.ADCommonFacadeRemote;
import power.ejb.administration.comm.CodeCommonFacadeRemote;
import power.ejb.administration.comm.ComAdCRight;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 值别维护Action
 * 
 * @author chaihao
 * 
 */
public class DutyTypeMaintenAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 读取位置 */
	private int start = 0;
	/** 读取行数 */
	private int limit = 0;
	/** 值别实体 */
	private AdCDutytype dutyType;
	/** 值别维护处理远程对象 */
	AdCDutytypeFacadeRemote remote;
	/** 用户权限取得处理远程对象 */
	ADCommonFacadeRemote adcremote;
	/** 值别编码取得处理远程对象 */
	CodeCommonFacadeRemote ccremote;

	/**
	 * 构造函数
	 */
	public DutyTypeMaintenAction() {
		remote = (AdCDutytypeFacadeRemote) factory
				.getFacadeRemote("AdCDutytypeFacade");
		adcremote = (ADCommonFacadeRemote) factory
				.getFacadeRemote("ADCommonFacade");
		ccremote = (CodeCommonFacadeRemote) factory
				.getFacadeRemote("CodeCommonFacade");
	}

	/**
	 * 值别维护查询
	 * 
	 */
	public void dutyTypeQuery() {
		try {
			LogUtil.log("Action:查询值别开始", Level.INFO, null);
			// 获取用户权限
			String strUserRight = getUserRight();
			// 查询结果
			PageObject pob = remote.findByProperty(strUserRight, employee.getEnterpriseCode(), start, limit);
			if (pob.getTotalCount() != 0) {
				String strRes = JSONUtil.serialize(pob);
				write(strRes);
			} else {
				write("{totalCount:0,list:[]}");
			}
			LogUtil.log("Action:查询值别结束", Level.INFO, null);
		} catch (JSONException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:数据格式化失败", Level.SEVERE, e);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * 取得用户工作类别名
	 */
	public void getUserWorktypeName(){
		LogUtil.log("Action:取得用户工作类别名开始", Level.INFO, null);
		try {
			// 用户工作类别PageObject
			PageObject userRightPob = adcremote.getUserRight(employee.getWorkerCode(), employee.getEnterpriseCode());
			if (userRightPob.getList().size() > 0) {
				ComAdCRight objTemp = (ComAdCRight) userRightPob.getList().get(0);
				String strWorktypeName = objTemp.getWorktypeName();
				LogUtil.log("Action:取得用户工作类别名结束", Level.INFO, null);
				write("{tfWorkType:'" + strWorktypeName + "'}");
			} else {
				LogUtil.log("Action:取得用户工作类别名结束", Level.INFO, null);
				write("{tfWorkType:''}");
			}
		} catch (RuntimeException e) {
			LogUtil.log("Action:取得用户工作类别名失败", Level.SEVERE, e);
			throw e;
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}
	
	/**
	 * 取得用户权限
	 * 
	 * @return 用户权限
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	public String getUserRight() throws SQLException {
		LogUtil.log("Action:取得用户权限开始", Level.INFO, null);
		try {
			// 用户工作类别PageObject
			PageObject userRightPob = adcremote.getUserRight(employee.getWorkerCode(), employee.getEnterpriseCode());
			if (userRightPob.getList().size() > 0) {
				ComAdCRight objTemp = (ComAdCRight) userRightPob.getList().get(
						0);
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
	 * 新增值别
	 */
	public void addDutyType() {
		LogUtil.log("Action:新增值别开始", Level.INFO, null);
		try {
			// 设置工作类别
			dutyType.setWorktypeCode(getUserRight());
			// 设置值别
			dutyType.setDutyType(ccremote.getDutyCode());
			// 设置是否使用标志
			dutyType.setIsUse("Y");
			// 设置修改时间
			dutyType.setUpdateTime(new Date());
			// 设置修改人
			dutyType.setUpdateUser(employee.getWorkerCode());
			// 设置企业代码
			dutyType.setEnterpriseCode(employee.getEnterpriseCode());
			// 保存新增工作类别
			remote.save(dutyType);
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:新增值别结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("Action:新增失败", Level.SEVERE, re);
			throw re;
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * 修改值别
	 */
	public void modifyDutyType() {
		LogUtil.log("Action:修改值别开始", Level.INFO, null);
		try {
			// 按序号查找值别
			AdCDutytype model = remote.findById(dutyType.getId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			// 取得最后一次修改时间
			String strLastModifiedTime = "";
			if(model.getUpdateTime() != null){
				strLastModifiedTime = sdf.format(model.getUpdateTime());
			}
			// 取得上一次取得数据时的最后修改时间
			String strModifyingTime = "";
			if(dutyType.getUpdateTime() != null){
				strModifyingTime = sdf.format(dutyType.getUpdateTime());
			}

			// 排他
			if (!strLastModifiedTime.equals(strModifyingTime)) {
				write(Constants.DATA_USING);
				return;
			}
			// 设置值别名称
			model.setDutyTypeName(dutyType.getDutyTypeName());
			// 设置开始时间
			model.setStartTime(dutyType.getStartTime());
			// 设置结束时间
			model.setEndTime(dutyType.getEndTime());
			// 设置修改人
			model.setUpdateUser(employee.getWorkerCode());
			// 设置修改时间
			model.setUpdateTime(new Date());
			// 更新数据
			remote.update(model);
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:修改值别结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * 删除值别
	 */
	public void deleteDutyType() {
		LogUtil.log("Action:删除值别开始", Level.INFO, null);
		try {
			// 按序号查找值别
			AdCDutytype model = remote.findById(dutyType.getId());
			// 设置是否使用标志
			model.setIsUse("N");
			// 设置修改人
			model.setUpdateUser(employee.getWorkerCode());
			// 设置修改时间
			model.setUpdateTime(new Date());
			// 更新数据
			remote.update(model);
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除值别结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
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

	/**
	 * @return 值别实体
	 */
	public AdCDutytype getDutyType() {
		return dutyType;
	}

	/**
	 * @param 值别实体
	 *            设置值别实体
	 */
	public void setDutyType(AdCDutytype dutyType) {
		this.dutyType = dutyType;
	}
}
