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
public interface RunCWorkticketWorktypeFacadeRemote {

	/**
	 * 增加一条工作类型记录
	 * 
	 * @param entity
	 *            工作类型对象
	 * @return RunCWorkticketWorktype 工作类型对象
	 * @throws CodeRepeatException
	 */
	public RunCWorkticketWorktype save(RunCWorkticketWorktype entity)
			throws CodeRepeatException;

	/**
	 * 删除一条工作类型记录
	 * 
	 * @param workticketTypeCode
	 *            工作类型id
	 */
	public void delete(Long worktypeId) throws CodeRepeatException;

	/**
	 * 批量删除工作类型记录
	 * 
	 * @param contentKeyIds
	 *            工作类型id e.g:"1","2",...
	 */
	public void deleteMulti(String worktypeIds);

	/**
	 * 修改一条工作类型记录
	 * 
	 * @param entity
	 *            工作类型对象
	 * @return RunCWorkticketType 工作类型对象
	 */
	public RunCWorkticketWorktype update(RunCWorkticketWorktype entity)
			throws CodeRepeatException;

	/**
	 * 查找一条工作类型记录
	 * 
	 * @param id
	 *            工作类型编码
	 * @return
	 */
	public RunCWorkticketWorktype findById(Long id);

	/**
	 * 查询工作类型列表
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            动态参数（开始行数和查询行数）
	 * @return
	 */
	public PageObject findAll(String enterpriseCode, String fuzzy,
			final int... rowStartIdxAndCount);

	/**
	 * 根据工作票类别编码与工作类别名称查询工作类型列表
	 * 
	 * @param String
	 *            enterpriseCode 企业编码
	 * @param String
	 *            workticketTypeCode 工作票类别编码
	 * @param String
	 *            fuzzy 工作类型名称
	 * @return List<RunCWorkticketWorktype> 工作类型列表
	 */
	public List<RunCWorkticketWorktype> findByWorkticketTypeCodeAndName(
			String enterpriseCode, String workticketTypeCode, String fuzzy);

	/**
	 * 根据工作票类别编码（包括公共类型编码）查询工作类型列表
	 * 
	 * @param String
	 *            enterpriseCode 企业编码
	 * @param String
	 *            workticketTypeCode 工作票类别编码
	 * @return List<RunCWorkticketWorktype> 工作类型列表
	 */
	public List<RunCWorkticketWorktype> findByWorkticketTypeCode(
			String enterpriseCode, String workticketTypeCode);
}