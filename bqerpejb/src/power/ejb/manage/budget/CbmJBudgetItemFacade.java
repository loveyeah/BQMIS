package power.ejb.manage.budget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.budget.form.CbmJBudgetItemForm;

/**
 * Facade for entity CbmJBudgetItem.
 * 
 * @see power.ejb.manage.budget.CbmJBudgetItem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmJBudgetItemFacade implements CbmJBudgetItemFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "CbmCFactFormulaFacade")
	protected CbmCFactFormulaFacadeRemote factRemote;
	@EJB(beanName = "CbmCCenterTopicFacade")
	protected CbmCCenterTopicFacadeRemote topicRemote;

	public CbmJBudgetItem save(CbmJBudgetItem entity) {
		LogUtil.log("saving CbmJBudgetItem instance", Level.INFO, null);
		try {
			entity.setBudgetItemId(bll.getMaxId("CBM_J_BUDGET_ITEM",
					"BUDGET_ITEM_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		return entity;
	}

	public void delete(CbmJBudgetItem entity) {
		LogUtil.log("deleting CbmJBudgetItem instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmJBudgetItem.class, entity
					.getBudgetItemId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmJBudgetItem update(CbmJBudgetItem entity) {
		LogUtil.log("updating CbmJBudgetItem instance", Level.INFO, null);
		try {
			CbmJBudgetItem result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmJBudgetItem findById(Long id) {
		LogUtil.log("finding CbmJBudgetItem instance with id: " + id,
				Level.INFO, null);
		try {
			CbmJBudgetItem instance = entityManager.find(CbmJBudgetItem.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	// add by fyyang 增加及修改
	@SuppressWarnings("unchecked")
	public void addOrUpdateRecords(List<CbmJBudgetItem> list, String isHappen) {
		Long budgetMakeId = 0l;
		for (CbmJBudgetItem model : list) {
			budgetMakeId = model.getBudgetMakeId();
			if (model.getBudgetItemId() == null || model.getBudgetItemId() == 0) {
				//model.setEnsureBudget(model.getAdviceBudget());
				this.save(model);
				entityManager.flush();
			} else {
				CbmJBudgetItem entity = this.findById(model.getBudgetItemId());
				if (isHappen == null || "".equals("isHappen")) {
					entity.setAdviceBudget(model.getAdviceBudget());
					//entity.setEnsureBudget(model.getAdviceBudget());
					entity.setBudgetBasis(model.getBudgetBasis());
				} else {
					entity.setFinanceHappen(model.getFinanceHappen());
				}
				// add by ltong 20100505
				this.update(entity);
			}
		}
	}

	/*@SuppressWarnings("unchecked")
	public void calculateItemValue(Long budgetMakeId) {

		// String [] itemIds=new String[15];
		List<CbmJBudgetItem> list = new ArrayList<CbmJBudgetItem>();

		String sql = "select t.center_item_id,a.account_order from CBM_J_BUDGET_ITEM t,CBM_C_ITEM a\n"
				+ " ,CBM_J_BUDGET_MAKE b,CBM_C_CENTER_TOPIC c,CBM_C_CENTER_ITEM d \n"
				+ "where t.center_item_id=a.item_id\n"
				+ "and t.budget_make_id="
				+ budgetMakeId
				+ "\n"
				+ "and b.center_id=c.center_id \n"
				+ "and b.topic_id=c.topic_id \n"
				+ "and c.center_topic_id=d.center_topic_id \n"
				+ "and t.center_item_id=d.item_id \n"
				+ "and d.data_source='2' \n"
				+ "and a.is_use='Y' \n"
				+ "and c.is_use='Y' \n"
				+ "and d.is_use='Y' \n"
				+ "and a.account_order <> 1  and a.come_from='2'\n"
				+ "order by a.account_order asc";
		List objList = bll.queryByNativeSQL(sql);
		Iterator it = objList.iterator();
		while (it.hasNext()) {
			CbmJBudgetItem entity = new CbmJBudgetItem();
			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				//entity.setItemId(Long.parseLong(data[0].toString()));
				list.add(entity);
			}

		}

		String sqlStatus = "select d.make_status\n"
				+ "     from cbm_j_budget_make d\n"
				+ "    where d.budget_make_id = " + budgetMakeId + "\n"
				+ "      and rownum = 1";
		Object obj = bll.getSingal(sqlStatus);
		String status = "0";
		if (obj != null) {
			status = obj.toString();
		}

		for (CbmJBudgetItem itemModel : list) {

			String sqlValue = "";
			
			if (sqlStatus.equals("0")) {
				sqlValue = "select GetMakeitemcalculateValue(" + budgetMakeId
						+ ", (select t.item_id from CBM_C_CENTER_ITEM t where t.center_item_id = "+itemModel.getCenterItemId()+") ) from dual";

			} else {
				sqlValue = "select GetGatheritemcalculateValue(" + budgetMakeId
						+ ", (select t.item_id from CBM_C_CENTER_ITEM t where t.center_item_id = "+itemModel.getCenterItemId() + ") from dual";
			}

			String itemValue = bll.getSingal(sqlValue).toString();
			String sqlUpdate = "update CBM_J_BUDGET_ITEM  a\n";
			if (status.equals("0")) {
				sqlUpdate += "set a.advice_budget=" + itemValue;
			} else {
				sqlUpdate += "set a.ensure_budget=" + itemValue;
			}

			bll.exeNativeSQL(sqlUpdate);
			entityManager.flush();

		}

		

	}*/
	
	
	@SuppressWarnings("unchecked")
	public void calculateItemValue(Long budgetMakeId) {

		// String [] itemIds=new String[15];
		List<CbmJBudgetItem> list = new ArrayList<CbmJBudgetItem>();

		String sql = 
			"SELECT a.item_id,\n" +
			"       a.account_order,t.budget_item_id\n" + 
			"  FROM CBM_J_BUDGET_ITEM t,\n" + 
			"       CBM_C_ITEM        a,\n" + 
			"       CBM_C_CENTER_ITEM d\n" + 
			" WHERE t.budget_make_id = \n" +
			budgetMakeId + 
			"   AND t.center_item_id = d.center_item_id\n" + 
			"   AND d.item_id = a.item_id\n" + 
			"   AND d.data_source = '2'\n" + 
			"   AND t.is_use = 'Y'\n" + 
			"   AND a.account_order <> 1\n" + 
			"   AND a.come_from = '2'\n" + 
			" ORDER BY a.account_order ASC";
		
		List objList = bll.queryByNativeSQL(sql);
		Iterator it = objList.iterator();
		while (it.hasNext()) {
			CbmJBudgetItem entity = new CbmJBudgetItem();
			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				entity.setCenterItemId(Long.parseLong(data[0].toString())); //itemId
				
			}
			if (data[2] != null) {
				entity.setBudgetItemId(Long.parseLong(data[2].toString())); 
				
			}
			list.add(entity);

		}


		for (CbmJBudgetItem itemModel : list) {

				String sqlValue = "";
			

				sqlValue = "select GetMakeitemcalculateValue(" + budgetMakeId
						+ ", "+itemModel.getCenterItemId()+") from dual";



			String itemValue = bll.getSingal(sqlValue).toString();
			String sqlUpdate = "update CBM_J_BUDGET_ITEM  a\n";
			sqlUpdate += "set a.advice_budget=" + itemValue
							+" where a.budget_item_id = "+itemModel.getBudgetItemId();
			

			bll.exeNativeSQL(sqlUpdate);
			entityManager.flush();
		}

	}

	/**
	 * 计算财务发生值
	 * 
	 * @param budgetMakeId
	 */
	@SuppressWarnings("unchecked")
	public void calFinanceItemValue(Long budgetMakeId) {

		// String [] itemIds=new String[15];
		List<CbmJBudgetItem> list = new ArrayList<CbmJBudgetItem>();

		String sql = "select t.item_id,a.account_order from CBM_J_BUDGET_ITEM t,CBM_C_ITEM a \n"
				+ " ,CBM_J_BUDGET_MAKE b,CBM_C_CENTER_TOPIC c,CBM_C_CENTER_ITEM d \n"
				+ "where t.item_id=a.item_id\n"
				+ "and t.budget_make_id="
				+ budgetMakeId
				+ " \n"
				+ "and t.budget_make_id=b.budget_make_id \n"
				+ "and b.center_id=c.center_id \n"
				+ "and b.topic_id=c.topic_id \n"
				+ "and c.center_topic_id=d.center_topic_id \n"
				+ "and t.item_id=d.item_id \n"
				+ "and d.data_source='2' \n"
				+ "and a.is_use='Y' \n"
				+ "and c.is_use='Y' \n"
				+ "and d.is_use='Y' \n"
				+ "and a.account_order <> 1  and a.come_from='2'\n"
				+ "order by a.account_order asc";
		List objList = bll.queryByNativeSQL(sql);
		Iterator it = objList.iterator();
		while (it.hasNext()) {
			CbmJBudgetItem entity = new CbmJBudgetItem();
			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				list.add(entity);
			}

		}
		for (CbmJBudgetItem itemModel : list) {


		}

	}

	// ----------------liuyi-------start-------------------------------------------------------------------

	// modify by liuyi 090819
	@SuppressWarnings("unchecked")
	public PageObject findToCheck(String DeptCode, String bugdet_time,
			String data_type, String enterpriseCode, int... rowStartIdxAndCount) {
		try { // ==============该方法需修改=============================================
			PageObject obj = new PageObject();
			String sqlFirString = "select distinct  1 as flag,\n";
			// update by sychen 20100611
			String sql="a.budget_item_id,\n" +
				"       b.item_alias,\n" + 
				"       a.ensure_budget,\n" + 
				"       a.fact_happen,\n" + 
				"       a.item_id,\n" + 
				"       a.finance_happen,\n" + 
				"       f.center_id,\n" + 
				"       f.topic_id,\n" + 
				"       TRIM('"+bugdet_time+"'),\n" + 
				"       c.dep_code,\n" + 
				"       c.dep_name,\n" + 
				"       d.item_code,\n" + 
				"       d.item_name,\n" + 
				"       f.budget_make_id,e.zbbmtx_code\n" + 
				"  FROM cbm_j_budget_item a,\n" + 
				"       cbm_c_master_item b,\n" + 
				"       CBM_J_BUDGET_MAKE f,\n" + 
				"       CBM_C_CENTER      c,\n" + 
				"       CBM_C_ITEM        d,\n" + 
				" 		cbm_c_itemtx e, \n"+
				"		cbm_j_budget_gather g" + // add by ywliu 20100625
				" WHERE f.budget_make_id = a.budget_make_id\n" + 
				"   AND b.item_id = a.item_id\n" + 
				"   AND f.center_id = c.center_id\n" + 
				"   AND b.center_id = f.center_id\n" + 
				"	AND f.budget_gather_id = g.budget_gather_id\n" +
				"   AND a.item_id = d.item_id\n" + 
				" and e.item_id=a.item_id and e.is_use='Y' \n"+
				"   AND b.is_use = 'Y'\n" + 
				"   AND c.is_use = 'Y'\n" + 
				"   AND d.is_use = 'Y'"	+
				"	AND g.gather_status = '2'" + // add by ywliu 20100625
				"   AND c.dep_code = '"+ DeptCode+ "'\n";

//			String sql = " a.budget_item_id, \n"
//					+ "                b.item_alias,\n"
//					+ "                a.ensure_budget,\n"
//					+ "                a.fact_happen,\n"
//					+ "                e.center_item_id,\n"
//					+ "                 a.item_id,\n"
//					+ "                a.finance_happen,\n"
//
//					+ "                f.center_id,\n"
//					+ "                f.topic_id,\n"
//					// + " f.budget_time,\n"
//					+ "trim('"
//					+ bugdet_time
//					+ "')"
//					+ ",                c.dep_code,\n"
//					+ "                c.dep_name,\n"
//					+ "                d.item_code,\n"
//					+ "                d.item_name, \n"
//					+ "                e.item_alias sss, \n"
//					+ "                 e.data_source, \n"
//					+ "                 e.display_no, \n"
//					+ "                 f.budget_make_id \n"
//					+ "  from cbm_j_budget_item a, cbm_c_master_item b,CBM_C_CENTER c,CBM_C_ITEM d,"
//					+ "  CBM_C_CENTER_ITEM e,CBM_J_BUDGET_MAKE f,CBM_C_CENTER_TOPIC g \n"
//					+ " where f.center_id = c.center_id\n"
//					+ "  and c.dep_code = '"
//					+ DeptCode
//					+ "'\n"
//					+ "   and a.item_id = b.item_id\n"
//					+ "   and f.center_id=b.center_id\n"
//					+ "   and a.item_id=d.item_id \n"
//					+ "   and a.item_id=e.item_id \n"
//					+ "   and e.center_topic_id=g.center_topic_id \n"
//					+ "   and f.center_id=g.center_id \n"
//					+ "   and f.topic_id=g.topic_id \n"
//					+ "   and a.budget_make_id=f.budget_make_id \n"
//					+ "   and a.enterprise_code='"
//					+ enterpriseCode
//					+ "' \n"
//					+ "   and b.enterprise_code='"
//					+ enterpriseCode
//					+ "' \n"
//					+ "   and c.enterprise_code='"
//					+ enterpriseCode
//					+ "' \n"
//					+ "   and d.enterprise_code='"
//					+ enterpriseCode
//					+ "' \n"
//					+ "   and e.enterprise_code='"
//					+ enterpriseCode
//					+ "' \n"
//					+ "   and g.enterprise_code='"
//					+ enterpriseCode
//					+ "' \n"
//					+ "   and f.enterprise_code='"
//					+ enterpriseCode
//					+ "' \n"
//					+ "   and b.is_use='Y' \n"
//					+ "   and c.is_use='Y' \n"
//					+ "   and d.is_use='Y' \n"
//					+ "   and e.is_use='Y' \n"
//					+ "   and g.is_use='Y' \n"
//					+ "   and a.budget_make_id=f.budget_make_id \n";
			if (data_type != null && !(data_type.equals(""))) {
				sql = sql + "   and b.data_type = '" + data_type + "'\n";
			}

			sql = sql + "   and b.come_from = '1'\n";
			String sqlTime = "";
			if (bugdet_time != null && !(bugdet_time.equals(""))) {
				sqlTime = "   and f.budget_time = '" + bugdet_time + "'";
			}
			String sqlStr = sqlFirString + sql + sqlTime
			        + " order by e.zbbmtx_code \n";
//					+ " order by e.display_no \n";
			List list = bll.queryByNativeSQL(sqlStr, rowStartIdxAndCount);
			if (list == null || list.size() == 0) {
//				String yearSql="a.budget_item_id,\n" +
//				"       b.item_alias,\n" + 
//				"       a.ensure_budget,\n" + 
//				"       '',\n" + 
//				"       a.item_id,\n" + 
//				"       a.finance_happen,\n" + 
//				"       f.center_id,\n" + 
//				"       f.topic_id,\n" + 
//				"       TRIM('"+bugdet_time+"'),\n" + 
//				"       c.dep_code,\n" + 
//				"       c.dep_name,\n" + 
//				"       d.item_code,\n" + 
//				"       d.item_name,\n" + 
//				"       f.budget_make_id,e.zbbmtx_code\n" + 
//				"  FROM cbm_j_budget_item a,\n" + 
//				"       cbm_c_master_item b,\n" + 
//				"       CBM_J_BUDGET_MAKE f,\n" + 
//				"       CBM_C_CENTER      c,\n" + 
//				"       CBM_C_ITEM        d\n" + 
//				" ,cbm_c_itemtx e \n"+
//				" WHERE f.budget_make_id = a.budget_make_id\n" + 
//				"   AND b.item_id = a.item_id\n" + 
//				"   AND f.center_id = c.center_id\n" + 
//				"   AND b.center_id = f.center_id\n" + 
//				"   AND a.item_id = d.item_id\n" + 
//				" and e.item_id=a.item_id and e.is_use='Y' \n"+
//				"   AND b.is_use = 'Y'\n" + 
//				"   AND c.is_use = 'Y'\n" + 
//				"   AND d.is_use = 'Y'"	+
//				"   AND c.dep_code = '"+ DeptCode+ "'\n";
//				sqlStr = "select distinct  0 as flag,\n" + yearSql;
//				if (bugdet_time != null && !(bugdet_time.equals(""))) {
//					sqlStr += "   and f.budget_time = '"
//							+ bugdet_time.substring(0, 4) + "'";
//				}
//				sqlStr += " order by e.zbbmtx_code \n";
//				sqlStr += " order by e.display_no \n";
//				list = bll.queryByNativeSQL(sqlStr, rowStartIdxAndCount);
			}

			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				CbmJBudgetItemForm model = new CbmJBudgetItemForm();
				Object[] data = (Object[]) it.next();
				if (data[0] != null) {
					model.setChangeId(Long.parseLong(data[0].toString()));
				}

				model.setBudgetItemId(Long.parseLong(data[1].toString()));

				if (data[2] != null)
					model.setItemAlias(data[2].toString());
				if (data[3] != null)
					model.setEnsureBudget(Double
							.parseDouble(data[3].toString()));
				if (data[4] != null)
					model.setFactHappen(Double.parseDouble(data[4].toString()));
				if (data[5] != null)
					model.setItemId(Long.parseLong(data[5].toString()));
				if (data[6] != null)
					model.setFinanceHappen(Double.parseDouble(data[6]
							.toString()));
				if (data[7] != null)
					model.setCenterId(Long.parseLong(data[7].toString()));
				if (data[8] != null)
					model.setTopicId(Long.parseLong(data[8].toString()));
				if (data[9] != null)
					model.setBudgetTime(data[9].toString());
				if (data[10] != null)
					model.setCenterCode(data[10].toString());
				if (data[11] != null)
					model.setCenterName(data[11].toString());
				if (data[12] != null)
					model.setItemCode(data[12].toString());
				if (data[13] != null)
					model.setItemName(data[13].toString());
				if (data[14] != null)
					model.setBudgetMakeId(Long.parseLong(data[14].toString()));
//				if (data[5] != null)
//					model.setCenterItemId(Long.parseLong(data[5].toString()));
//				if (data[6] != null)
//					model.setItemId(Long.parseLong(data[6].toString()));
//				if (data[7] != null)
//					model.setFinanceHappen(Double.parseDouble(data[7]
//							.toString()));
//				if (data[8] != null)
//					model.setCenterId(Long.parseLong(data[8].toString()));
//				if (data[9] != null)
//					model.setTopicId(Long.parseLong(data[9].toString()));
//				if (data[10] != null)
//					model.setBudgetTime(data[10].toString());
//				if (data[11] != null)
//					model.setCenterCode(data[11].toString());
//				if (data[12] != null)
//					model.setCenterName(data[12].toString());
//				if (data[13] != null)
//					model.setItemCode(data[13].toString());
//				if (data[14] != null)
//					model.setItemName(data[14].toString());
//				if (data[15] != null)
//					model.setDeptItemAlias(data[15].toString());
//				if (data[16] != null)
//					model.setDataSource(data[16].toString());
//				if (data[17] != null)
//					model.setDispalyNo(data[17].toString());
//				if (data[18] != null)
//					model.setBudgetMakeId(Long.parseLong(data[18].toString()));
				if(data[15]!=null)
				{
					model.setDispalyNo(data[15].toString());
				}
				arrlist.add(model);
			}
			obj.setList(arrlist);
			String sqlCount = "select count(*) from (" + sqlStr + ") \n";
			obj.setTotalCount(Long
					.parseLong(bll.getSingal(sqlCount).toString()));
			return obj;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过指标itemId,部门centerId,月份budgetTime查找财务发生值 //modify by liuyi 090819
	 */
	@SuppressWarnings("unchecked")
	public Double findFinanceValue(Long itemId, Long centerId,
			String budetTime, String enterpriseCode) {
		Double flag = null;
		String sql = "select a.finance_happen \n"
				+ "from CBM_J_BUDGET_ITEM a \n" + "where a.center_id="
				+ centerId + " \n" + "and a.item_id=" + itemId + " \n"
				+ "and a.budget_time='" + budetTime + "' \n"
				+ "and a.enterprise_code='" + enterpriseCode + "' \n";
		List list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			Object[] obj = (Object[]) list.get(0);
			if (obj[0] != null)
				flag = Double.parseDouble(obj[0].toString());
		}
		return flag;
	}

	// add by liuyi 090819
	public void saveModified(List<CbmJBudgetItemForm> modList) {
		if (modList != null && modList.size() > 0) {
			for (CbmJBudgetItemForm form : modList) {
				if (form.getDataSource().equals("1")) {
					CbmJBudgetItem entity = this.findById(form
							.getBudgetItemId());
					entity.setFinanceHappen(form.getFinanceHappen());
					this.update(entity);
					entityManager.flush();
					// this.calFinanceItemValue(form.getBudgetMakeId());
				}
			}
			this.calFinanceItemValue(modList.get(0).getBudgetMakeId());
			// for(CbmJBudgetItemForm form : modList)
			// {
			// if(form.getDataSource().equals("2"))
			// {
			// this.calFinanceItemValue(form.getBudgetMakeId());
			// }
			// }
		}
	}

	// =================汇总查询列表 start==========================================

	// modify by liuyi 090819
	//modify by fyyang 20100610
	@SuppressWarnings("unchecked")
	private List<CbmJBudgetItemForm> findAllItemList(String topicId,
			String budgetTime, String enterpriseCode) {
		List<CbmJBudgetItemForm> arraylist = new ArrayList();
//		String sql = "select distinct t.item_id,c.item_name, \n"
//				+ "       getunitname((select r.unit_code from cbm_c_item r where t.item_id = r.item_id)) \n"
//				+ ",a.zbbmtx_code \n" // add by fyyang 20100310
//				+ "  from CBM_C_CENTER_ITEM t, cbm_c_item c,CBM_C_CENTER_TOPIC d \n"
//				+ ",cbm_c_itemtx       a \n" // add by fyyang 20100310
//				+ " where t.item_id=c.item_id\n"
//				+ "   and d.topic_id = "
//				+ topicId
//				+ "\n"
//				+ " and t.center_topic_id=d.center_topic_id \n"
//				+ "   and t.enterprise_code = c.enterprise_code\n"
//				+ "   and t.enterprise_code = '"
//				+ enterpriseCode
//				+ "'"
//				+ "   and d.enterprise_code = '"
//				+ enterpriseCode
//				+ "'"
//				+ "   and t.is_use='Y'"
//				+ "   and c.is_use='Y'"
//				+ "   and d.is_use='Y'"
//				+ "   and a.item_id = c.item_id  and a.is_use = 'Y'  and a.enterprise_code='"
//				+ enterpriseCode + "' \n" + "  order by a.zbbmtx_code asc ";
	
		String sql=
			"select distinct *\n" +
			"  from ((select t.item_id,\n" + 
			"                t.item_alias,\n" + 
			"                getunitname(c.unit_code),\n" + 
			"                a.zbbmtx_code\n" + 
			"           from CBM_C_CENTER_ITEM  t,\n" + 
			"                cbm_c_item         c,\n" + 
			"                CBM_C_CENTER_TOPIC d,\n" + 
			"                cbm_c_itemtx       a\n" + 
			"          where t.item_id = c.item_id\n" + 
			"            and d.topic_id = "+topicId+"\n" + 
			"            and t.center_topic_id = d.center_topic_id\n" + 
			"            and t.enterprise_code = c.enterprise_code\n" + 
			"            and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"            and d.enterprise_code = '"+enterpriseCode+"'\n" + 
			"            and t.is_use = 'Y'\n" + 
			"            and c.is_use = 'Y'\n" + 
			"            and d.is_use = 'Y'\n" + 
			"            and a.item_id = c.item_id\n" + 
			"            and a.is_use = 'Y'\n" + 
			"            and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"            and d.center_id not in\n" + 
			"                (select t.center_id\n" + 
			"                   from cbm_j_budget_make t\n" + 
			"                  where t.budget_time = '"+budgetTime+"')) union\n" + 
			"        (select r.item_id,\n" + 
			"                s.item_alias,\n" + 
			"                GETUNITNAME(u.unit_code),\n" + 
			"                a.zbbmtx_code\n" + 
			"           from CBM_J_BUDGET_MAKE  t,\n" + 
			"                CBM_J_BUDGET_ITEM  r,\n" + 
			"                CBM_C_CENTER_ITEM  s,\n" + 
			"                CBM_C_ITEM         u,\n" + 
			"                CBM_C_CENTER_TOPIC g,\n" + 
			"                cbm_c_itemtx       a\n" + 
			"          where t.budget_make_id = r.budget_make_id\n" + 
			"            and r.item_id = s.item_id(+)\n" + 
			"            and r.item_id = u.item_id(+)\n" + 
			"            and t.center_id = g.center_id(+)\n" + 
			"            and t.topic_id = g.topic_id(+)\n" + 
			"            and g.center_topic_id = s.center_topic_id\n" + 
			"            and t.topic_id = "+topicId+"\n" + 
			"            and a.item_id = r.item_id\n" + 
			"            and t.budget_time = '"+budgetTime+"'\n" + 
			"            and u.is_use(+) = 'Y')) tt\n" + 
			" order by tt.zbbmtx_code asc";

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
			if(o[3]!=null)
			{
				//add by fyyang 20100612
				model.setItemCode(o[3].toString());
			}
			arraylist.add(model);
		}

		return arraylist;
	}

	// modify by liuyi 090819
	// @SuppressWarnings("unchecked")
	// public List<CbmJBudgetItemForm> findAllItemAdviceBudgetList(String
	// topicId,
	// String budgetTime, String enterpriseCode, Long itemId) {
	// List<CbmJBudgetItemForm> arraylist = new ArrayList();
	// String sql = "select b.center_id,t.advice_budget,t.budget_make_id\n"
	// + " from cbm_j_budget_item t,CBM_J_BUDGET_MAKE b \n"
	// + " where b.topic_id =" + topicId + "\n"
	// + " and b.make_status='2'" + " and b.budget_time ='"
	// + budgetTime + "'\n"
	// + " and t.budget_make_id=b.budget_make_id \n"
	// + " and t.item_id =" + itemId + "\n"
	// + " and t.enterprise_code ='" + enterpriseCode + "'";
	// List list = bll.queryByNativeSQL(sql);
	// Iterator i = list.iterator();
	// while (i.hasNext()) {
	// Object[] o = (Object[]) i.next();
	// CbmJBudgetItemForm form = new CbmJBudgetItemForm();
	// if (o[0] != null) {
	// form.setCenterId(Long.parseLong(o[0].toString()));
	// }
	// if (o[1] != null) {
	// form.setAdviceBudget(Double.parseDouble(o[1].toString()));
	// }
	// if (o[2] != null)
	// form.setBudgetMakeId(Long.parseLong(o[2].toString()));
	// arraylist.add(form);
	// }
	// return arraylist;
	// }
	@SuppressWarnings("unchecked")
	private void queryDeptValue(List<Object[]> data, Long itemId,
			Map<String, Object> map) {
		Double dd = 0.0;
		Long budgetMakeId = null;
		if (data != null && data.size() > 0) {
			Iterator it = data.iterator();
			while (it.hasNext()) {
				Object[] rec = (Object[]) it.next();
				if (itemId.equals(Long.parseLong(rec[0].toString()))) {
					map.put(rec[1].toString(), rec[2]);
					if (rec[3] != null)
						budgetMakeId = Long.parseLong(rec[3].toString());
					if (rec[2] != null)
						dd += Double.parseDouble(rec[2].toString());
					it.remove();
				}
			}
		}
		map.put("companyBudget", dd);
		map.put("budgetMakeId", budgetMakeId);
	}

	// ensureBudget 审定预算
	/**
	 * modify by fyyang 20100610
	 */
	public Map<String, Object> queryGather(Long gatherId, String topicId,
			String date, String enterpriseCode) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		Object[] dataObj = null;
		List<CbmJBudgetItemForm> arrayList = this.findAllItemList(topicId,
				date, enterpriseCode);

		String sql=
			"SELECT a.item_id, b.center_id, a.ensure_budget, b.budget_make_id\n" +
			"          FROM cbm_j_budget_item a, CBM_J_BUDGET_MAKE b, cbm_c_center c\n" + 
			"         WHERE b.center_id = c.center_id\n" + 
			"           and c.is_use = 'Y'\n" + 
			"           and a.budget_make_id = b.Budget_Make_Id\n" + 
			"           AND a.enterprise_code = b.enterprise_code\n" + 
			"           AND a.enterprise_code = 'hfdc'\n" + 
			"           AND b.make_status = '2'\n" + 
			"           and b.topic_id="+topicId+"\n" + 
			"           AND b.Budget_Time = '"+date+"'";

		List<Object[]> data = bll.queryByNativeSQL(sql);
		if (arrayList != null && arrayList.size() > 0) {
			dataObj = new Object[arrayList.size()];
			Iterator it = arrayList.iterator();
			int i = 0;
			while (it.hasNext()) {
				Map<String, Object> result = new HashMap<String, Object>();
				CbmJBudgetItemForm model = (CbmJBudgetItemForm) it.next();
				//----modify by fyyang 20100612----显示指标名称的层次结构---------
				int itemLevle=model.getItemCode().length()/3-2;
				
				 if(itemLevle>0)
				 {
					 String levelItemName="";
					 for(int num=0;num<itemLevle;num++)
					 {
						 levelItemName+="  ";
					 }
					 levelItemName=levelItemName+model.getItemAlias();
					 result.put("itemName", "<pre>"+levelItemName+"</pre>");
				 }
				 else
				 {
					 result.put("itemName", "<pre>"+model.getItemAlias()+"</pre>");
				 }
				//-------------------------------------------
				
				result.put("itemId", model.getItemId());
				result.put("unitName", model.getUnitName());
				result.put("budgetGatherId", gatherId);
				result.put("topicId", topicId);
				result.put("budgetTime", date);
				// add by liuyi 20100412 预算状态，预算工作流
//				CbmJBudgetGather gatherEntity = null;
//				if (gatherId != null)
//					gatherEntity = entityManager.find(CbmJBudgetGather.class,
//							gatherId);
//				else
//					gatherEntity = new CbmJBudgetGather();
//				result.put("gatherStatus", gatherEntity.getGatherStatus());
//				result.put("gatherWorkFlowNo", gatherEntity.getWorkFlowNo());

				this.queryDeptValue(data, model.getItemId(), result);
				dataObj[i++] = result;
			}
		}
		;
		if (dataObj == null) {
			resultMap.put("data", "[]");
		} else {
			resultMap.put("data", dataObj);
		}
		List<CbmJBudgetItemForm> headerList = topicRemote.getDeptList(Long
				.parseLong(topicId), enterpriseCode);
		if (headerList != null && headerList.size() > 0) {
			Iterator it = headerList.iterator();
			while (it.hasNext()) {
				CbmJBudgetItemForm form = (CbmJBudgetItemForm) it.next();
			}
		}
		sb.append("[");
		if (headerList != null && headerList.size() > 0) {
			Iterator it = headerList.iterator();
			sb
					.append("{'header' : '预算指标','width':100,'dataIndex' : 'itemName','align':'left'},");
			sb
					.append("{'header' : '计量单位','width':100,'dataIndex' : 'unitName','align':'center'}");
			while (it.hasNext()) {
				CbmJBudgetItemForm model = (CbmJBudgetItemForm) it.next();
				sb.append(",{'header' :'");
				sb.append(model.getCenterName());
				sb.append("','width': 100,'dataIndex': '");
				sb.append(model.getCenterId());
				sb
						.append("','align':'center','editor':new Ext.form.NumberField({allowDecimals : true,decimalPrecision : 4})}");

			}
			sb
					.append(",{'header' : '公司预算','width':100,'dataIndex' : 'companyBudget','align':'center','renderer':function(value){if(value!='') return value.toFixed(4); else return 0; }}");
		}
		sb.append("]");
		resultMap.put("columModel", sb.toString());
		sb = new StringBuffer();
		sb.append("[");
		if (headerList != null && headerList.size() > 0) {
			Iterator it = headerList.iterator();
			sb
					.append("{'name':'itemName'},{'name':'unitName'},{'name':'itemId'},{'name':'companyBudget'}");
			sb
					.append(",{'name':'budgetGatherId'},{'name':'topicId'},{'name':'budgetTime'},{'name':'gatherWorkFlowNo'}");
			sb.append(",{'name':'budgetMakeId'},{'name':'gatherStatus'}");
			while (it.hasNext()) {
				CbmJBudgetItemForm model = (CbmJBudgetItemForm) it.next();
				sb.append(",{'name':'" + model.getCenterId() + "'}");
			}
		}
		sb.append("]");
		resultMap.put("fieldsNames", sb.toString());

		return resultMap;
	}

	// =================汇总查询列表 end==========================================

	// ----------------liuyi-------end-------------------------------------------------------------------

	// ----------------drdu-------start-------------------------------------------------------------------
	// add by drdu 090819
	@SuppressWarnings("unchecked")
	public PageObject findModifyItemList(String deptCode, String topicId,
			String budgetTime, String formatType, String enterpriseCode,String itemName,
			int... rowStartIdxAndCount) {
		PageObject obj = new PageObject();
		String sql = "select distinct tt.*, tt1.work_flow_no, tt1.change_status,tt1.change_date,h.zbbmtx_code\n"
				+ "  from (select distinct a.budget_item_id,\n"
				+ "                        a.item_id,\n"
				+ "                        (select g.item_alias\n"
				+ "                           from CBM_C_CENTER_TOPIC h, CBM_C_CENTER_ITEM g\n"
				+ "                          where a.budget_make_id = c.budget_make_id\n"
				+ "                            and c.center_id = h.center_id\n"
				+ "                            and c.topic_id = h.topic_id\n"
				+ "                            and h.center_topic_id = g.center_topic_id\n"
				+ "                            and g.item_id = a.item_id) item_alias,\n"
				+ "                        a.budget_make_id,\n"
				+ "                        a.advice_budget,\n"
				+"                         (nvl(a.ensure_budget,0)+nvl(a.budget_add,0)),\n"//update by sychen 20100624
//				+ "                        a.ensure_budget,\n"
				+ "                        getunitname((select r.unit_code\n"
				+ "                                      from cbm_c_item r\n"
				+ "                                     where a.item_id = r.item_id\n"
				+ "                                       and r.is_use = 'Y')),\n"
				+ "                        c.topic_id,\n"
				+ "                        c.budget_time,\n"
				+ "                        (select max(d.change_id)\n"
				+ "                           from CBM_J_BUDGET_CHANGE d\n"
				+ "                          where d.budget_item_id = a.budget_item_id\n"
				+ "                            and d.is_use = 'Y') change_id\n"
				+ "          from CBM_J_BUDGET_ITEM  a,\n"
				+ "               cbm_c_center_item  b,\n"
				+ "               CBM_J_BUDGET_MAKE  c,\n"
				+ "                       CBM_C_CENTER_TOPIC p,\n"
				+ "                       CBM_J_BUDGET_GATHER k\n"
				+ "                 where a.item_id = b.item_id\n"
				+ "                   and a.budget_make_id = c.budget_make_id\n"
				+ "                    and c.budget_gather_id = k.budget_gather_id\n"
				+ "                   and k.gather_status = '2'\n"
				+ "           and b.center_topic_id = p.center_topic_id\n"
				+ "           and c.center_id in (select m.center_id\n"
				+ "                                from cbm_c_center m\n"
				+ "                               where m.dep_code like '"
				+ deptCode
				+ "')\n"
				+ "           and a.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and b.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and c.enterprise_code = '"
				+ enterpriseCode
				+ "') tt,\n"
				+ "       CBM_J_BUDGET_CHANGE tt1,  cbm_c_itemtx h\n"
				+ " where tt.change_id = tt1.change_id(+)   and  h.item_id=tt.item_id  \n";

//		String sqlCount = "select count(1)\n"
//				+ "  from (select distinct tt.*, tt1.work_flow_no, tt1.change_status\n"
//				+ "          from (select distinct a.budget_item_id,\n"
//				+ "                                a.item_id,\n"
//				+ "                                (select g.item_alias\n"
//				+ "                                   from CBM_C_CENTER_TOPIC h,\n"
//				+ "                                        CBM_C_CENTER_ITEM  g\n"
//				+ "                                  where a.budget_make_id = c.budget_make_id\n"
//				+ "                                    and c.center_id = h.center_id\n"
//				+ "                                    and c.topic_id = h.topic_id\n"
//				+ "                                    and h.center_topic_id = g.center_topic_id\n"
//				+ "                                    and g.item_id = a.item_id) item_alias,\n"
//				+ "                                a.budget_make_id,\n"
//				+ "                                a.advice_budget,\n"
//				+ "                                a.ensure_budget,\n"
//				+ "                                getunitname((select r.unit_code\n"
//				+ "                                              from cbm_c_item r\n"
//				+ "                                             where a.item_id = r.item_id\n"
//				+ "                                               and r.is_use = 'Y')),\n"
//				+ "                                c.topic_id,\n"
//				+ "                                c.budget_time,\n"
//				+ "                                (select max(d.change_id)\n"
//				+ "                                   from CBM_J_BUDGET_CHANGE d\n"
//				+ "                                  where d.budget_item_id = a.budget_item_id\n"
//				+ "                                    and d.is_use = 'Y') change_id\n"
//				+ "                  from CBM_J_BUDGET_ITEM  a,\n"
//				+ "                       cbm_c_center_item  b,\n"
//				+ "                       CBM_J_BUDGET_MAKE  c,\n"
//				+ "                       CBM_C_CENTER_TOPIC p,\n"
//				+ "                       CBM_J_BUDGET_GATHER k\n"
//				+ "                 where a.item_id = b.item_id\n"
//				+ "                   and a.budget_make_id = c.budget_make_id\n"
//				+ "                    and c.budget_gather_id = k.budget_gather_id\n"
//				+ "                   and k.gather_status = '2'\n"
//				+ "                   and b.center_topic_id = p.center_topic_id\n"
//				+ "                   and c.center_id = (select m.center_id\n"
//				+ "                                        from cbm_c_center m\n"
//				+ "                                       where m.dep_code = '"
//				+ deptCode + "')\n"
//				+ "                   and a.enterprise_code = '"
//				+ enterpriseCode + "'\n"
//				+ "                   and b.enterprise_code = '"
//				+ enterpriseCode + "'\n"
//				+ "                   and c.enterprise_code = '"
//				+ enterpriseCode + "') tt,\n"
//				+ "               CBM_J_BUDGET_CHANGE tt1\n"
//				+ "         where tt.change_id = tt1.change_id(+)\n"
//				+ "           and tt.topic_id = " + topicId + "\n"
//				+ "           and tt.budget_time= '" + budgetTime + "') bb";

		if (topicId != null && !(topicId.equals(""))) {
			sql = sql + "and tt.topic_id='" + topicId + "' \n";
		}
		if (budgetTime != null && !(budgetTime.equals(""))) {
			sql = sql + "and tt.budget_time='" + budgetTime + "' \n";
		}
		if(itemName!=null&&!itemName.equals(""))
		{
			sql+=" and tt.item_alias like '%"+itemName+"%'";
		}

		String sqlCount="select count(*) from ("+sql+")";
		// add by liuyi 20100601 增加排序
		sql += " order by  h.zbbmtx_code \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arraylist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			CbmJBudgetItemForm model = new CbmJBudgetItemForm();
			Object[] data = (Object[]) it.next();
			model.setBudgetItemId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				model.setItemId(Long.parseLong(data[1].toString()));
			if (data[2] != null)
				model.setItemAlias(data[2].toString());
			if (data[3] != null)
				model.setBudgetMakeId(Long.parseLong(data[3].toString()));
			if (data[4] != null)
				model.setAdviceBudget(Double.parseDouble(data[4].toString()));
			if (data[5] != null)
				model.setEnsureBudget(Double.parseDouble(data[5].toString()));
			if (data[6] != null)
				model.setUnitName(data[6].toString());
			if (data[7] != null)
				model.setTopicId(Long.parseLong(data[7].toString()));
			if (data[8] != null)
				model.setBudgetTime(data[8].toString());
			if (data[9] != null)
				model.setChangeId(Long.parseLong(data[9].toString()));
			if (data[10] != null)
				model.setChangeWorkFlowNo(Long.parseLong(data[10].toString()));
			if (data[11] != null)
				model.setChangeStatus(data[11].toString());
			if (data[12] != null)
				model.setChangeDate(data[12].toString());
			//add by fyyang 20100612
			if(data[13]!=null)
				model.setItemCode(data[13].toString());
			arraylist.add(model);
		}
		obj.setList(arraylist);
		if (list != null) {
			obj.setTotalCount(Long
					.parseLong(bll.getSingal(sqlCount).toString()));
		}
		return obj;
	}

	// ----------------drdr-------end-------------------------------------------------------------------
	/**
	 * 通过指标，预算编制单id查找一条部门明细实例id add by liuyi 090820
	 * 
	 * @param budgetMakeId
	 * @param itemId
	 * @param enterpriseCode
	 * @return
	 */
	public List<Long> findDetailsInstance(Long budgetMakeId, Long itemId,
			String enterpriseCode) {
		List<Long> arrlist = new ArrayList<Long>();
		String sql = "select a.budget_item_id \n"
				+ "from CBM_J_BUDGET_ITEM a,CBM_J_BUDGET_MAKE b \n"
				+ "where a.budget_make_id=b.budget_make_id \n"
				+ "and a.item_id=" + itemId + " \n" + "and b.budget_make_id="
				+ budgetMakeId + " \n" + "and a.enterprise_code='"
				+ enterpriseCode + "' \n";
		List list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				arrlist.add(Long.parseLong(it.next().toString()));
			}
		}
		return arrlist;
	}

	/**
	 * 通过指标列表，预算时间查询明细表中的各个预算制 add by liuyi 090829
	 * 
	 * @param itemList
	 * @param budgetTime
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject getComplexBudgetList(List<CbmJBudgetItemForm> itemList,
			String budgetTime, String enterpriseCode) {
		PageObject pg = new PageObject();
		String sql = "";
		String sqlCount = "";
		if (itemList != null && itemList.size() > 0) {
			for (int i = 0; i <= itemList.size() - 1; i++) {
				sql = "select a.forecast_budget, \n" + "a.advice_budget, \n"
						+ "a.budget_add, \n" + "a.ensure_budget, \n"
						+ "a.fact_happen, \n" + "a.finance_happen, \n"
						+ "b.budget_time \n"
						+ "from CBM_J_BUDGET_ITEM a,CBM_J_BUDGET_MAKE b \n"
						+ "where a.budget_make_id=b.budget_make_id \n"
						+ "and b.budget_time='" + budgetTime + "' \n"
						+ "and a.item_id=" + itemList.get(i).getItemId()
						+ " \n" + "and a.enterprise_code='" + enterpriseCode
						+ "' \n" + "and b.enterprise_code='" + enterpriseCode
						+ "' \n";
				List list = bll.queryByNativeSQL(sql);
				if (list != null && list.size() > 0) {
					Iterator it = list.iterator();
					while (it.hasNext()) {
						Object[] data = (Object[]) it.next();
						if (data[0] != null)
							itemList.get(i).setForecastBudget(
									Double.parseDouble(data[0].toString()));
						if (data[1] != null)
							itemList.get(i).setAdviceBudget(
									Double.parseDouble(data[1].toString()));
						if (data[2] != null)
							itemList.get(i).setBudgetAdd(
									Double.parseDouble(data[2].toString()));
						if (data[3] != null)
							itemList.get(i).setEnsureBudget(
									Double.parseDouble(data[3].toString()));
						if (data[4] != null)
							itemList.get(i).setFactHappen(
									Double.parseDouble(data[4].toString()));
						if (data[5] != null)
							itemList.get(i).setFinanceHappen(
									Double.parseDouble(data[5].toString()));
						if (data[6] != null)
							itemList.get(i).setBudgetTime(data[6].toString());
					}
				}
			}
		}
		pg.setList(itemList);
		pg.setTotalCount(Long.parseLong(String.valueOf(itemList.size())));
		return pg;
	}

	@SuppressWarnings("unchecked")
	public List<CbmJBudgetItem> findBudgetByMakeId(Long makeId) {
		String sqlString = "select * from cbm_j_budget_item a\n"
				+ " where a.budget_make_id=" + makeId;
		List<CbmJBudgetItem> list = bll.queryByNativeSQL(sqlString,
				CbmJBudgetItem.class);
		return list;

	}
}