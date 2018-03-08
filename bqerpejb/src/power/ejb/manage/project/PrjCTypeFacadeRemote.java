package power.ejb.manage.project;


import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for PrjCTypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PrjCTypeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PrjCType entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PrjCType entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public PrjCType save(PrjCType entity);
	
	 public int checkMarkCode(String markcode);

	/**
	 * Delete a persistent PrjCType entity.
	 * 
	 * @param entity
	 *            PrjCType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Long id);

	/**
	 * Persist a previously saved PrjCType entity and return it or a copy of it
	 * to the sender. A copy of the PrjCType entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PrjCType entity to update
	 * @return PrjCType the persisted PrjCType entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PrjCType update(PrjCType entity);

	public PrjCType findById(Long id);

	/**
	 * Find all PrjCType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PrjCType property to query
	 * @param value
	 *            the property value to match
	 * @return List<PrjCType> found by query
	 */
	public List<PrjCType> findByProperty(String propertyName, Object value);

	public List<PrjCType> findByPPrjtypeId(Object prjPTypeId);
	
	public boolean getByPPrjtypeId(Long PContypeId);



	/**
	 * Find all PrjCType entities.
	 * 
	 * @return List<PrjCType> all PrjCType entities
	 */
	public List<PrjCType> findAll();
	public String findPrjTypeById(long id);
}