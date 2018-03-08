package power.ejb.productiontec.dependabilityAnalysis.business;

import java.util.List;

import javax.ejb.Remote;


/**
 * Remote interface for PtJFeedpumpParameterFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJFeedpumpParameterFacadeRemote {

	/**
	 * 增加一条给水泵组技术参数记录
	 * @param entity
	 */
	public void save(PtJFeedpumpParameter entity);

	/**
	 * 删除一条或多条给水泵组技术参数记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条给水泵组技术参数记录信息
	 * @param entity
	 * @return
	 */
	public PtJFeedpumpParameter update(PtJFeedpumpParameter entity);

	/**
	 * 根据ID查找一条给水泵组技术参数详细信息
	 * @param id
	 * @return
	 */
	public PtJFeedpumpParameter findById(Long id);

	public List<PtJFeedpumpParameter> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount);
	
}