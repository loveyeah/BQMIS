package power.web.basedata.action;

import java.util.List;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.comm.TreeNode;
import power.web.comm.AbstractAction;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * @author Administrator
 * 
 */
@SuppressWarnings("serial")
public class BaseDataAction extends AbstractAction {

	BaseDataManager bll = (BaseDataManager) Ejb3Factory.getInstance()
			.getFacadeRemote("BaseDataManagerImpl");

	/**
	 * 
	 */
	public void checkWorkerIsAdmin() {
		if (bll.checkWorkerIsAdmin(employee.getWorkerCode(), employee
				.getEnterpriseCode())) {
			write("true");
		} else {
			write("false");
		}

	}

	public void getAdminWorkers() {

	}

	/**
	 * 取得当前Session中的人员信息
	 */
	public void getCurrentSessionEmployee() {
		try {
			write(JSONUtil.serialize(employee));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// add by fyyang 090212
	/** 用户验证 */
	public void workticketApproveCheckUser() {
		// 工号
		String workerCode = request.getParameter("workerCode");
		// 密码
		String loginPwd = request.getParameter("loginPwd");
		boolean blnOK = bll.checkUserRight(employee.getEnterpriseCode(),
				workerCode, loginPwd);
		write(String.valueOf(blnOK));
	}

	/**
	 * 取得子部门
	 */
	public void getDeptsByPid() {
		try {
			String code = request.getParameter("node");
			List<TreeNode> list = bll.getDeptsByPid(code);

			if (list != null) {
				super.write(JSONUtil.serialize(list));
//				System.out.print(JSONUtil.serialize(list));
			} else {
				super.write("[]");
			}
		} catch (Exception exc) {
			// exc.printStackTrace();
			super.write("[]");
		}
	}

	/**
	 * 部门选人
	 */
	public void getWorkerByDept() {
		// String deptId = request.getParameter("deptId");
		String depCode = request.getParameter("deptId");
		 String notInWorkerCodes = request.getParameter("notInWorkerCodes");
//	 String notInWorkerCodes ="0100";
		String queryKey = request.getParameter("queryKey");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject result = null;
		if (start != null && limit != null) {
			result = bll.getWorkersByDeptId(depCode.equals("")?"00":depCode, queryKey, notInWorkerCodes,Integer
					.parseInt(start), Integer.parseInt(limit));
		} else {
			result = bll.getWorkersByDeptId(depCode, queryKey,notInWorkerCodes);
		}
		try {
			if (result == null) {
				super.write("{list:[],totalCount:0}");
			} else {
				super.write(JSONUtil.serialize(result));
//				System.out.print(JSONUtil.serialize(result));

			}
		} catch (JSONException e) {
			e.printStackTrace();
			super.write("{list:[],totalCount:0}");
		}
	}

	public void checkIsDianJianYuan() {
		String workerCode = employee.getWorkerCode();
		boolean b = bll.checkIsDianJianYuan(workerCode);
		super.write((b ? "Y" : "N"));
	}

	/**
	 * 取子专业
	 */
	public void getSpecialsByPCode() {
		try {
			String enterpriseCode = request.getParameter("enterpriseCode");
			// String specialCode=request.getParameter("specialCode");
			String specialCode = request.getParameter("node");
			String specialType = request.getParameter("specialType");
			if (specialCode != null && !"".equals(specialCode)) {
				List<TreeNode> list = bll.getSpecialsByPCode(enterpriseCode,
						specialType, specialCode);
				if (list != null) {
					super.write(JSONUtil.serialize(list));
				} else {
					super.write("[]");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			super.write("[]");
		}
	}

	/**
	 * 更新参数值
	 */
	public void updateParamsValue() {
		try {
			String parameterNo = request.getParameter("parmNo");
			String parameterValue = request.getParameter("parmValue");
			bll.updateParamValue(parameterNo, parameterValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getOriCurrency() throws Exception {
		super.write("{\"data\":\"" + bll.getOriCurrency() + "\"}");
	}

	public void getIssueType() {
		super.write("{\"data\":\"" + bll.getIssueType() + "\"}");
	}

}
