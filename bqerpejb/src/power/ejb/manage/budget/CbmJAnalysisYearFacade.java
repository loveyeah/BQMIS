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
import power.ejb.manage.budget.form.YearAnalysisForm;

/**
 * Facade for entity CbmJAnalysisYear.
 * 
 * @see power.ejb.manage.budget.CbmJAnalysisYear
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmJAnalysisYearFacade implements CbmJAnalysisYearFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(CbmJAnalysisYear entity) {
		LogUtil.log("saving CbmJAnalysisYear instance", Level.INFO, null);
		try {
			entity.setAnalysisYearId(bll.getMaxId("CBM_J_ANALYSIS_YEAR",
					"ANALYSIS_YEAR_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(CbmJAnalysisYear entity) {
		LogUtil.log("deleting CbmJAnalysisYear instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmJAnalysisYear.class, entity
					.getAnalysisYearId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String ids) {
		String sql = "update CBM_J_ANALYSIS_YEAR a set a.is_use='N' \n"
				+ "where a.analysis_year_id in (" + ids + ") ";
		bll.exeNativeSQL(sql);
	}

	public CbmJAnalysisYear update(CbmJAnalysisYear entity) {
		LogUtil.log("updating CbmJAnalysisYear instance", Level.INFO, null);
		try {
			CbmJAnalysisYear result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmJAnalysisYear findById(Long id) {
		LogUtil.log("finding CbmJAnalysisYear instance with id: " + id,
				Level.INFO, null);
		try {
			CbmJAnalysisYear instance = entityManager.find(
					CbmJAnalysisYear.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAnalysisYearList(Long centerId, String dataTime,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding all CbmJAnalysisYear instances", Level.INFO, null);
		try {
			PageObject pg = new PageObject();
			String sql = "";
			String sqlCount = "";
			List list;
			List arrlist = new ArrayList();
			sql = "select a.analysis_year_id, \n"
					+ "a.item_id, \n"
					+ "a.center_id, \n"
					+ "a.data_time, \n"
					+ "a.total_fact, \n"
					+ "a.year_budget, \n"
					+ "a.percent_value, \n"
					+ "a.item_content, \n"
					+ "a.item_explain, \n"
					+ "a.is_use, \n"
					+ "a.enterprise_code, \n"
					+ "c.finance_name, \n"
					+ "d.item_alias \n"
					+ "from CBM_J_ANALYSIS_YEAR a,CBM_C_ITEM_FININCE_ITEM b,CBM_C_FINANCE_ITEM c,CBM_C_MASTER_ITEM d \n"
					+ "where a.item_id=b.item_id \n"
					+ "and b.finance_id=c.finance_id \n"
					+ "and a.item_id=d.item_id \n"
					+ "and a.center_id=d.center_id \n" + "and a.center_id="
					+ centerId + " \n" + "and a.data_time='" + dataTime
					+ "' \n" + "and a.is_use='Y' \n" + "and b.is_use='Y' \n"
					+ "and c.is_use='Y' \n" + "and d.is_use='Y' \n"
					+ "and a.enterprise_code='" + enterpriseCode + "' \n"
					+ "and b.enterprise_code='" + enterpriseCode + "' \n"
					+ "and c.enterprise_code='" + enterpriseCode + "' \n"
					+ "and d.enterprise_code='" + enterpriseCode + "' \n";
			sqlCount = "select count(*) from (" + sql + ")";
			list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					YearAnalysisForm form = new YearAnalysisForm();
					Object[] data = (Object[]) it.next();
					if (data[0] != null)
						form.setAnalysisYearId(Long.parseLong(data[0]
								.toString()));
					if (data[1] != null)
						form.setItemId(Long.parseLong(data[1].toString()));
					if (data[2] != null)
						form.setCenterId(Long.parseLong(data[2].toString()));
					if (data[3] != null)
						form.setDataTime(data[3].toString());
					if (data[4] != null)
						form.setTotalFact(Double
								.parseDouble(data[4].toString()));
					if (data[5] != null)
						form.setYearBudget(Double.parseDouble(data[5]
								.toString()));
					if (data[6] != null)
						form
								.setPercentValue(Math
										.rint(Double.parseDouble(data[6]
												.toString()) * 1000) / 1000);
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
					if (form.getPercentValue() != null) {
						Double dd = (Math.rint(form.getPercentValue() * 100000)) / 1000.0;
						String str = dd.toString() + "%";
						form.setComplatePercent(str);
					}
					arrlist.add(form);
				}
			} else {
				String year = dataTime.substring(0, 4);
				sql = "select a.item_id, \n"
						+ "e.center_id, \n"
						+ "e.budget_time, \n"
						+ "c.finance_name, \n"
						+ "d.item_alias \n"
						+ "from CBM_J_BUDGET_ITEM a,CBM_C_ITEM_FININCE_ITEM b,CBM_C_FINANCE_ITEM c,CBM_C_MASTER_ITEM d,CBM_J_BUDGET_MAKE e \n"
						+ "where a.item_id=b.item_id \n"
						+ "and b.finance_id=c.finance_id \n"
						+ "and a.item_id=d.item_id \n"
						+ "and e.center_id=d.center_id \n"
						+ "and a.budget_make_id=e.budget_make_id \n"
						+ "and e.center_id="
						+ centerId
						+ " \n"
						+ "and e.budget_time='"
						+ dataTime
						+ "' \n"
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
						+ "and e.enterprise_code='" + enterpriseCode + "' \n";
				sqlCount = "select count(*) from (" + sql + ") ";
				list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
				if (list != null && list.size() > 0) {
					Iterator it = list.iterator();
					while (it.hasNext()) {
						YearAnalysisForm form = new YearAnalysisForm();
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
							form.setFinaceName(data[3].toString());
						if (data[4] != null)
							form.setItemAlias(data[4].toString());
						form.setTotalFact(this.countTotalFact(form
								.getCenterId(), form.getItemId(), form
								.getDataTime(), enterpriseCode));
						form.setYearBudget(this.findYearBudgetValue(form
								.getCenterId(), form.getItemId(), year,
								enterpriseCode));
						if (form.getYearBudget() != null
								&& form.getYearBudget() != 0.0) {
							form.setPercentValue(form.getTotalFact()
									/ form.getYearBudget());
						}
						if (form.getPercentValue() != null) {
							Double dd = (Math
									.rint(form.getPercentValue() * 1000)) / 10.0;
							String str = dd.toString() + "%";
							form.setComplatePercent(str);
						} else {
							form.setComplatePercent("0.0%");
						}
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

	/**
	 * 计算当年到当月的累计值
	 */
	public Double countTotalFact(Long centerId, Long itemId, String dataTime,
			String enterpriseCode) {
		String sql = "select sum(a.ensure_budget) \n"
				+ "from CBM_J_BUDGET_ITEM a,CBM_J_BUDGET_MAKE e \n"
				+ "where a.item_id=" + itemId + " \n"
				+ "and a.budget_make_id=e.budget_make_id \n"
				+ "and e.center_id=" + centerId + " \n"
				+ "and e.budget_time >= '" + dataTime.substring(0, 5) + "01"
				+ "' \n" + "and e.budget_time <= '" + dataTime + "' \n"
				+ "and a.enterprise_code='" + enterpriseCode + "' "
				+ "and e.enterprise_code='" + enterpriseCode + "' ";
		Object obj = bll.getSingal(sql);
		if (obj == null)
			return 0.0;
		else
			return Double.parseDouble(obj.toString());
	}

	/**
	 * 获取当年的预算值
	 */
	public Double findYearBudgetValue(Long centerId, Long itemId, String year,
			String enterpriseCode) {
		String sql = "select a.ensure_budget \n"
				+ "from CBM_J_BUDGET_ITEM a,CBM_J_BUDGET_MAKE e  \n"
				+ "where a.item_id=" + itemId + " \n"
				+ "and a.budget_make_id=e.budget_make_id \n"
				+ "and e.center_id=" + centerId + " \n"
				+ "and a.enterprise_code='" + enterpriseCode + "' "
				+ "and e.enterprise_code='" + enterpriseCode + "' "
				+ "and e.budget_time='" + year + "' \n";
		Object obj = bll.getSingal(sql);
		if (obj == null)
			return 0.0;
		else
			return Double.parseDouble(obj.toString());
	}

	public void saveAnalysisYearModified(List<CbmJAnalysisYear> addList,
			List<CbmJAnalysisYear> updateList) {
		if (addList != null && addList.size() > 0) {
			for (CbmJAnalysisYear entity : addList) {
				this.save(entity);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (CbmJAnalysisYear entity : updateList) {
				this.update(entity);
			}
		}
	}
}