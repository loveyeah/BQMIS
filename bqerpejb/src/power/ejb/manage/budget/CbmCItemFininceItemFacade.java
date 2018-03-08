package power.ejb.manage.budget;

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
 * Facade for entity CbmCItemFininceItem.
 * 
 * @see power.ejb.manage.budget.CbmCItemFininceItem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCItemFininceItemFacade implements CbmCItemFininceItemFacadeRemote {
	public static final String ITEM_ID = "itemId";
	public static final String FINANCE_ID = "financeId";
	public static final String DEBIT_CREDIT = "debitCredit";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved CbmCItemFininceItem
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            CbmCItemFininceItem entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(CbmCItemFininceItem entity) {
		try {
			entity.setRelationId(bll.getMaxId("CBM_C_ITEM_FININCE_ITEM",
					"RELATION_ID"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent CbmCItemFininceItem entity.
	 * 
	 * @param entity
	 *            CbmCItemFininceItem entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(CbmCItemFininceItem entity) {
		try {
			// entity = entityManager.getReference(CbmCItemFininceItem.class,
			// entity.getRelationId());
			// entityManager.remove(entity);
			entity.setIsUse("N");
			this.update(entity);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * @param entity
	 *            CbmCItemFininceItem entity to update
	 */
	public CbmCItemFininceItem update(CbmCItemFininceItem entity) {
		try {
			CbmCItemFininceItem result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmCItemFininceItem findById(Long id) {
		try {
			CbmCItemFininceItem instance = entityManager.find(
					CbmCItemFininceItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all CbmCItemFininceItem entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CbmCItemFininceItem property to query
	 * @param value
	 *            the property value to match
	 * @return List<CbmCItemFininceItem> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<CbmCItemFininceItem> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding CbmCItemFininceItem instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from CbmCItemFininceItem model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<CbmCItemFininceItem> findByItemId(Object itemId) {
		return findByProperty(ITEM_ID, itemId);
	}

	public List<CbmCItemFininceItem> findByFinanceId(Object financeId) {
		return findByProperty(FINANCE_ID, financeId);
	}

	public List<CbmCItemFininceItem> findByDebitCredit(Object debitCredit) {
		return findByProperty(DEBIT_CREDIT, debitCredit);
	}

	public List<CbmCItemFininceItem> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<CbmCItemFininceItem> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all CbmCItemFininceItem entities.
	 * 
	 * @return List<CbmCItemFininceItem> all CbmCItemFininceItem entities
	 */
	@SuppressWarnings("unchecked")
	public List<CbmCItemFininceItem> findAll() {
		LogUtil.log("finding all CbmCItemFininceItem instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from CbmCItemFininceItem model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public CbmCItemFininceItem findByItemId(Long itemId) {
		String sql = "select t.relation_id from CBM_C_ITEM_FININCE_ITEM t"
				+ " where t.item_id = '" + itemId + "'" + " and t.is_use = 'Y'";
		Long id = null;
		if (bll.getSingal(sql) != null) {
			id = Long.parseLong(bll.getSingal(sql).toString());
		}
		if (id != null) {
			return (this.findById(id));
		} else {
			return null;
		}

	}
}