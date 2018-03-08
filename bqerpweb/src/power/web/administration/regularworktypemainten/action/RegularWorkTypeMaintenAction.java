/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.web.administration.regularworktypemainten.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdCWorktype;
import power.ejb.administration.AdCWorktypeFacadeRemote;
import power.ejb.administration.comm.CodeCommonFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 工作类别维护Action
 * 
 * @author chaihao
 * 
 */
public class RegularWorkTypeMaintenAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 工作类别维护序号 */
	private String strWorktypeCode = "";
	/** 读取位置 */
	private int start = 0;
	/** 读取记录数 */
	private int limit = 0;
	/** 工作类别实体 */
	private AdCWorktype worktype;
	/** 工作类别维护处理远程对象 */
	AdCWorktypeFacadeRemote remote;
	/** 编码生成处理远程对象 */
	CodeCommonFacadeRemote cremote;

	/**
	 * 构造函数
	 */
	public RegularWorkTypeMaintenAction() {
		remote = (AdCWorktypeFacadeRemote) factory
				.getFacadeRemote("AdCWorktypeFacade");
		cremote = (CodeCommonFacadeRemote) factory
				.getFacadeRemote("CodeCommonFacade");
	}

	/**
	 * 工作类别维护查询
	 * 
	 */
	public void workTypeQuery() {
		LogUtil.log("Action:定期工作查询开始", Level.INFO, null);
		try {
			// 按属性查找数据
			PageObject pob = remote.findByProperty(strWorktypeCode, employee.getEnterpriseCode(), start, limit);
			if (pob.getTotalCount() != 0) {
				String strRes = JSONUtil.serialize(pob);
				write(strRes);
			} else {
				write("{totalCount:0,list:[]}");
			}
			LogUtil.log("Action:定期工作查询结束", Level.INFO, null);
		} catch (JSONException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询失败", Level.SEVERE, e);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * 新增定期工作类别
	 * 
	 */
	public void addWorktype() {
		LogUtil.log("Action:新增定期工作类别开始", Level.INFO, null);
		try {
			// 设置子类别编码
			worktype.setSubWorktypeCode(cremote.getSubWorkTypeCode(worktype
					.getWorktypeCode()));
			// 设置是否使用标志
			worktype.setIsUse("Y");
			// 设置修改时间
			worktype.setUpdateTime(new Date());
			// 设置修改人
			worktype.setUpdateUser(employee.getWorkerCode());
			// 设置企业代码
			worktype.setEnterpriseCode(employee.getEnterpriseCode());
			// 保存新增工作类别
			remote.save(worktype);
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:新增定期工作类别结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * 修改定期工作类别
	 * 
	 */
	public void modifyWorktype() {
		LogUtil.log("Action:修改定期工作类别开始", Level.INFO, null);
		try {
			// 按序号查找工作类别
			AdCWorktype model = remote.findById(worktype.getId());
			// 该工作类别不存在
			if(model == null){
				write(Constants.DATA_USING);
				return;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			// 取得最后一次修改时间
			String strLastModifiedTime = sdf.format(model.getUpdateTime());
			// 取得上一次取得数据时的最后修改时间
			String strModifyingTime = sdf.format(worktype.getUpdateTime());

			// 排他
			if (!strLastModifiedTime.equals(strModifyingTime)) {
				write(Constants.DATA_USING);
				return;
			}
			// 设置子工作类别编码
			if (model.getSubWorktypeName() != null) {
				if (!model.getSubWorktypeName().equals(
						worktype.getSubWorktypeName())) {
					model.setSubWorktypeName(worktype.getSubWorktypeName());
					model.setSubWorktypeCode(cremote
							.getSubWorkTypeCode(worktype.getWorktypeCode()));
				}
			}
			// 设置检索码
			model.setRetrieveCode(worktype.getRetrieveCode());
			// 设置修改人
			model.setUpdateUser(employee.getWorkerCode());
			// 设置修改时间
			model.setUpdateTime(new Date());
			// 更新数据
			remote.update(model);
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:修改定期工作类别结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * 删除定期工作类别
	 * 
	 */
	public void deleteWorktype() {
		LogUtil.log("Action:删除定期工作类别开始", Level.INFO, null);
		try {
			// 按序号查找工作类别
			AdCWorktype model = remote.findById(worktype.getId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			// 取得最后一次修改时间
			String strLastModifiedTime = sdf.format(model.getUpdateTime());
			// 取得上一次取得数据时的最后修改时间
			String strModifyingTime = sdf.format(worktype.getUpdateTime());

			// 排他
			if (!strLastModifiedTime.equals(strModifyingTime)) {
				write(Constants.DATA_USING);
				return;
			}
			// 设置是否使用标志
			model.setIsUse("N");
			// 设置修改人
			model.setUpdateUser(employee.getWorkerCode());
			// 设置修改时间
			model.setUpdateTime(new Date());
			// 更新数据
			remote.update(model);
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除定期工作类别结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
		}
	}

	/**
	 * @return 工作类别实体
	 */
	public AdCWorktype getWorktype() {
		return worktype;
	}

	/**
	 * @param 工作类别实体
	 *            设置工作类别实体
	 */
	public void setWorktype(AdCWorktype worktype) {
		this.worktype = worktype;
	}

	/**
	 * @return 工作类别维护序号
	 */
	public String getStrWorktypeCode() {
		return strWorktypeCode;
	}

	/**
	 * @param 工作类别维护序号
	 *            设置工作类别维护序号
	 */
	public void setStrWorktypeCode(String strWorktypeCode) {
		this.strWorktypeCode = strWorktypeCode;
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
}
