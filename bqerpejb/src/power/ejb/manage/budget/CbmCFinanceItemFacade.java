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

import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;

/**
 * Facade for entity CbmCFinanceItem.
 * 
 * @see power.ejb.manage.budget.CbmCFinanceItem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCFinanceItemFacade implements CbmCFinanceItemFacadeRemote {
	// property constants
	public static final String FINANCE_ITEM = "financeItem";
	public static final String FINANCE_NAME = "financeName";
	public static final String UPPER_ITEM = "upperItem";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved CbmCFinanceItem entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            CbmCFinanceItem entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public CbmCFinanceItem save(CbmCFinanceItem entity) {
		LogUtil.log("saving CbmCFinanceItem instance", Level.INFO, null);
		try {
			String sql = "select count(*) from CBM_C_FINANCE_ITEM a \n"
					+ "where a.is_use='Y' \n" + "and a.enterprise_code='"
					+ entity.getEnterpriseCode() + "' \n"
					+ "and a.finance_item='" + entity.getFinanceItem() + "' \n";
			Long count = Long.parseLong(bll.getSingal(sql).toString());
			if (count > 0) {
				return null;
			}
			entity.setFinanceId(bll
					.getMaxId("cbm_c_finance_item", "finance_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent CbmCFinanceItem entity.
	 * 
	 * @param entity
	 *            CbmCFinanceItem entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(CbmCFinanceItem entity) {
		LogUtil.log("deleting CbmCFinanceItem instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmCFinanceItem.class, entity
					.getFinanceId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved CbmCFinanceItem entity and return it or a copy
	 * of it to the sender. A copy of the CbmCFinanceItem entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            CbmCFinanceItem entity to update
	 * @return CbmCFinanceItem the persisted CbmCFinanceItem entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public CbmCFinanceItem update(CbmCFinanceItem entity) {
		LogUtil.log("updating CbmCFinanceItem instance", Level.INFO, null);
		try {
			String sql = "select count(*) from CBM_C_FINANCE_ITEM a \n"
					+ "where a.is_use='Y' \n" + "and a.enterprise_code='"
					+ entity.getEnterpriseCode() + "' \n"
					+ "and a.finance_item='" + entity.getFinanceItem() + "' \n"
					+ "and a.finance_id !=" + entity.getFinanceId();
			Long count = Long.parseLong(bll.getSingal(sql).toString());
			if (count > 0) {
				return null;
			}
			CbmCFinanceItem result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmCFinanceItem findById(Long id) {
		LogUtil.log("finding CbmCFinanceItem instance with id: " + id,
				Level.INFO, null);
		try {
			CbmCFinanceItem instance = entityManager.find(
					CbmCFinanceItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all CbmCFinanceItem entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CbmCFinanceItem property to query
	 * @param value
	 *            the property value to match
	 * @return List<CbmCFinanceItem> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<CbmCFinanceItem> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding CbmCFinanceItem instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from CbmCFinanceItem model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<CbmCFinanceItem> findByFinanceName(Object financeName) {
		return findByProperty(FINANCE_NAME, financeName);
	}

	public List<CbmCFinanceItem> findByUpperItem(Object upperItem) {
		return findByProperty(UPPER_ITEM, upperItem);
	}

	public List<CbmCFinanceItem> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<CbmCFinanceItem> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all CbmCFinanceItem entities.
	 * 
	 * @return List<CbmCFinanceItem> all CbmCFinanceItem entities
	 */
	@SuppressWarnings("unchecked")
	public List<CbmCFinanceItem> findAll() {
		LogUtil.log("finding all CbmCFinanceItem instances", Level.INFO, null);
		try {
			final String queryString = "select model from CbmCFinanceItem model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<TreeNode> findFinanceItemTreeList(String node,
			String EnterpriseCode) {
		List<TreeNode> res = null;
		try {
			String sql = "select t.*,connect_by_isleaf\n"

			+ "  from cbm_c_finance_item t\n" + "where level = 1\n"
					+ " start with t.upper_item = ?\n"
					+ "connect by prior t.finance_item = t.upper_item\n";
			// + " order by t.display_no";
			List<Object[]> list = bll.queryByNativeSQL(sql,
					new Object[] { node });
			if (list != null && list.size() > 0) {
				res = new ArrayList<TreeNode>();
				for (Object[] o : list) {
					TreeNode n = new TreeNode();

					n.setId(o[0].toString());
					if (o[1] != null)
						n.setCode(o[1].toString());
					if (o[2] != null)
						n.setText(o[2].toString());
					if (o[6] != null)
						n.setLeaf((o[6].toString().equals("1")) ? true : false);
					res.add(n);
				}
			}
			return res;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CbmCFinanceItem> findByItemId(Long id) {
		try {
			List list = new ArrayList();
			String sql = "select a.finance_id,a.finance_name,a.finance_item,a.upper_item "
					+ "from cbm_c_finance_item a\n"
					+ "where a.finance_id ='"
					+ id;
			list = bll.queryByNativeSQL(sql);
			List<CbmCFinanceItem> arraylist = new ArrayList<CbmCFinanceItem>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				CbmCFinanceItem model = new CbmCFinanceItem();
				if (data[0] != null)
					model.setFinanceId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setFinanceName(data[1].toString());
				if (data[2] != null)
					model.setFinanceItem(data[2].toString());
				if (data[3] != null)
					model.setUpperItem(data[3].toString());
			}
			return arraylist;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmCFinanceItem findByFinanceId(Long id) {
		try {
			CbmCFinanceItem model = entityManager.find(CbmCFinanceItem.class,
					id);
			return model;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
}