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
import power.ejb.hr.LogUtil;
import power.ejb.manage.budget.form.BusinessItemForm;

/**
 * Facade for entity CbmCBusinessItem.
 * 
 * @see power.ejb.manage.budget.CbmCBusinessItem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCBusinessItemFacade implements CbmCBusinessItemFacadeRemote {
	// property constants
	public static final String ITEM_NAME = "itemName";
	public static final String UNIT_ID = "unitId";
	public static final String ITEM_ID1 = "itemId1";
	public static final String ITEM_ID2 = "itemId2";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(List<CbmCBusinessItem> addList) throws CodeRepeatException {
		if (addList != null && addList.size() > 0) {
			Long businessItemId = bll.getMaxId("CBM_C_BUSINESS_ITEM",
					"BUSINESS_ITEM_ID");
			for (int i = 0; i < addList.size(); i++) {
				CbmCBusinessItem entity = addList.get(i);
				entity.setBusinessItemId(businessItemId + i);
				this.save(entity);
			}
		}
	}

	public void save(CbmCBusinessItem entity) throws CodeRepeatException {
		LogUtil.log("saving CbmCBusinessItem instance", Level.INFO, null);
		try {

			if (this.checkItemId(entity.getItemId1()) == 1
					&& this.checkItemId(entity.getItemId2()) == 1
					&& this.checkItemName(entity.getItemName(), entity
							.getBusinessItemId()) == 0) {
				entityManager.persist(entity);
			} else {
				throw new CodeRepeatException("在预算部门指标维护，该指标被多个部门选取！");
			}
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(CbmCBusinessItem entity) {
		LogUtil.log("deleting CbmCBusinessItem instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmCBusinessItem.class, entity
					.getBusinessItemId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 批量删除录入报表数据
	 * 
	 * @param ids
	 * @return true 删除成功 false 删除不成功
	 */
	public boolean deleteMuti(String ids) {
		try {
			String[] temp1 = ids.split(",");

			for (String i : temp1) {
				CbmCBusinessItem entity = new CbmCBusinessItem();
				entity = this.findById(Long.parseLong(i));
				entity.setIsUse("N");
				try {
					this.update(entity);
				} catch (CodeRepeatException e) {
					e.printStackTrace();
				}
			}

			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public void update(List<CbmCBusinessItem> list) throws CodeRepeatException {

		for (int i = 0; i < list.size(); i++) {
			CbmCBusinessItem entity = list.get(i);
			this.update(entity);
		}
	}

	public CbmCBusinessItem update(CbmCBusinessItem entity)
			throws CodeRepeatException {
		LogUtil.log("updating CbmCBusinessItem instance", Level.INFO, null);
		try {
			CbmCBusinessItem result = null;
			if (this.checkItemId(entity.getItemId1()) == 1
					&& this.checkItemId(entity.getItemId2()) == 1
					&& this.checkItemName(entity.getItemName(), entity
							.getBusinessItemId()) == 0) {
				result = entityManager.merge(entity);
			} else {
				throw new CodeRepeatException("在预算部门指标维护，该指标被多个部门选取！");
			}
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmCBusinessItem findById(Long id) {
		LogUtil.log("finding CbmCBusinessItem instance with id: " + id,
				Level.INFO, null);
		try {
			CbmCBusinessItem instance = entityManager.find(
					CbmCBusinessItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CbmCBusinessItem> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding CbmCBusinessItem instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from CbmCBusinessItem model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<CbmCBusinessItem> findByItemName(Object itemName) {
		return findByProperty(ITEM_NAME, itemName);
	}

	public List<CbmCBusinessItem> findByUnitId(Object unitId) {
		return findByProperty(UNIT_ID, unitId);
	}

	public List<CbmCBusinessItem> findByItemId1(Object itemId1) {
		return findByProperty(ITEM_ID1, itemId1);
	}

	public List<CbmCBusinessItem> findByItemId2(Object itemId2) {
		return findByProperty(ITEM_ID2, itemId2);
	}

	public List<CbmCBusinessItem> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<CbmCBusinessItem> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, int... rowStartIdxAndCount) {
		LogUtil.log("finding all CbmCBusinessItem instances", Level.INFO, null);
		try {
			PageObject object = new PageObject();
			String sqlCount = "select count(*)\n"
					+ "  from CBM_C_BUSINESS_ITEM a\n"
					+ " where a.is_use = 'Y'\n"
					+ "   and a.enterprise_code = '" + enterpriseCode + "'";
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			object.setTotalCount(totalCount);
			if (totalCount > 0) {
				String sql = "select a.business_item_id,\n"
						+ "       a.item_name,\n" + "       a.unit_id,\n"
						+ "       a.item_id1,\n"
						+ "       GETCBMCITEMNAME(a.item_id1) as itemName1,\n"
						+ "       a.item_id2,\n"
						+ "       GETCBMCITEMNAME(a.item_id2) as itemName2\n"
						+ "  from CBM_C_BUSINESS_ITEM a\n"
						+ " where a.is_use = 'Y'\n"
						+ "   and a.enterprise_code = '" + enterpriseCode + "'";
				List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
				Iterator it = list.iterator();
				List<BusinessItemForm> arrList = new ArrayList<BusinessItemForm>();
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					BusinessItemForm model = new BusinessItemForm();
					if (data[0] != null)
						model.setBusinessItemId(Long.parseLong(data[0]
								.toString()));
					if (data[1] != null)
						model.setItemName(data[1].toString());
					if (data[2] != null)
						model.setUnitId(Long.parseLong(data[2].toString()));
					if (data[3] != null)
						model.setItemId1(Long.parseLong(data[3].toString()));
					if (data[4] != null)
						model.setItemName1(data[4].toString());
					if (data[5] != null)
						model.setItemId2(Long.parseLong(data[5].toString()));
					if (data[6] != null)
						model.setItemName2(data[6].toString());
					arrList.add(model);
				}
				object.setList(arrList);
			}
			return object;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	private Long checkItemId(Long itemId) {
		Long count = 0l;
		if (itemId == null || "".equals(itemId)) {
			count = 1l;
		} else {
			String sql = "select count(*) from CBM_C_CENTER_ITEM a where a.is_use = 'Y' and a.item_id = '"
					+ itemId + "'";
			count = Long.parseLong(bll.getSingal(sql).toString());
		}
		return count;
	}

	@SuppressWarnings("unused")
	private Long checkItemName(String itemName, Long businessItemId) {
		String sql = "";
		if (businessItemId == null || "".equals(businessItemId)) {
			sql = "select count(*) from CBM_C_BUSINESS_ITEM a where a.is_use = 'Y' and a.ITEM_NAME = '"
					+ itemName + "'";
		} else {
			sql = "select count(*) from CBM_C_BUSINESS_ITEM a where a.is_use = 'Y' and a.ITEM_NAME = '"
					+ itemName
					+ "' and a.BUSINESS_ITEM_ID <> '"
					+ businessItemId + "'";
		}
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		return count;
	}

	@SuppressWarnings( { "unchecked", "unchecked" })
	public List<BusinessItemForm> getBusinessItemReportPrint(String dateTime) {
		String sql = "select t.item_name,\n"
				+ "       getunitname(t.unit_id),\n"
				+ "       (select a.ADVICE_BUDGET\n"
				+ "          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n"
				+ "         where a.item_id = t.item_id1\n"
				+ "           and a.budget_make_id = c.budget_make_id\n"
				+ "           and c.budget_time = '" + dateTime
				+ "') as ADVICE_BUDGET,\n" + "       (select a.ADVICE_BUDGET\n"
				+ "          from CBM_J_BUDGET_ITEM a, CBM_J_BUDGET_MAKE c\n"
				+ "         where a.item_id = t.item_id2\n"
				+ "           and a.budget_make_id = c.budget_make_id\n"
				+ "           and c.budget_time = '" + dateTime
				+ "') as AVGADVICE_BUDGET\n" + "  from CBM_C_BUSINESS_ITEM t\n"
				+ " where t.is_use = 'Y'\n"
				+ "   and t.enterprise_code = 'hfdc'";
		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		List<BusinessItemForm> arrList = new ArrayList<BusinessItemForm>();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			BusinessItemForm model = new BusinessItemForm();
			if (data[0] != null)
				model.setItemName(data[0].toString());
			if (data[1] != null)
				model.setUnitName(data[1].toString());
			if (data[2] != null)
				model.setAdviceBudget(data[2].toString());
			if (data[3] != null)
				model.setAvgAdviceBudget(data[3].toString());
			arrList.add(model);
		}
		return arrList;
	}
}