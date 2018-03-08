package power.ejb.manage.stat;

import java.util.ArrayList;
import java.util.Date;
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
 * 倍率管理实现
 */
@Stateless
public class BpCMetricTableFacade implements BpCMetricTableFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * 保存或修改数据
	 */
	public void saveOrUpdate(BpCMetricTable entity) {
		BpCMetricTable model = this.findById(entity.getId());
		if (model == null) {
			this.save(entity);
		} else {
			this.update(entity);
		}
	}

	public void save(BpCMetricTable entity) {
		try {
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<BpCMetricTable> addList) {
		if (addList != null && addList.size() > 0) {

			for (BpCMetricTable entity : addList) {

				this.save(entity);
			}
		}
	}

	public void delete(BpCMetricTable entity) {
		LogUtil.log("deleting BpCMetricTable instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpCMetricTable.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean deleteMetric(String itemCode, String enterpriseCode) {
		try {
			String sql = "delete from bp_c_metric_table t"
					+ " where t.item_code='" + itemCode + "'"
					+ " and t.enterprise_code='" + enterpriseCode + "'";
			dll.exeNativeSQL(sql);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean delete(String ids) {
		try {

			String[] temp1 = ids.split(";");

			for (String i : temp1) {
				String[] temp2 = i.split(",");
				BpCMetricTableId id = new BpCMetricTableId();
				id.setItemCode(temp2[0]);
				id.setTableCode(temp2[1]);
				this.delete(this.findById(id));
			}

			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public BpCMetricTable update(BpCMetricTable entity) {
		try {
			BpCMetricTable result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void update(List<BpCMetricTableAdd> updateList) {

		try {
			for (BpCMetricTableAdd data : updateList) {
				String itemCode = data.getBaseInfo().getId().getItemCode();
				String tableCode = data.getBaseInfo().getId().getTableCode();

				String itemCodeAdd = data.getItemCodeAdd();
				String tableCodeAdd = data.getTableCodeAdd();

				Double maxTableValue = data.getBaseInfo().getMaxTableValue();
				Double multiple = data.getBaseInfo().getMultiple();
				Double startValue = data.getBaseInfo().getStartValue();
				Double endValue = data.getBaseInfo().getEndValue();
				Date fixDate = data.getBaseInfo().getFixDate();
				Date endDate = data.getBaseInfo().getEndDate();
				String ifUsed = data.getBaseInfo().getIfUsed();

				String sql = "update bp_c_metric_table t\n"
						+ "set t.item_code='"
						+ itemCode
						+ "',"

						+ " t.table_code="
						+ tableCode
						+ ","
						+ " t.max_table_value="
						+ maxTableValue
						+ ","
						+ "t.MULTIPLE="
						+ multiple
						+ ","
						+ " t.START_VALUE="
						+ startValue
						+ ","
						+ " t.END_VALUE="
						+ endValue
						+ ","
						+ " t.FIX_DATE='"
						+ fixDate
						+ "',"
						+ " t.END_DATE='"
						+ endDate
						+ "',"
						+ " t.IF_USED='"
						+ ifUsed
						+ "'\n"
						+ "where t.table_code='"
						+ tableCodeAdd
						+ "'\n"
						+ "and t.item_code='" + itemCodeAdd + "'\n";

				dll.exeNativeSQL(sql);

			}
		} catch (RuntimeException e) {
			throw e;
		}

	}

	public BpCMetricTable findById(BpCMetricTableId id) {
		LogUtil.log("finding BpCMetricTable instance with id: " + id,
				Level.INFO, null);
		try {
			BpCMetricTable instance = entityManager.find(BpCMetricTable.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<BpCMetricTable> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpCMetricTable instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpCMetricTable model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<BpCStatItem> getItemListToUse(String enterpriseCode) {
		String sql = "select t.* from bp_c_stat_item t\n" + "where"
				+ " t.ITEM_TYPE='2' \n" + "and t.DATA_COLLECT_WAY !='3'\n"
				+ "and " + " t.is_item='Y'\n"

				+ "and t.enterprise_code='" + enterpriseCode + "'\n"
				+ "ORDER BY    t.item_code";

		return dll.queryByNativeSQL(sql, BpCStatItem.class);
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.*  \n" + "  	FROM BP_C_METRIC_TABLE t\n"
					+ "     where  \n" + "       t.enterprise_code = '"
					+ enterpriseCode + "'\n" + " ORDER BY    t.item_code";

			String sqlCount = "SELECT count(*)\n"
					+ "  	 FROM BP_C_METRIC_TABLE t\n" + "     where \n"
					+ "     t.enterprise_code = '" + enterpriseCode + "'";

			List<BpCMetricTable> list = dll.queryByNativeSQL(sql,
					BpCMetricTable.class, rowStartIdxAndCount);

			List arrlist = new ArrayList();

			for (BpCMetricTable baseInfo : list) {

				BpCMetricTableAdd model = new BpCMetricTableAdd();
				model.setBaseInfo(baseInfo);
				model.setItemCodeAdd(baseInfo.getId().getItemCode());
				model.setTableCodeAdd(baseInfo.getId().getTableCode());

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

	public Long checkItemCode(String itemCode) {
		String sql = "select count(1) from bp_c_metric_table t"
				+ " where t.item_code='" + itemCode + "'";
		Long count = Long.valueOf(dll.getSingal(sql).toString());
		return count;
	}

}