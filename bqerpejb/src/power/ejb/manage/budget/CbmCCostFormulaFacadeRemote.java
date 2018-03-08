package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.form.CbmCItemFormulaForm;

/**
 * Remote interface for CbmCCostFormulaFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmCCostFormulaFacadeRemote {
public void save(CbmCCostFormula entity);

public void save(List<CbmCCostFormula> addList);

public boolean delete(String itemId);

public PageObject findAllCostItem(String argFuzzy,
		int... rowStartIdxAndCount);

public List<CbmCItemFormulaForm> findAll(String enterpriseCode,
		String itemId);

public String[] getBudgetFormula(Long id);}