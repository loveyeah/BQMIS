package power.web.manage.plan.action;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.web.comm.AbstractAction;
import power.web.comm.PostMessage;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;
import power.ejb.manage.plan.BpJPlanJobCompleteManager;
import power.ejb.manage.plan.BpJPlanJobCompleteDetail;
import power.ejb.manage.plan.BpJPlanJobCompleteMain;

@SuppressWarnings("serial")
public class BpJPlanCompleteAction extends AbstractAction {
	protected BpJPlanJobCompleteManager cRemote;


	private String workflowType;
	private String actionId;
	private String approveText;
	private String nextRoles;
	private String eventIdentify;
	private String stepId;
	
	
	/**
	 * 
	 * 构造方法
	 * 
	 */
	@SuppressWarnings("unchecked")
	public BpJPlanCompleteAction() {
		cRemote = (BpJPlanJobCompleteManager) factory
				.getFacadeRemote("BpJPlanJobCompleteManagerImpl");
	}
	
	@SuppressWarnings("unchecked")
	public void getPlanJobCompleteList() throws Exception {
		PageObject pg = null;
		String planTime = request.getParameter("planTime");
		pg = cRemote.getPlanJobCompleteList(planTime,employee.getWorkerCode(),employee.getEnterpriseCode(),employee.getDeptCode());
	
		write(JSONUtil.serialize(pg));
	}
	
	public BpJPlanJobCompleteMain savePlanJobCompleteMain() throws ParseException {

		String planTime = request.getParameter("planTime");
		BpJPlanJobCompleteMain model = new BpJPlanJobCompleteMain();
		
		model.setEditDepcode(employee.getDeptCode());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date planDate = sdf.parse(planTime);
		model.setPlanTime(planDate);
		
		model.setFinishEditBy(employee.getWorkerCode());
		model.setFinishEditDate(new Date());
		model.setFinishSignStatus(0L);
		model.setEnterpriseCode(employee.getEnterpriseCode());

		model.setFinishIfRead("N");
		 BpJPlanJobCompleteMain baseInfo = cRemote.save(model,employee.getDeptCode(),employee.getWorkerCode());
		return baseInfo;

	}
	
	public void updatePlanJobCompleteMain(String mainId) throws ParseException {

		BpJPlanJobCompleteMain model = cRemote.findByMainId(Long.parseLong(mainId));
		model.setFinishEditBy(employee.getWorkerCode());
		model.setFinishEditDate(new Date());
		cRemote.update(model);
	}
	
