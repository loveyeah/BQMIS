package power.ejb.manage.project;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.manage.contract.form.PrjQualityForm;

/**
 * Remote interface for PrjJInfoFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PrjJInfoFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PrjJInfo entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PrjJInfo entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public PrjJInfo save(PrjJInfo entity) throws CodeRepeatException;

	/**
	 * Delete a persistent PrjJInfo entity.
	 * 
	 * @param entity
	 *            PrjJInfo entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PrjJInfo entity);

	/**
	 * Persist a previously saved PrjJInfo entity and return it or a copy of it
	 * to the sender. A copy of the PrjJInfo entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PrjJInfo entity to update
	 * @return PrjJInfo the persisted PrjJInfo entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PrjJInfo update(PrjJInfo entity) throws CodeRepeatException;

	public PrjJInfo findById(Long id);

	/**
	 * Find all PrjJInfo entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PrjJInfo property to query
	 * @param value
	 *            the property value to match
	 * @return List<PrjJInfo> found by query
	 */
	public List<PrjJInfo> findByProperty(String propertyName, Object value);

	public List<PrjJInfo> findByPrjNo(Object prjNo);

	public List<PrjJInfo> findByPrjName(Object prjName);

	public List<PrjJInfo> findByPrjYear(Object prjYear);

	public List<PrjJInfo> findByPrjTypeId(Object prjTypeId);

	public List<PrjJInfo> findByPrjContent(Object prjContent);

	public List<PrjJInfo> findByConstructionUnit(Object constructionUnit);

	public List<PrjJInfo> findByPrjStatus(Object prjStatus);

	public List<PrjJInfo> findByItemId(Object itemId);

	public List<PrjJInfo> findByPlanAmount(Object planAmount);

	public List<PrjJInfo> findByChargeBy(Object chargeBy);

	public List<PrjJInfo> findByChargeDep(Object chargeDep);

	public List<PrjJInfo> findByPlanTimeLimit(Object planTimeLimit);

	public List<PrjJInfo> findByFactTimeLimit(Object factTimeLimit);

	public List<PrjJInfo> findByAnnex(Object annex);

	public List<PrjJInfo> findByMemo(Object memo);

	public List<PrjJInfo> findByEntryBy(Object entryBy);

	public List<PrjJInfo> findByCmlAppraisal(Object cmlAppraisal);

	public List<PrjJInfo> findByProWorkFlowNo(Object proWorkFlowNo);

	public List<PrjJInfo> findByAccWorkFlowNo(Object accWorkFlowNo);

	public List<PrjJInfo> findByEnterpriseCode(Object enterpriseCode);

	public List<PrjJInfo> findByIsUse(Object isUse);

	/**
	 * Find all PrjJInfo entities.
	 * 
	 * @return List<PrjJInfo> all PrjJInfo entities
	 */
	public List<PrjJInfo> findAll();
	//entryType判断使用entryIds
	public PageObject FindByMoreCondition(String workFlowType,String entryType,String entryIds,String workCode,String prjNo, String prjYear,
			String prjTypeId, String prjStatus, String chargeDep,
			String argFuzzy, String prjStatusType, String entryDateStart,
			String entryDateEnd, int... rowStartIdxAndCount);

	/**
	 * 通过项目编号查找项目
	 * 
	 * @param prjNo
	 * @return PrjJInfo
	 */
	public PrjJInfo findByPrjNo(String prjNo, String enterpriseCode);

	/**
	 * 执行
	 * 
	 * @param entryId
	 *            实例编号
	 * @param workerCode
	 *            审批人
	 * @param actionId
	 *            动作
	 * @param approveText
	 *            意见
	 */
	public String reportTo(String prjNo, String workflowType,
			String workerCode, String actionId, String approveText,
			String nextRoles, String enterpriseCode);

	/**
	 * @param prjNo
	 *            项目编号
	 * @param workflowType
	 *            工作流流程
	 * @param entryId
	 *            实例编号
	 * @param workerCode
	 *            审批人
	 * @param actionId
	 *            动作
	 * @param approveText
	 *            意见
	 * @param nextRoles
	 *            下一步角色
	 * 
	 * 审批步骤：工程项目 审批
	 * 
	 * 对应信息：
	 * 
	 * →项目管理单位审批通过→11
	 * 
	 * →生产技术部审批通过→12
	 * 
	 * →财务部审批通过→13
	 * 
	 * →监察审计部审批通过→14
	 * 
	 * →安全环保部审批通过→15
	 * 
	 * →计划经营部审批通过→16
	 * 
	 * →分管厂领导审批通过→17
	 * 
	 * →厂长审批通过→2
	 * 
	 * →验收页面点“保存”→4
	 * 
	 * →验收页面点“申请”→5
	 * 
	 * →工程建设单位审批通过→21
	 * 
	 * →工程管理单位审批通过→22
	 * 
	 * →安全环保部已验收→23
	 * 
	 * →档案管理部审批通过→24
	 * 
	 * →分管厂领导→6
	 * 
	 */
	// 立项审批
	public String approveStep(String prjNo, String workflowType,
			String workerCode, String actionId, String actionWord,
			String approveText, String nextRoles, String stepId,
			String enterpriseCode);

	// 验收申请
	public String checkReport(String prjNo, String workflowType,
			String workerCode, String actionId, String actionWord,
			String approveText, String nextRoles, String stepId,
			String enterpriseCode);

	// 验收审批
	public String checkStep(String prjNo, String workflowType,
			String workerCode, String actionId, String actionWord,
			String approveText, String nextRoles, String stepId,
			String enterpriseCode);

	// 根据类型选项目编号
	/**
	 * @param prjTypeId
	 *            项目编号类别
	 * @param queryKey
	 *            项目编号、负责人、项目名称
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getProjectTypeByPrjNo(String typeChoose,String prjTypeId, String queryKey,
			int... rowStartIdxAndCount);
	//取立项审批记录
	public List<PrjApproveForm> getApplyApprovelist(Long id);
	//取验收审批记录
	public List<PrjApproveForm> getCheckApprovelist(Long id);
	
	
	/**
	 * 根据项目编号取基本信息(报表）
	 * @param prjNo
	 * @return
	 * add by drdu 090730
	 */
	public PrjQualityForm findPrjPrintInfo(String prjNo);
	
	/**
	 * 根据工作流ID取得审批记录(报表）
	 * @param id
	 * @return
	 * add by drdu 090730
	 */
	public List<ConApproveForm> findPrjPrintApproveInfo(Long id);
}