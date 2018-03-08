package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.form.CbmJBudgetItemForm;

/**
 * 预算部门指标维护
 * 
 * @author liuyi 090805
 */
@Remote
public interface CbmCCenterItemFacadeRemote {
	/**
	 * 新增一条预算部门指标维护数据
	 */
	public void save(CbmCCenterItem entity);

	/**
	 * 删除一条预算部门指标维护数据
	 */
	public void delete(CbmCCenterItem entity);

	/**
	 * 删除一条或多条预算部门指标维护数据
	 */
	public void delete(String ids);

	/**
	 * 更新一条预算部门指标维护数据
	 */
	public CbmCCenterItem update(CbmCCenterItem entity);

	/**
	 * 通过id查找一条预算部门指标维护数据
	 * 
	 * @param id
	 * @return
	 */
	public CbmCCenterItem findById(Long id);

	public List<CbmCCenterItem> findAll(int... rowStartIdxAndCount);

	public PageObject findDeptTopicItemList(String centerTopicId,
			String enterpriseCode, int... rowStartIdxAndCount);

	public void saveDeptTopicItem(List<CbmCCenterItem> addList,
			List<CbmCCenterItem> updateList, String ids);

	/**
	 * 判断新增或修改的指标在该部门下是否已存在
	 */
	public boolean judgeIsExist(Long centerId, String itemIds, String dciIds,
			String enterpriseCode);

	public List<CbmJBudgetItemForm> findItemList(String centerId,
			String topicId, String enterpriseCode);
}