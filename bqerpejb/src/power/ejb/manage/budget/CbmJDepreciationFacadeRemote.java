package power.ejb.manage.budget;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for CbmJDepreciationFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmJDepreciationFacadeRemote {

	/**
	 * 增加一条折旧费预算记录
	 * 
	 * @param entity
	 */
	public CbmJDepreciation save(CbmJDepreciation entity);

	public void delete(CbmJDepreciation entity);

	public CbmJDepreciation update(CbmJDepreciation entity);

	public CbmJDepreciation findById(Long id);

	/**
	 * 根据年份，企业编码查找ID是否已存在
	 * 
	 * @param budgetTime
	 * @param enterpriseCode
	 * @return
	 */
	public Long getIdByTime(String budgetTime, String enterpriseCode);

	public void reportTo(Long depreciationId, String workflowType,
			String workerCode, Long actionId, String approveText,
			String nextRoles, String eventIdentify);

	public void depreciationCommSign(Long depreciationId, String approveText,
			String workerCode, Long actionId, String responseDate,
			String nextRoles, String eventIdentify);

	public PageObject depreciationApproveQuery(String budgetTime,
			String enterpriseCode, String entryIds, int... rowStartIdxAndCount);
}