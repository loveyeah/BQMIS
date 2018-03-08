package power.ejb.manage.budget;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.manage.budget.form.CbmCReportItemAdd;
import power.ejb.manage.budget.form.ReportItemForm;

/**
 * Remote interface for CbmCReportItemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmCReportItemFacadeRemote {
	
	public void save(CbmCReportItem entity) throws CodeRepeatException;

	
	public void delete(CbmCReportItem entity);

	
	public CbmCReportItem update(CbmCReportItem entity);

	public CbmCReportItem findById(Long id);

	
	public List<CbmCReportItem> findByProperty(String propertyName, Object value);

	public PageObject findAll(String argFuzzy);
	
	public CbmCReportItemAdd findReportItemInfo(String reportItemId);

	public List<TreeNode> findReportItemTreeList(String itemCode,
			String enterpriseCode);
	
	public List<ReportItemForm> getReportItemListByYear(String dateTime,String reportType);
	
	public List<ReportItemForm> getReportItemListByMonthRoll(String dateTime,String reportType);
}