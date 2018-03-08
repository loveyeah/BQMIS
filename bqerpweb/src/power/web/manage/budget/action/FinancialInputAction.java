package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.manage.budget.CbmJBudgetItem;
import power.ejb.manage.budget.CbmJBudgetItemFacadeRemote;
import power.ejb.manage.budget.form.CbmJBudgetItemForm;
import power.web.comm.AbstractAction;

public class FinancialInputAction extends AbstractAction
{
	private CbmJBudgetItemFacadeRemote remote;
	public FinancialInputAction()
	{
		remote = (CbmJBudgetItemFacadeRemote)factory.getFacadeRemote("CbmJBudgetItemFacade");
	}
	
	public void saveFinancialModified() throws JSONException
	{
		String modified = request.getParameter("modified");
		Object modObject = JSONUtil.deserialize(modified);
		List<Map> mlist = (List<Map>)modObject;
		List<CbmJBudgetItemForm> modList = new ArrayList<CbmJBudgetItemForm>();
		
		for(Map data : mlist)
		{
			CbmJBudgetItemForm form = new CbmJBudgetItemForm();
			Long budgetItemId = null;
			Double financeHappen = null;
			String dataSource = null;
			Long centerItemId = null;
			Long topicId = null;
			
			Long centerId = null;	
			String budgetTime =null;
			Long itemId = null;
			
			Long budgetMakeId = null;
			
			if(data.get("budgetMakeId") != null)
				budgetMakeId = Long.parseLong(data.get("budgetMakeId").toString());
			if(data.get("budgetItemId") != null)
				budgetItemId = Long.parseLong(data.get("budgetItemId").toString());
			if(data.get("financeHappen") != null)
				financeHappen = Double.parseDouble(data.get("financeHappen").toString());
			if(data.get("dataSource") != null)
				dataSource = data.get("dataSource").toString();
			if(data.get("centerItemId") != null)
				centerItemId = Long.parseLong(data.get("centerItemId").toString());
			if(data.get("topicId") != null)
				topicId = Long.parseLong(data.get("topicId").toString());
			if(data.get("centerId") != null)
				centerId = Long.parseLong(data.get("centerId").toString());
			if(data.get("budgetTime") != null)
				budgetTime = data.get("budgetTime").toString();
			if(data.get("itemId") != null)
				itemId = Long.parseLong(data.get("itemId").toString());
			
			form.setBudgetItemId(budgetItemId);
			form.setFinanceHappen(financeHappen);
			form.setDataSource(dataSource);
			form.setCenterItemId(centerItemId);
			form.setTopicId(topicId);
			form.setCenterId(centerId);
			form.setBudgetTime(budgetTime);
			form.setItemId(itemId);
			
			form.setBudgetMakeId(budgetMakeId);
			
			modList.add(form);
		}
		if(modList != null && modList.size() > 0)
		{
			remote.saveModified(modList);
			write("{success:true,msg:'数据保存修改成功！'}");
		}
		else {
			write("{failure:true,msg:'数据保存失败！'}");
		}
	}
}