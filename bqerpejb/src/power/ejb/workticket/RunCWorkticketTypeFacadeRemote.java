package power.ejb.workticket;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunCWorkticketTypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCWorkticketTypeFacadeRemote {

	/**
	 * 增加一条工作票类型记录
	 * @param entity 工作票类型对象
	 * @return  RunCWorkticketType  工作票类型对象
	 * @throws CodeRepeatException
	 */
	public RunCWorkticketType save(RunCWorkticketType entity) throws CodeRepeatException;

	/**
	 * 删除一条工作票类型记录
	 * @param workticketTypeId 工作票类型id
	 * @throws CodeRepeatException
	 */
	public void delete(Long workticketTypeId) throws CodeRepeatException;

	/**
	 * 批量删除
	 * @param workticketTypeIds  工作票类型id e.g:1,2,...
	 */
	public void deleteMulti(String workticketTypeIds);
	
	/**
	 * 修改一条工作票类型记录
	 * @param entity 工作票类型对象
	 * @return RunCWorkticketType 工作票类型对象
	 * @throws CodeRepeatException
	 */
	public RunCWorkticketType update(RunCWorkticketType entity) throws CodeRepeatException;

	/**
	 * 查找一条工作票类型记录
	 * @param id 工作票类型编码
	 * @return
	 */
	public RunCWorkticketType findById(Long id);
	
	/**
	 * 查询工作票类型列表
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
	 * @return
	 */
	public PageObject findAll(String enterpriseCode, final int... rowStartIdxAndCount);
}