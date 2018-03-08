/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.operateticket.register.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.opticket.BussiStatusEnum;
import power.ejb.opticket.OpticketApprove;
import power.ejb.opticket.RunCOpticketTask;
import power.ejb.opticket.RunCOpticketTaskFacadeRemote;
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
import power.ejb.opticket.form.OpInfoForm;
import power.ejb.opticket.form.OpstepInfo;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 操作票登记处理Action
 * 
 * @author huyou
 * 
 */
public class OperateticketRegisterAction extends AbstractAction {

	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 日期形式字符串 yyyy-MM-dd */
	private static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd HH:mm:ss";

	/** 操作票登记对象 */
	private RunJOpticket opticket;
	/** 操作任务维护树节点ID */
	private String node;
	/** 操作项目对象 */
	private RunJOpticketstep operateStep;

	/** 上传文件 */
	// private File uploadFile;
	/**
	 * 构造函数
	 */
	public OperateticketRegisterAction() {

	}

	/**
	 * 取得操作任务维护树
	 */
	public void getOpTaskTree() {
		// 操作任务维护树处理远程接口
		RunCOpticketTaskFacadeRemote remote = (RunCOpticketTaskFacadeRemote) factory
				.getFacadeRemote("RunCOpticketTaskFacade");

		// 序列化为JSON对象的字符串形式
		String str = toJSONStr(remote.findByParentOperateTaskId(
				Constants.ENTERPRISE_CODE, Long.parseLong(node)), true);
		// 以html方式输出字符串
		write(str);
	}

	/**
	 * 取得专业数据
	 * 
	 * @throws JSONException
	 */
	public void getOpticketSpecials() throws JSONException {
		// 专业数据远程接口
		RunCSpecialsFacadeRemote remote = (RunCSpecialsFacadeRemote) factory
				.getFacadeRemote("RunCSpecialsFacade");

		// 取得专业数据
		// List<RunCSpecials> lstSpecials = remote
		// .findSpeList(Constants.ENTERPRISE_CODE);
		List<RunCSpecials> lstSpecials = remote.findByType("1", employee
				.getEnterpriseCode());
		// 添加"所有"选项
		RunCSpecials all = new RunCSpecials();
		lstSpecials.add(0, all);
		// all.setSpecialityName(Constants.ALL_SELECT);
		// all.setSpecialityCode(Constants.BLANK_STRING);
		// // 以html方式输出字符串
		write("{\"list\":" + JSONUtil.serialize(lstSpecials) + "}");
	}

	/**
	 * 增加操作票
	 */
	public void addOpticket() throws IOException {
		// 操作票登记处理远程接口
		RunJOpticketFacadeRemote remote = (RunJOpticketFacadeRemote) factory
				.getFacadeRemote("RunJOpticketFacade");
		String isStand = request.getParameter("isStand");
		String standTicketNo = request.getParameter("standTicketNo");
		String endTicketNo = request.getParameter("endTicketNo");
		if (standTicketNo != null && !"".equals(standTicketNo)) {
			// 由标准票导入
			RunJOpticket model = remote.findById(standTicketNo);
			opticket.setOperateTaskId(model.getOperateTaskId());
			opticket.setOperateTaskName(model.getOperateTaskName());
			opticket.setOpticketType(model.getOpticketType());
		} else if (endTicketNo != null && !"".equals(endTicketNo)) {
			// 由终结票生成
			RunJOpticket model = remote.findById(endTicketNo);
			opticket.setOperateTaskId(model.getOperateTaskId());
			opticket.setOperateTaskName(model.getOperateTaskName());
			opticket.setOpticketType(model.getOpticketType());
		} else {
			// 通过手填操作任务
			if (opticket.getOperateTaskId() == null) {
				opticket.setOperateTaskId(0l);
				opticket.setOpticketType(opticket.getOpticketType());
			}
		}
		// 企业编码
		opticket.setEnterpriseCode(Constants.ENTERPRISE_CODE);
		opticket.setOpticketCode(null);
		// 填写人
		opticket.setCreateBy(employee.getWorkerCode());
		// 填写时间
		opticket.setCreateDate(new Date());
		opticket.setIsStandar(isStand);
		opticket.setSpecialityCode(opticket.getSpecialityCode());
		// 一位企业编码标识
		String strEnterpriseChar = employee.getEnterpriseChar();
		RunJOpticket result = new RunJOpticket();
		// 判断是否是由标准票生产
		if (!"".equals(standTicketNo) && standTicketNo != null) {
			result = remote.saveByStandTicketNo(strEnterpriseChar, opticket,
					standTicketNo);
		} else if (endTicketNo != null && !"".equals(endTicketNo)) {
			result = remote.saveByendTicketNo(strEnterpriseChar, opticket,
					endTicketNo);
		} else {
			result = remote.save(strEnterpriseChar, opticket);
		}
		// 设置Code
		opticket.setOpticketCode(result.getOpticketCode());
		write("{success:true,msg:'增加成功！',opticketCode:'"
				+ result.getOpticketCode() + "'}");
	}

