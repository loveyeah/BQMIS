package power.web.manage.plan.action;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.plan.BpJPlanForeword;
import power.ejb.manage.plan.BpJPlanGuideDetail;
import power.ejb.manage.plan.BpJPlanGuideDetailFacadeRemote;
import power.ejb.manage.plan.BpJPlanGuideMain;
import power.ejb.manage.plan.BpJPlanGuideMainFacadeRemote;
import power.ejb.manage.plan.BpJPlanJobDepDetail;
import power.ejb.manage.plan.BpJPlanJobDepDetailFacadeRemote;
import power.ejb.manage.plan.BpJPlanJobDepMain;
import power.ejb.manage.plan.BpJPlanJobDepMainFacadeRemote;
import power.ejb.manage.plan.BpJPlanJobMain;
import power.ejb.manage.plan.form.BpJPlanGuideMainForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class GuidePlanAction extends AbstractAction {
	BpJPlanGuideDetailFacadeRemote  dRemote;
	BpJPlanGuideMainFacadeRemote mRemote;
	private String editBy;
	private Date editDate;
	private int start;
	private int limit;
	private BpJPlanGuideDetail baseInfo;
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
	public GuidePlanAction() {
		dRemote = (BpJPlanGuideDetailFacadeRemote) factory
				.getFacadeRemote("BpJPlanGuideDetailFacade");
		mRemote = (BpJPlanGuideMainFacadeRemote) factory
				.getFacadeRemote("BpJPlanGuideMainFacade");
	}
	
	/**
	 * 
	 * 月度工作计划上报
	 */
	public void guidePlanCommit() {
		try {
			BpJPlanGuideMain obj = mRemote.reportTo(prjNo, workflowType, employee
					.getWorkerCode(), actionId, approveText, nextRoles,
					employee.getEnterpriseCode());
			write("{success:true,msg:'上报成功！',obj:" + JSONUtil.serialize(obj) + "}");
		} catch (Exception e) {
			write("{failure:true,msg:'" + e.getMessage() + "'}");
		}
	}
	/**
	 * 
	 * 工作计划审批
	 */
	public void guidePlanApprove() {
		try {
			BpJPlanGuideMain obj = mRemote.approveStep(prjNo, workflowType,
					employee.getWorkerCode(), actionId, eventIdentify,
					approveText, nextRoles, stepId, employee
							.getEnterpriseCode());
			write("{success:true,msg:'审批成功！',obj:" + JSONUtil.serialize(obj)
					+ "}");
		} catch (Exception e) {
			write("{failure:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	/**
	 * 
	 * 根据计划月度获得工作计划详细列表
	 * @throws JSONException
	 */
	public void getBpJPlanGuideDetail() throws JSONException{
		String planTime = request.getParameter("planTime");
		PageObject list = dRemote.getPlanGuideDetail(planTime,
				 employee.getEnterpriseCode(), start, limit);
		//System.out.print(JSONUtil.serialize(list));
		write(JSONUtil.serialize(list));
	}
	
	/**
	 * 
	 * 根据计划月度获得该月度基本信息概况
	 * @throws IOException
	 * @throws JSONException
	 */
	public void getBpJPlanGuideMain() throws IOException, JSONException{
		String planTime = request.getParameter("planTime");
		BpJPlanGuideMainForm form = mRemote.getBpJPlanGuideMain(planTime, employee.getEnterpriseCode());
		//System.out.println(JSONUtil.serialize(form));
		write(JSONUtil.serialize(form));
	}
	
	/**
	 * 生成一条工作计划主记录
	 * 
	 * @return
	 * @throws ParseException
	 */
	public BpJPlanGuideMain saveBpJPlanGuideMain() throws ParseException{
		String planTime = request.getParameter("monthTime");
		SimpleDateFormat formt = new SimpleDateFormat("yyyy-MM-dd");
		Date editDate = formt.parse(request.getParameter("editDate"));
		BpJPlanGuideMain model = new BpJPlanGuideMain();
		model.setEditBy(employee.getWorkerCode());
		model.setEditDate(editDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date planDate = sdf.parse(planTime);
		model.setPlanTime(planDate);
		// 新增时工作流状态设置为0L(未上报)
		model.setSignStatus(0L);
		model.setPlanStatus(0L);
		model.setEnterpriseCode(employee.getEnterpriseCode());
		BpJPlanGuideMain baseInfo = mRemote.save(model);
		return baseInfo;
		
	}
	
	/**
	 * 根据计划月度更新一条主记录
	 * 
	 * @param planTime
	 * @return
	 * @throws ParseException
	 */
	public BpJPlanGuideMain updateBpJPlanGuideMain(String planTime) throws ParseException{
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM");
		BpJPlanGuideMain entity = mRemote.findById(formatter.parse(planTime));
		if (entity.getSignStatus() == 3){
			entity.setSignStatus(0l);
		}
		entity.setEditBy(employee.getWorkerCode());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date editDate = sdf.parse(sdf.format(new Date()));
		entity.setEditDate(editDate);
		BpJPlanGuideMain baseInfo = mRemote.update(entity);
		return baseInfo;
	}
	
	/**
	 * 
	 * 更新一条计划月度明细记录
	 */
	public void updateBpJPlanGuideDetail(){
		BpJPlanGuideDetail entity = dRemote.findById(baseInfo.getGuideId());
		entity.setCheckDesc(baseInfo.getCheckDesc());
		entity.setCheckStatus(baseInfo.getCheckStatus());
		entity.setCompleteDesc(baseInfo.getCompleteDesc());
		entity.setIfCheck(baseInfo.getIfCheck());
		entity.setIfComplete(baseInfo.getIfComplete());
		entity.setTargetDepcode(baseInfo.getTargetDepcode());
		//java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM");
		//String planTime = formatter.format(dRemote.update(entity).getPlanTime());
		dRemote.update(entity);
		
	}
	
	/**
	 * 
	 * 批量保存修改记录
	 * 
	 * @throws Exception
	 */
	public void saveBpJPlanGuide() throws Exception {
		try {
			String planTime = request.getParameter("planTime");

			BpJPlanGuideMain baseInfo = new BpJPlanGuideMain();
			if (planTime.equals("") || planTime == null) {

				baseInfo = saveBpJPlanGuideMain();

			} else {
				baseInfo = updateBpJPlanGuideMain(planTime);
			}
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);
			//System.out.println("**********");
			//System.out.println(JSONUtil.deserialize(str));
			List<BpJPlanGuideDetail> addList = new ArrayList<BpJPlanGuideDetail>();
			List<BpJPlanGuideDetail> updateList = new ArrayList<BpJPlanGuideDetail>();

			List<Map> list = (List<Map>) obj;
			Object time = null;
			for (Map data : list) {

				String guideContent = null;
				String guideId = null;
				String referDepcode = null;
				String mainDepcode = null;
				String taskDepcode = null;
				time = baseInfo.getPlanTime() == null ? data.get(
						"planTime").toString() : baseInfo.getPlanTime();

				if (data.get("baseInfo.taskDepcode") != null) {
					taskDepcode = data.get("baseInfo.taskDepcode").toString();
				}
				if (data.get("baseInfo.mainDepcode") != null) {
					mainDepcode = data.get("baseInfo.mainDepcode").toString();
				}
				if (data.get("baseInfo.guideContent") != null) {
					guideContent = data.get("baseInfo.guideContent").toString();
				}

				if (data.get("baseInfo.guideId") != null) {
					guideId = data.get("baseInfo.guideId").toString();
				}
				if (data.get("baseInfo.referDepcode") != null) {
					referDepcode = data.get("baseInfo.referDepcode").toString();
				}

				BpJPlanGuideDetail model = new BpJPlanGuideDetail();

				// 增加
				if (guideId == null) {

					model.setPlanTime((Date)time);
					model.setGuideContent(guideContent);
					model.setReferDepcode(referDepcode);
					model.setMainDepcode(mainDepcode);
					model.setTaskDepcode(taskDepcode);
					model.setEnterpriseCode(employee.getEnterpriseCode());

					addList.add(model);

				} else {
					model = dRemote.findById(Long.parseLong(guideId));

					model.setGuideContent(guideContent);
					model.setReferDepcode(referDepcode);
					model.setMainDepcode(mainDepcode);
					model.setTaskDepcode(taskDepcode);

					updateList.add(model);

				}

			}

			if (addList.size() > 0)
				dRemote.save(addList);

			if (updateList.size() > 0)

				dRemote.update(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))
				dRemote.delete(deleteIds);
			//System.out.print("{success: true,msg:'保存成功！' }");
			write("{success:true,msg:'保存成功！'}");

		} catch (Exception exc) {
			write("{success:false,msg:'操作失败'}");
			throw exc;

		}
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
	public BpJPlanGuideDetail getBaseInfo() {
		return baseInfo;
	}
	public void setBaseInfo(BpJPlanGuideDetail baseInfo) {
		this.baseInfo = baseInfo;
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
