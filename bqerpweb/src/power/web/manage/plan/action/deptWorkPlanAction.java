package power.web.manage.plan.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.business.ConJContractInfo;
import power.ejb.manage.plan.BpJPlanJobDepDetail;
import power.ejb.manage.plan.BpJPlanJobDepDetailFacadeRemote;
import power.ejb.manage.plan.BpJPlanJobDepMain;
import power.ejb.manage.plan.BpJPlanJobDepMainFacadeRemote;
import power.ejb.manage.plan.form.BpJPlanJobDepMainForm;
import power.web.comm.AbstractAction;
import power.web.comm.PostMessage;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

@SuppressWarnings("serial")
public class deptWorkPlanAction extends AbstractAction {
	protected BpJPlanJobDepDetailFacadeRemote dRemote;
	protected BpJPlanJobDepMainFacadeRemote mRemote;

	private String editBy;
	private Date editDate;
	private String editDepcode;
	private int start;
	private int limit;
	private String prjNo;
	private String workflowType;
	private String actionId;
	private String approveText;
	private String nextRolePs; //add by sychen 20100327
	private String nextRoles;
	private String eventIdentify;
	private String stepId;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public deptWorkPlanAction() {
		dRemote = (BpJPlanJobDepDetailFacadeRemote) factory
				.getFacadeRemote("BpJPlanJobDepDetailFacade");
		mRemote = (BpJPlanJobDepMainFacadeRemote) factory
				.getFacadeRemote("BpJPlanJobDepMainFacade");
	}

	public void workPlanCommit() {
		try {
//			String isEqu = request.getParameter("isEqu");
//			BpJPlanJobDepMain obj = mRemote.reportTo(prjNo, workflowType,
//					employee.getWorkerCode(), actionId, approveText, nextRoles,
//					employee.getEnterpriseCode(), isEqu);
            //update by sychen 20100327
			BpJPlanJobDepMain obj = mRemote.reportTo(prjNo, workflowType,
					employee.getWorkerCode(), actionId, approveText, nextRolePs,
					nextRoles,employee.getEnterpriseCode());
			
			
			//----------add by ywliu 20100609--短信通知------------
			PostMessage postMsg=new PostMessage();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
			String msg = sdf.format(obj.getPlanTime())+"份月度计划等待您的审批，请您及时处理。";
		   //modify by fyyang 20100626 
	        if(nextRoles!=null&&!nextRoles.equals(""))
	        {
	        	
	        	postMsg.sendMsg(nextRoles, msg);
	        }
	        if(nextRolePs!=null&&!nextRolePs.equals(""))
	        {
	        	
	        	postMsg.sendMsgByWorker(nextRolePs, msg);
	        }
			
			
//			if(nextRoles==null||nextRoles.equals(""))
//			{
//				nextRoles=postMsg.getFistStepRoles(workflowType, actionId, employee.getDeptId().toString(), null);
//			}
//			if(nextRoles!=null&&!nextRoles.equals(""))
//			{
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
//				String msg = sdf.format(obj.getPlanTime())+"份月度计划等待您的审批，请您及时处理。";
//				postMsg.sendMsg(nextRoles, msg);
//			}
			//---------add end------------------------------------------
			write("{success:true,msg:'操作成功！',obj:" + JSONUtil.serialize(obj)
					+ "}");
		} catch (Exception e) {
			write("{failure:true,msg:'" + e.getMessage() + "'}");
		}
	}

