package power.ejb.equ.standardpackage;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.standardpackage.form.StandPackNeedMaterial;

/**
 * Remote interface for EquCStandardOrderstepFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCStandardOrderstepFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCStandardOrderstep
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardOrderstep entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquCStandardOrderstep entity);

	/**
	 * 
	 * EditGrid的增删改方法
	 * 
	 * @param addList
	 * @param updateList
	 * @param delIds
	 */
	public boolean save(List<EquCStandardOrderstep> addList,
			List<EquCStandardOrderstep> updateList, String delIds);

	/**
	 * Delete a persistent EquCStandardOrderstep entity.
	 * 
	 * @param entity
	 *            EquCStandardOrderstep entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(Long id);

	public boolean delete(String ids);

	/**
	 * Persist a previously saved EquCStandardOrderstep entity and return it or
	 * a copy of it to the sender. A copy of the EquCStandardOrderstep entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardOrderstep entity to update
	 * @return EquCStandardOrderstep the persisted EquCStandardOrderstep entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardOrderstep update(EquCStandardOrderstep entity);

	public EquCStandardOrderstep findById(Long id);

	/**
	 * Find all EquCStandardOrderstep entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardOrderstep property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardOrderstep> found by query
	 */
	public List<EquCStandardOrderstep> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all EquCStandardOrderstep entities.
	 * 
	 * @return List<EquCStandardOrderstep> all EquCStandardOrderstep entities
	 */
	public PageObject findAll(String enterpriseCode, String woCode,
			int... rowStartIdxAndCount);
	
	/**
	 * 获得一个标准工作包中需要的所有物资 
	 * add by liuyi 20100326 
	 * @param woCode
	 * @param enterpriseCode
	 * @return
	 */
	List<StandPackNeedMaterial> getAllNeedMaterialList(String woCode,String enterpriseCode);
}