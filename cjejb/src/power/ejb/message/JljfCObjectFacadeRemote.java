package power.ejb.message;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * Remote interface for JljfCObjectFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface JljfCObjectFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved JljfCObject entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            JljfCObject entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public JljfCObject save(JljfCObject entity) throws CodeRepeatException;

	/**
	 * Delete a persistent JljfCObject entity.
	 * 
	 * @param entity
	 *            JljfCObject entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(JljfCObject entity);

	/**
	 * Persist a previously saved JljfCObject entity and return it or a copy of
	 * it to the sender. A copy of the JljfCObject entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            JljfCObject entity to update
	 * @return JljfCObject the persisted JljfCObject entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public JljfCObject update(JljfCObject entity) throws CodeRepeatException;

	
	public JljfCObject findByZbbmtxCode(String zbbmtxCode);
	

	/**
	 * Find all JljfCObject entities.
	 * 
	 * @return List<JljfCObject> all JljfCObject entities
	 */
	public List<JljfCObject> findAll();
	

	/**
	 * 模糊查询客户公司
	 * @param String fuzzy  模糊查询参数（zbbmtxCode，zbbmtxName，zbbmtxAlias）
	 * @return List<JljfCObject> 
	 */
	
	public List<JljfCObject> findByFuzzy(String fuzzy);
}