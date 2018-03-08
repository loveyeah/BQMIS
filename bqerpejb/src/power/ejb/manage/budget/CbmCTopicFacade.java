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

/**
 * 预算主题维护
 * 
 * @author liuyi090803
 */
@Stateless
public class CbmCTopicFacade implements CbmCTopicFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	// private Employee employee;

	/**
	 * 新增一条预算主题维护数据
	 */
	public String save(CbmCTopic entity) {
		LogUtil.log("saving CbmCTopic instance", Level.INFO, null);
		String tempString = "";
		try {
			String sql = "select count(a.topic_name) \n"
					+ " from CBM_C_TOPIC a \n" + "where a.topic_name ='"
					+ entity.getTopicName() + "' \n" + "and a.is_use='Y' \n";
			if (Long.parseLong(bll.getSingal(sql).toString()) > 0) {
				tempString = "主题名称：" + entity.getTopicName() + " 已存在，请重新输入！";
			}
			if (!tempString.equals("")) {
				return tempString;
			}
			String ssql = "select count(a.topic_code) from CBM_C_TOPIC a "
					+ "where a.topic_code ='" + entity.getTopicCode() + "' \n"
					+ "and a.is_use='Y' \n";
			if (Long.parseLong(bll.getSingal(ssql).toString()) > 0) {
				tempString = "主题编码：" + entity.getTopicCode() + " 已存在，请重新输入！";
			}

			if (!tempString.equals("")) {
				return tempString;
			}
			entity.setTopicId(bll.getMaxId("CBM_C_TOPIC", "topic_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		return tempString;
	}

	/**
	 * 
	 */
	public void delete(CbmCTopic entity) {
		LogUtil.log("deleting CbmCTopic instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmCTopic.class, entity
					.getTopicCode());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String ids) {
		String sqlString = "update CBM_C_TOPIC a set a.is_use='N' "
				+ " where a.topic_id in (" + ids + ")";
		bll.exeNativeSQL(sqlString);
	}

	/**
	 * 
	 */
	public String update(CbmCTopic entity) {
		LogUtil.log("updating CbmCTopic instance", Level.INFO, null);
		String tempString = "";
		try {
			String sql = "select count(a.topic_name) \n"
					+ " from CBM_C_TOPIC a \n" + "where a.topic_name ='"
					+ entity.getTopicName() + "' \n" + "and a.is_use='Y' \n"
					+ " and a.topic_code !='" + entity.getTopicCode() + "' \n";
			if (Long.parseLong(bll.getSingal(sql).toString()) > 0) {
				tempString = "主题名称：" + entity.getTopicName() + " 已存在，请重新输入！";
			}
			if (!tempString.equals("")) {
				return tempString;
			}
			String ssql = "select count(a.topic_code) from CBM_C_TOPIC a "
					+ "where a.topic_code ='" + entity.getTopicCode() + "' \n"
					+ "and a.is_use='Y' \n";
			if (Long.parseLong(bll.getSingal(ssql).toString()) > 1) {
				tempString = "主题编码：" + entity.getTopicCode() + " 已存在，请重新输入！";
			}

			if (!tempString.equals("")) {
				return tempString;
			}
			CbmCTopic result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
		return tempString;
	}

	public CbmCTopic findById(String id) {
		LogUtil.log("finding CbmCTopic instance with id: " + id, Level.INFO,
				null);
		try {
			CbmCTopic instance = entityManager.find(CbmCTopic.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject findAll(String enterpriseCode, int... rowStartIdxAndCount) {
		try {
			PageObject pg = new PageObject();
			String sql = "select a.topic_code, \n" + "a.topic_name, \n"
					+ "a.budget_type, \n" + "a.data_type, \n"
					+ "a.time_type, \n" + "a.is_use, \n"
					+ "a.enterprise_code, \n" + "a.topic_id \n"
					+ "from CBM_C_TOPIC a \n" + "where a.enterprise_code ='"
					+ enterpriseCode + "' \n" + "and a.is_use='Y' \n"
					+ "order by a.topic_code";
			String sqlCount = "select count(*)  \n" + "from CBM_C_TOPIC a \n"
					+ "where a.enterprise_code ='" + enterpriseCode + "' \n"
					+ "and a.is_use='Y' \n";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();

			if (list != null && list.size() > 0) {
				while (it.hasNext()) {
					CbmCTopic cbm = new CbmCTopic();
					Object[] data = (Object[]) it.next();
					cbm.setTopicCode(data[0].toString());
					if (data[1] != null)
						cbm.setTopicName(data[1].toString());
					if (data[2] != null)
						cbm.setBudgetType(data[2].toString());
					if (data[3] != null)
						cbm.setDataType(data[3].toString());
					if (data[4] != null)
						cbm.setTimeType(data[4].toString());
					if (data[5] != null)
						cbm.setIsUse(data[5].toString());
					if (data[6] != null)
						cbm.setEnterpriseCode(data[6].toString());
					if (data[7] != null)
						cbm.setTopicId(Long.parseLong(data[7].toString()));

					arrlist.add(cbm);
				}
			}
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);

			return pg;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}

	}

	public String save(List<CbmCTopic> addList, List<CbmCTopic> updateList,
			String ids) {
		String tempString = "";
		if (addList.size() > 0) {
			for (CbmCTopic entity : addList) {
				tempString = this.save(entity);
				if (!tempString.equals("")) {
					return tempString;
				}
				entityManager.flush();
			}
		}

		if (updateList.size() > 0) {
			for (CbmCTopic entity : updateList) {
				tempString = this.update(entity);
				if (!tempString.equals("")) {
					return tempString;
				}
			}
		}

		if (ids.length() > 0) {
			this.delete(ids);
		}
		return tempString;
	}

}