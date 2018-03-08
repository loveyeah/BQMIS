package power.ejb.manage.budget;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.budget.form.CbmCItemFormulaForm;

/**
 * Facade for entity CbmCCostFormula.
 * 
 * @see power.ejb.manage.budget.CbmCCostFormula
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCCostFormulaFacade implements CbmCCostFormulaFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved CbmCCostFormula entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            CbmCCostFormula entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(CbmCCostFormula entity) {
		LogUtil.log("saving CbmCCostFormula instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/** 保存* */
	public void save(List<CbmCCostFormula> addList) {
		if (addList != null && addList.size() > 0) {
			Long id = bll.getMaxId("CBM_C_COST_FORMULA", "FORMULA_ID");
			for (CbmCCostFormula entity : addList) {
				entity.setFormulaId(id++);
				this.save(entity);
			}
		}
	}

	/** 删除* */
	public boolean delete(String itemId) {
		try {
			String sql = "delete from CBM_C_COST_FORMULA where item_id = ?";
			bll.exeNativeSQL(sql, new Object[] { itemId });
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}
	
	/**
	 * Delete a persistent CbmCCostFormula entity.
	 * 
	 * @param entity
	 *            CbmCCostFormula entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(CbmCCostFormula entity) {
		LogUtil.log("deleting CbmCCostFormula instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmCCostFormula.class, entity
					.getFormulaId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved CbmCCostFormula entity and return it or a copy
	 * of it to the sender. A copy of the CbmCCostFormula entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            CbmCCostFormula entity to update
	 * @return CbmCCostFormula the persisted CbmCCostFormula entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public CbmCCostFormula update(CbmCCostFormula entity) {
		LogUtil.log("updating CbmCCostFormula instance", Level.INFO, null);
		try {
			CbmCCostFormula result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmCCostFormula findById(Long id) {
		LogUtil.log("finding CbmCCostFormula instance with id: " + id,
				Level.INFO, null);
		try {
			CbmCCostFormula instance = entityManager.find(
					CbmCCostFormula.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all CbmCCostFormula entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CbmCCostFormula property to query
	 * @param value
	 *            the property value to match
	 * @return List<CbmCCostFormula> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<CbmCCostFormula> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding CbmCCostFormula instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from CbmCCostFormula model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all CbmCCostFormula entities.
	 * 
	 * @return List<CbmCCostFormula> all CbmCCostFormula entities
	 */
	@SuppressWarnings("unchecked")
	public List<CbmCCostFormula> findAll() {
		LogUtil.log("finding all CbmCCostFormula instances", Level.INFO, null);
		try {
			final String queryString = "select model from CbmCCostFormula model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}


	public PageObject findAllCostItem(String argFuzzy,
			int... rowStartIdxAndCount) {
		PageObject result = new PageObject();
		if ("".equals(argFuzzy)) {
			argFuzzy = "%";
		}
		String sql = "select t.*\n" + "  from cbm_c_cost_item t\n"
				+ " where (t.item_name like '%" + argFuzzy + "%'\n"
				+ "   or t.item_code like '%" + argFuzzy + "%')\n"
				 +	" and t.item_code is not null";
		String sqlCount = "select count(*)\n" + "  from cbm_c_cost_item t\n"
				+ " where (t.item_name like '%" + argFuzzy + "%'\n"
				+ "   or t.item_code like '%" + argFuzzy + "%')\n"
				+	" and t.item_code is not null";

		List<CbmCCostItem> list = bll.queryByNativeSQL(sql, CbmCCostItem.class,
				rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
		result.setList(list);
		result.setTotalCount(count);
		return result;
	}

	public String[] getBudgetFormula(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 公式Id查找公式* */
	@SuppressWarnings("unchecked")
	public List<CbmCItemFormulaForm> findAll(String enterpriseCode,
			String itemId) {
		try {
			String sql = "select t.formula_id,\n"
					+ "       t.formula_no,\n"
					+ "       t.formula_content,\n"
					+ "       nvl(getstatitemname(t.formula_content), t.formula_content),\n"
					+ "       t.fornula_type\n"
					+ "  from Cbm_c_Cost_Formula t\n"
					+ " where t.item_id = ?\n" + "   and t.enterprise_code = ?";
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

}