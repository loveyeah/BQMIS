package power.ejb.manage.budget;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 预算执行控制
 * 
 * @author liuyi 090806
 */
@Remote
public interface CbmJMasterItemFacadeRemote {
	/**
	 * 新增一条预算执行控制记录
	 */
	public void save(CbmJMasterItem entity);

	/**
	 * 删除一条预算执行控制记录
	 */
	public void delete(CbmJMasterItem entity);

	/**
	 * 删除一条或多条预算执行控制记录
	 */
	public void delete(String ids);

	/**
	 * 更新一条预算执行控制记录
	 */
	public CbmJMasterItem update(CbmJMasterItem entity);

	/**
	 * 通过id查找一条预算执行控制记录
	 * 
	 * @param id
	 * @return
	 */
	public CbmJMasterItem findById(Long id);

	public List<CbmJMasterItem> findAll(int... rowStartIdxAndCount);

	public PageObject getAllList(String isFeeRegister, String budgetItemId,
			String deptCode, String dataType, String budgetTime,
			String enterpriseCode, int... rowStartIdxAndCount);

	public void saveModified(String isFeeRegister,
			List<CbmJMasterItem> addList, List<CbmJMasterItem> updateList,
			String ids);

	/**
	 * 上报
	 * 
	 * @param happenId
	 * @param workflowType
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param eventIdentify
	 */
	public void reportTo(Long happenId, String workflowType, String workerCode,
			Long actionId, String approveText, String nextRoles,
			String eventIdentify);

	/**
	 * 签字
	 * 
	 * @param happenId
	 * @param approveText
	 * @param workerCode
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @param eventIdentify
	 */
	public void masterItemCommSign(Long happenId, String approveText,
			String workerCode, Long actionId, String responseDate,
			String nextRoles, String eventIdentify);

	/**
	 * 审批查询
	 * 
	 * @param deptId
	 * @param budgetTime
	 * @param entryIds
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject masterItemApproveQuery(String deptId, String budgetTime,
			String entryIds, int... rowStartIdxAndCount);
}