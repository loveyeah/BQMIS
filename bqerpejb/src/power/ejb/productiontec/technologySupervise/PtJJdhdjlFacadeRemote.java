package power.ejb.productiontec.technologySupervise;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtJJdhdjlFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Remote
public interface PtJJdhdjlFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJJdhdjl entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJJdhdjl entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJJdhdjl entity);

	/**
	 * Delete a persistent PtJJdhdjl entity.
	 * 
	 * @param entity
	 *            PtJJdhdjl entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJJdhdjl entity);

	/**
	 * Persist a previously saved PtJJdhdjl entity and return it or a copy of it
	 * to the sender. A copy of the PtJJdhdjl entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJJdhdjl entity to update
	 * @return PtJJdhdjl the persisted PtJJdhdjl entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJJdhdjl update(PtJJdhdjl entity);

	public PtJJdhdjl findById(Long id);
	
	public void deleteMulti(String ids);
	/**
	 * Find all PtJJdhdjl entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJJdhdjl property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJJdhdjl> found by query
	 */
	public List<PtJJdhdjl> findByProperty(String propertyName, Object value);

	public List<PtJJdhdjl> findByJdzyId(Object jdzyId);

	public List<PtJJdhdjl> findByMainTopic(Object mainTopic);

	public List<PtJJdhdjl> findByEmceeMan(Object emceeMan);

	public List<PtJJdhdjl> findByJoinMan(Object joinMan);

	public List<PtJJdhdjl> findByPlace(Object place);

	public List<PtJJdhdjl> findByContent(Object content);

	public List<PtJJdhdjl> findByMemo(Object memo);

	public List<PtJJdhdjl> findByFillBy(Object fillBy);

	public List<PtJJdhdjl> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all PtJJdhdjl entities.
	 * 
	 * @return List<PtJJdhdjl> all PtJJdhdjl entities
	 */
	public PageObject findAll(String jdzyId,String topicName,String enterpriseCode,int... rowStartIdxAndCount);
}