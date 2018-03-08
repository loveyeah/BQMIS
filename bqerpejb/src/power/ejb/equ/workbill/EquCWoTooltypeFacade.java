package power.ejb.equ.workbill;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquCWoTooltype.
 * 
 * @see power.ejb.equ.workbill.EquCWoTooltype
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCWoTooltypeFacade implements EquCWoTooltypeFacadeRemote {
	// property constants
	public static final String PID = "pid";
	public static final String TYPENAME = "typename";
	public static final String MARK_CODE = "markCode";
	public static final String MEMO = "memo";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
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
	public EquCWoTooltype save(EquCWoTooltype entity) {
		LogUtil.log("saving EquCWoTooltype instance", Level.INFO, null);
		try {
			if(entity.getId()==null){
				entity.setId(bll.getMaxId("EQU_C_WO_TOOLTYPE", "id")); 
			}
			entity.setIsUse("Y");
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent EquCWoTooltype entity.
	 * 
	 * @param entity
	 *            EquCWoTooltype entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCWoTooltype entity) {
		LogUtil.log("deleting EquCWoTooltype instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(EquCWoTooltype.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

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
	public EquCWoTooltype update(EquCWoTooltype entity) {
		LogUtil.log("updating EquCWoTooltype instance", Level.INFO, null);
		try {
			EquCWoTooltype result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquCWoTooltype findById(Long id) {
		LogUtil.log("finding EquCWoTooltype instance with id: " + id,
				Level.INFO, null);
		try {
			EquCWoTooltype instance = entityManager.find(EquCWoTooltype.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquCWoTooltype entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCWoTooltype property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCWoTooltype> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCWoTooltype> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding EquCWoTooltype instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquCWoTooltype model where model."
					+ propertyName + "= :propertyValue"+" and model.isUse='Y'";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<EquCWoTooltype> findByPid(Object pid) {
		return findByProperty(PID, pid);
	}

	public List<EquCWoTooltype> findByTypename(Object typename) {
		return findByProperty(TYPENAME, typename);
	}

	public List<EquCWoTooltype> findByMarkCode(Object markCode) {
		return findByProperty(MARK_CODE, markCode);
	}

	public List<EquCWoTooltype> findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}

	public List<EquCWoTooltype> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<EquCWoTooltype> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<EquCWoTooltype> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	/**
	 * Find all EquCWoTooltype entities.
	 * 
	 * @return List<EquCWoTooltype> all EquCWoTooltype entities
	 */
	@SuppressWarnings("unchecked")
	public List<EquCWoTooltype> findAll() {
		LogUtil.log("finding all EquCWoTooltype instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquCWoTooltype model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	//识别码
	  public int checkMarkCode(String markcode){
			int count=0;
			String sql = "select count(1) from EQU_C_WO_TOOLTYPE t where t.mark_code='"
				+ markcode
				+"' and t.is_use='Y'"
				;
			count=Integer.parseInt(bll.getSingal(sql).toString());

				return count;
	    }
	  
	  public boolean getByPtypeId(Long PContypeId){
			int count=0;
			String sql = "select count(1) from EQU_C_WO_TOOLTYPE t where t.pid="
				+ PContypeId
				+" and t.is_use='Y'"
				;
			count=Integer.parseInt(bll.getSingal(sql).toString());
			if(count!=0){
				return true;
			}
			else
				return false;
		}
	  public List<EquCWoTooltype> findByPtypeId(Object Pid) {
			return findByProperty("pid", Pid);
		}
}