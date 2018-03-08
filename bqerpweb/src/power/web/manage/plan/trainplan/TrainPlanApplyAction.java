package power.web.manage.plan.trainplan;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.plan.BpJPlanJobDepMainFacadeRemote;
import power.ejb.manage.plan.trainplan.BpJTrainingDetail;
import power.ejb.manage.plan.trainplan.BpJTrainingSumApproval;
import power.ejb.manage.plan.trainplan.TrainPlanManager;
import power.ejb.manage.plan.trainplan.form.BpJTrainingSumForm;
import power.ejb.manage.plan.trainplan.form.BpJTrainingTypeForm;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

@SuppressWarnings("serial")
public class TrainPlanApplyAction extends AbstractAction {

	private BpJTrainingDetail trainDetail;
	private TrainPlanManager remoteS;
	protected BpJPlanJobDepMainFacadeRemote mRemote;

	public TrainPlanApplyAction() {
		remoteS = (TrainPlanManager) factory
				.getFacadeRemote("TrainPlanManagerImpl");
		mRemote = (BpJPlanJobDepMainFacadeRemote) factory
		.getFacadeRemote("BpJPlanJobDepMainFacade");
	}

	/**
	 * 新增修改
	 * 
	 * @throws ParseException
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings("unchecked")
	public void addTrainPlanApply() throws JSONException, ParseException,
			CodeRepeatException {
		// 详细类别Id
		String typeId = request.getParameter("typeId");
		String str = request.getParameter("isUpdate");
		String deleteId = request.getParameter("isDelete");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<BpJTrainingDetail> addList = new ArrayList<BpJTrainingDetail>();
		List<BpJTrainingDetail> updateList = new ArrayList<BpJTrainingDetail>();
		// 计划时间
		String planTime = request.getParameter("planDate");
		// 计划部门
		String planDept = request.getParameter("planDeptCode");
		try {
			for (Map data : list) {
				String trainingDetailId = null;
				String trainingName = null;
				String trainingLevel = null;
				String trainingNumber = null;
				String trainingHours = null;
				String chargeBy = null;
				if (data.get("trainDetail.trainingDetailId") != null)
					trainingDetailId = data.get("trainDetail.trainingDetailId")
							.toString();
				if (data.get("trainDetail.trainingName") != null)
					trainingName = data.get("trainDetail.trainingName")
							.toString();
				if (data.get("trainDetail.trainingLevel") != null)
					trainingLevel = data.get("trainDetail.trainingLevel")
							.toString();
				if (data.get("trainDetail.trainingNumber") != null)
					trainingNumber = data.get("trainDetail.trainingNumber")
							.toString();
				if (data.get("trainDetail.trainingHours") != null)
					trainingHours = data.get("trainDetail.trainingHours")
							.toString();
				if (data.get("trainDetail.chargeBy") != null)
					chargeBy = data.get("trainDetail.chargeBy").toString();
				BpJTrainingDetail model = new BpJTrainingDetail();
				if (trainingDetailId == null) {
					model.setTrainingName(trainingName);
					if (trainingLevel != null && !trainingLevel.equals(""))
						model.setTrainingLevel(Long.parseLong(trainingLevel));
					if (trainingNumber != null )
					model.setTrainingNumber(Long.parseLong(trainingNumber));
					else
						model.setTrainingNumber(null);
					if (trainingHours != null && !trainingHours.equals(""))
						model.setTrainingHours(Double.valueOf(trainingHours));
					model.setChargeBy(chargeBy);
					model.setFillBy(employee.getWorkerCode());
					model.setFillDate(new Date());
					if (typeId != null && !typeId.equals(""))
						model.setTrainingTypeId(Long.parseLong(typeId));
					model.setEnterpriseCode(employee.getEnterpriseCode());
					addList.add(model);
				} else {
					model = remoteS.findByDetailId(Long
							.parseLong(trainingDetailId));
					model.setTrainingDetailId(Long.parseLong(trainingDetailId));
					model.setTrainingMainId(model.getTrainingMainId());
					model.setTrainingName(trainingName);
					if (trainingLevel != null && !trainingLevel.equals(""))
						model.setTrainingLevel(Long.parseLong(trainingLevel));
					if (trainingNumber != null )
					model.setTrainingNumber(Long.parseLong(trainingNumber));
					else
						model.setTrainingNumber(null);
					if (trainingHours != null && !trainingHours.equals(""))
						model.setTrainingHours(Double.valueOf(trainingHours));
					model.setChargeBy(chargeBy);
					model.setFillBy(employee.getWorkerCode());
					model.setFillDate(new Date());
					// modified by liuyi 20100429 修改时，类型保持不变。
//					if (typeId != null && !typeId.equals(""))
//						model.setTrainingTypeId(Long.parseLong(typeId));
					model.setEnterpriseCode(employee.getEnterpriseCode());
					updateList.add(model);
				}
				
			}
			if (addList.size() > 0 || updateList.size() > 0
					|| deleteId.length() > 0)
//				remoteS.save(addList, updateList, deleteId, planTime, planDept,
//						employee.getEnterpriseCode());
				// modified by liuyi 20100427 培训计划申请时 需要由填写人过滤
				remoteS.save(addList, updateList, deleteId, planTime, planDept,
						employee.getEnterpriseCode(),employee.getWorkerCode());
			write("{success:true,msg:'操作成功！'}");
		} catch (CodeRepeatException e) {
			String out = "{success:true,msg :'" + e.getMessage() + "'}";
			write(out);
		}
	}


	/**
	 * 删除
	 */
	public void delTrainPlanApply() {
		String trainingDetailId = request.getParameter("id");
		if (trainingDetailId != null && !trainingDetailId.trim().equals("")) {
			if (remoteS.delTrainPlanApplyDetail(trainingDetailId) != null)
				write("{success:true}");
			else
				write("{success:false}");
		}
	}

