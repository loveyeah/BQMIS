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
import power.ejb.manage.budget.form.MonthAnalysisForm;

/**
 * 部门月度预算分析
 * 
 * @author liuyi 090813
 */
@Stateless
public class CbmJAnalysisMonthFacade implements CbmJAnalysisMonthFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(CbmJAnalysisMonth entity) {
		LogUtil.log("saving CbmJAnalysisMonth instance", Level.INFO, null);
		try {
			entity.setAnalysisMonthId(bll.getMaxId("CBM_J_ANALYSIS_MONTH",
					"ANALYSIS_MONTH_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(CbmJAnalysisMonth entity) {
		LogUtil.log("deleting CbmJAnalysisMonth instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmJAnalysisMonth.class, entity
					.getAnalysisMonthId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String ids) {
		String sql = "update CBM_J_ANALYSIS_MONTH a set a.is_use='N' \n"
				+ " where a.analysis_month_id in (" + ids + ") ";
		bll.exeNativeSQL(sql);
	}

	public CbmJAnalysisMonth update(CbmJAnalysisMonth entity) {
		LogUtil.log("updating CbmJAnalysisMonth instance", Level.INFO, null);
		try {
			CbmJAnalysisMonth result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmJAnalysisMonth findById(Long id) {
		LogUtil.log("finding CbmJAnalysisMonth instance with id: " + id,
				Level.INFO, null);
		try {
			CbmJAnalysisMonth instance = entityManager.find(
					CbmJAnalysisMonth.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all CbmJAnalysisMonth entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CbmJAnalysisMonth> all CbmJAnalysisMonth entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAnalysisMonthList(Long centerId, String dataTime,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		LogUtil
				.log("finding all CbmJAnalysisMonth instances", Level.INFO,
						null);
		try {
			PageObject pg = new PageObject();
			String sql = "";
			String sqlCount = "";
			List list;
			List arrlist = new ArrayList();
			sql = "select a.analysis_month_id, \n"
					+ "a.item_id, \n"
					+ "a.center_id, \n"
					+ "a.data_time, \n"
					+ "a.budget_value, \n"
					+ "a.fact_value, \n"
					+ "a.add_reduce, \n"
					+ "a.item_content, \n"
					+ "a.item_explain, \n"
					+ "a.is_use, \n"
					+ "a.enterprise_code, \n"
					+ "c.finance_name, \n"
					+ "d.item_alias \n"
					// add by ltong 分层显示 排序
					+ ", f.zbbmtx_code \n"
					+ "from CBM_J_ANALYSIS_MONTH a,CBM_C_ITEM_FININCE_ITEM b,CBM_C_FINANCE_ITEM c,CBM_C_MASTER_ITEM d ,cbm_c_itemtx f\n"
					+ "where a.item_id=b.item_id \n"
					+ "and b.finance_id=c.finance_id \n"
					+ "and a.item_id=d.item_id \n"
					+ "and a.center_id=d.center_id \n" + "and a.center_id="
					+ centerId
					+ " \n"
					+ "and a.data_time='"
					+ dataTime
					+ "' \n"
					+ "and a.is_use='Y' \n"
					+ "and b.is_use='Y' \n"
					+ "and c.is_use='Y' \n"
					+ "and d.is_use='Y' \n"
					+ "and a.enterprise_code='"
					+ enterpriseCode
					+ "' \n"
					+ "and b.enterprise_code='"
					+ enterpriseCode
					+ "' \n"
					+ "and c.enterprise_code='"
					+ enterpriseCode
					+ "' \n"
					+ "and d.enterprise_code='"
					+ enterpriseCode
					+ "' \n"
					+ "and f.item_id=a.item_id\n"
					+ "   and f.is_use='Y'\n"
					+ "   and f.enterprise_code='" + enterpriseCode + "'";
			sql = sql + " order by f.zbbmtx_code \n";

			sqlCount = "select count(*) from (" + sql + ")";
			list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					MonthAnalysisForm form = new MonthAnalysisForm();
					Object[] data = (Object[]) it.next();
					if (data[0] != null)
						form.setAnalysisMonthId(Long.parseLong(data[0]
								.toString()));
					if (data[1] != null)
						form.setItemId(Long.parseLong(data[1].toString()));
					if (data[2] != null)
						form.setCenterId(Long.parseLong(data[2].toString()));
					if (data[3] != null)
						form.setDataTime(data[3].toString());
					if (data[4] != null)
						form.setBudgetValue(Double.parseDouble(data[4]
								.toString()));
					if (data[5] != null)
						form.setFactValue(Double
								.parseDouble(data[5].toString()));
					if (data[6] != null)
						form.setAddReduce(Double
								.parseDouble(data[6].toString()));
					if (data[7] != null)
						form.setItemContent(data[7].toString());
					if (data[8] != null)
						form.setItemExplain(data[8].toString());
					if (data[9] != null)
						form.setIsUse(data[9].toString());
					if (data[10] != null)
						form.setEnterpriseCode(data[10].toString());
					if (data[11] != null)
						form.setFinaceName(data[11].toString());
					if (data[12] != null)
						form.setItemAlias(data[12].toString());
					if (data[13] != null) {
						form.setZbbmtxCode(data[13].toString());
					}
					arrlist.add(form);
				}
			} else {
				sql = "select a.item_id, \n"
						+ "e.center_id, \n"
						+ "e.budget_time, \n"
						+ "a.ensure_budget, \n"
						+ "a.fact_happen, \n"
						+ "c.finance_name, \n"
						+ "d.item_alias \n"
						// add by ltong 分层显示 排序
						+ ", f.zbbmtx_code \n"
						+ "from CBM_J_BUDGET_ITEM a,CBM_C_ITEM_FININCE_ITEM b,CBM_C_FINANCE_ITEM c,CBM_C_MASTER_ITEM d,CBM_J_BUDGET_MAKE e ,cbm_c_itemtx f\n"
						+ "where a.item_id=b.item_id \n"
						+ "and b.finance_id=c.finance_id \n"
						+ "and a.item_id=d.item_id \n"
						+ "and e.center_id=d.center_id \n" + "and e.center_id="
						+ centerId
						+ " \n"
						+ "and e.budget_time='"
						+ dataTime
						+ "' \n"
						+ "and a.budget_make_id=e.budget_make_id \n"
						+ "and b.is_use='Y' \n"
						+ "and c.is_use='Y' \n"
						+ "and d.is_use='Y' \n"
						+ "and a.enterprise_code='"
						+ enterpriseCode
						+ "' \n"
						+ "and b.enterprise_code='"
						+ enterpriseCode
						+ "' \n"
						+ "and c.enterprise_code='"
						+ enterpriseCode
						+ "' \n"
						+ "and d.enterprise_code='"
						+ enterpriseCode
						+ "' \n"
						+ "and f.item_id=a.item_id\n"
						+ "   and f.is_use='Y'\n"
						+ "   and f.enterprise_code='" + enterpriseCode + "'";

				sql = sql + " order by f.zbbmtx_code \n";

				sqlCount = "select count(*) from (" + sql + ")";
				list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
				if (list != null && list.size() > 0) {
					Iterator it = list.iterator();
					while (it.hasNext()) {
						MonthAnalysisForm form = new MonthAnalysisForm();
						Object[] data = (Object[]) it.next();
						if (data[0] != null)
							form.setItemId(Long.parseLong(data[0].toString()));
						if (data[1] != null)
							form
									.setCenterId(Long.parseLong(data[1]
											.toString()));
						if (data[2] != null)
							form.setDataTime(data[2].toString());
						if (data[3] != null)
							form.setBudgetValue(Double.parseDouble(data[3]
									.toString()));
						if (data[4] != null)
							form.setFactValue(Double.parseDouble(data[4]
									.toString()));
						if (form.getBudgetValue() == null)
							form.setBudgetValue(0.0);
						if (form.getFactValue() == null)
							form.setFactValue(0.0);
						form.setAddReduce(form.getFactValue()
								- form.getBudgetValue());
						if (data[5] != null)
							form.setFinaceName(data[5].toString());
						if (data[6] != null)
							form.setItemAlias(data[6].toString());
						if (data[7] != null) {
							form.setZbbmtxCode(data[7].toString());
						}
						form.setIsUse("Y");
						form.setEnterpriseCode(enterpriseCode);
						arrlist.add(form);
					}
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

	public void saveAnalysisMonthModified(List<CbmJAnalysisMonth> addList,
			List<CbmJAnalysisMonth> updateList) {
		if (addList != null && addList.size() > 0) {
			for (CbmJAnalysisMonth entity : addList) {
				this.save(entity);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (CbmJAnalysisMonth entity : updateList) {
				this.update(entity);
			}
		}
	}

}