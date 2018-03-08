/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.document.jobmanagement.intype;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCIntype;
import power.ejb.hr.HrCIntypeFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 员工进厂类别维护Action
 * 
 * @author liuxin
 * 
 */
public class InTypeAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private HrCIntypeFacadeRemote remote;
	private HrCIntype entity;
	/** 进厂类别ID */
	private String inTypeId;
	/** 进厂类别 */
	private String inType;
	/** 显示顺序 */
	private String orderBy;
	/** 分页信息 */
	public int start;
	public int limit;

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

	public String getInTypeId() {
		return inTypeId;
	}

	public void setInTypeId(String inTypeId) {
		this.inTypeId = inTypeId;
	}

	public String getInType() {
		return inType;
	}

	public void setInType(String inType) {
		this.inType = inType;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	// 构造函数
	public InTypeAction() {
		remote = (HrCIntypeFacadeRemote) factory
				.getFacadeRemote("HrCIntypeFacade");
	}

	/**
	 * 员工进厂类别维护查询
	 */
	public void getInTypeList() {
		LogUtil.log("Action:员工进厂类别维护查询开始。", Level.INFO, null);
		PageObject obj = new PageObject();
		List<HrCIntype> list = new ArrayList<HrCIntype>();
		list = remote.queryAllInType(Constants.IS_USE_Y, employee.getEnterpriseCode());
        obj.setTotalCount(new Long(list.size()));
		obj.setList(remote.queryAllInType(Constants.IS_USE_Y, employee
				.getEnterpriseCode(), start, limit));
		String str = null;
		try {
			str = JSONUtil.serialize(obj);
			write(str);
			LogUtil.log("Action:员工进厂类别维护查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:员工进厂类别维护查询失败。", Level.WARNING, null);
			write("failture");
		}
	}

	/**
	 * 增加员工进厂类别维护
	 */
	public void addInType() {
		LogUtil.log("Action:增加员工进厂类别维护开始。", Level.INFO, null);
		try {
			entity = new HrCIntype();
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			entity.setInsertby(employee.getWorkerCode());
			entity.setInsertdate(new Date());
			entity.setIsUse(Constants.IS_USE_Y);
			entity.setLastModifiedBy(employee.getWorkerCode());
			entity.setLastModifiedDate(new Date());
			if (!"".equals(orderBy)) {
				entity.setOrderBy(new Long(orderBy));
			}
			entity.setInType(inType);
			remote.save(entity);
			write("success");
			LogUtil.log("Action:增加员工进厂类别维护成功。", Level.INFO, null);
		} catch (Exception e) {
			write("failture");
			LogUtil.log("Action:增加员工进厂类别维护失败。", Level.WARNING, null);
		}
	}

	/**
	 * 修改员工进厂类别维护
	 */
	public void updateInType() {
		try {
			LogUtil.log("Action:修改员工进厂类别维护开始。", Level.INFO, null);
			if (!remote.findByIdIsuseCode(new Long(inTypeId),
					Constants.IS_USE_Y, employee.getEnterpriseCode()))
				write("notfind");
			else {
				entity = new HrCIntype();
				entity = remote.findById(new Long(inTypeId));
				entity.setInType(inType);
				entity.setLastModifiedBy(employee.getWorkerCode());
				entity.setLastModifiedDate(new Date());
				if (!"".equals(orderBy)) {
					entity.setOrderBy(new Long(orderBy));
				} else
					entity.setOrderBy(null);
				remote.update(entity);
				write("success");
				LogUtil.log("Action:修改员工进厂类别维护成功。", Level.INFO, null);
			}
		} catch (Exception e) {
			LogUtil.log("Action:修改员工进厂类别维护失败。", Level.WARNING, null);
		}
	}

	/**
	 * 删除员工进厂类别维护
	 */
	public void deleteInType() {
		try {
			LogUtil.log("Action:删除员工进厂类别维护开始。", Level.INFO, null);
			if (!remote.findByIdIsuseCode(new Long(inTypeId),
					Constants.IS_USE_Y, employee.getEnterpriseCode()))
				write("notfind");
			else {
				entity = new HrCIntype();
				entity = remote.findById(new Long(inTypeId));
				entity.setLastModifiedBy(employee.getWorkerCode());
				entity.setLastModifiedDate(new Date());
				entity.setIsUse(Constants.IS_USE_N);
				remote.update(entity);
				write("success");
				LogUtil.log("Action:删除员工进厂类别维护成功。", Level.INFO, null);
			}
		} catch (Exception e) {
			LogUtil.log("Action:删除员工进厂类别维护失败。", Level.WARNING, null);
			write("faiture");
		}
	}
}
