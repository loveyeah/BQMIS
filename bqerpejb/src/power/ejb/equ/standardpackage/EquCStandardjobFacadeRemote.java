package power.ejb.equ.standardpackage;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCStandardjobFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCStandardjobFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCStandardjob entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCStandardjob entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public long save(EquCStandardjob entity);

	/**
	 * Delete a persistent EquCStandardjob entity.
	 * 
	 * @param entity
	 *            EquCStandardjob entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCStandardjob entity);

	/**
	 * Persist a previously saved EquCStandardjob entity and return it or a copy
	 * of it to the sender. A copy of the EquCStandardjob entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardjob entity to update
	 * @return EquCStandardjob the persisted EquCStandardjob entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public long update(EquCStandardjob entity);

	public EquCStandardjob findById(Long id);

	/**
	 * Find all EquCStandardjob entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardjob property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquCStandardjob> found by query
	 */
	public List<EquCStandardjob> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<EquCStandardjob> findByJobCode(Object jobCode,
			int... rowStartIdxAndCount);

	public List<EquCStandardjob> findByCode(Object code,
			int... rowStartIdxAndCount);

	public List<EquCStandardjob> findByDescription(Object description,
			int... rowStartIdxAndCount);

	public List<EquCStandardjob> findByPriority(Object priority,
			int... rowStartIdxAndCount);

	public List<EquCStandardjob> findByJopDuration(Object jopDuration,
			int... rowStartIdxAndCount);

	public List<EquCStandardjob> findByMaintDep(Object maintDep,
			int... rowStartIdxAndCount);

	public List<EquCStandardjob> findBySpeciality(Object speciality,
			int... rowStartIdxAndCount);

	public List<EquCStandardjob> findByCrewId(Object crewId,
			int... rowStartIdxAndCount);

	public List<EquCStandardjob> findByInterruptable(Object interruptable,
			int... rowStartIdxAndCount);

	public List<EquCStandardjob> findByDownTime(Object downTime,
			int... rowStartIdxAndCount);

	public List<EquCStandardjob> findBySupervisor(Object supervisor,
			int... rowStartIdxAndCount);

	public List<EquCStandardjob> findByLaborCode(Object laborCode,
			int... rowStartIdxAndCount);

	public List<EquCStandardjob> findByCalnum(Object calnum,
			int... rowStartIdxAndCount);

	public List<EquCStandardjob> findByStatus(Object status,
			int... rowStartIdxAndCount);

	public List<EquCStandardjob> findByEnterprisecode(Object enterprisecode,
			int... rowStartIdxAndCount);

	public List<EquCStandardjob> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	public PageObject findByIsuse(Object isUse, int... rowStartIdxAndCount);

	/**
	 * Find all EquCStandardjob entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquCStandardjob> all EquCStandardjob entities
	 */
	public List<EquCStandardjob> findAll(int... rowStartIdxAndCount);
}