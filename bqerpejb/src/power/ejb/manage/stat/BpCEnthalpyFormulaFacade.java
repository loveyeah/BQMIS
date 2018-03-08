package power.ejb.manage.stat;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.stat.form.BpCEnthalpyFormulaForm;

/**
 * Facade for entity BpCEnthalpyFormula.
 * 
 * @see power.ejb.manage.stat.BpCEnthalpyFormula
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCEnthalpyFormulaFacade implements BpCEnthalpyFormulaFacadeRemote {
	// property constants
	public static final String ENTHALPY_TYPE = "enthalpyType";
	public static final String YL_ZBBM = "ylZbbm";
	public static final String WD_ZBBM = "wdZbbm";
	public static final String COMPUTE_TYPE = "computeType";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved BpCEnthalpyFormula
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            BpCEnthalpyFormula entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCEnthalpyFormula entity) {
		LogUtil.log("saving BpCEnthalpyFormula instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent BpCEnthalpyFormula entity.
	 * 
	 * @param entity
	 *            BpCEnthalpyFormula entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpCEnthalpyFormula entity) {
		LogUtil.log("deleting BpCEnthalpyFormula instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpCEnthalpyFormula.class,
					entity.getItemCode());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpCEnthalpyFormula entity and return it or a
	 * copy of it to the sender. A copy of the BpCEnthalpyFormula entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCEnthalpyFormula entity to update
	 * @return BpCEnthalpyFormula the persisted BpCEnthalpyFormula entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpCEnthalpyFormula update(BpCEnthalpyFormula entity) {
		LogUtil.log("updating BpCEnthalpyFormula instance", Level.INFO, null);
		try {
			BpCEnthalpyFormula result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCEnthalpyFormula findById(String id) {
		LogUtil.log("finding BpCEnthalpyFormula instance with id: " + id,
				Level.INFO, null);
		try {
			BpCEnthalpyFormula instance = entityManager.find(
					BpCEnthalpyFormula.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpCEnthalpyFormula entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCEnthalpyFormula property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCEnthalpyFormula> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpCEnthalpyFormula> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpCEnthalpyFormula instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpCEnthalpyFormula model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCEnthalpyFormulaForm getBpCEnthalpyFormula(String id,
			String enterpriseCode) {

		String sql = "	SELECT t.*," + " (SELECT s.item_name"
				+ " FROM bp_c_stat_item   s,bp_c_enthalpy_formula t"
				+ "  WHERE s.item_code = t.yl_zbbm" + " and t.item_code = '"
				+ id + "') ," + "  (SELECT s.item_name"
				+ "   FROM bp_c_stat_item s, bp_c_enthalpy_formula t"
				+ "  WHERE s.item_code = t.wd_zbbm" + " and t.item_code = '"
				+ id + "')" + " from bp_c_enthalpy_formula t"
				+ " WHERE t.item_code = '" + id + "'"
				+ " and t.enterprise_code='" + enterpriseCode + "'";
		List list = bll.queryByNativeSQL(sql);

		BpCEnthalpyFormulaForm baseInfo = new BpCEnthalpyFormulaForm();
		if (list.size() == 0) {
			return baseInfo;
		}
		Object[] o = (Object[]) list.get(0);
		if (o[0] != null) {
			baseInfo.setItemCode(o[0].toString());
		}

		if (o[2] != null) {
			baseInfo.setYlZbbm(o[2].toString());
		}

		if (o[3] != null) {
			baseInfo.setWdZbbm(o[3].toString());
		}
		if (o[6] != null) {
			baseInfo.setYlZbbmName(o[6].toString());
		}
		if (o[7] != null) {
			baseInfo.setWdZbbmName(o[7].toString());
		}
		return baseInfo;

	};

	/**
	 * Find all BpCEnthalpyFormula entities.
	 * 
	 * @return List<BpCEnthalpyFormula> all BpCEnthalpyFormula entities
	 */
	@SuppressWarnings("unchecked")
	public List<BpCEnthalpyFormula> findAll() {
		LogUtil.log("finding all BpCEnthalpyFormula instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from BpCEnthalpyFormula model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}