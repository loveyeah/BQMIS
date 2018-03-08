package power.web.manage.budget.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmCCenterItemFacadeRemote;
import power.ejb.manage.budget.CbmJBudgetItemFacadeRemote;
import power.ejb.manage.budget.form.CbmJBudgetItemForm;
import power.web.comm.AbstractAction;

public class ComplexBudgetQueryAction extends AbstractAction
{
	private CbmJBudgetItemFacadeRemote remote;
	private CbmCCenterItemFacadeRemote centerItemRemote;
	public ComplexBudgetQueryAction()
	{
		remote = (CbmJBudgetItemFacadeRemote)factory.getFacadeRemote("CbmJBudgetItemFacade");
		centerItemRemote = (CbmCCenterItemFacadeRemote)factory.getFacadeRemote("CbmCCenterItemFacade");
	}
	
	public void getComplexBudgetList() throws JSONException
	{
		String centerId = request.getParameter("centerId");
		String topicId = request.getParameter("topicId");
		String budgetTime = request.getParameter("budgetTime");
		
		List<CbmJBudgetItemForm> itemList = centerItemRemote.findItemList(centerId, topicId, 
				employee.getEnterpriseCode());
		if(itemList != null && itemList.size() > 0)
		{
			PageObject pg = remote.getComplexBudgetList(itemList,budgetTime,employee.getEnterpriseCode());
			write(JSONUtil.serialize(pg));
			
		}
		
	}
}