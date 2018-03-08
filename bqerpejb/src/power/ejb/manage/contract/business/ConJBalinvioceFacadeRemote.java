package power.ejb.manage.contract.business;

import java.util.List;
import javax.ejb.Remote;

import power.ejb.manage.contract.form.BalinvioceForm;

/**
 * Remote interface for ConJBalinvioceFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface ConJBalinvioceFacadeRemote {
	/**增加
	 * 
	 */
	public void save(ConJBalinvioce entity);
	public void save(List<ConJBalinvioce> addList);

	/**删除
	 *
	 */
	public boolean delete(String ids) ;

	/**修改
	 * 
	 */
	public ConJBalinvioce update(ConJBalinvioce entity);
	public void update(List<ConJBalinvioce> updateList) ;

	public ConJBalinvioce findById(Long id);
	public List<BalinvioceForm> findByBalanceId(String enterpriseCode,Long balanceId);
}