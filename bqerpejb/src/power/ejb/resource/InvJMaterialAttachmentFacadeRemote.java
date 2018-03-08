package power.ejb.resource;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for InvJMaterialAttachmentFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvJMaterialAttachmentFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved InvJMaterialAttachment
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            InvJMaterialAttachment entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvJMaterialAttachment entity);

	/**
	 * Delete a persistent InvJMaterialAttachment entity.
	 * 
	 * @param entity
	 *            InvJMaterialAttachment entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvJMaterialAttachment entity);

	/**
	 * Persist a previously saved InvJMaterialAttachment entity and return it or
	 * a copy of it to the sender. A copy of the InvJMaterialAttachment entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            InvJMaterialAttachment entity to update
	 * @return InvJMaterialAttachment the persisted InvJMaterialAttachment
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvJMaterialAttachment update(InvJMaterialAttachment entity);

	public InvJMaterialAttachment findById(Long id);

	/**
	 * Find all InvJMaterialAttachment entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvJMaterialAttachment property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvJMaterialAttachment> found by query
	 */
	public List<InvJMaterialAttachment> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all InvJMaterialAttachment entities.
	 * 
	 * @return List<InvJMaterialAttachment> all InvJMaterialAttachment entities
	 */
	public List<InvJMaterialAttachment> findAll();
	/**
	 * Find all InvJMaterialAttachment entities.
	 * 
	 * @return List<InvJMaterialAttachment> all InvJMaterialAttachment entities
	 */
	public InvJMaterialAttachment getMaterialMap(String enterpriseCode,String strdocNo);
	
}