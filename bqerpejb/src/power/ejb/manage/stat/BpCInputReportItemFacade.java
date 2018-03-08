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
import power.ejb.manage.stat.form.InputReprotItemForm;

/**
 * @author drdu
 */
@Stateless
public class BpCInputReportItemFacade implements BpCInputReportItemFacadeRemote {
	// property constants
	public static final String DATA_TYPE = "dataType";
	public static final String DISPLAY_NO = "displayNo";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(BpCInputReportItem entity) {
		LogUtil.log("saving BpCInputReportItem instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<BpCInputReportItem> addList) {
		if (addList != null && addList.size() > 0) {
			for (BpCInputReportItem entity : addList) {
				this.save(entity);
			}
		}
	}

	public Long isNew(String reportCode, String itemCode) {
		Long count;
		String sql = "select count(*) from BP_C_INPUT_REPORT_ITEM t "
				+ " where t.report_code='" + reportCode + "'"
				+ " and t.item_code='" + itemCode + "'";
		count = Long.valueOf(bll.getSingal(sql).toString());
		return count;
	}

	public void deleteMuti(String itemCode, Long reportCode) {
		String sql = "delete from BP_C_INPUT_REPORT_ITEM a\n"
				+ " where a.report_code = " + reportCode + "\n"
				+ "   and a.item_code in(" + itemCode + ")";
		bll.exeNativeSQL(sql);
	}

	public BpCInputReportItem update(BpCInputReportItem entity) {
		LogUtil.log("updating BpCInputReportItem instance", Level.INFO, null);
		try {
			BpCInputReportItem result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void update(List<BpCInputReportItem> updateList) {
		if (updateList != null && updateList.size() > 0) {
			for (BpCInputReportItem entity : updateList) {
				this.update(entity);
			}
		}
	}

	public BpCInputReportItem findById(BpCInputReportItemId id) {
		LogUtil.log("finding BpCInputReportItem instance with id: " + id,
				Level.INFO, null);
		try {
			BpCInputReportItem instance = entityManager.find(
					BpCInputReportItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String reportCode, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();

			String sqlCount = "select count(1)\n"
					+ "  FROM BP_C_INPUT_REPORT_ITEM t, bp_c_stat_item s\n"
					+ " WHERE t.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and t.report_code = '" + reportCode + "'\n"
					+ "   and t.item_code = s.item_code\n"
					+ " ORDER BY t.display_no";
			Long count = Long.parseLong(bll.getSingal(sqlCount).toString());

			String sql = "SELECT t.*, s.item_name\n"
					+ "  FROM BP_C_INPUT_REPORT_ITEM t, bp_c_stat_item s\n"
					+ " WHERE t.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and t.report_code = '" + reportCode + "'\n"
					+ "   and t.item_code = s.item_code\n"
					+ " ORDER BY t.display_no";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();

			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				BpCInputReportItem model = new BpCInputReportItem();
				BpCInputReportItemId id = new BpCInputReportItemId();
				InputReprotItemForm info = new InputReprotItemForm();
				if (data[0] != null)
					id.setReportCode(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					id.setItemCode(data[1].toString());
				if (data[2] != null)
					model.setDataType(data[2].toString());
				if (data[3] != null)
					model.setDisplayNo(Long.parseLong(data[3].toString()));
				if (data[4] != null)
					model.setEnterpriseCode(data[4].toString());
				if (data[5] != null)
					model.setItemAlias(data[5].toString());
				if (data[6] != null)
					model.setItemBaseName(data[6].toString());
				if (data[7] != null)
					info.setItemName(data[7].toString());
				model.setId(id);
				info.setModel(model);
				arrlist.add(info);
			}
			result.setList(arrlist);
			result.setTotalCount(count);
			return result;

		} catch (RuntimeException re) {
			throw re;
		}
	}

}