	/**
	 * 修改操作票
	 */
	public void updateOpticket() throws IOException {
		// 操作票登记处理远程接口
		RunJOpticketFacadeRemote remote = (RunJOpticketFacadeRemote) factory
				.getFacadeRemote("RunJOpticketFacade");
		// 按操作票编码查找操作票
		RunJOpticket model = remote.findById(opticket.getOpticketCode());
		// 操作任务ID
		// model.setOperateTaskId(opticket.getOperateTaskId());
		// 操作任务
		// model.setOperateTaskName(opticket.getOperateTaskName());
		// 专业
		// model.setSpecialityCode(opticket.getSpecialityCode());
		// 附件地址
		// model.setAppendixAddr(opticket.getAppendixAddr());
		// 开始日期
		// model.setPlanStartTime(opticket.getPlanStartTime());
		// // 结束日期
		// model.setPlanEndTime(opticket.getPlanEndTime());
		// 备注
		model.setMemo(opticket.getMemo());
		// 填写人
		model.setCreateBy(employee.getWorkerCode());
		// 填写时间
		model.setCreateDate(new Date());
		model.setWorkTicketNo(opticket.getWorkTicketNo());
		model.setIsSingle(opticket.getIsSingle());
		model.setOpticketName(opticket.getOpticketName());
		// model.setIsStandar(opticket.getIsStandar());
		// 增加一条操作票记录
		remote.update(model);

		write(Constants.MODIFY_SUCCESS);
	}

	/**
	 * 通过操作票编码取得操作票
	 * 
	 * @throws JSONException
	 */
	public void getOpticketByCode() throws JSONException {
		// 操作票登记处理远程接口
		RunJOpticketFacadeRemote remote = (RunJOpticketFacadeRemote) factory
				.getFacadeRemote("RunJOpticketFacade");

		// 从请求中获得参数
		String strOpticketCode = request.getParameter("opticketCode");

		// 取得操作票
		// RunJOpticket result = remote.findById(strOpticketCode);
		OpInfoForm result = remote.findByCode(strOpticketCode);
		write("{success:true,data:" + JSONUtil.serialize(result) + "}");
	}

	/**
	 * 取得操作票类型
	 * 
	 * @throws JSONException
	 */
	public void getOpticketTypes() throws JSONException {
		// 操作任务维护树处理远程接口
		RunCOpticketTaskFacadeRemote remote = (RunCOpticketTaskFacadeRemote) factory
				.getFacadeRemote("RunCOpticketTaskFacade");

		// 序列化为JSON对象的字符串形式
		String str = toJSONStr(remote
				.findByOTaskCodeLength(Constants.ENTERPRISE_CODE), false);
		// 以html方式输出字符串
		write(str);
	}

	/**
	 * 取得上报列表
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getApproveList() throws JSONException {
		// 操作票主记录管理远程接口
		RunJOpticketFacadeRemote remote = (RunJOpticketFacadeRemote) factory
				.getFacadeRemote("RunJOpticketFacade");
		// 从请求中获得参数
		String strStartDate = request.getParameter("sdate");
		// 结束时间
		String strEndDate = request.getParameter("edate");
		// 操作票类型
		String strOpType = request.getParameter("opType");
		// 操作票专业
		String strSpecialCode = request.getParameter("specialCode");
		// 最小
		String strStart = request.getParameter("start");
		// 最大
		String strLimit = request.getParameter("limit");
		String isStandar = request.getParameter("isStandar");
		String optaskName = request.getParameter("optaskName");
		// 取得上报列表
		PageObject result = remote.getOpticketReportList(employee
				.getWorkerCode(), Constants.ENTERPRISE_CODE, strStartDate,
				strEndDate, strOpType, strSpecialCode, null, isStandar,
				optaskName, Integer.parseInt(strStart), Integer
						.parseInt(strLimit));
		if (result.getTotalCount() != null && result.getList() != null) {
			List lstOpInfo = result.getList();
			long strCount = result.getTotalCount();
			write("{\"list\":" + JSONUtil.serialize(lstOpInfo)
					+ ",\"totalCount\":" + strCount + "}");
		} else {
			write("{\"list\":[],\"totalCount\":0}");
		}

	}

	/**
	 * 删除操作票
	 */
	public void deleteOpticket() {
		// 操作票登记处理远程接口
		RunJOpticketFacadeRemote remote = (RunJOpticketFacadeRemote) factory
				.getFacadeRemote("RunJOpticketFacade");

		// 从请求中获得参数
		String strOpticketCode = request.getParameter("opticketCode");

		// 删除操作票
		remote.delete(strOpticketCode);
		write(Constants.DELETE_SUCCESS);
	}

