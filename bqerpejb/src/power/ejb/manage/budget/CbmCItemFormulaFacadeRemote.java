package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.form.CbmCItemFormulaForm;

/**
 * Remote interface for CbmCItemFormulaFacade.
 * 
 * @author MyEclipse Persistence Tools123
 */
@Remote
public interface CbmCItemFormulaFacadeRemote {
	public void save(CbmCItemFormula entity);

	public void save(List<CbmCItemFormula> addList);

	public boolean delete(String itemId);

	public PageObject findAllStatItem(String argFuzzy,
			int... rowStartIdxAndCount);

	public List<CbmCItemFormulaForm> findAll(String enterpriseCode,
			String itemId);

	public String[] getBudgetFormula(Long id);
}