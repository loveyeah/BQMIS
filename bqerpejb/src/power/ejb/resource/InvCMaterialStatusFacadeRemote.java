/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for InvCMaterialStatusFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvCMaterialStatusFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved InvCMaterialStatus
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            InvCMaterialStatus entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvCMaterialStatus entity);

	/**
	 * Delete a persistent InvCMaterialStatus entity.
	 * 
	 * @param entity
	 *            InvCMaterialStatus entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvCMaterialStatus entity);

	/**
	 * Persist a previously saved InvCMaterialStatus entity and return it or a
	 * copy of it to the sender. A copy of the InvCMaterialStatus entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            InvCMaterialStatus entity to update
	 * @return InvCMaterialStatus the persisted InvCMaterialStatus entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvCMaterialStatus update(InvCMaterialStatus entity);

	public InvCMaterialStatus findById(Long id);

	/**
	 * Find all InvCMaterialStatus entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvCMaterialStatus property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCMaterialStatus> found by query
	 */
	public List<InvCMaterialStatus> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all InvCMaterialStatus entities.
	 * 
	 * @return List<InvCMaterialStatus> all InvCMaterialStatus entities
	 */
	public List<InvCMaterialStatus> findAll();
	/**
	 * 查询物料状态码	
	 * 检索项目：				
	 *	     物料状态码.编码			
	 *	     物料状态码.名称			
	 *	     物料状态码.描述						
	 * 检索数据库表				
	 *	     物料状态码			
	 * 检索条件：				
	 *		是否使用 = 'Y'		
	 * @return PageObject
	 */
	public PageObject getMaterialStatusList(String enterpriseCode, final int... rowStartIdxAndCount);
	/**
	 * [编码]在[物料状态码]表中作唯一性check
	 *  @param typeNo	 画面.编码
	 *  @return boolean			
	 */
	public boolean isStatusNoExist(String enterpriseCode, String statusNo);
	/**
	 * [名称]在[物料状态码]表中作唯一性check
	 *  @param typeName	 画面.名称
	 *  @return boolean			
	 */	
	public boolean isStatusNameExist(String enterpriseCode, String statusName);
}