	public void workPlanApprove() {
		try {//update  by sychen 20100407
			String prjNo= request.getParameter("prjNo");
		     String [] prjNos= prjNo.split(",");
		     System.out.println(nextRoles);
				for(int i=0;i<prjNos.length;i++)
				{
					if(!prjNos[i].equals(""))
					{
					// ----------add by ywliu 20100609--短信通知------------
					PostMessage postMsg = new PostMessage();
					String thisRoles = nextRoles;
					if (!"TH".equals(eventIdentify) && !"133".equals(actionId)
							&& !"113".equals(actionId)
							&& !"123".equals(actionId)
							&& !"83".equals(actionId)) {
						if (nextRoles == null || nextRoles.equals("")) {
							BpJPlanJobDepMain entity = mRemote.findById(Long.parseLong(prjNos[i]));
							thisRoles = postMsg.getNextSetpRoles(entity.getWorkFlowNo().toString(),
									actionId);
						}
					}
					// ----------add end------------
					String deptMainId = prjNos[i];
					BpJPlanJobDepMain obj = mRemote.approveStep(deptMainId,
							workflowType, employee.getWorkerCode(), actionId,
							eventIdentify, approveText, nextRoles, stepId,
							employee.getEnterpriseCode());
					// ----------add by ywliu 20100609--短信通知------------
					if (!"TH".equals(eventIdentify) && !"133".equals(actionId)
							&& !"113".equals(actionId)
							&& !"123".equals(actionId)
							&& !"83".equals(actionId)) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
						String msg = sdf.format(obj.getPlanTime())+"份月度计划等待您的审批，请您及时处理。";
					 //modify by fyyang 20100626
						//指定的人不为空时发短信给指定的人，为空是发给下一步角色
						if(nextRoles!=null&&!nextRoles.equals(""))
						{
							//System.out.println("人："+nextRoles);
							postMsg.sendMsgByWorker(nextRoles, msg);
						}
						else
						{
						if (thisRoles != null && !thisRoles.equals("")) {
							//System.out.println("角色："+thisRoles);
							postMsg.sendMsg(thisRoles, msg);
						}
						}
					}
					// ----------add end------------
					write("{success:true,msg:'操作成功！',obj:"
							+ JSONUtil.serialize(obj) + "}");
					}
				}
			
			
		} catch (Exception e) {
			write("{failure:true,msg:'" + e.getMessage() + "'}");
		}
	}

	public void getBpJPlanJobDepMain() throws JSONException, ParseException {
		String planTime = request.getParameter("planTime");
		String editBy = request.getParameter("editBy");
		String approve = request.getParameter("approve");
		String flag = request.getParameter("flag");
		String editDeptcode = request.getParameter("editDepcode");
		String entryIds = null;
		if (approve != null && approve.equals("approve")) {
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
			//update by sychen 20100326
//			entryIds = workflowService.getAvailableWorkflow(
//					new String[] { "bqDeptJobplan" }, employee.getWorkerCode());
//		}
			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqDeptWorkPlanApprove" }, employee.getWorkerCode());
		}

//		BpJPlanJobDepMainForm obj = mRemote.getBpJPlanJobDepMain(approve,
//				entryIds, planTime, editBy, employee.getEnterpriseCode());
		//update by sychen 20100414
		BpJPlanJobDepMainForm obj = mRemote.getBpJPlanJobDepMain(approve,flag,
				entryIds, planTime, editBy, editDeptcode,employee.getEnterpriseCode());
		write(JSONUtil.serialize(obj));
	}

	//add by sychen 20100406
	public void getBpJPlanJobDepMainApprove() throws JSONException, ParseException {
		String planTime = request.getParameter("planTime");
		String editBy = request.getParameter("editBy");
		String approve = request.getParameter("approve");
		String entryIds = null;
		if (approve != null && approve.equals("approve")) {
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();

			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqDeptWorkPlanApprove" }, employee.getWorkerCode());
		}
		PageObject pg = null;
		try {
			pg = mRemote.getBpJPlanJobDepMainApprove(approve,
					entryIds, planTime, editBy, employee.getEnterpriseCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(JSONUtil.serialize(pg));
		write(JSONUtil.serialize(pg));
	}
	
	
	//add by sychen 20100504
	@SuppressWarnings("unchecked")

	public void getBpJPlanJobDepMainCaller() throws JSONException {
		String entryId = request.getParameter("entryId");
		String caller = mRemote.getBpJPlanJobDepMainCaller(entryId);
		String outStr = caller ;
	
		write(JSONUtil.serialize(outStr));
	}
	
	//add by sychen 20100423
	public void getBpJPlanJobDepMainQuery() throws JSONException, ParseException {
		String planTime = request.getParameter("planTime");
		String editDeptcode = request.getParameter("editDepcode");
		PageObject pg = null;
		try {
			pg = mRemote.getBpJPlanJobDepMainQuery(planTime, editDeptcode, employee.getEnterpriseCode());
		} catch (Exception e) {
			e.printStackTrace();
		}

		write(JSONUtil.serialize(pg));
		System.out.println(JSONUtil.serialize(pg));
	}
	
	/**
	 * add by sychen 20100407 获取部门数据源
	 * @throws JSONException
	 */
	public void getBpJPlanJobDept() throws JSONException
	{
		List list =  mRemote.getBpJPlanJobDept();
		write(JSONUtil.serialize(list));
	}
	
	public void getBpJPlanJobDepMainList() throws JSONException, ParseException {
		String planTime = request.getParameter("planTime");
		String finish = request.getParameter("finish");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		PageObject pg = new PageObject();
		if (start != null && limit != null)
			pg = mRemote.getBpJPlanJobDepMainList(finish, planTime,employee.getEnterpriseCode(), 
					Integer.parseInt(start), Integer.parseInt(limit));
		else
			pg = mRemote.getBpJPlanJobDepMainList(finish, planTime,employee.getEnterpriseCode());

		write(JSONUtil.serialize(pg));
	}

	public void getBpJPlanJobDepDetailStat() throws JSONException,
			ParseException {
		String planTime = request.getParameter("planTime");

		PageObject obj = dRemote.getBpJPlanJobDepDetailStat(planTime, employee
				.getEnterpriseCode());
		write(JSONUtil.serialize(obj));

	}

	public void getBpJPlanJobDepDetail() throws JSONException {
		//update by sychen 20100630
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String depMainId = request.getParameter("depMainId");

		PageObject pg = new PageObject();
		if (start != null && limit != null)
			pg = dRemote.findByDepMainId(depMainId,employee.getEnterpriseCode(), Integer.parseInt(start), Integer.parseInt(limit));
		else
			pg =  dRemote.findByDepMainId(depMainId,employee.getEnterpriseCode());

		
//		String depMainId = request.getParameter("depMainId");
//		List<BpJPlanJobDepDetail> list = dRemote.findByDepMainId(depMainId);
		
		//update by sychen 20100630 end
		
		write(JSONUtil.serialize(pg));

	}
//add by sychen 20100406
	public void getBpJPlanJobDepDetailApprove() throws Exception {

		String depMainId = request.getParameter("depMainId");
		PageObject pg = null;
		pg = dRemote.findByDepMainIdApprove(depMainId);

		write(JSONUtil.serialize(pg));

	}
	
	  public void getBpJPlanJobDeptMainId()
	  {
		String planTime=request.getParameter("planTime");
		String str = mRemote.getBpJPlanJobDeptMainId(employee.getWorkerCode(),planTime,employee.getEnterpriseCode());
		write(str);
	  }
	
	public void queryBpJPlanJobDepDetail() throws JSONException {

		String planTime = request.getParameter("planTime");
		String editDepcode = request.getParameter("editDepcode");
		String flag = request.getParameter("flag");
//		PageObject list = dRemote.queryBpJPlanJobDepDetail(planTime,
//				editDepcode,employee.getEnterpriseCode(), start, limit);
		//update by sychen 20100414
		PageObject list = dRemote.queryBpJPlanJobDepDetail(planTime,
				editDepcode,employee.getWorkerCode(),flag,employee.getEnterpriseCode(), start, limit);

		write(JSONUtil.serialize(list));
		System.out.println(JSONUtil.serialize(list));
	}
	//部门月度计划审批保存主表记录方法 add by sychen 20100409
	public BpJPlanJobDepMain saveBpJPlanJobDepMainApprove(String deptCodeA) throws ParseException {

		String deptCode1 = employee.getDeptCode();
		String planTime = request.getParameter("planTime");
		String flag = request.getParameter("flag");   
		String workflowType = request.getParameter("workflowType"); 
		String signStatus = request.getParameter("signStatus");
		BpJPlanJobDepMain model = new BpJPlanJobDepMain();
		model.setEditBy(employee.getWorkerCode());
		model.setEditDate(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date planDate = sdf.parse(planTime);
		model.setPlanTime(planDate);
		model.setSignStatus(Long.parseLong(signStatus));
		model.setEditDepcode(deptCodeA);
		
		model.setEnterpriseCode(employee.getEnterpriseCode());

		 model.setIfRead("N"); 
		BpJPlanJobDepMain baseInfo = mRemote.save(model,deptCode1,flag,workflowType,
				employee.getWorkerCode(),signStatus);
		return baseInfo;

	}

	public BpJPlanJobDepMain saveBpJPlanJobDepMain() throws ParseException {

		String deptCode1 = employee.getDeptCode();
		String planTime = request.getParameter("planTime");
		// modified by liuyi 20100423 
//		String deptCode = request.getParameter("deptCode");
		String deptCode = employee.getDeptCode();
        //add by sychen 20100408
		String flag = request.getParameter("flag");   
		String workflowType = request.getParameter("workflowType"); 
		BpJPlanJobDepMain model = new BpJPlanJobDepMain();
		model.setEditBy(employee.getWorkerCode());
		model.setEditDate(new Date());
		model.setEditDepcode(deptCode);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date planDate = sdf.parse(planTime);
		model.setPlanTime(planDate);
		// 新增时工作流状态设置为0L(未上报)
		
			model.setSignStatus(0L);
		// model.setPlanStatus(0L);
		model.setEnterpriseCode(employee.getEnterpriseCode());

		 model.setIfRead("N"); // add by sychen 20100408
//		BpJPlanJobDepMain baseInfo = mRemote.save(model,deptCode1);
		BpJPlanJobDepMain baseInfo = mRemote.save(model,deptCode1,flag,workflowType,
				employee.getWorkerCode(),employee.getWorkerCode());
		return baseInfo;

	}

	public void updateBpJPlanJobDepMain(String mainId) throws ParseException {

		BpJPlanJobDepMain model = mRemote.findById(Long.parseLong(mainId));
		model.setEditBy(employee.getWorkerCode());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date editDate = sdf.parse(sdf.format(new Date()));
		model.setEditDate(editDate);
		//model.setEditDepcode(employee.getDeptCode());

		mRemote.update(model);

	}
	
	public void saveBpJPlanJobDep() throws Exception {
		try {
			String mainId = request.getParameter("mainId");
			String planTime = request.getParameter("planTime");

			BpJPlanJobDepMain baseInfo = new BpJPlanJobDepMain();
			//上报后增加补充计划（strId 为未上报和退回的主记录的主键）add by sychen 20100429 
			String strId = mRemote.getPlanJobDeptNewMainId(planTime,employee.getWorkerCode(),employee.getEnterpriseCode());
			if (mainId.equals("0") || mainId == null
					||strId.equals("")||strId==null) {//add by sychen 20100429

				baseInfo = saveBpJPlanJobDepMain();

			} else {
				if(mainId.equals("0") ||mainId.equals("") || mainId == null)
			       mainId =strId;
				updateBpJPlanJobDepMain(mainId);
			}
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<BpJPlanJobDepDetail> addList = new ArrayList<BpJPlanJobDepDetail>();
			List<BpJPlanJobDepDetail> updateList = new ArrayList<BpJPlanJobDepDetail>();

			List<Map> list = (List<Map>) obj;
			String depMainId = null;
			for (Map data : list) {

				String jobContent = null;
				String jobId = null;
				String ifComplete = null;
				String completeData = null;
				String completeDesc = null;
				String chargeBy=null;//add by sychen 20100504
				String orderBy =null;//add by sychen 20100505

				depMainId = baseInfo.getDepMainId() == null ? data.get(
						"depMainId").toString() : baseInfo.getDepMainId()
						.toString();

				if (data.get("ifComplete") != null) {
					ifComplete = data.get("ifComplete").toString();
				}
				if (data.get("completeData") != null) {
					completeData = data.get("completeData").toString();
				}
				if (data.get("completeDesc") != null) {
					completeDesc = data.get("completeDesc").toString();
				}
				if (data.get("jobContent") != null) {
					jobContent = data.get("jobContent").toString();
				}

				if (data.get("jobId") != null) {
					jobId = data.get("jobId").toString();
				}
				//add by sychen 20100504
				if (data.get("chargeBy") != null) {
					chargeBy = data.get("chargeBy").toString();
				}
				//add by sychen 20100505
				if (data.get("orderBy") != null) {
					orderBy = data.get("orderBy").toString();
				}

				BpJPlanJobDepDetail model = new BpJPlanJobDepDetail();

				// 增加
				if (jobId == null) {
					model.setDepMainId(Long.parseLong(depMainId));
					model.setJobContent(jobContent);
					model.setCompleteData(completeData);
					model.setCompleteDesc(completeDesc);
					model.setIfComplete(ifComplete);
					model.setChargeBy(chargeBy);//add by sychen 20100504
					model.setOrderBy(orderBy);//add by sychen 20100505
					model.setEnterpriseCode(employee.getEnterpriseCode());

					addList.add(model);

				} else {
					model = dRemote.findById(Long.parseLong(jobId));

					model.setJobContent(jobContent);
					model.setCompleteDesc(completeDesc);
					model.setIfComplete(ifComplete);
					model.setCompleteData(completeData);
					model.setChargeBy(chargeBy);//add by sychen 20100504
					model.setOrderBy(orderBy);//add by sychen 20100505
					updateList.add(model);

				}

			}

			if (addList.size() > 0)
				dRemote.save(addList);

			if (updateList.size() > 0)

				dRemote.update(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))
				dRemote.delete(deleteIds);
			write("{success:true,msg:'保存成功！',obj:" + depMainId + "}");

		} catch (Exception exc) {
			write("{success:false,msg:'操作失败'}");
			throw exc;

		}
	}

	/**
	 * 部门月度计划审批保存方法 add by sychen 20100409
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveBpJPlanJobDepApprove() throws Exception {
		try {
			String planTime = request.getParameter("planTime");
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<BpJPlanJobDepDetail> addList = new ArrayList<BpJPlanJobDepDetail>();
			List<BpJPlanJobDepDetail> updateList = new ArrayList<BpJPlanJobDepDetail>();

			List<Map> list = (List<Map>) obj;
			String depMainId = null;
			for (Map data : list) {

				String jobContent = null;
				String jobId = null;
				String ifComplete = null;
				String completeData = null;
				String completeDesc = null;
				String chargeBy=null;
				String orderBy=null;// add by sychen 20100505
				
				BpJPlanJobDepMain baseInfo = new BpJPlanJobDepMain();

				if(data.get("depMainId").toString()==null||data.get("depMainId").toString().equals("0"))
				{
					String method="add";
					String strId = mRemote.getPlanJobDeptMainId(planTime, data.get("editDepcode").toString(),
							employee.getWorkerCode(),employee.getEnterpriseCode(),method,data.get(
							"depMainId").toString());
					if(strId.equals("")||strId==null){
						baseInfo =saveBpJPlanJobDepMainApprove(data.get("editDepcode").toString());
						
						depMainId = baseInfo.getDepMainId() == null ? data.get(
						"depMainId").toString() : baseInfo.getDepMainId()
						.toString();
					}
					else{
						depMainId=strId;
						
					}
					
				}
				
				if (data.get("ifComplete") != null) {
					ifComplete = data.get("ifComplete").toString();
				}
				if (data.get("completeData") != null) {
					completeData = data.get("completeData").toString();
				}
				if (data.get("completeDesc") != null) {
					completeDesc = data.get("completeDesc").toString();
				}
				if (data.get("jobContent") != null) {
					jobContent = data.get("jobContent").toString();
				}

				if (data.get("jobId") != null) {
					jobId = data.get("jobId").toString();
				}
				if (data.get("chargeBy") != null) {
					chargeBy = data.get("chargeBy").toString();
				}
				// add by sychen 20100505
				if (data.get("orderBy") != null) {
					orderBy = data.get("orderBy").toString();
				}

				BpJPlanJobDepDetail model = new BpJPlanJobDepDetail();

				 
				if (jobId == null) {
					model.setDepMainId(Long.parseLong(depMainId));
					model.setJobContent(jobContent);
					model.setCompleteData(completeData);
					model.setCompleteDesc(completeDesc);
					model.setIfComplete(ifComplete);
					model.setChargeBy(chargeBy);
					model.setOrderBy(orderBy);// add by sychen 20100505
					model.setEnterpriseCode(employee.getEnterpriseCode());

					addList.add(model);

				} else {
					model = dRemote.findById(Long.parseLong(jobId));

					model.setJobContent(jobContent);
					model.setCompleteDesc(completeDesc);
					model.setIfComplete(ifComplete);
					model.setCompleteData(completeData);
					model.setChargeBy(chargeBy);
					model.setOrderBy(orderBy);// add by sychen 20100505

					updateList.add(model);

				}

			}

			if (addList.size() > 0)
				dRemote.save(addList);

			if (updateList.size() > 0)

				dRemote.update(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))
				dRemote.delete(deleteIds);
			
			 write("{success:true,msg:'保存成功！',obj:" + depMainId + "}");

		} catch (Exception exc) {
			write("{success:false,msg:'操作失败'}");
			throw exc;

		}
	}
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getEditBy() {
		return editBy;
	}

	public void setEditBy(String editBy) {
		this.editBy = editBy;
	}

	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public String getEditDepcode() {
		return editDepcode;
	}

	public void setEditDepcode(String editDepcode) {
		this.editDepcode = editDepcode;
	}

	public String getPrjNo() {
		return prjNo;
	}

	public void setPrjNo(String prjNo) {
		this.prjNo = prjNo;
	}

	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getApproveText() {
		return approveText;
	}

	public void setApproveText(String approveText) {
		this.approveText = approveText;
	}

	
	

	public String getNextRolePs() {
		return nextRolePs;
	}

	public void setNextRolePs(String nextRolePs) {
		this.nextRolePs = nextRolePs;
	}

	public String getNextRoles() {
		return nextRoles;
	}

	public void setNextRoles(String nextRoles) {
		this.nextRoles = nextRoles;
	}

	/**
	 * add by liuyi 091224
	 * 
	 * @throws JSONException
	 * @throws ParseException
	 */
	public void getBpPlanDeptModList() throws JSONException, ParseException {
		String planTime = request.getParameter("planTime");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String approve = request.getParameter("approve");
		PageObject obj = new PageObject();
		String entryIds = "";
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		entryIds = workflowService.getAvailableWorkflow(
				new String[] { "bqDeptJobplangather" }, employee
						.getWorkerCode());
		if (start != null && limit != null)
			obj = mRemote.getBpPlanDeptModList(approve, entryIds, planTime,
					employee.getEnterpriseCode(), Integer.parseInt(start),
					Integer.parseInt(limit));
		else
			obj = mRemote.getBpPlanDeptModList(approve, entryIds, planTime,
					employee.getEnterpriseCode());
		if (obj != null) {
			write(JSONUtil.serialize(obj));
		} else {
			write("{totalCount : 0,list :[]}");
		}

	}

	/**
	 * add by drdu 20100106 部门工作计划整理上报页面中“整体展示”方法
	 * 
	 * @throws JSONException
	 * @throws ParseException
	 */
	public void getBpPlanDeptShowAllList() throws JSONException, ParseException {
		String planTime = request.getParameter("planTime");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject obj = new PageObject();

		if (start != null && limit != null)
			obj = mRemote.getBpPlanDeptShowAllList(planTime, employee
					.getEnterpriseCode(), Integer.parseInt(start), Integer
					.parseInt(limit));
		else
			obj = mRemote.getBpPlanDeptShowAllList(planTime, employee
					.getEnterpriseCode());

		write(JSONUtil.serialize(obj));
	}

	/**
	 * add by sychen 20100415 部门工作计划完成情况整理上报页面中“整体展示”方法
	 * 
	 * @throws JSONException
	 * @throws ParseException
	 */
	public void getBpPlanDeptCompleteShowAllList() throws JSONException, ParseException {
		String planTime = request.getParameter("planTime");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject obj = new PageObject();

		if (start != null && limit != null)
			obj = mRemote.getBpPlanDeptCompleteShowAllList(planTime, employee
					.getEnterpriseCode(), Integer.parseInt(start), Integer
					.parseInt(limit));
		else
			obj = mRemote.getBpPlanDeptCompleteShowAllList(planTime, employee
					.getEnterpriseCode());

		write(JSONUtil.serialize(obj));
	}
	
	//显示当前月份下责任人下多个主表记录 add by sychen 20100505
	public void getBpPlanStatusByChargeBy() throws JSONException, ParseException {
		String deptH = request.getParameter("deptH");
		String planTime = request.getParameter("planTime");
		String entryIds = "";
		
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		
		entryIds = workflowService.getAvailableWorkflow(
				new String[] { "bqDeptPlanFinishApprove" }, employee.getWorkerCode());
		
		PageObject pg = null;
		try {
			pg = mRemote.getBpPlanStatusByChargeBy(deptH, planTime, entryIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		write(JSONUtil.serialize(pg));
	}
	
	
	/**
	 * add by liuyi 091224 通过部门编码，月份查询数据，用于判断状态
	 * 
	 * @throws JSONException
	 */
	public void getBpPlanStatusByDeptCode() throws JSONException {
		String deptH = request.getParameter("deptH");
		String planTime = request.getParameter("planTime");
		String entryIds = "";
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
//		entryIds = workflowService.getAvailableWorkflow(
//				new String[] { "bqDeptJobFinish" }, employee.getWorkerCode());
		//update by sychen 20100329
		entryIds = workflowService.getAvailableWorkflow(
				new String[] { "bqDeptPlanFinishApprove" }, employee.getWorkerCode());
		List list = null;
		// if( planTime != null)
		list = mRemote.getBpPlanStatusByDeptCode(deptH, planTime, entryIds);
		write(JSONUtil.serialize(list));
	}

	/**
	 * add by liuyi 091224
	 * 
	 * @throws JSONException
	 */
	public void getPlanJobCompleteDetialList() throws JSONException {
		// modified by liuyi 20100525 
//		String deptMainId = request.getParameter("depMainId");
		String deptMainId = request.getParameter("deptMainId");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String isApprove = request.getParameter("isApprove");
		String entryIds = "";
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
//		entryIds = workflowService.getAvailableWorkflow(
//				new String[] { "bqDeptJobFinish" }, employee.getWorkerCode());
		//update by sychen 20100329
		entryIds = workflowService.getAvailableWorkflow(
				new String[] { "bqDeptPlanFinishApprove" }, employee.getWorkerCode());
		PageObject pg = new PageObject();
		if (start != null && limit != null)
			pg = mRemote.getPlanJobCompleteDetialList(isApprove, entryIds,
					deptMainId, employee.getEnterpriseCode(),employee.getWorkerCode(), Integer
							.parseInt(start), Integer.parseInt(limit));
		else
			pg = mRemote.getPlanJobCompleteDetialList(isApprove, entryIds,
					deptMainId, employee.getEnterpriseCode(),employee.getWorkerCode());

		if (pg.getTotalCount() > 0) {
			write(JSONUtil.serialize(pg));
		} else {
			write("{totalCount : 0,list :[]}");
		}
	}
	
// update by sychen 20100613
//	public void saveDeptPlanComplete() throws JSONException {
//		String updates = request.getParameter("updates");
////		String deptMainId = request.getParameter("deptMainId");
////		BpJPlanJobDepMain deptMain = null;
////		if (deptMainId != null)
////			deptMain = mRemote.findById(Long.parseLong(deptMainId));
////		if (deptMain != null) {
////			deptMain.setFinishEditBy(employee.getWorkerCode());
////			deptMain.setFinishEditDate(new Date());
////			deptMain.setFinishSignStatus(0L);
////			 deptMain.setFinishIfRead("N");
////			mRemote.update(deptMain);
////		}
//
//		Object upObject = JSONUtil.deserialize(updates);
//		if (upObject instanceof List) {
//			List<Map> upMapList = (List<Map>) upObject;
//			for (Map map : upMapList) {
//				BpJPlanJobDepMain deptMain = new BpJPlanJobDepMain();
//				if (map.get("depMainId") != null)
//					deptMain = mRemote.findById(Long.parseLong(map.get("depMainId")
//							.toString()));
//				deptMain.setFinishEditBy(employee.getWorkerCode());
//				deptMain.setFinishEditDate(new Date());
//				deptMain.setFinishSignStatus(0L);
//				deptMain.setFinishIfRead("N");
//				mRemote.update(deptMain);
//				
//				
//				BpJPlanJobDepDetail entity = new BpJPlanJobDepDetail();
//				if (map.get("jobId") != null)
//					entity = dRemote.findById(Long.parseLong(map.get("jobId")
//							.toString()));
//				if (map.get("ifComplete") != null)
//					entity.setIfComplete(map.get("ifComplete").toString());
//				if (map.get("completeDesc") != null)
//					entity.setCompleteDesc(map.get("completeDesc").toString());
//				/*if (map.get("orderBy") != null)//add by wpzhu 2010/05/24
//					entity.setOrderBy(map.get("orderBy").toString());*/
//				dRemote.update(entity);
//			}
//		}
//	}

	/**
	 * 部门工作计划完成情况填写上报
	 */
	public void reportDeptPlanComplete() {

		String deptMainId = request.getParameter("deptMainId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");

		String nextRolePs = request.getParameter("nextRolePs");
		String workflowType = request.getParameter("workflowType");
		String actionId = request.getParameter("actionId");
		
	   // add by sychen 20100505
	    String [] deptMainIds= deptMainId.split(",");
			for(int i=0;i<deptMainIds.length;i++)
			{
				if (!deptMainIds[i].equals("")) {
				String mainId = deptMainIds[i];
				mRemote.reportDeptPlanComplete(Long.parseLong(mainId), Long
						.parseLong(actionId), employee.getWorkerCode(),
						approveText, nextRoles, nextRolePs, workflowType);
				// ----------add by ywliu 20100610--短信通知------------
				//modify by fyyang 20100626
				PostMessage postMsg = new PostMessage();
//				if (nextRoles == null || nextRoles.equals("")) {
//					nextRoles = postMsg.getFistStepRoles(workflowType,
//							actionId, null, null);
//				}
//				if (nextRoles != null && !nextRoles.equals("")) {
//					BpJPlanJobDepMain model = mRemote.findById(Long.parseLong(mainId));
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
//					String msg = sdf.format(model.getPlanTime())
//							+ "份月度计划等待您的审批，请您及时处理。";
//					postMsg.sendMsg(nextRoles, msg);
//				}
				   //modify by fyyang 20100626 
				BpJPlanJobDepMain model = mRemote.findById(Long.parseLong(mainId));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
				String msg = sdf.format(model.getPlanTime())
						+ "份月度计划等待您的审批，请您及时处理。";
		        if(nextRoles!=null&&!nextRoles.equals(""))
		        {
		        	
		        	postMsg.sendMsg(nextRoles, msg);
		        }
		        if(nextRolePs!=null&&!nextRolePs.equals(""))
		        {
		        	
		        	postMsg.sendMsgByWorker(nextRolePs, msg);
		        }
				
				// ---------add end------------------------------------------

				write("{success:true,msg:'上报成功！'}");
			}
			}
			// add end//
//		String isEqu = request.getParameter("isEqu");
//		mRemote.reportDeptPlanComplete(Long.parseLong(deptMainId), Long
//				.parseLong(actionId), employee.getWorkerCode(), approveText,
//				nextRoles, workflowType, isEqu);
		//update by sychen 20100329
//		mRemote.reportDeptPlanComplete(Long.parseLong(deptMainId), Long
//				.parseLong(actionId), employee.getWorkerCode(), approveText,nextRoles, nextRolePs,
//				 workflowType);
//		
//		write("{success:true,msg:'上报成功！'}");
	}

	public void deptPlanCompleteApprove() {
		try {
			// update by sychen 20100408
			String mainId = request.getParameter("deptMainId");
		     String [] mainIds= mainId.split(",");
				for(int i=0;i<mainIds.length;i++)
				{
					if(!mainIds[i].equals(""))
					{
						// ----------add by ywliu 20100609--短信通知------------
						PostMessage postMsg = new PostMessage();
						String thisRoles = nextRoles;
						if (!"TH".equals(eventIdentify) && !"133".equals(actionId)
								&& !"113".equals(actionId)
								&& !"123".equals(actionId)
								&& !"83".equals(actionId)) {
							if (nextRoles == null || nextRoles.equals("")) {
								BpJPlanJobDepMain entity = mRemote.findById(Long.parseLong(mainIds[i]));
								thisRoles = postMsg.getNextSetpRoles(entity
										.getFinishWorkFlowNo().toString(), actionId);
							}
						}
						// ----------add end------------
						String deptMainId = mainIds[i];
						BpJPlanJobDepMain obj = mRemote.deptPlanCompleteApprove(
								deptMainId, workflowType, employee.getWorkerCode(),
								actionId, eventIdentify, approveText, nextRoles,
								stepId, employee.getEnterpriseCode());
						// ----------add by ywliu 20100609--短信通知------------
						if (!"TH".equals(eventIdentify) && !"133".equals(actionId)
								&& !"113".equals(actionId)
								&& !"123".equals(actionId)
								&& !"83".equals(actionId)) {
							//modify by fyyang 20100626
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
							String msg = sdf.format(obj.getPlanTime())+"份月度计划等待您的审批，请您及时处理。";
							if(nextRoles!=null&&!nextRoles.equals(""))
							{
								System.out.println("人："+nextRoles);
								postMsg.sendMsgByWorker(nextRoles, msg);
							}
							else
							{
							if (thisRoles != null && !thisRoles.equals("")) {
								System.out.println("角色："+thisRoles);
								postMsg.sendMsg(thisRoles, msg);
							}
							}
						}
						// ----------add end------------
	
						write("{success:true,msg:'操作成功！',obj:"
								+ JSONUtil.serialize(obj) + "}");
					}
				}
		
			
//			String deptMainId = request.getParameter("deptMainId");
//			BpJPlanJobDepMain obj = mRemote.deptPlanCompleteApprove(deptMainId,
//					workflowType, employee.getWorkerCode(), actionId,
//					eventIdentify, approveText, nextRoles, stepId, employee
//							.getEnterpriseCode());
//
//			write("{success:true,msg:'操作成功！',obj:" + JSONUtil.serialize(obj)
//					+ "}");
		} catch (Exception e) {
			write("{failure:true,msg:'" + e.getMessage() + "'}");
		}
	}

	public void getPlanCompleteAllQuery() throws JSONException {
		String planTime = request.getParameter("planTime");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String isApprove = request.getParameter("isApprove");
		PageObject pg = new PageObject();
		;
		String entryIds = "";
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
//		entryIds = workflowService.getAvailableWorkflow(
//				new String[] { "bqDeptJobFinishGather" }, employee
//						.getWorkerCode());
		//update by sychen 20100329
		entryIds = workflowService.getAvailableWorkflow(
				new String[] { "bqDeptWorkPlanApprove" }, employee
						.getWorkerCode());
		if (start != null && limit != null)
			pg = mRemote.getPlanCompleteAllQuery(isApprove, entryIds, planTime,
					employee.getEnterpriseCode(), Integer.parseInt(start),
					Integer.parseInt(limit));
		else
			pg = mRemote.getPlanCompleteAllQuery(isApprove, entryIds, planTime,
					employee.getEnterpriseCode());
		if (pg.getTotalCount() > 0) {
			write(JSONUtil.serialize(pg));
		} else {
			write("{totalCount : 0,list :[]}");
		}
	}

	public void setDeptMainRead() {
		String deptMainId = request.getParameter("depMainId");
		BpJPlanJobDepMain model = mRemote.findById(Long.parseLong(deptMainId));
		model.setFinishSignStatus(13l);
		mRemote.update(model);
		write("{success:true,msg:'文件已上报！'}");
	}

	// 取出当前登陆人的信息bt
	public void getSessionEmployee() throws JSONException {
		String dept = mRemote.getManagerDept(employee.getDeptId());
		String outStr = dept + "," + employee.getWorkerCode() + ","
				+ employee.getWorkerName();
	
		write(JSONUtil.serialize(outStr));
	}

	/**
	 * add by sychen 20100613
	 * 月度计划完成情况填写 保存方法
	 * @throws Exception
	 */
	public void saveDeptPlanComplete() throws Exception {
		try {
			String planTime = request.getParameter("planTime");
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<BpJPlanJobDepDetail> addList = new ArrayList<BpJPlanJobDepDetail>();
			List<BpJPlanJobDepDetail> updateList = new ArrayList<BpJPlanJobDepDetail>();

			List<Map> list = (List<Map>) obj;
			String depMainId = null;
			for (Map data : list) {

				String jobContent = null;
				String jobId = null;
				String ifComplete = null;
				String completeData = null;
				String completeDesc = null;
				String orderBy=null;
				
				BpJPlanJobDepMain baseInfo = new BpJPlanJobDepMain();
				if(data.get("depMainId").toString()==null||data.get("depMainId").toString().equals("0"))
				{
					String mainId = mRemote.getPlanCompleteNewMainId(planTime,employee.getWorkerCode(),employee.getEnterpriseCode());
					if (mainId.equals("")||mainId==null) {
						
						String flag ="";   
						String workflowType = ""; 
						BpJPlanJobDepMain model = new BpJPlanJobDepMain();
						model.setEditBy(employee.getWorkerCode());
						model.setEditDate(new Date());
						model.setEditDepcode(employee.getDeptCode());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
						Date planDate = sdf.parse(planTime);
						model.setPlanTime(planDate);
						
						model.setFinishEditBy(employee.getWorkerCode());
						model.setFinishEditDate(new Date());
						model.setFinishSignStatus(0L);
						model.setEnterpriseCode(employee.getEnterpriseCode());

						model.setFinishIfRead("N");
						baseInfo = mRemote.save(model,employee.getDeptCode(),flag,workflowType,
								employee.getWorkerCode(),employee.getWorkerCode());
						
						depMainId = baseInfo.getDepMainId() == null ? data.get(
						"depMainId").toString() : baseInfo.getDepMainId()
						.toString();
					}
					else{
						BpJPlanJobDepMain model = mRemote.findById(Long.parseLong(mainId));
						model.setEditBy(employee.getWorkerCode());
						model.setEditDate(new Date());
						model.setFinishEditBy(employee.getWorkerCode());
						model.setFinishEditDate(new Date());
						model.setFinishSignStatus(0L);
						mRemote.update(model);
//						updateBpJPlanJobDepMain(mainId);
						depMainId=mainId;
					}
				}
				else {
					BpJPlanJobDepMain model = mRemote.findById(Long.parseLong(data.get("depMainId").toString()));
					model.setFinishEditBy(employee.getWorkerCode());
					model.setFinishEditDate(new Date());
					model.setFinishSignStatus(0L);
					mRemote.update(model);
				}
				
				if (data.get("ifComplete") != null) {
					ifComplete = data.get("ifComplete").toString();
				}
				if (data.get("completeData") != null) {
					completeData = data.get("completeData").toString();
				}
				if (data.get("completeDesc") != null) {
					completeDesc = data.get("completeDesc").toString();
				}
				if (data.get("jobContent") != null) {
					jobContent = data.get("jobContent").toString();
				}

				if (data.get("jobId") != null) {
					jobId = data.get("jobId").toString();
				}
				if (data.get("orderBy") != null) {
					orderBy = data.get("orderBy").toString();
				}

				BpJPlanJobDepDetail model = new BpJPlanJobDepDetail();

				 
				if (jobId == null) {
					model.setDepMainId(Long.parseLong(depMainId));
					model.setJobContent(jobContent);
					model.setCompleteData(completeData);
					model.setCompleteDesc(completeDesc);
					model.setIfComplete(ifComplete);
					model.setChargeBy(employee.getWorkerCode());
					model.setOrderBy(orderBy);
					model.setEnterpriseCode(employee.getEnterpriseCode());

					addList.add(model);

				} else {
					model = dRemote.findById(Long.parseLong(jobId));

					model.setJobContent(jobContent);
					model.setCompleteDesc(completeDesc);
					model.setIfComplete(ifComplete);
					model.setCompleteData(completeData);
					model.setChargeBy(employee.getWorkerCode());
					model.setOrderBy(orderBy);

					updateList.add(model);
				}
			}

			if (addList.size() > 0)
				dRemote.save(addList);

			if (updateList.size() > 0)

				dRemote.update(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))
				dRemote.delete(deleteIds);
			
			 write("{success:true,msg:'保存成功！',obj:" + depMainId + "}");

		} catch (Exception exc) {
			write("{success:false,msg:'操作失败'}");
			throw exc;
		}
	}
	
	public String getEventIdentify() {
		return eventIdentify;
	}

	public void setEventIdentify(String eventIdentify) {
		this.eventIdentify = eventIdentify;
	}
}