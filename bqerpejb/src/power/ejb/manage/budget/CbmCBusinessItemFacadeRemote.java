package power.ejb.manage.budget;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.form.BusinessItemForm;

/**
 * Remote interface for CbmCBusinessItemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmCBusinessItemFacadeRemote {
	 
	public void save(List<CbmCBusinessItem> list) throws CodeRepeatException;

	public void delete(CbmCBusinessItem entity);

	public void update(List<CbmCBusinessItem> list) throws CodeRepeatException;

	public CbmCBusinessItem findById(Long id);
	
	public boolean deleteMuti(String deleteIds);
	 
	public PageObject findAll(String enterpriseCode,int... rowStartIdxAndCount);
	
	public List<BusinessItemForm> getBusinessItemReportPrint(String dateTime);
}