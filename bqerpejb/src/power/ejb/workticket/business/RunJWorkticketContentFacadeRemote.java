package power.ejb.workticket.business;

import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJWorkticketContentFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJWorkticketContentFacadeRemote {
	/**
	 * 增加一条工作票内容记录
	 * 
	 * @param entity 工作票工作票内容对象
	 * @throws CodeRepeatException
	 */
	public RunJWorkticketContent save(RunJWorkticketContent entity);
	
	//public void modifyRecords(List<RunJWorkticketContent> list,String delIds); //delete by fyyang 090523
	/**
	 * 增删改工作内容
	 * add by fyyang 090523
	 */
	 public void modifyRecords(String workticketNo,List<RunJWorkticketContent> list,String delIds);
	/**
	 * 删除一条工作票内容记录
	 * @param Id 工作票内容Id
	 */
	public void delete(Long Id);

	/**
	 * 删除多条工作票内容记录
	 * @param Ids 工作票内容Ids
	 * @throws CodeRepeatException
	 */
	public void deleteMulti(String Ids);
	/**
	 * 修改一条工作票内容记录
	 * @param entity 
	 * @return RunJWorkticketContent
	 */
	public RunJWorkticketContent update(RunJWorkticketContent entity);

	/**
	 * 查找一条工作票内容记录
	 * @param id 工作票内容关键词id
	 * @return RunCWorktickSafety
	 */	
	public RunJWorkticketContent findById(Long id);

	/**
	 * 删除一条工作票内容记录
	 * @param workticketNO 工作票编号
	 */
	public void deleteByNO(String workticketNO);
	
	/**
	 * 查询工作票内容列表
	 * @param workticketTypeCode 工作票类型编码
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
	 * @return PageObject
	 * @throws ParseException
	 */
	public PageObject findAll(String enterpriseCode,String workticketNO,final int... rowStartIdxAndCount);
	
	/**
	 * 获得工作票的内容
	 * @param workticketNo
	 * @return
	 */
	public String getWorkticketContent(String workticketNo);
	
	/**
	 * 回写主表工作内容
	 * add by fyyang 090513
	 * @param workticketNo
	 */
	public void updateWorkticketsContent(String workticketNo);
	
}