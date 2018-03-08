package power.ejb.workticket;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 标识牌维护
 */
@Remote
public interface RunCWorkticketMarkcardFacadeRemote {
	/**
	 * 增加标识牌
	 * 
	 * @param entity
	 *            标识牌维护对象
	 * @return RunCWorkticketMarkcard 标识牌维护实体
	 */
	public RunCWorkticketMarkcard save(RunCWorkticketMarkcard entity)
			throws CodeRepeatException;

	/**
	 * 删除标识牌
	 * 
	 * @param markcardId
	 *            标识牌ID
	 */
	public void delete(Long markcardId);

	/**
	 * 批量删除标识牌
	 * 
	 * @param markcardIds
	 *            标识牌ID集合(1,2,3,e.g.)
	 */
	public void deleteMulti(String markcardIds);

	/**
	 * 修改标识牌
	 * 
	 * @param entity
	 *            标识牌维护对象
	 * @return RunCWorkticketMarkcard 标识牌维护实体
	 */
	public RunCWorkticketMarkcard update(RunCWorkticketMarkcard entity)
			throws CodeRepeatException;

	/**
	 * 查找标识牌
	 * 
	 * @param id
	 *            标识牌ID
	 * @return RunCWorkticketMarkcard 标识牌维护实体
	 */
	public RunCWorkticketMarkcard findById(Long id);

	/**
	 * 查询标识牌列表
	 * 
	 * @param enterpriseCode
	 *            企业编码*
	 * @param markcardTypeID
	 *            标识牌类型编码
	 * @param rowStartIdxAndCount
	 *            动态参数（开始行数和查询行数）
	 * @return
	 */
	public PageObject findAll(String enterpriseCode, Long markcardTypeID,
			String fuzzy, final int... rowStartIdxAndCount);
}