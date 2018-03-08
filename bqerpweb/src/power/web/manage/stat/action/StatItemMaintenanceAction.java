package power.web.manage.stat.action;

import java.util.List;

import power.ejb.comm.TreeNode;
import power.ejb.manage.stat.BpCStatItem;
import power.ejb.manage.stat.BpCStatItemFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class StatItemMaintenanceAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	BpCStatItemFacadeRemote remote;

	public StatItemMaintenanceAction() {
		remote = (BpCStatItemFacadeRemote) factory
				.getFacadeRemote("BpCStatItemFacade");
	}

	public void findTreeList() {
		String pid = request.getParameter("pid");
		String searchKey = request.getParameter("searchKey");
		try {
			List<TreeNode> list = remote.findStatTreeList(pid, employee
					.getEnterpriseCode(), searchKey);
			write(JSONUtil.serialize(list));
		} catch (Exception exc) {
			exc.printStackTrace();
			write("[]");
		}
	}

	public void findStatItemInfo() throws JSONException {
		String id = request.getParameter("id");
		Object o = remote.findById(id);
		write(JSONUtil.serialize(o));
	}

	/**
	 * 删除指标
	 */
	public void deleteStatItem() {
		String id = request.getParameter("id");
		boolean isleaf = remote.isleaf(id);
		if (isleaf) {
			BpCStatItem it = remote.findById(id);
			remote.delete(it);
			write("{success:true,msg:'删除成功！'}");
		} else {
			write("{success:false,msg:'该节点有子节点，不能删除！'}");
		}

	}

	/**
	 * 获取检索码
	 */
	public String getRetrieveCode() {
		String name = request.getParameter("name");
		String str = "";
		str = remote.getItemcode(name).toUpperCase();
		return str;
	}

	/**
	 * 获取指标详细
	 * 
	 * @throws JSONException
	 */
	public void getStatItemInfo() throws JSONException {
		String id = request.getParameter("id");
		BpCStatItem o = remote.findById(id);
		write(JSONUtil.serialize(o));
	}

	/**
	 * 获得计算等级
	 * 
	 * @throws JSONException
	 */
	public void getAccountOrder() throws JSONException {
		String id = request.getParameter("itemCode");
		BpCStatItem model = new BpCStatItem();

		List<Object[]> list = remote.getAllReferItem(id);
		for (Object[] data : list) {
			BpCStatItem o = remote.setAccountOrder(remote.findById(data[0]
					.toString()));
			remote.update(o);
		}
		model = remote.findById(id);
		BpCStatItem o = remote.setAccountOrder(model);
		remote.update(o);
		// System.out.print(o.getAccountOrder());
		write(o.getAccountOrder().toString());
	}

	/**
	 * 增加修改指标信息
	 * 
	 * @throws JSONException
	 */
	public void updateStatItem() throws JSONException {
		String method = request.getParameter("method");
		String isItem = request.getParameter("isItem");
		BpCStatItem statItem = null;
		// 非指标
		if (isItem.equals("N")) {
			String itemCode = request.getParameter("itemCode");
			String itemName = request.getParameter("itemName");
			String displayNo = request.getParameter("displayNo");
			if (method.equals("add")) {
				statItem = new BpCStatItem();
				statItem.setItemName(itemName);
				if (displayNo != null && !displayNo.trim().equals(""))
					statItem.setOrderBy(Long.parseLong(displayNo));
				statItem.setParentItemCode(itemCode);
				statItem.setEnterpriseCode(employee.getEnterpriseCode());
				statItem.setIsItem(isItem);
				statItem = remote.save(statItem);
			} else {
				statItem = remote.findById(itemCode);
				statItem.setItemName(itemName);
				if (displayNo != null && !displayNo.trim().equals(""))
					statItem.setOrderBy(Long.parseLong(displayNo));
				// 不是指标 默认false
				// remote.update(model, false);
				statItem = remote.update(statItem);
			}
		}
		// 指标
		else {
			String itemCode = request.getParameter("statItem.itemCode");
			String itemName = request.getParameter("statItem.itemName");
			String dataTimeType = request.getParameter("statItem.dataTimeType");
			String retrieveCode = request.getParameter("statItem.retrieveCode");
			String orderBy = request.getParameter("statItem.orderBy");
			String unitCode = request.getParameter("statItem.unitCode");
			String itemType = request.getParameter("statItem.itemType");
			String itemStatType = request.getParameter("statItem.itemStatType");
			String groupNature = request.getParameter("statItem.groupNature");
//			String accountOrder = request.getParameter("statItem.accountOrder");
//			Boolean isChange = false;
			if (method.equals("add")) {
				statItem = remote.findById(itemCode);
				if (statItem != null) {
					write("{success:false,msg:'该指标已经存在!'}");
					return;
				}
				statItem = new BpCStatItem();
				statItem.setParentItemCode(request
						.getParameter("statItem.parentItemCode"));
				statItem.setIsItem(isItem);
				statItem.setEnterpriseCode(employee.getEnterpriseCode());
			} else {
				statItem = remote.findById(itemCode);
				// if (statItem.getDataTimeType() == null ||
				// statItem.getDataTimeType().equals(dataTimeType)) {
				// isChange = false;
				// } else {
				// isChange = true;
				// }
			}
			statItem.setItemCode(itemCode);
			statItem.setItemName(itemName);
			statItem.setRetrieveCode(retrieveCode);
			if (orderBy != null && !orderBy.trim().equals(""))
				statItem.setOrderBy(Long.parseLong(orderBy));
			else
				statItem.setOrderBy(null);
			if (unitCode != null && !unitCode.trim().equals(""))
				statItem.setUnitCode(Long.parseLong(unitCode));
			else
				statItem.setUnitCode(null);
//			if (accountOrder != null && !accountOrder.trim().equals(""))
//				statItem.setAccountOrder(Long.parseLong(accountOrder));
//			else
//				statItem.setAccountOrder(null);
			statItem.setItemType(itemType);
			statItem.setDataTimeType(dataTimeType);
			statItem.setDeriveDataType(request
					.getParameter("statItem.deriveDataType"));
			statItem.setComputeMethod(request
					.getParameter("statItem.computeMethod"));
			statItem.setDataCollectWay(request
					.getParameter("statItem.dataCollectWay"));
			statItem.setDataAttribute(request
					.getParameter("statItem.dataAttribute"));
			statItem.setTotalDataType(request
					.getParameter("statItem.totalDataType"));
			statItem.setIgnoreZero(request.getParameter("statItem.ignoreZero"));
			statItem.setItemStatType(itemStatType);
			
			statItem.setGroupNature(groupNature);
			if (method.equals("add")) {
				statItem = remote.save(statItem);
			} else {
				// remote.update(statItem, itemCode, isChange);
				statItem = remote.update(statItem);
			}
		}
		write("{success:true,msg:'操作成功!',accountOrder:'"
				+ statItem.getAccountOrder() + "'}");
	}
}
