package power.ejb.equ.standardpackage;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCStandardRepairmethodFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCStandardRepairmethodFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCStandardRepairmethod
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardRepairmethod entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquCStandardRepairmethod entity);

	/**
	 * Delete a persistent EquCStandardRepairmethod entity.
	 * 
	 * @param id
	 *            id
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
	 * Delete the file of a EquCStandardRepairmethod entity.
	 * 
	 * @param ids
	 *            id序列1,2,3,4,5,...
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean deleteFile(String ids);

	/**
	 * Persist a previously saved EquCStandardRepairmethod entity and return it
	 * or a copy of it to the sender. A copy of the EquCStandardRepairmethod
	 * entity parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardRepairmethod entity to update
	 * @return EquCStandardRepairmethod the persisted EquCStandardRepairmethod
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardRepairmethod update(EquCStandardRepairmethod entity);

	public EquCStandardRepairmethod findById(Long id);

	/**
	 * Find all EquCStandardRepairmethod entities with a specific property
	 * value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardRepairmethod property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardRepairmethod> found by query
	 */
	public List<EquCStandardRepairmethod> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all EquCStandardRepairmethod entities.
	 * 
	 * @return List<EquCStandardRepairmethod> all EquCStandardRepairmethod
	 *         entities
	 */
	public PageObject findAll(String enterpriseCode, int... rowStartIdxAndCount);

	public PageObject findAllToUse(String enterpriseCode,
			int... rowStartIdxAndCount);
}