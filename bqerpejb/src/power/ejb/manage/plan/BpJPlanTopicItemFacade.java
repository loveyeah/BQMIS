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
import power.ejb.manage.plan.form.BpJPlanTopicItemForm;

/**
 * Facade for entity BpJPlanTopicItem.
 * 
 * @see power.ejb.manage.plan.BpJPlanTopicItem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJPlanTopicItemFacade implements BpJPlanTopicItemFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "BpCPlanTopicItemFacade")
	protected BpCPlanTopicItemFacadeRemote CTIRemote;
	@EJB(beanName = "BpJPlanTopicFacade")
	protected BpJPlanTopicFacadeRemote JTRemote;

	
	public void save(BpJPlanTopicItem entity) {
		LogUtil.log("saving BpJPlanTopicItem instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<BpJPlanTopicItem> addList,
			List<BpJPlanTopicItem> updateList, String delString) {
		if (addList.size() > 0) {
			Long id = bll.getMaxId("bp_j_plan_topic_item", "topic_Item_Id");
			for (BpJPlanTopicItem entity : addList) {

				entity.setTopicItemId(id++);
				save(entity);
			}

		}
		if (updateList.size() > 0) {

			for (BpJPlanTopicItem entity : updateList) {

				update(entity);
			}

		}
		if (delString != null) {
			String sqldel = "delete from bp_j_plan_topic_item t "
					+ " where t.topic_item_id in (" + delString + ")";
			bll.exeNativeSQL(sqldel);
		}

	}

	public void deleteByReportId(String reportId) {
		String sqldel = "delete from bp_j_plan_topic_item t "
				+ " where t.report_id=" + reportId;
		bll.exeNativeSQL(sqldel);

	}

	
	public void delete(BpJPlanTopicItem entity) {
		LogUtil.log("deleting BpJPlanTopicItem instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpJPlanTopicItem.class, entity
					.getTopicItemId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public BpJPlanTopicItem update(BpJPlanTopicItem entity) {
		LogUtil.log("updating BpJPlanTopicItem instance", Level.INFO, null);
		try {
			BpJPlanTopicItem result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJPlanTopicItem findById(Long id) {
		LogUtil.log("finding BpJPlanTopicItem instance with id: " + id,
				Level.INFO, null);
		try {
			BpJPlanTopicItem instance = entityManager.find(
					BpJPlanTopicItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	

	@SuppressWarnings("unchecked")
	public PageObject queryBpJPlanTopicItemList(String planTime,
			String topicCode, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String reportId = JTRemote.findByPlanTimeAndTopicCode(planTime,
					topicCode);

			String sql = "select s.item_code,s.item_name,"
					+ "m.plan_value,m.last_value,m.topic_item_id,n.unit_name"
					+ " from bp_c_plan_topic_item t ,bp_c_plan_item s,"
					+ " bp_j_plan_topic_item m,bp_c_measure_unit  n"
					+ " where s.item_code=t.item_code "
					+ " and  t.item_code=m.item_code(+)"
					+ " and s.unit_code=n.unit_id" + "    and t.topic_code='"
					+ topicCode + "'";
			if (reportId != null) {
				sql += " and m.report_id=" + reportId;
			}

			String sqlCount = "SELECT count(*)\n"
					+ " from bp_c_plan_topic_item t ,bp_c_plan_item s,"
					+ " bp_j_plan_topic_item m,bp_c_measure_unit  n"
					+ " where s.item_code=t.item_code "
					+ " and  t.item_code=m.item_code(+)"
					+ " and s.unit_code=n.unit_id" + "    and t.topic_code='"
					+ topicCode + "'";
			sql +=" order by t.display_no";
			List list = bll.queryByNativeSQL(sql);

			List<BpJPlanTopicItemForm> arrlist = new ArrayList<BpJPlanTopicItemForm>();
			Iterator it = list.iterator();
			while (it.hasNext()) {

				Object[] data = (Object[]) it.next();

				BpJPlanTopicItemForm model = new BpJPlanTopicItemForm();

				if (data[0] != null) {
					model.setItemCode(data[0].toString());
				}
				if (data[1] != null) {
					model.setItemName(data[1].toString());
				}
				if (data[2] != null) {
					model.setPlanValue(Double.parseDouble(data[2].toString()));
				}
				if (data[3] != null) {
					model.setLastValue(Double.parseDouble(data[3].toString()));
				}
				if (data[4] != null) {
					model.setTopicItemId(Long.parseLong(data[4].toString()));
				}

				if (data[5] != null) {
					model.setUnitName(data[5].toString());
				}

				arrlist.add(model);
			}
			Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(arrlist);
			result.setTotalCount(count);

			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Find all BpJPlanTopicItem entities.
	 * 
	 * @return List<BpJPlanTopicItem> all BpJPlanTopicItem entities
	 */
	@SuppressWarnings("unchecked")
	public List<BpJPlanTopicItem> findAll() {
		LogUtil.log("finding all BpJPlanTopicItem instances", Level.INFO, null);
		try {
			final String queryString = "select model from BpJPlanTopicItem model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}