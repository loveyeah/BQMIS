package power.web.manage.budget.action;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmJCostReport;
import power.ejb.manage.budget.CbmJCostReportFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

/**
 * 费用报销管理
 * @author fyyang 20100902
 *
 */
public class CostReportAction extends AbstractAction{
	private CbmJCostReportFacadeRemote CRRemote;
	private CbmJCostReport model;
	private WorkflowService service;
	public CostReportAction(){
		CRRemote=(CbmJCostReportFacadeRemote)factory.getFacadeRemote("CbmJCostReportFacade");
		service = new WorkflowServiceImpl();
	}
	/**
	 * 保存费用报销单信息
	 */
	public void saveOrUpdateExpR(){
		Long reportId=model.getReportId();
		if (reportId==null) {
			//添加
			model.setEnterpriseCode(employee.getEnterpriseCode());
			model.setReportBy(employee.getWorkerCode());
			model.setReportDept(employee.getDeptCode());
			model.setReportDate(new java.util.Date());
			model=CRRemote.save(model);
		} else {
			//修改
			CbmJCostReport CbmJCR=CRRemote.findById(reportId);
			if (model.getReportMoneyLower()!=null) {
				CbmJCR.setReportMoneyLower(model.getReportMoneyLower());
			}//报销金额小写
			if (model.getReportMoneyUpper()!=null&&model.getReportMoneyUpper()!="") {
				CbmJCR.setReportMoneyUpper(model.getReportMoneyUpper());
			}//报销金额大写
			if (model.getItemId()!=null) {
				CbmJCR.setItemId(model.getItemId());
			}//费用来源
			if (model.getReportUse()!=null&&model.getReportUse()!="") {
				CbmJCR.setReportUse(model.getReportUse());
			}//用途
			if (model.getMemo()!=null&&model.getMemo()!="") {
				CbmJCR.setMemo(model.getMemo());
			}//备注
			CRRemote.update(CbmJCR);
		}
		write("{success:true,reportId:'"+model.getReportId()+"'}");
	}
	/**
	 * 费用报销单查询
	 * @throws JSONException 
	 */
	public void finExpRList() throws JSONException{
		String limitl=request.getParameter("limit");
		String startl=request.getParameter("start");
		String flag = request.getParameter("flag");//是否审批 1审批查询，2费用报销查询查询
		//费用报销查询审批查询条件-----------------------------------------
		String startDate=request.getParameter("startDate");
		String endDate=request.getParameter("endDate");
		String entryIds="";
		if("1".equals(flag)){
			entryIds = service.getAvailableWorkflow(new String[] {
			"cBillwout"}, employee.getWorkerCode());
		}	
		//费用报销查询查询条件---------------------------------------------
		String reportorName=request.getParameter("reportorName");
		String reportUse=request.getParameter("reportUse");
		String status=request.getParameter("status");
		PageObject pg=new PageObject();
		if (startl!=null&&!"".equals(startl)&&limitl!=null&&!"".equals(limitl)) {
			int start=Integer.parseInt(startl);
			int limit=Integer.parseInt(limitl);
			pg=	CRRemote.findAll(flag, startDate, endDate, reportorName, reportUse, status, employee.getDeptId(), entryIds, employee.getEnterpriseCode(), start,limit);
		}else{
			pg=	CRRemote.findAll(flag, startDate, endDate, reportorName, reportUse, status, employee.getDeptId(), entryIds, employee.getEnterpriseCode());
		}
		write(JSONUtil.serialize(pg));
	}
	/**
	 * 费用报销单审批
	 */
	public void expRApprove(){
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String eventIdentify = request.getParameter("eventIdentify");
		String applyId=request.getParameter("applyId");
		CRRemote.expRApprove(applyId, entryId, workerCode, actionId, approveText, nextRoles, eventIdentify);
		write("{success:true,data:'审批成功!'}");
	}
	/**
	 * 费用报销单上报
	 */
	public void expRReportTo()
	{
		String actionId=request.getParameter("actionId");
		  String busitNo=request.getParameter("busitNo");
		  String flowCode=request.getParameter("flowCode");
		  String workerCode=request.getParameter("workerCode");
		  String approveText=request.getParameter("approveText");
		  CRRemote.endCheckReportTo(busitNo, workerCode,actionId, approveText,flowCode);
		  write("{success:true,msg:'&nbsp&nbsp&nbsp上报成功&nbsp&nbsp&nbsp'}");
	}
	/**
	 *费用报销增加action
	 */
	public void addCostReport() throws Exception {
		 //System.out.println("12312");
	
		 CbmJCostReport  entity = new CbmJCostReport();
		 entity.setEnterpriseCode(employee.getEnterpriseCode());
		 entity.setReportBy(employee.getWorkerCode());
		 entity.setReportDept(model.getReportDept());
		 entity.setReportDate(new java.util.Date());
		 entity.setReportMoneyUpper(model.getReportMoneyUpper());
		 entity.setWorkFlowStatus("0");
		 entity.setReportMoneyLower(model.getReportMoneyLower());
		 entity.setReportUse(model.getReportUse());
		 entity.setItemId(model.getItemId());
		 entity.setMemo(model.getMemo());
		 CRRemote.save(entity);
		
			write("{success:true,msg:'增加成功！'}");
	}
	/**
	 *费用报销修改action
	 */
	public void updateCostReport() throws Exception {
	       
		    String reportId = request.getParameter("reportId");
		    System.out.println("reportId"+reportId);
		    CbmJCostReport  entity = CRRemote.findById(Long.parseLong(reportId));
    			entity.setReportMoneyUpper(model.getReportMoneyUpper());
			entity.setReportMoneyLower(model.getReportMoneyLower());
			entity.setReportUse(model.getReportUse());
			entity.setMemo(model.getMemo());
			entity.setItemId(model.getItemId());
			CRRemote.update(entity);
			write("{success:true,msg:'修改成功！'}");
			
	}
	/**
	 *费用报销删除action
	 */
	public void deleteCostReport() {
		String ids = request.getParameter("ids");
		CRRemote.delete(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	/**
	 *费用报销查询action
	 */
	public void  creportList() throws JSONException {
	
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String fuzzy = request.getParameter("fuzzy");
		System.out.println("fuzzy111111="+fuzzy);
		System.out.println("a="+employee.getWorkerCode());
		PageObject pat = null;
		if (start != null && limit != null && !start.equals(""))
			pat= CRRemote.getCostReportList(fuzzy,employee.getEnterpriseCode(),employee.getWorkerCode(),Integer.parseInt(start), Integer.parseInt(limit));
		else
			pat = CRRemote.getCostReportList(fuzzy, employee.getEnterpriseCode(),employee.getWorkerCode());
		write(JSONUtil.serialize(pat));
		System.out.println("pat:"+JSONUtil.serialize(pat));
	
	}
	
	//---------------------------------   get/set 方法   ------------------------------------------------
	public CbmJCostReport getModel() {
		return model;
	}
	public void setModel(CbmJCostReport model) {
		this.model = model;
	}
	
}
