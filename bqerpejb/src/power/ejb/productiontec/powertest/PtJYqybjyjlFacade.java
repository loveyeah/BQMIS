package power.ejb.productiontec.powertest;

import java.util.ArrayList;
import java.util.Date;
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
import power.ejb.productiontec.powertest.form.PtJYqybjyjlForm;

/**
 * Facade for entity PtJYqybjyjl.
 * 
 * @see power.ejb.productiontec.powertest.PtJYqybjyjl
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJYqybjyjlFacade implements PtJYqybjyjlFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtJYqybjyjl entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJYqybjyjl entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public PtJYqybjyjl save(PtJYqybjyjl entity) {
		LogUtil.log("saving PtJYqybjyjl instance", Level.INFO, null);
		try {
			entity.setJyjlId(bll.getMaxId("pt_j_yqybjyjl", "jyjl_id"));
			entityManager.persist(entity);

			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PtJYqybjyjl entity.
	 * 
	 * @param entity
	 *            PtJYqybjyjl entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJYqybjyjl entity) {
		LogUtil.log("deleting PtJYqybjyjl instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJYqybjyjl.class, entity
					.getJyjlId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PtJYqybjyjl entity and return it or a copy of
	 * it to the sender. A copy of the PtJYqybjyjl entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJYqybjyjl entity to update
	 * @return PtJYqybjyjl the persisted PtJYqybjyjl entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJYqybjyjl update(PtJYqybjyjl entity) {
		LogUtil.log("updating PtJYqybjyjl instance", Level.INFO, null);
		try {
			PtJYqybjyjl result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJYqybjyjl findById(Long id) {
		LogUtil.log("finding PtJYqybjyjl instance with id: " + id, Level.INFO,
				null);
		try {
			PtJYqybjyjl instance = entityManager.find(PtJYqybjyjl.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PtJYqybjyjl entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJYqybjyjl property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<PtJYqybjyjl> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtJYqybjyjl> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtJYqybjyjl instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJYqybjyjl model where model."
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
	 * Find all PtJYqybjyjl entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJYqybjyjl> all PtJYqybjyjl entities
	 */
	@SuppressWarnings("unchecked")
	public List<PtJYqybjyjl> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtJYqybjyjl instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtJYqybjyjl model";
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

	public void deleteJLAndJYZ(String ids) {
		String sqlString = "delete from pt_j_yqybjyjl t "
				+ " where t.jyjl_id in (" + ids + ")";
		bll.exeNativeSQL(sqlString);
		String sqlString2 = " delete from pt_j_yqybjycsz t"
				+ " where t.jyjl_id in (" + ids + ")";
		bll.exeNativeSQL(sqlString2);

	}

	@SuppressWarnings("unchecked")
	public PageObject findJYJLList(String fuzzy, String jdzyId,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject object = new PageObject();
		String sqlString = "select s.*,getworkername(s.check_man),"
				+ " getworkername(s.test_man),getdeptname(s.check_dep_code),m.names "
				// add by liuyi 20100512 
				+ " ,m.test_cycle \n"
				+ " from  pt_j_yqybjyjl s,pt_j_yqybtz m "
				+ " where s.jdzy_id='" + jdzyId + "' and s.enterprise_code='"
				+ enterpriseCode + "'" + " and s.regulator_id in "
				+ " (select t.regulator_id from pt_j_yqybtz t"
				+ " where t.names like '%" + fuzzy + "%' " + " and t.jdzy_id='"
				+ jdzyId + "' and t.enterprise_code='" + enterpriseCode + "')"
				+ " and m.regulator_id=s.regulator_id"
				+ " order by s.check_date ";
		String sqlcountString = "select count(*)"
				+ " from  pt_j_yqybjyjl s,pt_j_yqybtz m "
				+ " where s.jdzy_id='" + jdzyId + "' and s.enterprise_code='"
				+ enterpriseCode + "'" + " and s.regulator_id in "
				+ " (select t.regulator_id from pt_j_yqybtz t"
				+ " where t.names like '%" + fuzzy + "%' " + " and t.jdzy_id='"
				+ jdzyId + "' and t.enterprise_code='" + enterpriseCode + "')"
				+ " and m.regulator_id=s.regulator_id";
		List list = bll.queryByNativeSQL(sqlString, rowStartIdxAndCount);
		Iterator iterator = list.iterator();
		List<PtJYqybjyjlForm> arrayList = new ArrayList<PtJYqybjyjlForm>();
		while (iterator.hasNext()) {
			PtJYqybjyjlForm jlForm = new PtJYqybjyjlForm();
			PtJYqybjyjl model = new PtJYqybjyjl();
			Object[] data = (Object[]) iterator.next();
			if (data[0] != null)
				model.setJyjlId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				model.setRegulatorId(Long.parseLong(data[1].toString()));
			if (data[2] != null)
				model.setPlanCheckDate((Date) data[2]);
			if (data[3] != null)
				model.setCheckDate((Date) data[3]);
			if (data[4] != null)
				model.setNextCheckDate((Date) data[4]);
			if (data[5] != null)
				model.setCheckTemperature(Double
						.parseDouble(data[5].toString()));
			if (data[6] != null)
				model.setCheckWet(Double.parseDouble(data[6].toString()));
			if (data[7] != null)
				model.setRusult(data[7].toString());
			if (data[8] != null)
				model.setCheckMan(data[8].toString());
			if (data[9] != null)
				model.setCheckDepCode(data[9].toString());
			if (data[10] != null)
				model.setTestMan(data[10].toString());
			if (data[11] != null)
				model.setMemo(data[11].toString());
			if (data[12] != null)
				model.setJdzyId(Long.parseLong(data[12].toString()));
			if (data[14] != null)
				jlForm.setCheckManName(data[14].toString());
			if (data[15] != null)
				jlForm.setTestManName(data[15].toString());
			if (data[16] != null)
				jlForm.setCheckDepName(data[16].toString());
			if (data[17] != null)
				jlForm.setNames(data[17].toString());
			if(data[18] != null)
				jlForm.setTestCycle(data[18].toString());
			jlForm.setModel(model);
			arrayList.add(jlForm);
		}
		Long count = Long.parseLong(bll.getSingal(sqlcountString).toString());
		object.setTotalCount(count);
		object.setList(arrayList);
		return object;
	}

	@SuppressWarnings("unchecked")
	public PtJYqybjyjl findLastJl(String regulatorId, String enterpriseCode) {
		PtJYqybjyjl modelPtJYqybjyjl = new PtJYqybjyjl();
		String sql = "select t.* from pt_j_yqybjyjl t "
				+ " where t.regulator_id ='" + regulatorId + "'"
				+ " and t.enterprise_code='" + enterpriseCode + "'"
				+ " order by t.next_check_date desc";
		List<PtJYqybjyjl> list = bll.queryByNativeSQL(sql, PtJYqybjyjl.class);
		if (list.size() > 0) {
			modelPtJYqybjyjl = list.get(0);
		}
		return modelPtJYqybjyjl;
	}
}