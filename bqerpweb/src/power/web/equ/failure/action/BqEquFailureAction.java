package power.web.equ.failure.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquCBlock;
import power.ejb.equ.base.EquCBlockFacadeRemote;
import power.ejb.equ.base.EquCBug;
import power.ejb.equ.base.EquCBugFacadeRemote;
import power.ejb.equ.failure.BqEquCFailureApprove;
import power.ejb.equ.failure.BqEquJfailuresRemote;
import power.ejb.equ.failure.EquFailuresHisInfo;
import power.ejb.equ.failure.EquFailuresInfo;
import power.ejb.equ.failure.EquJFailureHistory;
import power.ejb.equ.failure.EquJFailureHistoryFacadeRemote;
import power.ejb.equ.failure.EquJFailures;
import power.ejb.equ.failure.EquJFailuresFacadeRemote;
import power.ejb.equ.workbill.EquJWo;
import power.ejb.equ.workbill.EquJWoFacadeRemote;
import power.ejb.hr.HrCDept;
import power.ejb.hr.HrCDeptFacadeRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftWorkerFacadeRemote;
import power.ejb.system.SysCUlFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.PostMessage;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

/**
 * 缺陷类别的相关操作
 */
@SuppressWarnings("serial")
public class BqEquFailureAction extends AbstractAction {

	private RunCSpecialsFacadeRemote spremote;

	private HrJEmpInfoFacadeRemote eremote;

	private HrCDeptFacadeRemote hremote;

	private EquCBlockFacadeRemote equremote;

	private EquCBugFacadeRemote bugremote;

	private EquJFailuresFacadeRemote fremote;

	private EquJFailureHistoryFacadeRemote historyremote;

	private BqEquCFailureApprove approveremote;

	private RunCShiftWorkerFacadeRemote shifitremote;

	private BqEquJfailuresRemote bqremote;
	// 缺陷对象
	private EquJFailures failure;

	private EquJFailureHistory failurehis;

	// 密码
	private String confirmPwd;

	private String pid;

	private String deptId;

	// 查询条件之开始时间
	private String sdate;
	// 查询条件之结束时间
	private String edate;
	// 状态
	private String status;
	// 所属系统
	private String belongBlock;
	// 管辖专业
	private String specialityCode;
	// 检修部门
	private String deptCode;
	private String failureCode;
	private EquJWoFacadeRemote equWoRemote;
	/**
	 * 
	 * 构造方法
	 * 
	 */
	public BqEquFailureAction() {
		spremote = (RunCSpecialsFacadeRemote) factory
				.getFacadeRemote("RunCSpecialsFacade");

		fremote = (EquJFailuresFacadeRemote) factory
				.getFacadeRemote("EquJFailuresFacade");

		equremote = (EquCBlockFacadeRemote) factory
				.getFacadeRemote("EquCBlockFacade");

		bugremote = (EquCBugFacadeRemote) factory
				.getFacadeRemote("EquCBugFacade");

		hremote = (HrCDeptFacadeRemote) factory
				.getFacadeRemote("HrCDeptFacade");

		eremote = (HrJEmpInfoFacadeRemote) factory
				.getFacadeRemote("HrJEmpInfoFacade");

		historyremote = (EquJFailureHistoryFacadeRemote) factory
				.getFacadeRemote("EquJFailureHistoryFacade");
		approveremote = (BqEquCFailureApprove) factory
				.getFacadeRemote("BqEquCFailureApproveImpl");
		shifitremote = (RunCShiftWorkerFacadeRemote) factory
				.getFacadeRemote("RunCShiftWorkerFacade");
		bqremote = (BqEquJfailuresRemote) factory
				.getFacadeRemote("BqEquJfailures");
		//add by kzhang 20100928 工单生成
		equWoRemote = (EquJWoFacadeRemote) factory
		.getFacadeRemote("EquJWoFacade");

	}

	/**
	 * 增加缺陷
	 */
	public void addFailure() {
		failure.setEntrepriseCode(employee.getEnterpriseCode());
		failure.setIsuse("Y");
		// add by liuyi 20100312 默认is_send 为Y
		failure.setIsSend("Y");
		fremote.save(failure);
		write("{success:true}");
	}

	/**
	 * 修改缺陷
	 */
	public void updateFailure() {
		EquJFailures model = fremote.findById(failure.getId());
		failure.setEntrepriseCode(employee.getEnterpriseCode());
		failure.setIsuse("Y");
		failure.setFailureCode(model.getFailureCode());
		failure.setAttributeCode(model.getAttributeCode());
		
		// add by liuyi 20100312 默认is_send 为Y
		failure.setIsSend("Y");
		fremote.update(failure);
		write("{success:true}");
	}

	/**
	 * 删除缺陷
	 */
	public void deleteFailure() {
		EquJFailures model = fremote.findById(failure.getId());
		model.setIsuse("N");
		fremote.update(model);
		write("{success:true}");
	}

