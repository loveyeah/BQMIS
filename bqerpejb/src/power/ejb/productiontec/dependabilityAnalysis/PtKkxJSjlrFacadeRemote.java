package power.ejb.productiontec.dependabilityAnalysis;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;



/**
 * Remote interface for PtKkxJSjlrFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtKkxJSjlrFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtKkxJSjlr entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtKkxJSjlr entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtKkxJSjlr entity);

	/**
	 * Delete a persistent PtKkxJSjlr entity.
	 * 
	 * @param entity
	 *            PtKkxJSjlr entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtKkxJSjlr entity);

	/**
	 * Persist a previously saved PtKkxJSjlr entity and return it or a copy of
	 * it to the sender. A copy of the PtKkxJSjlr entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtKkxJSjlr entity to update
	 * @return PtKkxJSjlr the persisted PtKkxJSjlr entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtKkxJSjlr update(PtKkxJSjlr entity);

	public PtKkxJSjlr findById(Long id);

	/**
	 * Find all PtKkxJSjlr entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtKkxJSjlr property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtKkxJSjlr> found by query
	 */
	public List<PtKkxJSjlr> findByProperty(String propertyName, Object value);

	public List<PtKkxJSjlr> findByBlockCode(Object blockCode);

	public List<PtKkxJSjlr> findByJzztId(Object jzztId);

	public List<PtKkxJSjlr> findByKeepTime(Object keepTime);

	public List<PtKkxJSjlr> findByReduceExert(Object reduceExert);

	public List<PtKkxJSjlr> findByStopTimes(Object stopTimes);

	public List<PtKkxJSjlr> findBySuccessTimes(Object successTimes);

	public List<PtKkxJSjlr> findByFailureTimes(Object failureTimes);

	public List<PtKkxJSjlr> findByRepairMandays(Object repairMandays);

	public List<PtKkxJSjlr> findByRepairCost(Object repairCost);

	public List<PtKkxJSjlr> findByStopReason(Object stopReason);

	public List<PtKkxJSjlr> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all PtKkxJSjlr entities.
	 * 
	 * @return List<PtKkxJSjlr> all PtKkxJSjlr entities
	 */
	public PageObject findAll(String enterpriseCode,int... rowStartIdxAndCount);
	
	public void save(List<PtKkxJSjlr> addList, List<PtKkxJSjlr> updateList,
			String deleteId);
}