	/**
	 * 上报操作票
	 */
	public void reportOpticket() {
		String opticketCode = request.getParameter("opticketCode");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String flowCode = request.getParameter("flowCode");
		String approveText = request.getParameter("approveText");
		String eventIdentify = request.getParameter("eventIdentify");
		String appointNextRoleOrNextPeople = request
				.getParameter("appointNextRoleOrNextPeople");
		// 上报处理远程接口
		OpticketApprove remote = (OpticketApprove) factory
				.getFacadeRemote("OpticketApproveImpl");

		// 上报操作票
		remote.reportTo(opticketCode, flowCode, workerCode, Long
				.parseLong(actionId), eventIdentify, approveText,
				appointNextRoleOrNextPeople);
		write("{success:true,msg:'&nbsp&nbsp&nbsp上报成功&nbsp&nbsp&nbsp'}");
	}

	public void batchReport() {
		String codes = request.getParameter("opticketCode");
		String[] codeArr = codes.split(",");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String flowCode = request.getParameter("flowCode");
		String approveText = request.getParameter("approveText");
		String eventIdentify = request.getParameter("eventIdentify");
		String appointNextRoleOrNextPeople = request
		.getParameter("appointNextRoleOrNextPeople");
		OpticketApprove remote = (OpticketApprove) factory
				.getFacadeRemote("OpticketApproveImpl");
		for (int i = 0; i < codeArr.length; i++) {
			String opticketCode = codeArr[i];
			remote.reportTo(opticketCode, flowCode, workerCode, Long
					.parseLong(actionId), eventIdentify, approveText, appointNextRoleOrNextPeople);
		}
		write("{success:true,msg:'&nbsp&nbsp&nbsp上报成功&nbsp&nbsp&nbsp'}");
	}

	/**
	 * 查找操作项目
	 * 
	 * @throws JSONException
	 */
	public void getOpticketsteps() throws JSONException {
		// 操作项目处理远程接口
		RunJOpticketstepFacadeRemote remote = (RunJOpticketstepFacadeRemote) factory
				.getFacadeRemote("RunJOpticketstepFacade");

		// 从请求中获得参数
		String strOpticketCode = request.getParameter("opticketCode");

		// 查找操作项目
		List<RunJOpticketstep> lstResult = remote
				.findByOperateCode(strOpticketCode);
		// List<OpstepInfo> lstResult=remote.findByOpCode(strOpticketCode);
		// 总数
		String strCount = "0";
		write("{\"list\":" + JSONUtil.serialize(lstResult) + ",\"totalCount\":"
				+ strCount + "}");
	}

	public void getFillOpSteps() throws JSONException {
		RunJOpticketstepFacadeRemote remote = (RunJOpticketstepFacadeRemote) factory
				.getFacadeRemote("RunJOpticketstepFacade");
		String strOpticketCode = request.getParameter("opticketCode");
		List<OpstepInfo> lstResult = remote.findByOpCode(strOpticketCode);
		String strCount = "0";
		write("{\"list\":" + JSONUtil.serialize(lstResult) + ",\"totalCount\":"
				+ strCount + "}");
	}

	public void getChecksteps() throws JSONException {
		RunJOpStepcheckFacadeRemote remote = (RunJOpStepcheckFacadeRemote) factory
				.getFacadeRemote("RunJOpStepcheckFacade");
		String strOpticketCode = request.getParameter("opticketCode");
		List<RunJOpStepcheck> lstResult = remote
				.findByOpticketCode(strOpticketCode);
		String strCount = "0";
		write("{\"list\":" + JSONUtil.serialize(lstResult) + ",\"totalCount\":"
				+ strCount + "}");
	}

	public void getFinWorks() throws JSONException {
		RunJOpFinworkFacadeRemote remote = (RunJOpFinworkFacadeRemote) factory
				.getFacadeRemote("RunJOpFinworkFacade");
		String strOpticketCode = request.getParameter("opticketCode");
		List<RunJOpFinwork> lstResult = remote
				.findByOpticketCode(strOpticketCode);
		String strCount = "0";
		write("{\"list\":" + JSONUtil.serialize(lstResult) + ",\"totalCount\":"
				+ strCount + "}");
	}

