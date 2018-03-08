package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.form.FianceDetailForm;

/**
 * 财务费用预算明细
 * 
 * @author liuyi 090822
 */
@Remote
public interface CbmJFinanceDetailFacadeRemote {
	/**
	 * 新增一条财务费用预算明细记录
	 */
	public void save(CbmJFinanceDetail entity);

	/**
	 * 删除一条财务费用预算明细记录
	 */
	public void delete(CbmJFinanceDetail entity);

	/**
	 * 更新一条财务费用预算明细记录
	 */
	public CbmJFinanceDetail update(CbmJFinanceDetail entity);

	/**
	 * 通过id查找一条财务费用预算明细记录
	 * 
	 * @param id
	 * @return
	 */
	public CbmJFinanceDetail findById(Long id);

	/**
	 * 查找符合条件的所有财务预算明细
	 */
	public PageObject findAllFinanceDetail(String budgetTime,
			String financeType, String enterpriseCode,
			int... rowStartIdxAndCount);

	/**
	 * 批量保存修改财务预算明细记录
	 * 
	 * @param addList
	 * @param updateList
	 * @param ids
	 */
	public void saveFinanceDetails(List<FianceDetailForm> addList,
			List<FianceDetailForm> updateList, String ids);
}