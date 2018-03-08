package power.ejb.manage.plan;

import java.text.ParseException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 
 * @author sychen 20100624
 *
 */
@Remote
public interface BpJPlanJobCompleteManager {

/**
 * 部门月度计划完成情况根据主表主键查询主表
 * @param id
 * @return
 */	
public BpJPlanJobCompleteMain findByMainId(Long id);
	
/**
 * 部门月度计划完成情况根据明细表主键查询明细表
 * @param id
 * @return
 */	
public BpJPlanJobCompleteDetail findByDetailId (Long id) ;	

/**
 * 部门月度计划完成情况填写页面查询列表
 * @param planTime
 * @param workerCode
 * @param enterpriseCode
 * @return
 * @throws Exception
 */
public PageObject getPlanJobCompleteList(String planTime,String workerCode,String enterpriseCode,String editDeptCode) throws Exception;

/**
 * 查询部门月度计划完成情况填写页面新增主表的主键
 * @param planTime
 * @param workerCode
 * @param enterpriseCode
 * @return
 */
public String getPlanCompleteNewMainId(String planTime, String workerCode,
		String enterpriseCode);

/**
 * 查询部门月度计划完成情况填写页面当前月份下当前登录人的主记录的工作流状态
 * @param planTime
 * @param workerCode
 * @param enterpriseCode
 * @return
 */
public String getPlanCompleteStatus(String planTime, String workerCode,
		String enterpriseCode);

/**
 * 部门月度计划完成情况填写页面主表保存方法
 * @param entity
 * @param deptCode1
 * @param WorkerCode
 * @return
 */
public BpJPlanJobCompleteMain save(BpJPlanJobCompleteMain entity, String deptCode1, String WorkerCode ) ;

/**
 * 部门月度计划完成情况填写页面主表修改方法
 * @param entity
 * @return
 */
public BpJPlanJobCompleteMain update(BpJPlanJobCompleteMain entity);

/**
 * 部门月度计划完成情况填写页面明细表列表多条数据保存方法
 * @param addList
 */
public void saveDetail(List<BpJPlanJobCompleteDetail> addList);

/**
 * 部门月度计划完成情况填写页面明细表删除多条数据方法
 * @param ids
 * @return
 */
public boolean deleteDetail(String ids) ;

/**
 * 部门月度计划完成情况填写页面明细表多条数据修改方法
 * @param updateList
 */
public void updateDetail(List<BpJPlanJobCompleteDetail> updateList);

/**
 * 部门月度计划完成情况填写页面上报方法
 * @param deptMainId
 * @param actionId
 * @param workerCode
 * @param approveText
 * @param nextRoles
 * @param nextRolePs
 * @param workflowType
 */
public void reportPlanJobComplete(Long deptMainId, Long actionId,
		String workerCode, String approveText, String nextRoles,
		String nextRolePs, String workflowType);

/**
 * 部门月度计划完成情况审批查询列表
 * @param flag
 * @param planTime
 * @param entryIds
 * @param enterpriseCode
 * @param rowStartIdxAndCount
 * @return
 */
public PageObject getPlanJobCompleteApproveList(String flag,String planTime,String entryIds,String enterpriseCode,
		String txtIfComplete, int... rowStartIdxAndCount);

/**
 * 部门月度计划完成情况主表数据
 * @param planTime
 * @param entryIds
 * @param enterpriseCode
 * @return
 */
public List getPlanJobCompleteInfo(String planTime,String entryIds,String enterpriseCode);

/**
 * 部门月度计划完成情况审批方法
 * @param deptMainId
 * @param workflowType
 * @param workerCode
 * @param actionId
 * @param eventIdentify
 * @param approveText
 * @param nextRoles
 * @param stepId
 * @param enterpriseCode
 * @return
 */
public BpJPlanJobCompleteMain planJobCompleteApprove(String deptMainId,
		String workflowType, String workerCode, String actionId,
		String eventIdentify, String approveText, String nextRoles,
		String stepId, String enterpriseCode);

/**
 * 月度计划完成情况汇总主表查询列表
 * @param flag
 * @param planTime
 * @param enterpriseCode
 * @param rowStartIdxAndCount
 * @return
 */
public PageObject getPlanJobCompleteMainList(String flag,String planTime,String enterpriseCode,
		  int... rowStartIdxAndCount);

/**
 * 月度计划完成情况汇总明细表查询列表
 * @param deptMainId
 * @param enterpriseCode
 * @param rowStartIdxAndCount
 * @return
 */
public PageObject getPlanJobCompleteDetailList(String deptMainId,String enterpriseCode,
		  int... rowStartIdxAndCount);

/**
 * 月度计划完成情况汇总已读方法
 * @param deptMainId
 */
public void modifyFinishIfRead(Long deptMainId);

/**
 * 部门月度计划完成情况查询列表
 * @param planTime
 * @param enterpriseCode
 * @param editDepcode
 * @param rowStartIdxAndCount
 * @return
 */
public PageObject getPlanJobCompleteQuestList(String planTime,String enterpriseCode,String editDepcode,
		int... rowStartIdxAndCount);

/**
 * 部门月度计划完成情况需要审批的最大月份
 * @param enterpriseCode
 * @param entryIds
 * @return
 */
public String getPlanJobCompleteMaxPlanTime(String enterpriseCode,String entryIds);
}
