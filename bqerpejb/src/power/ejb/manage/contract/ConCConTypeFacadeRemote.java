package power.ejb.manage.contract;

import java.util.List;
import javax.ejb.Remote;

import power.ejb.manage.contract.form.ContypeInfo;

/**
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface ConCConTypeFacadeRemote {
	/**
	 *
	 */
	public ConCConType save(ConCConType entity);

	/**
	 * 
	 */
	public void delete(ConCConType entity);

	/**
	 * 
	 */
	public ConCConType update(ConCConType entity);

	public ContypeInfo findInfoById(Long id);
	public ConCConType findById(Long id);

	/**
	 * 根据
	 */
	public List<ConCConType> findByPContypeId(Long PContypeId);
	public boolean getByPContypeId(Long PContypeId);
}