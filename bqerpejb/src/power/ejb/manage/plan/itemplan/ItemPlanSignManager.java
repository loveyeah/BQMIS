package power.ejb.manage.plan.itemplan;

import javax.ejb.Remote;

@Remote
public interface ItemPlanSignManager {

	/**
	 * 全厂指标计划上报
	 * 
	 * @param mainId
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param workflowType
	 */
	public void wholeItemPlanReport(Long mainId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String workflowType);

	/**
	 * 全厂指标计划审批
	 * 
	 * @param mainId
	 * @param actionId
	 * @param entryId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 */
	public void wholeItemPlanApprove(Long mainId, Long actionId, Long entryId,
			String workerCode, String approveText, String nextRoles);

	/**
	 * 各部门指标计划上报
	 * 
	 * @param mainId
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param workflowType
	 */
	public void partItemPlanReport(Long mainId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String workflowType);

	/**
	 * 各部门指标计划审批
	 * 
	 * @param mainId
	 * @param actionId
	 * @param entryId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 */
	public void partItemPlanApprove(Long mainId, Long actionId, Long entryId,
			String workerCode, String approveText, String nextRoles);

	/**
	 * 全厂指标计划完成情况上报
	 * 
	 * @param mainId
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param workflowType
	 */
	public void wholeItemFactReport(Long mainId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String workflowType);

	/**
	 * 全厂指标计划完成情况审批
	 * 
	 * @param mainId
	 * @param actionId
	 * @param entryId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 */
	public void wholeItemFactApprove(Long mainId, Long actionId, Long entryId,
			String workerCode, String approveText, String nextRoles);

	/**
	 * 部门指标计划完成情况上报
	 * 
	 * @param mainId
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param workflowType
	 */
	public void partItemFactReport(Long mainId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String workflowType);

	/**
	 * 部门指标计划完成情况审批
	 * 
	 * @param mainId
	 * @param actionId
	 * @param entryId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 */
	public void partItemFactApprove(Long mainId, Long actionId, Long entryId,
			String workerCode, String approveText, String nextRoles);

	/**
	 * 月度技术指标填写上报
	 * 
	 * @param mainId
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param workflowType
	 */
	public void fillTecItemReport(Long TecmainId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String workflowType);

/**
 * 月度技术指标填写审批
 * 
 * @param TecmainId
 * @param actionId
 * @param entryId
 * @param workerCode
 * @param approveText
 * @param nextRoles
 */
	public void fillTecItemApprove(Long TecmainId,Long actionId,Long entryId, String workerCode,
			String approveText, String nextRoles);
	/**
	 * 月度技术指标完成情况上报
	 * @param TecmainId
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param workflowType
	 */
	public void TecItemFinishReport(Long TecmainId, Long actionId,
		String workerCode, String approveText, String nextRoles,
		String workflowType);
	
	/**
	 * 月度技术指标完成情况审批
	 * @param TecmainId
	 * @param actionId
	 * @param entryId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 */
	public void TecItemFinishApprove(Long TecmainId,Long actionId,Long entryId, String workerCode,
			String approveText, String nextRoles);
}

