package power.ejb.productiontec.technologySupervise;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtJJdzjFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Remote
public interface PtJJdzjFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJJdzj entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJJdzj entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJJdzj entity);

	/**
	 * Delete a persistent PtJJdzj entity.
	 * 
	 * @param entity
	 *            PtJJdzj entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJJdzj entity);

	/**
	 * Persist a previously saved PtJJdzj entity and return it or a copy of it
	 * to the sender. A copy of the PtJJdzj entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJJdzj entity to update
	 * @return PtJJdzj the persisted PtJJdzj entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJJdzj update(PtJJdzj entity);

	public PtJJdzj findById(Long id);
	
	public void deleteMulti(String ids);
	/**
	 * Find all PtJJdzj entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJJdzj property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJJdzj> found by query
	 */
	public List<PtJJdzj> findByProperty(String propertyName, Object value);

	public List<PtJJdzj> findByJdzyId(Object jdzyId);

	public List<PtJJdzj> findByMainTopic(Object mainTopic);

	public List<PtJJdzj> findByZjBy(Object zjBy);

	public List<PtJJdzj> findByContent(Object content);

	public List<PtJJdzj> findByMemo(Object memo);

	public List<PtJJdzj> findByFillBy(Object fillBy);

	public List<PtJJdzj> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all PtJJdzj entities.
	 * 
	 * @return List<PtJJdzj> all PtJJdzj entities
	 */
	public PageObject findAll(String jdzyId,String topicName,String enterpriseCode,int... rowStartIdxAndCount);
}