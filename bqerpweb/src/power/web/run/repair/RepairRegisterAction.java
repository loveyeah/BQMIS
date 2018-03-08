package power.web.run.repair;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.equ.workbill.EquJWo;
import power.ejb.equ.workbill.EquJWoFacadeRemote;
import power.ejb.manage.plan.BpJPlanJobDepMain;
import power.ejb.run.repair.RunJRepairProjectDetail;
import power.ejb.run.repair.RunJRepairProjectDetailFacadeRemote;
import power.ejb.run.repair.RunJRepairProjectMain;
import power.ejb.run.repair.RunJRepairProjectMainFacadeRemote;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

@SuppressWarnings("serial")
public class RepairRegisterAction extends AbstractAction {

	protected RunJRepairProjectMainFacadeRemote mainRemote;
	protected RunJRepairProjectDetailFacadeRemote detailRemote;
	protected EquJWoFacadeRemote equWBRemote ;
	private RunJRepairProjectMain main;

	public RepairRegisterAction() {
		mainRemote = (RunJRepairProjectMainFacadeRemote) factory
				.getFacadeRemote("RunJRepairProjectMainFacade");
		detailRemote = (RunJRepairProjectDetailFacadeRemote) factory
				.getFacadeRemote("RunJRepairProjectDetailFacade");
		equWBRemote = (EquJWoFacadeRemote) factory
		.getFacadeRemote("EquJWoFacade");
	}

	public void saveRepairRecord() throws JSONException {
		String method = request.getParameter("method");
		if (method != null && method.equals("add")) {
			RunJRepairProjectMain model = new RunJRepairProjectMain();
			main.setVersion(0l);
			main.setFillBy(employee.getWorkerCode());
			main.setEnterpriseCode(employee.getEnterpriseCode());
			model = mainRemote.save(main);

			write("{success:true,data:" + JSONUtil.serialize(model)
					+ ",msg:'保存成功！'}");

		} else {
			RunJRepairProjectMain model = mainRemote.findById(main.getProjectMainId());
			model.setFillBy(main.getFillBy());
			model.setFillTime(main.getFillTime());
			model.setFinalVersion(main.getFinalVersion());
			model.setMemo(main.getMemo());
			model.setProjectMainYear(main.getProjectMainYear());
			model.setRepairTypeId(main.getRepairTypeId());
			model.setSpecialityId(main.getSpecialityId());
			model.setTasklistId(main.getTasklistId());
			//model.setVersion(main.getVersion()); // modify by drdu 20100528
			model.setSituationProject(main.getSituationProject());
			model.setFillBy(employee.getWorkerCode());
			mainRemote.update(model);

			write("{success:true,data:" + JSONUtil.serialize(model)
					+ ",msg:'保存成功！'}");
		}
	}

