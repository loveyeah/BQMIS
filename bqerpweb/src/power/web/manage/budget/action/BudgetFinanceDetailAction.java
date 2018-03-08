package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmJFinanceDetailFacadeRemote;
import power.ejb.manage.budget.CbmJFinanceFacadeRemote;
import power.ejb.manage.budget.form.FianceDetailForm;
import power.web.comm.AbstractAction;

public class BudgetFinanceDetailAction extends AbstractAction
{
	private CbmJFinanceDetailFacadeRemote remote;
	private CbmJFinanceFacadeRemote approveRemote;
	
	public BudgetFinanceDetailAction()
	{
		remote = (CbmJFinanceDetailFacadeRemote)factory.getFacadeRemote("CbmJFinanceDetailFacade");
		approveRemote=(CbmJFinanceFacadeRemote)factory.getFacadeRemote("CbmJFinanceFacade");
	}
	
	public void getFinanceDetailList() throws JSONException
	{
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String budgetTime = request.getParameter("budgetTime");
		String financeType = request.getParameter("financeType");
		if(start == null || limit == null)
		{
			PageObject pg = remote.findAllFinanceDetail(budgetTime, financeType, employee.getEnterpriseCode());
			write(JSONUtil.serialize(pg));
		}
		else
		{
			PageObject pg = remote.findAllFinanceDetail(budgetTime, financeType, employee.getEnterpriseCode(),
					Integer.parseInt(start),Integer.parseInt(limit));
			write(JSONUtil.serialize(pg));
		}
	}
	/**
	 * 修改数据
	 * @throws JSONException
	 */
	public void saveFinanceDetailModified() throws JSONException
	{
		String addString = request.getParameter("isAdd");
		String updateString = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		Object addObject = JSONUtil.deserialize(addString);
		Object updateObject = JSONUtil.deserialize(updateString);
		List<Map> alist  = (List<Map>)addObject;
		List<Map> ulist = (List<Map>)updateObject;
		List<FianceDetailForm> addList = new ArrayList<FianceDetailForm>();
		List<FianceDetailForm> updateList = new ArrayList<FianceDetailForm>();
		
		for(Map data : alist)
		{
			FianceDetailForm temp = this.parseFinanceDetail(data);			
			addList.add(temp);
		}
		for(Map data : ulist)
		{
			FianceDetailForm temp = this.parseFinanceDetail(data);
			updateList.add(temp);
		}
		
		if(addList != null || updateList != null || ids != null)
		{
			if(addList.size() >0 || updateList.size() > 0 || ids.length() > 0)
			{
				remote.saveFinanceDetails(addList, updateList, ids);
				write("{success: true,msg:'数据保存修改成功！'}");
			}
		}		
	}
	
	/**
	 * 将一映射转为FianceDetailForm对象
	 * @param data
	 * @return
	 */
	public FianceDetailForm parseFinanceDetail(Map data)
	{
		FianceDetailForm temp = new FianceDetailForm();
		Long financeId = null;
		String budgetTime = null;
		String financeType = null;
		Long workFlowNo = null;
		String workFlowStatus = null;
		
		Long financeDetailId = null;
		String loanName = null;
		Double lastLoan = 0.0;
		Double newLoan = 0.0;
		Double repayment = 0.0;
		Double balance = 0.0;
		Double interest = 0.0;
		String memo = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		
		if(data.get("financeId") != null)
			financeId = Long.parseLong(data.get("financeId").toString());
		if(data.get("budgetTime") != null)
			budgetTime = data.get("budgetTime").toString();
		if(data.get("financeType") != null)
			financeType = data.get("financeType").toString();
		if(data.get("workFlowNo") != null)
			workFlowNo = Long.parseLong(data.get("workFlowNo").toString());
		if(data.get("workFlowStatus") != null)
			workFlowStatus = data.get("workFlowStatus").toString();
		if(data.get("financeDetailId") != null)
			financeDetailId = Long.parseLong(data.get("financeDetailId").toString());
		if(data.get("loanName") != null)
			loanName = data.get("loanName").toString();
		if(data.get("lastLoan") != null)
			lastLoan = Double.parseDouble(data.get("lastLoan").toString());
		if(data.get("newLoan") != null)
			newLoan = Double.parseDouble(data.get("newLoan").toString());		
		if(data.get("repayment") != null)
			repayment = Double.parseDouble(data.get("repayment").toString());
		if(data.get("balance") != null)
			balance = Double.parseDouble(data.get("balance").toString());
		if(data.get("interest") != null)
			interest = Double.parseDouble(data.get("interest").toString());
		if(data.get("memo") != null)
			memo = data.get("memo").toString();
		
		temp.setFinanceId(financeId);
		temp.setBudgetTime(budgetTime);
		temp.setFinanceType(financeType);
		temp.setWorkFlowNo(workFlowNo);
		temp.setWorkFlowStatus(workFlowStatus);
		temp.setFinanceDetailId(financeDetailId);
		temp.setLoanName(loanName);
		temp.setLastLoan(lastLoan);
		temp.setNewLoan(newLoan);
		temp.setRepayment(repayment);
		temp.setBalance(temp.getLastLoan() + temp.getNewLoan() - temp.getRepayment());
		temp.setInterest(interest);
		temp.setMemo(memo);
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		
		return temp;
	}
	
	//---------add by fyyang 090824--- 上报及审批-------
	
	public void financeReportTo()
	{
		String actionId=request.getParameter("actionId");
		String  approveText=request.getParameter("approveText");
		String workflowType=request.getParameter("flowCode");
		String nextRoles=request.getParameter("nextRoles");
		String eventIdentify=request.getParameter("eventIdentify");
		String financeId=request.getParameter("financeId");
		String workerCode=request.getParameter("workerCode");
		approveRemote.reportTo(Long.parseLong(financeId), workflowType, workerCode, Long.parseLong(actionId), approveText, nextRoles, eventIdentify);
		write("{success:true,msg:'上报成功！'}");
	}
	
	public void financeCommSign()
	{
		String actionId=request.getParameter("actionId");
		String  approveText=request.getParameter("approveText");
		String nextRoles=request.getParameter("nextRoles");
		String eventIdentify=request.getParameter("eventIdentify");
		String financeId=request.getParameter("financeId");
		String responseDate=request.getParameter("responseDate");
		String workerCode=request.getParameter("workerCode");
		approveRemote.financeCommSign(Long.parseLong(financeId), approveText, workerCode, Long.parseLong(actionId), responseDate, nextRoles, eventIdentify);
		write("{success:true,msg:'审批成功！'}");
	}
	
	public void financeApproveQuery() throws JSONException
	{
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String budgetTime = request.getParameter("budgetTime");
		String financeType = request.getParameter("financeType");
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(new String[] {
				"budgetDetailApprove"}, employee.getWorkerCode());
		PageObject obj=new PageObject(); 
		
		if(objstart!=null&&objlimit!=null)
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=approveRemote.financeApproveQuery(budgetTime, financeType, employee.getEnterpriseCode(), entryIds, start,limit);
		}
		else
		{
			obj=approveRemote.financeApproveQuery(budgetTime,financeType, employee.getEnterpriseCode(), entryIds);
		}
		String str=JSONUtil.serialize(obj);
		if(str.equals("null"))
		{
			str = "{\"list\":[],\"totalCount\":null}";
		}
		
		write(str);
	}
	
}