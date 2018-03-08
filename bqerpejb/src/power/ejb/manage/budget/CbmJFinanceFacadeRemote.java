package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.form.FianceDetailForm;

/**
 * 财务费用预算
 * 
 * @author liuyi 090822
 */
@Remote
public interface CbmJFinanceFacadeRemote {
	/**
	 * 新增一条财务费用预算记录
	 */
	public CbmJFinance save(CbmJFinance entity);

	/**
	 * 删除一条财务费用预算记录
	 */
	public void delete(CbmJFinance entity);

	/**
	 * 更新一条财务费用预算记录
	 */
	public CbmJFinance update(CbmJFinance entity);

	/**
	 * 通过id查找一条财务费用预算记录
	 * 
	 * @param id
	 * @return
	 */
	public CbmJFinance findById(Long id);

	/**
	 * Find all CbmJFinance entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CbmJFinance> all CbmJFinance entities
	 */
	public List<CbmJFinance> findAll(int... rowStartIdxAndCount);

	/**
	 * 通过时间，类型查找符合条件的记录id
	 */
	public Long getIdByTimeType(String budgetTime, String financeType,
			String enterpriseCode);

	/**
	 * 上报
	 * 
	 * @param financeId
	 * @param workflowType
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param eventIdentify
	 */
	public void reportTo(Long financeId, String workflowType,
			String workerCode, Long actionId, String approveText,
			String nextRoles, String eventIdentify);

	/**
	 * 审批签字
	 * 
	 * @param financeId
	 * @param approveText
	 * @param workerCode
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @param eventIdentify
	 */
	public void financeCommSign(Long financeId, String approveText,
			String workerCode, Long actionId, String responseDate,
			String nextRoles, String eventIdentify);

	/**
	 * 审批查询
	 * 
	 * @param budgetTime
	 * @param financeType
	 * @param enterpriseCode
	 * @param entryIds
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject financeApproveQuery(String budgetTime,
			String financeType, String enterpriseCode, String entryIds,
			int... rowStartIdxAndCount);

}