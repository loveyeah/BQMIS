package power.ejb.equ.standardpackage;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCRealatequFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCRealatequFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCRealatequ entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCRealatequ entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public long save(EquCRealatequ entity);

	/**
	 * Delete a persistent EquCRealatequ entity.
	 * 
	 * @param entity
	 *            EquCRealatequ entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCRealatequ entity);

	/**
	 * Persist a previously saved EquCRealatequ entity and return it or a copy
	 * of it to the sender. A copy of the EquCRealatequ entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCRealatequ entity to update
	 * @return EquCRealatequ the persisted EquCRealatequ entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCRealatequ update(EquCRealatequ entity);

	public EquCRealatequ findById(Long id);

	/**
	 * Find all EquCRealatequ entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCRealatequ property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquCRealatequ> found by query
	 */
	public List<EquCRealatequ> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<EquCRealatequ> findByCode(Object code,
			int... rowStartIdxAndCount);

	public List<EquCRealatequ> findByAttributeCode(Object attributeCode,
			int... rowStartIdxAndCount);

	public List<EquCRealatequ> findByStatus(Object status,
			int... rowStartIdxAndCount);

	public List<EquCRealatequ> findByEnterprisecode(Object enterprisecode,
			int... rowStartIdxAndCount);

	public List<EquCRealatequ> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	public PageObject findByIsuse(Object isUse, String code,
			int... rowStartIdxAndCount);

	/**
	 * Find all EquCRealatequ entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquCRealatequ> all EquCRealatequ entities
	 */
	public List<EquCRealatequ> findAll(int... rowStartIdxAndCount);
}