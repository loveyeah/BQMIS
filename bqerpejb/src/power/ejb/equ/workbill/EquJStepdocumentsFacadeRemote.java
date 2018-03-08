package power.ejb.equ.workbill;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquJStepdocumentsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJStepdocumentsFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquJStepdocuments entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJStepdocuments entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquJStepdocuments entity);
	public boolean save(List<EquJStepdocuments> addList);


	/**
	 * Delete a persistent EquJStepdocuments entity.
	 * 
	 * @param entity
	 *            EquJStepdocuments entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(String ids);
	public boolean deleteFiles(String ids);

	/**
	 * Persist a previously saved EquJStepdocuments entity and return it or a
	 * copy of it to the sender. A copy of the EquJStepdocuments entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquJStepdocuments entity to update
	 * @return EquJStepdocuments the persisted EquJStepdocuments entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJStepdocuments update(EquJStepdocuments entity);

	public EquJStepdocuments findById(Long id);

	/**
	 * Find all EquJStepdocuments entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJStepdocuments property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquJStepdocuments> found by query
	 */
	public List<EquJStepdocuments> findByProperty(String propertyName,
			Object value);



	/**
	 * Find all EquJStepdocuments entities.
	 * 
	 * @return List<EquJStepdocuments> all EquJStepdocuments entities
	 */
	public PageObject findAll(String enterpriseCode, String woCode,
			 int... rowStartIdxAndCount);
	public PageObject getEquCStepdocumentsList(String enterpriseCode, String woCode,
			 int... rowStartIdxAndCount);
}