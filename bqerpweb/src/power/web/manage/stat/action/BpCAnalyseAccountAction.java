package power.web.manage.stat.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpCAnalyseAccount;
import power.ejb.manage.stat.BpCAnalyseAccountFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class BpCAnalyseAccountAction extends AbstractAction {

	private BpCAnalyseAccountFacadeRemote remote;
	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */

	public BpCAnalyseAccountAction() {
		remote = (BpCAnalyseAccountFacadeRemote) factory
				.getFacadeRemote("BpCAnalyseAccountFacade");
	}

	@SuppressWarnings("unchecked")
	public void saveBpCAnalyseAccount() throws Exception {
		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<BpCAnalyseAccount> addList = new ArrayList<BpCAnalyseAccount>();
			List<BpCAnalyseAccount> updateList = new ArrayList<BpCAnalyseAccount>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {

				String accountCode = null;

				String accountName = null;
				String accountType = null;

				if (data.get("accountCode") != null) {
					accountCode = data.get("accountCode").toString();
				}

				if (data.get("accountName") != null) {
					accountName = data.get("accountName").toString();
				}
				if (data.get("accountType") != null) {
					accountType = data.get("accountType").toString();
				}

				BpCAnalyseAccount model = new BpCAnalyseAccount();

				// 增加
				if (accountCode == null || accountCode.equals("")) {

					model.setAccountName(accountName);
					model.setAccountType(accountType);

					model.setEnterpriseCode(employee.getEnterpriseCode());

					addList.add(model);

				} else {
					model.setAccountCode(Long.parseLong(accountCode));
					model.setAccountName(accountName);
					model.setAccountType(accountType);
					updateList.add(model);

				}

			}

			if (addList.size() > 0)
				remote.save(addList);

			if (updateList.size() > 0)

				remote.update(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))

				remote.delete(deleteIds);

			write("{success: true,msg:'保存成功！'}");

		} catch (Exception exc) {
			write("{success: false,msg:'操作失败'}");
			throw exc;

		}
	}

	/**
	 * 获取记录列表
	 * 
	 * @throws JSONException
	 * 
	 */

	public void getBpCAnalyseAccountList() throws JSONException {
		String type = request.getParameter("type");
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), type,employee.getWorkerCode(), start,
				limit);

		write(JSONUtil.serialize(obj));
	}

	// ******************************************get/set变量方法******************************************

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

}
