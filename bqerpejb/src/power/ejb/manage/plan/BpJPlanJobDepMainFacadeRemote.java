package power.ejb.manage.plan;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCDept;
import power.ejb.manage.plan.form.BpJPlanJobDepMainForm;

/**
 * Remote interface for BpJPlanJobDepMainFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpJPlanJobDepMainFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpJPlanJobDepMain entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpJPlanJobDepMain entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
//	public BpJPlanJobDepMain save(BpJPlanJobDepMain entity,String deptCode);
	//update by sychen 20100408
	public BpJPlanJobDepMain save(BpJPlanJobDepMain entity, String deptCode1,String flag,
			String workflowType,String WorkerCode,String signStatus);
	/**
	 * Delete a persistent BpJPlanJobDepMain entity.
	 * 
	 * @param entity
	 *            BpJPlanJobDepMain entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJPlanJobDepMain entity);

	/**
	 * Persist a previously saved BpJPlanJobDepMain entity and return it or a
	 * copy of it to the sender. A copy of the BpJPlanJobDepMain entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanJobDepMain entity to update
	 * @return BpJPlanJobDepMain the persisted BpJPlanJobDepMain entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanJobDepMain update(BpJPlanJobDepMain entity);

	public BpJPlanJobDepMain findById(Long id);

	/**
	 * Find all BpJPlanJobDepMain entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJPlanJobDepMain property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJPlanJobDepMain> found by query
	 */
	public List<BpJPlanJobDepMain> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all BpJPlanJobDepMain entities.
	 * 
	 * @return List<BpJPlanJobDepMain> all BpJPlanJobDepMain entities
	 */
	public List<BpJPlanJobDepMain> findAll();

//	public BpJPlanJobDepMainForm getBpJPlanJobDepMain(String approve,String entryIds,String planDate,
//			String editDepcode, String enterpriseCode);
	//update by sychen 20100414
	public BpJPlanJobDepMainForm getBpJPlanJobDepMain(String approve,String flag,
			String entryIds, String planDate, String editBy,String editDeptcode,
			String enterpriseCode);
	
	
	/**
	 * 部门工作计划审批查询主表信息 add by sychen 20100406
	 * @param approve
	 * @param entryIds
	 * @param planDate
	 * @param editBy
	 * @param enterpriseCode
	 * @return
	 * @throws Exception
	 */
	public PageObject getBpJPlanJobDepMainApprove(String approve,
			String entryIds, String planDate, String editBy,
			String enterpriseCode) throws Exception;
	
	/**
	 * 获取部门数据源 add by sychen 20100407
	 * @return
	 */
	public List getBpJPlanJobDept();
	
	/**
	 * 获取当前月份下当前登录人的deptMainId add by sychen 20100407
	 * @param editBy
	 * @param planTime
	 * @param enterpriseCode
	 * @return
	 */
	public String getBpJPlanJobDeptMainId(String editBy,String planTime,String enterpriseCode);
	
	/**
	 * 查找部门月度计划审批增加主表记录时获取当前月份下当前部门的登录人的主表记录是否存在 
	 * add by sychen 20100409
	 * @param planTime
	 * @param editDepcode
	 * @param workerCode
	 * @param enterpriseCode
	 * @return
	 */
	
	/**
	 * 获得部门汇总审批人 
	 * add by sychen 20100504
	 */
	public String getBpJPlanJobDepMainCaller(String entryId);
	
	public String getPlanJobDeptMainId(String planTime,String editDepcode,String workerCode,
			String enterpriseCode,String method,String depMainId);
	
	/**
	 * 新增部门月度计划增加主表记录时获取当前月份下当前部门的登录人的主表记录是否存在（包含记录上报后再次新增补充计划）
	 * add by sychen 20100429
	 * @param planTime
	 * @param workerCode
	 * @param enterpriseCode
	 * @return
	 */
	public String getPlanJobDeptNewMainId(String planTime,String workerCode,String enterpriseCode);
	
	public PageObject getBpJPlanJobDepMainList(String finish,String planDate,
			String enterpriseCode,int... rowStartIdxAndCount);

