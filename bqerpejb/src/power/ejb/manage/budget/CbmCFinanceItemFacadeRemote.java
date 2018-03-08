package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ejb.comm.TreeNode;

/**
 * Remote interface for CbmCFinanceItemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmCFinanceItemFacadeRemote {

	public CbmCFinanceItem save(CbmCFinanceItem entity);

	public void delete(CbmCFinanceItem entity);

	public CbmCFinanceItem update(CbmCFinanceItem entity);

	public CbmCFinanceItem findById(Long id);

	public List<CbmCFinanceItem> findByProperty(String propertyName,
			Object value);

	public List<CbmCFinanceItem> findByFinanceName(Object financeName);

	public List<CbmCFinanceItem> findByUpperItem(Object upperItem);

	public List<CbmCFinanceItem> findByIsUse(Object isUse);

	public List<CbmCFinanceItem> findByEnterpriseCode(Object enterpriseCode);

	public List<CbmCFinanceItem> findAll();

	public List<TreeNode> findFinanceItemTreeList(String pid,
			String EnterpriseCode);

	public List<CbmCFinanceItem> findByItemId(Long id);

	public CbmCFinanceItem findByFinanceId(Long id);

}