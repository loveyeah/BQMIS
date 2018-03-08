package power.ejb.productiontec.dependabilityAnalysis;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for PtJHeaterParameterFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJHeaterParameterFacadeRemote {
	
	public PtJHeaterParameter save(PtJHeaterParameter entity);

	
	public void delete(PtJHeaterParameter entity);

	
	public PtJHeaterParameter update(PtJHeaterParameter entity);

	public PtJHeaterParameter findById(Long id);

	
	public List<PtJHeaterParameter> findByProperty(String propertyName,
			Object value);

	
	public List<PtJHeaterParameter> findAll();
	public List<PtJHeaterParameter> findInfoBypId(String pId);
}