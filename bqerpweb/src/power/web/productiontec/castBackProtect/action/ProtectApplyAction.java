package power.web.productiontec.castBackProtect.action;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.castBackProtect.PtJProtectApply;
import power.ejb.productiontec.castBackProtect.protectMange;
import power.web.comm.AbstractAction;
import java.util.Date;
import java.util.Calendar ;




@SuppressWarnings("serial")
public class ProtectApplyAction  extends AbstractAction{
	private  protectMange  remote ;
	 private  PtJProtectApply  entity;
	public  ProtectApplyAction()
	{
		remote= (protectMange) factory.getFacadeRemote("protectMangeImpl");
	}
	 public void getProtectApply() throws JSONException
	 {
		 PageObject result = new PageObject();
		 String status = request.getParameter("status");
		 String approve = request.getParameter("approve");
		 String start = request.getParameter("start");
		 String limit = request.getParameter("limit");
		 String enterpriseCode=employee.getEnterpriseCode();
		 String protectType = request.getParameter("protectType");
		 String inOut = request.getParameter("inOut");
		 String entryIds = "";
			if (status != null && status.equals("approve")) {
				WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
				entryIds = workflowService.getAvailableWorkflow(
						new String[] { "bqCastBackProtectApprove" }, employee.getWorkerCode());
			}
			if(start!=null&&limit!=null)
			{
			
		 result=remote.getProtectApply(protectType, approve,status, inOut, enterpriseCode,entryIds,
				 employee.getWorkerCode(),Integer.parseInt(start), Integer.parseInt(limit));
			}else 
			{
				
				 result=remote.getProtectApply(protectType, approve,status, inOut, enterpriseCode,entryIds,employee.getWorkerCode());
			}
			
		 if (result != null) {
//			 System.out.println("the RESULT"+JSONUtil.serialize(result));
          	write(JSONUtil.serialize(result));
		 
          	
			} else {
				write("{totalCount : 0,list :[]}");
			}
		 
	 }
	 
