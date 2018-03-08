package power.web.manage.budget.action;

import java.util.List;

import power.ear.comm.CodeRepeatException;
import power.ejb.comm.TreeNode;
import power.ejb.manage.budget.CbmCReportItem;
import power.ejb.manage.budget.CbmCReportItemFacadeRemote;
import power.ejb.manage.budget.form.CbmCReportItemAdd;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class BudgetReportItemAction extends AbstractAction {
	private CbmCReportItem reportItem;
	private CbmCReportItemFacadeRemote remote;

	public BudgetReportItemAction() {
		remote = (CbmCReportItemFacadeRemote) factory
				.getFacadeRemote("CbmCReportItemFacade");
	}

	public void findReportTreeList() throws JSONException {
		String pid = request.getParameter("pid");
		if (pid.equals("00")) {
			String str = "[{'id' :'1111111111','text':'预算参数设置表','leaf' :false,'code' :'1','description' : 'N'},"
					+ "{'id' :'2222222222','text':'收入利润预算表','leaf' :false,'code' :'2','description' : 'N'},"
					+ "{'id' :'3333333333','text':'成本费用预算表','leaf' :false,'code' :'3','description' : 'N'},"
					+ "{'id' :'4444444444','text':'燃料费用预算表','leaf' :false,'code' :'4','description' : 'N'},"
					+ "{'id' :'5555555555','text':'材料费预算明细表','leaf' :false,'code' :'5','description' : 'N'},"
					+ "{'id' :'6666666666','text':'修理费支出预算明细表','leaf' :false,'code' :'6','description' : 'N'},"
					+ "{'id' :'7777777777','text':'工资及福利费预算明细表','leaf' :false,'code' :'7','description' : 'N'},"
					+ "{'id' :'8888888888','text':'其他费用预算明细表','leaf' :false,'code' :'8','description' : 'N'},"
					+ "{'id' :'9999999999','text':'财务费用预算明细表','leaf' :false,'code' :'9','description' : 'N'}]";
			write(str);
		} else {
			List<TreeNode> list = remote.findReportItemTreeList(pid, employee
					.getEnterpriseCode());
			if (list != null)
				write(JSONUtil.serialize(list));
			else
				write("[]");
		}
	}

	/**
	 * 增加修改指标信息
	 * 
	 * @throws JSONException
	 */
	public void saveOrupdateReportItem() throws JSONException {
		String method = request.getParameter("method");
		String isItem = request.getParameter("isItem");
		String DaddyItemId = request.getParameter("DaddyItemId");
		CbmCReportItem rItem = null;

		// 非指标
		if (isItem.equals("N")) {
			if (method.equals("add")) {
				rItem = new CbmCReportItem();
				rItem.setParentId(Long.parseLong(DaddyItemId));
				rItem.setDisplayNo(reportItem.getDisplayNo());
				rItem.setItemName(reportItem.getItemName());
				rItem.setReportType(reportItem.getReportType());
				rItem.setIsItem(isItem);
				rItem.setEnterpriseCode(employee.getEnterpriseCode());
				try {
					remote.save(rItem);
				} catch (CodeRepeatException e) {
					
				}
				write(Constants.ADD_SUCCESS);
			} else {
				CbmCReportItem entity = remote.findById(reportItem.getReportItemId());
				entity.setDisplayNo(reportItem.getDisplayNo());
				entity.setItemName(reportItem.getItemName());
				remote.update(entity);
				write(Constants.MODIFY_SUCCESS);
			}
		}
		// 指标
		else {
			if (method.equals("add")) {
				String itemName = request.getParameter("itemName");
				rItem = new CbmCReportItem();
				rItem.setEnterpriseCode(employee.getEnterpriseCode());
				rItem.setParentId(Long.parseLong(DaddyItemId));
				rItem.setItemName(itemName);
				rItem.setItemId(reportItem.getItemId());
				rItem.setDataType(reportItem.getDataType());
				rItem.setDisplayNo(reportItem.getDisplayNo());
				rItem.setReportType(reportItem.getReportType());
				rItem.setUnitId(reportItem.getUnitId());
				rItem.setIsItem(isItem);
				try {
					remote.save(rItem);
					write(Constants.ADD_SUCCESS);
				} catch (CodeRepeatException e) {
					write("{success:true,msg:'同一报表节点下，不能重复保存！'}");
				}
			} else {
				CbmCReportItem entity = remote.findById(reportItem.getReportItemId());
				String itemName = request.getParameter("itemName");
				entity.setItemId(reportItem.getItemId());
				entity.setDataType(reportItem.getDataType());
				entity.setUnitId(reportItem.getUnitId());
				entity.setDisplayNo(reportItem.getDisplayNo());
				entity.setItemName(itemName);
				remote.update(entity);
				write(Constants.MODIFY_SUCCESS);
			}
		}
//		write("{success:true,msg:'操作成功!'}");
	}

	// 删除
	public void delReportItem() {
		String modelId = request.getParameter("id");
		CbmCReportItem entity = remote.findById(Long.parseLong(modelId));
		entity.setIsUse("N");
		remote.update(entity);
	}

	// 指标信息
	public void getReportItemInfo() throws NumberFormatException, JSONException {
		String modelId = request.getParameter("id");
		CbmCReportItemAdd bean = remote.findReportItemInfo(modelId);
		write(JSONUtil.serialize(bean));
	}

	//所有指标
	public void getAllReportItemList() throws JSONException{
		String argFuzzy = request.getParameter("argFuzzy");
		write(JSONUtil.serialize(remote.findAll(argFuzzy)));
	}

	public CbmCReportItem getReportItem() {
		return reportItem;
	}

	public void setReportItem(CbmCReportItem reportItem) {
		this.reportItem = reportItem;
	}
	
}
