package power.web.manage.budget.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmCCenter;
import power.ejb.manage.budget.CbmCCenterFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BudgetDeptAction extends AbstractAction{

	private CbmCCenterFacadeRemote remote;
	private CbmCCenter dept;
	private Long id;
	
	public BudgetDeptAction()
	{
		remote = (CbmCCenterFacadeRemote)factory.getFacadeRemote("CbmCCenterFacade");
	}
	
	/**
	 * 增加一条预算部门记录
	 */
	public void addBudgetDept()
	{
		dept.setEnterpriseCode(employee.getEnterpriseCode());
		try{
			dept = remote.save(dept);
			write("{success:true,id:'"+dept.getCenterId()+"',code:'"+dept.getDepCode()+"',msg:'增加成功！'}");
		}catch (CodeRepeatException e) {
			write("{success:true,msg:'增加失败:该部门已加入预算！'}");
		}
	}
	
	/**
	 * 修改一条预算部门记录
	 */
	public void updateBudgetDept()
	{
		CbmCCenter model = remote.findById(dept.getCenterId());
		model.setDepCode(dept.getDepCode());
		model.setDepName(dept.getDepName());
		model.setIfDuty(dept.getIfDuty());
		model.setManager(dept.getManager());
		model.setCostCode(dept.getCostCode());
		try {
			remote.update(model);
			write("{success:true,id:'"+dept.getCenterId()+"',code:'"+dept.getDepCode()+"',msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'修改失败:该部门已加入预算！'}");
		}
	}
	
	/**
	 * 删除一条或多条预算部门记录
	 */
	public void deleteBudgetDept()
	{
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 预算部门记录列表
	 * @throws JSONException 
	 */
	public void findBudgetDeptList() throws JSONException
	{
		String deptName = request.getParameter("fuzzytext");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			// 查询
			object = remote.findAll(employee.getEnterpriseCode(), deptName, start,limit);
		} else {
			// 查询
			object = remote.findAll(employee.getEnterpriseCode(), deptName);
		}
		// 输出结果
		String strOutput = "";
		// 查询结果为null
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
			// 不为null
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	/**
	 * 预算部门记录列表 责任部门为Y add by liuyi 090805
	 * @throws JSONException 
	 */
	public void findDeptDutyList() throws JSONException
	{
		String deptName = request.getParameter("fuzzytext");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			// 查询
			object = remote.findAllDuty(employee.getEnterpriseCode(), deptName, start,limit);
		} else {
			// 查询
			object = remote.findAllDuty(employee.getEnterpriseCode(), deptName);
		}
		// 输出结果
		String strOutput = "";
		// 查询结果为null
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
			// 不为null
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	public CbmCCenter getDept() {
		return dept;
	}

	public void setDept(CbmCCenter dept) {
		this.dept = dept;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


}
