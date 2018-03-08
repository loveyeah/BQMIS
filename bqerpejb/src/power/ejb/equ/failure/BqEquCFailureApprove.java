package power.ejb.equ.failure;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import com.opensymphony.engineassistant.po.WorkflowActivity;
import com.opensymphony.engineassistant.po.WorkflowEvent;

@Remote
public interface BqEquCFailureApprove {
	
	/**
	 * 缺陷上报
	 * @param failureId
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 */
	public void failureReport(EquJFailures entity,String workflowType, Long actionId, String approveText, String nextRoles,String eventIdentify);
	/**
	 * 缺陷审批
	 * @param failure
	 * @param failurehis
	 * @param actionId
	 * @param m
	 * @param nextRoles
	 */
	public void failureApprove(EquJFailures failure,EquJFailureHistory failurehis,Long actionId,Map m,String nextRoles);
	/**
	 * 缺陷运行验收
	 * @param entity
	 * @param failurehis
	 * @param actionId
	 * @param nextRoles
	 */
	public void failureRunAcceptance(EquJFailures entity,EquJFailureHistory failurehis,Long actionId,String nextRoles);
	/**
	 * 缺陷点检验收
	 * @param entity
	 * @param failurehis
	 * @param actionId
	 * @param nextRoles
	 */
	public void failureAcceptance(EquJFailures entity,EquJFailureHistory failurehis,Long actionId,String nextRoles);
	/**
	 * 获得审批方式列表
	 * 
	 * @param entityId 缺陷单
	 * @return List<WorkflowEvent>
	 */
	public List<WorkflowEvent> findActionList(Long entityId);
	
	/**
	 * 获得需要审批的相关URL
	 * @param entryId
	 * @param workerCode
	 * @return
	 */
	public List<WorkflowActivity> getCurrentStepsInfo(Long entryId,String workerCode);
}
