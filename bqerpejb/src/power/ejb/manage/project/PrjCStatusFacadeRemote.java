package power.ejb.manage.project;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for PrjCStatusFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PrjCStatusFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PrjCStatus entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PrjCStatus entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PrjCStatus entity);

	/**
	 * Delete a persistent PrjCStatus entity.
	 * 
	 * @param entity
	 *            PrjCStatus entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PrjCStatus entity);

	/**
	 * Persist a previously saved PrjCStatus entity and return it or a copy of
	 * it to the sender. A copy of the PrjCStatus entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PrjCStatus entity to update
	 * @return PrjCStatus the persisted PrjCStatus entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PrjCStatus update(PrjCStatus entity);

	public PrjCStatus findById(Long id);

	/**
	 * Find all PrjCStatus entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PrjCStatus property to query
	 * @param value
	 *            the property value to match
	 * @return List<PrjCStatus> found by query
	 */
	public List<PrjCStatus> findByProperty(String propertyName, Object value);

	public List<PrjCStatus> findByPrjStatusName(Object prjStatusName);

	/**
	 * Find all PrjCStatus entities.
	 * 
	 * @return List<PrjCStatus> all PrjCStatus entities
	 */
	public List<PrjCStatus> findAll(String prjStatusType);
}