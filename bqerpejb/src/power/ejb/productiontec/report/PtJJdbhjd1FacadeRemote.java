package power.ejb.productiontec.report;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtJJdbhjd1Facade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJJdbhjd1FacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJJdbhjd1 entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJJdbhjd1 entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJJdbhjd1 entity);

	/**
	 * Delete a persistent PtJJdbhjd1 entity.
	 * 
	 * @param entity
	 *            PtJJdbhjd1 entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJJdbhjd1 entity);

	/**
	 * Persist a previously saved PtJJdbhjd1 entity and return it or a copy of
	 * it to the sender. A copy of the PtJJdbhjd1 entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJJdbhjd1 entity to update
	 * @return PtJJdbhjd1 the persisted PtJJdbhjd1 entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJJdbhjd1 update(PtJJdbhjd1 entity);

	public PtJJdbhjd1 findById(Long id);

	/**
	 * Find all PtJJdbhjd1 entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJJdbhjd1 property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJJdbhjd1> found by query
	 */
	public List<PtJJdbhjd1> findByProperty(String propertyName, Object value);

	/**
	 * Find all PtJJdbhjd1 entities.
	 * 
	 * @return List<PtJJdbhjd1> all PtJJdbhjd1 entities
	 */
	public List<PtJJdbhjd1> findAll();
	
	public PageObject findAllByMonthAndFlag(String month,String tabelFlag,String enterpriseCode,int...rowStartIdxAndCount);
	
	public void saveModiRec(List<PtJJdbhjd1> addList,List<PtJJdbhjd1> updateList);
}