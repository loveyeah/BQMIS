package power.ejb.run.repair;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJRepairProjectMainFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJRepairProjectMainFacadeRemote {

	public RunJRepairProjectMain save(RunJRepairProjectMain entity);

	/**
	 * 删除一条或多条记录
	 * 
	 * @param ids
	 */
	public void deleteMult(String ids);

	public RunJRepairProjectMain update(RunJRepairProjectMain entity);

	public RunJRepairProjectMain findById(Long id);

	public List<RunJRepairProjectMain> findAll(int... rowStartIdxAndCount);

	/**
	 * 根据年份，检修类别，专业，任务单查询列表记录
	 * 
	 * @param enterpriseCode
	 * @param year
	 * @param repairType
	 * @param speciality
	 * @param tastlist
	 * @param editTime
	 * @param flag
	 * @param entryIds
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findRepairList(String enterpriseCode, String year,
			String repairType, String speciality, String tastlist,
			String editTime, String flag, String entryIds,
			final int... rowStartIdxAndCount);

	/**
	 * 根据主表ID查找详细
	 * 
	 * @param mainId
	 * @return
	 */
	public Object findInfoById(Long mainId);

	/**
	 * 上报记录
	 * @param repairId
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param nextRolePs
	 * @param workflowType
	 */
	public void reportRepairRecord(Long repairId, Long actionId,
			String workerCode, String approveText, String nextRoles, 
			String nextRolePs,String workflowType);
	
	/**
	 * 检修项目审批
	 * @param projectMainId
	 * @param actionId
	 * @param entryId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param eventIdentify
	 * @param tasklistId
	 * @param enterpriseCode
	 * @return
	 */
	public RunJRepairProjectMain RepairRecordApprove(Long projectMainId, Long actionId, 
			Long entryId,String workerCode, String approveText, String nextRoles,
			String eventIdentify,String tasklistId,String specialityId,String enterpriseCode) ;
	
	/**
	 * 检修项目领导审批
	 * @param projectMainId
	 * @param actionId
	 * @param entryId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param eventIdentify
	 * @param enterpriseCode
	 * @return
	 */
	public RunJRepairProjectMain RepairRecordLeaderApprove(Long projectMainId, Long actionId, 
			Long entryId,String workerCode, String approveText, String nextRoles,
			String eventIdentify,String enterpriseCode);
	/**
	 * 通过主表ID 获得审批状态
	 * @param repairMainId
	 * @param flag
	 * @param enterpriseCode
	 * @return
	 */
	public String getRepairStatusMain(String repairMainId,String selectMainId,String flag, String enterpriseCode) ;

	/**
	 * 通过任务单ID 获得最新版本
	 * @param tasklistId
	 * @param enterpriseCode
	 * @return
	 */
	public String getRepairMaxVersionMain(String tasklistId,String enterpriseCode);
	
	
	/**
	 * 查询版本数据源
	 * @param enterpirseCode
	 * @return
	 */
	public PageObject getRepairVerisonList(String tasklistId,String enterpirseCode);

	
	/**
	 * 审批页面任务单查询列表
	 * @param entryIds
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getRepairTastListApprove(String entryIds,String enterpriseCode,
			final int... rowStartIdxAndCount);
	
	/**
	 * 审批页面根据任务单ID获得审批主表信息
	 * @param tasklist
	 * @param entryIds
	 * @param enterPriseCode
	 * @return
	 */
	public PageObject getLeaderApproveInfo(String tasklist,String entryIds,String enterPriseCode);
	
	/**
	 * 审批页面根据主表ID获得审批明细查询列表
	 * @param projectMainId
	 * @param enterPriseCode
	 * @return
	 */
	public PageObject getLeaderApproveDetail(String projectMainId,String enterPriseCode);
	
	/**
	 * 根据登录人workerCode 来判断进入的审批页面
	 * @param workerCode
	 * @return
	 */
	public String selectApproveByWorderCode(String workerCode);
	
	/**
	 * 检修计划查询列表
	 * @param tasklistId
	 * @param version
	 * @param enterPriseCode
	 * @return
	 */
	public PageObject getRepairQueryList(String tasklistId,String version ,String enterPriseCode);
	
	/**
	 * 根据专业ID和版本查找详细
	 * 
	 * @param mainId
	 * @return
	 */
	public PageObject findHistoryInfo(String spId, String version);
	public  String getsituation(String id,String enterpriseCode);
	
	/**
	 * 查询专业下的所有版本信息
	 * add by fyyang 20100624
	 * @param specialId
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject findVersionBySpecial(String specialId,String enterpriseCode);
	
	/**
	 * 查询专业下的最新版本
	 * add by fyyang 20100624
	 * @param specialId
	 * @param enterpriseCode
	 * @return
	 */
	public String findNewVersionBySpecial(String specialId,String enterpriseCode);
	
	public List getWBInfo(String projectMainId);
}