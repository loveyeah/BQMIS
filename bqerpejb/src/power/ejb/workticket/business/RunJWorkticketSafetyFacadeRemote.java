package power.ejb.workticket.business;

import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJWorkticketSafetyFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJWorkticketSafetyFacadeRemote {
	/**
	 * 增加一条工作票安全措施明细记录
	 * 
	 * @param entity 工作票安全措施明细对象
	 * @throws CodeRepeatException
	 */
	public RunJWorkticketSafety save(RunJWorkticketSafety entity);

	/**
	 * 删除一条工作票安全措施明细记录
	 * @param Id 工作票安全措施明细Id
	 * @throws CodeRepeatException
	 */
	public void delete(Long Id);

	/**
	 * 删除多条工作票安全措施明细记录
	 * @param Ids 工作票安全措施明细Ids
	 * @throws CodeRepeatException
	 */
	public void deleteMulti(String Ids);
	
	/**
	 * 修改一条工作票安全措施明细记录
	 * @param entity 
	 * @return RunCWorkticketContentKey
	 * @throws CodeRepeatException
	 */
	public RunJWorkticketSafety update(RunJWorkticketSafety entity);

	/**
	 * 查找一条工作票安全措施明细记录
	 * @param id 工作票内容关键词id
	 * @return RunCWorktickSafety
	 */	
	public RunJWorkticketSafety findById(Long id);
	/**
	 * 查询工作票安全措施明细列表
	 * @param enterpriseCode  企业编码
	 * @param workticketNO 工作票编码
	 * @return List<RunJWorkticketSafety>
	 */
	public List<RunJWorkticketSafety> getSafetyBy(String enterpriseCode,String workticketNo);

	/**
	 * 查询工作票安全措施明细列表
	 * @param workticketNO 工作票编码
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
	 * @return PageObject
	 * @throws ParseException
	 */
	public PageObject findAll(String enterpriseCode,String workticketNO,String safetyCode,final int... rowStartIdxAndCount);
	
	/**
	 * 删除工作票安全措施明细记录
	 * @param workticketNO 工作票号 workticketNO
	 */
	public void deleteByNO(String workticketNO);
	
	/**
	 * 安措办理
	 * modify by fyyang 090311 安措执行内容不回写
	 * @param entity
	 */
	public void exeSafety(RunJWorkticketSafety entity);
	
	/**
	 * 工作票安措增删改的保存
	 * @param list
	 * @param delIds
	 */
	public void modifyRecords(List<RunJWorkticketSafety>list,String delIds);
}