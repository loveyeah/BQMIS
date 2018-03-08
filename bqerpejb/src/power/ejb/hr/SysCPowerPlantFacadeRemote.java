package power.ejb.hr;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for SysCPowerPlantFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SysCPowerPlantFacadeRemote
{
	/**
	 * Perform an initial save of a previously unsaved SysCPowerPlant entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            SysCPowerPlant entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(SysCPowerPlant entity);

	/**
	 * Delete a persistent SysCPowerPlant entity.
	 * 
	 * @param entity
	 *            SysCPowerPlant entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SysCPowerPlant entity);

	/**
	 * Persist a previously saved SysCPowerPlant entity and return it or a copy
	 * of it to the sender. A copy of the SysCPowerPlant entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            SysCPowerPlant entity to update
	 * @return SysCPowerPlant the persisted SysCPowerPlant entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SysCPowerPlant update(SysCPowerPlant entity);

	public SysCPowerPlant findById(Long id);

	/**
	 * Find all SysCPowerPlant entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SysCPowerPlant property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<SysCPowerPlant> found by query
	 */
	public List<SysCPowerPlant> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByPropertys(String strWhere, Object[] o,
			final int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByPpowerPlantId(Object ppowerPlantId,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByPpowerPlantCode(Object ppowerPlantCode,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByPowerPlantCode(Object powerPlantCode,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByPowerPlantName(Object powerPlantName,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByPowerPlantAlias(Object powerPlantAlias,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByNatureId(Object natureId,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByPowerTypeId(Object powerTypeId,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByUnitsCapacity(Object unitsCapacity,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByRunsaleStatusId(Object runsaleStatusId,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findBySchedulingRelationsId(
			Object schedulingRelationsId, int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByAccountingMethodsId(
			Object accountingMethodsId, int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByManager(Object manager,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByIsPowerPlant(Object isPowerPlant,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByCurrentUnit(Object currentUnit,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByRegionalismTypeId(
			Object regionalismTypeId, int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByCity(Object city,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByMemo(Object memo,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByCreateBy(Object createBy,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByModifiyBy(Object modifiyBy,
			int... rowStartIdxAndCount);

	public List<SysCPowerPlant> findByLogoffBy(Object logoffBy,
			int... rowStartIdxAndCount);

	/**
	 * Find all SysCPowerPlant entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<SysCPowerPlant> all SysCPowerPlant entities
	 */
	public List<SysCPowerPlant> findAll(int... rowStartIdxAndCount);
}