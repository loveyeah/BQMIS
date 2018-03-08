/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.employee.cooperateunitmaintain.action;

import java.sql.SQLException;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJCooperateunit;
import power.ejb.hr.HrJCooperateunitFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 协作单位维护
 * 
 * @author zhaozhijie
 * @version 1.0
 */
public class CooperateUnitMaintainAction  extends AbstractAction {

	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 协作单位维护ejb远程维护对象 */
	protected HrJCooperateunitFacadeRemote remote;
	/** 协作单位维护实体 */
	private HrJCooperateunit cooperateUnit;
	/**是否使用*/
	private String IS_USE_Y = "Y";
	/**  协作单位id */
	private String id;
	/** 开始 */
	private String start;
	/** 限制 */
	private String limit;

	/**
	 * 构造函数
	 */
	public CooperateUnitMaintainAction() {
		// 协作单位维护ejb远程维护对象
		remote = (HrJCooperateunitFacadeRemote) factory
		.getFacadeRemote("HrJCooperateunitFacade");
	}

	/**
	 * 查询协作单位维护信息
	 */
	public void getCooperateUnitList() {
		LogUtil.log("Action:协作单位维护信息查询开始", Level.INFO, null);
		try {
			PageObject obj = remote.getCooperateUnit(employee.getEnterpriseCode(),
					Integer.parseInt(start), Integer.parseInt(limit));
			write(JSONUtil.serialize(obj));
			LogUtil.log("Action:协作单位维护信息查询结束", Level.INFO, null);
		} catch (SQLException e) {
			//显示失败
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:协作单位维护信息查询失败。", Level.SEVERE, null);
		} catch (JSONException je) {
			//显示失败
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:协作单位维护信息查询失败。", Level.SEVERE, null);
		} 
	}

	/**
	 * 保存操作
	 */
	public void saveCooperateUnit() {
		 try{
			 // 新增操作
			 if (cooperateUnit.getCooperateUnitId() == -1) {
				 LogUtil.log("Action:新增协作单位维护表开始。", Level.INFO, null);
				 // 设置修改人
				 cooperateUnit.setLastModifiedBy(employee.getWorkerCode());
				 // 设置修改时间
				 cooperateUnit.setLastModifiedDate(new java.util.Date());
				 // 设置企业代码
				 cooperateUnit.setEnterpriseCode(employee.getEnterpriseCode());
				 // 是否使用
				 cooperateUnit.setIsUse(IS_USE_Y);
				 remote.save(cooperateUnit);
				 write(Constants.ADD_SUCCESS);
				 LogUtil.log("Action:新增协作单位维护表结束。", Level.INFO, null);
	         // 修改操作
			 } else {
				 LogUtil.log("Action:更新协作单位维护表开始。", Level.INFO, null);
				 // 所需修改的数据
				 HrJCooperateunit model = remote.findById(cooperateUnit.getCooperateUnitId());
				 // 设置修改人
				 model.setLastModifiedBy(employee.getWorkerCode());
				 // 设置显示顺序
				 model.setOrderBy(cooperateUnit.getOrderBy());
				 // 设置协作单位名称
				 model.setCooperateUnit(cooperateUnit.getCooperateUnit());
				 // 设置修改时间
				 model.setLastModifiedDate(new java.util.Date());
				 remote.update(model);
				 write(Constants.MODIFY_SUCCESS);
				 LogUtil.log("Action:更新协作单位维护表结束。", Level.INFO, null);
			 }
		 } catch (SQLException e) {
				write(Constants.SQL_FAILURE);
				LogUtil.log("Action:更新协作单位维护表失败", Level.SEVERE, e);
		 }
	}

	/**
	 * 删除操作
	 */
	public void deleteCooperateUnit() {
		LogUtil.log("Action:删除协作单位维护开始", Level.INFO, null);
		try{
			// 所需删除的数据
			HrJCooperateunit model = remote.findById(Long.parseLong(id));
			// 设置是否使用标志
			model.setIsUse(Constants.IS_USE_N);
			// 设置修改人
			model.setLastModifiedBy(employee.getWorkerCode());
			// 设置修改时间
			model.setLastModifiedDate(new java.util.Date());
			// 更新数据
			remote.update(model);
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除协作单位维护结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:删除协作单位维护失败", Level.SEVERE, e);
		}
	}

	/**
	 * @return 协作单位维护实体
	 */
	public HrJCooperateunit getCooperateUnit() {
		return cooperateUnit;
	}

	/**
	 * @param 协作单位维护实体
	 */
	public void setCooperateUnit(HrJCooperateunit cooperateUnit) {
		this.cooperateUnit = cooperateUnit;
	}

	/**
	 * @return 协作单位id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param 协作单位id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return 开始
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param 开始
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @return 限制
	 */
	public String getLimit() {
		return limit;
	}

	/**
	 * @param 限制
	 */
	public void setLimit(String limit) {
		this.limit = limit;
	}
}
