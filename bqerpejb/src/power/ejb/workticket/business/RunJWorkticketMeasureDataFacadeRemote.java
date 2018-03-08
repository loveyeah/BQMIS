package power.ejb.workticket.business;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote  动火票测量数据
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJWorkticketMeasureDataFacadeRemote {

	public RunJWorkticketMeasureData save(RunJWorkticketMeasureData entity);
	public RunJWorkticketMeasureData update(RunJWorkticketMeasureData entity);
	public void deleteMulti(String ids);
	public RunJWorkticketMeasureData findById(Long id);
	public PageObject findAllByWorkticketNo(String workticketNo,final int... rowStartIdxAndCount);

}