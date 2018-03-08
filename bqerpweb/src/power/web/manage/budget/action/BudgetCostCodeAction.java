package power.web.manage.budget.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmCCenter;
import power.ejb.manage.budget.CbmCCenterFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BudgetCostCodeAction extends AbstractAction {

	private CbmCCenterFacadeRemote remote;
	private CbmCCenter dept;
	private Long id;

	public BudgetCostCodeAction() {
		remote = (CbmCCenterFacadeRemote) factory
				.getFacadeRemote("CbmCCenterFacade");
	}

	/**
	 * 预算部门记录列表
	 * 
	 * @throws JSONException
	 */
	public void findBudgetCostCodeList() throws JSONException {
		String deptName = request.getParameter("fuzzytext");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			// 查询
			object = remote.findAll(employee.getEnterpriseCode(), deptName,
					start, limit);
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
