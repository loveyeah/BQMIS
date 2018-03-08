package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.form.CbmJDepreciationForm;

/**
 * Remote interface for CbmJDepreciationDetailFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmJDepreciationDetailFacadeRemote {

	/**
	 * 增加一条折旧费预算明细记录
	 * 
	 * @param entity
	 */
	public void save(CbmJDepreciationDetail entity);

	/**
	 * 批量操作预算明细记录
	 * 
	 * @param addList
	 * @param updateList
	 * @param ids
	 */
	public void saveDepreciationDetails(List<CbmJDepreciationForm> addList,
			List<CbmJDepreciationForm> updateList, String ids);

	/**
	 * 删除一条预算明细记录
	 * 
	 * @param entity
	 */
	public void delete(CbmJDepreciationDetail entity);

	/**
	 * 删除一条或多条预算明细记录
	 * 
	 * @param ids
	 */
	public void delete(String ids);

	public CbmJDepreciationDetail update(CbmJDepreciationDetail entity);

	public CbmJDepreciationDetail findById(Long id);

	/**
	 * 根据时间，企业编码查找预算明细列表记录
	 * 
	 * @param budgetTime
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAllList(String budgetTime, String enterpriseCode,
			int... rowStartIdxAndCount);
}