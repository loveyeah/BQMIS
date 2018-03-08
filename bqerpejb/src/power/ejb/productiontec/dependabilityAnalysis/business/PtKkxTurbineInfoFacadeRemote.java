package power.ejb.productiontec.dependabilityAnalysis.business;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for PtKkxTurbineInfoFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtKkxTurbineInfoFacadeRemote {
	
	/**
	 * 新增一条汽轮机信息数据
	 */
	public void save(PtKkxTurbineInfo entity);

	/**
	 * 删除一条汽轮机信息数据
	 */
	public void delete(PtKkxTurbineInfo entity);

	/**
	 * 更新一条汽轮机信息数据
	 */
	public PtKkxTurbineInfo update(PtKkxTurbineInfo entity);

	public PtKkxTurbineInfo findById(Long id);

	public List<PtKkxTurbineInfo> findByProperty(String propertyName,
			Object value);

	public List<PtKkxTurbineInfo> findAll();
}