package power.ejb.productiontec.powertest;

import java.util.ArrayList;
import java.util.Iterator;
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
import power.ejb.productiontec.powertest.form.PtJYqybjycszForm;

/**
 * Facade for entity PtJYqybjycsz.
 * 
 * @see power.ejb.productiontec.powertest.PtJYqybjycsz
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJYqybjycszFacade implements PtJYqybjycszFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtJYqybjycsz entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJYqybjycsz entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJYqybjycsz entity) {
		LogUtil.log("saving PtJYqybjycsz instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PtJYqybjycsz entity.
	 * 
	 * @param entity
	 *            PtJYqybjycsz entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJYqybjycsz entity) {
		LogUtil.log("deleting PtJYqybjycsz instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJYqybjycsz.class, entity
					.getJycszId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PtJYqybjycsz entity and return it or a copy of
	 * it to the sender. A copy of the PtJYqybjycsz entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJYqybjycsz entity to update
	 * @return PtJYqybjycsz the persisted PtJYqybjycsz entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJYqybjycsz update(PtJYqybjycsz entity) {
		LogUtil.log("updating PtJYqybjycsz instance", Level.INFO, null);
		try {
			PtJYqybjycsz result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJYqybjycsz findById(Long id) {
		LogUtil.log("finding PtJYqybjycsz instance with id: " + id, Level.INFO,
				null);
		try {
			PtJYqybjycsz instance = entityManager.find(PtJYqybjycsz.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PtJYqybjycsz entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJYqybjycsz property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<PtJYqybjycsz> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtJYqybjycsz> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtJYqybjycsz instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJYqybjycsz model where model."
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

	/**
	 * Find all PtJYqybjycsz entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJYqybjycsz> all PtJYqybjycsz entities
	 */
	@SuppressWarnings("unchecked")
	public List<PtJYqybjycsz> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtJYqybjycsz instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtJYqybjycsz model";
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

	public void save(List<PtJYqybjycsz> addList, List<PtJYqybjycsz> updateList) {
		if (addList.size() > 0) {
			Long idLong = bll.getMaxId("pt_j_yqybjycsz", "jycsz_id");
			for (PtJYqybjycsz entity : addList) {
				entity.setJycszId(idLong++);
				this.save(entity);
			}

		}
		if (updateList.size() > 0) {
			for (PtJYqybjycsz entity : updateList) {
				this.update(entity);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findCSZ(String regulatorId, String jyjlId) {
		PageObject object = new PageObject();
		String sqlString = "";
		if (jyjlId != null && !jyjlId.equals("")) {
			sqlString += "select t.parameter_id,t.parameter_names,t.jdzy_id ,"
					+ " s.jyjl_id,s.parameter_value,s.jycsz_id"
					+ " from pt_c_yqybjycs t, pt_j_yqybjycsz s"
					+ " where t.yqyblb_id=" + " (select m.yqyblb_id "
					+ " from pt_j_yqybtz m where m.regulator_id='"
					+ regulatorId + "')" +

					" and s.parameter_id(+)=t.parameter_id "
					+ " and s.jyjl_id=" + jyjlId;

		} else {
			sqlString += "select t.parameter_id,t.parameter_names,t.jdzy_id "

			+ " from pt_c_yqybjycs t " + " where t.yqyblb_id="
					+ " (select m.yqyblb_id "
					+ " from pt_j_yqybtz m where m.regulator_id='"
					+ regulatorId + "')";
		}
		sqlString += " order by t.parameter_id";
		List list = bll.queryByNativeSQL(sqlString);
		List<PtJYqybjycszForm> arrayList = new ArrayList<PtJYqybjycszForm>();

		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Object[] data = (Object[]) iterator.next();
			PtJYqybjycszForm model = new PtJYqybjycszForm();
			if (data[0] != null)
				model.setParameterId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				model.setParameterName(data[1].toString());
			if (data[2] != null)
				model.setJdzyId(Long.parseLong(data[2].toString()));
			if (jyjlId != null) {
				if (data[3] != null)
					model.setJyjlId(Long.parseLong(data[3].toString()));
				if (data[4] != null)
					model.setParameterValue(data[4].toString());
				if (data[5] != null)
					model.setJycszId(Long.parseLong(data[5].toString()));
			}
			arrayList.add(model);
		}
		object.setList(arrayList);
		return object;

	}
}