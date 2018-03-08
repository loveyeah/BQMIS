package power.ejb.equ.workbill;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for EquCWoTooltypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCWoTooltypeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCWoTooltype entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCWoTooltype entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public EquCWoTooltype save(EquCWoTooltype entity);

	/**
	 * Delete a persistent EquCWoTooltype entity.
	 * 
	 * @param entity
	 *            EquCWoTooltype entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCWoTooltype entity);

	/**
	 * Persist a previously saved EquCWoTooltype entity and return it or a copy
	 * of it to the sender. A copy of the EquCWoTooltype entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCWoTooltype entity to update
	 * @return EquCWoTooltype the persisted EquCWoTooltype entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCWoTooltype update(EquCWoTooltype entity);

	public EquCWoTooltype findById(Long id);

	/**
	 * Find all EquCWoTooltype entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCWoTooltype property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCWoTooltype> found by query
	 */
	public List<EquCWoTooltype> findByProperty(String propertyName, Object value);

	public List<EquCWoTooltype> findByPid(Object pid);

	public List<EquCWoTooltype> findByTypename(Object typename);

	public List<EquCWoTooltype> findByMarkCode(Object markCode);

	public List<EquCWoTooltype> findByMemo(Object memo);

	public List<EquCWoTooltype> findByLastModifiedBy(Object lastModifiedBy);

	public List<EquCWoTooltype> findByEnterpriseCode(Object enterpriseCode);

	public List<EquCWoTooltype> findByIsUse(Object isUse);

	/**
	 * Find all EquCWoTooltype entities.
	 * 
	 * @return List<EquCWoTooltype> all EquCWoTooltype entities
	 */
	public List<EquCWoTooltype> findAll();

	/**
	 * 识别码
	 * 
	 * @param markcode
	 * @return
	 */
	public int checkMarkCode(String markcode);

	public boolean getByPtypeId(Long PContypeId);

	public List<EquCWoTooltype> findByPtypeId(Object Pid);
}