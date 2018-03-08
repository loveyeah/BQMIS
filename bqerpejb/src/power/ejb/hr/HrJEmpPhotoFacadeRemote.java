package power.ejb.hr;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for HrJEmpPhotoFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJEmpPhotoFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrJEmpPhoto entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJEmpPhoto entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJEmpPhoto entity);

	/**
	 * Delete a persistent HrJEmpPhoto entity.
	 * 
	 * @param entity
	 *            HrJEmpPhoto entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJEmpPhoto entity);

	/**
	 * Persist a previously saved HrJEmpPhoto entity and return it or a copy of
	 * it to the sender. A copy of the HrJEmpPhoto entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            HrJEmpPhoto entity to update
	 * @return HrJEmpPhoto the persisted HrJEmpPhoto entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJEmpPhoto update(HrJEmpPhoto entity);

	public HrJEmpPhoto findById(Long id);

	/**
	 * Find all HrJEmpPhoto entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJEmpPhoto property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrJEmpPhoto> found by query
	 */
	public List<HrJEmpPhoto> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<HrJEmpPhoto> findByPhoto(Object photo,
			int... rowStartIdxAndCount);

	/**
	 * Find all HrJEmpPhoto entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrJEmpPhoto> all HrJEmpPhoto entities
	 */
	public List<HrJEmpPhoto> findAll(int... rowStartIdxAndCount);
}