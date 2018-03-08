package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmCBusinessItem;
import power.ejb.manage.budget.CbmCBusinessItemFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class BudgetBusinessItemAction extends AbstractAction {
	
	private CbmCBusinessItemFacadeRemote remote;
	
	public BudgetBusinessItemAction() {
		remote = (CbmCBusinessItemFacadeRemote) factory.getFacadeRemote("CbmCBusinessItemFacade");
	}
	
	/**
	 * 保存操作
	 */
	@SuppressWarnings("unchecked")
	public void saveOrUpdateBusinessItem() throws CodeRepeatException {
		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("ids");
			Object obj = JSONUtil.deserialize(str);

			List<CbmCBusinessItem> addList = new ArrayList<CbmCBusinessItem>();
			List<CbmCBusinessItem> updateList = new ArrayList<CbmCBusinessItem>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
				String businessItemId = null;
				String itemName = null;
				String unitId = null;
				String itemId1 = null;
				String itemId2=null;
				if (data.get("businessItemId") != null&& !data.get("businessItemId").equals("")) {
					businessItemId = data.get("businessItemId").toString();
				}
				if (data.get("itemName") != null&&!data.get("itemName").equals("")) {
					itemName = data.get("itemName").toString();
				}
				if (data.get("unitId") != null&&!data.get("unitId").equals("")) {
					unitId = data.get("unitId").toString();
				}
				if (data.get("itemId1") != null&&!data.get("itemId1").equals("")) {
					itemId1 = data.get("itemId1").toString();
				}
				if (data.get("itemId2") != null&&!data.get("itemId2").equals("")) {
					itemId2 = data.get("itemId2").toString();
				}
				
				CbmCBusinessItem model = new CbmCBusinessItem();
				// 增加
				if (businessItemId == null || "".equals(businessItemId)) 
				{
					model.setItemName(itemName);
					if(unitId != null && !"".equals(unitId)) {
						model.setUnitId(Long.parseLong(unitId));
					}
					if(itemId1 != null && !"".equals(itemId1)) {
						model.setItemId1(Long.parseLong(itemId1));
					}
					if(itemId2 != null && !"".equals(itemId2)) {
						model.setItemId2(Long.parseLong(itemId2));
					}
					model.setEnterpriseCode(employee.getEnterpriseCode());
					model.setIsUse("Y");
					addList.add(model);
				} 
				else {
					model.setBusinessItemId(Long.parseLong(businessItemId));
					model.setItemName(itemName);
					if(unitId != null && !"".equals(unitId)) {
						model.setUnitId(Long.parseLong(unitId));
					}
					if(itemId1 != null && !"".equals(itemId1)) {
						model.setItemId1(Long.parseLong(itemId1));
					}
					if(itemId2 != null && !"".equals(itemId2)) {
						model.setItemId2(Long.parseLong(itemId2));
					}
					model.setEnterpriseCode(employee.getEnterpriseCode());
					model.setIsUse("Y");
					updateList.add(model);
				}
			}

			if (addList.size() > 0) {
				try {
					remote.save(addList);
					write(Constants.ADD_SUCCESS);
				} catch (CodeRepeatException e) {
					write("{success:true,msg:'在预算部门指标维护，该指标被多个部门选取！或者指标名称重复！'}");
				}
				
			}
			if (updateList.size() > 0) {
				try {
					remote.update(updateList);
					write(Constants.MODIFY_SUCCESS);
				} catch (CodeRepeatException e) {
					write("{success:true,msg:'在预算部门指标维护，该指标被多个部门选取！或者指标名称重复！'}");
				}
				
			}
			//String [] ids=deleteIds.split(",");
			

			if (deleteIds != null && !deleteIds.trim().equals(""))

			
			//	remote.delete(deleteIds);
				
				remote.deleteMuti(deleteIds);

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}

	public void getBusinessItemList() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start == null || limit == null)
		{
			PageObject pg = remote.findAll(employee.getEnterpriseCode());
			write(JSONUtil.serialize(pg));
		}
		else
		{
			PageObject pg = remote.findAll(employee.getEnterpriseCode(),Integer.parseInt(start),Integer.parseInt(limit));
			write(JSONUtil.serialize(pg));
		}
	}
}
