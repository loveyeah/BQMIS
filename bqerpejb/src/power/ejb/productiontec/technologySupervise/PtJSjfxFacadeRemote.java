package power.ejb.productiontec.technologySupervise;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtJSjfxFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Remote
public interface PtJSjfxFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJSjfx entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJSjfx entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJSjfx entity);

	/**
	 * Delete a persistent PtJSjfx entity.
	 * 
	 * @param entity
	 *            PtJSjfx entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJSjfx entity);

	/**
	 * Persist a previously saved PtJSjfx entity and return it or a copy of it
	 * to the sender. A copy of the PtJSjfx entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJSjfx entity to update
	 * @return PtJSjfx the persisted PtJSjfx entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJSjfx update(PtJSjfx entity);

	public PtJSjfx findById(Long id);
	
	public void deleteMulti(String ids);

	/**
	 * Find all PtJSjfx entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJSjfx property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJSjfx> found by query
	 */
	public List<PtJSjfx> findByProperty(String propertyName, Object value);

	public List<PtJSjfx> findByJdzyId(Object jdzyId);

	public List<PtJSjfx> findByMainTopic(Object mainTopic);

	public List<PtJSjfx> findByFxBy(Object fxBy);

	public List<PtJSjfx> findByContent(Object content);

	public List<PtJSjfx> findByMemo(Object memo);

	public List<PtJSjfx> findByFillBy(Object fillBy);

	public List<PtJSjfx> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all PtJSjfx entities.
	 * 
	 * @return List<PtJSjfx> all PtJSjfx entities
	 */
	public PageObject findAll(String jdzyId,String topicName,String enterpriseCode,int... rowStartIdxAndCount);
}