	/**
	 * 培训类别List
	 */
	public void getAllTypeList() throws JSONException {
		String fuzzy = Constants.ALL_DATA;
		String enterpriseCode = employee.getEnterpriseCode();
		PageObject obj = remoteS.findAllTypeList(fuzzy, enterpriseCode);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	public void getTrainPlanApplyList() throws Exception {
		// 计划时间
		String planTime = request.getParameter("planTime");
		// 计划计划类别
		String planType = request.getParameter("planType");
//		//部门
//		String planDept = request.getParameter("editDepcode");
		// 判断审批
		String approve = request.getParameter("approve");
		
		// add by liuyi 20100427 是否为部门培训计划申请，1是  同一管理部门下可以有多条主记录
		String isApply = request.getParameter("isApply");

		String entryIds = "";
		if (approve != null && approve.equals("approve")) {
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqTrainPlan" }, employee.getWorkerCode());
		}
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		int start = 0, limit = 10000;
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(objstart.toString());
			limit = Integer.parseInt(objlimit.toString());
		}
		String abs = mRemote.getManagerDept(employee.getDeptId());
		String[] arr = abs.split(",");
		String param = "";
		if(arr != null && arr.length > 0)
			param = arr[0];
		obj = remoteS.findTrainPlanApplyList(approve, entryIds, planType,
				planTime,param , employee.getEnterpriseCode(),isApply,employee.getWorkerCode(),
				start, limit);
		if (obj != null) {
			write(JSONUtil.serialize(obj));
		} else {
			write("{totalCount : 0,list :[]}");
		}
		//		
		// System.out.println(JSONUtil.serialize(obj));
	}

	public void getPlanMainObj() throws Exception {

		String planDate = request.getParameter("planDate");
//		String deptCode = employee.getDeptCode();
		// modified by liuyi 20100427
		String abs = mRemote.getManagerDept(employee.getDeptId());
		String[] arr = abs.split(",");
		String deptCode = "";
		if(arr != null && arr.length > 0)
			deptCode = arr[0];
		BpJTrainingTypeForm obj = remoteS
				.getBpTrainMainList(planDate, deptCode);

		// System.out.println(JSONUtil.serialize(obj));

		List lists = null;
		
		String returnStr = "{model:" + JSONUtil.serialize(obj)+
//		+ ",list:"
//				+ JSONUtil.serialize(lists) + 
				"}";

		write(returnStr);
	}

