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
import power.ejb.manage.plan.form.BpCPlanTopicItemForm;

/**
 * Facade for entity BpCPlanTopicItem.
 * 
 * @see power.ejb.manage.plan.BpCPlanTopicItem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCPlanTopicItemFacade implements BpCPlanTopicItemFacadeRemote {
	// property constants
	public static final String ITEM_NAME = "itemName";
	public static final String DISPLAY_NO = "displayNo";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * Perform an initial save of a previously unsaved BpCPlanTopicItem entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpCPlanTopicItem entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCPlanTopicItem entity) {
		LogUtil.log("saving BpCPlanTopicItem instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<BpCPlanTopicItem> addList,
			List<BpCPlanTopicItem> updateList, String ids) {
		delete(ids);

		if (addList != null && addList.size() > 0) {

			for (BpCPlanTopicItem entity : addList) {

				this.save(entity);
			}
			update(updateList);
		}
	}

	public Long isNew(String topicCode, String itemCode) {
		Long count;
		String sql = "select count(*) from bp_c_plan_topic_item t "
				+ " where t.topic_code='" + topicCode + "'"
				+ " and t.item_code='" + itemCode + "'";
		count = Long.valueOf(dll.getSingal(sql).toString());
		return count;
	}

	public boolean deleteTopicItem(String topicCode, String enterpriseCode) {
		try {
			String sql = "delete from bp_c_plan_topic_item t"
					+ " where t.topic_code='" + topicCode + "'"
					+ " and t.enterprise_code='" + enterpriseCode + "'";
			dll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException e) {
			throw e;
			// return false;
		}

	}

	public boolean delete(String ids) {
		try {

			String[] temp1 = ids.split(";");
			String temp3 = "";

			for (String i : temp1) {
				String[] temp2 = i.split(",");
				BpCPlanTopicItemId id = new BpCPlanTopicItemId();
				id.setTopicCode(temp2[0]);
				id.setItemCode(temp2[1]);
				temp3 += "'" + temp2[1] + "',";
				this.delete(this.findById(id));
			}
			String sql = "delete from bp_j_plan_topic_item t"
					+ " where t.item_code in("
					+ temp3.substring(0, temp3.length() - 1) + ")";

			dll.exeNativeSQL(sql);

			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Delete a persistent BpCPlanTopicItem entity.
	 * 
	 * @param entity
	 *            BpCPlanTopicItem entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpCPlanTopicItem entity) {
		LogUtil.log("deleting BpCPlanTopicItem instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpCPlanTopicItem.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpCPlanTopicItem entity and return it or a
	 * copy of it to the sender. A copy of the BpCPlanTopicItem entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCPlanTopicItem entity to update
	 * @return BpCPlanTopicItem the persisted BpCPlanTopicItem entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpCPlanTopicItem update(BpCPlanTopicItem entity) {
		LogUtil.log("updating BpCPlanTopicItem instance", Level.INFO, null);
		try {
			BpCPlanTopicItem result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void update(List<BpCPlanTopicItem> updateList) {
		if (updateList != null && updateList.size() > 0) {

			for (BpCPlanTopicItem entity : updateList) {

				this.update(entity);
			}
		}
	}

	public BpCPlanTopicItem findById(BpCPlanTopicItemId id) {
		LogUtil.log("finding BpCPlanTopicItem instance with id: " + id,
				Level.INFO, null);
		try {
			BpCPlanTopicItem instance = entityManager.find(
					BpCPlanTopicItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpCPlanTopicItem entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCPlanTopicItem property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCPlanTopicItem> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpCPlanTopicItem> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpCPlanTopicItem instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpCPlanTopicItem model where model."
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
	 * Find all BpCPlanTopicItem entities.
	 * 
	 * @return List<BpCPlanTopicItem> all BpCPlanTopicItem entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String topicCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.*,s.item_name itemName\n"
					+ " FROM bp_c_plan_topic_item t" + ",bp_c_plan_item s \n"
					+ "    WHERE  t.enterprise_code = '" + enterpriseCode + "'"
					+ "    and  t.topic_code = '" + topicCode + "'"
					+ "    and  t.item_code = s.item_code" + "\n"
					+ " ORDER BY t.display_no \n";

			String sqlCount = "SELECT count(*)\n"
					+ " FROM bp_c_plan_topic_item t" + ",bp_c_plan_item s \n"
					+ "    WHERE  t.enterprise_code = '" + enterpriseCode + "'"
					+ "    and  t.topic_code = '" + topicCode + "'"
					+ "    and  t.item_code = s.item_code" + "\n"
					+ " ORDER BY t.display_no \n";
			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);

			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {

				Object[] data = (Object[]) it.next();

				BpCPlanTopicItemForm model = new BpCPlanTopicItemForm();
				BpCPlanTopicItem baseInfo = new BpCPlanTopicItem();
				BpCPlanTopicItemId id = new BpCPlanTopicItemId();
				if (data[0] != null) {
					id.setTopicCode(data[0].toString());
				}
				if (data[1] != null) {
					id.setItemCode(data[1].toString());
				}

				baseInfo.setId(id);

				if (data[3] != null) {
					baseInfo.setDisplayNo(Long.parseLong(data[3].toString()));
				}
				if (data[5] != null) {
					model.setItemName(data[5].toString());
				}
				model.setBaseInfo(baseInfo);

				arrlist.add(model);
			}
			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(arrlist);
			result.setTotalCount(count);

			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

}