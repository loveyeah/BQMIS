package power.ejb.productiontec.metalSupervise;

import java.util.List;
import javax.ejb.Remote;



/**
 * Remote interface for PtJsjdJHgjlkhxmFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJsjdJHgjlkhxmFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJsjdJHgjlkhxm entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJsjdJHgjlkhxm entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJsjdJHgjlkhxm entity);

	/**
	 * Delete a persistent PtJsjdJHgjlkhxm entity.
	 * 
	 * @param entity
	 *            PtJsjdJHgjlkhxm entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJsjdJHgjlkhxm entity);

	/**
	 * Persist a previously saved PtJsjdJHgjlkhxm entity and return it or a copy
	 * of it to the sender. A copy of the PtJsjdJHgjlkhxm entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PtJsjdJHgjlkhxm entity to update
	 * @return PtJsjdJHgjlkhxm the persisted PtJsjdJHgjlkhxm entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJsjdJHgjlkhxm update(PtJsjdJHgjlkhxm entity);

	public PtJsjdJHgjlkhxm findById(Long id);

	/**
	 * Find all PtJsjdJHgjlkhxm entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJsjdJHgjlkhxm property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJsjdJHgjlkhxm> found by query
	 */
	public List<PtJsjdJHgjlkhxm> findByProperty(String propertyName,
			Object value);

	public List<PtJsjdJHgjlkhxm> findByHgjnkhId(Object hgjnkhId);

	public List<PtJsjdJHgjlkhxm> findByExamName(Object examName);

	public List<PtJsjdJHgjlkhxm> findByMaterial(Object material);

	public List<PtJsjdJHgjlkhxm> findBySizes(Object sizes);

	public List<PtJsjdJHgjlkhxm> findByMethod(Object method);

	public List<PtJsjdJHgjlkhxm> findByResults(Object results);

	public List<PtJsjdJHgjlkhxm> findByAllowWork(Object allowWork);

	/**
	 * Find all PtJsjdJHgjlkhxm entities.
	 * 
	 * @return List<PtJsjdJHgjlkhxm> all PtJsjdJHgjlkhxm entities
	 */
	public List<PtJsjdJHgjlkhxm> findAll();
	
	public void save(List<PtJsjdJHgjlkhxm> addList,
			List<PtJsjdJHgjlkhxm> updateList, String delIds);
	
}