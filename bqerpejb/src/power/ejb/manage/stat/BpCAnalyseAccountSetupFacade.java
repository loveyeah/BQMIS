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

/**
 * Facade for entity BpCAnalyseAccountSetup.
 * 
 * @see power.ejb.manage.stat.BpCAnalyseAccountSetup
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCAnalyseAccountSetupFacade implements
		BpCAnalyseAccountSetupFacadeRemote {
	// property constants
	public static final String IF_COLLECT = "ifCollect";
	public static final String TIME_TYPE = "timeType";
	public static final String IF_AUTO_SETUP = "ifAutoSetup";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * Perform an initial save of a previously unsaved BpCAnalyseAccountSetup
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            BpCAnalyseAccountSetup entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCAnalyseAccountSetup entity) {
		LogUtil.log("saving BpCAnalyseAccountSetup instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent BpCAnalyseAccountSetup entity.
	 * 
	 * @param entity
	 *            BpCAnalyseAccountSetup entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */

	public Long ifUpdate(String accountCode, String enterpriseCode) {
		Long count;
		String sql = "select count(*) from bp_c_analyse_account_setup t"
				+ "  where t.account_code='" + accountCode + "'"
				+ " and t.enterprise_code='" + enterpriseCode + "'";
		count = Long.valueOf(dll.getSingal(sql).toString());

		return count;
	}

	public boolean deleteAccountSetup(Long accountCode, String enterpriseCode) {
		try {
			String sql = "delete from bp_c_analyse_account_setup t"
					+ " where t.account_code=" + accountCode + ""
					+ " and t.enterprise_code='" + enterpriseCode + "'";
			dll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException e) {
			throw e;
		}

	}

	/**
	 * 变更时间类型时清空原时间类型数据
	 * 
	 * @param entity
	 * 
	 */
	public void delete(String accountCode) {
		LogUtil.log("deleting BpCAnalyseAccountSetup instance", Level.INFO,
				null);
		try {
			String sql = "delete from bp_c_analyse_account_setup t where t.account_code= '"
					+ accountCode + "'";
			dll.exeNativeSQL(sql);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpCAnalyseAccountSetup entity and return it or
	 * a copy of it to the sender. A copy of the BpCAnalyseAccountSetup entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCAnalyseAccountSetup entity to update
	 * @return BpCAnalyseAccountSetup the persisted BpCAnalyseAccountSetup
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpCAnalyseAccountSetup update(BpCAnalyseAccountSetup entity) {
		LogUtil.log("updating BpCAnalyseAccountSetup instance", Level.INFO,
				null);
		try {
			BpCAnalyseAccountSetup result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCAnalyseAccountSetup findById(BpCAnalyseAccountSetupId id) {
		LogUtil.log("finding BpCAnalyseAccountSetup instance with id: " + id,
				Level.INFO, null);
		try {
			BpCAnalyseAccountSetup instance = entityManager.find(
					BpCAnalyseAccountSetup.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpCAnalyseAccountSetup entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCAnalyseAccountSetup property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCAnalyseAccountSetup> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpCAnalyseAccountSetup> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpCAnalyseAccountSetup instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpCAnalyseAccountSetup model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<BpCAnalyseAccountSetup> findByIfCollect(Object ifCollect) {
		return findByProperty(IF_COLLECT, ifCollect);
	}

	public List<BpCAnalyseAccountSetup> findByTimeType(Object timeType) {
		return findByProperty(TIME_TYPE, timeType);
	}

	public List<BpCAnalyseAccountSetup> findByIfAutoSetup(Object ifAutoSetup) {
		return findByProperty(IF_AUTO_SETUP, ifAutoSetup);
	}

	public List<BpCAnalyseAccountSetup> findByEnterpriseCode(
			Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all BpCAnalyseAccountSetup entities.
	 * 
	 * @return List<BpCAnalyseAccountSetup> all BpCAnalyseAccountSetup entities
	 */
	@SuppressWarnings("unchecked")
	public List<BpCAnalyseAccountSetup> findAll() {
		LogUtil.log("finding all BpCAnalyseAccountSetup instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from BpCAnalyseAccountSetup model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}