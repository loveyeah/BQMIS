package power.ejb.manage.client;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 合作伙伴联系人管理
 * @author YWLiu
 *
 */
@Remote
public interface ConJClientsContactFacadeRemote {
	
	/**
	 * 保存一条合作伙伴联系人信息
	 * @param entity
	 */
	public void save(ConJClientsContact entity);

	/**
	 * 删除合作伙伴联系人信息
	 * @param contactId 联系人ID
	 */
	public Integer delete(String contactId);

	/**
	 * 修改一条合作伙伴联系人信息
	 * @param entity
	 * @return ConJClientsContact
	 */
	public ConJClientsContact update(ConJClientsContact entity);

	/**
	 * 根据联系人ID查询对应的合作伙伴信息
	 * @param id
	 * @return
	 */
	public ConJClientsContact findById(Long id);

	public List<ConJClientsContact> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);
	
	/**
	 * 查询合作伙伴联系人信息
	 * @param contactNameOrclientName
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	public PageObject findAll(String contactNameOrclientName ,String enterpriseCode ,String clientId, int... rowStartIdxAndCount);
}