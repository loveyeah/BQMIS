package power.ejb.equ.standardpackage;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCStandardRepairmodeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCStandardRepairmodeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCStandardRepairmode
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardRepairmode entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquCStandardRepairmode entity);
	
	public void save(List<EquCStandardRepairmode> addList);

	/**
	 * Delete a persistent EquCStandardRepairmode entity.
	 * 
	 * @param entity
	 *            EquCStandardRepairmode entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(Long id);
	
	/**
	 * Delete a persistent EquCStandardRepairmethod entity.
	 * 
	 * @param ids
	 *            id序列1,2,3,4,5,...
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(String ids);
	
	/**
	 * Lock a persistent EquCStandardRepairmethod entity.
	 * 
	 * @param ids
	 *            id序列1,2,3,4,5,...
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean lock(String ids);
	
	/**
	 * Unlock a persistent EquCStandardRepairmethod entity.
	 * 
	 * @param ids
	 *            id序列1,2,3,4,5,...
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean unlock(String ids);

	/**
	 * Persist a previously saved EquCStandardRepairmode entity and return it or
	 * a copy of it to the sender. A copy of the EquCStandardRepairmode entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardRepairmode entity to update
	 * @return EquCStandardRepairmode the persisted EquCStandardRepairmode
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardRepairmode update(EquCStandardRepairmode entity);
	
	public void update(List<EquCStandardRepairmode> updateList);

	public EquCStandardRepairmode findById(Long id);

	/**
	 * Find all EquCStandardRepairmode entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardRepairmode property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardRepairmode> found by query
	 */
	public List<EquCStandardRepairmode> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all EquCStandardRepairmode entities.
	 * 
	 * @return List<EquCStandardRepairmode> all EquCStandardRepairmode entities
	 */
	public PageObject findAll(String enterpriseCode, int... rowStartIdxAndCount);
	public PageObject findAlltoUse(String enterpriseCode, int... rowStartIdxAndCount);
}