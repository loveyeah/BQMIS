package power.ejb.workticket.business;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote 动火票测量
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJWorkticketMeasureFacadeRemote {

	public RunJWorkticketMeasure save(RunJWorkticketMeasure entity);

	public RunJWorkticketMeasure update(RunJWorkticketMeasure entity);

	public RunJWorkticketMeasure findById(Long id);
	public void delete(Long id);


}