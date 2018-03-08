package power.ejb.manage.stat;

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
import power.ejb.manage.stat.form.StatReportItemForm;

/**
 * Facade for entity BpCStatReportItem.
 * 
 * @see power.ejb.manage.stat.BpCStatReportItem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCStatReportItemFacade implements BpCStatReportItemFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(BpCStatReportItem entity) {
		LogUtil.log("saving BpCStatReportItem instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<BpCStatReportItem> addList,
			List<BpCStatReportItem> updateList, String itemCode, Long reportCode) {
		if (addList != null && addList.size() > 0) {
			for (BpCStatReportItem entity : addList) {
				this.save(entity);
			}
		}
		update(updateList);
		deleteMuti(itemCode, reportCode);
	}

	public Long isNew(String reportCode, String itemCode) {
		Long count;
		String sql = "select count(*) from BP_C_STAT_REPORT_ITEM t "
				+ " where t.report_code='" + reportCode + "'"
				+ " and t.item_code='" + itemCode + "'";
		count = Long.valueOf(bll.getSingal(sql).toString());
		return count;
	}

	public void deleteMuti(String itemCode, Long reportCode) {
		if (itemCode.length() > 0) {
			String sql = "delete from BP_C_STAT_REPORT_ITEM a\n"
					+ " where a.report_code = " + reportCode + "\n"
					+ "   and a.item_code in(" + itemCode + ")";
			bll.exeNativeSQL(sql);
		}
	}

	public BpCStatReportItem update(BpCStatReportItem entity) {
		LogUtil.log("updating BpCStatReportItem instance", Level.INFO, null);
		try {
			BpCStatReportItem result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void update(List<BpCStatReportItem> updateList) {
		if (updateList != null && updateList.size() > 0) {
			for (BpCStatReportItem entity : updateList) {
				this.update(entity);
			}
		}
	}

	public BpCStatReportItem findById(BpCStatReportItemId id) {
		LogUtil.log("finding BpCStatReportItem instance with id: " + id,
				Level.INFO, null);
		try {
			BpCStatReportItem instance = entityManager.find(
					BpCStatReportItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String reportCode,
			final int... rowStartIdxAndCount) {
		LogUtil
				.log("finding all BpCStatReportItem instances", Level.INFO,
						null);
		try {
			PageObject pg = new PageObject();
			String sqlCount = "select count(1)\n"
					+ "  FROM BP_C_STAT_REPORT_ITEM t, bp_c_stat_item s\n"
					+ " WHERE t.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and t.report_code = " + reportCode + "\n"
					+ "   and t.item_code = s.item_code\n"
					+ " ORDER BY t.display_no";
			Long count = Long.parseLong(bll.getSingal(sqlCount).toString());

			String sql = "SELECT t.*, s.item_name\n"
					+ "  FROM BP_C_STAT_REPORT_ITEM t, bp_c_stat_item s\n"
					+ " WHERE t.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and t.report_code = " + reportCode + "\n"
					+ "   and t.item_code = s.item_code\n"
					+ " ORDER BY t.display_no";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				BpCStatReportItem model = new BpCStatReportItem();
				BpCStatReportItemId id = new BpCStatReportItemId();
				StatReportItemForm info = new StatReportItemForm();
				if (data[0] != null)
					id.setReportCode(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					id.setItemCode(data[1].toString());
				if (data[2] != null)
					model.setDisplayNo(Long.parseLong(data[2].toString()));
				if (data[4] != null)
					info.setItemName(data[4].toString());
				model.setId(id);
				info.setModel(model);
				arrlist.add(info);
			}
			pg.setList(arrlist);
			pg.setTotalCount(count);
			return pg;

		} catch (RuntimeException re) {
			throw re;
		}
	}

}