	public void getMeasures() throws JSONException {
		RunJOpMeasuresFacadeRemote remote = (RunJOpMeasuresFacadeRemote) factory
				.getFacadeRemote("RunJOpMeasuresFacade");
		String strOpticketCode = request.getParameter("opticketCode");
		List<RunJOpMeasures> lstResult = remote
				.findByOpticketCode(strOpticketCode);
		String strCount = "0";
		write("{\"list\":" + JSONUtil.serialize(lstResult) + ",\"totalCount\":"
				+ strCount + "}");
	}

	/**
	 * 增加操作项目
	 */
	public void addOpticketstep() {
		// 操作项目处理远程接口
		RunJOpticketstepFacadeRemote remote = (RunJOpticketstepFacadeRemote) factory
				.getFacadeRemote("RunJOpticketstepFacade");

		// 设置操作项目ID
		operateStep.setOperateStepId(null);
		// 设置最后修改人
		// operateStep.setModifyBy(employee.getWorkerCode());

		// 增加操作项目
		remote.save(operateStep);
		write(Constants.ADD_SUCCESS);
	}

	/**
	 * 删除操作项目
	 */
	public void deleteOpticketsteps() {
		// 操作项目处理远程接口
		RunJOpticketstepFacadeRemote remote = (RunJOpticketstepFacadeRemote) factory
				.getFacadeRemote("RunJOpticketstepFacade");

		// 从请求中获得删除的ID
		String ids = request.getParameter("ids");
		// 以逗号为做分隔符
		String[] strIdArrays = ids.split(",");

		for (int intCnt = 0; intCnt < strIdArrays.length; intCnt++) {
			// 删除操作项目记录
			remote.delete(remote.findById(new Long(strIdArrays[intCnt])));
		}

		write(Constants.DELETE_SUCCESS);
	}

	/**
	 * 批量保存操作项目
	 */
	@SuppressWarnings("unchecked")
	public void saveOpticketsteps() throws JSONException {
		// 操作项目处理远程接口
		RunJOpticketstepFacadeRemote remote = (RunJOpticketstepFacadeRemote) factory
				.getFacadeRemote("RunJOpticketstepFacade");

		String strSaveInfo = request.getParameter("saveInfo");
		Object object = JSONUtil.deserialize(strSaveInfo);

		if (object != null) {
			if (List.class.isAssignableFrom(object.getClass())) {
				// 如果是数组
				List lst = (List) object;
				int intLen = lst.size();
				for (int intCnt = 0; intCnt < intLen; intCnt++) {
					// 保存操作项目
					updateOpticketstep((Map) lst.get(intCnt), remote);
				}
			} else {
				// 保存操作项目
				updateOpticketstep((Map) object, remote);
			}
		}

		write(Constants.MODIFY_SUCCESS);
	}

	/**
	 * 设置操作票登记对象
	 * 
	 * @param argOpticket
	 *            操作票登记对象
	 */
	public void setOpticket(RunJOpticket argOpticket) {
		opticket = argOpticket;
	}

	/**
	 * 取得操作票登记对象
	 * 
	 * @return 操作票登记对象
	 */
	public RunJOpticket getOpticket() {
		return opticket;
	}

	/**
	 * 取得操作任务维护树节点ID
	 */
	public String getNode() {
		return node;
	}

	/**
	 * 设置操作任务维护树节点ID
	 * 
	 * @param argNode
	 *            操作任务维护树节点ID
	 */
	public void setNode(String argNode) {
		node = argNode;
	}

	/**
	 * 获取操作项目对象
	 */
	public RunJOpticketstep getOperateStep() {
		return operateStep;
	}

	/**
	 * 设置操作项目对象
	 * 
	 * @param argOperateStep
	 *            操作项目对象
	 */
	public void setOperateStep(RunJOpticketstep argOperateStep) {
		operateStep = argOperateStep;
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
		String strOperateStepId = (String) argMap.get("operateStepId");

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

		// 保存操作项目
		argRemote.update(model);
	}

