package power.ejb.run.repair;

import java.text.ParseException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJRepairProjectDetailFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJRepairProjectDetailFacadeRemote {

	/**
	 * 批量增加修改操作明细记录
	 * @param addList
	 * @param updateList
	 * @throws ParseException
	 */
	public  void saveRepairDetail(List<RunJRepairProjectDetail> addList,List<RunJRepairProjectDetail> updateList) throws ParseException;
	
	public void save(RunJRepairProjectDetail entity);

	public void deleteMult(String ids);

	public RunJRepairProjectDetail update(RunJRepairProjectDetail entity);

	public RunJRepairProjectDetail findById(Long id);
	
	/**
	 * 根据主表ID查询明细列表
	 * @param repairMainId
	 * @param enterPriseCode
	 * @return
	 */
	public PageObject getRepairDetailList(String repairMainId,String enterPriseCode);
}