package power.ejb.manage.client;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 合作伙伴类型维护
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface ConCClientsTypeFacadeRemote {
	/**
	 * 增加一条合作伙伴类型记录
	 * @param entity
	 * @return
	 */
	public ConCClientsType save(ConCClientsType entity) throws CodeRepeatException;

	/**
	 * 删除一条或多条合作伙伴类型记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条合作伙伴类型记录
	 * @param entity
	 * @return
	 */
	public ConCClientsType update(ConCClientsType entity) throws CodeRepeatException;

	/**
	 * 根据ID查找详细信息
	 * @param id
	 * @return
	 */
	public ConCClientsType findById(Long id);

	/**
	 * 根据合作伙伴类型名称查询列表信息
	 * @param typeName
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String typeName,String enterpriseCode, final int... rowStartIdxAndCount);
}