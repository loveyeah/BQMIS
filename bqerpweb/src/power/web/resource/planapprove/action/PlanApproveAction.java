package power.web.resource.planapprove.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.resource.MrpJPlanRequirementDetail;
import power.ejb.resource.MrpJPlanRequirementDetailFacadeRemote;
import power.ejb.resource.MrpJPlanRequirementHead;
import power.ejb.resource.MrpJPlanRequirementHeadFacadeRemote;
import power.ejb.resource.business.MrpPlanReqApprove;
import power.ejb.resource.form.PlanApproveStatus;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

public class PlanApproveAction extends AbstractAction{

	private MrpPlanReqApprove approveRemote;
	private MrpJPlanRequirementHeadFacadeRemote planRemote;
	private MrpJPlanRequirementDetailFacadeRemote mrpDetailRemote;
	
	public PlanApproveAction()
	{
		approveRemote=(MrpPlanReqApprove)factory.getFacadeRemote("MrpPlanReqApproveImp");
		planRemote=(MrpJPlanRequirementHeadFacadeRemote)factory.getFacadeRemote("MrpJPlanRequirementHeadFacade");
	}
	
	public void findAllPlanBusiStatus() throws JSONException
	{
		List<PlanApproveStatus> list=approveRemote.findAllPlanBusi();
		PlanApproveStatus model=new PlanApproveStatus();
		model.setStatusCode("");
		model.setStatusName("所有");
		list.add(0,model);
		 PageObject object = new PageObject();
	        // 设置list
	        object.setList(list);
	        // 设置长度
	        object.setTotalCount(new Long(list.size()));
	        write(JSONUtil.serialize(object));
	}
	
	public void findPlanBusiStatusForApprove() throws JSONException
	{
		List<PlanApproveStatus> list=approveRemote.findPlanBusiForApprove();
		PlanApproveStatus model=new PlanApproveStatus();
		model.setStatusCode("");
		model.setStatusName("所有");
		list.add(0,model);
		 PageObject object = new PageObject();
	        // 设置list
	        object.setList(list);
	        // 设置长度
	        object.setTotalCount(new Long(list.size()));
	        write(JSONUtil.serialize(object));
	}
	public void planReport()
	{
		String dateMemo=request.getParameter("dateMemo");
		String mrNo=request.getParameter("mrNo");
		String workflowType=request.getParameter("workflowType");
		String workerCode=request.getParameter("workerCode");
		String actionId=request.getParameter("actionId");
		String approveText=request.getParameter("approveText");
		String nextRoles=request.getParameter("nextRoles");
		String eventIdentify=request.getParameter("eventIdentify");
		approveRemote.reportTo(mrNo, workflowType, workerCode, Long.parseLong(actionId), approveText, nextRoles, eventIdentify,dateMemo);
	    write("{success:true,msg:'上报成功！'}");
	}
	
	public void planReqCommSign()
	{
		String mrNo=request.getParameter("mrNo");
		String workerCode=request.getParameter("workerCode");
		String actionId=request.getParameter("actionId");
		String approveText=request.getParameter("approveText");
		String nextRoles=request.getParameter("nextRoles");
		String eventIdentify=request.getParameter("eventIdentify");
		String responseDate=request.getParameter("responseDate");
		String itemCode=request.getParameter("itemCode"); //add by fyyang 20100325 费用来源审批时可修改
		approveRemote.planReqSign(mrNo, approveText, workerCode, Long.parseLong(actionId), responseDate, nextRoles, eventIdentify,itemCode);
		 write("{success:true,msg:'审批成功！'}");
	}
	
