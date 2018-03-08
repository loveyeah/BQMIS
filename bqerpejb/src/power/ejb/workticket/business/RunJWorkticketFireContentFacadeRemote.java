package power.ejb.workticket.business;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJWorkticketFireContentFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJWorkticketFireContentFacadeRemote {

	/**
	 * 增加一条动火票作业内容记录
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public RunJWorkticketFireContent save(RunJWorkticketFireContent entity) throws CodeRepeatException;

	/**
	 * 删除一条动火票作业内容记录
	 * @param id
	 */
	public void delete(Long id);
	/**
	 * 批量删除动火票作业内容记录
	 * @param ids
	 */
	public void deleteMutil(String ids);

	
	public RunJWorkticketFireContent update(RunJWorkticketFireContent entity);

	public RunJWorkticketFireContent findById(Long id);
	
	/**
	 * 查询动火票作业方式列表
	 * @param workticketNo 工作票号
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
	 * @return
	 */
	public PageObject findAll(String workticketNo,String enterpriseCode,
			final int... rowStartIdxAndCount);

	


	

}