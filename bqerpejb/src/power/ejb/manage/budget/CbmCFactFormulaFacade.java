package power.ejb.manage.budget;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.budget.form.CbmCFactFormulaForm;

/**
 * Facade for entity CbmCFactFormula.
 * 
 * @see power.ejb.manage.budget.CbmCFactFormula
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCFactFormulaFacade implements CbmCFactFormulaFacadeRemote {
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

	/**
	 * Perform an initial save of a previously unsaved CbmCFactFormula entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            CbmCFactFormula entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(CbmCFactFormula entity) {
		LogUtil.log("saving CbmCFactFormula instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/** 保存* */
	public void save(List<CbmCFactFormula> addList) {
		Long id = bll.getMaxId("cbm_c_fact_formula", "fact_formula_id");
		if (addList != null && addList.size() > 0) {
			for (CbmCFactFormula entity : addList) {
				entity.setFactFormulaId(id++);
				this.save(entity);
			}
		}
	}

	/** 删除* */
	public boolean delete(String itemId) {
		try {
			String sql = "delete from cbm_c_fact_formula where item_id = ?";
			bll.exeNativeSQL(sql, new Object[] { itemId });
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/** 公式Id查找公式* */
	@SuppressWarnings("unchecked")
	public List<CbmCFactFormulaForm> findAll(String enterpriseCode,
			String itemId) {
		try {
			String sql = "select t.fact_formula_id,\n"
					+ "       t.formula_no,\n"
					+ "       t.formula_content,\n"
					//modified by kzhang 20100831
					//+ "       nvl(getstatitemname(t.formula_content), t.formula_content),\n"
					+ "       nvl(a.item_name, t.formula_content),\n"
					+ "       t.fornula_type\n"
					+ "  from cbm_c_fact_formula t,cbm_c_item a\n"
					+ " where t.formula_content=a.item_code(+) and t.item_id = ?\n" + "   and t.enterprise_code = ? " +
							"order by t.fact_formula_id";
			List<Object[]> list = bll.queryByNativeSQL(sql, new Object[] {
					itemId, enterpriseCode });
			if (list != null) {
				List<CbmCFactFormulaForm> result = new ArrayList<CbmCFactFormulaForm>();
				for (Object[] o : list) {
					CbmCFactFormulaForm m = new CbmCFactFormulaForm();
					m.setItemId(itemId);
					if (o[0] != null) {
						m.setFactFormulaId(o[0].toString());
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

	public CbmCFactFormula update(CbmCFactFormula entity) {
		LogUtil.log("updating CbmCFactFormula instance", Level.INFO, null);
		try {
			CbmCFactFormula result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmCFactFormula findById(Long id) {
		LogUtil.log("finding CbmCFactFormula instance with id: " + id,
				Level.INFO, null);
		try {
			CbmCFactFormula instance = entityManager.find(
					CbmCFactFormula.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/* 取公式 */
	@SuppressWarnings("unchecked")
	public String[] getFactFormula(Long id) {
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