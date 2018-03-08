package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.form.CapitalDetailForm;
import power.ejb.manage.budget.form.FianceDetailForm;

/**
 * 资本性支出预算明细
 * 
 * @author liuyi 090824
 */
@Remote
public interface CbmJCapitalDetailFacadeRemote {
	/**
	 * 新增一条资本性支出预算明细记录
	 */
	public void save(CbmJCapitalDetail entity);

	/**
	 * 删除一条资本性支出预算明细记录
	 */
	public void delete(CbmJCapitalDetail entity);

	/**
	 * 删除一条或多条资本性支出预算明细记录
	 */
	public void delete(String ids);

	/**
	 * 更新一条资本性支出预算明细记录
	 */
	public CbmJCapitalDetail update(CbmJCapitalDetail entity);

	public CbmJCapitalDetail findById(Long id);

	/**
	 * 查询符合条件的资本性支出预算明细记录
	 * 
	 * @param budgetTime
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findCapitalDetailList(String budgetTime,
			String enterpriseCode, int... rowStartIdxAndCount);

	/**
	 * 批量保存修改资本性支出预算明细记录
	 * 
	 * @param addList
	 * @param updateList
	 * @param ids
	 */
	public void saveCapitalDetails(List<CapitalDetailForm> addList,
			List<CapitalDetailForm> updateList, String ids);
}