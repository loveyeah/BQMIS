package power.ejb.manage.client;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 合作伙伴性质维护
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface ConCClientsCharacterFacadeRemote {
	/**
	 * 增加一条合作伙伴性质记录
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public ConCClientsCharacter save(ConCClientsCharacter entity) throws CodeRepeatException;

	/**
	 * 删除一条或多条合作伙伴性质记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条合作伙伴性质记录
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public ConCClientsCharacter update(ConCClientsCharacter entity) throws CodeRepeatException;

	/**
	 * 根据ID查找详细信息
	 * @param id
	 * @return
	 */
	public ConCClientsCharacter findById(Long id);

	/**
	 * 根据合作伙伴性质名称查询列表
	 * @param characterName
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String characterName,String enterpriseCode, int... rowStartIdxAndCount);
}