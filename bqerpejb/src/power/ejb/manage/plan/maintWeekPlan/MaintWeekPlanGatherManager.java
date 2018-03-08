package power.ejb.manage.plan.maintWeekPlan;

import java.text.ParseException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

@Remote
public interface MaintWeekPlanGatherManager {

	/**
	 * 通过汇总Id获取汇总信息
	 * @param gatherId
	 * @return
	 */
	public BpJPlanRepairGather findByGatherId(Long gatherId);
	
	public List<BpJPlanRepairGatherDetail> findDetailByGatherId(Long gatherId);

	/**
	 * 获取汇总信息查询列表信息
	 * @param approve
	 * @param entryIds
	 * @param stratDate
	 * @param endDate
	 * @param content
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 * @throws Exception
	 */
	
	/**
	 * 通过汇总明细Id获取汇总明细信息
	 */
	public BpJPlanRepairGatherDetail findByGatherDetailId(Long planDetailId);
	
	public PageObject findRepairGatherList(String approve, String entryIds,
			String planTime,String planWeek, String content,
			String enterpriseCode, int... rowStartIdxAndCount) throws Exception;
	
	/**
	 * 批量增加、修改和删除汇总明细表信息
	 * @param addList
	 * @param updateList
	 * @param deleteId
	 * @param planTime
	 * @param enterpriseCode
	 * @throws ParseException
	 * @throws CodeRepeatException
	 */
	public void save(List<BpJPlanRepairGatherDetail> addList,
			List<BpJPlanRepairGatherDetail> updateList, String deleteId,
			String planTime,String planWeek,String gatherDept,String weekStartTime,String weekEndTime,String totalMemo, String workerCode, String enterpriseCode)
			throws ParseException, CodeRepeatException;
	
	/**
	 * 批量删除汇总明细表信息
	 * @param ids
	 * @return
	 */
	public BpJPlanRepairGatherDetail delRepairGatherDetail(String ids);
	
	/**
	 * 汇总上报
	 * @param gatherId
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param workflowType
	 */
	public void RepairGatherReport(Long gatherId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String workflowType);
	
	/**
	 * 汇总审批
	 * @param gatherId
	 * @param actionId
	 * @param entryId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 */
	public void RepairGatherApprove(Long gatherId,Long actionId,Long entryId, String workerCode,
			String approveText, String nextRoles);
	
	/**
	 * 获取周检修计划审批查询列表信息
	 * @param status
	 * @param entryIds
	 * @param planTime
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 * @throws Exception
	 */
	public PageObject getRepairApproveList(String status, String entryIds,
			String planTime,String planWeek,String enterpriseCode, int... rowStartIdxAndCount)
			throws Exception;
	/**
	 * 获取审批中最大月份下最大周的时间值
	 * @param enterpriseCode
	 * @return
	 */
	String getRepairGatherApproveList(String enterpriseCode);
	
	/**
	 * 判断审批中查询页面时间的汇总Id是否存在
	 * @param planTime
	 * @param planWeek
	 * @param enterpriseCode
	 * @return
	 */
	public String getRepairApproveGatherId(String planTime,String planWeek,String enterpriseCode);
}