	/**
	 * 将list转换为json格式数据
	 * 
	 * @param argOpTaskList
	 *            操作任务
	 * @return json格式数据
	 */
	private String toJSONStr(List<RunCOpticketTask> argOpTaskList,
			boolean argIsTask) {
		int intCodeLen = argIsTask ? 13 : 4;
		if (argOpTaskList != null && argOpTaskList.size() > 0) {
			List<TreeNode> list = new ArrayList();
			try {
				for (int i = 0; i < argOpTaskList.size(); i++) {
					if (argOpTaskList.get(i).getOperateTaskCode().length() > intCodeLen) {
						continue;
					}
					TreeNode treeNode = new TreeNode();
					treeNode.setText(argOpTaskList.get(i).getOperateTaskName());
					treeNode.setId(argOpTaskList.get(i).getOperateTaskId()
							.toString());
					treeNode.setCode(argOpTaskList.get(i).getOperateTaskCode());
					treeNode.setLeaf(argOpTaskList.get(i).getOperateTaskCode()
							.length() == 13 || argOpTaskList.get(i).getOperateTaskCode()
							.length() == 12 ? true : false);
					list.add(treeNode);
				}
				return JSONUtil.serialize(list);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "[]";

			}
		} else {
			return "[]";
		}
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
	 * 设置附件地址
	 */
	private void setFilePath() {
		// 上传附件
		String strFilePath = request.getParameter("filePath");

		// 文件扩展名
		String strExtension = "";
		int index = strFilePath.lastIndexOf(".");
		if (index > 0) {
			strExtension = strFilePath.substring(index);
		}

		// 设置附件地址
		opticket.setAppendixAddr("upload-file/opticket/"
				+ opticket.getOpticketCode() + strExtension);
	}

	/**
	 * 上传文件
	 * 
	 * @param argExtension
	 *            文件扩展名
	 * @throws IOException
	 */
	// private void uploadFile() throws IOException {
	// InputStream bis = null;
	// OutputStream bos = null;
	//
	// try {
	// bis = new BufferedInputStream(new FileInputStream(uploadFile), 1024);
	// // 上传到/powererpweb/WebRoot/upload-file/opticket中
	// // 文件名保存为操作票号
	// String desFilePath = ServletActionContext.getServletContext()
	// .getRealPath(opticket.getAppendixAddr());
	// bos = new BufferedOutputStream(new FileOutputStream(desFilePath),
	// 1024);
	//
	// byte[] data = new byte[1024];
	// while (bis.read(data) != -1) {
	// bos.write(data);
	// }
	// } finally {
	// if (bis != null) {
	// bis.close();
	// }
	// if (bos != null) {
	// bos.close();
	// }
	// }
	// }
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

	public void getStantdOpticktetList() throws JSONException {
		RunJOpticketFacadeRemote remote = (RunJOpticketFacadeRemote) factory
				.getFacadeRemote("RunJOpticketFacade");
		String opticketType = request.getParameter("opticketType");
		String specialityCodefinal = request.getParameter("specialityCode");
		String strStart = request.getParameter("start");
		String strLimit = request.getParameter("limit");
		// 只取已终结票
		String opStatus = BussiStatusEnum.ENGINEER_APPROVE_STATUS.getValue();// request.getParameter("opStatus");

		String key = request.getParameter("key");
		String newOrOld = request.getParameter("newOrOld");
		if (specialityCodefinal.equals("")) {
			specialityCodefinal = null;
		}
		PageObject pg = remote.getStantdOpticktetList(employee
				.getEnterpriseCode(), newOrOld, opticketType,
				specialityCodefinal, opStatus, key, Integer.parseInt(strStart),
				Integer.parseInt(strLimit));
		List<RunJOpticket> list = pg.getList();
		if (pg != null && pg.getTotalCount() > 0) {

			write("{\"list\":" + JSONUtil.serialize(pg.getList())
					+ ",\"totalCount\":" + pg.getTotalCount() + "}");
		} else {
			write("{\"list\":[],\"totalCount\":0}");
		}

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

	public void creatNewOptickectByNo() {
		RunJOpticketFacadeRemote remote = (RunJOpticketFacadeRemote) factory
				.getFacadeRemote("RunJOpticketFacade");
		String optickectNo = request.getParameter("optickectNo");
		RunJOpticket model = new RunJOpticket();
		RunJOpticket entity = remote.findById(optickectNo);
		model.setOperateTaskId(entity.getOperateTaskId());
		model.setOperateTaskName(entity.getOperateTaskName());
		model.setOpticketName(entity.getOpticketName());
		model.setOpticketType(entity.getOpticketType());
		model.setSpecialityCode(entity.getSpecialityCode());
		model.setIsStandar("N");
		model.setIsUse("Y");
		model.setCreateBy(employee.getWorkerCode());
		model.setEnterpriseCode(entity.getEnterpriseCode());
		String opcode = remote.createAnddelOptickect(optickectNo, employee
				.getEnterpriseChar(), model);
		write(opcode);
	}
}
