package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmJCapitalDetailFacadeRemote;
import power.ejb.manage.budget.CbmJCapitalFacadeRemote;
import power.ejb.manage.budget.CbmJFinanceFacadeRemote;
import power.ejb.manage.budget.form.CapitalDetailForm;
import power.ejb.manage.budget.form.FianceDetailForm;
import power.web.comm.AbstractAction;

public class BudgetCapitalDetailAction extends AbstractAction
{
	private CbmJCapitalDetailFacadeRemote remote;
	private CbmJCapitalFacadeRemote approveRemote;
	public BudgetCapitalDetailAction()
	{
		remote = (CbmJCapitalDetailFacadeRemote)factory.getFacadeRemote("CbmJCapitalDetailFacade");
		approveRemote = (CbmJCapitalFacadeRemote)factory.getFacadeRemote("CbmJCapitalFacade");
	}
	
	public void getCapitalDetailList() throws JSONException
	{
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String budgetTime = request.getParameter("budgetTime");
		if(start == null || limit == null)
		{
			PageObject pg = remote.findCapitalDetailList(budgetTime, employee.getEnterpriseCode());
			write(JSONUtil.serialize(pg));
		}
		else
		{
			PageObject pg = remote.findCapitalDetailList(budgetTime, employee.getEnterpriseCode(),
					Integer.parseInt(start),Integer.parseInt(limit));
			write(JSONUtil.serialize(pg));
		}
	}
	
	/**
	 * 批量保存修改资本性支出明细记录
	 * @throws JSONException 
	 */
	public void saveCapitalDetailMod() throws JSONException
	{
		String addString = request.getParameter("isAdd");
		String updateString = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		Object addObject = JSONUtil.deserialize(addString);
		Object updateObject = JSONUtil.deserialize(updateString);
		List<Map> alist  = (List<Map>)addObject;
		List<Map> ulist = (List<Map>)updateObject;
		List<CapitalDetailForm> addList = new ArrayList<CapitalDetailForm>();
		List<CapitalDetailForm> updateList = new ArrayList<CapitalDetailForm>();
		
		for(Map data : alist)
		{
			CapitalDetailForm temp = this.parseCapitalDetail(data);			
			addList.add(temp);
		}
		for(Map data : ulist)
		{
			CapitalDetailForm temp = this.parseCapitalDetail(data);
			updateList.add(temp);
		}
		
		if(addList != null || updateList != null || ids != null)
		{
			if(addList.size() >0 || updateList.size() > 0 || ids.length() > 0)
			{
				remote.saveCapitalDetails(addList, updateList, ids);
				write("{success: true,msg:'数据保存修改成功！'}");
			}
		}		
	}
	
	/**
	 * 将一映射转为FianceDetailForm对象
	 * @param data
	 * @return
	 */
	public CapitalDetailForm parseCapitalDetail(Map data)
	{
		CapitalDetailForm temp = new CapitalDetailForm();
		Long capitalId = null;
		String budgetTime = null;
		Long workFlowNo = null;
		String workFlowStatus = null;
		
		Long capitalDetailId = null;
		String project = null;
		Double materialCost = 0.0;
		Double workingCost = 0.0;
		Double otherCost = 0.0;
		Double deviceCost = 0.0;
		Double totalCost = 0.0;
		String memo = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		
		if(data.get("capitalId") != null)
			capitalId = Long.parseLong(data.get("capitalId").toString());
		if(data.get("budgetTime") != null)
			budgetTime = data.get("budgetTime").toString();
		if(data.get("workFlowNo") != null)
			workFlowNo = Long.parseLong(data.get("workFlowNo").toString());
		if(data.get("workFlowStatus") != null)
			workFlowStatus = data.get("workFlowStatus").toString();
		if(data.get("capitalDetailId") != null)
			capitalDetailId = Long.parseLong(data.get("capitalDetailId").toString());
		if(data.get("project") != null)
			project = data.get("project").toString();
		if(data.get("materialCost") != null)
			materialCost = Double.parseDouble(data.get("materialCost").toString());
		if(data.get("workingCost") != null)
			workingCost = Double.parseDouble(data.get("workingCost").toString());		
		if(data.get("otherCost") != null)
			otherCost = Double.parseDouble(data.get("otherCost").toString());
		if(data.get("deviceCost") != null)
			deviceCost = Double.parseDouble(data.get("deviceCost").toString());
		if(data.get("totalCost") != null)
			totalCost = Double.parseDouble(data.get("totalCost").toString());
		if(data.get("memo") != null)
			memo = data.get("memo").toString();
		
		temp.setCapitalId(capitalId);
		temp.setBudgetTime(budgetTime);
		temp.setWorkFlowNo(workFlowNo);
		temp.setWorkFlowStatus(workFlowStatus);
		temp.setCapitalDetailId(capitalDetailId);
		temp.setProject(project);
		temp.setMaterialCost(materialCost);
		temp.setWorkingCost(workingCost);
		temp.setOtherCost(otherCost);
		temp.setDeviceCost(deviceCost);
		temp.setTotalCost(temp.getMaterialCost() + temp.getWorkingCost() +
				temp.getOtherCost() + temp.getDeviceCost());
		temp.setMemo(memo);
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		
		return temp;
	}
	
//--------- 上报及审批-------
	
	public void capitalReportTo()
	{
		String actionId=request.getParameter("actionId");
		String  approveText=request.getParameter("approveText");
		String workflowType=request.getParameter("flowCode");
		String nextRoles=request.getParameter("nextRoles");
		String eventIdentify=request.getParameter("eventIdentify");
		String capitalId=request.getParameter("capitalId");
		String workerCode=request.getParameter("workerCode");
		approveRemote.reportTo(Long.parseLong(capitalId), workflowType, workerCode, Long.parseLong(actionId), approveText, nextRoles, eventIdentify);
		write("{success:true,msg:'上报成功！'}");
	}
	
	public void capitalCommSign()
	{
		String actionId=request.getParameter("actionId");
		String  approveText=request.getParameter("approveText");
		String nextRoles=request.getParameter("nextRoles");
		String eventIdentify=request.getParameter("eventIdentify");
		String capitalId=request.getParameter("capitalId");
		String responseDate=request.getParameter("responseDate");
		String workerCode=request.getParameter("workerCode");
		approveRemote.capitalCommSign(Long.parseLong(capitalId), approveText, workerCode, Long.parseLong(actionId), responseDate, nextRoles, eventIdentify);
		write("{success:true,msg:'审批成功！'}");
	}
	
	public void capitalApproveQuery() throws JSONException
	{
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String budgetTime = request.getParameter("budgetTime");
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(new String[] {
				"budgetDetailApprove"}, employee.getWorkerCode());
		PageObject obj=new PageObject(); 
		
		if(objstart!=null&&objlimit!=null)
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=approveRemote.capitalApproveQuery(budgetTime, employee.getEnterpriseCode(), entryIds, start,limit);
		}
		else
		{
			obj=approveRemote.capitalApproveQuery(budgetTime, employee.getEnterpriseCode(), entryIds);
		}
		String str=JSONUtil.serialize(obj);
		if(str.equals("null"))
		{
			str = "{\"list\":[],\"totalCount\":null}";
		}
		
		write(str);
	}
}