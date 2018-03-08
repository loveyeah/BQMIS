package power.ejb.run.repair;

import java.util.List;

import javax.ejb.Remote;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.run.repair.form.RepairAcceptForm;

@Remote
public interface RunJRepairProjectAcceptFacadeRemote {
	public RunJRepairProjectAccept save(RunJRepairProjectAccept entity);

	public void delete(RunJRepairProjectAccept entity);

	public RunJRepairProjectAccept update(RunJRepairProjectAccept entity);

	public RunJRepairProjectAccept findById(Long id);

	/**
	 * 项目明细记录选择
	 * 
	 * @param workflowStatus
	 * @param enterPriseCode
	 * @throws year
	 * @throws edition
	 */
	public PageObject getRepairItemList(String workflowStatus,
			String enterPriseCode, String year, String repairType,
			String edition, final int... rowStartIdxAndCount);

	/**
	 * 验收明细记录查询
	 * 
	 * @param workflowStatus
	 * @param enterPriseCode
	 * @throws workerCode
	 * @throws year
	 */
	public PageObject getRepairAcceptList(String workflowStatus,
			String enterPriseCode, String workerCode, String year,
			String projectName, String startDate, String endDate,
			String isFillBy,String entryIds, String fillSdate,String 
			fillEdate,final int... rowStartIdxAndCount);

	/**
	 * 检修项目验收审批 add by drdu 20100524
	 * 
	 * @param acceptId
	 * @param actionId
	 * @param entryId
	 * @param eventIdentify
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 */
	public void repairAcceptApprove(Long acceptId, Long actionId, Long entryId,
			String eventIdentify, String workerCode, String approveText,
			String nextRoles);

	/**
	 * 检修项目验收上报 add by ltong 20100525
	 * 
	 * @param protectNo
	 * @param workflowType
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param eventIdentify
	 * @param approveText
	 */
	public void reportTo(String protectNo, String workflowType,
			String workerCode, Long actionId, String approveText,
			String nextRoles, String nextRolePs,String eventIdentify, String enterpriseCode);
	
	/**
	 * 报表取值
	 * @param acceptId
	 * @return
	 */
	public List<RepairAcceptForm> getRepairAcceptBase(String acceptId);
	
	public List<ConApproveForm>	 getRepairApprove(Long acceptId);
}