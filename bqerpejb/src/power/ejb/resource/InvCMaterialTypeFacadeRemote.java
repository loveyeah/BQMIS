/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for InvCMaterialTypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvCMaterialTypeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved InvCMaterialType entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            InvCMaterialType entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvCMaterialType entity);

	/**
	 * Delete a persistent InvCMaterialType entity.
	 * 
	 * @param entity
	 *            InvCMaterialType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvCMaterialType entity);

	/**
	 * Persist a previously saved InvCMaterialType entity and return it or a
	 * copy of it to the sender. A copy of the InvCMaterialType entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            InvCMaterialType entity to update
	 * @return InvCMaterialType the persisted InvCMaterialType entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvCMaterialType update(InvCMaterialType entity);

	public InvCMaterialType findById(Long id);

	/**
	 * Find all InvCMaterialType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvCMaterialType property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCMaterialType> found by query
	 */
	public List<InvCMaterialType> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all InvCMaterialType entities.
	 * 
	 * @return List<InvCMaterialType> all InvCMaterialType entities
	 */
	public List<InvCMaterialType> findAll();
	/**
	 * 查询物料类型	
	 * 检索项目：				
	 *	     物料类型.编码			
	 *	     物料类型.名称			
	 *	     物料类型.描述						
	 * 检索数据库表				
	 *	     物料类型			
	 * 检索条件：				
	 *		是否使用 = 'Y'		
	 * @return PageObject
	 */
	public PageObject getMaterialTypeList(String enterpriseCode, final int... rowStartIdxAndCount);
	/**
	 * [编码]在[物料类型]表中作唯一性check
	 *  @param typeNo	 画面.编码
	 *  @return boolean			
	 */
	public boolean isTypeNoExist(String enterpriseCode, String typeNo);
	/**
	 * [名称]在[物料类型]表中作唯一性check
	 *  @param typeName	 画面.名称
	 *  @return boolean			
	 */	
	public boolean isTypeNameExist(String enterpriseCode, String typeName);
}