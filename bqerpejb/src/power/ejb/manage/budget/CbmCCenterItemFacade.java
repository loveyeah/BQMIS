package power.ejb.manage.budget;

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
import power.ejb.manage.budget.form.CbmJBudgetItemForm;
import power.ejb.manage.budget.form.DeptTopicItemForm;

/**
 * 预算部门指标维护
 * 
 * @author liuyi 090805
 */
@Stateless
public class CbmCCenterItemFacade implements CbmCCenterItemFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 新增一条预算部门指标维护数据
	 */
	public void save(CbmCCenterItem entity) {
		LogUtil.log("saving CbmCCenterItem instance", Level.INFO, null);
		try {
			entity.setCenterItemId(bll.getMaxId("CBM_C_CENTER_ITEM",
					"CENTER_ITEM_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条预算部门指标维护数据
	 */
	public void delete(CbmCCenterItem entity) {
		LogUtil.log("deleting CbmCCenterItem instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmCCenterItem.class, entity
					.getCenterItemId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条或多条部门指标维护数据
	 */
	public void delete(String ids) {
		String sql = " update CBM_C_CENTER_ITEM a set a.is_use='N' \n"
				+ " where a.center_item_id in (" + ids + ")";
		bll.exeNativeSQL(sql);
	}

	/**
	 * 更新一条预算部门指标维护书记员
	 */
	public CbmCCenterItem update(CbmCCenterItem entity) {
		LogUtil.log("updating CbmCCenterItem instance", Level.INFO, null);
		try {
			CbmCCenterItem result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过id查找一条数据
	 */
	public CbmCCenterItem findById(Long id) {
		LogUtil.log("finding CbmCCenterItem instance with id: " + id,
				Level.INFO, null);
		try {
			CbmCCenterItem instance = entityManager.find(CbmCCenterItem.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CbmCCenterItem> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all CbmCCenterItem instances", Level.INFO, null);
		try {
			final String queryString = "select model from CbmCCenterItem model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject findDeptTopicItemList(String centerTopicId,
			String enterpriseCode, int... rowStartIdxAndCount) {
		try {
			//modify by ypan 20100903
			PageObject pg = new PageObject();
			String sql = "select a.center_item_id, \n"
					+ "a.center_topic_id, \n"
					+ "a.item_id, \n"
					+ "a.item_alias, \n"
					+ "a.data_source, \n"
					+ "a.display_no, \n"
					+ "a.is_use, \n"
					+ "a.enterprise_code, \n"
					+ "b.item_code, \n"
					+ "b.item_name,\n"
					+ "a.master_mode,\n"
					+ "a.data_type\n"
					+ "from CBM_C_CENTER_ITEM a,CBM_C_ITEM b,CBM_C_CENTER_TOPIC c \n"
					+ "where a.center_topic_id = c.center_topic_id \n"
					+ "and a.item_id=b.item_id \n" + "and a.is_use='Y' \n"
					+ "and b.is_use='Y' \n" + "and c.is_use='Y' \n"
					+ "and a.enterprise_code='" + enterpriseCode + "' \n"
					+ "and b.enterprise_code='" + enterpriseCode + "' \n"
					+ "and b.enterprise_code='" + enterpriseCode + "' \n";
			String sqlCount = "select count(*) \n"
					+ "from CBM_C_CENTER_ITEM a,CBM_C_ITEM b,CBM_C_CENTER_TOPIC c \n"
					+ "where a.center_topic_id = c.center_topic_id \n"
					+ "and a.item_id=b.item_id \n" + "and a.is_use='Y' \n"
					+ "and b.is_use='Y' \n" + "and c.is_use='Y' \n"
					+ "and a.enterprise_code='" + enterpriseCode + "' \n"
					+ "and b.enterprise_code='" + enterpriseCode + "' \n"
					+ "and b.enterprise_code='" + enterpriseCode + "' \n";
			if (centerTopicId != null && !(centerTopicId.equals(""))) {
				sql = sql + "and a.center_topic_id='" + centerTopicId + "' \n";
				sqlCount = sqlCount + "and a.center_topic_id='" + centerTopicId
						+ "' \n";
			}
			sql = sql + "order by a.display_no \n";

			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			if (list.size() > 0) {
				while (it.hasNext()) {
					CbmCCenterItem ccc = new CbmCCenterItem();
					DeptTopicItemForm form = new DeptTopicItemForm();
					Object[] data = (Object[]) it.next();
					ccc.setCenterItemId(Long.parseLong(data[0].toString()));
					if (data[1] != null)
						ccc
								.setCenterTopicId(Long.parseLong(data[1]
										.toString()));
					if (data[2] != null)
						ccc.setItemId(Long.parseLong(data[2].toString()));
					if (data[3] != null)
						ccc.setItemAlias(data[3].toString());
					if (data[4] != null)
						ccc.setDataSource(data[4].toString());
					if (data[5] != null)
						ccc.setDisplayNo(Long.parseLong(data[5].toString()));
					if (data[6] != null)
						ccc.setIsUse(data[6].toString());
					if (data[7] != null)
						ccc.setEnterpriseCode(data[7].toString());
					if (data[8] != null)
						form.setItemCode(data[8].toString());
					if (data[9] != null)
						form.setItemName(data[9].toString());
					if (data[10] != null)
						ccc.setMasterMode(data[10].toString());
					if (data[11] != null)
						ccc.setDataType(data[11].toString());
					form.setCcc(ccc);
					arrlist.add(form);
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

	public void saveDeptTopicItem(List<CbmCCenterItem> addList,
			List<CbmCCenterItem> updateList, String ids) {
		if (addList.size() > 0) {
			for (CbmCCenterItem entity : addList) {
				this.save(entity);
				entityManager.flush();
			}
		}
		if (updateList.size() > 0) {
			for (CbmCCenterItem entity : updateList) {
				this.update(entity);
			}
		}
		if (ids.length() > 0) {
			this.delete(ids);
		}
	}

	/**
	 * 判断新增或修改的指标在该部门下是否已存在
	 */
	public boolean judgeIsExist(Long centerId, String itemIds, String dciIds,
			String enterpriseCode) {
		String[] itemArray = itemIds.split(",");
		String[] dciArray = dciIds.split(",");
		String sql = "";
		int num = 0;
		for (int i = 0; i <= itemArray.length - 1; i++) {
			if (dciArray[i].equals("null")) {
				sql = "select count(*) \n"
						+ "from CBM_C_CENTER_ITEM a,CBM_C_CENTER_TOPIC b \n"
						+ "where a.center_topic_id=b.center_topic_id \n"
						+ "and a.is_use='Y' \n" + "and b.is_use='Y' \n"
						+ "and a.enterprise_code='" + enterpriseCode + "' \n"
						+ "and b.enterprise_code='" + enterpriseCode + "' \n"
//						+ "and b.center_id=" + centerId + " \n"
						+ "and a.item_id=" + itemArray[i] + " \n";
			} else {
				sql = "select count(*) \n"
						+ "from CBM_C_CENTER_ITEM a,CBM_C_CENTER_TOPIC b \n"
						+ "where a.center_topic_id=b.center_topic_id \n"
						+ "and a.is_use='Y' \n" + "and b.is_use='Y' \n"
						+ "and a.enterprise_code='" + enterpriseCode + "' \n"
						+ "and b.enterprise_code='" + enterpriseCode + "' \n"
//						+ "and b.center_id=" + centerId + " \n"
						+ "and a.item_id=" + itemArray[i] + " \n"
						+ "and a.center_item_id!=" + dciArray[i] + " \n";
			}
			num = Integer.parseInt(bll.getSingal(sql).toString());
			if (num > 0)
				return true;
		}
		return false;
	}

	public List<CbmJBudgetItemForm> findItemList(String centerId,
			String topicId, String enterpriseCode) {
		List<CbmJBudgetItemForm> arraylist = new ArrayList<CbmJBudgetItemForm>();
		String sql = "select distinct t.item_id,c.item_name, \n"
				+ "       getunitname((select r.unit_code from cbm_c_item r where t.item_id = r.item_id)), \n"
				+ "d.center_id,d.topic_id,  \n"
				+ "t.DISPLAY_NO  ,\n"
				+ "e.zbbmtx_code	\n"
				+ "  from CBM_C_CENTER_ITEM t, cbm_c_item c,CBM_C_CENTER_TOPIC d ,cbm_c_itemtx e\n"
				+ " where t.item_id=c.item_id\n" + "   and d.center_id = "
				+ centerId + "\n"
				+ " and t.center_topic_id=d.center_topic_id \n"
				+ "   and t.enterprise_code = c.enterprise_code\n"
				+ "   and t.enterprise_code = '" + enterpriseCode + "'"
				+ "   and d.enterprise_code = '" + enterpriseCode + "'"
				+ "   and t.is_use='Y'" + "   and c.is_use='Y'"
				+ "   and d.is_use='Y'" + "and e.item_id = t.item_id\n"
				+ "  and e.is_use = 'Y'\n" + "  and e.enterprise_code = '"
				+ enterpriseCode + "'";

		if (topicId != null && !(topicId.equals("")))
			sql = sql + " and d.topic_id =" + topicId + " \n";
		// sql = sql + " order by t.DISPLAY_NO \n";
		sql = sql + " order by e.zbbmtx_code \n";
		List list = bll.queryByNativeSQL(sql);
		Iterator i = list.iterator();
		while (i.hasNext()) {
			Object[] o = (Object[]) i.next();
			CbmJBudgetItemForm model = new CbmJBudgetItemForm();
			if (o[0] != null) {
				model.setItemId(Long.parseLong(o[0].toString()));
			}
			if (o[1] != null) {
				model.setItemAlias(o[1].toString());
			}
			if (o[2] != null) {
				model.setUnitName(o[2].toString());
			}
			if (o[3] != null) {
				model.setCenterId(Long.parseLong(o[3].toString()));
			}
			if (o[4] != null) {
				model.setTopicId(Long.parseLong(o[4].toString()));
			}
			if (o[6] != null) {
				model.setItemCode(o[6].toString());
			}
			arraylist.add(model);
		}

		return arraylist;
	}
}