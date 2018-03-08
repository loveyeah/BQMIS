package power.ejb.resource.business;

import java.util.List;

import javax.ejb.Remote;

import power.ejb.resource.form.PlanApproveStatus;

@Remote 
public interface MrpPlanReqApprove {

	/**
	 * 查找所有状态（用于查询）
	 * @return
	 */
	public List<PlanApproveStatus> findAllPlanBusi();
	/**
	 * 查找审批页面所用的状态
	 * @return
	 */
	public List<PlanApproveStatus> findPlanBusiForApprove();
	/**
	 * 上报
	 * @param mrNo
	 * @param workflowType
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param eventIdentify
	 */
	public void reportTo(String mrNo,String workflowType,String workerCode,Long actionId,String approveText,String nextRoles,String eventIdentify,String dateMemo);
	/**
	 * 通用签字页面
	 * @param mrNo
	 * @param approveText
	 * @param workerCode
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @param eventIdentify
	 */
	public void planReqSign(String mrNo,String approveText,
			String workerCode,Long actionId,String responseDate,
			String nextRoles,String eventIdentify,String itemCode);
	
	/**
	 * 获得部门的类型 检修、发电、实业
	 * add by fyyang 090515
	 * @param deptCode
	 * @return
	 */
	public String  findDeptType(String deptCode);
}
