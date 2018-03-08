package power.ejb.manage.project;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PrjJCheckFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PrjJCheckFacadeRemote {
	
	/**
	 * 保存一条竣工验收单信息
	 * @param entity
	 */
	public PrjJCheck save(PrjJCheck entity,String dept);

	/**
	 * 批量删除验收单信息
	 * @param ids
	 */
	public void delete(String ids);

	/**
	 * 保存一条竣工验收单信息
	 * @param entity
	 * @return
	 */
	public PageObject findReportList(String enterpriseCode,String projectName,int statusId,final int... rowStartIdxAndCount );
	
	public PrjJCheck update(PrjJCheck entity);

	public PrjJCheck findById(Long id);

	/**
	 * Find all PrjJCheck entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PrjJCheck property to query
	 * @param value
	 *            the property value to match
	 * @return List<PrjJCheck> found by query
	 */
	public List<PrjJCheck> findByProperty(String propertyName, Object value);

	/**
	 * 根据查询条件查询竣工验收单信息
	 * @param statusId
	 * @param projectName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public PageObject findAll(String statusId, String workerCode,String projectName,String flag, String startTime, String endTime, int... rowStartIdxAndCount);
	
	/**
	 * 竣工验收单上报
	 * @param checkId
	 * @param workercode
	 * @param actionId
	 * @param approveText
	 * @param workFlowCode
	 */
	public void endCheckReportTo(String checkId, String workercode,String actionId, String approveText,String workFlowCode);
	
	/**
	 * 竣工验收单审批
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
	public boolean endCheckApprove(String entryId,String workerCode,String actionId,String approveText,String nextRoles,String identify,String applyId,String entryByCode);
	/**
	 * 根据查询条件查询竣工验收单信息
	 * @param projectName
	 * @param enterpriseCode
	 * @param workFlowNos 工作流实例号
	 * @return PageObject
	 * add by kzhang 20100821
	 */
	public PageObject checkfindAll(String propertyName, String enterpriseCode,String workFlowNos,
			Long deptId,int... rowStartIdxAndCount);
	
	/**
	 * 事务提醒显示
	 * @param workCode
	 * @param deptId
	 * @return
	 */
	public String getCheckList(String workCode,Long deptId);
	/**
	 * 保存竣工验收审批信息
	 * @param entity
	 * @throws RuntimeException
	 */
	public void prjCheckApproveSave(PrjJCheckApprove entity);
}