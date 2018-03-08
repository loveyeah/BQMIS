package power.ejb.manage.plan;

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
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.plan.form.BpJPlanGuideDetailForm;

/**
 * Facade for entity BpJPlanGuideDetail.
 * 
 * @see power.ejb.manage.plan.BpJPlanGuideDetail
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJPlanGuideDetailFacade implements BpJPlanGuideDetailFacadeRemote {
	// property constants
	public static final String GUIDE_CONTENT = "guideContent";
	public static final String REFER_DEPCODE = "referDepcode";
	public static final String MAIN_DEPCODE = "mainDepcode";
	public static final String TASK_DEPCODE = "taskDepcode";
	public static final String IF_COMPLETE = "ifComplete";
	public static final String COMPLETE_DESC = "completeDesc";
	public static final String IF_CHECK = "ifCheck";
	public static final String CHECK_STATUS = "checkStatus";
	public static final String TARGET_DEPCODE = "targetDepcode";
	public static final String CHECK_DESC = "checkDesc";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * Perform an initial save of a previously unsaved BpJPlanGuideDetail
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            BpJPlanGuideDetail entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpJPlanGuideDetail entity) {
		LogUtil.log("saving BpJPlanGuideDetail instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent BpJPlanGuideDetail entity.
	 * 
	 * @param entity
	 *            BpJPlanGuideDetail entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJPlanGuideDetail entity) {
		LogUtil.log("deleting BpJPlanGuideDetail instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpJPlanGuideDetail.class,
					entity.getGuideId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpJPlanGuideDetail entity and return it or a
	 * copy of it to the sender. A copy of the BpJPlanGuideDetail entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanGuideDetail entity to update
	 * @return BpJPlanGuideDetail the persisted BpJPlanGuideDetail entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanGuideDetail update(BpJPlanGuideDetail entity) {
		LogUtil.log("updating BpJPlanGuideDetail instance", Level.INFO, null);
		try {
			BpJPlanGuideDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<BpJPlanGuideDetail> addList) {
		if (addList != null && addList.size() > 0) {
			int i = 0;
			Long idtemp = dll.getMaxId("BP_J_PLAN_GUIDE_DETAIL", "guide_id");
			for (BpJPlanGuideDetail entity : addList) {
				Long id = idtemp + i++;

				entity.setGuideId(id);
				this.save(entity);
			}
		}
	}

	public void update(List<BpJPlanGuideDetail> updateList) {
		if (updateList != null && updateList.size() > 0) {

			for (BpJPlanGuideDetail entity : updateList) {

				this.update(entity);
			}
		}
	}

	public boolean delete(String ids) {
		try {
			String[] temp1 = ids.split(",");

			for (String i : temp1) {
				BpJPlanGuideDetail entity = new BpJPlanGuideDetail();
				entity = this.findById(Long.parseLong(i));
				this.delete(entity);

			}

			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public BpJPlanGuideDetail findById(Long id) {
		LogUtil.log("finding BpJPlanGuideDetail instance with id: " + id,
				Level.INFO, null);
		try {
			BpJPlanGuideDetail instance = entityManager.find(
					BpJPlanGuideDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpJPlanGuideDetail entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJPlanGuideDetail property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJPlanGuideDetail> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpJPlanGuideDetail> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpJPlanGuideDetail instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpJPlanGuideDetail model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<BpJPlanGuideDetail> findByGuideContent(Object guideContent) {
		return findByProperty(GUIDE_CONTENT, guideContent);
	}

	public List<BpJPlanGuideDetail> findByReferDepcode(Object referDepcode) {
		return findByProperty(REFER_DEPCODE, referDepcode);
	}

	public List<BpJPlanGuideDetail> findByMainDepcode(Object mainDepcode) {
		return findByProperty(MAIN_DEPCODE, mainDepcode);
	}

	public List<BpJPlanGuideDetail> findByTaskDepcode(Object taskDepcode) {
		return findByProperty(TASK_DEPCODE, taskDepcode);
	}

	public List<BpJPlanGuideDetail> findByIfComplete(Object ifComplete) {
		return findByProperty(IF_COMPLETE, ifComplete);
	}

	public List<BpJPlanGuideDetail> findByCompleteDesc(Object completeDesc) {
		return findByProperty(COMPLETE_DESC, completeDesc);
	}

	public List<BpJPlanGuideDetail> findByIfCheck(Object ifCheck) {
		return findByProperty(IF_CHECK, ifCheck);
	}

	public List<BpJPlanGuideDetail> findByCheckStatus(Object checkStatus) {
		return findByProperty(CHECK_STATUS, checkStatus);
	}

	public List<BpJPlanGuideDetail> findByTargetDepcode(Object targetDepcode) {
		return findByProperty(TARGET_DEPCODE, targetDepcode);
	}

	public List<BpJPlanGuideDetail> findByCheckDesc(Object checkDesc) {
		return findByProperty(CHECK_DESC, checkDesc);
	}

	public List<BpJPlanGuideDetail> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all BpJPlanGuideDetail entities.
	 * 
	 * @return List<BpJPlanGuideDetail> all BpJPlanGuideDetail entities
	 */
	@SuppressWarnings("unchecked")
	public List<BpJPlanGuideDetail> findAll() {
		LogUtil.log("finding all BpJPlanGuideDetail instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from BpJPlanGuideDetail model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject getPlanGuideDetail(String planTime,
			String enterpriseCode, int... rowStartIdxAndCount) {
		try {
			PageObject object = new PageObject();
			String strWhere = "";
			String sqlString = "select s.*,\n"
					+ "       getdeptname(s.refer_depcode) referDepName,\n"
					+ "       getdeptname(s.main_depcode) mainDepName, \n"
					+ "       getdeptname(s.target_depcode) targetDepName, \n"
					+ "       decode(s.if_complete,'0','未完成','1','进行中','2','已完成','') ifCompleteName, \n"
					+ "       decode(s.if_check,'0','否','1','是','') ifCheckName, \n"
					+ "       decode(s.check_status,'0','未考核','1','已考核','') checkstatusName \n"
					+ "  from bp_j_plan_guide_detail s where s.plan_time = to_date('"
					+ planTime + "'||'-01','yyyy-MM-dd')\n"
					+ "  and s.enterprise_code = '" + enterpriseCode + "'";
			String sqlCount = "select count(*)\n"
					+ "  from bp_j_plan_guide_detail s where s.plan_time = to_date('"
					+ planTime + "'||'-01','yyyy-MM-dd')\n"
					+ "  and s.enterprise_code = '" + enterpriseCode + "'";
			List list = dll.queryByNativeSQL(sqlString, rowStartIdxAndCount);
			List<BpJPlanGuideDetailForm> arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				BpJPlanGuideDetailForm model = new BpJPlanGuideDetailForm();
				BpJPlanGuideDetail baseInfo = new BpJPlanGuideDetail();
				if (data[0] != null)
					baseInfo.setGuideId(Long.parseLong(data[0].toString()));
				if (data[2] != null)
					baseInfo.setGuideContent(data[2].toString());
				if (data[3] != null)
					baseInfo.setReferDepcode(data[3].toString());
				if (data[4] != null)
					baseInfo.setMainDepcode(data[4].toString());
				if (data[5] != null)
					baseInfo.setTaskDepcode(data[5].toString());
				if (data[6] != null)
					baseInfo.setIfComplete(data[6].toString());
				if (data[7] != null)
					baseInfo.setCompleteDesc(data[7].toString());
				if (data[8] != null)
					baseInfo.setIfCheck(data[8].toString());
				if (data[9] != null)
					baseInfo.setCheckStatus(data[9].toString());
				if (data[10] != null)
					baseInfo.setTargetDepcode(data[10].toString());
				if (data[11] != null)
					baseInfo.setCheckDesc(data[11].toString());
				if (data[13] != null)
					model.setReferDepName(data[13].toString());
				if (data[14] != null)
					model.setMainDepName(data[14].toString());
				if (data[15] != null)
					model.setTargetDepName(data[15].toString());
				if (data[16] != null)
					model.setIfCompleteName(data[16].toString());
				if (data[17] != null)
					model.setIfCheckName(data[17].toString());
				if (data[18] != null)
					model.setCheckStatusName(data[18].toString());
				model.setBaseInfo(baseInfo);
				arrlist.add(model);
			}
			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			object.setList(arrlist);
			object.setTotalCount(count);
			return object;
		} catch (RuntimeException re) {
			throw re;
		}
	}

}