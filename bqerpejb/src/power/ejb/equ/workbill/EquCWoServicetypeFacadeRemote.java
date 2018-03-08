package power.ejb.equ.workbill;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ejb.manage.project.PrjCType;

/**
 * Remote interface for EquCWoServicetypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCWoServicetypeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCWoServicetype entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCWoServicetype entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public EquCWoServicetype save(EquCWoServicetype entity);

	/**
	 * Delete a persistent EquCWoServicetype entity.
	 * 
	 * @param entity
	 *            EquCWoServicetype entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCWoServicetype entity);

	/**
	 * Persist a previously saved EquCWoServicetype entity and return it or a
	 * copy of it to the sender. A copy of the EquCWoServicetype entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCWoServicetype entity to update
	 * @return EquCWoServicetype the persisted EquCWoServicetype entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCWoServicetype update(EquCWoServicetype entity);

	public EquCWoServicetype findById(Long id);

	/**
	 * Find all EquCWoServicetype entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCWoServicetype property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCWoServicetype> found by query
	 */
	public List<EquCWoServicetype> findByProperty(String propertyName,
			Object value);

	public List<EquCWoServicetype> findByPid(Object pid);

	public List<EquCWoServicetype> findByTypename(Object typename);

	public List<EquCWoServicetype> findByMarkCode(Object markCode);

	public List<EquCWoServicetype> findByMemo(Object memo);

	public List<EquCWoServicetype> findByLastModifiedBy(Object lastModifiedBy);

	public List<EquCWoServicetype> findByEnterpriseCode(Object enterpriseCode);

	public List<EquCWoServicetype> findByIsUse(Object isUse);

	/**
	 * Find all EquCWoServicetype entities.
	 * 
	 * @return List<EquCWoServicetype> all EquCWoServicetype entities
	 */
	public List<EquCWoServicetype> findAll();
	
	/**
	 * 
	 * @param markcode
	 * @return
	 */
	 public int checkMarkCode(String markcode);
	 /**
	  * 
	  * @param PContypeId
	  * @return
	  */
	 public boolean getByPtypeId(Long PContypeId);
	 
	 /**
	  * 
	  * @param prjPTypeId
	  * @return
	  */
	 public List<EquCWoServicetype> findByPtypeId(Object prjPTypeId); 
}