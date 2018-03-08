package power.ejb.manage.client;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 评价项目设置维护
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface ConCAppraiseItemFacadeRemote {
	
	/**
	 * 增加一条评价项目设置记录
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public ConCAppraiseItem save(ConCAppraiseItem entity) throws CodeRepeatException;

	/**
	 * 删除一条或多条评价项目设置记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条评价项目设置记录
	 * @param entity
	 * @return
	 */
	public ConCAppraiseItem update(ConCAppraiseItem entity) throws CodeRepeatException;
	
	/**
	 * 点“确定”按钮设置表中所有记录
	 */
	public void confirmMulti();

	/**
	 * 根据ID查找信息
	 * @param id
	 * @return
	 */
	public ConCAppraiseItem findById(Long id);

	/**
	 * 根据评价项目名称查询列表记录
	 * @param appraiseItem
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String appraiseItem,String enterpriseCode, final int... rowStartIdxAndCount);
}