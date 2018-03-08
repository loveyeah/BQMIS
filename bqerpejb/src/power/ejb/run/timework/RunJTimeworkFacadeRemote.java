package power.ejb.run.timework;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJTimeworkFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJTimeworkFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunJTimework entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunJTimework entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public long save(RunJTimework entity);

	public long saveTemp(RunJTimework entity);

	/**
	 * Delete a persistent RunJTimework entity.
	 * 
	 * @param entity
	 *            RunJTimework entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunJTimework entity);

	/**
	 * Persist a previously saved RunJTimework entity and return it or a copy of
	 * it to the sender. A copy of the RunJTimework entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            RunJTimework entity to update
	 * @return RunJTimework the persisted RunJTimework entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunJTimework update(RunJTimework entity);

	public RunJTimework findById(Long id);

	/**
	 * Find all RunJTimework entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunJTimework property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJTimework> found by query
	 */
	public List<RunJTimework> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByWorkItemCode(Object workItemCode,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByMachprofCode(Object machprofCode,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByWorkType(Object workType,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByWorkItemName(Object workItemName,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByCycle(Object cycle,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByClassSequence(Object classSequence,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByClassTeam(Object classTeam,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByDutytype(Object dutytype,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByIfdelay(Object ifdelay,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByDelayresult(Object delayresult,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByDelayman(Object delayman,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByDelaytype(Object delaytype,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByOpTicket(Object opTicket,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByWorkresult(Object workresult,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByWorkExplain(Object workExplain,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByIfcheck(Object ifcheck,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByCheckman(Object checkman,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByCheckresult(Object checkresult,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByIfimage(Object ifimage,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByImagecode(Object imagecode,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByIfExplain(Object ifExplain,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByMemo(Object memo,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByOperator(Object operator,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByProtector(Object protector,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByIfOpticket(Object ifOpticket,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByConman(Object conman,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByStatus(Object status,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByEnterprisecode(Object enterprisecode,
			int... rowStartIdxAndCount);

	public List<RunJTimework> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	/**
	 * Find all RunJTimework entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJTimework> all RunJTimework entities
	 */
	public List<RunJTimework> findAll(int... rowStartIdxAndCount);

	public PageObject findByIsUse(Object object, String queryWorkType,
			String queryRunType, String queryApproveType,
			String queryDelayType, String querystimeDate,
			String queryetimeDate, String queryWorkitemName,
			String queryMachprofCode, String queryWorkResult,
			int... rowStartIdxAndCount);

	public PageObject findByIsOther(Object object, String queryWorkType,
			String queryRunType, String queryApproveType,
			String queryDelayType, String querystimeDate,
			String queryetimeDate, String queryWorkitemName,
			String queryMachprofCode, String queryWorkResult,
			int... rowStartIdxAndCount);

	public PageObject getlistRun(Object object, String queryWorkType,
			String queryRunType, int... rowStartIdxAndCount);
}