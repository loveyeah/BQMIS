package power.web.manage.plan.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.comm.TreeNode;
import power.ejb.manage.plan.BpCPlanItem;
import power.ejb.manage.plan.BpCPlanItemFacadeRemote;

import power.web.comm.AbstractAction;

public class PlanItemAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	BpCPlanItemFacadeRemote remote;
	BpCPlanItem planItemInfo;

	public BpCPlanItem getPlanItemInfo() {
		return planItemInfo;
	}

	public void setPlanItemInfo(BpCPlanItem planItemInfo) {
		this.planItemInfo = planItemInfo;
	}

	public PlanItemAction() {
		remote = (BpCPlanItemFacadeRemote) factory
				.getFacadeRemote("BpCPlanItemFacade");
	}

	public void getPlanItemModel() throws JSONException {
		String id = request.getParameter("id");
		BpCPlanItem o = remote.findById(id);
		write(JSONUtil.serialize(o));
	}

	public void getPlanItemTreeNode() {
		String pid = request.getParameter("pid");
		try {
			List<TreeNode> list = remote.findStatTreeList(pid, employee
					.getEnterpriseCode());
			write(JSONUtil.serialize(list));
		} catch (Exception exc) {
			exc.printStackTrace();
			write("[]");
		}
	}

	public void savePlanItemModel() {
		String method = request.getParameter("method");
		if (("add").equals(method)) {
			planItemInfo.setEnterpriseCode(employee.getEnterpriseCode());
			if (!remote.save(planItemInfo))
				write("{success:false,msg:'指 标 已 存 在 ！'}");
		} else if (("update").equals(method)) {
			String id = request.getParameter("id");
			BpCPlanItem model = new BpCPlanItem();
			model = remote.findById(id);
			model.setAccountOrder(planItemInfo.getAccountOrder());
			model.setCollectWay(planItemInfo.getCollectWay());
			model.setComputeMethod(planItemInfo.getComputeMethod());
			model.setFormulaType(planItemInfo.getFormulaType());
			model.setIfBudget(planItemInfo.getIfBudget());
			model.setIfTotal(planItemInfo.getIfTotal());
			model.setIsItem(planItemInfo.getIsItem());
			model.setItemCode(planItemInfo.getItemCode());
			model.setItemName(planItemInfo.getItemName());
			model.setItemType(planItemInfo.getItemType());
			model.setMemo(planItemInfo.getMemo());
			model.setRetrieveCode(planItemInfo.getRetrieveCode());
			model.setUnitCode(planItemInfo.getUnitCode());
			if (!remote.update(model, id))
				write("{success:false,msg:'指 标 已 存 在 ！'}");
		} else {
			write("{success:false,msg:'未 指 定 的 操 作 ！'}");
		}
	}

	public void updatePlanItemModel() {
		try {
		} catch (Exception e) {
			write("{success:false,msg:'修 改 失 败 ！'}");
		}
	}

	public void findTreeList() {

	}

	public void deletePlanItemModel() {
		String id = request.getParameter("id");
		boolean isleaf = remote.isleaf(id);
		if (isleaf) {
			BpCPlanItem it = remote.findById(id);
			remote.delete(it);
			write("{success:true,msg:'删除成功！'}");
		} else {
			write("{success:false,msg:'该节点有子节点，不能删除！'}");
		}
	}
	
	public void getAccountOrder() throws JSONException {
		String id = request.getParameter("itemCode");
		BpCPlanItem model=new BpCPlanItem();
		
		
		 List<Object[]> list=remote.getAllReferItem(id);
		 for(Object[]data:list){
			 remote.setAccountOrder(remote.findById(data[0].toString()));
		 }
		 model= remote.findById(id);
		 BpCPlanItem o = remote.setAccountOrder(model);
//		remote.update(o);
//		System.out.print(o.getAccountOrder());
		write(o.getAccountOrder().toString());
	}
}
