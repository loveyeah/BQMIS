package power.ejb.manage.budget;

import java.util.ArrayList;
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
import power.ejb.manage.budget.form.CbmCItemFormulaForm;

/**
 * Facade for entity CbmCItemFormula.
 * 
 * @see power.ejb.manage.budget.CbmCItemFormula
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCItemFormulaFacade implements CbmCItemFormulaFacadeRemote {
	// property constants
	public static final String ITEM_ID = "itemId";
	public static final String FORMULA_NO = "formulaNo";
	public static final String FORMULA_CONTENT = "formulaContent";
	public static final String FORNULA_TYPE = "fornulaType";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(CbmCItemFormula entity) {
		LogUtil.log("saving CbmCItemFormula instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/** 保存* */
	public void save(List<CbmCItemFormula> addList) {
		if (addList != null && addList.size() > 0) {
			Long id = bll.getMaxId("cbm_c_item_formula", "formula_id");
			for (CbmCItemFormula entity : addList) {
				entity.setFormulaId(id++);
				this.save(entity);
			}
		}
	}

	/** 删除* */
	public boolean delete(String itemId) {
		try {
			String sql = "delete from cbm_c_item_formula where item_id = ?";
			bll.exeNativeSQL(sql, new Object[] { itemId });
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public CbmCItemFormula update(CbmCItemFormula entity) {
		LogUtil.log("updating CbmCItemFormula instance", Level.INFO, null);
		try {
			CbmCItemFormula result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmCItemFormula findById(Long id) {
		LogUtil.log("finding CbmCItemFormula instance with id: " + id,
				Level.INFO, null);
		try {
			CbmCItemFormula instance = entityManager.find(
					CbmCItemFormula.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CbmCItemFormula> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding CbmCItemFormula instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from CbmCItemFormula model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/** 公式Id查找公式* */
	@SuppressWarnings("unchecked")
	public List<CbmCItemFormulaForm> findAll(String enterpriseCode,
			String itemId) {
		try {
			String sql = "select t.formula_id,\n"
					+ "       t.formula_no,\n"
					+ "       t.formula_content,\n"
					//modified by kzhang 20100831
					//+ "       nvl(getstatitemname(t.formula_content), t.formula_content),\n"
					+ "       nvl(a.item_name, t.formula_content),\n"
					+ "       t.fornula_type\n"
					+ "  from cbm_c_item_formula t" +
							",cbm_c_item a\n"
					+ " where t.formula_content=a.item_code(+) " +
							"and t.item_id = ?\n" + "   and t.enterprise_code = ? " +
									"order by t.formula_id";
			List<Object[]> list = bll.queryByNativeSQL(sql, new Object[] {
					itemId, enterpriseCode });
			if (list != null) {
				List<CbmCItemFormulaForm> result = new ArrayList<CbmCItemFormulaForm>();
				for (Object[] o : list) {
					CbmCItemFormulaForm m = new CbmCItemFormulaForm();
					m.setItemId(itemId);
					if (o[0] != null) {
						m.setFormulaId(o[0].toString());
					}
					if (o[1] != null) {
						m.setFormulaNo(o[1].toString());
					}
					if (o[2] != null) {
						m.setRowItemCode(o[2].toString());
					}
					if (o[3] != null) {
						m.setFormulaContent(o[3].toString());
					}
					if (o[4] != null) {
						m.setFornulaType(o[4].toString());
					}
					result.add(m);
				}
				return result;
			}
			return null;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/** 模糊查询指标* */
	@SuppressWarnings("unchecked")
	public PageObject findAllStatItem(String argFuzzy,
			int... rowStartIdxAndCount) {
		PageObject result = new PageObject();

		if ("".equals(argFuzzy)) {
			argFuzzy = "%";
		}

		String sql = "select t.*\n" + "  from CBM_C_ITEM t\n"
				+ " where (t.item_name like '%" + argFuzzy + "%'\n"
				+ "   or t.item_code like '%" + argFuzzy + "%')";
		String sqlCount = "select count(*)\n" + "  from CBM_C_ITEM t\n"
				+ " where (t.item_name like '%" + argFuzzy + "%'\n"
				+ "   or t.item_code like '%" + argFuzzy + "%')";

		List<CbmCItem> list = bll.queryByNativeSQL(sql, CbmCItem.class,
				rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());

		result.setList(list);
		result.setTotalCount(count);

		return result;
	}

	/* 取公式 */
	@SuppressWarnings("unchecked")
	public String[] getBudgetFormula(Long id) {
		String[] formula = null;
		String itemId = String.valueOf(id);
		String sql = "select formula_content from cbm_c_item_formula"
				+ "where item_id =" + itemId;
		List<String> list = bll.queryByNativeSQL(sql);
		for (int i = 0; i < list.size(); i++) {
			formula[i] = list.get(i);
		}
		return formula;
	}
}