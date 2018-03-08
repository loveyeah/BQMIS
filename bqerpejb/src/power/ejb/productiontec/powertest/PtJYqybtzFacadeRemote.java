package power.ejb.productiontec.powertest;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtJYqybtzFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJYqybtzFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJYqybtz entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJYqybtz entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJYqybtz entity);

	/**
	 * Delete a persistent PtJYqybtz entity.
	 * 
	 * @param entity
	 *            PtJYqybtz entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJYqybtz entity);

	/**
	 * Persist a previously saved PtJYqybtz entity and return it or a copy of it
	 * to the sender. A copy of the PtJYqybtz entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJYqybtz entity to update
	 * @return PtJYqybtz the persisted PtJYqybtz entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJYqybtz update(PtJYqybtz entity);

	public PtJYqybtz findById(Long id);

	/**
	 * Find all PtJYqybtz entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJYqybtz property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJYqybtz> found by query
	 */
	public List<PtJYqybtz> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all PtJYqybtz entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJYqybtz> all PtJYqybtz entities
	 */
	public List<PtJYqybtz> findAll(int... rowStartIdxAndCount);

	public PageObject getYaybtzlist(String fuzzy, Long jdzyId,
			String enterpriseCode, int... rowStartIdxAndCount);

	public void delete(String ids);

	public PageObject getInstrumentTestRecord(String fuzzy, Long jdzyId,
			String enterpriseCode, int... rowStartIdxAndCount);
	public PageObject getOverdueInstrumentTestRecord(String date,String names, Long jdzyId,
			String enterpriseCode, int... rowStartIdxAndCount);
	public Long checkTheSame(String tableName, String property, String value,
			String enterpriseCode, String jdzyId);
}