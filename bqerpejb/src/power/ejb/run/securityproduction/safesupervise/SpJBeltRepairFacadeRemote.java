package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for SpJBeltRepairFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJBeltRepairFacadeRemote {

	public void save(SpJBeltRepair entity);

	
	public void delete(SpJBeltRepair entity);

	
	public SpJBeltRepair update(SpJBeltRepair entity);

	public SpJBeltRepair findById(Long id);

	public PageObject  getBeltRepair( String workCode,String beginTime,String endTime,String enterPriseCode,final int... rowStartIdxAndCount);
	public SpJBeltRepair addBeltRepair(SpJBeltRepair entity);
	public SpJBeltRepair updateBeltRepair(SpJBeltRepair entity);
	
	public void deleteBeltRepair(String ids);
	
	public List<SpJBeltRepair> findAll(int... rowStartIdxAndCount);
}