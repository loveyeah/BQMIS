package power.ejb.manage.plan.trainplan;

import javax.ejb.Remote;

@Remote
public interface TrainPlanSignManager {


	/**
	 * 培训计划上报
	 * 
	 * @param mainId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param workflowType
	 */
	public void TrainPlanReport(Long mainId,Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType);
	
	/**
	 * 汇总上报
	 * @param mainId
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param workflowType
	 */
	public void TrainPlanGatherReport(Long gatherId,Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType);
	
	/**
	 * 培训计划审批
	 * @param mainId
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param workflowType
	 */
	public void TrainPlanApprove(Long mainId,Long actionId,Long entryId, String workerCode,
			String approveText, String nextRoles);
	
	/**
	 * 汇总审批
	 * @param gatherId
	 * @param actionId
	 * @param entryId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 */
	public void TrainPlanGatherApprove(Long gatherId,Long actionId,Long entryId, String workerCode,
			String approveText, String nextRoles);
	
	/**
	 * 培训计划回填上报
	 * add by drdu 091211
	 * @param mainId
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param workflowType
	 */
	public void TrainPlanBackReport(Long mainId, Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType);
	
	/**
	 * 培训计划回填审批
	 * @param mainId
	 * @param actionId
	 * @param entryId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 */
	public void TrainPlanBackApprove(Long mainId, Long actionId, Long entryId,
			String workerCode, String approveText, String nextRoles);
	
	/**
	 * 培训计划回填汇总上报
	 * add by drdu 091212
	 * @param mainId
	 * @param approveId
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param workflowType
	 * @param enterpriseCode
	 */
	public void TrainPlanBackGatherReport(Long approveId, Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType);
	
	/**
	 * 培训计划回填汇总审批
	 * add by drdu 091215
	 * @param approvalId
	 * @param actionId
	 * @param entryId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 */
	public void trainPlanBackGatherApprove(Long approvalId,Long actionId,Long entryId, String workerCode,
			String approveText, String nextRoles);
}
