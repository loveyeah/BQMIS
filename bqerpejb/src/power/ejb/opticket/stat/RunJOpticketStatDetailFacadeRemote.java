package power.ejb.opticket.stat;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for RunJOpticketStatDetailFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJOpticketStatDetailFacadeRemote {
	 
	public void save(RunJOpticketStatDetail entity);

	 
	public void delete(RunJOpticketStatDetail entity);

	 
	public RunJOpticketStatDetail update(RunJOpticketStatDetail entity);

	public RunJOpticketStatDetail findById(Long id); 
}