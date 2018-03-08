package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.form.CbmCFactFormulaForm;
import power.ejb.manage.budget.form.CbmCItemFormulaForm;

@Remote
public interface CbmCFactFormulaFacadeRemote {

	public void save(CbmCFactFormula entity);

	public void save(List<CbmCFactFormula> addList);

	public boolean delete(String itemId);

	public List<CbmCFactFormulaForm> findAll(String enterpriseCode,
			String itemId);

	public String[] getFactFormula(Long id);
}