package power.ejb.productiontec.dependabilityAnalysis.business;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for PtJDesulfurizationParameterFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJDesulfurizationParameterFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved
	 * PtJDesulfurizationParameter entity. All subsequent persist actions of
	 * this entity should use the #update() method.
	 * 
	 * @param entity
	 *            PtJDesulfurizationParameter entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJDesulfurizationParameter entity);

	/**
	 * Delete a persistent PtJDesulfurizationParameter entity.
	 * 
	 * @param entity
	 *            PtJDesulfurizationParameter entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJDesulfurizationParameter entity);

	/**
	 * Persist a previously saved PtJDesulfurizationParameter entity and return
	 * it or a copy of it to the sender. A copy of the
	 * PtJDesulfurizationParameter entity parameter is returned when the JPA
	 * persistence mechanism has not previously been tracking the updated
	 * entity.
	 * 
	 * @param entity
	 *            PtJDesulfurizationParameter entity to update
	 * @return PtJDesulfurizationParameter the persisted
	 *         PtJDesulfurizationParameter entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJDesulfurizationParameter update(PtJDesulfurizationParameter entity);

	public PtJDesulfurizationParameter findById(Long id);

	/**
	 * Find all PtJDesulfurizationParameter entities with a specific property
	 * value.
	 * 
	 * @param propertyName
	 *            the name of the PtJDesulfurizationParameter property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJDesulfurizationParameter> found by query
	 */
	public List<PtJDesulfurizationParameter> findByProperty(
			String propertyName, Object value, int... rowStartIdxAndCount);

	/**
	 * Find all PtJDesulfurizationParameter entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJDesulfurizationParameter> all
	 *         PtJDesulfurizationParameter entities
	 */
	public List<PtJDesulfurizationParameter> findAll(int... rowStartIdxAndCount);
}