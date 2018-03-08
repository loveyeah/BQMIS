package power.ejb.equ.planrepair;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquJPlanRepairMainFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJPlanRepairMainFacadeRemote {

	public EquJPlanRepairMain save(EquJPlanRepairMain entity);

	
	public void delete(EquJPlanRepairMain entity);

	
	public EquJPlanRepairMain update(EquJPlanRepairMain entity);

	public EquJPlanRepairMain findById(Long id);

	/**
	 * 新增判断编号重复
	 * 
	 * @param repairCode
	 * @return
	 */
	public boolean judgeAddPlanCode(String repairCode);

	/**
	 * 修改判断编号重复
	 * 
	 * @param repairId
	 * @param repairCode
	 * @return
	 */
	public boolean judgeUpdatePlanCode(String repairId, String repairCode);

	/**
	 * 计划登记列表
	 * 
	 * @param startDate
	 * @param endDate
	 * @param rowStartIdxAndCount
	 * @returnString
	 */
	public PageObject getPlanRepairList(String startDate, String endDate,
			int... rowStartIdxAndCount);

	/**
	 * 计划检修上报
	 * 
	 * @param repairId
	 * @param nextRoles
	 * @param workercode
	 * @param approveText
	 */
	public void reportPlanRepair(Long repairId, String nextRoles,
			String workercode, Long actionId,String approveText);
	
	/**
	 * 计划检修审批列表
	 * add by fyyang 090925
	 * @param startDate
	 * @param endDate
	 * @param entryId
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findRepairApproveList(String startDate, String endDate,String entryId,
			int... rowStartIdxAndCount);
	/**
	 * 计划检修审批签字 
	 * add by fyyang 090925
	 * @param repairId
	 * @param approveText
	 * @param workerCode
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @param eventIdentify
	 */
	public void planRepairSign(Long repairId, String approveText,
			String workerCode, Long actionId, String responseDate,
			String nextRoles, String eventIdentify);
/**
 * 计划检修综合查询
 * @param startDate
 * @param endDate
 * @param rowStartIdxAndCount
 */
	public PageObject getAllPlanRepairList(String startDate, String endDate,
			int... rowStartIdxAndCount);
}