	 //by ghzhou 2010-07-30 增加自动生成投退单编号
	 //继电保护：继电+年份+月份+流水号+投/退
	 //热控保护：热控+年份+月份+流水号+投/退
	 public void getApplyNo() {
		String type = request.getParameter("type");//0:投入；1：退出
		String special = request.getParameter("special");//1，2：继电、3,4：热控
		if ("1".equals(special)||"3".equals(special)) {
			type="1";
		} else {
			type="0";
		}
		String applyNo = "";
		//专业
		if("3".equals(special)||"4".equals(special)){
			special = "热控";
		}else {
			special = "继电";
		}
		//类型
		if("1".equals(type)){
			type = "I";
		}else{
			type = "O";
		}
		
		Calendar time =  Calendar.getInstance();
		
		//得到流水号
		int maxId = remote.getMaxId(type,employee.getEnterpriseCode());
		if("I".equals(type)){
			type = "投";
		}else{
			type = "退";
		}
		
		String no = null;
		if(maxId < 10){
			no = "00" + (maxId);
		}else{
			no = "0" + (maxId);
		}
		//得到月份
		String formatMonth = null;
		int intMonth = time.get(Calendar.MONTH) + 1;
		if(intMonth < 10){
			formatMonth = "0" + intMonth;
		}else{
			formatMonth = intMonth + "";
		}
		//得到日
		String formatDay = null;
		int intDay = time.get(Calendar.DAY_OF_MONTH) + 1;
		if(time.get(Calendar.DAY_OF_MONTH) < 10){
			formatDay = "0" + time.get(Calendar.DAY_OF_MONTH);
		}else{
			formatDay = time.get(Calendar.DAY_OF_MONTH) + "";
		}
		applyNo = special + time.get(Calendar.YEAR) + 
			formatMonth + formatDay + no + type;
		
		write(applyNo);
		//return applyNo;
	 }
//	 public String addApplyNo() {
//			String type = request.getParameter("type");//0:投入；1：退出
//			String special = entity.getProtectionType();//1，2：继电、3：热控
//			String applyNo = "";
//			//专业
//			if("3".equals(special)){
//				special = "热控";
//			}else {
//				special = "继电";
//			}
//			//类型
//			if("I".equals(type)){
//				type = "I";
//			}else{
//				type = "O";
//			}
//			
//			Calendar time =  Calendar.getInstance();
//			
//			//得到流水号
//			int maxId = remote.getMaxId(type);
//			String no = null;
//			if(maxId < 10){
//				no = "00" + maxId;
//			}else{
//				no = "0" + maxId;
//			}
//			//类型
//			if("I".equals(type)){
//				type = "投";
//			}else{
//				type = "退";
//			}
//			//得到月份
//			String formatMonth = null;
//			int intMonth = time.get(Calendar.MONTH) + 1;
//			if(intMonth < 10){
//				formatMonth = "0" + intMonth;
//			}else{
//				formatMonth = intMonth + "";
//			}
//			//得到日
//			String formatDay = null;
//			int intDay = time.get(Calendar.DAY_OF_MONTH) + 1;
//			if(time.get(Calendar.DAY_OF_MONTH) < 10){
//				formatDay = "0" + time.get(Calendar.DAY_OF_MONTH);
//			}else{
//				formatDay = time.get(Calendar.DAY_OF_MONTH) + "";
//			}
//			applyNo = special + time.get(Calendar.YEAR) + 
//				formatMonth + formatDay + no + type;
//			
//			//write(applyNo);
//			return applyNo;
//		 }
	 
	 
	 public void addProtectApply() throws ParseException
	 {
		 
		 String type = request.getParameter("type");
		
		 String time = request.getParameter("time");
//		 System.out.println("the time"+time);
		 PtJProtectApply model = new PtJProtectApply();
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
//			Date date=new Date();
//			model.setApplyTime(dateFm.parse(time));
			model.setApplyTime(new Date());
//			System.out.println("the time"+model.getApplyTime());
			model.setEnterpriseCode(employee.getEnterpriseCode());
			model.setApplyBy(employee.getWorkerCode());
			//String applyNo = addApplyNo();
			//model.setApplyCode(applyNo);
			model.setApplyCode(entity.getApplyCode());
			//add by ypan 20100811
			model.setBlockId(entity.getBlockId());
			model.setApplyDep(employee.getDeptId().toString());
			model.setExecuteTime(entity.getExecuteTime());
			model.setExecutor(entity.getExecutor());
			model.setGuardian(entity.getGuardian());
			model.setInOut(type);
			model.setIsUse("Y");
			model.setMeasures(entity.getMeasures());
			model.setMemo(entity.getMemo());
			model.setProtectionName(entity.getProtectionName());
			model.setProtectionReason(entity.getProtectionReason());
			model.setProtectionType(entity.getProtectionType());
			model.setStatus(entity.getStatus());
			model.setStatus("0");
			model.setWorkflowNo(entity.getWorkflowNo());
			boolean  flag = remote.save(model);
			write("{success:true,msg:'增加成功!'}");
		 
	 }
	 public void updateProtectApply()
	 { 
		 Long  applyId = Long.parseLong(request.getParameter("applyId"));
//		 System.out.println("the  applyid "+applyId);
		 PtJProtectApply model = remote.findById(applyId);
		 model.setMeasures(entity.getMeasures());
			model.setMemo(entity.getMemo());
			model.setProtectionName(entity.getProtectionName());
			model.setProtectionReason(entity.getProtectionReason());
			model.setProtectionType(entity.getProtectionType());
			model.setBlockId(entity.getBlockId());
			remote.update(model);
			write("{success:true,msg:'修改成功!'}");
		 
	 }
	public void  deleteProtect()
	{
		String ids = request.getParameter("ids");
		remote.deleteProtect(ids);
		write("{success:true,msg:'删除成功!'}");
		
	}
	public void protectReport(){
		String actionId=request.getParameter("actionId");
		  String applyId=request.getParameter("applyId");
		  String flowCode=request.getParameter("flowCode");
		  String workerCode=request.getParameter("workerCode");
		  String approveText=request.getParameter("approveText");
		  String nextRoles=request.getParameter("nextRoles");
		  String eventIdentify=request.getParameter("eventIdentify");
	
		  remote.ProtectReport(applyId, flowCode, workerCode,Long.parseLong(actionId),approveText,nextRoles,eventIdentify);
		  write("{success:true,msg:'&nbsp&nbsp&nbsp上报成功&nbsp&nbsp&nbsp'}");
		
		/*String applyId = request.getParameter("applyId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String workflowType = request.getParameter("flowCode");
		String  actionId = request.getParameter("actionId");
		remote.ProtectReport(Long.parseLong(applyId),Long.parseLong(actionId),employee
				.getWorkerCode(), approveText, nextRoles, workflowType);
		write("{success:true,msg:'上报成功！'}");*/
		
	}
	public PtJProtectApply getEntity() {
		return entity;
	}
	public void setEntity(PtJProtectApply entity) {
		this.entity = entity;
	}
	
	
}