package power.ejb.manage.plan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import power.ejb.manage.plan.form.BpJPlanGuideNewForm;

/**
 * Facade for entity BpJPlanGuideNew.
 * 
 * @see power.ejb.manage.plan.BpJPlanGuideNew
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJPlanGuideNewFacade implements BpJPlanGuideNewFacadeRemote {
	// property constants
	public static final String GUIDE_CONTENT = "guideContent";
	public static final String REFER_DEPCODE = "referDepcode";
	public static final String MAIN_DEPCODE = "mainDepcode";
	public static final String OTHER_DEPCODE = "otherDepcode";
	public static final String IF_COMPLETE = "ifComplete";
	public static final String COMPLETE_DESC = "completeDesc";
	public static final String EDIT_BY = "editBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved BpJPlanGuideNew entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpJPlanGuideNew entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public BpJPlanGuideNew save(BpJPlanGuideNew entity) {
		try {
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyyMM");
			String temp = dateFm.format(entity.getPlanTime());
			entity.setGuideId(this.creatPlanGuidId(temp, entity
					.getEnterpriseCode()));
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent BpJPlanGuideNew entity.
	 * 
	 * @param entity
	 *            BpJPlanGuideNew entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJPlanGuideNew entity) {
		try {
			entity = entityManager.getReference(BpJPlanGuideNew.class, entity
					.getGuideId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpJPlanGuideNew entity and return it or a copy
	 * of it to the sender. A copy of the BpJPlanGuideNew entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanGuideNew entity to update
	 * @return BpJPlanGuideNew the persisted BpJPlanGuideNew entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanGuideNew update(BpJPlanGuideNew entity) {
		try {
			BpJPlanGuideNew result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJPlanGuideNew findById(String id) {
		try {
			BpJPlanGuideNew instance = entityManager.find(
					BpJPlanGuideNew.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpJPlanGuideNew entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJPlanGuideNew property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJPlanGuideNew> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpJPlanGuideNew> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpJPlanGuideNew instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpJPlanGuideNew model where model."
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
	 * Find all BpJPlanGuideNew entities.
	 * 
	 * @return List<BpJPlanGuideNew> all BpJPlanGuideNew entities
	 */
	@SuppressWarnings("unchecked")
	public List<BpJPlanGuideNewForm> findAll(String month) {
		try {
			final String queryString = "select a.guide_id,\n" + "a.edit_by,\n"
					+ "a.guide_content,\n" + "a.refer_depcode,\n"
					+ "a.main_depcode,\n" + "a.other_depcode,\n"
					+ "getworkername(a.edit_by),\n"
					+ "getdeptname(a.refer_depcode),\n"
					+ "getdeptname(a.main_depcode),\n"
					+ "to_char(a.edit_date,'yyyy-MM-dd'),\n"
					+ "a.if_complete,\n" + "a.complete_desc,\n"
					+ "substr(a.guide_id,7)\n" + "from BP_J_PLAN_GUIDE_NEW a\n"
					+ "where to_char(a.plan_time,'yyyy-MM') like '" + month
					+ "'" + " order by a.guide_id";
			List list = bll.queryByNativeSQL(queryString);
			List<BpJPlanGuideNewForm> arraylist = new ArrayList<BpJPlanGuideNewForm>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				BpJPlanGuideNew model = new BpJPlanGuideNew();
				BpJPlanGuideNewForm gnForm = new BpJPlanGuideNewForm();
				if (data[0] != null)
					model.setGuideId(data[0].toString());
				if (data[1] != null)
					model.setEditBy(data[1].toString());
				if (data[2] != null)
					model.setGuideContent(data[2].toString());
				if (data[3] != null)
					model.setReferDepcode(data[3].toString());
				if (data[4] != null)
					model.setMainDepcode(data[4].toString());
				if (data[5] != null)
					model.setOtherDepcode(data[5].toString());
				if (data[6] != null)
					gnForm.setEditByName(data[6].toString());
				if (data[7] != null)
					gnForm.setReferDepName(data[7].toString());
				if (data[8] != null)
					gnForm.setMainDepName(data[8].toString());
				if (data[9] != null)
					gnForm.setEditDate(data[9].toString());
				if (data[10] != null)
					model.setIfComplete(Long.parseLong(data[10].toString()));
				if (data[11] != null)
					model.setCompleteDesc(data[11].toString());
				if (data[12] != null)
					gnForm.setGuideCode(data[12].toString());
				gnForm.setGuideNew(model);
				arraylist.add(gnForm);
			}
			return arraylist;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 生产临时顺序号
	 */
	private String creatPlanGuidId(String planTime, String EnterpriseCode) {
		String sql = "select '"
				+ planTime
				+ "'\n"
				+ "|| nvl(trim(to_char(max(substr(t.guide_id,length(t.guide_id)-2))+1,'000')),'001')\n"
				+ "from bp_j_plan_guide_new t\n"
				+ "where to_char(t.plan_time,'yyyymm') like '" + planTime
				+ "%'";

		Object o = bll.getSingal(sql);
		return o.toString();
	}
}