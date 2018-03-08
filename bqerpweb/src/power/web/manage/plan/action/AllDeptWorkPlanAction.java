package power.web.manage.plan.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.manage.plan.BpJPlanJobDepDetail;
import power.ejb.manage.plan.BpJPlanJobDepDetailFacadeRemote;
import power.ejb.manage.plan.BpJPlanJobDepMainFacadeRemote;

import power.ejb.manage.plan.BpJPlanJobMain;
import power.ejb.manage.plan.BpJPlanJobMainFacadeRemote;

import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class AllDeptWorkPlanAction extends AbstractAction {

	private BpJPlanJobMainFacadeRemote mRemote;
	protected BpJPlanJobDepDetailFacadeRemote dRemote;
	protected BpJPlanJobDepMainFacadeRemote depMain;
	
	private String editBy;
	private Date editDate;

	private int start;
	private int limit;
	private String prjNo;
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
	public AllDeptWorkPlanAction() {

		mRemote = (BpJPlanJobMainFacadeRemote) factory
				.getFacadeRemote("BpJPlanJobMainFacade");
		dRemote = (BpJPlanJobDepDetailFacadeRemote) factory
				.getFacadeRemote("BpJPlanJobDepDetailFacade");
		depMain = (BpJPlanJobDepMainFacadeRemote) factory
		.getFacadeRemote("BpJPlanJobDepMainFacade");
	}

	public void workPlanGatherCommit() {
		try {
			BpJPlanJobMain obj = mRemote.reportTo(prjNo, workflowType, employee
					.getWorkerCode(), actionId, approveText, nextRoles,
					employee.getEnterpriseCode());

			write("{success:true,msg:'操作成功！',obj:" + JSONUtil.serialize(obj)
					+ "}");
		} catch (Exception e) {
			write("{failure:true,msg:'" + e.getMessage() + "'}");
		}
	}

	public void workPlanGatherApprove() {
		try {
			BpJPlanJobMain obj = mRemote.approveStep(prjNo, workflowType,
					employee.getWorkerCode(), actionId, eventIdentify,
					approveText, nextRoles, stepId, employee
							.getEnterpriseCode());

			write("{success:true,msg:'操作成功！',obj:" + JSONUtil.serialize(obj)
					+ "}");
		} catch (Exception e) {
			write("{failure:true,msg:'" + e.getMessage() + "'}");
		}
	}

	public void getBpJPlanJobMain() throws JSONException, ParseException {
		String planTime = request.getParameter("planTime");
		String flag=request.getParameter("flag");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//		Date planDate = sdf.parse(planTime);
		String status = mRemote.findNoReadInfo(planTime,flag);
		write(status);
		

	}

	public BpJPlanJobMain saveBpJPlanJobMain() throws ParseException {
		String finish = request.getParameter("finish");
		String planTime = request.getParameter("planTime");
		BpJPlanJobMain model = new BpJPlanJobMain();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date planDate = sdf.parse(planTime);
		if (finish != null && finish.equals("finish")) {
			model = mRemote.findById(planDate);
			model.setFinishEditDate(planDate);
			model.setFinishEditBy(employee.getWorkerCode());
			if (model.getFinishSignStatus() == null)
				model.setFinishSignStatus(0l);

			BpJPlanJobMain baseInfo = mRemote.update(model);
			return baseInfo;
		} else {
			if (mRemote.findById(planDate) != null) {
				model = mRemote.findById(planDate);
				model.setEditDate(new Date());
				model.setEditBy(employee.getWorkerCode());

				BpJPlanJobMain baseInfo = mRemote.update(model);

				return baseInfo;
			} else {
				model.setEditBy(employee.getWorkerCode());
				model.setEditDate(new Date());

				model.setPlanTime(planDate);
				// 新增时工作流状态设置为0L(未上报)
				model.setSignStatus(0L);

				model.setEnterpriseCode(employee.getEnterpriseCode());

				BpJPlanJobMain baseInfo = mRemote.save(model);

				return baseInfo;
			}
		}
	}

	public void saveBpJPlanJob() throws Exception {
		try {
			saveBpJPlanJobMain();
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

				depMainId = data.get("depMainId").toString();

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

				BpJPlanJobDepDetail model = new BpJPlanJobDepDetail();

				// 增加
				if (jobId == null) {

					model.setDepMainId(Long.parseLong(depMainId));
					model.setJobContent(jobContent);
					model.setCompleteData(completeData);
					model.setCompleteDesc(completeDesc);
					model.setIfComplete(ifComplete);
					model.setEnterpriseCode(employee.getEnterpriseCode());

					addList.add(model);

				} else {
					model = dRemote.findById(Long.parseLong(jobId));
					model.setJobContent(jobContent);
					model.setCompleteData(completeData);
					model.setCompleteDesc(completeDesc);
					model.setIfComplete(ifComplete);

					updateList.add(model);

				}

			}

			if (addList.size() > 0)
				dRemote.save(addList);

			if (updateList.size() > 0)

				dRemote.update(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))
				dRemote.delete(deleteIds);
			System.out.print("{success: true,msg:'保存成功！',obj:" + depMainId
					+ "}");
			write("{success:true,msg:'保存成功！',obj:" + depMainId + "}");

		} catch (Exception exc) {
			write("{success:false,msg:'操作失败'}");
			throw exc;

		}
	}

	public void updateMainreportTo()
	{
		String depMainId = request.getParameter("depMainId");
		depMain.updateMainreportTo(Long.parseLong(depMainId));
		write("{success:true,msg:'操作成功！'");
	}
	
	/**部门月度计划整理上报 已读 方法
	 *  add by sychen 20100408
	 */
	public void updateIfRead()
	{
		String depMainId = request.getParameter("depMainId");
		depMain.updateMainIfReadRecord(Long.parseLong(depMainId));
		write("{success:true,msg:'操作成功！'");
	}
	
	/**部门月度计划完成情况整理上报 已读 方法
	 *  add by sychen 20100409
	 */
	public void updateFinishIfRead()
	{
		String depMainId = request.getParameter("depMainId");
		depMain.updateMainFinishIfReadRecord(Long.parseLong(depMainId));
		write("{success:true,msg:'操作成功！'");
	}
	
	
	// 计划完成情况整理上报
	public void allDeptWorkFinishReport() throws NumberFormatException,
			ParseException {
		String planTime = request.getParameter("planTime");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String workflowType = request.getParameter("workflowType");
		String actionId = request.getParameter("actionId");
		mRemote.allDeptWorkFinishReport(planTime, Long.parseLong(actionId),
				employee.getWorkerCode(), approveText, nextRoles, workflowType);
		write("{success:true,msg:'上报成功！'}");
	}

	// 计划完成情况整理审批
	public void allDeptWorkFinishApprove() throws NumberFormatException,
			ParseException {
		String planTime = request.getParameter("planTime");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		mRemote.allDeptWorkFinishApprove(planTime, Long.parseLong(actionId),
				Long.parseLong(entryId), workerCode, approveText, nextRoles);
		write("{success:true,msg:'审批成功！'}");
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

	public String getNextRoles() {
		return nextRoles;
	}

	public void setNextRoles(String nextRoles) {
		this.nextRoles = nextRoles;
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
}