	// write(JSONUtil.serialize(obj));

	// 汇总查询
	public void getPlanTrainGather() throws JSONException {
		String planTime = request.getParameter("planTime");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		int start = 0, limit = 10000;
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(objstart.toString());
			limit = Integer.parseInt(objlimit.toString());
		}
		obj = remoteS.BpPlanGatherList(planTime, employee.getEnterpriseCode(),
				start, limit);
		if (obj != null) {
			write(JSONUtil.serialize(obj));
		} else {
			write("{totalCount : 0,list :[]}");
		}
	}

	// 汇总按钮
	public void trainPlanGather() {
		String mainIds = request.getParameter("mainIds");
		String planTime = request.getParameter("planTime");
		remoteS.trainPlanGather(mainIds, planTime, employee.getWorkerCode(),
				employee.getEnterpriseCode());

	}

	// 汇总后查询
	public void trainPlanGatherAfter() throws JSONException {
		String planTime = request.getParameter("planTime");
		// 审批判断
		String approve = request.getParameter("approve");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		int start = 0, limit = 10000;
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(objstart.toString());
			limit = Integer.parseInt(objlimit.toString());
		}
		String entryIds = "";
		if (approve != null && approve.equals("approve")) {
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bpTrainPlanGather" }, employee
							.getWorkerCode());
		}
		obj = remoteS.trainPlanGatherAfter(approve, entryIds, planTime,
				employee.getEnterpriseCode(), start, limit);

		if (obj != null) {
			write(JSONUtil.serialize(obj));
		} else {
			write("{totalCount : 0,list :[]}");
		}

	}
	
	//汇总查询列表
	public void getPlanGatherQueryList() throws Exception {
		// 计划时间
		String planTime = request.getParameter("planTime");
		// 计划计划类别
		String planType = request.getParameter("planType");
//		//部门
		String planDept = request.getParameter("editDepcode");
		// 判断审批
		String approve = request.getParameter("approve");
		
		// add by liuyi 20100427 增加isGatherQuery 是否为汇总查询，1是  汇总查询时，为汇总的数据查询不到
		String isGatherQuery = request.getParameter("isGatherQuery");

		String entryIds = "";
		if (approve != null && approve.equals("approve")) {
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqTrainPlan" }, employee.getWorkerCode());
		}
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		int start = 0, limit = 10000;
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(objstart.toString());
			limit = Integer.parseInt(objlimit.toString());
		}
		obj = remoteS.findPlanGatherQueryList(approve, entryIds, planType,
				planTime, planDept, employee.getEnterpriseCode(),isGatherQuery,
				start, limit);
		if (obj != null) {
			write(JSONUtil.serialize(obj));
		} else {
			write("{totalCount : 0,list :[]}");
		}
		//		
		// System.out.println(JSONUtil.serialize(obj));
	}
	
	/**
	 * add by liuyi 20100427 
	 * 部门培训计划查询 只为该查询页面用 
	 * @throws Exception
	 */
	public void getDeptTrainPlanQueryList() throws Exception {
		// 计划时间
		String planTime = request.getParameter("planTime");
		// 计划计划类别
		String planType = request.getParameter("planType");
//		//部门
		String planDept = request.getParameter("editDepcode");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(objstart.toString());
			int limit = Integer.parseInt(objlimit.toString());
			obj = remoteS.getDeptTrainPlanQueryList(planTime,planType,planDept,employee.getEnterpriseCode(),start, limit);
		}else{
			obj = remoteS.getDeptTrainPlanQueryList(planTime,planType,planDept,employee.getEnterpriseCode());
		}
		
			write(JSONUtil.serialize(obj));
	}
	
	/**
	 * add by liuyi 20100427 查询部门表中为一级部门的部门
	 * @throws JSONException 
	 */
	public void getManageDeptList() throws JSONException{
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = null;
		if(start != null && limit != null)
			pg = remoteS.getManageDeptList(employee.getEnterpriseCode(),Integer.parseInt(start),Integer.parseInt(limit));
		else
			pg = remoteS.getManageDeptList(employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
		
	}
	
	/**
	 * 部门培训计划执行回填列表记录
	 * add by drdu 091210
	 * @throws JSONException
	 */
	public void findTrainPlanBackfillList() throws JSONException
	{
		String planDate = request.getParameter("planDate");
		// modified by liuyi 20100504 数据库中存的部门为一级部门
//		String planDept = employee.getDeptCode();
		String abs = mRemote.getManagerDept(employee.getDeptId());
		String[] arr = abs.split(",");
		String planDept = "";
		if(arr != null && arr.length > 0)
			planDept = arr[0];
		String report_By = employee.getWorkerCode();//request.getParameter("report_By");
		String flag = request.getParameter("flag");
		String entryIds = "";
		if (flag != null && flag.equals("Y")) {
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqDeptplanBack" }, employee.getWorkerCode());
		}
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remoteS.findTrainPlanBackfillList(employee.getEnterpriseCode(), planDept, planDate,report_By,flag, entryIds,start,limit);
		} else {
			object = remoteS.findTrainPlanBackfillList(employee.getEnterpriseCode(), planDept, planDate,report_By,flag,entryIds);
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	/**
	 * 部门培训计划执行回填保存
	 * add by drdu 091210
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveFinishNumber() throws Exception {
		String str = request.getParameter("isUpdate");
		Object obj = JSONUtil.deserialize(str);
		String mainId = request.getParameter("id"); 
		String month = request.getParameter("month");
		 
		if (str!=null && !str.trim().equals(""))
		{
			remoteS.update((List<Map>) obj, Long.parseLong(mainId),employee.getWorkerCode(),month);
		} 
		write("{success: true,msg:'保存成功！'}");
	}
	
	/**
	 * 取得部门培训计划执行回填状态
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getPlanMainBackObj() throws Exception {

		String planDate = request.getParameter("planDate");
		// modified by liuyi 20100504 数据库中存的部门为一级部门
//		String deptCode = employee.getDeptCode();
		String abs = mRemote.getManagerDept(employee.getDeptId());
		String[] arr = abs.split(",");
		String deptCode = "";
		if(arr != null && arr.length > 0)
			deptCode = arr[0];
		
		BpJTrainingTypeForm obj = remoteS.getBpTrainMainBackList(planDate, deptCode);
		List lists = null;
		String returnStr = "{model:" + JSONUtil.serialize(obj) + ",list:"
				+ JSONUtil.serialize(lists) + "}";
		write(returnStr);
	}
	
	/**
	 * 部门培训计划回填汇总列表
	 * add by drdu 091212
	 * @throws JSONException
	 */
	public void findTrainPlanBackGatherList() throws JSONException
	{
		String month = request.getParameter("planDate");
		String flag = request.getParameter("flag");
		String depName = request.getParameter("depName");
		
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remoteS.findPlanBackGatherList(employee.getEnterpriseCode(), month,depName, flag, start,limit);
		} else {
			object = remoteS.findPlanBackGatherList(employee.getEnterpriseCode(), month,depName, flag);
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	/**
	 * 批量修改部门培训计划回填汇总记录
	 * add by drdu 091214
	 * @throws JSONException
	 */
	@SuppressWarnings({ "unchecked", "unchecked" })
	public void updateBackGatherRecord() throws JSONException
	{
		String str = request.getParameter("isUpdate");
		String month = request.getParameter("month");
		String trainingMainId = request.getParameter("trainingMainId");
		
		Object obj = JSONUtil.deserialize(str);
		 
		if (str!=null && !str.trim().equals(""))
		{
			remoteS.updateBackGather((List<Map>) obj,month,Long.parseLong(trainingMainId));
		} 
		write("{success: true,msg:'保存成功！'}");
	}

	/**
	 * 回填汇总上报前执行的保存操作
	 */
	public void addSumApproveRecord()
	{
		String month = request.getParameter("month");
		String trainingMainId = request.getParameter("trainingMainId");
				remoteS.addSumApproveRecord(month, Long.parseLong(trainingMainId));
				write("{success: true,msg:'保存成功！'}");
	}
	
	/**
	 * 
	 * 回填汇总上报方法取相应值
	 */
	 
	@SuppressWarnings("unchecked")
	public void getTrainingSumIdForAdd() throws JSONException
	{
		String month = request.getParameter("planTime");
		BpJTrainingSumForm obj = remoteS.getTrainingSumIdForAdd(month);
		List lists = null;
		String returnStr = "{model:" + JSONUtil.serialize(obj) + ",list:"
				+ JSONUtil.serialize(lists) + "}";
		write(returnStr);
	}
	
	/**
	 * 取得部门培训计划执行回填汇总状态
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getPlanBackGetherWo() throws Exception {

		String planTime = request.getParameter("planTime");
		BpJTrainingSumApproval obj = remoteS.getBpTrainBackGatherList(planTime);
		List lists = null;
		String returnStr = "{model:" + JSONUtil.serialize(obj) + ",list:"
				+ JSONUtil.serialize(lists) + "}";
		write(returnStr);
	}
	
	/**
	 * 退回按钮弹出的部门选择页面的方法
	 * add by drdu 20100104
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void findTraingDeptReturnSelectList() throws JSONException
	{
		String month = request.getParameter("month"); 
		List list = remoteS.findTraingDept(month);
		String str = null;
		str = JSONUtil.serialize(list);
		write("{list:"+str+"}");
	}
	
	/**
	 * 回填汇总页面退回方法
	 * add by drdu 20100104
	 */
	public void backGatherReturnSelectMethod()
	{
		String deptCode = request.getParameter("deptCode");
		String month = request.getParameter("month");
		remoteS.backGatherReturnSelectMethod(month, deptCode);
		
		write("{success: true,msg:'操作成功！'}");
	}
	
	/**
	 * 部门计划汇总修改保存
	 * add by drdu 20100105
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void updateDeptGatherRecord() throws JSONException
	{
		String str = request.getParameter("isUpdate");
		Object obj = JSONUtil.deserialize(str);
		 
		if (str!=null && !str.trim().equals(""))
		{
			remoteS.updateDeptGather((List<Map>) obj);
		} 
		write("{success: true,msg:'保存成功！'}");
	}
	
	
	public BpJTrainingDetail getTrainDetail() {
		return trainDetail;
	}

	public void setTrainDetail(BpJTrainingDetail trainDetail) {
		this.trainDetail = trainDetail;
	}
	
	/**
	 * 根据时间取得部门列表
	 * @throws Exception
	 */
	public void findTraingDept() throws Exception
	{
		String month = request.getParameter("month");
		GetDataForCombox(remoteS.findTraingDept(month));
	}
	
	// 获得某个combox的数据源
	public void GetDataForCombox(List list) throws Exception {
		Iterator it = list.iterator();
		String str = "[";
		String id = "";
		String name = "";
		while (it.hasNext()) {
			id = "";
			name = "";
			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				id = data[0].toString();
			}
			if (data[1] != null) {
				name = data[1].toString();
			}
			str = str + "{'id':" + id + ",'name':'" + name + "'},";
		}
		if (!str.equals("[")) {
			str = str.substring(0, str.length() - 1);
		}
		str = str + "]";
		write(str);
	}
	/**
	 * add by liuyi 091215 
	 */
	public void getCompleteDescription()
	{
		String month = request.getParameter("month");
		String temp = null;
		temp = remoteS.getCompleteDescription(month);
		write("{success:true,entryId:"+temp+"}");
	}
	
	/**
	 * add by liuyi 20100427 
	 * 获得计划汇总的审批的工作流号
	 */
	public void getPlanGatherDesc(){
		String month = request.getParameter("month");
		String temp = null;
		temp = remoteS.getPlanGatherDesc(month);
		write("{success:true,entryId:"+temp+"}");
	}
	
	
	// 判断一部门在某一月份下可能审批
	public void judgeDeptCanApprove(){
		String planDept = request.getParameter("planDept");
		String planTime = request.getParameter("planTime");
		String flag = request.getParameter("flag");
		String outstr = "{success:true,msg:null}";
		String msg = remoteS.judgeDeptCanApprove(planDept,planTime,employee.getEnterpriseCode(),flag);
		if(msg != null)
			outstr = "{success:true,msg:'"+msg+"'}";
		write(outstr);
	}
}
