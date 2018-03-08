package power.ejb.resource;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for InvCReasonFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvCReasonFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved InvCReason entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            InvCReason entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvCReason entity);

	/**
	 * Delete a persistent InvCReason entity.
	 * 
	 * @param entity
	 *            InvCReason entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvCReason entity);

	/**
	 * Persist a previously saved InvCReason entity and return it or a copy of
	 * it to the sender. A copy of the InvCReason entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            InvCReason entity to update
	 * @return InvCReason the persisted InvCReason entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvCReason update(InvCReason entity);

	public InvCReason findById(Long id);

	/**
	 * Find all InvCReason entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvCReason property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCReason> found by query
	 */
	public List<InvCReason> findByProperty(String propertyName, Object value);

	/**
	 * Find all InvCReason entities.
	 * 
	 * @return List<InvCReason> all InvCReason entities
	 */
	public List<InvCReason> findAll();
	/**
	 * Find all InvCReason entities with a specific property value 
	 * 
	 * @param propertyName
	 *            the name of the InvCReason property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCReason> found by query
	 */
	public PageObject findByTransId(String transId,String enterpriseCode,final int... rowStartIdxAndCount);
	/**
	 * 查询事物原因码是否是唯一的
	 * 
	 * @param reansonCode
	 * @param enterpriseCode          
	 * @return boolean
	 */
	public boolean checkReasonCode(String reansonCode,String enterpriseCode);
	/**
	 * 查询事物原因名称是否是唯一的
	 * 
	 * @param reansonName
	 * @param enterpriseCode          
	 * @return boolean
	 */
	public boolean checkReasonName(String reansonName,String enterpriseCode);
	/**
     * 删除对应一个事务作用对应的所有事务原因码记录
     *
     * @param transId 流水号
     * @param workerCode 登录者id
     * @throws CodeRepeatException
     * @throws RuntimeException
     *             when the operation fails
     */
    public void deleteByTransId(String transId, String workerCode) throws CodeRepeatException ;
    /**
	 * 更新
     * 查询事物原因名称是否是唯一的
     * @param reasonName
     * @
     * @return list 
     */
	public InvCReason checkReasonNameForUpdate(String reasonName, String enterpriseCode);
}