package power.ejb.manage.plan;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.plan.form.PlanForewordForm;

/**
 * Facade for entity BpJPlanForeword.
 * 
 * @see power.ejb.manage.plan.BpJPlanForeword
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJPlanForewordFacade implements BpJPlanForewordFacadeRemote {
	// property constants
	public static final String PLAN_FOREWORD = "planForeword";
	public static final String PLAN_STATUS = "planStatus";
	public static final String EDIT_BY = "editBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved BpJPlanForeword entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpJPlanForeword entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public BpJPlanForeword save(BpJPlanForeword entity) {

		try {
			String str = "";
			str = this.DateToString(entity.getPlanTime());
			String sql = "select count(*) from BP_J_PLAN_FOREWORD where to_char(PLAN_TIME,'yyyy-MM-dd') = '"
					+ str + "'";
			if (Long.parseLong(bll.getSingal(sql).toString()) >= 1) {
				return null;
			} else {
				entity.setPlanStatus(0L);
				entityManager.persist(entity);
				return entity;
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			return null;
		}
	}

	/**
	 * Delete a persistent BpJPlanForeword entity.
	 * 
	 * @param entity
	 *            BpJPlanForeword entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(BpJPlanForeword entity) {
		try {
			String str = "";
			str = this.DateToString(entity.getPlanTime());
			String sql = "delete from BP_J_PLAN_FOREWORD where to_char(PLAN_TIME,'yyyy-mm-dd') = '"
					+ str + "'";
			bll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			return false;
		}
	}

	/**
	 * Persist a previously saved BpJPlanForeword entity and return it or a copy
	 * of it to the sender. A copy of the BpJPlanForeword entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanForeword entity to update
	 * @return BpJPlanForeword the persisted BpJPlanForeword entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanForeword update(BpJPlanForeword entity) {
		try {
			BpJPlanForeword result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			return null;
		}
	}

	public BpJPlanForeword findById(Date id) {
		LogUtil.log("finding BpJPlanForeword instance with id: " + id,
				Level.INFO, null);
		try {
			BpJPlanForeword instance = entityManager.find(
					BpJPlanForeword.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpJPlanForeword entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJPlanForeword property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJPlanForeword> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpJPlanForeword> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpJPlanForeword instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpJPlanForeword model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<BpJPlanForeword> findByPlanForeword(Object planForeword) {
		return findByProperty(PLAN_FOREWORD, planForeword);
	}

	public List<BpJPlanForeword> findByPlanStatus(Object planStatus) {
		return findByProperty(PLAN_STATUS, planStatus);
	}

	public List<BpJPlanForeword> findByEditBy(Object editBy) {
		return findByProperty(EDIT_BY, editBy);
	}

	public List<BpJPlanForeword> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all BpJPlanForeword entities.
	 * 
	 * @return List<BpJPlanForeword> all BpJPlanForeword entities
	 */
	@SuppressWarnings("unchecked")
	public List<BpJPlanForeword> findAll() {
		LogUtil.log("finding all BpJPlanForeword instances", Level.INFO, null);
		try {
			final String queryString = "select model from BpJPlanForeword model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PlanForewordForm findModelByPlantime(String planTime,
			String enterpriseCode) throws ParseException {
		try {
			String sql = "select b.plan_time,\n"
					+ "       b.plan_foreword,\n"
					+ "       b.plan_status,\n"
					+ "       b.edit_by,\n"
					+ "       getworkername(b.edit_by) editName,\n"
					+ "       b.edit_date,\n"
					+ "       to_char(b.edit_date, 'yyyy-MM-dd') editDateString\n"
					+ "  from bp_j_plan_foreword b\n"
					+ " where to_char(b.plan_time, 'yyyy-mm') = '" + planTime
					+ "'\n" + "   and b.enterprise_code = '" + enterpriseCode
					+ "'";
			Object[] data = (Object[]) bll.getSingal(sql);
			if (data != null) {
				PlanForewordForm form = new PlanForewordForm();
				BpJPlanForeword model = new BpJPlanForeword();
				java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
						"yyyy-MM-dd");
				if (data[0] != null)
					model.setPlanTime(formatter.parse(data[0].toString()));
				if (data[1] != null)
					model.setPlanForeword(data[1].toString());
				if (data[2] != null)
					model.setPlanStatus(Long.parseLong(data[2].toString()));
				if (data[3] != null)
					model.setEditBy(data[3].toString());
				if (data[4] != null)
					form.setEditName(data[4].toString());
				if (data[5] != null)
					model.setEditDate(formatter.parse(data[5].toString()));
				if (data[6] != null)
					form.setEditDateString(data[6].toString());
				form.setBaseInfo(model);
				return form;
			} else {
				return null;
			}
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			return null;
		}

	}

	/**
	 * 
	 * 将日期类型转换为相应字符串
	 * 
	 * @param date
	 * @return
	 */
	public String DateToString(Date date) {
		String str = "";
		java.text.DateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		str = format.format(date);
		return str;
	}

}