	//modify by fyyang 090722 增加按部门过滤
	public void planApproveQuery() throws JSONException
	{
		//modify by fyyang 090618
		 /** 计划开始时间 */
        String dateFrom = request.getParameter("dateFrom");
        /** 计划结束时间 */
        String dateTo = request.getParameter("dateTo");
        //add by fyyang 090618------------------------------
        /** 按月份查询 */
        String monthPlan = request.getParameter("monthPlan");
        /** 按季度查询 */
        String quarterYear = request.getParameter("quarterYear");
        String quarter = request.getParameter("quarter");
        /** 按季度查询 */
        String planOriginalID = request.getParameter("mrOriginal");
        if("6".equals(planOriginalID)) {
        	if("1".equals(quarter)) {
        		Long year = Long.valueOf(quarterYear)-1;
        		dateFrom = year.toString() + "-12-26";
        		dateTo = quarterYear + "-03-25";
        	} else if("2".equals(quarter)){
        		dateFrom = quarterYear + "-03-26";
        		dateTo = quarterYear + "-06-25";
        	} else if("3".equals(quarter)) {
        		dateFrom = quarterYear + "-06-26";
        		dateTo = quarterYear + "09-25";
        	} else if("4".equals(quarter)) {
        		Long year = Long.valueOf(quarterYear)+1;
        		dateFrom = year.toString() + "-09-26";
        		dateTo = quarterYear + "12-25";
        	}
        } else if("4".equals(planOriginalID)||"5".equals(planOriginalID)) {
        	String year = monthPlan.substring(0, 5);
        	String month = monthPlan.substring(5);
        	dateFrom = year+((Long.valueOf(month)-1) > 9 ? "" : "0") + (Long.valueOf(month)-1)+"-26";
        	dateTo = monthPlan+"-25";
        }
        //------------------------------------------------------
        /** 申请部门 CODE */
        String mrDept = request.getParameter("mrDept");
        
        /** 申请人 */
        String mrBy = request.getParameter("mrBy");
        /** 物料名称 */
        String maName = request.getParameter("maName");
        /** 物料类型 */
        String maClassName = request.getParameter("maClassName");
        /** 开始查询位置 */
        int start = Integer.parseInt(request.getParameter("start"));
        /** 查询行数 */
        int limit = Integer.parseInt(request.getParameter("limit"));
        String status=request.getParameter("status");
        /** 获取企业编码 */
        String enterpriseCode = employee.getEnterpriseCode();
        PageObject obj = new PageObject();
        WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(new String[] {
				"hfResourcePlanSC", "hfResourcePlanXZ","hfResourcePlanGDZC","hfResourcePlanJSJ","hfResourcePlanLB"}, employee.getWorkerCode());
     if(entryIds==null||entryIds.equals(""))
     {
    	 entryIds = "''";
     }
		obj=planRemote.findApproveListByFuzzy(enterpriseCode, dateFrom, dateTo, mrDept, mrBy, maName, maClassName, status, entryIds, planOriginalID,employee.getDeptCode(),employee.getWorkerCode(),start, limit);
 
        String str = "{\"list\":[],\"totalCount\":null}";
        if(obj!=null&&obj.getList()!=null)
        {
        	str=JSONUtil.serialize(obj);
        }
      
        write(str);
	}
	
	public void updatePlanDetail() throws JSONException
	{
		String str=request.getParameter("data");
		 Object obj = JSONUtil.deserialize(str);
		   List<Map> list = (List<Map>)obj;
		   mrpDetailRemote=(MrpJPlanRequirementDetailFacadeRemote)factory.getFacadeRemote("MrpJPlanRequirementDetailFacade");
		   for(Map data : list)
		   {
			   String id=((Map) ((Map) data)).get("requirementDetailId").toString();
			   String approvedQty="0";
			   if(((Map) ((Map) data)).get("approvedQty")!=null)
			   {
			    approvedQty=((Map) ((Map) data)).get("approvedQty").toString();
			   }
			   if(id!=null&&!id.equals(""))
			   {
				   MrpJPlanRequirementDetail model=mrpDetailRemote.findById(Long.parseLong(id), employee.getEnterpriseCode());
				   //-----add by fyyang 20100112---------
			
				 
				   if(model.getApprovedQty()==null)
				   {
					   model.setApprovedQty(0.0);
				   }
				   
				   if(model.getApprovedQty()!=Double.parseDouble(approvedQty))
				   {
				   SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
				   String modifyInfo="由"+employee.getWorkerName()+"于"+sf.format(new Date())+"将核准数量由"+model.getApprovedQty()+"改为："+Double.parseDouble(approvedQty)+";";
				   if(model.getModifyMemo()!=null)
				   {
				   model.setModifyMemo(model.getModifyMemo()+modifyInfo);
				   }
				   else
				   {
					   model.setModifyMemo(modifyInfo);
				   }
				   }
				   //----------add end -------------------
				   model.setApprovedQty(Double.parseDouble(approvedQty));
				   mrpDetailRemote.update(model);
			   }
		   }
		   write("{success:true,msg:'保存成功！'}"); 
	}
	
	public void getDeptTypeForPlanApprove()
	{
		String deptCode=request.getParameter("deptCode");
		String deptType=approveRemote.findDeptType(deptCode);
		write(deptType);
	}
	
	
}
