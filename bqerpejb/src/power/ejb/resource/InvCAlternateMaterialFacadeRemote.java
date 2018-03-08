package power.ejb.resource;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.Remote;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ejb.hr.LogUtil;

/**
 * Remote interface for InvCAlternateMaterialFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvCAlternateMaterialFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved InvCAlternateMaterial
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            InvCAlternateMaterial entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvCAlternateMaterial entity);

	/**
	 * Delete a persistent InvCAlternateMaterial entity.
	 * 
	 * @param entity
	 *            InvCAlternateMaterial entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvCAlternateMaterial entity);

	/**
	 * Persist a previously saved InvCAlternateMaterial entity and return it or
	 * a copy of it to the sender. A copy of the InvCAlternateMaterial entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            InvCAlternateMaterial entity to update
	 * @return InvCAlternateMaterial the persisted InvCAlternateMaterial entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvCAlternateMaterial update(InvCAlternateMaterial entity);

	public InvCAlternateMaterial findById(Long id);

	/**
	 * Find all InvCAlternateMaterial entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvCAlternateMaterial property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCAlternateMaterial> found by query
	 */
	public List<InvCAlternateMaterial> findByProperty(String propertyName,
			Object value);

	public List<InvCAlternateMaterial> findByMaterialId(Object materialId);

	public List<InvCAlternateMaterial> findByAlterMaterialId(
			Object alterMaterialId);

	public List<InvCAlternateMaterial> findByQty(Object qty);

	public List<InvCAlternateMaterial> findByPriority(Object priority);

	public List<InvCAlternateMaterial> findByLastModifiedBy(
			Object lastModifiedBy);

	public List<InvCAlternateMaterial> findByEnterpriseCode(
			Object enterpriseCode);

	public List<InvCAlternateMaterial> findByIsUse(Object isUse);

	/**
	 * Find all InvCAlternateMaterial entities.
	 * 
	 * @return List<InvCAlternateMaterial> all InvCAlternateMaterial entities
	 */
	public List<InvCAlternateMaterial> findAll();
	/**
	 * 根据替代物料.物料id进行查找
	 * 
	 * @return List<InvCAlternateMaterial> all InvCAlternateMaterial entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvCAlternateMaterial> findByMaterialId(Long id);
	  /**
     * 删除对应一个物料的所有记录
     *
     * @param materialId 替代物料id
     * @param workerCode 登录者id
     * @throws CodeRepeatException
     * @throws RuntimeException
     *             when the operation fails
     */
    public void deleteByMaterialId(String materialId,String workerCode,String enterpriseCode) throws CodeRepeatException ;
    /**
     * 检查替代物料和替代物料是否是唯一的
     * @param MATERIAL_ID 物料id
     * @param ALTER_MATERIAL_ID 替代物料id
     * @return false 如果库位名称是唯一的
     */
    public boolean checkAlterMaterialForAdd(long materialId, long alterMaterialId);
    /**
	 * 更新
     * 检查替代物料和替代物料是否是唯一的
     * @param MATERIAL_ID 物料id
     * @param ALTER_MATERIAL_ID 替代物料id
     * @return list 如果存在替代物料id和物料id 是相同的
     */
	public InvCAlternateMaterial checkAlterMaterialForUpdate(long materialId, long alterMaterialId);
	/**
	 * 更新
     * 检查日期重叠性
     * @param MATERIAL_ID 物料id
     * @param ALTER_MATERIAL_ID 替代物料id
     * @param effectiveDate 有效开始日期
     * @param discontinueDate 有效截止日期
     * @return list
     */
	public InvCAlternateMaterial checkDate(long materialId, long alterMaterialId,String effectiveDate,String discontinueDate,String enterpriseCode );
}