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

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;
import power.ejb.hr.LogUtil;
import power.ejb.manage.budget.form.CbmCReportItemAdd;
import power.ejb.manage.budget.form.ReportItemForm;

/**
 * Facade for entity CbmCReportItem.
 * 
 * @see power.ejb.manage.budget.CbmCReportItem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCReportItemFacade implements CbmCReportItemFacadeRemote {
	// property constants
	public static final String IS_ITEM = "isItem";
	public static final String REPORT_TYPE = "reportType";
	public static final String ITEM_ID = "itemId";
	public static final String ITEM_NAME = "itemName";
	public static final String UNIT_ID = "unitId";
	public static final String DATA_TYPE = "dataType";
	public static final String DISPLAY_NO = "displayNo";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(CbmCReportItem entity) throws CodeRepeatException{
		LogUtil.log("saving CbmCReportItem instance", Level.INFO, null);
		try {
			if("Y".equals(entity.getIsItem())) {
				if(this.IsExist(entity.getItemId(), entity.getReportType()) == 0) {
					entity.setReportItemId(bll.getMaxId("CBM_C_REPORT_ITEM", "REPORT_ITEM_ID"));
					entity.setIsUse("Y");
					entityManager.persist(entity);
					LogUtil.log("save successful", Level.INFO, null);
				} else {
					throw new CodeRepeatException("同一报表节点下，不能重复保存！");
				}
			} else {
				entity.setReportItemId(bll.getMaxId("CBM_C_REPORT_ITEM", "REPORT_ITEM_ID"));
				entity.setIsUse("Y");
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(CbmCReportItem entity) {
		LogUtil.log("deleting CbmCReportItem instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmCReportItem.class, entity
					.getReportItemId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmCReportItem update(CbmCReportItem entity) {
		LogUtil.log("updating CbmCReportItem instance", Level.INFO, null);
		try {
			CbmCReportItem result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmCReportItem findById(Long id) {
		LogUtil.log("finding CbmCReportItem instance with id: " + id,
				Level.INFO, null);
		try {
			CbmCReportItem instance = entityManager.find(CbmCReportItem.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CbmCReportItem> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding CbmCReportItem instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from CbmCReportItem model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String argFuzzy) {
		try {
			PageObject pg = new PageObject();
			final String queryString = "select * from cbm_c_model a\n"
					+ "where a.model_item_code like '" + argFuzzy + "%'\n"
					+ " or a.model_item_name like '" + argFuzzy + "%'"
					+ " and is_item ='Y'";
			String sqlcount = "select count(1) from cbm_c_model a\n"
					+ "where a.model_item_code like '" + argFuzzy + "%'\n"
					+ " or a.model_item_name like '" + argFuzzy + "%'"
					+ " and is_item ='Y'";

			List<CbmCModel> query = bll.queryByNativeSQL(queryString,
					CbmCModel.class);
			Long count = null;
			if (bll.getSingal(sqlcount) != null)
				count = Long.parseLong(bll.getSingal(sqlcount).toString());
			pg.setList(query);
			pg.setTotalCount(count);
			return pg;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<TreeNode> findReportItemTreeList(String node, String enterpriseCode) {
		List<TreeNode> res = null;
		try {
//			String sql = "select t.MODEL_ITEM_ID,\n"
//			+ "       t.MODEL_ITEM_NAME,\n" + "       t.MODEL_TYPE,\n"
//			+ "       t.is_item,\n" + "       connect_by_isleaf,\n"
//			+ "       t.DISPLAY_NO \n" + "  from CBM_C_MODEL t\n"
//			+ "where level = 1\n" + " start with t.DADDY_ITEM_ID = ?\n"
//			+ "connect by prior t.MODEL_ITEM_ID = t.DADDY_ITEM_ID\n"
//			+ " order by t.DISPLAY_NO";
			String sql = "select t.report_item_id,\n" +
					"       t.item_name,\n" + 
					"       t.report_type,\n" +  
					"       t.is_item,\n" + 
					"       connect_by_isleaf,\n" + 
					"       t.DISPLAY_NO\n" + 
					"  from CBM_C_REPORT_ITEM t\n" + 
					"where level = 1\n" + 
					"and t.is_use = 'Y'\n" +
					" start with t.parent_id = ?\n" + 
					"connect by prior t.report_item_id = t.parent_id\n" + 
					" order by t.DISPLAY_NO";

			List<Object[]> list = bll.queryByNativeSQL(sql,
					new Object[] { node });
			if (list != null && list.size() > 0) {
				res = new ArrayList<TreeNode>();
				for (Object[] o : list) {
					TreeNode n = new TreeNode();
					n.setId(o[0].toString());
					if (o[1] != null)
						n.setText(o[1].toString());
					if (o[2] != null)
						n.setCode(o[2].toString());
					String isItem = "N";
					if (o[3] != null)
						isItem = o[3].toString();
					n.setDescription(isItem);

					if (o[4] != null)
						n.setLeaf(o[4].toString().equals("1") ? true : false);
					String icon = "";
					if (isItem.equals("N")) {
						icon = "box";
					} else {
						if (("Y").equals(isItem))
							icon = n.getLeaf() ? "my-iconCls" : "my-iconCls";
						else
							icon = n.getLeaf() ? "file" : "folder";
					}
					n.setIconCls(icon);
					if (o[5] != null)
						n.setOpenType(o[5].toString());
					res.add(n);
				}

			}
			return res;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unused")
	private Long IsExist(Long itemId,Long reprotType) {
		String sql ="select count(*) from cbm_c_report_item t where t.is_item='Y' and t.item_id= '"+itemId+"' and t.report_type = '"+reprotType+"'";
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		return count;
	}
	
	@SuppressWarnings("unchecked")
	public List<ReportItemForm> getReportItemListByYear(String dateTime,String reportType) {
		String sql = "";
		if(dateTime.length() == 4) {
			sql = "select t.item_name,\n" +
				"       getunitname(t.unit_id),\n" + 
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = to_char(to_number('"+dateTime+"') - 1)) as lastENSUREBUDGET,\n" + 
				"       (select a.FACT_HAPPEN\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = to_char(to_number('"+dateTime+"') - 1)) as lastFACT_HAPPEN,\n" + 
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = '"+dateTime+"') as ENSURE_BUDGET,\n" + 
				"       (select a.BUDGET_BASIS\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = '"+dateTime+"') as BUDGET_BASIS\n" + 
				"  from CBM_C_REPORT_ITEM t\n" + 
				" where t.is_use = 'Y'\n" + 
				"   and t.enterprise_code = 'hfdc'\n" + 
				"   and t.is_item = 'Y'  and t.report_type ='"+reportType+"' order by t.display_no";
		} else {
			sql = 
				"select t.item_name,\n" +
				"       getunitname(t.unit_id),\n" + 
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = to_char(to_number(substr('"+dateTime+"', 0, 4)) - 1) ||\n" + 
				"               substr('"+dateTime+"', 5, 7)) as lastENSUREBUDGET,\n" + 
				"       (select a.FACT_HAPPEN\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = to_char(to_number(substr('"+dateTime+"', 0, 4)) - 1) ||\n" + 
				"               substr('"+dateTime+"', 5, 7)) as lastFACT_HAPPEN,\n" + 
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = '"+dateTime+"') as ENSURE_BUDGET,\n" + 
				"       (select a.BUDGET_BASIS\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = '"+dateTime+"') as BUDGET_BASIS\n" + 
				"  from CBM_C_REPORT_ITEM t\n" + 
				" where t.is_use = 'Y'\n" + 
				"   and t.enterprise_code = 'hfdc'\n" + 
				"   and t.is_item = 'Y' and t.report_type ='"+reportType+"' order by t.display_no";
		}
		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		List<ReportItemForm> rItemList = new ArrayList<ReportItemForm>();
		while(it.hasNext()) {
			Object[] data = (Object[]) it.next();
			ReportItemForm model = new ReportItemForm();
			if(data[0] != null) 
				model.setItemName(data[0].toString());
			if(data[1] != null) 
				model.setUnitName(data[1].toString());
			if(data[2] != null)
				model.setLastEnsureBudget(data[2].toString());
			if(data[3] != null)
				model.setLastFactHappen(data[3].toString());
			if(data[4] != null)
				model.setEnsureBudget(data[4].toString());
			if(data[5] != null)
				model.setBudgetBasis(data[5].toString());
			rItemList.add(model);
		}
		return rItemList;
	}
	
	@SuppressWarnings("unchecked")
	public List<ReportItemForm> getReportItemListByMonthRoll(String dateTime,String reportType) {
		String sql = "select t.item_name,\n" +
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = ('"+dateTime+"' || '-01')) as month1,\n" + 
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = ('"+dateTime+"' || '-02')) as month2,\n" + 
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = ('"+dateTime+"' || '-03')) as month3,\n" + 
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = ('"+dateTime+"' || '-04')) as month4,\n" + 
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = ('"+dateTime+"' || '-05')) as month5,\n" + 
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = ('"+dateTime+"' || '-06')) as month6,\n" + 
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = ('"+dateTime+"' || '-07')) as month7,\n" + 
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = ('"+dateTime+"' || '-08')) as month8,\n" + 
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = ('"+dateTime+"' || '-09')) as month9,\n" + 
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = ('"+dateTime+"' || '-10')) as month10,\n" + 
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = ('"+dateTime+"' || '-11')) as month11,\n" + 
				"       (select a.ENSURE_BUDGET\n" + 
				"          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n" + 
				"         where a.item_id = t.item_id\n" + 
				"           and a.budget_make_id = c.budget_make_id\n" + 
				"           and c.budget_time = ('"+dateTime+"' || '-12')) as month12\n" + 
				"  from CBM_C_REPORT_ITEM t\n" + 
				" where t.is_use = 'Y'\n" + 
				"   and t.enterprise_code = 'hfdc'\n" + 
				"   and t.is_item = 'Y' and t.report_type ='"+reportType+"' order by t.display_no";
		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		List<ReportItemForm> rItemList = new ArrayList<ReportItemForm>();
		while(it.hasNext()) {
			Object[] data = (Object[]) it.next();
			ReportItemForm model = new ReportItemForm();
			if(data[0] != null) 
				model.setItemName(data[0].toString());
			if(data[1] != null) 
				model.setMonth1(data[1].toString());
			if(data[2] != null)
				model.setMonth2(data[2].toString());
			if(data[3] != null)
				model.setMonth3(data[3].toString());
			if(data[4] != null)
				model.setMonth4(data[4].toString());
			if(data[5] != null)
				model.setMonth5(data[5].toString());
			if(data[6] != null) 
				model.setMonth6(data[6].toString());
			if(data[7] != null)
				model.setMonth7(data[7].toString());
			if(data[8] != null)
				model.setMonth8(data[8].toString());
			if(data[9] != null)
				model.setMonth9(data[9].toString());
			if(data[10] != null)
				model.setMonth10(data[10].toString());
			if(data[11] != null) 
				model.setMonth11(data[11].toString());
			if(data[12] != null)
				model.setMonth12(data[12].toString());
			rItemList.add(model);
		}
		return rItemList;

	}
	
	
	@SuppressWarnings("unchecked")
	public CbmCReportItemAdd findReportItemInfo(String reportItemId) {
		String sql = "select a.report_item_id,\n" +
				"       a.item_id,\n" + 
				"       a.item_name,\n" + 
				"       a.unit_id,\n" + 
				"       a.data_type,\n" + 
				"       a.display_no,\n" + 
				"       b.item_code\n" + 
				"  from cbm_c_report_item a, cbm_c_item b\n" + 
				" where a.is_item = 'Y'\n" + 
				"   and a.is_use = 'Y'\n" + 
				"   and a.item_id = b.item_id\n" + 
				"   and a.report_item_id = '"+reportItemId+"'";
		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		CbmCReportItemAdd bean = new CbmCReportItemAdd();
		while(it.hasNext()) {
			Object[] data = (Object[]) it.next();
			if(data[0] != null) 
				bean.setReportItemId(Long.parseLong(data[0].toString()));
			if(data[1] != null) 
				bean.setItemId(Long.parseLong(data[1].toString()));
			if(data[2] != null)
				bean.setItemName(data[2].toString());
			if(data[3] != null)
				bean.setUnitId(Long.parseLong(data[3].toString()));
			if(data[4] != null)
				bean.setDataType(Long.parseLong(data[4].toString()));
			if(data[5] != null)
				bean.setDisplayNo(Long.parseLong(data[5].toString()));
			if(data[6] != null) 
				bean.setItemCode(data[6].toString());
		}
		return bean;
	}
}