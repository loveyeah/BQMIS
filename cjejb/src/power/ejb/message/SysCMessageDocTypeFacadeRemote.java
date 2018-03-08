package power.ejb.message;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * Remote interface for SysCMessageDocTypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SysCMessageDocTypeFacadeRemote  {
	/**
	 * Perform an initial save of a previously unsaved SysCMessageDocType
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            SysCMessageDocType entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public SysCMessageDocType save(SysCMessageDocType entity) throws CodeRepeatException ;

	/**
	 * Delete a persistent SysCMessageDocType entity.
	 * 
	 * @param entity
	 *            SysCMessageDocType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SysCMessageDocType entity);
	public void delete(String ids);
	/**
	 * Persist a previously saved SysCMessageDocType entity and return it or a
	 * copy of it to the sender. A copy of the SysCMessageDocType entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            SysCMessageDocType entity to update
	 * @return SysCMessageDocType the persisted SysCMessageDocType entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SysCMessageDocType update(SysCMessageDocType entity) throws CodeRepeatException;

	public SysCMessageDocType findById(Long id);

	/**
	 * Find all SysCMessageDocType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SysCMessageDocType property to query
	 * @param value
	 *            the property value to match
	 * @return List<SysCMessageDocType> found by query
	 */
	public List<SysCMessageDocType> findByProperty(String propertyName,
			Object value);

	public List<SysCMessageDocType> findByDocTypeName(Object docTypeName);

	public List<SysCMessageDocType> findByLastModifiedBy(Object lastModifiedBy);

	public List<SysCMessageDocType> findByEnterpriseCode(Object enterpriseCode);

	public List<SysCMessageDocType> findByIsUse(Object isUse);

	/**
	 * Find all SysCMessageDocType entities.
	 * 
	 * @return List<SysCMessageDocType> all SysCMessageDocType entities
	 */
	public List<SysCMessageDocType> findAll();
}