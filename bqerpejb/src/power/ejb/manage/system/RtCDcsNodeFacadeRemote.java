package power.ejb.manage.system;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RtCDcsNodeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RtCDcsNodeFacadeRemote {

	/**
	 * 增加
	 * 
	 * @param entity
	 * @return
	 */
	public RtCDcsNode save(RtCDcsNode entity) throws CodeRepeatException;

	/**
	 * 删除
	 * 
	 * @param entity
	 */
	public void delete(RtCDcsNode entity);

	/**
	 * 修改
	 * 
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public RtCDcsNode update(RtCDcsNode entity) throws CodeRepeatException;

	/**
	 * 主键查找
	 * 
	 * @param id
	 * @return
	 */
	public RtCDcsNode findById(String id);

	/**
	 * 查询
	 * 
	 * @param enterpriseCode
	 * @param fuzzy
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findDcsNodeList(String enterpriseCode, String sys,
			String queryKey, final int... rowStartIdxAndCount);
}