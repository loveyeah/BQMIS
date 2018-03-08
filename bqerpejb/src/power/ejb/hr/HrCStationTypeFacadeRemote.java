package power.ejb.hr;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCStationTypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCStationTypeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCStationType entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCStationType entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCStationType entity);

	/**
	 * Delete a persistent HrCStationType entity.
	 * 
	 * @param entity
	 *            HrCStationType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCStationType entity);

	/**
	 * Persist a previously saved HrCStationType entity and return it or a copy
	 * of it to the sender. A copy of the HrCStationType entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            HrCStationType entity to update
	 * @returns HrCStationType the persisted HrCStationType entity instance, may
	 *          not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCStationType update(HrCStationType entity);

	
	public HrCStationType findById(Long id);

	/**
	 * Find all HrCStationType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCStationType property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCStationType> found by query
	 */
	public List<HrCStationType> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<HrCStationType> findByStationTypeName(Object stationTypeName,
			int... rowStartIdxAndCount);

	public List<HrCStationType> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	public List<HrCStationType> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount);

	/**
	 * Find all HrCStationType entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCStationType> all HrCStationType entities
	 */
	public List<HrCStationType> findAll(int... rowStartIdxAndCount);
	
	
	/**
	 * add by liuyi 091123
	 * 查找岗位类别
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllStationTypes(String enterpriseCode);
}