	@SuppressWarnings("unchecked")
	public void savePlanJobComplete() throws Exception {
		try {
			String planTime = request.getParameter("planTime");
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			
			BpJPlanJobCompleteMain baseInfo = new BpJPlanJobCompleteMain();
			String mainId = cRemote.getPlanCompleteNewMainId(planTime,employee.getWorkerCode(),employee.getEnterpriseCode());
			if (mainId.equals("")||mainId==null) {
				
				baseInfo = savePlanJobCompleteMain();
			}
			else{
				updatePlanJobCompleteMain(mainId);
			}
			
			Object obj = JSONUtil.deserialize(str);

			List<BpJPlanJobCompleteDetail> addList = new ArrayList<BpJPlanJobCompleteDetail>();
			List<BpJPlanJobCompleteDetail> updateList = new ArrayList<BpJPlanJobCompleteDetail>();

			List<Map> list = (List<Map>) obj;
			String deptMainId = null;
			for (Map data : list) {

				String jobContent = null;
				String jobId = null;
				String ifComplete = null;
				String completeData = null;
				String completeDesc = null;
				String orderBy=null;
				String linkJobId=null;
				
				
				deptMainId = baseInfo.getDepMainId() == null ? mainId : baseInfo.getDepMainId()
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
				if (data.get("orderBy") != null) {
					orderBy = data.get("orderBy").toString();
				}
				if (data.get("linkJobId") != null) {
					linkJobId = data.get("linkJobId").toString();
				}

				BpJPlanJobCompleteDetail model = new BpJPlanJobCompleteDetail();

				 
				if (jobId == null) {
					model.setDepMainId(Long.parseLong(deptMainId));
					model.setJobContent(jobContent);
					model.setCompleteData(completeData);
					model.setCompleteDesc(completeDesc);
					model.setIfComplete(ifComplete);
					model.setChargeBy(employee.getWorkerCode());
					model.setOrderBy(orderBy);
					model.setEnterpriseCode(employee.getEnterpriseCode());
					if(linkJobId!=null && !linkJobId.equals(""))
						model.setLinkJobId(Long.parseLong(linkJobId));
					addList.add(model);

				} else {
					model = cRemote.findByDetailId(Long.parseLong(jobId));

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
				cRemote.saveDetail(addList);

			if (updateList.size() > 0)

				cRemote.updateDetail(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))
				cRemote.deleteDetail(deleteIds);
			
			 write("{success:true,msg:'保存成功！',obj:" + deptMainId + "}");

		} catch (Exception exc) {
			write("{success:false,msg:'操作失败'}");
			throw exc;
		}
	}
	
	public void reportPlanJobComplete() {

		String deptMainId = request.getParameter("deptMainId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");

		String nextRolePs = request.getParameter("nextRolePs");
		String workflowType = request.getParameter("workflowType");
		String actionId = request.getParameter("actionId");

		cRemote.reportPlanJobComplete(Long.parseLong(deptMainId), Long
				.parseLong(actionId), employee.getWorkerCode(), approveText,
				nextRoles, nextRolePs, workflowType);
		// --短信通知------------
		PostMessage postMsg = new PostMessage();
		
		BpJPlanJobCompleteMain model = cRemote.findByMainId(Long
				.parseLong(deptMainId));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		String msg = sdf.format(model.getPlanTime())
				+ "份月度计划完成情况等待您的审批，请您及时处理。";
		
		if(nextRoles!=null&&!nextRoles.equals(""))
        {
        	
        	postMsg.sendMsg(nextRoles, msg);
        }
        if(nextRolePs!=null&&!nextRolePs.equals(""))
        {
        	
        	postMsg.sendMsgByWorker(nextRolePs, msg);
        }

		write("{success:true,msg:'上报成功！'}");

	}	
	
	 public void getPlanCompleteStatus()
	  {
		String planTime = request.getParameter("planTime");
		String str = "";
		str = cRemote.getPlanCompleteStatus(planTime, employee.getWorkerCode(),employee.getEnterpriseCode());

		write(str);
	}
	 
		public void getPlanJobCompleteApproveList() throws JSONException {
			
			String start = request.getParameter("start");
			String limit = request.getParameter("limit");
			String flag = request.getParameter("flag");
			String planTime = request.getParameter("planTime");
			String txtIfComplete = request.getParameter("txtIfComplete");
			String entryIds = "";
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();

			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqDeptPlanFinishApprove" }, employee.getWorkerCode());
			PageObject pg = new PageObject();
			if (start != null && limit != null)
				pg = cRemote.getPlanJobCompleteApproveList(flag, planTime,entryIds,employee.getEnterpriseCode(), txtIfComplete,
					Integer.parseInt(start), Integer.parseInt(limit));
			else
				pg = cRemote.getPlanJobCompleteApproveList(flag, planTime,entryIds,employee.getEnterpriseCode(), txtIfComplete);

			if (pg.getTotalCount() > 0) {
				write(JSONUtil.serialize(pg));
			} else {
				write("{totalCount : 0,list :[]}");
			}
		}
	 
		@SuppressWarnings("unchecked")
		public void getPlanJobCompleteInfo() throws JSONException {
			String planTime = request.getParameter("planTime");
			String entryIds = "";
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();

			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqDeptPlanFinishApprove" }, employee.getWorkerCode());
			List list = null;
			list = cRemote.getPlanJobCompleteInfo(planTime, entryIds,employee.getEnterpriseCode());
			write(JSONUtil.serialize(list));
		}
	
		public void planJobCompleteApprove() {
			try {
				String mainId = request.getParameter("deptMainId");
			     String [] mainIds= mainId.split(",");
					for(int i=0;i<mainIds.length;i++)
					{
						if(!mainIds[i].equals(""))
						{
							// ------------短信通知------------
							PostMessage postMsg = new PostMessage();
							String thisRoles = nextRoles;
							if (!"TH".equals(eventIdentify) && !"133".equals(actionId)
									&& !"113".equals(actionId)
									&& !"123".equals(actionId)
									&& !"83".equals(actionId)) {
								if (nextRoles == null || nextRoles.equals("")) {
									BpJPlanJobCompleteMain entity = cRemote.findByMainId(Long.parseLong(mainIds[i]));
									thisRoles = postMsg.getNextSetpRoles(entity
											.getFinishWorkFlowNo().toString(), actionId);
								}
							}
							
							String deptMainId = mainIds[i];
							BpJPlanJobCompleteMain obj = cRemote.planJobCompleteApprove(
									deptMainId, workflowType, employee.getWorkerCode(),
									actionId, eventIdentify, approveText, nextRoles,
									stepId, employee.getEnterpriseCode());
							// ------------短信通知------------
							if (!"TH".equals(eventIdentify) && !"133".equals(actionId)
									&& !"113".equals(actionId)
									&& !"123".equals(actionId)
									&& !"83".equals(actionId)) {
								if (thisRoles != null && !thisRoles.equals("")) {
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
									String msg = sdf.format(obj.getPlanTime())+"份月度计划完成情况等待您的审批，请您及时处理。";
									postMsg.sendMsg(thisRoles, msg);
								}
							}
							
							write("{success:true,msg:'操作成功！',obj:"
									+ JSONUtil.serialize(obj) + "}");
						}
					}
	
			} catch (Exception e) {
				write("{failure:true,msg:'" + e.getMessage() + "'}");
			}
		}
		
		public void getPlanJobCompleteMainList() throws JSONException, ParseException {
			String planTime = request.getParameter("planTime");
			String flag = request.getParameter("flag");
			String start = request.getParameter("start");
			String limit = request.getParameter("limit");
			
			PageObject pg = new PageObject();
			if (start != null && limit != null)
				pg = cRemote.getPlanJobCompleteMainList(flag, planTime,employee.getEnterpriseCode(), 
						Integer.parseInt(start), Integer.parseInt(limit));
			else
				pg = cRemote.getPlanJobCompleteMainList(flag, planTime,employee.getEnterpriseCode());

			write(JSONUtil.serialize(pg));
		}
		
		public void getPlanJobCompleteDetailList() throws JSONException, ParseException {
			String deptMainId = request.getParameter("deptMainId");
			String start = request.getParameter("start");
			String limit = request.getParameter("limit");
			
			PageObject pg = new PageObject();
			if (start != null && limit != null)
				pg = cRemote.getPlanJobCompleteDetailList(deptMainId,employee.getEnterpriseCode(), 
						Integer.parseInt(start), Integer.parseInt(limit));
			else
				pg = cRemote.getPlanJobCompleteDetailList(deptMainId,employee.getEnterpriseCode());

			write(JSONUtil.serialize(pg));
		}
		
		@SuppressWarnings("unchecked")
		public void savePlanJobCompleteGather() throws Exception {
			try {
//				savePlanJobCompleteMain();
				String deptMainId=request.getParameter("deptMainId");
				String str = request.getParameter("isUpdate");
				String deleteIds = request.getParameter("isDelete");
				Object obj = JSONUtil.deserialize(str);

				List<BpJPlanJobCompleteDetail> addList = new ArrayList<BpJPlanJobCompleteDetail>();
				List<BpJPlanJobCompleteDetail> updateList = new ArrayList<BpJPlanJobCompleteDetail>();

				List<Map> list = (List<Map>) obj;
//				String depMainId = null;
				for (Map data : list) {

					String jobContent = null;
					String jobId = null;
					String ifComplete = null;
					String completeData = null;
					String completeDesc = null;
					String orderBy = null;

//					depMainId = data.get("deptMainId").toString();

					if (data.get("ifComplete") != null) {
						ifComplete = data.get("ifComplete").toString();
					}
					if (data.get("completeDesc") != null) {
						completeDesc = data.get("completeDesc").toString();
					}
					if (data.get("jobContent") != null) {
						jobContent = data.get("jobContent").toString();
					}
					if (data.get("completeData") != null) {
						completeData = data.get("completeData").toString();
					}
					if (data.get("jobId") != null) {
						jobId = data.get("jobId").toString();
					}
					if (data.get("orderBy") != null) {
						orderBy = data.get("orderBy").toString();
					}

					BpJPlanJobCompleteDetail model = new BpJPlanJobCompleteDetail();

					// 增加
					if (jobId == null) {

						model.setDepMainId(Long.parseLong(deptMainId));
						model.setJobContent(jobContent);
						model.setCompleteData(completeData);
						model.setCompleteDesc(completeDesc);
						model.setIfComplete(ifComplete);
						model.setOrderBy(orderBy);
						model.setEnterpriseCode(employee.getEnterpriseCode());

						addList.add(model);

					} else {
						model = cRemote.findByDetailId(Long.parseLong(jobId));
						model.setJobContent(jobContent);
						model.setCompleteData(completeData);
						model.setCompleteDesc(completeDesc);
						model.setIfComplete(ifComplete);
						model.setOrderBy(orderBy);

						updateList.add(model);

					}

				}

				if (addList.size() > 0)
					cRemote.saveDetail(addList);

				if (updateList.size() > 0)

					cRemote.updateDetail(updateList);

				if (deleteIds != null && !deleteIds.trim().equals(""))
					cRemote.deleteDetail(deleteIds);
				
				write("{success:true,msg:'保存成功！',obj:" + deptMainId + "}");

			} catch (Exception exc) {
				write("{success:false,msg:'操作失败'}");
				throw exc;

			}
		}
		
		public void modifyFinishIfRead()
		{
			String deptMainId = request.getParameter("deptMainId");
			cRemote.modifyFinishIfRead(Long.parseLong(deptMainId));
			write("{success:true,msg:'操作成功！'");
		}
		
		public void getPlanJobCompleteQuestList() throws JSONException {
			
			String start = request.getParameter("start");
			String limit = request.getParameter("limit");
			String planTime = request.getParameter("planTime");
			String editDepcode = request.getParameter("editDepcode");
			PageObject pg = new PageObject();
			
			if (start != null && limit != null)
				pg = cRemote.getPlanJobCompleteQuestList( planTime,employee.getEnterpriseCode(),editDepcode, 
					Integer.parseInt(start), Integer.parseInt(limit));
			else
				pg = cRemote.getPlanJobCompleteQuestList( planTime,employee.getEnterpriseCode(),editDepcode);

			if (pg.getTotalCount() > 0) {
				write(JSONUtil.serialize(pg));
			} else {
				write("{totalCount : 0,list :[]}");
			}
		}

		
		public void getPlanJobCompleteMaxPlanTime() {
		String entryIds = "";
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();

		entryIds = workflowService.getAvailableWorkflow(
				new String[] { "bqDeptPlanFinishApprove" }, employee.getWorkerCode());
			String str = cRemote.getPlanJobCompleteMaxPlanTime(employee
					.getEnterpriseCode(),entryIds);
			write(str);
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

		public String getNextRoles() {
			return nextRoles;
		}

		public void setNextRoles(String nextRoles) {
			this.nextRoles = nextRoles;
		}

		public String getEventIdentify() {
			return eventIdentify;
		}

		public void setEventIdentify(String eventIdentify) {
			this.eventIdentify = eventIdentify;
		}

		public String getStepId() {
			return stepId;
		}

		public void setStepId(String stepId) {
			this.stepId = stepId;
		}
}