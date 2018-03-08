package power.ejb.productiontec.dependabilityAnalysis.business;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for PtJPrecipitationParameterFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJPrecipitationParameterFacadeRemote {
	
	/**
	 * 增加一条电除尘参数记录
	 * @param entity
	 */
	public void save(PtJPrecipitationParameter entity);

    /**
     * 修改一条电除尘参数记录
     * @param entity
     * @return
     */
	public PtJPrecipitationParameter update(PtJPrecipitationParameter entity);

	/**
	 * 根据ID查找一条电除尘参数记录详细信息
	 * @param id
	 * @return
	 */
	public PtJPrecipitationParameter findById(Long id);

	/**
	 * 查找符合条件的电除尘参数记录列表
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public List<PtJPrecipitationParameter> findByProperty(String propertyName,
			Object value);
}