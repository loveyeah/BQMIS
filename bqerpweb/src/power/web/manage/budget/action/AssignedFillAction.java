package power.web.manage.budget.action;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.manage.budget.CbmJAssignedFill;
import power.ejb.manage.budget.CbmJAssignedFillFacadeRemote;
import power.ejb.manage.project.PrjJEndCheck;
import power.web.comm.AbstractAction;

/**
 * 外委单管理
 * @author fyyang 20100902
 *
 */
@SuppressWarnings("serial")
public class AssignedFillAction extends AbstractAction{

	private CbmJAssignedFill temp;
	private CbmJAssignedFillFacadeRemote remote;
	
	public AssignedFillAction() {
		remote = (CbmJAssignedFillFacadeRemote) factory.getFacadeRemote("CbmJAssignedFillFacade");
	}
	
	/**
	 * 获得列表
	 * @throws JSONException 
	 * @throws ParseException 
	 */
	public void assignedFillList() throws JSONException, ParseException 
	{	
		PageObject obj=new PageObject();
		String assignName = request.getParameter("assignName")==null?"":request.getParameter("assignName");
		
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	   
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findAssignedFillList(employee.getWorkerCode(),employee.getEnterpriseCode(), assignName, start,limit);
	    }
	    else
	    {
	    	obj=remote.findAssignedFillList(employee.getWorkerCode(),employee.getEnterpriseCode(), assignName);
	    }
	    String str=JSONUtil.serialize(obj);

		write(str);
		
	}
	
	/**
	 * 获得所有状态的列表
	 * @throws JSONException 
	 * @throws ParseException 
	 */
	public void assignedFillListall() throws JSONException, ParseException 
	{	
		PageObject obj=new PageObject();
		String assignName = request.getParameter("assignName")==null?"":request.getParameter("assignName");
		String applyBy = request.getParameter("applyBy")==null?"":request.getParameter("applyBy");
		
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findAssignedFillListall(employee.getWorkerCode(),employee.getEnterpriseCode(), assignName, applyBy,start,limit);
	    }
	    else
	    {
	    	obj=remote.findAssignedFillListall(employee.getWorkerCode(),employee.getEnterpriseCode(), assignName,applyBy);
	    }
	    String str=JSONUtil.serialize(obj);

		write(str);
		
	}
	
	
	//增加
	public void saveAssignedFill() throws ParseException
	{
		System.out.println("进入保存action");
		String applyDept=request.getParameter("applyDept");
		temp.setWorkFlowStatus("0");
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		temp.setApplyBy(employee.getWorkerCode());
		temp.setApplyDept(applyDept);
		temp.setApplyDate(new Date());
		temp = remote.save(temp);
		if(temp == null)
		{
			write("{failure:true,msg:'此类型的外委单已存在！'}");
		}
		else
		write("{success:true,id:'"+temp.getAssignId()+"',msg:'增加成功！'}");
	}
	
	//修改
	public void updateAssignedFill() throws ParseException
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		CbmJAssignedFill model = remote.findById(temp.getAssignId());
		model.setWorkFlowStatus(model.getWorkFlowStatus());
		model.setAssignName(temp.getAssignName());
		model.setAssignId(temp.getAssignId());
		model.setEstimateMoney(temp.getEstimateMoney());
		model.setItemId(temp.getItemId());
		model.setApplyBy(temp.getApplyBy());
		model.setApplyDate(temp.getApplyDate());		
		model.setApplyDept(temp.getApplyDept());
		model.setAssignFunction(temp.getAssignFunction());
		model.setMemo(temp.getMemo());
		temp = remote.update(model);
		if(temp == null)
		{
			write("{failure:true,msg:'此类型的外委单已存在！'}");
		}
		else
		write("{success:true,msg:'修改成功！'}");
	}
	
	public void assignedFillApproveList() throws JSONException, ParseException
	{
		String assignName = request.getParameter("assignName")==null?"":request.getParameter("assignName");
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    PageObject obj=new  PageObject();
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(new String[] {
				"assignedFill"}, employee.getWorkerCode());
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findAssignedFillApproveList(employee.getDeptId()+"",employee.getEnterpriseCode(),assignName,entryIds,start,limit);
	    }
	    else
	    {
	    	obj=remote.findAssignedFillApproveList(employee.getDeptId()+"",employee.getEnterpriseCode(),assignName,entryIds);
	    }
	    String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	public void assignedFillDelete()
	{
		
		
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	   * 上报
	   */
	  public void reportTo()
	  {
		  String actionId=request.getParameter("actionId");
		  String assignId=request.getParameter("assignId");
		  String flowCode=request.getParameter("flowCode");
		  String workerCode=request.getParameter("workerCode");
		  remote.reportTo(assignId, flowCode, workerCode,Long.parseLong(actionId));
		  write("{success:true,msg:'&nbsp&nbsp&nbsp上报成功&nbsp&nbsp&nbsp'}");
	  }
	  
	/*
	 * 审批
	 */
	  public void approveSign() throws NumberFormatException, ParseException
		{
			String eventIdentify=request.getParameter("eventIdentify");
			String assignId=request.getParameter("assignId");
			String workflowNo=request.getParameter("workflowNo");
			String workerCode=request.getParameter("workerCode");
			String actionId=request.getParameter("actionId");
			String responseDate=request.getParameter("responseDate");
			String nextRoles=request.getParameter("nextRoles");
			String approveText=request.getParameter("approveText");
//			temp.setEnterpriseCode(employee.getEnterpriseCode());

			remote.approveSign(assignId, approveText, workflowNo, workerCode,Long.parseLong(actionId), responseDate, nextRoles,eventIdentify);
			  write("{success:true,msg:'审批成功！'}");
		}
	  
	// 指标树
		public void getItemIdTree() {
			String pid = request.getParameter("pid");
			String budgetTime =request.getParameter("budgetTime");
			String itemType = request.getParameter("itemType");
			try {
				List list = remote.getItemIdTree(pid, employee
						.getDeptId(), budgetTime,itemType);

				write(JSONUtil.serialize(list));
			} catch (Exception exc) {
				exc.printStackTrace();
				write("[]");
			}
		}
	  
	//取得当前剩余费用来源金额 add by mlian_20101014
		public void getCurrentItemFinanceLeft(){
			SimpleDateFormat format = new SimpleDateFormat("yyyy");
			String date = format.format(new Date());
			String time= request.getParameter("budgetTime");
			Long itemId = Long.parseLong(request.getParameter("itemId"));
			String budgetTime =time.equals("null")?date:time;
			try {
				Long financeLeft = remote.getCurrentItemFinanceLeft(itemId,budgetTime);
				write("{finance:'"+financeLeft+"'}");
			} catch (Exception exc) {
				exc.printStackTrace();
				write("[]");
			}
		}
		
		
	public CbmJAssignedFillFacadeRemote getRemote() {
		return remote;
	}

	public void setRemote(CbmJAssignedFillFacadeRemote remote) {
		this.remote = remote;
	}

	public CbmJAssignedFill getTemp() {
		return temp;
	}

	public void setTemp(CbmJAssignedFill temp) {
		this.temp = temp;
	} 
	
	
}
