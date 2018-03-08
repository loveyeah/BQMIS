package power.web.manage.project.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import power.ear.comm.ejb.PageObject;
import power.ejb.equ.workbill.EquJWo;
import power.ejb.equ.workbill.EquJWoFacadeRemote;
import power.ejb.hr.HrCDept;
import power.ejb.hr.HrCDeptFacadeRemote;
import power.ejb.manage.project.PrjJApply;
import power.ejb.manage.project.PrjJApplyFacadeRemote;
import power.web.comm.AbstractAction;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

public class PrjNewApplyAction extends AbstractAction{
private PrjJApplyFacadeRemote remote;
private EquJWoFacadeRemote remotel;//add by ypan 20100928
private HrCDeptFacadeRemote remotell;//add by ypan 20100928
private PrjJApply pja;
public PrjNewApplyAction() {
	remote=(PrjJApplyFacadeRemote)factory.getFacadeRemote("PrjJApplyFacade");
	remotel=(EquJWoFacadeRemote)factory.getFacadeRemote("EquJWoFacade");//add by ypan 20100928
	remotell=(HrCDeptFacadeRemote)factory.getFacadeRemote("HrCDeptFacade");//add by ypan 20100928
	}
public PrjJApply getPja() {
	return pja;
}
public void setPja(PrjJApply pja) {
	this.pja = pja;
}

/**
 * 查找项目开工申请单信息
 * 通过项目名称模糊查询
 * 状态
 */
public void finPrjApplyByContractNameAndStatus(){
	String ContractName=request.getParameter("ContractName");
	String flag = request.getParameter("flag");
	String status=request.getParameter("status");
	String startl=request.getParameter("start");
	String limitl=request.getParameter("limit");
	PageObject page=new PageObject();
	if (status!=null&&!"".equals(status)) {
		if (startl!=null&&!"".equals(startl)&&limitl!=null&&!"".equals(limitl)) {
			int start=Integer.parseInt(startl);
			int limit=Integer.parseInt(limitl);
			page=remote.findByPropertyAndStatus(ContractName, status, employee.getEnterpriseCode(),employee.getWorkerCode(), start,limit);
		}else{
			page=remote.findByPropertyAndStatus(ContractName, status, employee.getEnterpriseCode(),employee.getWorkerCode());
		}
		try {
			write(JSONUtil.serialize(page));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}else{
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(new String[] {
				"prjApply"}, employee.getWorkerCode());
		if (startl!=null&&!"".equals(startl)&&limitl!=null&&!"".equals(limitl)) {
			int start=Integer.parseInt(startl);
			int limit=Integer.parseInt(limitl);
			page=remote.findAll(ContractName, null, employee.getEnterpriseCode(),entryIds,employee.getDeptId(), flag,employee.getWorkerCode(),start,limit);
		}else{
			page=remote.findAll(ContractName, null, employee.getEnterpriseCode(),entryIds,employee.getDeptId(),flag,employee.getWorkerCode());
		}
		try {
			write(JSONUtil.serialize(page));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
/**
 * 保存项目开工申请信息
 * 判断申请单Id是否为空
 * 为空则保存
 * 不为空则修改
 * @throws ParseException 
 */

public void savePrjApply() throws ParseException{
	pja.setEnterpriseCode(employee.getEnterpriseCode());
	String date=request.getParameter("modifDate");
	String chargeByName=request.getParameter("chargeBy");
	String ContractorName=request.getParameter("contractorName");
	String startTime=request.getParameter("startDate");
	String endTime=request.getParameter("endDate");
	String ContracName=request.getParameter("contractName");
	DateFormat format= new SimpleDateFormat("yyyy-MM-dd");
	Date entryDate=format.parse(date);
	pja.setEntryDate(entryDate);
	pja.setIsUse("Y");
	if (pja.getApplyId()!=null) {
		if (pja.getStatusId()!=0) {
			pja.setContractName(ContracName);
			pja.setChargeBy(chargeByName);
			pja.setContractorName(ContractorName);
			pja.setStartDate(format.parse(startTime));
			pja.setEndDate(format.parse(endTime));
		}
		remote.update(pja);
	}else{
		pja.setEntryBy(employee.getWorkerCode());
		pja.setStatusId(0L);
		System.out.println(pja.getEntryBy());
		pja=remote.save(pja);
	}
	write("{success:true,applyId:'"+pja.getApplyId()+"',statusId:'"+pja.getStatusId()+"'}");
	//write("{success:true,msg:'操作成功！'}");
}
/**
 * 删除项目开工申请单信息
 * 通过申请单Id字符串
 */
public void deletePrjApplyByIds(){
	String ids=request.getParameter("ids");
	if (ids!=null&&!"".equals(ids)) {
		remote.delete(ids);
		write("{success:true,msg:'操作成功！'}");
	}else{
		write("{success:false,msg:'操作失败！'}");
	}
}
//审批
public void checkOperation() {
	String entryId = request.getParameter("entryId");
	String workerCode = request.getParameter("workerCode");
	String actionId = request.getParameter("actionId");
	String approveText = request.getParameter("approveText");
	String nextRoles = request.getParameter("nextRoles");
	String identify = request.getParameter("eventIdentify");
	String applyId=request.getParameter("applyId");
	String dealContent=request.getParameter("content");//add by ypan 20100928
	String dealType=request.getParameter("type");//add by ypan 20100928
	//String fillBy=request.getParameter("by");//add by ypan 20100928
	//String depName=request.getParameter("name");//add by ypan 20100928
	String applyStartDate=request.getParameter("startDate");//add by ypan 20100928
	String applyEndDate=request.getParameter("endDate");//add by ypan 20100928
	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");//add by ypan 20100928
	      
	if (entryId != null && actionId != null) {
		try {
		boolean flag=remote.prjNewApplyApproval(entryId, workerCode, actionId, approveText, nextRoles, identify, applyId, employee.getEnterpriseCode());
		if (flag) {
			write("{success:true,data:'审批成功!'}");
			//add by ypan 20100928  
			PrjJApply pja=new PrjJApply();
			pja =remote.findPrjJApplyById(Long.parseLong(applyId));
			HrCDept d=remotell.getFirstLevelDept(pja.getEntryBy(),"3",employee.getEnterpriseCode());
			if("Y".equals(pja.getIsUse())){
			if(pja.getStatusId()==5){
				EquJWo entity=new EquJWo();
				entity.setWfState(Long.parseLong("0"));//add by ypan 20101011
				entity.setWorkorderContent(dealContent);
				entity.setWorkorderType(dealType);
				entity.setRequireEndtime(format.parse(applyEndDate));
				entity.setRequireStarttime(format.parse(applyStartDate));
				entity.setRepairDepartment(d.getDeptCode());
				entity.setRequireManCode(pja.getEntryBy());
				entity.setEnterprisecode(employee.getEnterpriseCode());
				entity.setWorkorderStatus("0");
				remotel.save(entity, null, null);
			}
		  }
		}else{
			write("{success:true,data:'审批失败!'}");
		}
		} catch (Exception e) {
			write("{success:false,errorMsg:'" + e.getMessage() + "'}");
		}
	}
}
/**
 * 上报
 */
public void reportTo()
{
	  String actionId=request.getParameter("actionId");
	  String busitNo=request.getParameter("busitNo");
	  String flowCode=request.getParameter("flowCode");
	  String workerCode=request.getParameter("workerCode");
	  remote.reportTo(busitNo, flowCode, workerCode,Long.parseLong(actionId));
	  write("{success:true,msg:'&nbsp&nbsp&nbsp上报成功&nbsp&nbsp&nbsp'}");
}
}
