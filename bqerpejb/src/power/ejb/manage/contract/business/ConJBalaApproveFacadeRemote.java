package power.ejb.manage.contract.business;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for ConJBalaApproveFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface ConJBalaApproveFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved ConJBalaApprove entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            ConJBalaApprove entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(ConJBalaApprove entity);

	/**
	 * Delete a persistent ConJBalaApprove entity.
	 * 
	 * @param entity
	 *            ConJBalaApprove entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(ConJBalaApprove entity);

	/**
	 * Persist a previously saved ConJBalaApprove entity and return it or a copy
	 * of it to the sender. A copy of the ConJBalaApprove entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            ConJBalaApprove entity to update
	 * @return ConJBalaApprove the persisted ConJBalaApprove entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public ConJBalaApprove update(ConJBalaApprove entity);

	public ConJBalaApprove findById(Long id);

	/**
	 * Find all ConJBalaApprove entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the ConJBalaApprove property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ConJBalaApprove> found by query
	 */
	public List<ConJBalaApprove> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all ConJBalaApprove entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ConJBalaApprove> all ConJBalaApprove entities
	 */
	public List<ConJBalaApprove> findAll(int... rowStartIdxAndCount);
}