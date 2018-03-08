package power.ejb.run.runlog.shift;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for RunCShiftWorkerStationFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCShiftWorkerStationFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCShiftWorkerStation
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            RunCShiftWorkerStation entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunCShiftWorkerStation entity);

	/**
	 * Delete a persistent RunCShiftWorkerStation entity.
	 * 
	 * @param entity
	 *            RunCShiftWorkerStation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCShiftWorkerStation entity);

	/**
	 * Persist a previously saved RunCShiftWorkerStation entity and return it or
	 * a copy of it to the sender. A copy of the RunCShiftWorkerStation entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCShiftWorkerStation entity to update
	 * @returns RunCShiftWorkerStation the persisted RunCShiftWorkerStation
	 *          entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCShiftWorkerStation update(RunCShiftWorkerStation entity);

	public RunCShiftWorkerStation findById(Long id);

	/**
	 * Find all RunCShiftWorkerStation entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCShiftWorkerStation property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShiftWorkerStation> found by query
	 */
	public List<RunCShiftWorkerStation> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all RunCShiftWorkerStation entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShiftWorkerStation> all RunCShiftWorkerStation entities
	 */
	public List<RunCShiftWorkerStation> findAll(int... rowStartIdxAndCount);
	/**
	 * 根据班组下员工id获取岗位列表
	 * @param workerId
	 * @param enterpriseCode
	 * @return
	 */
	public List<RunCShiftWorkerStation> findWStationByShiftWorker(Long workerId,String enterpriseCode);
	/*
	 * 判断岗位是否存在
	 */
	
	public boolean isExsit(Long shiftWorkerId,Long stationId,String enterpriseCode);
	/**
	 * 判断员工是否是值长
	 * @param empcode
	 * @param enterprisecode
	 * @return
	 */
	public boolean isStaionZZ(String empcode,String enterprisecode);
}