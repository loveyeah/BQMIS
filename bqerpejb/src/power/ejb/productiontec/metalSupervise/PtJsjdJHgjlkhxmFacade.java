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
 * Facade for entity PtJsjdJHgjlkhxm.
 * 
 * @see power.ejb.productiontec.metalSupervise.PtJsjdJHgjlkhxm
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJsjdJHgjlkhxmFacade implements PtJsjdJHgjlkhxmFacadeRemote {
	// property constants
	public static final String HGJNKH_ID = "hgjnkhId";
	public static final String EXAM_NAME = "examName";
	public static final String MATERIAL = "material";
	public static final String SIZES = "sizes";
	public static final String METHOD = "method";
	public static final String RESULTS = "results";
	public static final String ALLOW_WORK = "allowWork";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtJsjdJHgjlkhxm entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJsjdJHgjlkhxm entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJsjdJHgjlkhxm entity) {
		LogUtil.log("saving PtJsjdJHgjlkhxm instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PtJsjdJHgjlkhxm entity.
	 * 
	 * @param entity
	 *            PtJsjdJHgjlkhxm entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJsjdJHgjlkhxm entity) {
		LogUtil.log("deleting PtJsjdJHgjlkhxm instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJsjdJHgjlkhxm.class, entity
					.getHgjnkhxmId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PtJsjdJHgjlkhxm entity and return it or a copy
	 * of it to the sender. A copy of the PtJsjdJHgjlkhxm entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PtJsjdJHgjlkhxm entity to update
	 * @return PtJsjdJHgjlkhxm the persisted PtJsjdJHgjlkhxm entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJsjdJHgjlkhxm update(PtJsjdJHgjlkhxm entity) {
		LogUtil.log("updating PtJsjdJHgjlkhxm instance", Level.INFO, null);
		try {
			PtJsjdJHgjlkhxm result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJsjdJHgjlkhxm findById(Long id) {
		LogUtil.log("finding PtJsjdJHgjlkhxm instance with id: " + id,
				Level.INFO, null);
		try {
			PtJsjdJHgjlkhxm instance = entityManager.find(
					PtJsjdJHgjlkhxm.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PtJsjdJHgjlkhxm entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJsjdJHgjlkhxm property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJsjdJHgjlkhxm> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtJsjdJHgjlkhxm> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PtJsjdJHgjlkhxm instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJsjdJHgjlkhxm model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<PtJsjdJHgjlkhxm> findByHgjnkhId(Object hgjnkhId) {
		return findByProperty(HGJNKH_ID, hgjnkhId);
	}

	public List<PtJsjdJHgjlkhxm> findByExamName(Object examName) {
		return findByProperty(EXAM_NAME, examName);
	}

	public List<PtJsjdJHgjlkhxm> findByMaterial(Object material) {
		return findByProperty(MATERIAL, material);
	}

	public List<PtJsjdJHgjlkhxm> findBySizes(Object sizes) {
		return findByProperty(SIZES, sizes);
	}

	public List<PtJsjdJHgjlkhxm> findByMethod(Object method) {
		return findByProperty(METHOD, method);
	}

	public List<PtJsjdJHgjlkhxm> findByResults(Object results) {
		return findByProperty(RESULTS, results);
	}

	public List<PtJsjdJHgjlkhxm> findByAllowWork(Object allowWork) {
		return findByProperty(ALLOW_WORK, allowWork);
	}

	/**
	 * Find all PtJsjdJHgjlkhxm entities.
	 * 
	 * @return List<PtJsjdJHgjlkhxm> all PtJsjdJHgjlkhxm entities
	 */
	@SuppressWarnings("unchecked")
	public List<PtJsjdJHgjlkhxm> findAll() {
		LogUtil.log("finding all PtJsjdJHgjlkhxm instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtJsjdJHgjlkhxm model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 重写SAVE
	 */
	public void save(List<PtJsjdJHgjlkhxm> addList,
			List<PtJsjdJHgjlkhxm> updateList, String delIds) {
		if (addList.size() > 0) {
			Long idLong = bll.getMaxId("PT_JSJD_J_HGJLKHXM", "hgjnkhxm_id");
			for (PtJsjdJHgjlkhxm entity : addList) {
				entity.setHgjnkhxmId(idLong++);
				this.save(entity);
			}
		}
		if (updateList.size() > 0) {
			for (PtJsjdJHgjlkhxm entity : updateList) {

				this.update(entity);
			}
		}
		if (delIds.length() > 0) {
			String sqlString = "delete from PT_JSJD_J_HGJLKHXM t"
					+ " where t.hgjnkhxm_id in(" + delIds + ")";
			bll.exeNativeSQL(sqlString);
		}

	}
}