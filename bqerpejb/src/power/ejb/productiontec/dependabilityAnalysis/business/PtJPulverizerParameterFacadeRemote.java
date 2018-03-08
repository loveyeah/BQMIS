package power.ejb.productiontec.dependabilityAnalysis.business;

import java.util.List;
import javax.ejb.Remote;

/**
 * 磨煤机技术参数
 * @author liuyi 091020
 */
@Remote
public interface PtJPulverizerParameterFacadeRemote {
	
	public void save(PtJPulverizerParameter entity);

	
	public void delete(PtJPulverizerParameter entity);

	public PtJPulverizerParameter update(PtJPulverizerParameter entity);

	public PtJPulverizerParameter findById(Long id);

	public List<PtJPulverizerParameter> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<PtJPulverizerParameter> findAll(int... rowStartIdxAndCount);
}