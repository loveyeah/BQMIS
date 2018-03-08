package power.ejb.productiontec.metalSupervise;

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
import power.ejb.productiontec.metalSupervise.form.PtJsjdJHgjbxxForm;

/**
 * Facade for entity PtJsjdJHgjbxx.
 * 
 * @see power.ejb.productiontec.metalSupervise.PtJsjdJHgjbxx
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJsjdJHgjbxxFacade implements PtJsjdJHgjbxxFacadeRemote {
	// property constants
	public static final String WORKER_CODE = "workerCode";
	public static final String WELD_CODE = "weldCode";
	public static final String WELD_AGE = "weldAge";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtJsjdJHgjbxx entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJsjdJHgjbxx entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJsjdJHgjbxx entity) {
		LogUtil.log("saving PtJsjdJHgjbxx instance", Level.INFO, null);
		try {

			entity.setWeldId(bll.getMaxId("pt_jsjd_j_hgjbxx", "weld_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PtJsjdJHgjbxx entity.
	 * 
	 * @param entity
	 *            PtJsjdJHgjbxx entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJsjdJHgjbxx entity) {
		LogUtil.log("deleting PtJsjdJHgjbxx instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJsjdJHgjbxx.class, entity
					.getWeldId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PtJsjdJHgjbxx entity and return it or a copy
	 * of it to the sender. A copy of the PtJsjdJHgjbxx entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PtJsjdJHgjbxx entity to update
	 * @return PtJsjdJHgjbxx the persisted PtJsjdJHgjbxx entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJsjdJHgjbxx update(PtJsjdJHgjbxx entity) {
		LogUtil.log("updating PtJsjdJHgjbxx instance", Level.INFO, null);
		try {
			PtJsjdJHgjbxx result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJsjdJHgjbxx findById(Long id) {
		LogUtil.log("finding PtJsjdJHgjbxx instance with id: " + id,
				Level.INFO, null);
		try {
			PtJsjdJHgjbxx instance = entityManager
					.find(PtJsjdJHgjbxx.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PtJsjdJHgjbxx entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJsjdJHgjbxx property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJsjdJHgjbxx> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtJsjdJHgjbxx> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PtJsjdJHgjbxx instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJsjdJHgjbxx model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<PtJsjdJHgjbxx> findByWorkerCode(Object workerCode) {
		return findByProperty(WORKER_CODE, workerCode);
	}

	public List<PtJsjdJHgjbxx> findByWeldCode(Object weldCode) {
		return findByProperty(WELD_CODE, weldCode);
	}

	public List<PtJsjdJHgjbxx> findByWeldAge(Object weldAge) {
		return findByProperty(WELD_AGE, weldAge);
	}

	public List<PtJsjdJHgjbxx> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all PtJsjdJHgjbxx entities.
	 * 
	 * @return List<PtJsjdJHgjbxx> all PtJsjdJHgjbxx entities
	 */
	@SuppressWarnings("unchecked")
	public List<PtJsjdJHgjbxx> findAll() {
		LogUtil.log("finding all PtJsjdJHgjbxx instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtJsjdJHgjbxx model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String ids) {
		String sqlString = "delete from pt_jsjd_j_hgjbxx t "
				+ " where t.weld_id in (" + ids + ")";
		bll.exeNativeSQL(sqlString);

	}

	@SuppressWarnings("unchecked")
	public PageObject getPtJsjdJHgjbxxList(String fuzzy, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject object = new PageObject();
		if (fuzzy == null) {
			fuzzy = "%";
		}

		String sqlcount = "select count(*) " + "from pt_jsjd_j_hgjbxx t "
				+ " where getworkername(t.worker_code) like '%" + fuzzy + "%'"
				+ " and t.enterprise_code='" + enterpriseCode + "'";
		String sqlString = "select t.*,getworkername(t.worker_code) from pt_jsjd_j_hgjbxx t "
				+ " where getworkername(t.worker_code) like '%"
				+ fuzzy
				+ "%'"
				+ " and t.enterprise_code='" + enterpriseCode + "'";
		List list = bll.queryByNativeSQL(sqlString, rowStartIdxAndCount);

		Iterator iterator = list.iterator();
		List<PtJsjdJHgjbxxForm> arrayList = new ArrayList<PtJsjdJHgjbxxForm>();
		while (iterator.hasNext()) {
			Object[] data = (Object[]) iterator.next();
			PtJsjdJHgjbxxForm modelForm = new PtJsjdJHgjbxxForm();
			PtJsjdJHgjbxx model = new PtJsjdJHgjbxx();
			if (data[0] != null)
				model.setWeldId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				model.setWorkerCode(data[1].toString());
			if (data[2] != null)
				model.setWeldCode(data[2].toString());
			if (data[3] != null)
				model.setWeldWorkDate((Date) data[3]);
			if (data[4] != null)
				model.setWeldAge(Long.parseLong(data[4].toString()));
			if(data[6] != null)//add by drdu 091106
				model.setWeldLevel(data[6].toString());
			if (data[7] != null)
				modelForm.setWorkerName(data[7].toString());
			modelForm.setModel(model);
			arrayList.add(modelForm);

		}
		Long countLong = Long.parseLong(bll.getSingal(sqlcount).toString());

		object.setList(arrayList);
		object.setTotalCount(countLong);
		return object;

	}
}