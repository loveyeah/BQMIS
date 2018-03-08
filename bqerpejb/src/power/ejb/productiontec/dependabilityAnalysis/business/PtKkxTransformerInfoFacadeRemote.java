package power.ejb.productiontec.dependabilityAnalysis.business;

import java.util.List;
import javax.ejb.Remote;

/**
 * 主变压器信息
 * @author liuyi 091020
 */
@Remote
public interface PtKkxTransformerInfoFacadeRemote {
	
	public void save(PtKkxTransformerInfo entity);

	public void delete(PtKkxTransformerInfo entity);

	
	public PtKkxTransformerInfo update(PtKkxTransformerInfo entity);

	public PtKkxTransformerInfo findById(Long id);

	
	public List<PtKkxTransformerInfo> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	
	public List<PtKkxTransformerInfo> findAll(int... rowStartIdxAndCount);
}