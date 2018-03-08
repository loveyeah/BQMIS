package power.ejb.productiontec.dependabilityAnalysis;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for KksCMainsTransformerFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface KksCMainsTransformerFacadeRemote {
	
	public void save(KksCMainsTransformer entity);

	
	public void delete(KksCMainsTransformer entity);

	
	public KksCMainsTransformer update(KksCMainsTransformer entity);

	public KksCMainsTransformer findById(String id);

	
	
	public List<KksCMainsTransformer> findAll(int... rowStartIdxAndCount);
}