	public void deleteRepairRecord() {
		String ids = request.getParameter("ids");
		mainRemote.deleteMult(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public void findRepairRecordList() throws JSONException {
		String year = request.getParameter("year");
		String repairType = request.getParameter("repairType");
		String speciality = request.getParameter("speciality");
		String tastlist = request.getParameter("tastlist");

		String editTime = request.getParameter("editTime");
		String flag = request.getParameter("flag");

		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");

		String entryIds = null;
		if (flag != null && flag.equals("approve")) {
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();

			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqRepairPlanApprove" }, employee
							.getWorkerCode());
		}

		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = mainRemote.findRepairList(employee.getEnterpriseCode(), year,
					repairType, speciality, tastlist, editTime, flag, entryIds,
					start, limit);
		} else {
			obj = mainRemote.findRepairList(employee.getEnterpriseCode(), year,
					repairType, speciality, tastlist, editTime, flag, entryIds);
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	public void findRepairProInfoById() throws JSONException {
		String id = request.getParameter("id");
		Object o = mainRemote.findInfoById(Long.parseLong(id));
		write(JSONUtil.serialize(o));
	}
   public void findSituation() {
		String id = request.getParameter("id");
		String situation=mainRemote.getsituation(id,employee.getEnterpriseCode());
		write(situation);

	}
	public void reportRepairRecord() {
		String repairId = request.getParameter("repairId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");//角色
		String nextRolePs = request.getParameter("nextRolePs");//人员
		String workflowType = request.getParameter("flowCode");
		String actionId = request.getParameter("actionId");
		
		mainRemote.reportRepairRecord(Long.parseLong(repairId), Long.parseLong(actionId),
				employee.getWorkerCode(), approveText, nextRoles, nextRolePs, workflowType);
		write("{success:true,msg:'上报成功！'}");
	}
	
	
	//审批
	public void RepairRecordApprove() throws ParseException{
		String projectMainId = request.getParameter("projectMainId");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");//角色
		String eventIdentify = request.getParameter("eventIdentify");
		String tasklistId = request.getParameter("tasklistId");
		String specialityId = request.getParameter("specialityId");
		mainRemote.RepairRecordApprove(Long.parseLong(projectMainId), Long.parseLong(actionId),
				Long.parseLong(entryId),workerCode, approveText, nextRoles,eventIdentify,
				tasklistId,specialityId,employee.getEnterpriseCode());
		
		RunJRepairProjectMain repair = mainRemote.findById(Long.parseLong(projectMainId));
		//如果审批结束，则生成工单 add by qxjiao 20100929
		if(repair.getWorkflowStatus()==7&&repair.getIsUse().equals("Y")){
			EquJWo wb = new EquJWo();
			List result  = mainRemote.getWBInfo(projectMainId);
			if(result.size()>0){
				SimpleDateFormat fomat = new SimpleDateFormat("yyyy-MM-dd");
				for(int i=0;i<result.size();i++){
					Object[] o = (Object[])result.get(i);
					if(o[0]!=null){
						wb.setWorkorderContent(o[0].toString());
					}
					if(o[1]!=null){
						String type = "";
						if(o[1].toString().equals("1")){
							type = "D";
						}
						if(o[1].toString().equals("3")){
							type = "Z";
						}if(o[1].toString().equals("4")){
							type = "L";
						}if(o[1].toString().equals("2")){
							type = "X";
						}
						wb.setWorkorderType(type);
					}if(o[2]!=null){
						wb.setProfessionCode(o[2].toString());
					}if(o[3]!=null){
						wb.setRequireManCode(o[3].toString());
						wb.setWorkChargeCode(o[3].toString());
					}if(o[4]!=null){
						wb.setRepairDepartment(o[4].toString());
					}if(o[5]!=null){
						wb.setRequireStarttime(fomat.parse(o[5].toString()));
					}if(o[6]!=null){
						wb.setRequireEndtime(fomat.parse(o[6].toString()));
					}
					wb.setWfState(0l);
					wb.setEnterprisecode(employee.getEnterpriseCode());
					wb.setWorkorderStatus("0");
					try {
						equWBRemote.save(wb, null, null);
					} catch (CodeRepeatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
		write("{success:true,msg:'审批成功！'}");
	}
	
	//审批
	public void RepairRecordLeaderApprove(){
		String mainId= request.getParameter("projectMainId");
	    String Id = request.getParameter("entryId");
	    String [] ids= mainId.split(",");
	    String [] eIds= Id.split(",");
			for(int i=0;i<ids.length;i++)
			{
				if(!ids[i].equals(""))
				{
				    String projectMainId=ids[i];
				    String entryId=eIds[i];
					String workerCode = request.getParameter("workerCode");
					String actionId = request.getParameter("actionId");
					String approveText = request.getParameter("approveText");
					String nextRoles = request.getParameter("nextRoles");
					String eventIdentify = request.getParameter("eventIdentify");
				   mainRemote.RepairRecordLeaderApprove(Long.parseLong(projectMainId), Long.parseLong(actionId),
							Long.parseLong(entryId),workerCode, approveText, nextRoles,eventIdentify,
							employee.getEnterpriseCode());

				   write("{success:true,msg:'审批成功！'}");
				}
			}
	}
	
  public void getRepairStatusMain()
	  {
		String repairMainId=request.getParameter("repairMainId");
		String selectMainId=request.getParameter("selectMainId");
		String flag=request.getParameter("flag");
		String str = mainRemote.getRepairStatusMain(repairMainId,selectMainId,flag,employee.getEnterpriseCode());
		write(str);
	  }
	
  public void getRepairMaxVersionMain()
  {
	String tasklistId=request.getParameter("tasklistId");
	String str = mainRemote.getRepairMaxVersionMain(tasklistId,employee.getEnterpriseCode());
	write(str);
  }
  
	public void getRepairVerisonList() throws JSONException
	{
		 String tasklistId=request.getParameter("tasklistId");
		PageObject result =new PageObject ();
		result=mainRemote.getRepairVerisonList(tasklistId,employee.getEnterpriseCode());
		write(JSONUtil.serialize(result));
	}
	

	public void getRepairTastListApprove() throws JSONException {
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");

		String entryIds = null;
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();

		entryIds = workflowService.getAvailableWorkflow(
				new String[] { "bqRepairPlanApprove" }, employee
						.getWorkerCode());
		
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = mainRemote.getRepairTastListApprove(entryIds,employee.getEnterpriseCode(),
					start, limit);
		} else {
			obj = mainRemote.getRepairTastListApprove(entryIds,employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}
	
	
	public void getLeaderApproveInfo() throws JSONException {
		String tasklist = request.getParameter("tasklist");
		PageObject result = new PageObject();
		String entryIds = null;
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();

		entryIds = workflowService.getAvailableWorkflow(
				new String[] { "bqRepairPlanApprove" }, employee
						.getWorkerCode());
		result = mainRemote.getLeaderApproveInfo(tasklist,entryIds,employee
				.getEnterpriseCode());
		write(JSONUtil.serialize(result));
	}
	
	public void getLeaderApproveDetail() throws JSONException {
		String projectMainId = request.getParameter("projectMainId");
		PageObject result = new PageObject();
		result = mainRemote.getLeaderApproveDetail(projectMainId, employee
				.getEnterpriseCode());
		write(JSONUtil.serialize(result));
	}
	
	public void selectApprovePage() {
		
		String str = mainRemote.selectApproveByWorderCode(employee.getWorkerCode());
		write(str);
	}
	
	public void getRepairQueryList() throws JSONException {
		String tasklistId = request.getParameter("tasklistId");
		String version = request.getParameter("version");
		PageObject result = new PageObject();
		result = mainRemote.getRepairQueryList(tasklistId,version,employee.getEnterpriseCode());
		write(JSONUtil.serialize(result));
	}
	
	
	
	// -------------------------------明细表操作---------------------------------

	public void repairDetailList() throws JSONException {
		String repairMainId = request.getParameter("repairMainId");
		PageObject result = new PageObject();
		result = detailRemote.getRepairDetailList(repairMainId, employee
				.getEnterpriseCode());
		write(JSONUtil.serialize(result));
	}

	@SuppressWarnings("unchecked")
	public void saveOrUpdateRecord() throws ParseException, JSONException {
		String str = request.getParameter("isUpdate");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<RunJRepairProjectDetail> addList = null;
		List<RunJRepairProjectDetail> updateList = null;
		if (list != null && list.size() > 0) {
			addList = new ArrayList<RunJRepairProjectDetail>();
			updateList = new ArrayList<RunJRepairProjectDetail>();
			for (Map data : list) {
				String projectDetailId = null;
				String projectMainId = null;
				String repairProjectId = null;
				String standard = null;
				String material = null;
				String workingCharge = null;
				String workingMenbers = null;
				String workingTime = null;
				String startDate = null;
				String endDate = null;
				String situation = null;
				String reason = null;
				String repairProjectName = null;
				String acceptanceSecond=null;//add by wphzu 
				String acceptanceThree=null;
				
				if (data.get("projectDetailId") != null)
					projectDetailId = data.get("projectDetailId").toString();

				if (data.get("projectMainId") != null)
					projectMainId = data.get("projectMainId").toString();

				if (data.get("repairProjectId") != null)
					repairProjectId = data.get("repairProjectId").toString();

				if (data.get("standard") != null)
					standard = data.get("standard").toString();

				if (data.get("material") != null)
					material = data.get("material").toString();

				if (data.get("workingCharge") != null)
					workingCharge = data.get("workingCharge").toString();
				
				if (data.get("workingMenbers") != null)
					workingMenbers = data.get("workingMenbers").toString();
				if (data.get("acceptanceSecond") != null)
					acceptanceSecond = data.get("acceptanceSecond").toString();//add by wphzu 
				
				if (data.get("acceptanceThree") != null)
					acceptanceThree = data.get("acceptanceThree").toString();
				
				if (data.get("workingTime") != null)
					workingTime = data.get("workingTime").toString();

				
				if (data.get("situation") != null)
					situation = data.get("situation").toString();
				
				if (data.get("reason") != null)
					reason = data.get("reason").toString();
				
				if(data.get("startDate") != null)
					startDate = data.get("startDate").toString();
				
				if(data.get("endDate") != null)
					endDate = data.get("endDate").toString();
				
				if(data.get("repairProjectName") != null)
					repairProjectName = data.get("repairProjectName").toString();
				
				DateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				
				RunJRepairProjectDetail model = new RunJRepairProjectDetail();
				if (projectDetailId == null || projectDetailId.equals("")) {
					if (projectMainId != null && !projectMainId.equals(""))
						model.setProjectMainId(Long.parseLong(projectMainId));
					if (repairProjectId != null && !repairProjectId.equals(""))
						model.setRepairProjectId(Long.parseLong(repairProjectId));
					if (standard != null && !standard.equals(""))
						model.setStandard(standard);
					if (material != null && !material.equals(""))
						model.setMaterial(material);
					if(workingCharge != null && !workingCharge.equals(""))
						model.setWorkingCharge(workingCharge);
					if(workingMenbers != null && !workingMenbers.equals(""))
						model.setWorkingMenbers(workingMenbers);
					if(workingTime != null && !workingTime.equals(""))
						model.setWorkingTime(workingTime);
					
					if(situation != null && !situation.equals(""))
						model.setSituation(situation);
					if(reason != null && !reason.equals(""))
						model.setReason(reason);
				
					if(startDate != null && !startDate.equals(""))
						model.setStartDate(sf.parse(startDate));
					
					if(endDate != null && !endDate.equals(""))
						model.setEndDate(sf.parse(endDate));
					
					if(repairProjectName != null && !repairProjectName.equals(""))
						model.setRepairProjectName(repairProjectName);
					if(acceptanceSecond != null && !acceptanceSecond.equals(""))//add by wpzhu 
						model.setAcceptanceSecond(acceptanceSecond);
					if(acceptanceThree != null && !acceptanceThree.equals(""))
						model.setAcceptanceThree(acceptanceThree);
					
					
					model.setIsUse("Y");
					model.setEnterpriseCode(employee.getEnterpriseCode());
					addList.add(model);
				} else {
					model = detailRemote.findById(Long
							.parseLong(projectDetailId));
					if (repairProjectId != null && !repairProjectId.equals(""))
						model.setRepairProjectId(Long
								.parseLong(repairProjectId));
					if (standard != null && !standard.equals(""))
						model.setStandard(standard);
					if (material != null && !material.equals(""))
						model.setMaterial(material);
				
					if(workingCharge != null && !workingCharge.equals(""))
						model.setWorkingCharge(workingCharge);
					if(workingMenbers != null && !workingMenbers.equals(""))
						model.setWorkingMenbers(workingMenbers);
					
					if(situation != null && !situation.equals(""))
						model.setSituation(situation);
					if(reason != null && !reason.equals(""))
						model.setReason(reason);
					
					if(startDate != null && !startDate.equals(""))
						model.setStartDate(sf.parse(startDate));
					if(endDate != null && !endDate.equals(""))
						model.setEndDate(sf.parse(endDate));
					
					if(repairProjectName != null && !repairProjectName.equals(""))
						model.setRepairProjectName(repairProjectName);
					if(acceptanceSecond != null && !acceptanceSecond.equals(""))//add by wpzhu 
						model.setAcceptanceSecond(acceptanceSecond);
					if(acceptanceThree != null && !acceptanceThree.equals(""))
						model.setAcceptanceThree(acceptanceThree);
					
					updateList.add(model);
				}
			}
		}
		detailRemote.saveRepairDetail(addList, updateList);
		write("{success:true,msg:'操作成功！'}");
	}

	public void findHistoryInfo() throws JSONException {
		String spId = request.getParameter("spId");
		String version = request.getParameter("version");
		PageObject result = new PageObject();
		result = mainRemote.findHistoryInfo(spId, version);
		write(JSONUtil.serialize(result));
	}
	
	/**
	 * 检修项目专业取得
	 * add by fyyang 20100617
	 * @throws JSONException
	 */
	public void getRepairSpecialityType() throws JSONException {
		RunCSpecialsFacadeRemote remoteSpecials=(RunCSpecialsFacadeRemote) factory
		.getFacadeRemote("RunCSpecialsFacade");
		// 检修专业取得远程处理对象
		remoteSpecials = (RunCSpecialsFacadeRemote) factory
				.getFacadeRemote("RunCSpecialsFacade");
		// 取得检修项目的专业 “检修”+“检修项目专用”
		List<RunCSpecials> list = remoteSpecials.findByType("2,3",
				Constants.ENTERPRISE_CODE);
		String str = JSONUtil.serialize(list);
		write("{list:" + str + "}");
	}

	
	public void deleteRepairDetail() {
		String ids = request.getParameter("ids");
		detailRemote.deleteMult(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * add by fyyang 20100624
	 */
	public void findVerisonListBySpecial() throws JSONException
	{
		 String specialId=request.getParameter("specialId");
		PageObject result =new PageObject ();
		result=mainRemote.findVersionBySpecial(specialId, employee.getEnterpriseCode());
		write(JSONUtil.serialize(result));
	}
	
	public void findNewVersionBySpecial()
	{
		 String specialId=request.getParameter("specialId");
		 String version=mainRemote.findNewVersionBySpecial(specialId, employee.getEnterpriseCode());
		 write(version);
	}

	public RunJRepairProjectMain getMain() {
		return main;
	}

	public void setMain(RunJRepairProjectMain main) {
		this.main = main;
	}
}
