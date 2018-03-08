package power.ejb.run.securityproduction;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ejb.run.securityproduction.form.BriefNewsForm;

/**
 * Remote interface for SpJBriefnewsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJBriefnewsFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved SpJBriefnews entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            SpJBriefnews entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public SpJBriefnews save(SpJBriefnews entity);

	/**
	 * Delete a persistent SpJBriefnews entity.
	 * 
	 * @param entity
	 *            SpJBriefnews entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SpJBriefnews entity);

	/**
	 * Persist a previously saved SpJBriefnews entity and return it or a copy of
	 * it to the sender. A copy of the SpJBriefnews entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            SpJBriefnews entity to update
	 * @return SpJBriefnews the persisted SpJBriefnews entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SpJBriefnews update(SpJBriefnews entity);

	public SpJBriefnews findById(Long id);

	/**
	 * Find all SpJBriefnews entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SpJBriefnews property to query
	 * @param value
	 *            the property value to match
	 * @return List<SpJBriefnews> found by query
	 */
	public List<SpJBriefnews> findByProperty(String propertyName, Object value);

	public List<SpJBriefnews> findByIssue(Object issue);

	public List<SpJBriefnews> findByContent(Object content);

	public List<SpJBriefnews> findByCommonBy(Object commonBy);

	public List<SpJBriefnews> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all SpJBriefnews entities.
	 * 
	 * @return List<SpJBriefnews> all SpJBriefnews entities
	 */
	public List<BriefNewsForm> findAll(String month);
}