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
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.run.securityproduction.form.BriefNewsForm;
import power.ejb.run.securityproduction.form.SafetyAnnualPlanForm;

/**
 * Facade for entity SpJSafetyAnnualplan.
 * 
 * @see power.ejb.run.securityproduction.SpJSafetyAnnualplan
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJSafetyAnnualplanFacade implements
		SpJSafetyAnnualplanFacadeRemote {
	// property constants
	public static final String PLAN_YEAR = "planYear";
	public static final String DEP_CODE = "depCode";
	public static final String PLAN_CONTENT = "planContent";
	public static final String MEMO = "memo";
	public static final String FILL_BY = "fillBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved SpJSafetyAnnualplan
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            SpJSafetyAnnualplan entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public SpJSafetyAnnualplan save(SpJSafetyAnnualplan entity) {
		LogUtil.log("saving SpJSafetyAnnualplan instance", Level.INFO, null);
		try {
			if(entity.getAnnualplanId()== null){
				entity.setAnnualplanId(bll.getMaxId("SP_J_SAFETY_ANNUALPLAN", "ANNUALPLAN_ID"));	
			}
			entityManager.persist(entity);
			return entity;
			
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent SpJSafetyAnnualplan entity.
	 * 
	 * @param entity
	 *            SpJSafetyAnnualplan entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SpJSafetyAnnualplan entity) {
		LogUtil.log("deleting SpJSafetyAnnualplan instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(SpJSafetyAnnualplan.class,
					entity.getAnnualplanId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved SpJSafetyAnnualplan entity and return it or a
	 * copy of it to the sender. A copy of the SpJSafetyAnnualplan entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            SpJSafetyAnnualplan entity to update
	 * @return SpJSafetyAnnualplan the persisted SpJSafetyAnnualplan entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SpJSafetyAnnualplan update(SpJSafetyAnnualplan entity) {
		LogUtil.log("updating SpJSafetyAnnualplan instance", Level.INFO, null);
		try {
			SpJSafetyAnnualplan result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJSafetyAnnualplan findById(Long id) {
		LogUtil.log("finding SpJSafetyAnnualplan instance with id: " + id,
				Level.INFO, null);
		try {
			SpJSafetyAnnualplan instance = entityManager.find(
					SpJSafetyAnnualplan.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all SpJSafetyAnnualplan entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SpJSafetyAnnualplan property to query
	 * @param value
	 *            the property value to match
	 * @return List<SpJSafetyAnnualplan> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<SpJSafetyAnnualplan> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding SpJSafetyAnnualplan instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from SpJSafetyAnnualplan model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<SpJSafetyAnnualplan> findByPlanYear(Object planYear) {
		return findByProperty(PLAN_YEAR, planYear);
	}

	public List<SpJSafetyAnnualplan> findByDepCode(Object depCode) {
		return findByProperty(DEP_CODE, depCode);
	}

	public List<SpJSafetyAnnualplan> findByPlanContent(Object planContent) {
		return findByProperty(PLAN_CONTENT, planContent);
	}

	public List<SpJSafetyAnnualplan> findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}

	public List<SpJSafetyAnnualplan> findByFillBy(Object fillBy) {
		return findByProperty(FILL_BY, fillBy);
	}

	public List<SpJSafetyAnnualplan> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all SpJSafetyAnnualplan entities.
	 * 
	 * @return List<SpJSafetyAnnualplan> all SpJSafetyAnnualplan entities
	 */
	@SuppressWarnings("unchecked")
	public List<SafetyAnnualPlanForm> findAll(String year) {
//		LogUtil.log("finding all SpJSafetyAnnualplan instances", Level.INFO,
//				null);
		try {
			final String queryString = 
				"select t.annualplan_id,\n" +
				"       t.plan_year,\n" + 
				"       t.dep_code,\n" + 
				"       getdeptname(t.dep_code) depName,\n" + 
				"       t.plan_content,\n" + 
				"       t.memo,\n" + 
				"       t.fill_by,\n" + 
				"       getworkername(t.fill_by) fillName,\n" + 
				"       to_char(t.fill_time,'yyyy-MM-dd') fillTime\n" + 
				"  from sp_j_safety_annualplan t\n" + 
				"  where t.plan_year like '"+year+"%'";
			List list = bll.queryByNativeSQL(queryString);
			List<SafetyAnnualPlanForm> arrayList = new ArrayList<SafetyAnnualPlanForm>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				SpJSafetyAnnualplan model = new SpJSafetyAnnualplan();
				SafetyAnnualPlanForm form = new SafetyAnnualPlanForm();
				if (data[0] != null) {
					model.setAnnualplanId(Long.parseLong(data[0].toString()));
				}
				if(data[1] != null)
					model.setPlanYear(data[1].toString());
				if(data[2] != null)
					model.setDepCode(data[2].toString());
				if(data[3] != null)
					form.setDepName(data[3].toString());
				if(data[4] != null)
					model.setPlanContent(data[4].toString());
				if(data[5] != null)
					model.setMemo(data[5].toString());
				if(data[6] != null)
					model.setFillBy(data[6].toString());
				if(data[7] != null)
					form.setFillName(data[7].toString());
				if(data[8] != null)
					form.setFillTime(data[8].toString());
				form.setSafetyAnnualplan(model);
				arrayList.add(form);
			}
			return arrayList;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}