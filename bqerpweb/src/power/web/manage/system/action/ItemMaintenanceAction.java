package power.web.manage.system.action;

import java.util.List;

import power.ear.comm.CodeRepeatException;
import power.ejb.comm.TreeNode;
import power.ejb.manage.system.BpCItem;
import power.ejb.manage.system.BpCItemFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class ItemMaintenanceAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private String pid;
	private String id;
	private String name;
	private String method;
	BpCItemFacadeRemote remote;
	private BpCItem bpItem;

	/**
	 * 构造函数
	 */
	public ItemMaintenanceAction() {
		remote = (BpCItemFacadeRemote) factory.getFacadeRemote("BpCItemFacade");
	}

	/**
	 * 查询指标数据
	 */
	public void findItemList() {
		try {
			 
			String searchKey =  request.getParameter("searchKey"); 
			List<TreeNode> res = remote.findItemTreeList(pid, employee
					.getEnterpriseCode(),searchKey);
			if (res != null) {
				for (TreeNode tn : res) {
					String icon = "";
					if (tn.getLeaf()) {
						icon = "box";
					} else {
						if (("Y").equals(tn.getDescription()))
							icon = tn.getLeaf() ? "my-iconCls" : "my-iconCls";
						else
							icon = tn.getLeaf() ? "file" : "folder";
					}
					tn.setIconCls(icon);
				}
				write(JSONUtil.serialize(res));
			} else {
				write("[]");
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			write("[]");
		} 
	} 
	// ADD bjxu
	public void checkforItem() {
		String itemCode = request.getParameter("itemCode");
		if (remote.checkItem(itemCode)) {
			write("true");
		} else {
			write("false");
		}
	}

	/**
	 * 根据id获取指标详细信息
	 * 
	 * @throws JSONException
	 */
	public void findItemInfo() throws JSONException {
		Object o = remote.findItemInfo(id);
		write(JSONUtil.serialize(o));
	}

	/**
	 * 删除指标
	 */
	public void deleteItem() {
		boolean isleaf = remote.isLeaf(id);
		if (isleaf) {
			BpCItem it = remote.findById(id);

			remote.delete(it);
			write("{success:true,msg:'删除成功！'}");
		} else {
			write("{success:false,msg:'该节点有子节点，不能删除！'}");
		}

	}

	/**
	 * 增加修改指标信息
	 * 
	 * @throws JSONException
	 * @throws CodeRepeatException 
	 */
	public void updateItem() throws JSONException {
		if (method.equals("update")) {
			BpCItem model = new BpCItem();
			model = remote.findById(id);
			model.setItemName(bpItem.getItemName());
			model.setOrderBy(bpItem.getOrderBy());
			if (bpItem.getUnitCode() != null) {
				model.setUnitCode(bpItem.getUnitCode());
			}
			model.setItemMemo(bpItem.getItemMemo());
			model.setIsItem(bpItem.getIsItem());
			model.setRetrieveCode(bpItem.getRetrieveCode());
			remote.delete(model);
			BpCItem base =remote.update(model);
			write("{success:true,msg:'" + base.getItemCode() + "'}");
		} else {
			BpCItem model = null;
			if (method.equals("add")) {
				String itemName = request.getParameter("bpItem.itemName");
				model = remote.findById(itemName);
				if (model != null) {
					write("{success:false,msg:'指标名称不能重复!'}");
					return;
				}
				model = new BpCItem();
				model.setItemName(bpItem.getItemName());
				if (bpItem.getUnitCode() != null) {
					model.setUnitCode(bpItem.getUnitCode());
				}
				model.setOrderBy(bpItem.getOrderBy());
				model.setItemMemo(bpItem.getItemMemo());
				model.setIsItem(bpItem.getIsItem());
				model.setRetrieveCode(bpItem.getRetrieveCode());
				model.setEnterpriseCode(employee.getEnterpriseCode());
				model.setParentItemCode(id);
				
				String id = remote.save(model);
				write("{success:true,msg:'" + id + "'}");
			

			}
		}
	}
	/**
	 * 生成检索码
	 * 
	 * @throws JSONException
	 */
	public void getRetrieveCode() throws JSONException {
		String retrieveCode = remote.findRetrieveCode(name);
		write(JSONUtil.serialize(retrieveCode));
	}

	public BpCItem getBpItem() {
		return bpItem;
	}

	public void setBpItem(BpCItem bpItem) {
		this.bpItem = bpItem;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
