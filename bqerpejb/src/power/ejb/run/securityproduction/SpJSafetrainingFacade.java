package power.ejb.run.securityproduction;

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
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

import power.ejb.run.securityproduction.form.SpJSafetrainingForm;

/**
 * Facade for entity SpJSafetraining.
 * 
 * @see power.ejb.run.securityproduction.SpJSafetraining
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJSafetrainingFacade implements SpJSafetrainingFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved SpJSafetraining entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            SpJSafetraining entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(SpJSafetraining entity) {
		try {
			entity.setTrainingId(bll.getMaxId("SP_J_SAFETRAINING",
					"training_id"));
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Delete a persistent SpJSafetraining entity.
	 * 
	 * @param entity
	 *            SpJSafetraining entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(SpJSafetraining entity) {
		try {
			entity = entityManager.getReference(SpJSafetraining.class, entity
					.getTrainingId());
			entityManager.remove(entity);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Persist a previously saved SpJSafetraining entity and return it or a copy
	 * of it to the sender. A copy of the SpJSafetraining entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            SpJSafetraining entity to update
	 * @return SpJSafetraining the persisted SpJSafetraining entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public boolean update(SpJSafetraining entity) {
		LogUtil.log("updating SpJSafetraining instance", Level.INFO, null);
		try {
			SpJSafetraining result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			return false;
		}
	}

	public SpJSafetraining findById(Long id) {
		LogUtil.log("finding SpJSafetraining instance with id: " + id,
				Level.INFO, null);
		try {
			SpJSafetraining instance = entityManager.find(
					SpJSafetraining.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all SpJSafetraining entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SpJSafetraining property to query
	 * @param value
	 *            the property value to match
	 * @return List<SpJSafetraining> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<SpJSafetraining> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding SpJSafetraining instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from SpJSafetraining model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all SpJSafetraining entities.
	 * 
	 * @return List<SpJSafetraining> all SpJSafetraining entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String argFuzzy, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		LogUtil.log("finding all SpJSafetraining instances", Level.INFO, null);
		try {

			String queryString = "select t.*,to_char(t.training_time,'yyyy-MM-dd') trainingTime,getworkername(t.training_speaker) speakerName,getdeptname(t.dep_code) deptName "
					+ " from SP_J_SAFETRAINING t "
					+ " where t.enterprise_code='" + enterpriseCode + "'";
			;

			String countsql = "select count(*)" + " from SP_J_SAFETRAINING t "
					+ " where t.enterprise_code='" + enterpriseCode + "'";
			if (argFuzzy == null || argFuzzy.equals("")) {

				queryString += " order by t.training_time";
			} else {
				queryString += " and to_char(t.training_time,'yyyy') ='"
						+ argFuzzy + "' order by t.training_time";

				countsql += " and to_char(t.training_time,'yyyy') ='"
						+ argFuzzy+"'";
			}

			PageObject result = new PageObject();
			List list = bll.queryByNativeSQL(queryString, rowStartIdxAndCount);

			List<SpJSafetrainingForm> arraylist = new ArrayList<SpJSafetrainingForm>();

			Iterator it = list.iterator();
			while (it.hasNext()) {

				Object[] data = (Object[]) it.next();
				SpJSafetrainingForm model = new SpJSafetrainingForm();
				SpJSafetraining baseInfo = new SpJSafetraining();
				if (data[0] != null) {
					baseInfo.setTrainingId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					baseInfo.setDepCode(data[1].toString());
				}
				if (data[2] != null) {
					baseInfo.setTrainingSubject(data[2].toString());
				}
				if (data[3] != null) {
					baseInfo.setTrainingTime((Date) data[3]);
				}
				if (data[4] != null) {
					baseInfo.setTrainingSpeaker(data[4].toString());
				}
				if (data[5] != null) {
					baseInfo.setCompletion(data[5].toString());
				}
				if (data[6] != null) {
					baseInfo.setContent(data[6].toString());
				}
				if (data[7] != null) {
					baseInfo.setMemo(data[7].toString());
				}
				if (data[9] != null) {
					model.setTrainingTime(data[9].toString());
				}
				if (data[10] != null) {
					model.setSpeakerName(data[10].toString());
				}
				if (data[11] != null) {
					model.setDeptName(data[11].toString());
				}
				model.setBaseInfo(baseInfo);
				arraylist.add(model);
			}
			Long count = Long.parseLong(bll.getSingal(countsql).toString());

			result.setList(arraylist);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}