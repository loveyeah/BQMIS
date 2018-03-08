package power.ejb.equ.workbill;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCWoToolsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCWoToolsFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCWoTools entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCWoTools entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquCWoTools entity);

	/**
	 * Delete a persistent EquCWoTools entity.
	 * 
	 * @param entity
	 *            EquCWoTools entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCWoTools entity);
	
	public PageObject getToolsByType(String typeId,String queryKey,int... rowStartIdxAndCount);

	/**
	 * Persist a previously saved EquCWoTools entity and return it or a copy of
	 * it to the sender. A copy of the EquCWoTools entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            EquCWoTools entity to update
	 * @return EquCWoTools the persisted EquCWoTools entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCWoTools update(EquCWoTools entity);

	public EquCWoTools findById(Long id);

	/**
	 * Find all EquCWoTools entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCWoTools property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCWoTools> found by query
	 */
	public List<EquCWoTools> findByProperty(String propertyName, Object value);

	public List<EquCWoTools> findByCode(Object code);

	public List<EquCWoTools> findByName(Object name);

	public List<EquCWoTools> findByType(Object type);

	public List<EquCWoTools> findByFromCom(Object fromCom);

	public List<EquCWoTools> findBySerUnit(Object serUnit);

	public List<EquCWoTools> findByFee(Object fee);

	public List<EquCWoTools> findByEnterpriseCode(Object enterpriseCode);

	public List<EquCWoTools> findByIsUse(Object isUse);

	/**
	 * Find all EquCWoTools entities.
	 * 
	 * @return List<EquCWoTools> all EquCWoTools entities
	 */
	public PageObject findAll(int... rowStartIdxAndCount);
	
	public boolean checkName(String name);
	
	/**
	 * 更新时 名称检查
	 * @param name
	 * @param id
	 * @return
	 */
	public boolean checkUpdateName(String name,Long id);
}