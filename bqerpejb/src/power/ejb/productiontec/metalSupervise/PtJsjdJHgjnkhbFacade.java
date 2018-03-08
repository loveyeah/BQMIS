package power.ejb.productiontec.metalSupervise;


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
 * Facade for entity PtJsjdJHgjnkhb.
 * 
 * @see power.ejb.productiontec.metalSupervise.PtJsjdJHgjnkhb
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJsjdJHgjnkhbFacade implements PtJsjdJHgjnkhbFacadeRemote {
	// property constants
	public static final String WELD_ID = "weldId";
	public static final String CHECK_UNIT = "checkUnit";
	public static final String SEND_UNIT = "sendUnit";
	public static final String CARD_CODE = "cardCode";
	public static final String STEEL_CODE = "steelCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtJsjdJHgjnkhb entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJsjdJHgjnkhb entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJsjdJHgjnkhb entity) {
		LogUtil.log("saving PtJsjdJHgjnkhb instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PtJsjdJHgjnkhb entity.
	 * 
	 * @param entity
	 *            PtJsjdJHgjnkhb entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJsjdJHgjnkhb entity) {
		LogUtil.log("deleting PtJsjdJHgjnkhb instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJsjdJHgjnkhb.class, entity
					.getHgjnkhId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PtJsjdJHgjnkhb entity and return it or a copy
	 * of it to the sender. A copy of the PtJsjdJHgjnkhb entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PtJsjdJHgjnkhb entity to update
	 * @return PtJsjdJHgjnkhb the persisted PtJsjdJHgjnkhb entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJsjdJHgjnkhb update(PtJsjdJHgjnkhb entity) {
		LogUtil.log("updating PtJsjdJHgjnkhb instance", Level.INFO, null);
		try {
			PtJsjdJHgjnkhb result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJsjdJHgjnkhb findById(Long id) {
		LogUtil.log("finding PtJsjdJHgjnkhb instance with id: " + id,
				Level.INFO, null);
		try {
			PtJsjdJHgjnkhb instance = entityManager.find(PtJsjdJHgjnkhb.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PtJsjdJHgjnkhb entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJsjdJHgjnkhb property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJsjdJHgjnkhb> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtJsjdJHgjnkhb> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PtJsjdJHgjnkhb instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJsjdJHgjnkhb model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<PtJsjdJHgjnkhb> findByWeldId(Object weldId) {
		return findByProperty(WELD_ID, weldId);
	}

	public List<PtJsjdJHgjnkhb> findByCheckUnit(Object checkUnit) {
		return findByProperty(CHECK_UNIT, checkUnit);
	}

	public List<PtJsjdJHgjnkhb> findBySendUnit(Object sendUnit) {
		return findByProperty(SEND_UNIT, sendUnit);
	}

	public List<PtJsjdJHgjnkhb> findByCardCode(Object cardCode) {
		return findByProperty(CARD_CODE, cardCode);
	}

	public List<PtJsjdJHgjnkhb> findBySteelCode(Object steelCode) {
		return findByProperty(STEEL_CODE, steelCode);
	}

	/**
	 * Find all PtJsjdJHgjnkhb entities.
	 * 
	 * @return List<PtJsjdJHgjnkhb> all PtJsjdJHgjnkhb entities
	 */
	@SuppressWarnings("unchecked")
	public List<PtJsjdJHgjnkhb> findAll() {
		LogUtil.log("finding all PtJsjdJHgjnkhb instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtJsjdJHgjnkhb model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<PtJsjdJHgjnkhb> addList,
			List<PtJsjdJHgjnkhb> updateList, String delIds) {
		if (addList.size() > 0) {
			Long idLong = bll.getMaxId("pt_jsjd_j_hgjnkhb", "hgjnkh_id");
			for (PtJsjdJHgjnkhb entity : addList) {
				entity.setHgjnkhId(idLong++);
				this.save(entity);
			}
		}
		if (updateList.size() > 0) {
			for (PtJsjdJHgjnkhb entity : updateList) {

				this.update(entity);
			}
		}
		if (delIds.length() > 0) {
			String sqlString = "delete from pt_jsjd_j_hgjnkhb t"
					+ " where t.hgjnkh_id in(" + delIds + ")";
			bll.exeNativeSQL(sqlString);
		}

	}

}