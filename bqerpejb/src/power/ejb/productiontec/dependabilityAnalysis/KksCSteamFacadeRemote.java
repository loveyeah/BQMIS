package power.ejb.productiontec.dependabilityAnalysis;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for KksCSteamFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface KksCSteamFacadeRemote {
	
	public void save(KksCSteam entity);

	
	public void delete(KksCSteam entity);

	
	public KksCSteam update(KksCSteam entity);

	public KksCSteam findById(String id);

	
	public List<KksCSteam> findAll(int... rowStartIdxAndCount);
}