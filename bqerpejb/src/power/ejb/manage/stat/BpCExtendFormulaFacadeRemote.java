package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.form.StatItemFormula;

/**
 * Remote interface for BpCExtendFormulaFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCExtendFormulaFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpCExtendFormula entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpCExtendFormula entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCExtendFormula entity);
	/**
	 * 检查扩展公式是否正确
	 * @param sqlContent
	 * @return
	 */
	public boolean checkFormulaCorrect(String sqlContent); 
	public void save(List<BpCExtendFormula> addList);

	/**
	 * Delete a persistent BpCExtendFormula entity.
	 * 
	 * @param entity
	 *            BpCExtendFormula entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpCExtendFormula entity);

	public boolean delete(String ids);

	public boolean deleteFormula(String itemCode, String enterpriseCode);

	/**
	 * Persist a previously saved BpCExtendFormula entity and return it or a
	 * copy of it to the sender. A copy of the BpCExtendFormula entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCExtendFormula entity to update
	 * @return BpCExtendFormula the persisted BpCExtendFormula entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */

	public BpCExtendFormula findById(BpCExtendFormulaId id);

	/**
	 * 
	 * @param enterpriseCode
	 * @param itemCode
	 * @return
	 */
	public List<StatItemFormula> findAll(String enterpriseCode, String itemCode);

	/**
	 * 模糊查询指标信息
	 * 
	 * @param itemCode
	 *            指标编码
	 * @param itemName
	 *            指标名称
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	public PageObject findAllStatItem(String argFuzzy, String dataTimeType,
			String itemType, int... rowStartIdxAndCount);
}