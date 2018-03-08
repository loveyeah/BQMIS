package power.ejb.manage.stat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.stat.form.ItemReprotNewForm;

/**
 * Facade for entity BpCItemReportNew.
 * 
 * @see power.ejb.manage.plan.BpCItemReportNew
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCItemReportNewFacade implements BpCItemReportNewFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(BpCItemReportNew entity) {
		try {
			Long id = bll.getMaxId("BP_C_ITEM_REPORT_NEW", "ID");
			entity.setId(id);
			entityManager.persist(entity);
			entityManager.flush();
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<BpCItemReportNew> addList) {
		if (addList != null && addList.size() > 0) {
			for (BpCItemReportNew entity : addList) {
				this.save(entity);
			}
		}
	}

	public Long isNew(String reportCode, String itemCode) {
		Long count;
		String sql = "select count(*) from BP_C_ITEM_REPORT_NEW t "
				+ " where t.report_code='" + reportCode + "'"
				+ " and t.item_code='" + itemCode + "'";
		count = Long.valueOf(bll.getSingal(sql).toString());
		return count;
	}

	public void deleteMuti(String itemCode, Long reportCode) {
		String sql = "delete from BP_C_ITEM_REPORT_NEW a\n"
				+ " where a.report_code = " + reportCode + "\n"
				+ "   and a.id in(" + itemCode + ")";
		bll.exeNativeSQL(sql);
	}

	public BpCItemReportNew update(BpCItemReportNew entity) {
		try {
			BpCItemReportNew result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void update(List<BpCItemReportNew> updateList) {
		if (updateList != null && updateList.size() > 0) {
			for (BpCItemReportNew entity : updateList) {
				this.update(entity);
			}
		}
	}

	public BpCItemReportNew findById(Long id) {
		try {
			BpCItemReportNew instance = entityManager.find(
					BpCItemReportNew.class, id);
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
					+ "  FROM BP_C_ITEM_REPORT_NEW t\n"
					+ " WHERE t.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and t.report_code = '" + reportCode + "'\n"
					+ " ORDER BY t.display_no";
			Long count = Long.parseLong(bll.getSingal(sqlCount).toString());

			// modified by liuyi 20100524 
			String sql = 
//				"SELECT t.*, getitemname(t.item_code)\n"
//					+ "  FROM BP_C_ITEM_REPORT_NEW t\n"
				"SELECT t.report_code,\n" +
				" t.item_code,\n" + 
				" t.data_type,\n" + 
				" t.display_no,\n" + 
				" t.item_alias,\n" + 
				" t.enterprise_code,\n" + 
				" t.item_base_name,\n" + 
				" t.item_second_name,\n" + 
				" t.data_time_type,\n" + 
				" t.id,\n" + 
				" t.conkers_no\n" + 
				" , getitemname(t.item_code)\n" +
				" ,t.is_show_zero \n" + 
				"  FROM BP_C_ITEM_REPORT_NEW t"
					+ " WHERE t.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and t.report_code = '" + reportCode + "'\n"
					+ " ORDER BY to_number(t.item_second_name), t.display_no";
//					+ " ORDER BY  t.display_no";
//			if(reportCode.equals("6") || reportCode.equals("2")){
//				sql +=" ORDER BY t.item_second_name, t.display_no";
//			}else{
//				sql +="ORDER BY  t.display_no";
//			}
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				BpCItemReportNew model = new BpCItemReportNew();
				ItemReprotNewForm info = new ItemReprotNewForm();
				if (data[0] != null)
					model.setReportCode(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setItemCode(data[1].toString());
				if (data[2] != null)
					model.setDataType(data[2].toString());
				if (data[3] != null)
					model.setDisplayNo(Long.parseLong(data[3].toString()));
				if (data[4] != null)
					model.setItemAlias(data[4].toString());
				if (data[5] != null)
					model.setEnterpriseCode(data[5].toString());
				if (data[6] != null)
					model.setItemBaseName(data[6].toString());
				if (data[7] != null)
					model.setItemSecondName(data[7].toString());
				if (data[8] != null)
					model.setDataTimeType(data[8].toString());
				if (data[9] != null)
					model.setId(Long.parseLong(data[9].toString()));
				if(data[10]!=null)
					model.setConkersNo(Long.parseLong(data[10].toString()));
				if(data[11]!=null)
					info.setItemName(data[11].toString());
				if(data[12] != null)
					model.setIsShowZero(data[12].toString());
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