	private boolean isLeafdept(Long pid) throws NamingException {
		List<HrCDept> ld = hremote.findByPdeptId(pid);
		if (ld != null && ld.size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	private String toDeptTreeJsonStr(List<HrCDept> list) throws Exception {
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("[");
		String icon = "";
		for (int i = 0; i < list.size(); i++) {
			HrCDept dept = list.get(i);
			if (isLeafdept(dept.getDeptId())) {
				icon = "file";
			} else {
				icon = "folder";
			}
			JSONStr.append("{\"text\":\"" + dept.getDeptName() + "\",\"id\":\""
					+ dept.getDeptId() + "\",\"deptCode\":\""
					+ dept.getDeptCode() + "\",\"leaf\":"
					+ isLeafdept(dept.getDeptId()) + ",\"cls\":\"" + icon
					+ "\"},");
		}
		if (JSONStr.length() > 1) {
			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		}
		JSONStr.append("]");
		return JSONStr.toString();
	}

	public void getDeptTree() throws Exception {
		List<HrCDept> list = hremote.findByPdeptId(Long.parseLong(pid));
		String str = toDeptTreeJsonStr(list);
		write(str);
	}

	public void getEmpbyList() throws JSONException {
		List<HrJEmpInfo> list = eremote.findByDeptId(Long.parseLong(deptId));
		write(JSONUtil.serialize(list));
	}

	/**
	 * 获取机组信息列表，用于登记，没有 "全部" 选项
	 */
	public void findBlockListByRe() throws JSONException {
		String fuzzy = "";
		Object myobj = request.getParameter("fuzzy");
		if (myobj != null) {
			fuzzy = myobj.toString();
		}
		String enterpriseCode = employee.getEnterpriseCode();
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = equremote.findEquList(fuzzy, enterpriseCode, start, limit);
		} else {
			obj = equremote.findEquList(fuzzy, enterpriseCode);
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 获取机组信息列表
	 */
	public void findBlockList() throws JSONException {
		String fuzzy = "";
		Object myobj = request.getParameter("fuzzy");
		if (myobj != null) {
			fuzzy = myobj.toString();
		}
		String enterpriseCode = employee.getEnterpriseCode();
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = equremote.findEquList(fuzzy, enterpriseCode, start, limit);
		} else {
			obj = equremote.findEquList(fuzzy, enterpriseCode);
		}
		obj.getList().add(0, new EquCBlock(-99l, "", "全部", employee.getEnterpriseCode(), "Y"));
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 获取运行专业列表
	 * 
	 * @throws JSONException
	 */
	public void getRunProfessionList() throws JSONException {
		List<RunCSpecials> spList = spremote.findByType("1", employee
				.getEnterpriseCode());
		String str = JSONUtil.serialize(spList);
		write("{list:" + str + "}");
	}

	/**
	 * 获取管辖专业列表
	 * 
	 * @throws JSONException
	 */
	public void getDominationProfessionList() throws JSONException {
		List<RunCSpecials> spList = spremote.findByType("0", employee
				.getEnterpriseCode());
		List<RunCSpecials> rpList = spremote.findByType("2", employee
				.getEnterpriseCode());
		List<RunCSpecials> list = new ArrayList();
		for (RunCSpecials tmp : spList) {
			if (!list.contains(tmp)) {
				list.add(tmp);
			}
		}
		for (RunCSpecials tmp : rpList) {
			if (!list.contains(tmp)) {
				list.add(tmp);
			}
		}
		String str = JSONUtil.serialize(list);
		write("{list:" + str + "}");
	}

	/**
	 * 检修部门列表
	 * 
	 * @throws JSONException
	 */
	public void getRepairDeptList() throws JSONException {
		//String fType = request.getParameter("fType").toString();
		List<HrCDept> deptList;
		deptList = hremote.getFailDeptById();
		String str = JSONUtil.serialize(deptList);
		write("{list:" + str + "}");
//		if(fType != null)
//		{
//			if("1".equals(fType) ||"2".equals(fType))
//			{
//				deptList = hremote.getFailDeptById1();
//			}
//			else if("3".equals(fType))
//			{
//				deptList = hremote.getFailDeptById2();
//			}
//			else
//			{
//				deptList = hremote.getFailDeptById();
//			}
//			String str = JSONUtil.serialize(deptList);
//			write("{list:" + str + "}");
//		}
//		
		
	}
	
//by ghzhou 090507
	public void getRepairDeptListByEqu() throws JSONException
	{
		String fType = request.getParameter("fType").toString();
		List<HrCDept> deptList;
		if(fType != null)
		{
			if("1".equals(fType) ||"2".equals(fType))
			{
				deptList = hremote.getFailDeptById1();
			}
			else if("3".equals(fType))
			{
				deptList = hremote.getFailDeptById2();
			}
			else
			{
				deptList = hremote.getFailDeptById();
			}
			String str = JSONUtil.serialize(deptList);
			write("{list:" + str + "}");
		}
		
	}

	/**
	 * 查询用获取检修专业列表---带“全部”选项
	 * 
	 * @throws JSONException
	 */
	public void queryDominationProfessionList() throws JSONException {
		List<RunCSpecials> spList = spremote.findByType("0", employee
				.getEnterpriseCode());
		List<RunCSpecials> rpList = spremote.findByType("2", employee
				.getEnterpriseCode());
		List<RunCSpecials> list = new ArrayList();
		for (RunCSpecials tmp : spList) {
			if (!list.contains(tmp)) {
				list.add(tmp);
			}
		}
		for (RunCSpecials tmp : rpList) {
			if (!list.contains(tmp)) {
				list.add(tmp);
			}
		}
		list.add(0, new RunCSpecials(-99l, "", "全部", "", "", "", -99l, "Y",
				employee.getEnterpriseCode()));
		String str = JSONUtil.serialize(list);
		write("{list:" + str + "}");
	}

	/**
	 * 查询用检修停用部门列表
	 * add by bjxu 091219
	 * @return
	 */
	public void queryRepairDeptStopList() throws JSONException {
//		String fType = request.getParameter("fType").toString();
		List<HrCDept> deptList = hremote.getFailDeptByIdStop();
		HrCDept allDept = new HrCDept();
		allDept.setDeptCode("");
		allDept.setDeptName("全部");
		deptList.add(0, allDept);
		String str = JSONUtil.serialize(deptList);
		write("{list:" + str + "}");
	}
	/**
	 * 查询用检修部门列表---带“全部”选项
	 * 
	 * @throws JSONException
	 */
	public void queryRepairDeptList() throws JSONException {
//		String fType = request.getParameter("fType").toString();
		List<HrCDept> deptList = hremote.getFailDeptById();
		HrCDept allDept = new HrCDept();
		allDept.setDeptCode("");
		allDept.setDeptName("全部");
		deptList.add(0, allDept);
		String str = JSONUtil.serialize(deptList);
		write("{list:" + str + "}");
//		List<HrCDept> deptList;
//		if(fType != null)
//		{
//			if("1".equals(fType) ||"2".equals(fType))
//			{
//				deptList = hremote.getFailDeptById1();
//			}
//			else
//			{
//				deptList = hremote.getFailDeptById2();
//			}
//			HrCDept allDept = new HrCDept();
//			allDept.setDeptCode("");
//			allDept.setDeptName("全部");
//			deptList.add(0, allDept);
//			String str = JSONUtil.serialize(deptList);
//			write("{list:" + str + "}");
//		}
	}

	/**
	 * 获取故障树列表
	 */
	public void getBugTreeByCode() {
		String code = "";
		String id = "";
		code = request.getParameter("id");
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("[");
		if (code.equals("root")) {
			id = "0";
		} else {
			EquCBug equmodel = bugremote.findByCode(code, employee
					.getEnterpriseCode());
			if (equmodel != null) {
				id = equmodel.getBugId().toString();
			}
		}
		if (!id.equals("")) {
			List<EquCBug> list = bugremote.getListByParent(Long.parseLong(id),
					employee.getEnterpriseCode());
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					EquCBug model = list.get(i);
					boolean isLeaf = (model.getIfLeaf() == "1") ? true : false;
					JSONStr.append("{id:'" + model.getBugCode() + "',text:'"
							+ model.getBugName() + "',leaf:" + isLeaf + "},");
				}
				if (JSONStr.length() > 1) {
					JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
				}
			}
		}

		JSONStr.append("]");
		write(JSONStr.toString());

	}

	/**
	 * 上报缺陷
	 */
	public void reportFailure() {
		String flowCode = request.getParameter("flowCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String eventIdentify=request.getParameter("eventIdentify");
		try {
			EquJFailures entity = fremote.findById(failure.getId());
			
			
			// add by ywliu 20100611
			PostMessage postMsg=new PostMessage();
			if (nextRoles == null || nextRoles.equals("")) {
				if(entity.getFailuretypeCode().equals("1")||entity.getFailuretypeCode().equals("2"))
				{
				  RunCSpecialsFacadeRemote specialRemote=(RunCSpecialsFacadeRemote)factory.getFacadeRemote("RunCSpecialsFacade");
				  String specialName= specialRemote.findByCode(entity.getDominationProfession(), employee.getEnterpriseCode()).getSpecialityName()+"缺陷点检长";
				  
				  nextRoles = postMsg.getFistStepRoles(flowCode, actionId, null,
						  specialName);
				}
				else
				{
				nextRoles = postMsg.getFistStepRoles(flowCode, actionId, null,
						null);
				}
			}
			
			
			approveremote.failureReport(entity, flowCode, Long
					.parseLong(actionId), approveText, nextRoles,eventIdentify);
			//add by fyyang 20100521 短信通知
			if(nextRoles!=null&&!nextRoles.equals(""))
			{
			
			String msg="缺陷"+entity.getFailureContent()+"已上报，请您审批!";
			postMsg.sendMsg(nextRoles, msg);
			
			}
			write("{success:true}");
			//上报检修消缺时，生成工单 add by kzhang 20100928
			if ("SB(JXXQ)".equals(eventIdentify)) {
				EquJWo ejw=new EquJWo();
				ejw.setWorkorderContent(entity.getFailureContent());
				ejw.setWorkorderType("Q");
				ejw.setProfessionCode(entity.getDominationProfession());
				HrCDept dept=hremote.getFirstLevelDept(entity.getWriteBy(), "3", employee.getEnterpriseCode());
				if (dept!=null) {
					ejw.setRepairDepartment(dept.getDeptCode());
				}
				ejw.setRequireStarttime(new Date());
				if("3".equals(entity.getFailuretypeCode())){
					Calendar date = Calendar.getInstance();
					date.setTime(new Date());
					date.add(date.HOUR, 24);
					ejw.setRequireEndtime(date.getTime());
				}
				ejw.setRequireManCode(entity.getWriteBy());
				ejw.setRequireTime(entity.getWriteDate());
				ejw.setEnterprisecode(employee.getEnterpriseCode());
				ejw.setIfUse("Y");
				ejw.setWorkorderStatus("0");
				//add by mlian
				ejw.setWfState(0l);
				//end add
				equWoRemote.save(ejw, entity.getFailureCode(), null);
			}
		} catch (Exception e) {
			write("{success:false,errorMessage:'操作失败!'}");
		}
	}

	/**
	 * 缺陷认领
	 */
	public void claimFailure()
	{
		if (checkUserInfo(failurehis.getApprovePeople())) {
			try {
				EquJFailures entity = fremote.findById(failure.getId());
				entity.setCliamBy(employee.getWorkerCode());
				entity.setCliamDate(new Date());
				fremote.update(entity);
				List<EquFailuresInfo> list=bqremote.findFailureById(entity.getId().toString(), entity.getEntrepriseCode()).getList();
				write("{success:true,data:"+JSONUtil.serialize(list.get(0).getCliamDate())+"}");
			} catch (Exception e) {
				write("{success:false,errorMessage:'操作失败,请联系管理员!'}");
			}
		}
		 else {
				write("{success:false,errorMessage:'密码错误，请联系管理员!'}");
			}
	}
	/**
	 * 查找缺陷上报列表
	 * 
	 * @throws Exception
	 */
	public void findFailureList() throws Exception {
		int start = 0;
		int limit = 100000;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject list = bqremote.findListByStatus("'0','10'", employee
				.getEnterpriseCode(), sdate, edate, start, limit, belongBlock,
				specialityCode, deptCode, employee.getWorkerCode());
		String str = "{total:" + list.getTotalCount() + ",list:"
				+ JSONUtil.serialize(list.getList()) + "}";
		write(str);
	}

	/**
	 * 缺陷审批列表
	 * 
	 * @throws Exception
	 */
	public void findApproveFailureList() throws Exception {
		int start = 0;
		int limit = 900000000;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String nos = workflowService.getAvailableWorkflow(
				new String[] { "bqFailure" }, employee.getWorkerCode());
		String whereDep = employee.getDeptCode();
		String whereProfession = shifitremote.getRunSpecialByEmp(employee
				.getWorkerCode(), employee.getEnterpriseCode());
		PageObject list = bqremote.findApproveList(nos, employee
				.getEnterpriseCode(), sdate, edate, status, start, limit,
				belongBlock, specialityCode, deptCode, whereDep,
				whereProfession);
		String str = "{total:" + list.getTotalCount() + ",list:"
				+ JSONUtil.serialize(list.getList()) + "}";
		write(str);
	}
	/**
	 * 查询缺陷详细信息
	 * @throws JSONException
	 */
	
	public void findFailureById() throws JSONException {
		PageObject list = bqremote.findFailureById(failure.getId().toString(),
				employee.getEnterpriseCode());
		String str = JSONUtil.serialize(list.getList());
		str = str.replace("[", "");
		str = str.replace("]", "");
		write("{success:false,data:" + str + ",msg:'detailFailure.jsp'}");
	}
	/**
	 * 根据状态进行审批跳转
	 * 
	 * @throws JSONException
	 */
	public void approveFailure() throws JSONException {
		EquJFailures model = fremote.findById(failure.getId());
		String url = approveremote.getCurrentStepsInfo(
				Long.parseLong(model.getWorkFlowNo()), null).get(0).getUrl();
		PageObject list = bqremote.findFailureById(failure.getId().toString(),
				employee.getEnterpriseCode());
		// int status = Integer.parseInt(model.getWoStatus());
		String str = JSONUtil.serialize(list.getList());
		str = str.replace("[", "");
		str = str.replace("]", "");
		write("{success:false,data:" + str + ",msg:'" + url + "'}");
	}

	/**
	 * 用户验证登陆
	 * 
	 * @return boolean true表示验证通过， false表示验证失败
	 */
	private boolean checkUserInfo(String worker) {
		SysCUlFacadeRemote userRemote = (SysCUlFacadeRemote) factory
				.getFacadeRemote("SysCUlFacade");
		return userRemote.checkUserRight(employee.getEnterpriseCode(), worker,
				this.getConfirmPwd());
	}
	/**
	 * 缺陷确认
	 */
	public void confirmFailure(){
		String actionId = request.getParameter("actionId");
		String nextRoles = request.getParameter("nextRoles");
		String repairDept = request.getParameter("repairDept");
		if (checkUserInfo(failurehis.getApprovePeople())) {
			try {
				EquJFailures entity = fremote.findById(failure.getId());
				entity.setRepairDep(repairDept);
				System.out.println("修改检修部门成功:"+ entity.getRepairDep());
				entity.setWoStatus("18");// 已确认待消缺
				failurehis.setApproveType("18");
				failurehis.setFailureCode(entity.getFailureCode());
				failurehis.setApproveTime(new Date());
				failurehis.setEnterpriseCode(entity.getEntrepriseCode());
				failurehis.setIsuse("Y");
				// add by ywliu 20100611
				PostMessage postMsg=new PostMessage();
				if (nextRoles == null || nextRoles.equals("")) {
					nextRoles = postMsg.getNextSetpRoles(entity.getWorkFlowNo().toString(), actionId);
				}
				approveremote.failureApprove(entity, failurehis, Long
						.parseLong(actionId), null, nextRoles);
				//add by fyyang 20100521--短信通知
				if(nextRoles!=null&&!nextRoles.equals(""))
				{
				
				String msg="缺陷"+entity.getFailureContent()+"已确认，请您审批!";
				postMsg.sendMsgByWorker(entity.getFindBy().toString(), msg);
				
				}
				//------------add end --------------
				write("{success:true}");
				//点检确认确认时，生成工单 add by kzhang 20100928
				EquJWo ejw=new EquJWo();
				ejw.setWorkorderContent(entity.getFailureContent());
				ejw.setWorkorderType("Q");
				ejw.setProfessionCode(entity.getDominationProfession());
				HrCDept dept=hremote.getFirstLevelDept(entity.getWriteBy(), "3", employee.getEnterpriseCode());
				if (dept!=null) {
					ejw.setRepairDepartment(dept.getDeptCode());
				}
				ejw.setRequireStarttime(new Date());
				if("2".equals(entity.getFailuretypeCode())){
					Calendar date = Calendar.getInstance();
					date.setTime(new Date());
					date.add(date.HOUR, 72);
					ejw.setRequireEndtime(date.getTime());
				}
				ejw.setRequireManCode(entity.getWriteBy());
				ejw.setRequireTime(entity.getWriteDate());
				ejw.setEnterprisecode(employee.getEnterpriseCode());
				ejw.setIfUse("Y");
				ejw.setWorkorderStatus("0");
				//add by mlian
				ejw.setWfState(0l);
				//end add
				equWoRemote.save(ejw, entity.getFailureCode(), null);
			} catch (Exception e) {
				write("{success:false,errorMessage:'" + e.getMessage() + "'}");
			}
		} else {
			write("{success:false,errorMessage:'密码错误，请联系管理员!'}");
		}
	}
	/**
	 * 缺陷消缺
	 */
	@SuppressWarnings("unchecked")
	public void eliminateFailure() {
		String actionId = request.getParameter("actionId");
		String nextRoles = request.getParameter("nextRoles");
		if (checkUserInfo(failurehis.getApprovePeople())) {
			try {
				EquJFailures entity = fremote.findById(failure.getId());
				Map m = new java.util.HashMap();
				if (entity.getFailuretypeCode().equals("1")// 12类缺陷
						|| entity.getFailuretypeCode().equals("2")) {
					m.put("is12Failure", true);
					entity.setWoStatus("3");// 已消缺,状态修改为点检待验收
					failurehis.setApproveType("1");
				} else {
					m.put("is12Failure", false);// 34类缺陷
					entity.setWoStatus("14");// 已消缺，状态修改为运行待验收
					failurehis.setApproveType("1");
				}
				failurehis.setFailureCode(entity.getFailureCode());
				failurehis.setApproveTime(new Date());
				failurehis.setEnterpriseCode(entity.getEntrepriseCode());
				failurehis.setIsuse("Y");
				// add by ywliu 20100611
				PostMessage postMsg=new PostMessage();
				if (nextRoles == null || nextRoles.equals("")) {
					nextRoles = postMsg.getNextSetpRoles(entity.getWorkFlowNo().toString(), actionId);
				}
				approveremote.failureApprove(entity, failurehis, Long
						.parseLong(actionId), m, nextRoles);
				//add by fyyang 20100521--短信通知
				if(nextRoles!=null&&!nextRoles.equals(""))
				{
				
				String msg="缺陷"+entity.getFailureContent()+"已消缺，请您审批!";
				postMsg.sendMsgByWorker(entity.getFindBy().toString(), msg);
				
				}
				//------------add end --------------
				write("{success:true}");
			} catch (Exception e) {
				write("{success:false,errorMessage:'" + e.getMessage() + "'}");
			}
		} else {
			write("{success:false,errorMessage:'密码错误，请联系管理员!'}");
		}
	}

	/**
	 * 缺陷申请仲裁处理
	 * 
	 * @throws ParseException
	 */
	public void applyArbitrateFailure() throws ParseException {
		String actionId = request.getParameter("actionId");
		String nextRoles = request.getParameter("nextRoles");
		if (checkUserInfo(failurehis.getApprovePeople())) {
			try {
				EquJFailures entity = fremote.findById(failure.getId());
				entity.setWoStatus("7");// 状态修改为设备部仲裁
				failurehis.setFailureCode(entity.getFailureCode());
				failurehis.setApproveTime(new Date());
				failurehis.setApproveType("7");
				failurehis.setEnterpriseCode(entity.getEntrepriseCode());
				failurehis.setIsuse("Y");
				// add by ywliu 20100611
				PostMessage postMsg=new PostMessage();
				if (nextRoles == null || nextRoles.equals("")) {
					nextRoles = postMsg.getNextSetpRoles(entity.getWorkFlowNo().toString(), actionId);
				}
				approveremote.failureApprove(entity, failurehis, Long
						.parseLong(actionId), null, nextRoles);
				//---add by fyyang 20100521 短信通知
				if(nextRoles!=null&&!nextRoles.equals(""))
				{
				
				String msg="缺陷"+entity.getFailureContent()+"已申请仲裁，请您审批!";
				postMsg.sendMsg(nextRoles, msg);
				
				}
				//----------------------------------
				write("{success:true}");
			} catch (Exception e) {
				write("{success:false,errorMessage:'操作失败!'}");
			}
		} else {
			write("{success:false,errorMessage:'密码错误，请联系管理员!'}");
		}
	}

	/**
	 * 缺陷仲裁处理(发往检修消缺)
	 */
	public void arbitrateFailure() {
		String actionId = request.getParameter("actionId");
		String nextRoles = request.getParameter("nextRoles");
		if (checkUserInfo(failurehis.getApprovePeople())) {
			try {
				EquJFailures entity = fremote.findById(failure.getId());
				
				if (failurehis.getArbitrateType().equals("1"))// 管辖专业仲裁
				{
					entity.setDominationProfession(failurehis.getArbitrateProfession());
				} 
				else if (failurehis.getArbitrateType().equals("2"))// 检修部门仲裁
				{
					entity.setRepairDep(failurehis.getArbitrateDept());
				}
				if (failurehis.getArbitrateType().equals("3"))// 验收仲裁类别
				{
					failurehis.setCheckArbitrateType("1");
					
				} 
				else if (failurehis.getArbitrateType().equals("4"))// 仲裁类别为其它
				{
					failurehis.setCheckArbitrateType("1");
					entity.setDominationProfession(failurehis
							.getArbitrateProfession());
					entity.setRepairDep(failurehis.getArbitrateDept());
					entity.setFailuretypeCode(failurehis.getArbitrateKind());
						
				} 
				else if (failurehis.getArbitrateType().equals("5"))// 类别仲裁
				{
					entity.setFailuretypeCode(failurehis.getArbitrateKind());
				}
				entity.setWoStatus("8");// 已仲裁继续消缺，状态修改为已仲裁待消缺
				failurehis.setApproveType("8");
				failurehis.setFailureCode(entity.getFailureCode());
				failurehis.setApproveTime(new Date());
				failurehis.setEnterpriseCode(entity.getEntrepriseCode());
				failurehis.setIsuse("Y");
				// add by ywliu 20100611
				PostMessage postMsg=new PostMessage();
				if (nextRoles == null || nextRoles.equals("")) {
					nextRoles = postMsg.getNextSetpRoles(entity.getWorkFlowNo().toString(), actionId);
				}
				approveremote.failureApprove(entity, failurehis, Long
						.parseLong(actionId), null, nextRoles);
				//---add by fyyang 20100521 短信通知
				if(nextRoles!=null&&!nextRoles.equals(""))
				{
				
				String msg="缺陷"+entity.getFailureContent()+"已仲裁发往检修消缺，请您审批!";
				postMsg.sendMsg(nextRoles, msg);
				
				}
				//----------------------------------
				write("{success:true}");
				//缺陷仲裁处理(发往检修消缺)时，生成工单 add by kzhang 20100928
				//通过缺陷单号判断是否已生成工单
				if (equWoRemote.checkWorkBillIsExists(entity.getFailureCode(), employee.getEnterpriseCode())) {
					EquJWo ejw=new EquJWo();
					ejw.setWorkorderContent(entity.getFailureContent());
					ejw.setWorkorderType("Q");
					ejw.setProfessionCode(entity.getDominationProfession());
					HrCDept dept=hremote.getFirstLevelDept(entity.getWriteBy(), "3", employee.getEnterpriseCode());
					if (dept!=null) {
						ejw.setRepairDepartment(dept.getDeptCode());
					}
					ejw.setRequireStarttime(new Date());
					if("2".equals(entity.getFailuretypeCode())){
						Calendar date = Calendar.getInstance();
						date.setTime(new Date());
						date.add(date.HOUR, 72);
						ejw.setRequireEndtime(date.getTime());
					}
					ejw.setRequireManCode(entity.getWriteBy());
					ejw.setRequireTime(entity.getWriteDate());
					ejw.setEnterprisecode(employee.getEnterpriseCode());
					ejw.setIfUse("Y");
					ejw.setWorkorderStatus("0");
					//add by mlian
					ejw.setWfState(0l);
					//end add
					equWoRemote.save(ejw, entity.getFailureCode(), null);
				}
			} catch (Exception e) {
				write("{success:false,errorMessage:'操作失败!'}");
			}
		} else {
			write("{success:false,errorMessage:'密码错误，请联系管理员!'}");
		}
	}

	/**
	 * 缺陷仲裁处理(发往运行验收)
	 */
	public void runarbitrateFailure() {
		String actionId = request.getParameter("actionId");
		String nextRoles = request.getParameter("nextRoles");
		if (checkUserInfo(failurehis.getApprovePeople())) {
			try {
				EquJFailures entity = fremote.findById(failure.getId());
				
				if (failurehis.getArbitrateType().equals("1"))// 管辖专业仲裁
				{
					entity.setDominationProfession(failurehis.getArbitrateProfession());
				} 
				else if (failurehis.getArbitrateType().equals("2"))// 检修部门仲裁
				{
					entity.setRepairDep(failurehis.getArbitrateDept());
				}
				if (failurehis.getArbitrateType().equals("3"))// 验收仲裁类别
				{
					failurehis.setCheckArbitrateType("2");
					
				} 
				else if (failurehis.getArbitrateType().equals("4"))// 仲裁类别为其它
				{
					failurehis.setCheckArbitrateType("2");
					entity.setDominationProfession(failurehis
							.getArbitrateProfession());
					entity.setRepairDep(failurehis.getArbitrateDept());
					entity.setFailuretypeCode(failurehis.getArbitrateKind());
						
				} 
				else if (failurehis.getArbitrateType().equals("5"))// 类别仲裁
				{
					entity.setFailuretypeCode(failurehis.getArbitrateKind());
				}
				entity.setWoStatus("14");// 已仲裁继续消缺，状态修改为运行待验收
				failurehis.setApproveType("2");
				failurehis.setFailureCode(entity.getFailureCode());
				failurehis.setApproveTime(new Date());
				failurehis.setEnterpriseCode(entity.getEntrepriseCode());
				failurehis.setIsuse("Y");
				// add by ywliu 20100611
				PostMessage postMsg=new PostMessage();
				if (nextRoles == null || nextRoles.equals("")) {
					nextRoles = postMsg.getNextSetpRoles(entity.getWorkFlowNo().toString(), actionId);
				}
				approveremote.failureApprove(entity, failurehis, Long
						.parseLong(actionId), null, nextRoles);
				//---add by fyyang 20100521 短信通知
				if(nextRoles!=null&&!nextRoles.equals(""))
				{
				
				String msg="缺陷"+entity.getFailureContent()+"已仲裁发往运行验收，请您审批!";
				postMsg.sendMsgByWorker(entity.getFindBy().toString(), msg);
				
				}
				//----------------------------------
				write("{success:true}");
			} catch (Exception e) {
				write("{success:false,errorMessage:'操作失败!'}");
			}
		} else {
			write("{success:false,errorMessage:'密码错误，请联系管理员!'}");
		}
	}

	/**
	 * 申请待处理
	 */
	public void applyAwaitFailure() {
		String actionId = request.getParameter("actionId");
		String nextRoles = request.getParameter("nextRoles");
		if (checkUserInfo(failurehis.getApprovePeople())) {
			try {
				
				EquJFailures entity = fremote.findById(failure.getId());
				Map m = new java.util.HashMap();
				String count=historyremote.findAwaitCount(entity.getFailureCode(), entity.getEntrepriseCode());
				if(count != null)
				{
					if (entity.getFailuretypeCode().equals( "1") || entity.getFailuretypeCode().equals("2")) {
						if (Integer.parseInt(count) < 3) {
							m.put("isableAwait", true);
						} else {
							m.put("isableAwait", false);
						}
					} else {
						m.put("isableAwait", true);
//						if (Integer.parseInt(count) < 1) {
//							m.put("isableAwait", true);
//						} else {
//							m.put("isableAwait", false);
//						}
					}
				}
				entity.setWoStatus("11");// 状态修改为点检待处理审批
				failurehis.setFailureCode(entity.getFailureCode());
				failurehis.setApproveTime(new Date());
				failurehis.setApproveType("11");
				failurehis.setEnterpriseCode(entity.getEntrepriseCode());
				failurehis.setIsuse("Y");
				// add by ywliu 20100611
				
				PostMessage postMsg=new PostMessage();
				if (nextRoles == null || nextRoles.equals("")) {
//					if(entity.getFailuretypeCode().equals("1")||entity.getFailuretypeCode().equals("2"))
//					{
					  RunCSpecialsFacadeRemote specialRemote=(RunCSpecialsFacadeRemote)factory.getFacadeRemote("RunCSpecialsFacade");
					  String specialName= specialRemote.findByCode(entity.getDominationProfession(), employee.getEnterpriseCode()).getSpecialityName()+"缺陷点检长";
					  
					  nextRoles = postMsg.getEquFailuresRoles(specialName);
//					}
//					else
//					{
//					nextRoles = postMsg.getFistStepRoles(entity.getWorkFlowNo(), actionId, null,
//							null);
//					}
				}
//				if (nextRoles == null || nextRoles.equals("")) {
//					nextRoles = postMsg.getNextSetpRoles(entity.getWorkFlowNo().toString(), actionId);
//				}
				approveremote.failureApprove(entity, failurehis, Long
						.parseLong(actionId), m, nextRoles);
				//---add by fyyang 20100521 短信通知
				if(nextRoles!=null&&!nextRoles.equals(""))
				{
				
				String msg="缺陷"+entity.getFailureContent()+"已申请延期，请您审批!";
				postMsg.sendMsg(nextRoles, msg);
				
				}
				//----------------------------------
				write("{success:true}");
			} catch (Exception e) {
				write("{success:false,errorMessage:'操作失败!'}");
			}
		} else {
			write("{success:false,errorMessage:'密码错误，请联系管理员!'}");
		}
	}

	/**
	 * 点检待处理审批
	 */
	public void repaireAwaitFailure() {
		String actionId = request.getParameter("actionId");
		String nextRoles = request.getParameter("nextRoles");
		String eventIdentify=request.getParameter("eventIdentify");
		String msg="";
		if (checkUserInfo(failurehis.getApprovePeople())) {
			try {
				EquJFailures entity = fremote.findById(failure.getId());
				if(failurehis.getTackleResult().equals("1"))
				{
					if (eventIdentify.equals("YCL"))// 已处理
					{
						msg="点检已处理";
						entity.setWoStatus("5");// 状态修改为点检已处理
						failurehis.setApproveType("5");
					} 
					else if(eventIdentify.equals("TY1"))
					{
						msg="点检已审批发至设备部主任";
						entity.setWoStatus("12");// 状态修改为设备部主任待处理
						failurehis.setApproveType("12");
					}
					else if(eventIdentify.equals("TY2"))
					{
						msg="点检已审批发至发电部主任";
						entity.setWoStatus("13");// 状态修改为发电部主任待处理
						failurehis.setApproveType("13");
					}
				}
				else if(failurehis.getTackleResult().equals("2"))//退回
				{
					entity.setWoStatus("15");// 状态修改为点检退回
					failurehis.setApproveType("15");
				}
				failurehis.setFailureCode(entity.getFailureCode());
				failurehis.setApproveTime(new Date());
				failurehis.setEnterpriseCode(entity.getEntrepriseCode());
				failurehis.setIsuse("Y");
				// add by ywliu 20100611
				PostMessage postMsg=new PostMessage();
				if (nextRoles == null || nextRoles.equals("")) {
					nextRoles = postMsg.getNextSetpRoles(entity.getWorkFlowNo().toString(), actionId);
				}
				approveremote.failureApprove(entity, failurehis, Long
						.parseLong(actionId), null, nextRoles);
				//---add by fyyang 20100521 短信通知
				if(nextRoles!=null&&!nextRoles.equals("")&&!msg.equals(""))
				{
				
			     msg="缺陷"+entity.getFailureContent()+msg+"，请您审批!";
				postMsg.sendMsg(nextRoles, msg);
				
				}
				//----------------------------------
				
				
				write("{success:true}");
			} catch (Exception e) {
				write("{success:false,errorMessage:'操作失败!'}");
			}
		} else {
			write("{success:false,errorMessage:'密码错误，请联系管理员!'}");
		}
	}

	/**
	 * 设备部主任待处理审批
	 */
	public void depawaitFailure() {
		String actionId = request.getParameter("actionId");
		String nextRoles = request.getParameter("nextRoles");
		String eventIdentify=request.getParameter("eventIdentify");
		String msg="";
		if (checkUserInfo(failurehis.getApprovePeople())) {
			try {
				EquJFailures entity = fremote.findById(failure.getId());
				if(failurehis.getTackleResult().equals("1"))
				{
					if (eventIdentify.equals("YCL"))// 已处理
					{
						msg="设备部主任已处理";
						entity.setWoStatus("5");// 状态修改为已处理
						failurehis.setApproveType("19");
					} 
					else if(eventIdentify.equals("TY"))
					{
						msg="设备部主任已同意";
						entity.setWoStatus("20");// 状态修改为总工待处理
						failurehis.setApproveType("20");
					}
				}
				else if(failurehis.getTackleResult().equals("2"))//退回
				{
					msg="设备部主任已退回";
					entity.setWoStatus("16");// 状态修改为设计部退回
					failurehis.setApproveType("16");
				}
				failurehis.setFailureCode(entity.getFailureCode());
				failurehis.setApproveTime(new Date());
				failurehis.setEnterpriseCode(entity.getEntrepriseCode());
				failurehis.setIsuse("Y");
				// add by ywliu 20100611
				PostMessage postMsg=new PostMessage();
				if (nextRoles == null || nextRoles.equals("")) {
					nextRoles = postMsg.getNextSetpRoles(entity.getWorkFlowNo().toString(), actionId);
				}
				approveremote.failureApprove(entity, failurehis, Long
						.parseLong(actionId), null, nextRoles);
				//---add by fyyang 20100524 短信通知
				if(nextRoles!=null&&!nextRoles.equals("")&&!msg.equals(""))
				{
				
			     msg="缺陷"+entity.getFailureContent()+msg+"，请您审批!";
				postMsg.sendMsg(nextRoles, msg);
				
				}
				//----------------------------------
				write("{success:true}");
			} catch (Exception e) {
				write("{success:false,errorMessage:'操作失败!'}");
			}
		} else {
			write("{success:false,errorMessage:'密码错误，请联系管理员!'}");
		}
	}

	/**
	 * 发电部待处理审批
	 */
	public void shiftawaitFailure() {
		String actionId = request.getParameter("actionId");
		String nextRoles = request.getParameter("nextRoles");
		String msg="";
		if (checkUserInfo(failurehis.getApprovePeople())) {
			try {
				EquJFailures entity = fremote.findById(failure.getId());
				if(failurehis.getTackleResult().equals("1"))//同意
				{
					msg="发电部已同意";
					entity.setWoStatus("12");// 设备部待处理审批
					failurehis.setApproveType("21");
				}
				else if(failurehis.getTackleResult().equals("2"))//退回
				{
					msg="发电部已退回";
					entity.setWoStatus("17");// 发电部待处理退回
					failurehis.setApproveType("17");
				}
				
				failurehis.setFailureCode(entity.getFailureCode());
				failurehis.setApproveTime(new Date());
				failurehis.setEnterpriseCode(entity.getEntrepriseCode());
				failurehis.setIsuse("Y");
				// add by ywliu 20100611
				PostMessage postMsg=new PostMessage();
				if (nextRoles == null || nextRoles.equals("")) {
					nextRoles = postMsg.getNextSetpRoles(entity.getWorkFlowNo().toString(), actionId);
				}
				approveremote.failureApprove(entity, failurehis, Long
						.parseLong(actionId), null, nextRoles);
				//---add by fyyang 20100524 短信通知
				if(nextRoles!=null&&!nextRoles.equals("")&&!msg.equals(""))
				{
				
			     msg="缺陷"+entity.getFailureContent()+msg+"，请您审批!";
				postMsg.sendMsg(nextRoles, msg);
				
				}
				//----------------------------------
				write("{success:true}");
			} catch (Exception e) {
				write("{success:false,errorMessage:'操作失败!'}");
			}
		} else {
			write("{success:false,errorMessage:'密码错误，请联系管理员!'}");
		}
	}

	/**
	 * 总工待处理审批
	 */
	public void chiefawaitFailure() {
		String actionId = request.getParameter("actionId");
		String nextRoles = request.getParameter("nextRoles");
		String msg="";
		if (checkUserInfo(failurehis.getApprovePeople())) {
			try {
				EquJFailures entity = fremote.findById(failure.getId());
				if(failurehis.getTackleResult().equals("1"))//同意
				{
					msg="总工已处理";
					entity.setWoStatus("5");// 已出理
					failurehis.setApproveType("22");
				}
				else if(failurehis.getTackleResult().equals("2"))//退回
				{
					msg="总工已审批退回";
					entity.setWoStatus("21");// 总工待处理退回
					failurehis.setApproveType("23");
				}
				
				failurehis.setFailureCode(entity.getFailureCode());
				failurehis.setApproveTime(new Date());
				failurehis.setEnterpriseCode(entity.getEntrepriseCode());
				failurehis.setIsuse("Y");
				// add by ywliu 20100611
				PostMessage postMsg=new PostMessage();
				if (nextRoles == null || nextRoles.equals("")) {
					nextRoles = postMsg.getNextSetpRoles(entity.getWorkFlowNo().toString(), actionId);
				}
				approveremote.failureApprove(entity, failurehis, Long
						.parseLong(actionId), null, nextRoles);
				//---add by fyyang 20100524 短信通知
				if(nextRoles!=null&&!nextRoles.equals("")&&!msg.equals(""))
				{
				
			     msg="缺陷"+entity.getFailureContent()+msg+"，请您审批!";
				postMsg.sendMsg(nextRoles, msg);
				
				}
				write("{success:true}");
			} catch (Exception e) {
				write("{success:false,errorMessage:'操作失败!'}");
			}
		} else {
			write("{success:false,errorMessage:'密码错误，请联系管理员!'}");
		}
	}
	/**
	 * 点检 缺陷验收
	 */
	public void acceptanceFailure() {
		String actionId = request.getParameter("actionId");
		String nextRoles = request.getParameter("nextRoles");
		if (checkUserInfo(failurehis.getApprovePeople())) {
			try {
				EquJFailures entity = fremote.findById(failure.getId());
				// add by ywliu 20100611
				PostMessage postMsg=new PostMessage();
				if (nextRoles == null || nextRoles.equals("")) {
					if(entity.getFailuretypeCode().equals("1")||entity.getFailuretypeCode().equals("2"))
					{
					  RunCSpecialsFacadeRemote specialRemote=(RunCSpecialsFacadeRemote)factory.getFacadeRemote("RunCSpecialsFacade");
					  String specialName= specialRemote.findByCode(entity.getDominationProfession(), employee.getEnterpriseCode()).getSpecialityName()+"缺陷点检人员";
					
					  nextRoles = postMsg.getFistStepRoles(entity.getWorkFlowNo().toString(), actionId, null,
							  specialName);
					}
					else
					{
						nextRoles = postMsg.getNextSetpRoles(entity.getWorkFlowNo().toString(), actionId);
					}
					
				}
				
				approveremote.failureAcceptance(entity, failurehis, Long
						.parseLong(actionId), nextRoles);
				//---add by fyyang 20100521 短信通知
				if(nextRoles!=null&&!nextRoles.equals(""))
				{
				
				String msg="";
				if(Long.parseLong(actionId)==89)
				{
				 msg="缺陷"+entity.getFailureContent()+"已验收合格，请您审批!";
				}
				if(Long.parseLong(actionId)==85)
				{
				 msg="缺陷"+entity.getFailureCode()+"已验收退回，请您审批!";
				}
				postMsg.sendMsgByWorker(entity.getFindBy().toString(), msg);
				
				}
				//----------------------------------
				write("{success:true}");
			} catch (Exception e) {
				write("{success:false,errorMessage:'操作失败!'}");
			}
		} else {
			write("{success:false,errorMessage:'密码错误，请联系管理员!'}");
		}
	}

	/**
	 * 缺陷运行验收
	 */
	public void runacceptanceFailure() {
		String actionId = request.getParameter("actionId");
		String nextRoles = request.getParameter("nextRoles");
		if (checkUserInfo(failurehis.getApprovePeople())) {
			try {
				EquJFailures entity = fremote.findById(failure.getId());
				// add by ywliu 20100611
				PostMessage postMsg=new PostMessage();
				if((nextRoles == null || nextRoles.equals(""))&&Long.parseLong(actionId)==95) {
					nextRoles = postMsg.getNextSetpRoles(entity.getWorkFlowNo().toString(), actionId);
				}
				approveremote.failureRunAcceptance(entity, failurehis, Long
						.parseLong(actionId), nextRoles);
				//---add by fyyang 20100521 短信通知
				if(nextRoles!=null&&!nextRoles.equals("")&&Long.parseLong(actionId)==95)
				{
				
				String msg="";
				 msg="缺陷"+entity.getFailureContent()+"已验收退回，请您审批!";
				postMsg.sendMsg(nextRoles, msg);
				}
				//----------------------------------
				write("{success:true}");
			} catch (Exception e) {
				write("{success:false,errorMessage:'操作失败!'}");
			}
		} else {
			write("{success:false,errorMessage:'密码错误，请联系管理员!'}");
		}
	}

	/**
	 * 缺陷作废
	 */
	public void invalidFailure() {
		String actionId = request.getParameter("actionId");
		String nextRoles = request.getParameter("nextRoles");
		if (checkUserInfo(failurehis.getApprovePeople())) {
			try {
				EquJFailures entity = fremote.findById(failure.getId());
				entity.setWoStatus("6");// 缺陷作废
				failurehis.setFailureCode(entity.getFailureCode());
				failurehis.setApproveTime(new Date());
				failurehis.setApproveType("6");
				failurehis.setEnterpriseCode(entity.getEntrepriseCode());
				failurehis.setIsuse("Y");
				approveremote.failureApprove(entity, failurehis, Long
						.parseLong(actionId), null, nextRoles);
				write("{success:true}");
			} catch (Exception e) {
				write("{success:false,errorMessage:'操作失败!'}");
			}
		} else {
			write("{success:false,errorMessage:'密码错误，请联系管理员!'}");
		}
	}

	/**
	 * 缺陷退回
	 */
	public void sendBackFailure() {
		String actionId = request.getParameter("actionId");
		String nextRoles = request.getParameter("nextRoles");
		if (checkUserInfo(failurehis.getApprovePeople())) {
			try {
				EquJFailures entity = fremote.findById(failure.getId());
				entity.setWoStatus("10");// 缺陷退回
				failurehis.setFailureCode(entity.getFailureCode());
				failurehis.setApproveTime(new Date());
				failurehis.setApproveType("10");
				failurehis.setEnterpriseCode(entity.getEntrepriseCode());
				failurehis.setIsuse("Y");
				approveremote.failureApprove(entity, failurehis, Long
						.parseLong(actionId), null, nextRoles);
				write("{success:true}");
			} catch (Exception e) {
				write("{success:false,errorMessage:'操作失败!'}");
			}
		} else {
			write("{success:false,errorMessage:'密码错误，请联系管理员!'}");
		}
	}

	/**
	 * 根据操作类型和缺陷编号查询历史记录
	 */
	public void getApplyType() throws JSONException {
		String applyType = request.getParameter("applyType");
		EquJFailures entity = fremote.findById(failure.getId());
		EquFailuresHisInfo model = historyremote.findApplyType(entity
				.getFailureCode(), applyType, entity.getEntrepriseCode());
		write("{success:true,data:" + JSONUtil.serialize(model) + "}");
	}
	/**
	 * 根据缺陷待处理的次数
	 */
	public void getAwaitCount() throws JSONException {
		EquJFailures entity = fremote.findById(failure.getId());
		String count = historyremote.findAwaitCount(entity.getFailureCode(),  entity.getEntrepriseCode());
		write("{success:true,data:" + JSONUtil.serialize(count) + "}");
	}
	
	public void getToptipMsg()
	{
		try
		{
			String enterpriseCode = employee.getEnterpriseCode();
			status = (status == null) ? "" : status;
			List<Object[]> msg = bqremote.getToptipMsg(sdate, edate, status,
					enterpriseCode,  belongBlock, specialityCode,deptCode);
			write(JSONUtil.serialize(msg));
		}
		catch(Exception exc)
		{
			exc.printStackTrace(); 
			write("[]");
		}
	}
	//add by wpzhu 20100809----------------
	public void queryListByType()
	{
		PageObject result=new  PageObject();
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String strDate=request.getParameter("strDate");
		String endDate=request.getParameter("endDate");
		String type=request.getParameter("type");
		String queryType=request.getParameter("queryType");
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			result = bqremote.queryListByType(strDate, endDate, 
					employee.getEnterpriseCode(), start, limit,type,queryType);
		}
		try {
			write(JSONUtil.serialize(result));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	//-------------------------------------------------
	/**
	 * 缺陷查询（所有）
	 */
	public void queryListByStatus() {
		PageObject obj = new PageObject();
		String enterpriseCode = employee.getEnterpriseCode();
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String findDeptId=request.getParameter("findDept"); //add  by fyyang 20100805 加发现部门查询
		
		// add by liuyi 20100312 增加一个按月份查询
		String month = request.getParameter("month");
		if(month != null && !month.equals(""))
		{
			sdate = month + "-01";
			int y = Integer.parseInt(month.substring(0,4));
			int m = Integer.parseInt(month.substring(5,7));
			GregorianCalendar  gc = new GregorianCalendar(y,m,1);
			gc.add(Calendar.DAY_OF_MONTH, -1);
			int maxDays = gc.get(Calendar.DAY_OF_MONTH);
			edate = month + "-" + maxDays;
		}
		
		//add by bjxu 判断查询页面是否为停用部门091219
		String stop = request.getParameter("stop");
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			status = (status == null) ? "" : status;
			obj = bqremote.queryListByStatus(stop,sdate, edate, status,
					enterpriseCode, start, limit, belongBlock, specialityCode,
					deptCode,findDeptId);
		}
		try {
			write(JSONUtil.serialize(obj));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

    public void queryApproveList() throws JSONException
    {
    	List<EquFailuresHisInfo> list=historyremote.findApproveList(failureCode, employee.getEnterpriseCode());
    	write(JSONUtil.serialize(list));
    }
	public String getSpecialityCode() {
		return specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	/**
	 * 密码
	 * 
	 * @return
	 */
	public String getConfirmPwd() {
		return confirmPwd;
	}

	public void setConfirmPwd(String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public String getEdate() {
		return edate;
	}

	public void setEdate(String edate) {
		this.edate = edate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBelongBlock() {
		return belongBlock;
	}

	public void setBelongBlock(String belongBlock) {
		this.belongBlock = belongBlock;
	}

	public EquJFailures getFailure() {
		return failure;
	}

	public void setFailure(EquJFailures failure) {
		this.failure = failure;
	}

	public EquJFailureHistory getFailurehis() {
		return failurehis;
	}

	public void setFailurehis(EquJFailureHistory failurehis) {
		this.failurehis = failurehis;
	}

	public String getFailureCode() {
		return failureCode;
	}

	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	/**
	 * add by liuyi 20100312 
	 * 保存是否上报分公司
	 */
	public void saveFailContrSend()
	{
		String ids = request.getParameter("ids");
		String isSends = request.getParameter("isSends");
		if(ids != null && isSends != null)
		{
			String[] idArray = ids.split(",");
			String[] isSendArray = isSends.split(",");
			
			for(int i = 0; i < idArray.length; i++)
			{
				EquJFailures model = fremote.findById(Long.parseLong(idArray[i]));
				model.setIsSend(isSendArray[i]);
				fremote.update(model);
			}
			
		}
	}
}
