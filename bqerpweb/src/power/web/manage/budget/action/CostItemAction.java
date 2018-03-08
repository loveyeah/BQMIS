package power.web.manage.budget.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ejb.manage.budget.CbmCCostItem;
import power.ejb.manage.budget.CbmCCostItemFacadeRemote;
import power.web.comm.AbstractAction;
import power.ejb.comm.TreeNode;

@SuppressWarnings("serial")
public class CostItemAction extends AbstractAction {
	CbmCCostItemFacadeRemote remote;
	private CbmCCostItem cost;

	public CostItemAction() {
		remote = (CbmCCostItemFacadeRemote) factory
				.getFacadeRemote("CbmCCostItemFacade");
	}

	public void costItemSaveorUpdate() {
		String method = request.getParameter("method");
		
		if (method.equals("add"))
			try {
				{
					cost.setEnterpriseCode(employee.getEnterpriseCode());
					remote.save(cost);
					write("{success : true,msg : '数据保存成功！'}");
				}
			} catch (CodeRepeatException e) {
				write("{success : true,msg : '该类型的指标已存在！'}");
			}
		else {
			CbmCCostItem entity = remote.findById(cost.getItemId());
			cost.setIsUse(entity.getIsUse());
			cost.setEnterpriseCode(entity.getEnterpriseCode());
			cost.setPItemId(entity.getPItemId());
			try {
				remote.update(cost);
				write("{success : true,msg : '数据修改成功！'}");
			} catch (CodeRepeatException e) {
				write("{success : true,msg : '该类型的指标已存在！'}");
			}
		}
	}

	public void costItemDelete() {
		CbmCCostItem entity = remote.findById(cost.getItemId());
		remote.delete(entity);
	}

	public void getCostItemTree() throws JSONException {
		String node = request.getParameter("pid");
		List<TreeNode> list = remote.findCostTreeList(node);
		if (list != null) {
			write(JSONUtil.serialize(list));
		} else {
			write("[]");
		}
	}

	public void getCostItemInfo() throws JSONException {
		String id = request.getParameter("id");
		List<CbmCCostItem> list= remote.findAll(id);
		if(list.size()>0){
			write(JSONUtil.serialize(list));
		}else{
			write("[{}]");
		}
 	}
	/**
	 *计算等级
	 */
	public void geAccountCostOrder() throws JSONException {
		Long id = null;
		if (request.getParameter("itemId") != null) {
			id = Long.parseLong(request.getParameter("itemId"));
		}
		CbmCCostItem model = remote.findById(id);
		// 计算公式等级
		Long accountOrder = remote.getaccountOrder(id);
		model.setAccountOrder(accountOrder);
		try {
			remote.update(model);
		} catch (CodeRepeatException e) {
//			write("{success : true,msg : '该类型的指标已存在！'}");
			e.printStackTrace();
		}
		write("{success : true,account:"+accountOrder+"}");

	}
	public CbmCCostItem getCost() {
		return cost;
	}

	public void setCost(CbmCCostItem cost) {
		this.cost = cost;
	}

}
