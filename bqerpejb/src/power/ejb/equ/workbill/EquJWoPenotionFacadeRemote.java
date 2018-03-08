package power.ejb.equ.workbill;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for EquJWoPenotionFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJWoPenotionFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquJWoPenotion entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJWoPenotion entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public EquJWoPenotion save(EquJWoPenotion entity);

	/**
	 * Delete a persistent EquJWoPenotion entity.
	 * 
	 * @param entity
	 *            EquJWoPenotion entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquJWoPenotion entity);

	/**
	 * Persist a previously saved EquJWoPenotion entity and return it or a copy
	 * of it to the sender. A copy of the EquJWoPenotion entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquJWoPenotion entity to update
	 * @return EquJWoPenotion the persisted EquJWoPenotion entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJWoPenotion update(EquJWoPenotion entity);

	public EquJWoPenotion findById(Long id);

	/**
	 * Find all EquJWoPenotion entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJWoPenotion property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquJWoPenotion> found by query
	 */
	public List<EquJWoPenotion> findByProperty(String propertyName, Object value);
	public EquJWoPenotion findBywoCode(String woCode);


	/**
	 * Find all EquJWoPenotion entities.
	 * 
	 * @return List<EquJWoPenotion> all EquJWoPenotion entities
	 */
	public List<EquJWoPenotion> findAll();
}