package power.ejb.productiontec.metalSupervise;


import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for PtJsjdJHgjnkhbFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJsjdJHgjnkhbFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJsjdJHgjnkhb entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJsjdJHgjnkhb entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJsjdJHgjnkhb entity);

	/**
	 * Delete a persistent PtJsjdJHgjnkhb entity.
	 * 
	 * @param entity
	 *            PtJsjdJHgjnkhb entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJsjdJHgjnkhb entity);

	/**
	 * Persist a previously saved PtJsjdJHgjnkhb entity and return it or a copy
	 * of it to the sender. A copy of the PtJsjdJHgjnkhb entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PtJsjdJHgjnkhb entity to update
	 * @return PtJsjdJHgjnkhb the persisted PtJsjdJHgjnkhb entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJsjdJHgjnkhb update(PtJsjdJHgjnkhb entity);

	public PtJsjdJHgjnkhb findById(Long id);

	/**
	 * Find all PtJsjdJHgjnkhb entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJsjdJHgjnkhb property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJsjdJHgjnkhb> found by query
	 */
	public List<PtJsjdJHgjnkhb> findByProperty(String propertyName, Object value);

	public List<PtJsjdJHgjnkhb> findByWeldId(Object weldId);

	public List<PtJsjdJHgjnkhb> findByCheckUnit(Object checkUnit);

	public List<PtJsjdJHgjnkhb> findBySendUnit(Object sendUnit);

	public List<PtJsjdJHgjnkhb> findByCardCode(Object cardCode);

	public List<PtJsjdJHgjnkhb> findBySteelCode(Object steelCode);

	/**
	 * Find all PtJsjdJHgjnkhb entities.
	 * 
	 * @return List<PtJsjdJHgjnkhb> all PtJsjdJHgjnkhb entities
	 */
	public List<PtJsjdJHgjnkhb> findAll();

	public void save(List<PtJsjdJHgjnkhb> addList,
			List<PtJsjdJHgjnkhb> updateList, String delIds);
}