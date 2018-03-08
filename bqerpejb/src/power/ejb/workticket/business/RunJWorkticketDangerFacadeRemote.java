package power.ejb.workticket.business;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJWorkticketDangerFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJWorkticketDangerFacadeRemote {
	
	public RunJWorkticketDanger save(RunJWorkticketDanger entity) throws CodeRepeatException;
	
	public void save(List<RunJWorkticketDanger> addList,List<RunJWorkticketDanger> updateList,String delIds);
	/**
	 * 删除控制措施
	 * @param controlIds
	 */
	public void deleteControlMulti(String controlIds);
	/**
	 * 删除危险点
	 * @param dangerIds
	 */
	public void deleteDangerMulti(String dangerIds);

	
	public RunJWorkticketDanger update(RunJWorkticketDanger entity) throws CodeRepeatException;

	public RunJWorkticketDanger findById(Long id);
	
	/**
	 * 获得工作票危险点信息
	 * @param workticketNo
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findDangerList(String workticketNo);
	
	/**
	 * 获得控制措施信息
	 * @param dangerId
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findDangerControlList(String dangerId,final int... rowStartIdxAndCount);

	/**
	 * 删除某票的全部危险点
	 * @param workticketNo
	 */
	public void deleteDangerByNo(String workticketNo);
}