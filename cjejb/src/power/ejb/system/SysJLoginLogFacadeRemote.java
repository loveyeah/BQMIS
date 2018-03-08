package power.ejb.system;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for SysJLoginLogFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SysJLoginLogFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved SysJLoginLog entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            SysJLoginLog entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(SysJLoginLog entity);

	/**
	 * Delete a persistent SysJLoginLog entity.
	 * 
	 * @param entity
	 *            SysJLoginLog entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SysJLoginLog entity);

	/**
	 * Persist a previously saved SysJLoginLog entity and return it or a copy of
	 * it to the sender. A copy of the SysJLoginLog entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            SysJLoginLog entity to update
	 * @returns SysJLoginLog the persisted SysJLoginLog entity instance, may not
	 *          be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SysJLoginLog update(SysJLoginLog entity);

	public SysJLoginLog findById(Long id);

	/**
	 * Find all SysJLoginLog entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SysJLoginLog property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<SysJLoginLog> found by query
	 */
	public List<SysJLoginLog> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all SysJLoginLog entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<SysJLoginLog> all SysJLoginLog entities
	 */
	public List<SysJLoginLog> findAll(int... rowStartIdxAndCount);
	
	/*
	 * 根据用户工号与登录时间查询用户登陆日志
	 * @param workerCode 工号
	 * @param beginDate 登录时间
	 * @param endDate 登录时间
	 * @return List<SysJLoginLog>
	 */
	public PageObject findByWorkerCode(String workerCode,String beginDate,String endDate,int... rowStartIdxAndCount);
}