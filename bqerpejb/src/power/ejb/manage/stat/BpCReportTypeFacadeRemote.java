package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for BpCReportTypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCReportTypeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpCReportType entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpCReportType entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCReportType entity);

	/**
	 * Delete a persistent BpCReportType entity.
	 * 
	 * @param entity
	 *            BpCReportType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpCReportType entity);

	/**
	 * Persist a previously saved BpCReportType entity and return it or a copy
	 * of it to the sender. A copy of the BpCReportType entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCReportType entity to update
	 * @return BpCReportType the persisted BpCReportType entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpCReportType update(BpCReportType entity);

	public BpCReportType findById(Long id);

	/**
	 * Find all BpCReportType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCReportType property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCReportType> found by query
	 */
	public List<BpCReportType> findByProperty(String propertyName, Object value);

	/**
	 * Find all BpCReportType entities.
	 * 
	 * @return List<BpCReportType> all BpCReportType entities
	 */
	public List<BpCReportType> findAll();
}