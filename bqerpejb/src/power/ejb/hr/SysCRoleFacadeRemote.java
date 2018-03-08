package power.ejb.hr;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for SysCRoleFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SysCRoleFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved SysCRole entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            SysCRole entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(SysCRole entity);

	/**
	 * Delete a persistent SysCRole entity.
	 * 
	 * @param entity
	 *            SysCRole entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SysCRole entity);

	/**
	 * Persist a previously saved SysCRole entity and return it or a copy of it
	 * to the sender. A copy of the SysCRole entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            SysCRole entity to update
	 * @return SysCRole the persisted SysCRole entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SysCRole update(SysCRole entity);

	public SysCRole findById(Long id);

	/**
	 * Find all SysCRole entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SysCRole property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<SysCRole> found by query
	 */
	public List<SysCRole> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);
	
	public List<SysCRole> findByPropertys(String strWhere, Object[] o,
			final int... rowStartIdxAndCount);

	public List<SysCRole> findByRoleTypeId(Object roleTypeId,
			int... rowStartIdxAndCount);

	public List<SysCRole> findByRoleName(Object roleName,
			int... rowStartIdxAndCount);

	public List<SysCRole> findByRoleStatus(Object roleStatus,
			int... rowStartIdxAndCount);

	public List<SysCRole> findByMemo(Object memo, int... rowStartIdxAndCount);

	public List<SysCRole> findByOrderBy(Object orderBy,
			int... rowStartIdxAndCount);

	public List<SysCRole> findByCreateBy(Object createBy,
			int... rowStartIdxAndCount);

	public List<SysCRole> findByLastModifiyBy(Object lastModifiyBy,
			int... rowStartIdxAndCount);

	/**
	 * Find all SysCRole entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<SysCRole> all SysCRole entities
	 */
	public List<SysCRole> findAll(int... rowStartIdxAndCount);
}