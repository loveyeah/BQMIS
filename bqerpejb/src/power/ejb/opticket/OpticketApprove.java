package power.ejb.opticket;

import java.util.List;

import javax.ejb.Remote;

import power.ejb.opticket.form.CheckBaseForPrint;
import power.ejb.opticket.form.DangerousBaseForPrint;
import power.ejb.opticket.form.OpticketPrintModel;
import power.ejb.opticket.form.WorkBaseForPrint;

import com.opensymphony.engineassistant.po.WorkflowEvent;

@Remote
public interface OpticketApprove {
	/**
	 * 取得审批事件
	 * 
	 * @param opticketCode
	 * @return
	 */
	public List<WorkflowEvent> findActionList(String opticketCode);

	/**
	 * 上报
	 * 
	 * @param workflowType
	 *            工作流类型
	 * @param workerCode
	 *            审批人
	 * @param actionId
	 *            动作
	 */
	public void reportTo(String opticketCode, String flowCode,
			String workerCode, Long actionId, String eventIdentify,
			String approveText,String appointNextRoleOrNextPeople);

	/**
	 * 监护人签字
	 * 
	 * @param opticketCode
	 *            操作票编码
	 * @param workerCode
	 *            工号
	 * @param actionId
	 *            动作ID
	 * @param approveText
	 *            审批意见
	 * @param appointNextRoleOrNextPeople
	 *            指定下一步角色或人员
	 */
	public void watcherSign(String opticketCode, String workerCode,
			Long actionId, String approveText, String eventIdentify,
			String appointNextRoleOrNextPeople);

	/**
	 * 值班负责人签字
	 * 
	 * @param opticketCode操作票编号
	 * @param workerCode工号
	 * @param actionId动作ID
	 * @param approveText审批意见
	 * @param appointNextRoleOrNextPeople指定下一步角色或人员
	 */
	public void chargSign(String opticketCode, String workerCode,
			Long actionId, String approveText, String eventIdentify,
			String appointNextRoleOrNextPeople);

	/**
	 * 值长签字
	 * 
	 * @param opticketCode操作票编码
	 * @param workerCode工号
	 * @param actionId动作ID
	 * @param approveText审批意见
	 * @param appointNextRoleOrNextPeople提定下一步角色或人员
	 */
	public void dutySign(String opticketCode, String workerCode, Long actionId,
			String approveText, String eventIdentify,
			String appointNextRoleOrNextPeople);

	/**
	 * 操作票终结
	 * 
	 * @param opticketCode操作票编码
	 * @param workerCode工号
	 * @param actionId动作ID
	 * @param approveText审批意见
	 * @param planStartTime
	 *            计划开始时间
	 * @param planEndTime
	 *            计划结束时间
	 */
	public void endOpticket(String opticketCode, String workerCode,
			Long actionId, String approveText, String eventIdentify,String planStartTime,
			String planEndTime,String completeTime,String aftTime);

	/**
	 * 安环部审核
	 * 
	 * @param opticketCode操作票编码
	 * @param workerCode工号
	 * @param actionId动作ID
	 * @param approveText审批意见
	 * @param appointNextRoleOrNextPeople提定下一步角色或人员
	 */
	public void safeDeptSign(String opticketCode, String workerCode,
			Long actionId, String approveText, String eventIdentify,
			String appointNextRoleOrNextPeople);

	/**
	 * 总工程师审核
	 * 
	 * @param opticketCode操作票编码
	 * @param workerCode工号
	 * @param actionId动作ID
	 * @param approveText审批意见
	 */
	public void engineerSign(String opticketCode, String workerCode,
			Long actionId, String approveText);

	/**
	 * 部门主任审核
	 * 
	 * @param opticketCode操作票编码
	 * @param workerCode工号
	 * @param actionId动作ID
	 * @param approveText审批意见
	 */
	public void headSign(String opticketCode, String workerCode, Long actionId,
			String approveText, String eventIdentify,
			String appointNextRoleOrNextPeople);

	/**
	 * 票面数据
	 * 
	 * @param String
	 *            opticketCode操作票编码
	 */
	public OpticketPrintModel getOpticketData(String opticketCode);

	/**
	 * 电气倒闸操作前检查的项目数据
	 * 
	 * @param String
	 *            opticketCode操作票编码
	 */
	public CheckBaseForPrint getBefCheckStepData(String opticketCode);

	/**
	 * 电气倒闸操作后完成的工作数据
	 * 
	 * @param String
	 *            opticketCode操作票编码
	 */
	public WorkBaseForPrint getAftWorkData(String opticketCode);
	/**
	 * wzhyan temp 2009-4-18
	 * @param opticketCode
	 * @return
	 */
	public WorkBaseForPrint getBefWorkData(String opticketCode);

	/**
	 * 危险点控制措施数据
	 * 
	 * @param String
	 *            opticketCode操作票编码
	 */
	public DangerousBaseForPrint getDangeroursData(String opticketCode);
}
