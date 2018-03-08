package power.ejb.manage.budget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.budget.form.CbmCenterTopicForm;
import power.ejb.manage.budget.form.CbmJBudgetItemForm;

/**
 * Facade for entity CbmCCenterTopic.
 * 
 * @see power.ejb.manage.budget.CbmCCenterTopic
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCCenterTopicFacade implements CbmCCenterTopicFacadeRemote {
	// property constants
	public static final String CENTER_ID = "centerId";
	public static final String TOPIC_ID = "topicId";
	public static final String DIRECT_MANAGER = "directManager";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(CbmCCenterTopic entity) {
		LogUtil.log("saving CbmCCenterTopic instance", Level.INFO, null);
		try {
			entity.setCenterTopicId(bll.getMaxId("CBM_C_CENTER_TOPIC",
					"center_topic_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<CbmCCenterTopic> addList) {
		if (addList != null && addList.size() > 0) {
			for (CbmCCenterTopic entity : addList) {
				this.save(entity);
				entityManager.flush();
			}
		}
	}

	public Long isNew(Long cTopicId) {
		Long count;
		String sql = "select count(*)\n"
				+ "              from CBM_C_CENTER_TOPIC t\n"
				+ "             where t.center_topic_id = " + cTopicId + "";
		count = Long.valueOf(bll.getSingal(sql).toString());
		return count;
	}

	public void deleteMuti(String ids) {
		String sql = "update CBM_C_CENTER_TOPIC a set a.is_use='N' "
				+ " where a.center_topic_id in (" + ids + ")";
		bll.exeNativeSQL(sql);
	}

	public CbmCCenterTopic update(CbmCCenterTopic entity) {
		LogUtil.log("updating CbmCCenterTopic instance", Level.INFO, null);
		try {
			CbmCCenterTopic result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void update(List<CbmCCenterTopic> updateList) {
		if (updateList != null && updateList.size() > 0) {
			for (CbmCCenterTopic entity : updateList) {
				this.update(entity);
			}
		}
	}

	public CbmCCenterTopic findById(Long id) {
		LogUtil.log("finding CbmCCenterTopic instance with id: " + id,
				Level.INFO, null);
		try {
			CbmCCenterTopic instance = entityManager.find(
					CbmCCenterTopic.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String centerId, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		LogUtil.log("finding all CbmCCenterTopic instances", Level.INFO, null);
		try {
			PageObject pg = new PageObject();
			String sqlCount = "select count(1)\n"
					+ "  from CBM_C_CENTER_TOPIC a, CBM_C_CENTER b, CBM_C_TOPIC c\n"
					+ " where a.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and b.center_id = '" + centerId + "'\n"
					+ "    and a.is_use = 'Y'\n"
					+ "   and a.center_id = b.center_id\n"
					+ "   and a.topic_id = c.topic_id\n"
					+ " order by c.topic_code, b.dep_code";
			Long count = Long.parseLong(bll.getSingal(sqlCount).toString());

			String sql = "select a.center_topic_id,\n"
					+ "       a.center_id,\n"
					+ "       a.topic_id,\n"
					+ "       a.direct_manager,\n"
					+ "       a.is_use,\n"
					+ "       a.enterprise_code,\n"
					+ "       b.dep_code,\n"
					+ "       b.dep_name,\n"
					+ "       c.topic_code,\n"
					+ "       c.topic_name,\n"
					+ "       getworkername(a.direct_manager)\n"
					+ "  from CBM_C_CENTER_TOPIC a, CBM_C_CENTER b, CBM_C_TOPIC c\n"
					+ " where a.enterprise_code = '" + enterpriseCode + "'\n"
					+ " and b.enterprise_code = '" + enterpriseCode + "'\n"
					+ " and c.enterprise_code = '" + enterpriseCode + "'\n"
					+ "    and a.is_use = 'Y'\n" + "    and b.is_use = 'Y'\n"
					+ "    and c.is_use = 'Y'\n" + "   and b.center_id = '"
					+ centerId + "'\n" + "   and a.center_id = b.center_id\n"
					+ "   and a.topic_id = c.topic_id\n"
					+ " order by c.topic_code, b.dep_code";

			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				CbmCenterTopicForm form = new CbmCenterTopicForm();
				CbmCCenterTopic model = new CbmCCenterTopic();
				if (data[0] != null)
					model.setCenterTopicId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setCenterId(Long.parseLong(data[1].toString()));
				if (data[2] != null)
					model.setTopicId(Long.parseLong(data[2].toString()));
				if (data[3] != null)
					model.setDirectManager(data[3].toString());
				if (data[4] != null)
					model.setIsUse(data[4].toString());
				if (data[5] != null)
					model.setEnterpriseCode(data[5].toString());
				if (data[6] != null)
					form.setDeptCode(data[6].toString());
				if (data[7] != null)
					form.setDeptName(data[7].toString());
				if (data[8] != null)
					form.setTopicCode(data[8].toString());
				if (data[9] != null)
					form.setTopicName(data[9].toString());
				if (data[10] != null)
					form.setDirectManageName(data[10].toString());

				form.setTop(model);
				arrlist.add(form);

			}
			pg.setList(arrlist);
			pg.setTotalCount(count);
			return pg;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject findAllInMaint(String enterpriseCode, String isquery,
			final int... rowStartIdxAndCount) {
		try {
			PageObject pg = new PageObject();
			String sqlCount = "select count(distinct a.center_id) \n"
					+ "  from CBM_C_CENTER_TOPIC a,CBM_C_CENTER b \n"
					+ " where a.enterprise_code = '" + enterpriseCode + "'\n"
					+ " and b.enterprise_code = '" + enterpriseCode + "'\n"
					+ " and a.center_id=b.center_id \n"
					+ "    and a.is_use = 'Y'\n" + "   and b.is_use='Y' \n";

			
			String sql ="";
			// ---------add by ltong-------------
			if (isquery != null && isquery.equals("1")) {
				sql= "select distinct a.center_id, b.dep_code, b.dep_name, a.center_topic_id\n"
					+ "  from CBM_C_CENTER_TOPIC a, CBM_C_CENTER b\n"
					+ " where a.enterprise_code = '"
					+ enterpriseCode
					+ "'\n"
					+ "   and b.enterprise_code = '"
					+ enterpriseCode
					+ "'\n"
					+ "   and a.center_id = b.center_id\n"
					+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n";
				sqlCount = sqlCount
						+ "   and a.topic_id =\n"
						+ "       (select t.topic_id from CBM_C_TOPIC t where t.topic_code = '705')\n";
				sql = sql
						+ "   and a.topic_id =\n"
						+ "       (select t.topic_id from CBM_C_TOPIC t where t.topic_code = '705')\n";
			}
			else {
				
				sql= "select distinct a.center_id, b.dep_code, b.dep_name, 0\n"
					+ "  from CBM_C_CENTER_TOPIC a, CBM_C_CENTER b\n"
					+ " where a.enterprise_code = '"
					+ enterpriseCode
					+ "'\n"
					+ "   and b.enterprise_code = '"
					+ enterpriseCode
					+ "'\n"
					+ "   and a.center_id = b.center_id\n"
					+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n";
			}
			sql = sql + "order by a.center_id \n";
			// ---------add by ltong----end---------
			Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				CbmCenterTopicForm form = new CbmCenterTopicForm();
				CbmCCenterTopic model = new CbmCCenterTopic();
				if (data[0] != null)
					model.setCenterId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					form.setDeptCode(data[1].toString());
				if (data[2] != null)
					form.setDeptName(data[2].toString());
				// add by ltong 预算部门主题ID
				if (data[3] != null)
					model.setCenterTopicId(Long.parseLong(data[3].toString()));

				form.setTop(model);
				arrlist.add(form);

			}
			pg.setList(arrlist);
			pg.setTotalCount(count);
			return pg;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<CbmJBudgetItemForm> getDeptList(Long topicId,
			String enterprseCode) {
		List<CbmJBudgetItemForm> list = new ArrayList<CbmJBudgetItemForm>();
		String sql = "select a.center_id, \n" + "b.dep_name \n"
				+ "from CBM_C_CENTER_TOPIC a,CBM_C_CENTER b  \n"
				+ "where a.topic_id=" + topicId + " \n"
				+ "and a.center_id=b.center_id \n"
				+ "and a.is_use=b.is_use and a.is_use='Y' \n"
				+ "and a.enterprise_code='" + enterprseCode
				+ "'  order by b.dep_name\n";
		List sqlList = bll.queryByNativeSQL(sql);
		if (sqlList != null && sqlList.size() > 0) {
			Iterator it = sqlList.iterator();
			while (it.hasNext()) {
				CbmJBudgetItemForm form = new CbmJBudgetItemForm();
				Object[] data = (Object[]) it.next();
				form.setCenterId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					form.setCenterName(data[1].toString());
				list.add(form);
			}
		}
		return list;
	}

	public String judgeSource(Long topicId, Long centerId, Long itemId,
			String enterpriseCode) {
		String sql = "select a.data_source \n"
				+ " from CBM_C_CENTER_ITEM a,CBM_C_CENTER_TOPIC b \n"
				+ " where a.center_topic_id=b.center_topic_id \n"
				+ "and b.center_id=" + centerId + " \n" + "and b.topic_id="
				+ topicId + " \n" + "and a.item_id=" + itemId + " \n"
				//+ "and a.is_use='Y' \n" + "and b.is_use='Y' \n" //modify by fyyang 20100610
				+ " and a.enterprise_code=b.enterprise_code \n"
				+ "and a.enterprise_code='" + enterpriseCode + "'   and rownum=1\n";
//		System.out.println("the sql"+sql);
		Object obj = bll.getSingal(sql);
		if (obj == null) {
			return null;
		} else {
			return obj.toString();
		}

	}
}