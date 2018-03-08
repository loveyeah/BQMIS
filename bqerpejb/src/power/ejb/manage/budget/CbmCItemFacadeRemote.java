package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ejb.manage.budget.form.CbmItemForm;

/**
 * Remote interface for CbmCItemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmCItemFacadeRemote {

	public CbmCItem save(CbmCItem entity);

	public void delete(CbmCItem entity);

	public CbmCItem update(CbmCItem entity);

	public CbmCItem findById(Long id);

	/**
	 * 
	 * @param itemCode
	 *            指标编码
	 * @return
	 */
	public boolean findByCode(String itemCode);

	/**
	 * 
	 * @param ItemxId
	 * @return
	 */
	public List<CbmItemForm> findByItemId(String ItemxId);

	/**
	 * 预算计算等级
	 * 
	 * @param id
	 * @return
	 */
	public Long getaccountOrder(Long id);

	/**
	 * 实际计算等级
	 * 
	 * @param id
	 * @return
	 */
	public Long getfactOrder(Long id);

}