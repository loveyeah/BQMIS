package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 资本性支出预算
 * 
 * @author liuyi 090824
 */
@Remote
public interface CbmJCapitalFacadeRemote {
	/**
	 * 新增一条资本性支出预算记录
	 */
	public CbmJCapital save(CbmJCapital entity);

	/**
	 * 删除一条资本性支出预算记录
	 */
	public void delete(CbmJCapital entity);

	/**
	 * 更新一条资本性支出预算记录
	 */
	public CbmJCapital update(CbmJCapital entity);

	public CbmJCapital findById(Long id);

	/**
	 * 通过预算时间查询资本性支出预算记录id
	 */
	public Long fingIdByTime(String budgetTime, String enterpriseCode);

	/**
	 * 上报
	 * 
	 * @param capitalId
	 * @param workflowType
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param eventIdentify
	 */
	public void reportTo(Long capitalId, String workflowType,
			String workerCode, Long actionId, String approveText,
			String nextRoles, String eventIdentify);

	/**
	 * 审批签字
	 * 
	 * @param capitalId
	 * @param approveText
	 * @param workerCode
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @param eventIdentify
	 */
	public void capitalCommSign(Long capitalId, String approveText,
			String workerCode, Long actionId, String responseDate,
			String nextRoles, String eventIdentify);

	/**
	 * 审批查询
	 * 
	 * @param budgetTime
	 * @param enterpriseCode
	 * @param entryIds
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject capitalApproveQuery(String budgetTime,
			String enterpriseCode, String entryIds, int... rowStartIdxAndCount);

}