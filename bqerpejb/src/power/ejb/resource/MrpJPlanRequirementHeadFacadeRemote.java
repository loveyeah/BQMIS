package power.ejb.resource;

import java.util.List;
import javax.ejb.Remote;
import power.ear.comm.ejb.PageObject;
import power.ejb.resource.form.CostFromInfo;

/**
 * Remote interface for MrpJPlanRequirementHeadFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface MrpJPlanRequirementHeadFacadeRemote {
	/**根据时间，部门编码，费用来源编码来查询材料领用信息
	 * @param sTime
	 * @param eTime
	 * @param deptCode
	 * @param costFrom
	 * @return
	 */
	public PageObject getCostFrom(String sTime,String eTime,String ouSTime,String outETime,String deptCode,String costFrom);
	/**
	 * Perform an initial save of a previously unsaved MrpJPlanRequirementHead
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            MrpJPlanRequirementHead entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public MrpJPlanRequirementHead save(MrpJPlanRequirementHead entity);

	/**
	 * Delete a persistent MrpJPlanRequirementHead entity.
	 * 
	 * @param entity
	 *            MrpJPlanRequirementHead entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(MrpJPlanRequirementHead entity);

	/**
	 * Persist a previously saved MrpJPlanRequirementHead entity and return it
	 * or a copy of it to the sender. A copy of the MrpJPlanRequirementHead
	 * entity parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            MrpJPlanRequirementHead entity to update
	 * @return MrpJPlanRequirementHead the persisted MrpJPlanRequirementHead
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public MrpJPlanRequirementHead update(MrpJPlanRequirementHead entity);

	public MrpJPlanRequirementHead findById(Long id);

	public MrpJPlanRequirementHead findByIdWithName(Long id);
	public MrpJPlanRequirementHead findMaterialById(Long id) ;

	/**
	 * Find all MrpJPlanRequirementHead entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the MrpJPlanRequirementHead property to query
	 * @param value
	 *            the property value to match
	 * @return List<MrpJPlanRequirementHead> found by query
	 */
	public List<MrpJPlanRequirementHead> findByProperty(String propertyName,
			Object value);

	public List<MrpJPlanRequirementHead> findByMrNo(Object mrNo);

	public List<MrpJPlanRequirementHead> findByWoNo(Object woNo);

	public List<MrpJPlanRequirementHead> findByPlanOriginalId(
			Object planOriginalId);

	public List<MrpJPlanRequirementHead> findByItemId(Object itemId);

	public List<MrpJPlanRequirementHead> findByMrBy(Object mrBy);

	public List<MrpJPlanRequirementHead> findByMrDept(Object mrDept);

	public List<MrpJPlanRequirementHead> findByPlanGrade(Object planGrade);

	public List<MrpJPlanRequirementHead> findByCostDept(Object costDept);

	public List<MrpJPlanRequirementHead> findByCostSpecial(Object costSpecial);

	public List<MrpJPlanRequirementHead> findByMrReason(Object mrReason);

	public List<MrpJPlanRequirementHead> findByMrType(Object mrType);

	public List<MrpJPlanRequirementHead> findByWfNo(Object wfNo);

	public List<MrpJPlanRequirementHead> findByWfState(Object wfState);

	public List<MrpJPlanRequirementHead> findByEnterpriseCode(
			Object enterpriseCode);

	public List<MrpJPlanRequirementHead> findByIsUse(Object isUse);

	/**
	 * Find all MrpJPlanRequirementHead entities.
	 * 
	 * @return List<MrpJPlanRequirementHead> all MrpJPlanRequirementHead
	 *         entities
	 */
	public List<MrpJPlanRequirementHead> findAll();

	/**
	 * 根据条件查询物料需求计划 modify by fyyang 090424 增加了按照审批状态查询
	 * 
	 * @param enterpriseCode
	 * @param startDate
	 * @param endDate
	 * @param dept
	 * @param mrBy
	 * @param maName
	 * @param maClassName
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findByFuzzy(String enterpriseCode, String startDate,
			String endDate, String dept, String mrBy, String maName,
			String maClassName, String status, String planOriginalID,
			final int... rowStartIdxAndCount);

	/**
	 * 根据主ID获取查询画面物料明细 modify by fyyang 090807
	 * 
	 * @param enterpriseCode
	 * @param headId
	 * @return
	 */
	public PageObject getMaterialDetail(String enterpriseCode, Long headId,
			String flag);

	/**
	 * 根据主ID获取登记画面物料明细
	 * 
	 * @param enterpriseCode
	 * @param headId
	 * @return
	 */
	public PageObject getMaterialEdit(String enterpriseCode, Long headId);
	public PageObject getMaterial(String enterpriseCode, Long headId);
	/**
	 * 根据物料ID获取当前库存
	 * 
	 * @param materialId
	 * @return
	 */
	public String getMaterialStock(Long materialId, String enterpriseCode);

	/**
	 * 物料需求计划登记账票信息查询
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param headId
	 *            选中项ID
	 * @return ReceiveGoodsBean 到货单明细
	 */
	public MaterialRequestReportBean fillallForMaterialRequestReport(
			String enterpriseCode, String headId);

	/**
	 * 
	 * @param initialBookNo
	 *            传入的单号格式 如"DP000000"
	 * @param tabelName
	 *            表名
	 * @param idColumnName
	 *            列名
	 * @return 最大的单号
	 */
	public String getMaxBookNo(String initialBookNo, String tabelName,
			String idColumnName);

	/**
	 * 需求计划审批列表查询 add by fyyang 090414 modify by fyyang 090722 增加登录人部门字段
	 * 
	 * @param enterpriseCode
	 * @param startDate
	 * @param endDate
	 * @param dept
	 * @param mrBy
	 * @param maName
	 * @param maClassName
	 * @param status
	 * @param entryIds
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findApproveListByFuzzy(String enterpriseCode,
			String startDate, String endDate, String dept, String mrBy,
			String maName, String maClassName, String status, String entryIds,
			String planOriginalID, String deptCode, String workCode,
			int... rowStartIdxAndCount);

	/**
	 * 获取所有审批结束后的物料明细 add by ywliu 090414 modify by fyyang 090521
	 * 
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getMRPMaterialDetail(String buyer, String applyDept,
			String enterpriseCode, final int... rowStartIdxAndCount);

	/**
	 * 通过费用编码查出费用名称(物资公用方法)
	 * 
	 * @param itemCode
	 * @return String
	 */
	public String getItemNameByItemCode(String itemCode);

	/**
	 * 删除需求计划申请单 add by fyyang 090728
	 * 
	 * @param entryId
	 *            实例号id
	 * @param headId
	 *            计划单id
	 */
	public void deletePlanRequirement(Long entryId, Long headId);

	/**
	 * 获得已经执行过计划作废的物资列表 add by fyyang 090807
	 * 
	 * @param applyDept
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getBlankOutMaterialDetail(String applyDept,
			String wzName, String ggSize, String enterpriseCode,
			int... rowStartIdxAndCount);

	/**
	 * add by liuyi 091029 需求物资查询
	 * 
	 * @param fromDate
	 *            需求计划上报日期开始
	 * @param toDate
	 *            需求计划上报日期至
	 * @param materialNo
	 *            物资编码
	 * @param materialName
	 *            物资名称
	 * @param specNo
	 *            物资规格
	 * @param needPlanNo
	 *            需求计划单号
	 * @param mrBy
	 *            求计划填写人
	 * @param buyBy
	 *            采购员
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @param dept
	 *            add by ywliu 20091112
	 * @param queryType
	 *            add by ywliu 20091112
	 * @return
	 */
	public PageObject findMaterialDetailByCond(String fromDate, String toDate,
			String materialNo, String materialName, String specNo,
			String needPlanNo, String mrBy, String buyBy, String dept,
			String queryType, String observer, String status,
			String enterpriseCode, int... rowStartIdxAndCount);

	/**
	 * 根据登陆人查询登陆人参与的物料需求计划 add by ywliu 091110
	 * 
	 * @param enterpriseCode
	 * @param startDate
	 * @param endDate
	 * @param dept
	 * @param mrBy
	 * @param maName
	 * @param maClassName
	 * @param rowStartIdxAndCount
	 * @param mrPlanNo
	 *            add by fyyang 100113 申请单号
	 * @return
	 */
	public PageObject findPlanHeadByLogin(String enterpriseCode,
			String startDate, String endDate, String mrBy, String maName,
			String maClassName, String status, String planOriginalID,
			String queryType, String dept, String observer, String mrPlanNo,
			final int... rowStartIdxAndCount);

	/**
	 * 根据登录人所在部门决定是否显示实例
	 * 
	 * @param entryIds
	 *            工作流实例号
	 * @param deptCode
	 *            登录人所在部门 add by Derek 2010/01/28
	 * @return
	 */
	public int inDeptCase(String entryIds, String deptCode, String tableName,
			String columnNameWf, String columnNameDp);
}