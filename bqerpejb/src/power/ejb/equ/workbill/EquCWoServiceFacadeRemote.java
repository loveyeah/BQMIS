package power.ejb.equ.workbill;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCWoServiceFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCWoServiceFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCWoService entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCWoService entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquCWoService entity);

	/**
	 * Delete a persistent EquCWoService entity.
	 * 
	 * @param entity
	 *            EquCWoService entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCWoService entity);

	/**
	 * Persist a previously saved EquCWoService entity and return it or a copy
	 * of it to the sender. A copy of the EquCWoService entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCWoService entity to update
	 * @return EquCWoService the persisted EquCWoService entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCWoService update(EquCWoService entity);
	public PageObject getServiceByType(String typeId,String queryKey,int... rowStartIdxAndCount);

	public EquCWoService findById(Long id);

	/**
	 * Find all EquCWoService entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCWoService property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCWoService> found by query
	 */
	public List<EquCWoService> findByProperty(String propertyName, Object value);

	public List<EquCWoService> findByCode(Object code);

	public List<EquCWoService> findByName(Object name);

	public List<EquCWoService> findByType(Object type);

	public List<EquCWoService> findByFromCom(Object fromCom);

	public List<EquCWoService> findBySerUnit(Object serUnit);

	public List<EquCWoService> findByFee(Object fee);

	public List<EquCWoService> findByEnterpriseCode(Object enterpriseCode);

	public List<EquCWoService> findByIsUse(Object isUse);

	/**
	 * Find all EquCWoService entities.
	 * 
	 * @return List<EquCWoService> all EquCWoService entities
	 */
	public PageObject findAll(int... rowStartIdxAndCount);
	
	/**
	 * 同名判断
	 * @param name
	 * @return
	 */
	 public boolean checkName(String name);
	 
	 /**
		 * 同名判断 修改时
		 * @param name
		 * @return
		 */
		 public boolean checkUpdateName(String name,Long id);
}