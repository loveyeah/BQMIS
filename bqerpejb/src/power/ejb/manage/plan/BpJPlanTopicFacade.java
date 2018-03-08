package power.ejb.manage.plan;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity BpJPlanTopic.
 * 
 * @see power.ejb.manage.plan.BpJPlanTopic
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJPlanTopicFacade implements BpJPlanTopicFacadeRemote {
	// property constants
	public static final String TOPIC_CODE = "topicCode";
	public static final String EDIT_BY = "editBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public BpJPlanTopic save(BpJPlanTopic entity) throws ParseException {
		LogUtil.log("saving BpJPlanTopic instance", Level.INFO, null);
		try {

			entity.setReportId(bll.getMaxId("BP_J_PLAN_TOPIC", "report_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);

			return entity;

		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(BpJPlanTopic entity) {
		LogUtil.log("deleting BpJPlanTopic instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpJPlanTopic.class, entity
					.getReportId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public BpJPlanTopic update(BpJPlanTopic entity) {
		LogUtil.log("updating BpJPlanTopic instance", Level.INFO, null);
		try {
			BpJPlanTopic result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJPlanTopic findById(Long id) {
		LogUtil.log("finding BpJPlanTopic instance with id: " + id, Level.INFO,
				null);
		try {
			BpJPlanTopic instance = entityManager.find(BpJPlanTopic.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<BpJPlanTopic> findAll() {
		LogUtil.log("finding all BpJPlanTopic instances", Level.INFO, null);
		try {
			final String queryString = "select model from BpJPlanTopic model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public String findByPlanTimeAndTopicCode(String planTime, String topicCode) {
		String sqlString = "select t.report_id from bp_j_plan_topic t"
				+ " where t.topic_code='" + topicCode + "'"
				+ " and t.plan_time=to_date( '" + planTime
				+ "'||'-01','yyyy-mm-dd' )";

		String reportIdString = bll.getSingal(sqlString) == null ? null : bll
				.getSingal(sqlString).toString();
		return reportIdString;

	}
}