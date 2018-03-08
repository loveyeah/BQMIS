package power.ejb.workticket.business;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for RunJWorkticketMapFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJWorkticketMapFacadeRemote {

	public void save(RunJWorkticketMap entity);

	
	public void delete(RunJWorkticketMap entity);

	
	public RunJWorkticketMap update(RunJWorkticketMap entity);

	public RunJWorkticketMap findById(Long id);

	
	public List<RunJWorkticketMap> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<RunJWorkticketMap> findAll(int... rowStartIdxAndCount);
	
	/**
	 * 通过工作票票号查找一条记录
	 * @param workticketNo
	 * @return
	 */
	public RunJWorkticketMap findByWorkticketNo(String workticketNo);
}