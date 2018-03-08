package power.ejb.equ.standardpackage;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCStandardWoFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCStandardWoFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCStandardWo entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCStandardWo entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquCStandardWo entity);
	   public Long checkJobCode(String jobCode);

	/**
	 * Delete a persistent EquCStandardWo entity.
	 * 
	 * @param entity
	 *            EquCStandardWo entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(String ids);

	public boolean lock(String ids);

	public boolean unlock(String ids);

	/**
	 * Persist a previously saved EquCStandardWo entity and return it or a copy
	 * of it to the sender. A copy of the EquCStandardWo entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardWo entity to update
	 * @return EquCStandardWo the persisted EquCStandardWo entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardWo update(EquCStandardWo entity);

	public EquCStandardWo findById(Long id);

	/**
	 * Find all EquCStandardWo entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardWo property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardWo> found by query
	 */
	public List<EquCStandardWo> findByProperty(String propertyName, Object value);

	/**
	 * Find all EquCStandardWo entities.
	 * 
	 * @return List<EquCStandardWo> all EquCStandardWo entities
	 */
	public PageObject findAll(String enterpriseCode, int... rowStartIdxAndCount);

	/**
	 * 取用标准包 由设备关联选取
	 * 
	 * @param enterpriseCode
	 * @param kksCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAllToUse(String enterpriseCode, String kksCode,
			int... rowStartIdxAndCount);
}