package power.ejb.productiontec.powertest;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity PtCYqyblb.
 * 
 * @see power.ejb.productiontec.powertest.PtCYqyblb
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtCYqyblbFacade implements PtCYqyblbFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved PtCYqyblb entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtCYqyblb entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtCYqyblb entity) {
		LogUtil.log("saving PtCYqyblb instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PtCYqyblb entity.
	 * 
	 * @param entity
	 *            PtCYqyblb entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtCYqyblb entity) {
		LogUtil.log("deleting PtCYqyblb instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtCYqyblb.class, entity
					.getYqyblbId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PtCYqyblb entity and return it or a copy of it
	 * to the sender. A copy of the PtCYqyblb entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtCYqyblb entity to update
	 * @return PtCYqyblb the persisted PtCYqyblb entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtCYqyblb update(PtCYqyblb entity) {
		LogUtil.log("updating PtCYqyblb instance", Level.INFO, null);
		try {
			PtCYqyblb result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtCYqyblb findById(Long id) {
		LogUtil.log("finding PtCYqyblb instance with id: " + id, Level.INFO,
				null);
		try {
			PtCYqyblb instance = entityManager.find(PtCYqyblb.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PtCYqyblb entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtCYqyblb property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<PtCYqyblb> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtCYqyblb> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtCYqyblb instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtCYqyblb model where model."
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



	public void save(List<PtCYqyblb> addList, List<PtCYqyblb> updateList,
			String ids) {
		if (addList.size() > 0) {
			Long bjdId = bll.getMaxId("pt_c_yqyblb", "yqyblb_id");
			int i = 0;
			for (PtCYqyblb entity : addList) {
				entity.setYqyblbId(bjdId + (i++));
				this.save(entity);
			}
		}
		if (updateList.size() > 0) {
			for (PtCYqyblb entity : updateList) {

				this.update(entity);
			}
		}
		if (ids.length() > 0)
			deleteMulti(ids);

	}

	public void deleteMulti(String ids) {
		String sql = "delete pt_c_yqyblb t\n" + "where t.yqyblb_id in (" + ids
				+ ")";
		bll.exeNativeSQL(sql);

	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, Long specialId) {

		PageObject obj = new PageObject();

		String sql = "select * from pt_c_yqyblb  t\n"
				+ "where t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.jdzy_id=" + specialId;
		List<PtCYqyblb> list = bll.queryByNativeSQL(sql, PtCYqyblb.class);
		obj.setList(list);
		return obj;

	}
}
