package power.ejb.workticket.business;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJWorkticketActorsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJWorkticketActorsFacadeRemote {
	
	public RunJWorkticketActors save(RunJWorkticketActors entity) throws CodeRepeatException;

	
	public void delete(Long id) throws CodeRepeatException;
	public void deleteMulti(String ids);

	public RunJWorkticketActors update(RunJWorkticketActors entity) throws CodeRepeatException;

	public RunJWorkticketActors findById(Long id);


	public PageObject findAll(String enterpriseCode,String workticketNo,final int... rowStartIdxAndCount);
}