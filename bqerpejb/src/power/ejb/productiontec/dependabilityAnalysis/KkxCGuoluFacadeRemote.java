package power.ejb.productiontec.dependabilityAnalysis;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for KkxCGuoluFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface KkxCGuoluFacadeRemote {
	
	public void save(KkxCGuolu entity);

	
	public void delete(KkxCGuolu entity);

	
	public KkxCGuolu update(KkxCGuolu entity);

	public KkxCGuolu findById(String id);

	

	public List<KkxCGuolu> findAll(int... rowStartIdxAndCount);
}