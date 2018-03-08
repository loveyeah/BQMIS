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
import power.ejb.manage.project.PrjCType;

/**
 * Facade for entity EquCWoServicetype.
 * 
 * @see power.ejb.equ.workbill.EquCWoServicetype
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCWoServicetypeFacade implements EquCWoServicetypeFacadeRemote {
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
	 * Perform an initial save of a previously unsaved EquCWoServicetype entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCWoServicetype entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public EquCWoServicetype save(EquCWoServicetype entity) {
		LogUtil.log("saving EquCWoServicetype instance", Level.INFO, null);
		try {
			if(entity.getId()==null){
				entity.setId(bll.getMaxId("EQU_C_WO_SERVICETYPE", "id")); 
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
	 * Delete a persistent EquCWoServicetype entity.
	 * 
	 * @param entity
	 *            EquCWoServicetype entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCWoServicetype entity) {
		LogUtil.log("deleting EquCWoServicetype instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(EquCWoServicetype.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

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
	public EquCWoServicetype update(EquCWoServicetype entity) {
		LogUtil.log("updating EquCWoServicetype instance", Level.INFO, null);
		try {
			EquCWoServicetype result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquCWoServicetype findById(Long id) {
		LogUtil.log("finding EquCWoServicetype instance with id: " + id,
				Level.INFO, null);
		try {
			EquCWoServicetype instance = entityManager.find(
					EquCWoServicetype.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquCWoServicetype entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCWoServicetype property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCWoServicetype> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCWoServicetype> findByProperty(String propertyName,
			final Object value) {
		
		LogUtil.log("finding EquCWoServicetype instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquCWoServicetype model where model."
					+ propertyName + "= :propertyValue"+" and model.isUse='Y'";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<EquCWoServicetype> findByPid(Object pid) {
		return findByProperty(PID, pid);
	}

	public List<EquCWoServicetype> findByTypename(Object typename) {
		return findByProperty(TYPENAME, typename);
	}

	public List<EquCWoServicetype> findByMarkCode(Object markCode) {
		return findByProperty(MARK_CODE, markCode);
	}

	public List<EquCWoServicetype> findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}

	public List<EquCWoServicetype> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<EquCWoServicetype> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<EquCWoServicetype> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	/**
	 * Find all EquCWoServicetype entities.
	 * 
	 * @return List<EquCWoServicetype> all EquCWoServicetype entities
	 */
	@SuppressWarnings("unchecked")
	public List<EquCWoServicetype> findAll() {
		LogUtil
				.log("finding all EquCWoServicetype instances", Level.INFO,
						null);
		try {
			final String queryString = "select model from EquCWoServicetype model";
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
			String sql = "select count(1) from EQU_C_WO_SERVICETYPE t where t.mark_code='"
				+ markcode
				+"' and t.is_use='Y'"
				;
			count=Integer.parseInt(bll.getSingal(sql).toString());

				return count;
	    }
	  //
	  public boolean getByPtypeId(Long PContypeId){
			int count=0;
			String sql = "select count(1) from EQU_C_WO_SERVICETYPE t where t.pid="
				+ PContypeId
				+"and t.is_use='Y'"
				;
			count=Integer.parseInt(bll.getSingal(sql).toString());
			if(count!=0){
				return true;
			}
			else
				return false;
		}
	  public List<EquCWoServicetype> findByPtypeId(Object Pid) {
			return findByProperty("pid", Pid);
		}
}