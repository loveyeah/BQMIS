package power.ejb.productiontec.dependabilityAnalysis.business;

import java.util.List;
import javax.ejb.Remote;

/**
 * 锅炉信息
 * @author liuyi 091019
 */
@Remote
public interface PtKkxBoilerInfoFacadeRemote {
	/**
	 * 新增一条锅炉信息数据
	 */
	public void save(PtKkxBoilerInfo entity);

	/**
	 * 删除一条锅炉信息数据
	 */
	public void delete(PtKkxBoilerInfo entity);

	/**
	 * 更新一条锅炉信息数据
	 */
	public PtKkxBoilerInfo update(PtKkxBoilerInfo entity);

	public PtKkxBoilerInfo findById(Long id);

	
	public List<PtKkxBoilerInfo> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	
	public List<PtKkxBoilerInfo> findAll(int... rowStartIdxAndCount);
}