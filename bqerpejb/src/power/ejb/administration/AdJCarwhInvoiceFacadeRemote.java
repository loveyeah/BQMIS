package power.ejb.administration;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for AdJCarwhInvoiceFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJCarwhInvoiceFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved AdJCarwhInvoice entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJCarwhInvoice entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJCarwhInvoice entity);

	/**
	 * Delete a persistent AdJCarwhInvoice entity.
	 * 
	 * @param entity
	 *            AdJCarwhInvoice entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJCarwhInvoice entity);

	/**
	 * Persist a previously saved AdJCarwhInvoice entity and return it or a copy
	 * of it to the sender. A copy of the AdJCarwhInvoice entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            AdJCarwhInvoice entity to update
	 * @return AdJCarwhInvoice the persisted AdJCarwhInvoice entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJCarwhInvoice update(AdJCarwhInvoice entity);

	public AdJCarwhInvoice findById(Long id);

	/**
	 * Find all AdJCarwhInvoice entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJCarwhInvoice property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJCarwhInvoice> found by query
	 */
	public List<AdJCarwhInvoice> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all AdJCarwhInvoice entities.
	 * 
	 * @return List<AdJCarwhInvoice> all AdJCarwhInvoice entities
	 */
	public List<AdJCarwhInvoice> findAll();
}