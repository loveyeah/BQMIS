package power.ejb.equ.standardpackage;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCStandardEquFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCStandardEquFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCStandardEqu entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCStandardEqu entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquCStandardEqu entity);

	/**
	 * Delete a persistent EquCStandardEqu entity.
	 * 
	 * @param entity
	 *            EquCStandardEqu entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(Long id);

	/**
	 * Delete some persistent EquCStandardEqu entitys.
	 * 
	 * @param ids
	 *            id of EquCStandardEqu entitys to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(String ids);

	public boolean lock(String ids);

	public boolean unlock(String ids);

	/**
	 * Persist a previously saved EquCStandardEqu entity and return it or a copy
	 * of it to the sender. A copy of the EquCStandardEqu entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardEqu entity to update
	 * @return EquCStandardEqu the persisted EquCStandardEqu entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardEqu update(EquCStandardEqu entity);

	public EquCStandardEqu findById(Long id);

	/**
	 * Find all EquCStandardEqu entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardEqu property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardEqu> found by query
	 */
	public List<EquCStandardEqu> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all EquCStandardEqu entities.
	 * 
	 * @return List<EquCStandardEqu> all EquCStandardEqu entities
	 */
	public PageObject findAll(String enterpriseCode, String woCode,
			int... rowStartIdxAndCount);

	public List<EquCStandardEqu> findToUse(String enterpriseCode, String kksCode);
}