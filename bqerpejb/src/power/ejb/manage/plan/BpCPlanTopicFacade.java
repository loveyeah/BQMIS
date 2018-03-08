package power.ejb.manage.plan;

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

/**
 * Facade for entity BpCPlanTopic.
 * 
 * @see power.ejb.manage.plan.BpCPlanTopic
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCPlanTopicFacade implements BpCPlanTopicFacadeRemote {
	// property constants
	public static final String TOPIC_NAME = "topicName";
	public static final String TOPIC_MEMO = "topicMemo";
	public static final String DISPLAY_NO = "displayNo";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@EJB(beanName = "BpCPlanTopicItemFacade")
	protected BpCPlanTopicItemFacadeRemote ItemRemote;

	/**
	 * Perform an initial save of a previously unsaved BpCPlanTopic entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpCPlanTopic entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCPlanTopic entity) {
		LogUtil.log("saving BpCPlanTopic instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<BpCPlanTopic> addList) {
		if (addList != null && addList.size() > 0) {

			Long id = dll.getMaxId("bp_c_plan_topic", "topic_code");
			for (BpCPlanTopic entity : addList) {

				entity.setTopicCode(id.toString());
				id++;
				this.save(entity);
			}
		}
	}

	public boolean delete(String ids) {
		try {
			String[] temp1 = ids.split(",");

			for (String i : temp1) {
				BpCPlanTopic entity = new BpCPlanTopic();
				entity = this.findById(i);
				this.delete(entity);
				ItemRemote.deleteTopicItem(entity.getTopicCode(), entity
						.getEnterpriseCode());

			}

			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public void update(List<BpCPlanTopic> updateList) {

		if (updateList != null && updateList.size() > 0) {

			for (BpCPlanTopic entity : updateList) {

				this.update(entity);
			}
		}

	}

	/**
	 * Delete a persistent BpCPlanTopic entity.
	 * 
	 * @param entity
	 *            BpCPlanTopic entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpCPlanTopic entity) {
		LogUtil.log("deleting BpCPlanTopic instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpCPlanTopic.class, entity
					.getTopicCode());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpCPlanTopic entity and return it or a copy of
	 * it to the sender. A copy of the BpCPlanTopic entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            BpCPlanTopic entity to update
	 * @return BpCPlanTopic the persisted BpCPlanTopic entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpCPlanTopic update(BpCPlanTopic entity) {
		LogUtil.log("updating BpCPlanTopic instance", Level.INFO, null);
		try {
			BpCPlanTopic result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCPlanTopic findById(String id) {
		LogUtil.log("finding BpCPlanTopic instance with id: " + id, Level.INFO,
				null);
		try {
			BpCPlanTopic instance = entityManager.find(BpCPlanTopic.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpCPlanTopic entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCPlanTopic property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCPlanTopic> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpCPlanTopic> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpCPlanTopic instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpCPlanTopic model where model."
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
	 * Find all BpCPlanTopic entities.
	 * 
	 * @return List<BpCPlanTopic> all BpCPlanTopic entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.*\n" + "  	FROM bp_c_plan_topic t\n"
					+ "    WHERE  t.enterprise_code = '" + enterpriseCode
					+ "'\n" + " ORDER BY t.display_no\n";

			String sqlCount = "SELECT count(*)\n"
					+ "  	FROM bp_c_plan_topic t\n"
					+ "    WHERE  t.enterprise_code = '" + enterpriseCode
					+ "'\n" + " ORDER BY t.display_no\n";

			List<BpCPlanTopic> list = dll.queryByNativeSQL(sql,
					BpCPlanTopic.class, rowStartIdxAndCount);

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(count);

			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Long checkTopicName(String topicName) {
		String sql = "select count(1) from bp_c_plan_topic t"
				+ " where t.topic_name='" + topicName + "'";
		Long count = Long.valueOf(dll.getSingal(sql).toString());
		return count;
	}

	public PageObject findAllFitItem(String argFuzzy,
			int... rowStartIdxAndCount) {
		PageObject result = new PageObject();

		if ("".equals(argFuzzy)) {
			argFuzzy = "%";
		}

		String sql = "select t.*\n" + "  from bp_c_plan_item t\n"
				+ " where (t.item_name like '%" + argFuzzy + "%'\n"
				+ "   or t.item_code like '%" + argFuzzy + "%')"
				+ " and t.is_item='Y'" + "  and t.item_code not in("
				+ " select s.item_code from bp_c_plan_topic_item s )";
		String sqlCount = "select count(*)\n" + "  from bp_c_plan_item t\n"
				+ " where (t.item_name like '%" + argFuzzy + "%'\n"
				+ "   or t.item_code like '%" + argFuzzy + "%')"
				+ " and t.is_item='Y'" + "  and t.item_code not in("
				+ " select s.item_code from bp_c_plan_topic_item s )";

		List<BpCPlanItem> list = dll.queryByNativeSQL(sql, BpCPlanItem.class,
				rowStartIdxAndCount);
		Long count = Long.parseLong(dll.getSingal(sqlCount).toString());

		result.setList(list);
		result.setTotalCount(count);

		return result;
	}

}