//	public BpJPlanJobDepMain reportTo(String prjNo, String workflowType,
//			String workerCode, String actionId, String approveText,
//			String nextRoles, String enterpriseCode,String isEqu);
	//update by sychen 20100327
	public BpJPlanJobDepMain reportTo(String prjNo, String workflowType,
			String workerCode, String actionId, String approveText,String nextRolePs,
			String nextRoles, String enterpriseCode);

	public BpJPlanJobDepMain approveStep(String prjNo, String workflowType,
			String workerCode, String actionId, String actionWord,
			String approveText, String nextRoles, String stepId,
			String enterpriseCode);
	/**
	 * add by liuyi 091224
	 * @param entryIds
	 * @param planDate
	 * @param enterpriseCode
	 * @param rowIndexAndCount
	 * @return
	 */
	public PageObject getBpPlanDeptModList(String approve,String entryIds,String planDate,
			String enterpriseCode,int... rowIndexAndCount);
	
	
	/**
	 *  应该是显示当前月份下责任人下多个主表记录 
	 *  add by sychen 20100505
	 * @param deptH
	 * @param planTime
	 * @param entryIds
	 * @return
	 * @throws Exception
	 */
	public PageObject getBpPlanStatusByChargeBy(String deptH, String planTime,
			String entryIds) throws Exception;
	
	/**
	 * add by liuyi 091224 通过部门编码，月份查询数据，用于判断状态
	 */
	public List getBpPlanStatusByDeptCode(String deptH, String planTime,
			String entryIds);
	
	/**
	 * add by liuyi 091225
	 * @param isApprove
	 * @param entryIds
	 * @param deptMainId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getPlanJobCompleteDetialList(String isApprove,
			String entryIds, String deptMainId, String enterpriseCode,
			String chargeBy,int... rowStartIdxAndCount);
	
	/**
	 * add by liuyi 091225 部门工作计划完成情况填写上报
	 * @param deptMainId
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param workflowType
	 */
//	public void reportDeptPlanComplete(Long deptMainId, Long actionId,
//			String workerCode, String approveText, String nextRoles,
//			String workflowType,String isEqu);
	// update by sychen 20100329
	public void reportDeptPlanComplete(Long deptMainId, Long actionId,
			String workerCode, String approveText, String nextRoles,String nextRolePs,
			String workflowType);
	/**
	 * add by liuyi 091225 部门工作计划完成情况审批
	 * @param deptMainId
	 * @param workflowType
	 * @param workerCode
	 * @param actionId
	 * @param actionWord
	 * @param approveText
	 * @param nextRoles
	 * @param stepId
	 * @param enterpriseCode
	 * @return
	 */
	public BpJPlanJobDepMain deptPlanCompleteApprove(String deptMainId, String workflowType,
			String workerCode, String actionId, String actionWord,
			String approveText, String nextRoles, String stepId,
			String enterpriseCode);
	
	/**
	 * 部门工作计划整理上报页面中“上报”按钮方法
	 * add by drdu 20100105
	 * @param depMainId
	 */
	public void updateMainreportTo(Long depMainId);
	
	/**
	 * 部门工作计划整理上报 已读 方法 add by sychen 20100408
	 * @param depMainId
	 */
	public void updateMainIfReadRecord(Long depMainId);
	
	
	/**
	 * 部门工作计划完成情况整理上报 已读 方法 add by sychen 20100409
	 * @param depMainId
	 */
	public void updateMainFinishIfReadRecord(Long depMainId);
	
	public PageObject getPlanCompleteAllQuery(String isApprove,String entryIds,String planTime,
			String enterpriseCode,int... rowStartIdxAndCount);
	
	/**
	 * 部门工作计划整理上报页面中“整体展示”方法
	 * add by drdu 20100106
	 * @param planDate
	 * @param enterpriseCode
	 * @param rowIndexAndCount
	 * @return
	 */
	public PageObject getBpPlanDeptShowAllList(String planDate,
			String enterpriseCode, int... rowIndexAndCount);
	
	
	/**
	 * 部门工作计划完成情况整理上报页面中“整体展示”方法
	 * add by sychen 20100415
	 * @param planDate
	 * @param enterpriseCode
	 * @param rowIndexAndCount
	 * @return
	 */
	public PageObject getBpPlanDeptCompleteShowAllList(String planDate,
			String enterpriseCode, int... rowIndexAndCount);
	
	/**
	 * 部门工作计划查询 add by sychen 20100423
	 * @param planTime
	 * @param editDeptcode
	 * @param enterpriseCode
	 * @return
	 * @throws Exception
	 */
	public PageObject getBpJPlanJobDepMainQuery(String planTime, String editDeptcode,
			String enterpriseCode) throws Exception;
	
	/**
	 * 查询部门月度计划完成情况填写页面新增主表的主键
	 * add by sychen 20100613
	 * @param planTime
	 * @param workerCode
	 * @param enterpriseCode
	 * @return
	 */
	public String getPlanCompleteNewMainId(String planTime, String workerCode,
			String enterpriseCode);
	
	//取当前登陆人所属部门的第一个部门的类别为管理部门的部门（包括自己）
	public String getManagerDept(Long id);
	//判断是否属于多经总公司
//	public String checkDept(String deptcode, String flag);
}