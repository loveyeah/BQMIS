/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.workticket.workticketlocation.action;

import java.text.ParseException;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketLocation;
import power.ejb.workticket.RunCWorkticketLocationFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 工作票区域维护Action
 * 
 * @author 黄维杰
 * @version 1.0
 */
public class WorkTicketLocationAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 远程服务 */
	protected RunCWorkticketLocationFacadeRemote remote;
	/** 所属系统编码，对应页面所属系统组合框 */
	private String blockCode;
	/** 区域名称，对应弹出对话框的区域名称文本框对应页面区域名称文本框 */
	private String locationNamePop;
	/** 区域编码 */
	private String locationId;
	/** 排序号 */
	private String orderBy;
	/** 所属系统编码，对应弹出对话框的所属系统编码 */
	private String hidBlockCodePop;
	/** 区域名称，对应页面区域名称文本框 */
	private String locationName = "";
	/** 工作票区域bean */
	private RunCWorkticketLocation location;
	/** 批量删除时locationid组 */
	private String ids;

	/**
	 * 构造函数 实例化remote
	 */
	public WorkTicketLocationAction() {
		remote = (RunCWorkticketLocationFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketLocationFacade");
	}

	/**
	 * 查询
	 * 
	 * @throws ParseException
	 * @throws JSONException
	 */
	public void getLocationList() throws ParseException, JSONException {
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		// 从数据库查询从start开始的limit条记录
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findAll(Constants.ENTERPRISE_CODE, blockCode,
					locationName, start, limit);
			// 读取所有记录
		} else {
			obj = remote.findAll(Constants.ENTERPRISE_CODE, blockCode,
					locationName);
		}
		// 序列化输出字符串
		String str = JSONUtil.serialize(obj);
		// 以html形式输出
		write(str);
	}

	/**
	 * 删除
	 */
	public void deleteLocation() {
		remote.deleteMulti(ids);
		write(Constants.DELETE_SUCCESS);
	}

	/**
	 * 增加
	 */
	public void addLocation() throws CodeRepeatException {
		try {
			if (null == location) {
				location = new RunCWorkticketLocation();
			}
			// 设置区域属性
			// 设置所属系统
			location.setBlockCode(hidBlockCodePop);
			// 设置区域名称
			location.setLocationName(locationNamePop);
			// 设置更新者
			location.setModifyBy(employee.getWorkerCode());
			// 设置排序号
			if ("".equals(orderBy)) {
				location.setOrderBy(null);
			} else
				location.setOrderBy(Long.parseLong(orderBy));
			// 设置企业编号
			location.setEnterpriseCode(Constants.ENTERPRISE_CODE);
			remote.save(location);
			write(Constants.ADD_SUCCESS);
		} catch (CodeRepeatException e) {
			write(Constants.ADD_FAILURE);
			throw (e);
		}
	}

	/**
	 * 修改
	 */
	public void updateLocation() throws CodeRepeatException {
		try {
			if (null == location) {
				location = new RunCWorkticketLocation();
			}
			// 从数据库读取欲修改的那条记录
			RunCWorkticketLocation oldLocation = remote.findById(Long
					.parseLong(locationId));
			// 设置新的区域名称
			oldLocation.setLocationName(locationNamePop);
			// 设置新的排序号
			if ("".equals(orderBy)) {
				oldLocation.setOrderBy(null);
			} else
				oldLocation.setOrderBy(Long.parseLong(orderBy));
			remote.update(oldLocation);
			write(Constants.MODIFY_SUCCESS);
		} catch (CodeRepeatException e) {
			write(Constants.MODIFY_FAILURE);
			throw (e);
		}
	}

	/**
	 * @return 所属系统编码
	 */
	public String getBlockCode() {
		return blockCode;
	}

	/**
	 * @param blockCode
	 *            设置所属系统编码
	 */
	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}

	/**
	 * @return 弹出的编辑框的区域名称
	 */
	public String getLocationNamePop() {
		return locationNamePop;
	}

	/**
	 * @param locationNamePop
	 *            设置弹出的编辑框的区域名称
	 */
	public void setLocationNamePop(String locationNamePop) {
		this.locationNamePop = locationNamePop;
	}

	/**
	 * @return 区域Id
	 */
	public String getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId
	 *            设置区域Id
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return 排序号
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy
	 *            设置排序号
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return 弹出对话框的所属系统
	 */
	public String getHidBlockCodePop() {
		return hidBlockCodePop;
	}

	/**
	 * @param hidBlockCodePop
	 *            设置弹出对话框的所属系统
	 */
	public void setHidBlockCodePop(String hidBlockCodePop) {
		this.hidBlockCodePop = hidBlockCodePop;
	}

	/**
	 * @return 页面的区域名称
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @param locationName
	 *            设置页面的区域名称
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	/**
	 * @return 区域bean
	 */
	public RunCWorkticketLocation getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            设置区域bean
	 */
	public void setLocation(RunCWorkticketLocation location) {
		this.location = location;
	}

	/**
	 * @return 删除操作的Id序列
	 */
	public String getIds() {
		return ids;
	}

	/**
	 * @param ids
	 *            设置删除操作的Id序列
	 */
	public void setIds(String ids) {
		this.ids = ids;
	}
}