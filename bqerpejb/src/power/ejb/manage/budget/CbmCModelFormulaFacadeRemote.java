package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ejb.manage.budget.form.ModelFormulaForm;

/**
 * Remote interface for CbmCModelFormulaFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmCModelFormulaFacadeRemote {

	/**
	 * 保存
	 * 
	 * @param entity
	 */
	public void save(CbmCModelFormula entity);

	public void save(List<CbmCModelFormula> addList);

	/**
	 * 删除
	 * 
	 * @param entity
	 */
	public void delete(CbmCModelFormula entity);

	public void delete(String modelId);

	/**
	 * 修改
	 * 
	 * @param entity
	 * @return
	 */
	public CbmCModelFormula update(CbmCModelFormula entity);

	/**
	 * 查找一条记录
	 * 
	 * @param id
	 * @return
	 */
	public CbmCModelFormula findById(Long id);

	public List<CbmCModelFormula> findByProperty(String propertyName,
			Object value);

	/**
	 * 由modelItemId查询记录
	 * 
	 * @param enterpriseCode
	 * @param modelItemId
	 * @return
	 */
	public List<ModelFormulaForm> findAll(String enterpriseCode,
			String modelItemId);
}