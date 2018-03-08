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
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.budget.form.CbmItemForm;

/**
 * Facade for entity CbmCItem.
 * 
 * @see power.ejb.manage.budget.CbmCItem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCItemFacade implements CbmCItemFacadeRemote {
	// property constants
	public static final String ITEM_CODE = "itemCode";
	public static final String ITEM_NAME = "itemName";
	public static final String ITEM_TYPE = "itemType";
	public static final String BELONG_DEPARTMENT = "belongDepartment";
	public static final String DUTY_DEPCODE = "dutyDepcode";
	public static final String IF_DISPART = "ifDispart";
	public static final String TIME_TYPE = "timeType";
	public static final String UNIT_CODE = "unitCode";
	public static final String COME_FROM = "comeFrom";
	public static final String FACT_FROM = "factFrom";
	public static final String FORMULA_TYPE = "formulaType";
	public static final String DATA_ATTRIBUTE = "dataAttribute";
	public static final String FORECAST_TYPE = "forecastType";
	public static final String IF_TOTAL = "ifTotal";
	public static final String COMPUTE_METHOD = "computeMethod";
	public static final String ACCOUNT_ORDER = "accountOrder";
	public static final String FACT_ORDER = "factOrder";
	public static final String RETRIEVE_CODE = "retrieveCode";
	public static final String ITEM_EXPLAIN = "itemExplain";
	public static final String FACT_EXPLAIN = "factExplain";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * @param entity
	 *            CbmCItem entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public CbmCItem save(CbmCItem entity) {
		try {
			entity.setItemId(bll.getMaxId("CBM_C_ITEM", "ITEM_ID"));
			entity.setIsUse("Y");
			entity.setAccountOrder(1l);
			entity.setFactOrder(1l);
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent CbmCItem entity.
	 */
	public void delete(CbmCItem entity) {
		try {
			// entity = entityManager.getReference(CbmCItem.class, entity
			// .getItemId());
			// entityManager.remove(entity);
			entity.setIsUse("N");
			this.update(entity);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * update a persistent CbmCItem entity.
	 */
	public CbmCItem update(CbmCItem entity) {
		try {
			CbmCItem result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmCItem findById(Long id) {
		try {
			CbmCItem instance = entityManager.find(CbmCItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CbmItemForm> findByItemId(String ItemxId) {
		List list = new ArrayList();
		String sql = "select a.*,b.zbbmtx_id,b.zbbmtx_code,b.zbbmtx_name,b.is_item ,\n"
				+ "(select d.finance_name from cbm_c_finance_item d "
				+ " where d.finance_id=( select c.finance_id from cbm_c_item_finince_item c where c.item_id = a.item_id and c.is_use='Y') and d.is_use='Y') financeName,\n"
				+ "(select c.finance_id from cbm_c_item_finince_item c where c.item_id = a.item_id and c.is_use='Y') financeId,\n"
				+ "(select c.debit_credit from cbm_c_item_finince_item c where c.item_id = a.item_id and c.is_use='Y') debitCredit, \n"
				+ "(select c.dept_name from hr_c_dept c where c.dept_id = a.CENTER_ID and c.is_use='Y') deptName \n"
				+ " from cbm_c_item a ,cbm_c_itemtx b\n"
				+ " where b.zbbmtx_id = '"
				+ ItemxId
				+ "'\n"
				+ " and a.item_id = b.item_id\n" +
				// " and c.item_id = a.item_id +\n" +
				// " and d.finance_id = c.finance_id +\n" +
				// " and d.is_use = 'Y'\n" +
				// " and c.is_use = 'Y'\n" +
				" and a.is_use = 'Y'\n" + " and b.is_use = 'Y'";

		list = bll.queryByNativeSQL(sql);
		List<CbmItemForm> arraylist = new ArrayList<CbmItemForm>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			CbmCItem model = new CbmCItem();
			CbmItemForm itemForm = new CbmItemForm();
			if (data[0] != null)
				model.setItemId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				model.setItemCode(data[1].toString());
			if (data[3] != null)
				model.setItemType(data[3].toString());
			if (data[4] != null)
				model.setBelongDepartment(data[4].toString());
			if (data[5] != null)
				model.setDutyDepcode(data[5].toString());
			if (data[6] != null)
				model.setIfDispart(data[6].toString());
			if (data[7] != null)
				model.setTimeType(data[7].toString());
			if (data[8] != null)
				model.setUnitCode(Long.parseLong(data[8].toString()));
			if (data[9] != null)
				model.setComeFrom(data[9].toString());
			if (data[10] != null)
				model.setFactFrom(data[10].toString());
			if (data[11] != null)
				model.setFormulaType(data[11].toString());
			if (data[12] != null)
				model.setDataAttribute(data[12].toString());
			if (data[13] != null)
				model.setForecastType(data[13].toString());
			if (data[14] != null)
				model.setIfTotal(data[14].toString());
			if (data[15] != null)
				model.setComputeMethod(data[15].toString());
			if (data[18] != null)
				model.setRetrieveCode(data[18].toString());
			if (data[19] != null)
				model.setItemExplain(data[19].toString());
			if (data[20] != null)
				model.setFactExplain(data[20].toString());
			if(data[23] != null){
				model.setFirstclassValue(Double.parseDouble(data[23].toString()));
			}
			if(data[24] != null){
				model.setCreateValue(Double.parseDouble(data[24].toString()));
			}
			if(data[25] != null){
				model.setCenterId(Long.parseLong(data[25].toString()));
			}
			if(data[26] != null){
				model.setOrderBy(Long.parseLong(data[26].toString()));
			}
			if (data[27] != null)
				itemForm.setZbbmtxId(Long.parseLong(data[27].toString()));
			if (data[28] != null)
				itemForm.setZbbmtxCode(data[28].toString());
			if (data[29] != null)
				itemForm.setZbbmtxName(data[29].toString());
			if (data[30] != null)
				itemForm.setIsItem(data[30].toString());
			if (data[31] != null)
				itemForm.setFinaceName(data[31].toString());
			if (data[32] != null)
				itemForm.setFinaceId(Long.parseLong(data[32].toString()));
			if (data[33] != null)
				itemForm.setDebitCredit(data[33].toString());
			if (data[34] != null)
				itemForm.setDeptName(data[34].toString());
			itemForm.setItem(model);
			arraylist.add(itemForm);
		}
		return arraylist;
	}

	public boolean findByCode(String itemCode) {
		String sql = "select count(1) from CBM_C_ITEM t "
				+ "where t.ITEM_CODE='" + itemCode + "'" + "and t.is_use = 'Y'";
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		if (count > 0)
			return true;
		else
			return false;
	}

	public Long getaccountOrder(Long id) {

		String sqlString = "select (max(a.account_order)+1) accountorder  from cbm_c_item a\n"
				+ " where a.item_code in (select t.formula_content from\n"
				+ " CBM_C_ITEM_FORMULA t where t.fornula_type = 1 and t.item_id='"
				+ id + "'and t.is_use = 'Y')";
		if (bll.getSingal(sqlString) != null) {
			return Long.parseLong(bll.getSingal(sqlString).toString());
		} else {
			return 1l;
		}
	}

	public Long getfactOrder(Long id) {
		String sqlString = "select (max(a.account_order)+1) accountorder  from cbm_c_item a\n"
				+ " where a.item_code in (select t.formula_content from\n"
				+ " CBM_C_FACT_FORMULA t where t.fornula_type = 1 and t.item_id='"
				+ id + "'and t.is_use = 'Y')";
		if (bll.getSingal(sqlString) != null) {
			return Long.parseLong(bll.getSingal(sqlString).toString());
		} else {
			return 1l;
		}
	}
}