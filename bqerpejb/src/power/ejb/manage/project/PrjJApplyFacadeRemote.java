package power.ejb.manage.project;

import javax.ejb.Remote;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PrjJApplyFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PrjJApplyFacadeRemote {
	/**
	 * 保存开工申请单信息
	 * @param entity
	 * @return PrjJApply 
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public PrjJApply save(PrjJApply entity);

	/**
	 * 删除开工申请单信息
	 * 通过申请单id字符串
	 * @param ids 
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public void delete(String ids);

	/**
	 * 修改开工申请单信息
	 * @param entity
	 * @return PrjJApply 
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public PrjJApply update(PrjJApply entity);
	/**
	 * 查找开工申请单信息
	 * 通过开工申请单Id
	 * @param id
	 * @return PrjJApply
	 */
	@SuppressWarnings("unchecked")
	public PrjJApply findPrjJApplyById(Long id);
	/**
	 *通过项目名称查找开工申请单信息
	 * 
	 * @param propertyName 项目名称
	 * @param enterpriseCode 企业编码
	 * @param status 状态
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByPropertyAndStatus(String propertyName,String status, String enterpriseCode,String entryByCode,
			int... rowStartIdxAndCount);
	/**
	 * 上报
	 * @param busitNo
	 * @param flowCode
	 * @param workerCode
	 * @param actionId
	 */
	
	public void reportTo(String busitNo,String flowCode,String workerCode,Long actionId);
	/**
	 *通过项目名称查找开工申请单信息
	 * 
	 * @param propertyName 项目名称
	 * @param enterpriseCode 企业编码
	 * @param status 状态
	 * @param workFlowNos 工作流实例号
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String propertyName,String status, String enterpriseCode,String workFlowNos,
			Long deptId,String flag,String entryByCode,int... rowStartIdxAndCount);
	
	/**
	 * 事务提醒列表
	 * @param workCode
	 * @param deptId
	 * @return
	 */
	public String getApplyList(String workCode,Long deptId);
	/**
	 * 
	 * 工程开工申请单审批
	 * @param entryId
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param identify
	 * @param applyId
	 * @param entryByCode
	 * @return boolean
	 */
	public boolean prjNewApplyApproval(String entryId,String workerCode,String actionId,String approveText,String nextRoles,String identify,String applyId,String entryByCode);
	/**
	 * 保存开工申请审批信息
	 * 
	 * @param entity
	 * @throws RuntimeException
	 */
	public void prjAproveSave(PrjJApplyApprove entity);

}