package power.ejb.manage.contract;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for ConCItemSourceFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface ConCItemSourceFacadeRemote {

	/**
	 * 增加一条费用来源记录
	 * @param entity
	 * @return
	 */
	public ConCItemSource save(ConCItemSource entity);

	/**
	 * 删除一条费用来源记录
	 * @param entity
	 */
	public void delete(ConCItemSource entity);

	/**
	 * 修改一条费用来源记录信息
	 * @param entity
	 * @return
	 */
	public ConCItemSource update(ConCItemSource entity);

	public ConCItemSource findById(Long id);
	
	/**
	 * 通过ID查找一条费用来源记录的详细信息
	 * @param id
	 * @return
	 */
	public ConCItemSource findInfoById(Long id);

	/**
	 * 通过费用来源父ID查找记录
	 * @param PItemId
	 * @return
	 */
	public List<ConCItemSource> findByPItemId(Long PItemId);
	
	
	public boolean getByPItemId(Long PItemId);
	
	// add by liuyi 
	public ConCItemSource getRecordByCode(String code);
}