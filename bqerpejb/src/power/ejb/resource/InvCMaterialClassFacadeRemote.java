package power.ejb.resource;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for InvCMaterialClassFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvCMaterialClassFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved InvCMaterialClass entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            InvCMaterialClass entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvCMaterialClass entity);

	/**
	 * Delete a persistent InvCMaterialClass entity.
	 * 
	 * @param entity
	 *            InvCMaterialClass entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvCMaterialClass entity);

	/**
	 * Persist a previously saved InvCMaterialClass entity and return it or a
	 * copy of it to the sender. A copy of the InvCMaterialClass entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            InvCMaterialClass entity to update
	 * @return InvCMaterialClass the persisted InvCMaterialClass entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvCMaterialClass update(InvCMaterialClass entity) throws CodeRepeatException;

	public InvCMaterialClass findById(Long id);

	/**
	 * Find all InvCMaterialClass entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvCMaterialClass property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCMaterialClass> found by query
	 */
	public List<InvCMaterialClass> findByProperty(String propertyName,
			Object value);

	/**
	 * 根据父编码查询物料分类信息
	 * 
	 * @param parentClassNo 父编码
	 * @param enterpriseCode 企业编码
	 * @return PageObject 物料分类列表
	 */
	public PageObject findByParentClassNo(String parentClassNo, String enterpriseCode);

	/**
	 * 根据编码查询物料分类信息
	 * 
	 * @param classNo 编码
	 * @param enterpriseCode 企业编码
	 * @return PageObject 物料分类列表
	 */
	public PageObject findByClassNo(String classNo, String enterpriseCode);

	public List<InvCMaterialClass> findByClassName(Object className);

	public List<InvCMaterialClass> findByLastModifiedBy(Object lastModifiedBy);

	public List<InvCMaterialClass> findByEnterpriseCode(Object enterpriseCode);

	public List<InvCMaterialClass> findByIsUse(Object isUse);

	/**
	 * Find all InvCMaterialClass entities.
	 * 
	 * @return List<InvCMaterialClass> all InvCMaterialClass entities
	 */
	public List<InvCMaterialClass> findAll();
	
	/**
	 * 根据编码查找父编码和父名称
	 * 
	 * @param classNo 编码
	 * @param enterpriseCode 企业编码
	 */
	@SuppressWarnings("unchecked")
	public List findByParentCodeName(String classNo, String enterpriseCode);

	/**
	 * 根据编码查找孩子结点
	 * 
	 * @param classNo 编码
	 * @param enterpriseCode 企业编码
	 */
	@SuppressWarnings("unchecked")
	public List findChildNode(String classNo, String enterpriseCode);

	/**
	 * 检查编码是否唯一
	 * 
	 * @param classNo 编码
	 * @param enterpriseCode 企业编码
	 */
	@SuppressWarnings("unchecked")
	public boolean checkClassNo(String classNo, String enterpriseCode);
	
	/**
	 * 查找该当物料分类节点下所有的子节点
	 * 
	 * @param classNo 物料分类编码
	 * @param enterpriseCode 企业编码
	 * @return 返回该当物料分类节点下所有的子节点和自己
     * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List findAllChildrenNode(String classNo, String enterpriseCode);
	
	 /**    
     * 通过物料分类id查找其祖先编码
     * add by fyyang 090519
     * @return 
     */
	public String getAllParentCode(Long classId);
	

}