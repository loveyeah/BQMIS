package power.ejb.manage.budget;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.form.CbmJBudgetItemForm;

/**
 * 预算变更明细表
 * 
 * @author Administrator
 * 
 */
@Remote
public interface CbmJBudgetItemFacadeRemote {

	public CbmJBudgetItem save(CbmJBudgetItem entity);

	public CbmJBudgetItem update(CbmJBudgetItem entity);

	public CbmJBudgetItem findById(Long id);

	public void addOrUpdateRecords(List<CbmJBudgetItem> list, String isHappen);

	public PageObject findToCheck(String deptCode, String budgetTime,
			String dataType, String enterpriseCode, int... rowStartIdxAndCount);

	public void saveModified(List<CbmJBudgetItemForm> modList);

	/**
	 * 通过指标itemId,部门centerId,月份budgetTime查找财务发生值
	 */
	public Double findFinanceValue(Long itemId, Long centerId,
			String budetTime, String enterpriseCode);

//	public List<CbmJBudgetItemForm> findAllItemList(String topicId,
//			String budgetTime, String enterpriseCode);

	// public List<CbmJBudgetItemForm> findAllItemAdviceBudgetList(String
	// topicId,
	// String budgetTime, String enterpriseCode, Long itemId);

	/**
	 * 根据主题，时间段查找某一部门下预算明细列表
	 * 
	 * @param deptCode
	 * @param topicId
	 * @param budgetTime
	 * @param formatType
	 * @param enterpriseCode
	 * @param itemName add by fyyang 20100609 指标名称模糊查询
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findModifyItemList(String deptCode, String topicId,
			String budgetTime, String formatType, String enterpriseCode,
			String itemName,
			int... rowStartIdxAndCount);

	public Map<String, Object> queryGather(Long gatherId, String topicId,
			String date, String enterpriseCode);

	/**
	 * 通过指标，预算编制单id查找一条部门明细实例id列表 add by liuyi 090820
	 * 
	 * @param budgetMakeId
	 * @param itemId
	 * @param enterpriseCode
	 * @return
	 */
	public List<Long> findDetailsInstance(Long budgetMakeId, Long itemId,
			String enterpriseCode);

	public void calculateItemValue(Long budgetMakeId);

	/**
	 * 通过指标列表，预算时间查询明细表中的各个预算制 add by liuyi 090829
	 * 
	 * @param itemList
	 * @param budgetTime
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject getComplexBudgetList(List<CbmJBudgetItemForm> itemList,
			String budgetTime, String enterpriseCode);

	/**
	 * 通过budgetMakeId查询明细 add by ltong 20100601
	 * 
	 * @param makeId
	 * @return
	 */

	public List<CbmJBudgetItem> findBudgetByMakeId(Long makeId);
}