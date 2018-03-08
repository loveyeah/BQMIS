package power.ejb.hr;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;

/**
 * Facade for entity HrJEmpPhoto.
 * 
 * @see powereai.po.hr.HrJEmpPhoto
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJEmpPhotoFacade implements HrJEmpPhotoFacadeRemote {
	// property constants
	public static final String PHOTO = "photo";

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved HrJEmpPhoto entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJEmpPhoto entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJEmpPhoto entity) {
		LogUtil.log("saving HrJEmpPhoto instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrJEmpPhoto entity.
	 * 
	 * @param entity
	 *            HrJEmpPhoto entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJEmpPhoto entity) {
		LogUtil.log("deleting HrJEmpPhoto instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJEmpPhoto.class, entity
					.getEmpId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrJEmpPhoto entity and return it or a copy of
	 * it to the sender. A copy of the HrJEmpPhoto entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            HrJEmpPhoto entity to update
	 * @return HrJEmpPhoto the persisted HrJEmpPhoto entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJEmpPhoto update(HrJEmpPhoto entity) {
		LogUtil.log("updating HrJEmpPhoto instance", Level.INFO, null);
		try {
			HrJEmpPhoto result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJEmpPhoto findById(Long id) {
		LogUtil.log("finding HrJEmpPhoto instance with id: " + id, Level.INFO,
				null);
		try {
			HrJEmpPhoto instance = entityManager.find(HrJEmpPhoto.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJEmpPhoto entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJEmpPhoto property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<HrJEmpPhoto> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJEmpPhoto> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrJEmpPhoto instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJEmpPhoto model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrJEmpPhoto> findByPhoto(Object photo,
			int... rowStartIdxAndCount) {
		return findByProperty(PHOTO, photo, rowStartIdxAndCount);
	}

	/**
	 * Find all HrJEmpPhoto entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrJEmpPhoto> all HrJEmpPhoto entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJEmpPhoto> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrJEmpPhoto instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJEmpPhoto model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}