package power.ejb.productiontec.dependabilityAnalysis.business;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for PtJBlowerParameterFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJBlowerParameterFacadeRemote {
	
	public void save(PtJBlowerParameter entity);

	
	public void delete(PtJBlowerParameter entity);

	
	public PtJBlowerParameter update(PtJBlowerParameter entity);

	public PtJBlowerParameter findById(Long id);

	
	public List<PtJBlowerParameter> findByProperty(String propertyName,
			Object value);

	public List<PtJBlowerParameter> findAll();
}