package power.ejb.manage.plan;

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
import power.ejb.manage.plan.form.PlanItemFormula;

/**
 * Facade for entity BpCPlanFormula.
 * 
 * @see power.ejb.manage.plan.BpCPlanFormula
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCPlanFormulaFacade implements BpCPlanFormulaFacadeRemote {
	// property constants

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved BpCPlanFormula entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpCPlanFormula entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCPlanFormula entity) {
		LogUtil.log("saving BpCPlanFormula instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent BpCPlanFormula entity.
	 * 
	 * @param entity
	 *            BpCPlanFormula entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpCPlanFormula entity) {
		LogUtil.log("deleting BpCPlanFormula instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpCPlanFormula.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpCPlanFormula entity and return it or a copy
	 * of it to the sender. A copy of the BpCPlanFormula entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCPlanFormula entity to update
	 * @return BpCPlanFormula the persisted BpCPlanFormula entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpCPlanFormula update(BpCPlanFormula entity) {
		LogUtil.log("updating BpCPlanFormula instance", Level.INFO, null);
		try {
			BpCPlanFormula result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCPlanFormula findById(BpCPlanFormulaId id) {
		LogUtil.log("finding BpCPlanFormula instance with id: " + id,
				Level.INFO, null);
		try {
			BpCPlanFormula instance = entityManager.find(BpCPlanFormula.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpCPlanFormula entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCPlanFormula property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCPlanFormula> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpCPlanFormula> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpCPlanFormula instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpCPlanFormula model where model."
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
	 * 保存
	 */
	public void save(List<BpCPlanFormula> addList) {

		if (addList != null && addList.size() > 0) {
			for (BpCPlanFormula entity : addList) {
				this.save(entity);
			}
		}
	}

	public boolean delete(String itemCode) {
		try {
			String sql = "delete from BP_C_PLAN_FORMULA where item_code = ?";
			bll.exeNativeSQL(sql, new Object[] { itemCode });
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Find all BpCPlanFormula entities.
	 * 
	 * @return List<BpCPlanFormula> all BpCPlanFormula entities
	 */
	@SuppressWarnings("unchecked")
	public List<PlanItemFormula> findAll(String enterpriseCode, String itemCode) {
		try {
			String sql = "select t.formula_no,\n"
					+ "       t.formula_content,\n"
					+ "       nvl(getplanitemname(t.formula_content), t.formula_content),\n"
					+ "       t.fornula_type\n"
					+ "  from BP_C_PLAN_FORMULA t\n"
					+ " where t.item_code = ?\n"
					+ "   and t.enterprise_code = ?";
			List<Object[]> list = bll.queryByNativeSQL(sql, new Object[] {
					itemCode, enterpriseCode });
			if (list != null) {
				List<PlanItemFormula> result = new ArrayList<PlanItemFormula>();
				for (Object[] o : list) {
					PlanItemFormula m = new PlanItemFormula();
					m.setItemCode(itemCode);
					if (o[0] != null) {
						m.setFormulaNo(o[0].toString());
					}
					if (o[1] != null) {
						m.setRowItemCode(o[1].toString());
					}
					if (o[2] != null) {
						m.setFormulaContent(o[2].toString());
					}
					if (o[3] != null) {
						m.setFornulaType(o[3].toString());
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

	@SuppressWarnings("unchecked")
	public PageObject findAllPlanItem(String argFuzzy,
			int... rowStartIdxAndCount) {
		PageObject result = new PageObject();

		if ("".equals(argFuzzy)) {
			argFuzzy = "%";
		}

		String sql = "select t.*\n" + "  from bp_c_plan_item t\n"
				+ " where (t.item_name like '%" + argFuzzy + "%'\n"
				+ "   or t.item_code like '%" + argFuzzy + "%')"
				+ " and t.is_item='Y'";
		String sqlCount = "select count(*)\n" + "  from bp_c_plan_item t\n"
				+ " where (t.item_name like '%" + argFuzzy + "%'\n"
				+ "   or t.item_code like '%" + argFuzzy + "%')"
				+ "and t.is_item='Y'";

		List<BpCPlanItem> list = bll.queryByNativeSQL(sql, BpCPlanItem.class,
				rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());

		result.setList(list);
		result.setTotalCount(count);

		return result;
	}

}