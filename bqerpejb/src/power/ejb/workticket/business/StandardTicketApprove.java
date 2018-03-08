package power.ejb.workticket.business;

import java.util.List;

import javax.ejb.Remote;

import power.ejb.workticket.form.WorkticketBusiStatus;

import com.opensymphony.engineassistant.po.WorkflowEvent;

@Remote 
public interface StandardTicketApprove {
	
	/**
	 * 所有审批状态
	 * @return
	 */
	public List<WorkticketBusiStatus> findAllBusiStatusList();
	/**
	 * 上报页面的审批状态
	 * @return
	 */
	public List<WorkticketBusiStatus> findBusiStatuForReport();
	/**
	 * 审批页面的审批状态
	 * @return
	 */
	public List<WorkticketBusiStatus> findBusiStatuForApprove();
	/**
	 * 获得审批方式列表
	 * @param workticketNo
	 * @return
	 */
	public List<WorkflowEvent> findActionList(String workticketNo);
	/**
	 * 上报
	 * @param workticketNo
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param eventIdentify
	 * @param nextRoles
	 */
	public void reportTo(String workticketNo,String workerCode,Long actionId,String approveText,String eventIdentify,String nextRoles);
	/**
	 * 安环部审批
	 * @param workticketNo
	 * @param approveText
	 * @param workerCode
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 */
	public void workticketAhDept(String workticketNo,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles );
	
	/**
	 * 总工程师审批
	 * @param workticketNo
	 * @param approveText
	 * @param workerCode
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 */
	public void workticketEngineer(String workticketNo,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles);
}
