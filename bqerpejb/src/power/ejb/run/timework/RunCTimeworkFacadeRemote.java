package power.ejb.run.timework;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunCTimeworkFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCTimeworkFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCTimework entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCTimework entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public long save(RunCTimework entity);

	/**
	 * Delete a persistent RunCTimework entity.
	 * 
	 * @param entity
	 *            RunCTimework entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCTimework entity);

	/**
	 * Persist a previously saved RunCTimework entity and return it or a copy of
	 * it to the sender. A copy of the RunCTimework entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            RunCTimework entity to update
	 * @return RunCTimework the persisted RunCTimework entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCTimework update(RunCTimework entity);

	public RunCTimework findById(Long id);

	/**
	 * Find all RunCTimework entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCTimework property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCTimework> found by query
	 */
	public List<RunCTimework> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByWorkType(Object workType,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByWorkItemCode(Object workItemCode,
			int... rowStartIdxAndCount);

	public RunCTimework findByWorkItemCode(String workitemcode);

	public List<RunCTimework> findByWorkItemName(Object workItemName,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByMachprofCode(Object machprofCode,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByOperator(Object operator,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByProtector(Object protector,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByCycle(Object cycle,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByWorkRangeType(Object workRangeType,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByCycleNumber(Object cycleNumber,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByWeekNo(Object weekNo,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByWeekDay(Object weekDay,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByClassSequence(Object classSequence,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByCycleSequence(Object cycleSequence,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByDelaytype(Object delaytype,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByMemo(Object memo,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByImportantlvl(Object importantlvl,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByWorkExplain(Object workExplain,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByIfexplain(Object ifexplain,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByIfimage(Object ifimage,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByIfcheck(Object ifcheck,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByIftest(Object iftest,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByIfopticket(Object ifopticket,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByOpticketCode(Object opticketCode,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByStatus(Object status,
			int... rowStartIdxAndCount);

	public List<RunCTimework> findByEnterprisecode(Object enterprisecode,
			int... rowStartIdxAndCount);

	public PageObject findByIsUse(Object isUse, Object CenterWorkType,
			Object CenterMachprofCode, int... rowStartIdxAndCount);

	public List<RunCTimework> findByIsUse(Object status,
			int... rowStartIdxAndCount);

	/**
	 * Find all RunCTimework entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCTimework> all RunCTimework entities
	 */
	public List<RunCTimework> findAll(int... rowStartIdxAndCount);

	public List<RunCTimework> findToGenerate(String enterpriseCode,
			int... rowStartIdxAndCount);

	public long MakeTimeworklist();
}