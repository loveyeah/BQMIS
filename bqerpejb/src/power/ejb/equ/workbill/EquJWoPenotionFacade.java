package power.ejb.equ.workbill;

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
 * Facade for entity EquJWoPenotion.
 * 
 * @see power.ejb.equ.workbill.EquJWoPenotion
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJWoPenotionFacade implements EquJWoPenotionFacadeRemote {
	// property constants


	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * Perform an initial save of a previously unsaved EquJWoPenotion entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJWoPenotion entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public EquJWoPenotion save(EquJWoPenotion entity) {
		LogUtil.log("saving EquJWoPenotion instance", Level.INFO, null);
		try {
			Long id = dll.getMaxId("equ_j_wo_penotion", "ID");
			entity.setId(id);
			
			entityManager.persist(entity);
			
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent EquJWoPenotion entity.
	 * 
	 * @param entity
	 *            EquJWoPenotion entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquJWoPenotion entity) {
		LogUtil.log("deleting EquJWoPenotion instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(EquJWoPenotion.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved EquJWoPenotion entity and return it or a copy
	 * of it to the sender. A copy of the EquJWoPenotion entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquJWoPenotion entity to update
	 * @return EquJWoPenotion the persisted EquJWoPenotion entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJWoPenotion update(EquJWoPenotion entity) {
		LogUtil.log("updating EquJWoPenotion instance", Level.INFO, null);
		try {
			EquJWoPenotion result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJWoPenotion findById(Long id) {
		LogUtil.log("finding EquJWoPenotion instance with id: " + id,
				Level.INFO, null);
		try {
			EquJWoPenotion instance = entityManager.find(EquJWoPenotion.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquJWoPenotion entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJWoPenotion property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquJWoPenotion> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquJWoPenotion> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding EquJWoPenotion instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquJWoPenotion model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}
	// 根据工单编号查找工单
	@SuppressWarnings("unchecked")
	public EquJWoPenotion findBywoCode(String woCode) {
		String sql = "select ttt.* from Equ_j_Wo_Penotion ttt\n" +
			"where ttt.wo_code='"+woCode+"'\n" ;
		List<EquJWoPenotion> list = dll.queryByNativeSQL(sql, EquJWoPenotion.class);
		if (list != null && list.size() > 0) {
			EquJWoPenotion entity = list.get(0);
			return entity;
		} else {
			return null;
		}
	}

	/**
	 * Find all EquJWoPenotion entities.
	 * 
	 * @return List<EquJWoPenotion> all EquJWoPenotion entities
	 */
	@SuppressWarnings("unchecked")
	public List<EquJWoPenotion> findAll() {
		LogUtil.log("finding all EquJWoPenotion instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquJWoPenotion model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}