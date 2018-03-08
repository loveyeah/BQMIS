package power.ejb.productiontec.relayProtection;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for PtJdbhJDzdjbFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJdbhJDzdjbFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJdbhJDzdjb entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJdbhJDzdjb entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJdbhJDzdjb entity);

	/**
	 * Delete a persistent PtJdbhJDzdjb entity.
	 * 
	 * @param entity
	 *            PtJdbhJDzdjb entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJdbhJDzdjb entity);

	/**
	 * Persist a previously saved PtJdbhJDzdjb entity and return it or a copy of
	 * it to the sender. A copy of the PtJdbhJDzdjb entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJdbhJDzdjb entity to update
	 * @return PtJdbhJDzdjb the persisted PtJdbhJDzdjb entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJdbhJDzdjb update(PtJdbhJDzdjb entity);

	public PtJdbhJDzdjb findById(Long id);

	/**
	 * Find all PtJdbhJDzdjb entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJdbhJDzdjb property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJdbhJDzdjb> found by query
	 */
	public List<PtJdbhJDzdjb> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all PtJdbhJDzdjb entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJdbhJDzdjb> all PtJdbhJDzdjb entities
	 */
	public List<PtJdbhJDzdjb> findAll(int... rowStartIdxAndCount);
}