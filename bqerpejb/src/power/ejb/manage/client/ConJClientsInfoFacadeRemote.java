package power.ejb.manage.client;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 合作伙伴信息管理
 * @author YWLiu
 *
 */
@Remote
public interface ConJClientsInfoFacadeRemote {
	
	/**
	 * 保存一条合作伙伴信息
	 * @param entity
	 */
	public ConJClientsInfo save(ConJClientsInfo entity) throws CodeRepeatException ;

	/**
	 * 
	 * @param cliendId
	 */
	public Integer delete(String cliendId);

	/**
	 * 
	 * @param entity
	 * @return
	 */
	public ConJClientsInfo update(ConJClientsInfo entity) throws CodeRepeatException ;

	/**
	 * 
	 * @param id
	 * @return
	 */
	public ConJClientsInfo findById(Long id);

	public List<ConJClientsInfo> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	
	public PageObject findAll(String clientCodeOrClientName, String approveFlag, String enterpriseCode, int... rowStartIdxAndCount);
}