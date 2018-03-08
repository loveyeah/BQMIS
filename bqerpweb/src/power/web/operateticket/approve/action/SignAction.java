/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.operateticket.approve.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.opticket.BussiStatusEnum;
import power.ejb.opticket.OpticketApprove;
import power.ejb.opticket.RunJOpticket;
import power.ejb.opticket.RunJOpticketFacadeRemote;
import power.ejb.opticket.RunJOpticketstep;
import power.ejb.opticket.RunJOpticketstepFacadeRemote;
import power.ejb.opticket.bussiness.RunJOpFinwork;
import power.ejb.opticket.bussiness.RunJOpFinworkFacadeRemote;
import power.ejb.opticket.bussiness.RunJOpMeasures;
import power.ejb.opticket.bussiness.RunJOpMeasuresFacadeRemote;
import power.ejb.opticket.bussiness.RunJOpStepcheck;
import power.ejb.opticket.bussiness.RunJOpStepcheckFacadeRemote;
import power.ejb.webservice.run.ticketmanage.WsJTaskFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import service.ProductServiceManager;
import service.ProductServiceManagerImpl;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.engineassistant.po.WorkflowEvent;

/**
 * 操作票审批Action
 * 
 * @author 黄维杰
 */
public class SignAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 执行状态 1：已执行；0：未执行 */
	private static final String YES = "1";
	private static final String NO = "0";
	/** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
	private static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd HH:mm:ss";
	/** 审批方式remote */
	OpticketApprove approveRemote;
	/** 操作项目信息remote */
	RunJOpticketstepFacadeRemote stepRemote;
	/** 操作票remote */
	RunJOpticketFacadeRemote opticketRemote;
	RunJOpFinworkFacadeRemote workremote;
	RunJOpStepcheckFacadeRemote checkremote;
	RunJOpMeasuresFacadeRemote dangerremote;
	private ProductServiceManager productManager;// add by ywliu
	private WsJTaskFacadeRemote taskRemote;// add by ywliu 090513
	private BussiStatusEnum bussiStatus;
	/** 操作票编码 */
	private String opticketCode;
	/** 工号 */
	private String workerCode;
	/** 动作ID */
	private Long actionId;
	/** 审批意见 */
	private String approveText;
	/** 指定下一步角色或人员 */
	private String appointNextRoleOrNextPeople;
	/** 事件标识 */
	private String eventIdentify;
	/** 操作项目id */
	private Long operateTaskId;
	/** 操作票实体 */
	private RunJOpticket ticketEntity;
	/** 操作步骤实体 */
	private RunJOpticketstep stepEntity;
	/** 批量操作的操作项目 */
	private String stepNames;
	/** 批量操作的完成时间 */
	private String finishTimes;
	/** 批量操作的项目执行情况 */
	private String execStatuss;
	/** 批量删除时的操作步骤id */
	private String ids;

	public SignAction() {
		// 操作票审批remote
		approveRemote = (OpticketApprove) factory
				.getFacadeRemote("OpticketApproveImpl");
		stepRemote = (RunJOpticketstepFacadeRemote) factory
				.getFacadeRemote("RunJOpticketstepFacade");
		opticketRemote = (RunJOpticketFacadeRemote) factory
				.getFacadeRemote("RunJOpticketFacade");
		workremote = (RunJOpFinworkFacadeRemote) factory
				.getFacadeRemote("RunJOpFinworkFacade");
		checkremote = (RunJOpStepcheckFacadeRemote) factory
				.getFacadeRemote("RunJOpStepcheckFacade");
		dangerremote = (RunJOpMeasuresFacadeRemote) factory
				.getFacadeRemote("RunJOpMeasuresFacade");
		taskRemote = (WsJTaskFacadeRemote) factory
				.getFacadeRemote("WsJTaskFacade");// add by ywliu 090513
	}

	/**
	 * 获得审批方式
	 * 
	 * @throws JSONException
	 * @throws Exception
	 */
	public void getApproveType() throws JSONException, Exception {
		if (opticketCode == null) {
			opticketCode = "";
		}
		// 查找
		List<WorkflowEvent> actions = approveRemote
				.findActionList(opticketCode);
		// 返回
		write(JSONUtil.serialize(actions));
	}

	/**
	 * 监护人审批
	 * 
	 * @throws Exception
	 */
	public void watcherSign() throws Exception {
		approveRemote.watcherSign(opticketCode, workerCode, actionId,
				approveText, eventIdentify, appointNextRoleOrNextPeople);
		write(Constants.SIGN_SUCCESS);
	}

	/**
	 * 值班负责人人审批
	 * 
	 * @throws Exception
	 */
	public void chargerSign() throws Exception {
		approveRemote.chargSign(opticketCode, workerCode, actionId,
				approveText, eventIdentify, appointNextRoleOrNextPeople);
		write(Constants.SIGN_SUCCESS);
	}

	/**
	 * 值长审批
	 * 
	 * @throws Exception
	 */
	public void dutySign() throws Exception {
		approveRemote.dutySign(opticketCode, workerCode, actionId, approveText,
				eventIdentify, appointNextRoleOrNextPeople);
		write(Constants.SIGN_SUCCESS);
	}

	/**
	 * 终结
	 * 
	 * @throws Exception
	 */
	public void endSign() throws Exception {
		String planStartTime = request.getParameter("planStartTime");
		String planEndTime = request.getParameter("planEndTime");
		String completeTime = request.getParameter("completeTime");
		String aftTime = request.getParameter("aftTime");
		approveRemote.endOpticket(opticketCode, workerCode, actionId,
				approveText, eventIdentify, planStartTime, planEndTime,
				completeTime, aftTime);
		write(Constants.SIGN_SUCCESS);
		RunJOpticket model = opticketRemote.findById(opticketCode);
		// add by ywliu
		if (eventIdentify.equals("ZJ")) {
			productManager = new ProductServiceManagerImpl();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String wsAddr = request.getSession().getServletContext()
					.getInitParameter("productWebServiceAddr");
			productManager.DisuseTicket(wsAddr, employee.getEnterpriseCode(),
					employee.getWorkerCode(), model.getApplyNo(), planEndTime);
		} else {
			productManager = new ProductServiceManagerImpl();
			String wsAddr = request.getSession().getServletContext()
					.getInitParameter("productWebServiceAddr");
			productManager.BeforeDestroyTask(wsAddr, employee
					.getEnterpriseCode(), employee.getWorkerCode(), model
					.getApplyNo(), "1", approveText);
			// add by ywliu 090513
			taskRemote.delete(model.getApplyNo());
		}
	}

	/**
	 * 查找操作项目
	 * 
	 * @throws JSONException
	 */
	public void safeDeptSign() {
		approveRemote.safeDeptSign(opticketCode, workerCode, actionId,
				approveText, eventIdentify, appointNextRoleOrNextPeople);
		write(Constants.SIGN_SUCCESS);
	}

	public void engineerSign() {
		approveRemote.engineerSign(opticketCode, workerCode, actionId,
				approveText);
		write(Constants.SIGN_SUCCESS);
	}

	public void headSign() {
		approveRemote.headSign(opticketCode, workerCode, actionId, approveText,
				eventIdentify, appointNextRoleOrNextPeople);
		write(Constants.SIGN_SUCCESS);
	}

	public void batchApprove() {
		String opticketStatus = request.getParameter("opticketStatus");
		String oCode = request.getParameter("oCode");
		String[] opCode = oCode.split(",");
		for (int i = 0; i < opCode.length; i++) {
			String code=opCode[i];
			if (opticketStatus.equals(bussiStatus.REPORT_STATUS.getValue())) {
				approveRemote
						.headSign(code, workerCode, actionId,
								approveText, eventIdentify,
								appointNextRoleOrNextPeople);
			} else if (opticketStatus.equals(bussiStatus.HEAD_APPROVE_STATUS
					.getValue())) {
				approveRemote
						.safeDeptSign(code, workerCode, actionId,
								approveText, eventIdentify,
								appointNextRoleOrNextPeople);
			} else {
				approveRemote.engineerSign(code, workerCode, actionId,
						approveText);
			}
		}
		write(Constants.SIGN_SUCCESS);
	}

	public void getStepList() throws JSONException {

		PageObject pobjInfo = new PageObject();
		// 查询工作条件信息列表
		if (opticketCode != null) {
			List<RunJOpticketstep> list = stepRemote
					.findByOperateCode(opticketCode);
			// 由操作项目号查找操作项目
			pobjInfo.setList(list);
			// 记录总数
			pobjInfo.setTotalCount(new Long(pobjInfo.getList().size()));
			// 序列化为JSON对象的字符串形式
			String strInfo = JSONUtil.serialize(pobjInfo);
			// 以HTML方式输出字符串
			write(strInfo);
		}
	}

	public void getCheckStepList() throws JSONException {
		List<RunJOpStepcheck> list = checkremote
				.findByOpticketCode(opticketCode);
		StringBuffer sb = new StringBuffer();
		sb.append("");
		sb.append(JSONUtil.serialize(list));
		write("{\"list\":" + sb.toString() + ",\"total\":" + 0 + "}");
	}

	public void getFinishWorkList() throws JSONException {
		List<RunJOpFinwork> list = workremote.findByOpticketCode(opticketCode);
		StringBuffer sb = new StringBuffer();
		sb.append("");
		sb.append(JSONUtil.serialize(list));
		write("{\"list\":" + sb.toString() + ",\"total\":" + 0 + "}");
	}

	public void getDangerousList() throws JSONException {
		List<RunJOpMeasures> list = dangerremote
				.findByOpticketCode(opticketCode);
		StringBuffer sb = new StringBuffer();
		sb.append("");
		sb.append(JSONUtil.serialize(list));
		write("{\"list\":" + sb.toString() + ",\"total\":" + 0 + "}");
	}

	/**
	 * 修改操作项目
	 * 
	 * @throws ParseException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateStepList() throws ParseException, Exception {
		String strSaveInfo = request.getParameter("saveInfo");
		Object object = JSONUtil.deserialize(strSaveInfo);

		if (object != null) {
			if (List.class.isAssignableFrom(object.getClass())) {
				// 如果是数组
				List lst = (List) object;
				int intLen = lst.size();
				for (int intCnt = 0; intCnt < intLen; intCnt++) {
					// 保存操作项目
					updateOpticketstep((Map) lst.get(intCnt), stepRemote);
				}
			} else {
				// 保存操作项目
				updateOpticketstep((Map) object, stepRemote);
			}
		}
		write(Constants.MODIFY_SUCCESS);
	}

	/**
	 * 保存操作项目
	 * 
	 * @param argMap
	 *            操作项目
	 * @param argRemote
	 *            操作项目处理远程对象
	 */
	@SuppressWarnings("unchecked")
	private void updateOpticketstep(Map argMap,
			RunJOpticketstepFacadeRemote argRemote) {
		// 操作项目ID
		String strOperateStepId = argMap.get("operateStepId").toString();

		// 通过操作项目ID查找操作项目
		RunJOpticketstep model = argRemote.findById(Long
				.parseLong(strOperateStepId));
		// 设置操作项目
		model.setOperateStepName((String) argMap.get("operateStepName"));
		// 设置完成时间
		model.setFinishTime(formatStringToDate((String) argMap
				.get("finishTime"), DATE_FORMAT_YYYYMMDD));
		// 设置执行情况
		model.setExecStatus(getStrFromBoolStr(argMap.get("execStatus")));
		// 设置修改人
		// model.setModifyBy(employee.getWorkerCode());//;"999999"

		// 保存操作项目
		argRemote.update(model);
	}

	/**
	 * 转换布尔型字符串
	 * 
	 * @param argBool
	 *            布尔型
	 * @return 字符串<br/> "true": "1"<br/> "false": "0"
	 */
	private String getStrFromBoolStr(Object argBool) {
		if (argBool == null) {
			return "0";
		}

		if (argBool instanceof Boolean) {
			return ((Boolean) argBool).booleanValue() ? "1" : "0";
		}

		return argBool.toString();
	}

	/**
	 * 删除操作项目信息
	 * 
	 * @throws Exception
	 */
	public void deleteStepList() throws Exception {
		if (opticketCode == null) {
			opticketCode = "";
		}
		// 把传过来的批量数据分开
		String[] operateStepIds = ids.split(",");
		for (int i = 0; i < operateStepIds.length; i++) {
			// 查找
			Long id = Long.parseLong(operateStepIds[i]);
			RunJOpticketstep step = stepRemote.findById(id);
			// 删除该条记录
			stepRemote.delete(step);
		}
		write(Constants.DELETE_SUCCESS);
	}

	/**
	 * 根据日期日期字符串和形式返回日期
	 * 
	 * @param argDateStr
	 *            日期字符串
	 * @param argFormat
	 *            日期形式字符串
	 * @return 日期
	 */
	private Date formatStringToDate(String argDateStr, String argFormat) {
		if (argDateStr == null || argDateStr.trim().length() < 1) {
			return null;
		}

		// 日期形式
		SimpleDateFormat sdfFrom = null;
		// 返回日期
		Date result = null;

		try {
			sdfFrom = new SimpleDateFormat(argFormat);
			// 格式化日期
			result = sdfFrom.parse(argDateStr);
		} catch (Exception e) {
			result = null;
		} finally {
			sdfFrom = null;
		}

		return result;
	}

	/**
	 * 根据日期和形式返回日期字符串
	 * 
	 * @param argDate
	 *            日期
	 * @param argFormat
	 *            日期形式字符串
	 * @return 日期字符串
	 */
	private String formatDate(Date argDate, String argFormat) {
		if (argDate == null) {
			return "";
		}

		// 日期形式
		SimpleDateFormat sdfFrom = null;
		// 返回字符串
		String strResult = null;

		try {
			sdfFrom = new SimpleDateFormat(argFormat);
			// 格式化日期
			strResult = sdfFrom.format(argDate).toString();
		} catch (Exception e) {
			strResult = "";
		} finally {
			sdfFrom = null;
		}

		return strResult;
	}

	public void createTicket() {
		String opticketCode = request.getParameter("opticketCode");
		RunJOpticket model = opticketRemote.findById(opticketCode);
		if (model.getApplyNo() != null && !model.getApplyNo().equals("")) {
			productManager = new ProductServiceManagerImpl();
			String wsAddr = request.getSession().getServletContext()
					.getInitParameter("productWebServiceAddr");
			String isCreatebyStand;
			// 判断是否调用标准票
			if("B".equals(opticketCode.substring(opticketCode.length()-1, opticketCode.length()))) {
				isCreatebyStand = "Y";
			} else {
				isCreatebyStand = "N";
			}
			// 两票系统创建（开票或重开票）一张票，修改系统中所对应的任务信息
			productManager.CreateTicket(wsAddr, employee.getEnterpriseCode(),
					employee.getWorkerCode(), model.getApplyNo(), opticketCode,
					isCreatebyStand, "0", model.getOpticketType(), model
							.getOpticketName(), "WatchTypeID", "WatchTypeName",
					"", "");
		}
		write("{success : true}");

	}

	/**
	 * 返回非NULL字符串
	 * 
	 * @param argObj
	 *            对象
	 * @return 非NULL字符串
	 */
	private String getEmptyString(Object argObj) {
		return argObj == null ? "" : argObj.toString();
	}

	/**
	 * @return the opticketCode
	 */
	public String getOpticketCode() {
		return opticketCode;
	}

	/**
	 * @param opticketCode
	 *            the opticketCode to set
	 */
	public void setOpticketCode(String opticketCode) {
		this.opticketCode = opticketCode;
	}

	/**
	 * @return the workerCode
	 */
	public String getWorkerCode() {
		return workerCode;
	}

	/**
	 * @param workerCode
	 *            the workerCode to set
	 */
	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

	/**
	 * @return the actionId
	 */
	public Long getActionId() {
		return actionId;
	}

	/**
	 * @param actionId
	 *            the actionId to set
	 */
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}

	/**
	 * @return the approveText
	 */
	public String getApproveText() {
		return approveText;
	}

	/**
	 * @param approveText
	 *            the approveText to set
	 */
	public void setApproveText(String approveText) {
		this.approveText = approveText;
	}

	/**
	 * @return the appointNextRoleOrNextPeople
	 */
	public String getAppointNextRoleOrNextPeople() {
		return appointNextRoleOrNextPeople;
	}

	/**
	 * @param appointNextRoleOrNextPeople
	 *            the appointNextRoleOrNextPeople to set
	 */
	public void setAppointNextRoleOrNextPeople(
			String appointNextRoleOrNextPeople) {
		this.appointNextRoleOrNextPeople = appointNextRoleOrNextPeople;
	}

	/**
	 * @return the operateTaskId
	 */
	public Long getOperateTaskId() {
		return operateTaskId;
	}

	/**
	 * @param operateTaskId
	 *            the operateTaskId to set
	 */
	public void setOperateTaskId(Long operateTaskId) {
		this.operateTaskId = operateTaskId;
	}

	/**
	 * @return the stepNames
	 */
	public String getStepNames() {
		return stepNames;
	}

	/**
	 * @param stepNames
	 *            the stepNames to set
	 */
	public void setStepNames(String stepNames) {
		this.stepNames = stepNames;
	}

	/**
	 * @return the finishTimes
	 */
	public String getFinishTimes() {
		return finishTimes;
	}

	/**
	 * @param finishTimes
	 *            the finishTimes to set
	 */
	public void setFinishTimes(String finishTimes) {
		this.finishTimes = finishTimes;
	}

	/**
	 * @return the execStatuss
	 */
	public String getExecStatuss() {
		return execStatuss;
	}

	/**
	 * @param execStatuss
	 *            the execStatuss to set
	 */
	public void setExecStatuss(String execStatuss) {
		this.execStatuss = execStatuss;
	}

	/**
	 * @return the ticketEntity
	 */
	public RunJOpticket getTicketEntity() {
		return ticketEntity;
	}

	/**
	 * @param ticketEntity
	 *            the ticketEntity to set
	 */
	public void setTicketEntity(RunJOpticket ticketEntity) {
		this.ticketEntity = ticketEntity;
	}

	/**
	 * @return the stepEntity
	 */
	public RunJOpticketstep getStepEntity() {
		return stepEntity;
	}

	/**
	 * @param stepEntity
	 *            the stepEntity to set
	 */
	public void setStepEntity(RunJOpticketstep stepEntity) {
		this.stepEntity = stepEntity;
	}

	/**
	 * @return the ids
	 */
	public String getIds() {
		return ids;
	}

	/**
	 * @param ids
	 *            the ids to set
	 */
	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getEventIdentify() {
		return eventIdentify;
	}

	public void setEventIdentify(String eventIdentify) {
		this.eventIdentify = eventIdentify;
	}
}