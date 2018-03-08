package power.ejb.productiontec.powertest;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtCYqyblbFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtCYqyblbFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtCYqyblb entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtCYqyblb entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtCYqyblb entity);

	/**
	 * Delete a persistent PtCYqyblb entity.
	 * 
	 * @param entity
	 *            PtCYqyblb entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtCYqyblb entity);

	/**
	 * Persist a previously saved PtCYqyblb entity and return it or a copy of it
	 * to the sender. A copy of the PtCYqyblb entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtCYqyblb entity to update
	 * @return PtCYqyblb the persisted PtCYqyblb entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtCYqyblb update(PtCYqyblb entity);

	public PtCYqyblb findById(Long id);

	/**
	 * Find all PtCYqyblb entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtCYqyblb property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtCYqyblb> found by query
	 */
	public List<PtCYqyblb> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public void save(List<PtCYqyblb> addList, List<PtCYqyblb> updateList,
			String ids);

	public PageObject findAll(String enterpriseCode, Long specialId);
}